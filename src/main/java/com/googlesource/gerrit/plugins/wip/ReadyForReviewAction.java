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

import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.extensions.restapi.ResourceConflictException;
import com.google.gerrit.extensions.restapi.Response;
import com.google.gerrit.extensions.restapi.RestModifyView;
import com.google.gerrit.extensions.webui.UiAction;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.Change.Status;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.change.RevisionResource;
import com.google.gwtorm.server.OrmException;
import com.google.inject.Inject;
import com.google.inject.Provider;

@RequiresCapability(WorkInProgressCapability.WORK_IN_PROGRESS)
class ReadyForReviewAction extends BaseAction implements
    UiAction<RevisionResource>,
    RestModifyView<RevisionResource, BaseAction.Input> {

  @Inject
  ReadyForReviewAction(Provider<ReviewDb> dbProvider,
      Provider<CurrentUser> userProvider) {
    super(dbProvider, userProvider);
  }

  @Override
  public Object apply(RevisionResource rsrc, Input input)
      throws ResourceConflictException, OrmException {
    Change change = rsrc.getChange();
    if (change.getStatus() != Status.WORKINPROGRESS) {
      throw new ResourceConflictException("change is " + status(change));
    }

    if (!change.currentPatchSetId().equals(rsrc.getPatchSet().getId())) {
      throw new ResourceConflictException("not current patch set");
    }

    changeStatus(change, input, Status.WORKINPROGRESS, Status.NEW);
    return Response.none();
  }

  @Override
  public Description getDescription(RevisionResource rsrc) {
    PatchSet.Id current = rsrc.getChange().currentPatchSetId();
    return new Description()
        .setLabel("Ready")
        .setTitle("Set Ready For Review")
        .setVisible(rsrc.getChange().getStatus() == Status.WORKINPROGRESS
           && rsrc.getPatchSet().getId().equals(current));
  }
}
