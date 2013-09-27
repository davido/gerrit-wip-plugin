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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.extensions.restapi.ResourceConflictException;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.client.RevId;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.change.ChangeResource;
import com.google.gerrit.server.change.RevisionResource;
import com.google.gerrit.server.project.ChangeControl;
import com.google.gerrit.server.project.NoSuchChangeException;
import com.google.gerrit.server.project.ProjectControl;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.ResultSet;
import com.google.inject.Inject;
import com.google.inject.Provider;

@RequiresCapability(WorkInProgressCapability.WORK_IN_PROGRESS)
@CommandMetaData(name = "set", description = "Mark the change as WIP or Ready")
public class SetCommand extends SshCommand {
  private static final Logger log = LoggerFactory.getLogger(SetCommand.class);

  private final Set<PatchSet> patchSets = new HashSet<PatchSet>();

  @Argument(index = 0, required = true,
      multiValued = true,
      metaVar = "{COMMIT | CHANGE,PATCHSET}",
      usage = "list of commits or patch sets to review")
  void addPatchSetId(String token) {
    try {
      patchSets.add(parsePatchSet(token));
    } catch (UnloggedFailure e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    } catch (OrmException e) {
      throw new IllegalArgumentException("database error", e);
    }
  }

  @Option(name = "--project", aliases = "-p",
      usage = "project containing the specified patch set(s)")
  private ProjectControl projectControl;

  @Option(name = "--message", aliases = "-m",
      usage = "cover message on change(s)", metaVar = "MESSAGE")
  private String changeComment;

  @Option(name = "--wip", aliases = "-w",
      usage = "mark the specified change(s) as WIP")
  private boolean wipChange;

  @Option(name = "--readyChange", aliases = "-r",
      usage = "mark the specified change(s) as Ready for Review")
  private boolean readyChange;

  @Inject
  private ReviewDb db;

  @Inject
  private ChangeControl.Factory changeControlFactory;

  @Inject
  private Provider<WorkInProgressAction> wipProvider;

  @Inject
  private Provider<ReadyForReviewAction> readyProvider;

  @Override
  protected void run() throws UnloggedFailure {
    if (wipChange && readyChange) {
      throw error("wip and ready options are mutually exclusive");
    }
    if (!wipChange && !readyChange) {
      throw error("wip or ready option must be specified");
    }

    boolean ok = true;
    for (PatchSet patchSet : patchSets) {
      try {
        mark(patchSet);
      } catch (NoSuchChangeException e) {
        ok = false;
        writeError("no such change " + patchSet.getId().getParentKey().get());
      } catch (ResourceConflictException e) {
        writeError("error: " + e.getMessage() + "\n");
        ok = false;
      } catch (OrmException e) {
        ok = false;
        writeError("fatal: internal server error while approving "
            + patchSet.getId() + "\n");
        log.error("internal error while approving " + patchSet.getId(), e);
      }
    }

    if (!ok) {
      throw new UnloggedFailure(1, "one or more mark operations failed");
    }
  }

  private void mark(PatchSet patchSet) throws NoSuchChangeException,
      ResourceConflictException, OrmException {
    RevisionResource rsrc = new RevisionResource(
        new ChangeResource(changeControlFactory
            .controlFor(patchSet.getId().getParentKey())), patchSet);
    BaseAction.Input input = new BaseAction.Input();
    input.message = changeComment;
    if (wipChange) {
      wipProvider.get().apply(rsrc, input);
    } else {
      readyProvider.get().apply(rsrc, input);
    }
  }

  private PatchSet parsePatchSet(String patchIdentity)
      throws UnloggedFailure, OrmException {
    // By commit?
    //
    if (patchIdentity.matches("^([0-9a-fA-F]{4," + RevId.LEN + "})$")) {
      RevId id = new RevId(patchIdentity);
      ResultSet<PatchSet> patches;
      if (id.isComplete()) {
        patches = db.patchSets().byRevision(id);
      } else {
        patches = db.patchSets().byRevisionRange(id, id.max());
      }

      Set<PatchSet> matches = new HashSet<PatchSet>();
      for (PatchSet ps : patches) {
        Change change = db.changes().get(ps.getId().getParentKey());
        if (inProject(change)) {
          matches.add(ps);
        }
      }

      switch (matches.size()) {
        case 1:
          return matches.iterator().next();
        case 0:
          throw error("\"" + patchIdentity + "\" no such patch set");
        default:
          throw error("\"" + patchIdentity + "\" matches multiple patch sets");
      }
    }

    // By older style change,patchset?
    //
    if (patchIdentity.matches("^[1-9][0-9]*,[1-9][0-9]*$")) {
      PatchSet.Id patchSetId;
      try {
        patchSetId = PatchSet.Id.parse(patchIdentity);
      } catch (IllegalArgumentException e) {
        throw error("\"" + patchIdentity + "\" is not a valid patch set");
      }
      PatchSet patchSet = db.patchSets().get(patchSetId);
      if (patchSet == null) {
        throw error("\"" + patchIdentity + "\" no such patch set");
      }
      if (projectControl != null) {
        Change change = db.changes().get(patchSetId.getParentKey());
        if (!inProject(change)) {
          throw error("change " + change.getId() + " not in project "
              + projectControl.getProject().getName());
        }
      }
      return patchSet;
    }

    throw error("\"" + patchIdentity + "\" is not a valid patch set");
  }

  private boolean inProject(Change change) {
    if (projectControl == null) {
      // No --project option, so they want every project.
      return true;
    }
    return projectControl.getProject().getNameKey().equals(change.getProject());
  }

  private void writeError(String msg) {
    try {
      err.write(msg.getBytes(ENC));
    } catch (IOException e) {
    }
  }

  private static UnloggedFailure error(String msg) {
    return new UnloggedFailure(1, msg);
  }
}
