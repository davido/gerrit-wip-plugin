// Copyright (C) 2013 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.wip;

import java.util.Collections;

import com.google.common.base.Strings;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.extensions.restapi.ResourceConflictException;
import com.google.gerrit.extensions.restapi.RestModifyView;
import com.google.gerrit.extensions.webui.UiAction;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.Change.Status;
import com.google.gerrit.reviewdb.client.ChangeMessage;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.ApprovalsUtil;
import com.google.gerrit.server.ChangeUtil;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.change.RevisionResource;
import com.google.gerrit.server.project.ChangeControl;
import com.google.gwtorm.server.AtomicUpdate;
import com.google.gwtorm.server.OrmException;
import com.google.inject.Inject;
import com.google.inject.Provider;

@RequiresCapability(WorkInProgressCapability.WORK_IN_PROGRESS)
class ReadyForReviewAction implements UiAction<RevisionResource>,
    RestModifyView<RevisionResource, ReadyForReviewAction.Input> {
  static class Input {
    String message;
  }

  private final Provider<ReviewDb> dbProvider;

  @Inject
  ReadyForReviewAction(Provider<ReviewDb> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public String apply(RevisionResource rsrc, Input input)
      throws AuthException, ResourceConflictException, OrmException {
    ChangeControl control = rsrc.getControl();
    if (!control.getCurrentUser().isIdentifiedUser()) {
      throw new AuthException("not permitted to modify change status");
    }

    IdentifiedUser caller = (IdentifiedUser) control.getCurrentUser();
    Change change = rsrc.getChange();
    if (change.getStatus() != Status.WORKINPROGRESS) {
      throw new ResourceConflictException("change is " + status(change));
    }

    if (!change.currentPatchSetId().equals(rsrc.getPatchSet().getId())) {
      throw new ResourceConflictException("not current patch set");
    }

    ChangeMessage message;
    ReviewDb db = dbProvider.get();
    db.changes().beginTransaction(change.getId());
    try {
      change = db.changes().atomicUpdate(
        change.getId(),
        new AtomicUpdate<Change>() {
          @Override
          public Change update(Change change) {
            if (change.getStatus() == Status.WORKINPROGRESS) {
              change.setStatus(Status.NEW);
              ChangeUtil.updated(change);
              return change;
            }
            return null;
          }
        });

      if (change == null) {
        throw new ResourceConflictException("change is "
            + status(db.changes().get(rsrc.getChange().getId())));
      }

      message = newMessage(input, caller, change);
      db.changeMessages().insert(Collections.singleton(message));
      new ApprovalsUtil(db).syncChangeStatus(change);
      db.commit();
    } finally {
      db.rollback();
    }

    return String.format("Status changed to NEW");
  }

  @Override
  public Description getDescription(RevisionResource rsrc) {
    PatchSet.Id current = rsrc.getChange().currentPatchSetId();
    return new Description()
        .setLabel("Ready For Review")
        .setTitle("Set Ready For Review")
        .setVisible(rsrc.getChange().getStatus() == Status.WORKINPROGRESS
           && rsrc.getPatchSet().getId().equals(current));
  }

  private ChangeMessage newMessage(Input input, IdentifiedUser caller,
      Change change) throws OrmException {
    StringBuilder msg = new StringBuilder(
        "Change "
        + change.getId().get()
        + ": Ready For Review");
    if (!Strings.nullToEmpty(input.message).trim().isEmpty()) {
      msg.append("\n\n");
      msg.append(input.message.trim());
    }

    ChangeMessage message = new ChangeMessage(
        new ChangeMessage.Key(
            change.getId(),
            ChangeUtil.messageUUID(dbProvider.get())),
        caller.getAccountId(),
        change.getLastUpdatedOn(),
        change.currentPatchSetId());
    message.setMessage(msg.toString());
    return message;
  }

  private static String status(Change change) {
    return change != null
        ? change.getStatus().name().toLowerCase()
        : "deleted";
  }
}
