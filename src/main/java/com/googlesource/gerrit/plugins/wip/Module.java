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

import static com.google.gerrit.server.change.RevisionResource.REVISION_KIND;
import static com.googlesource.gerrit.plugins.wip.WorkInProgressCapability.WORK_IN_PROGRESS;

import com.google.gerrit.extensions.annotations.Exports;
import com.google.gerrit.extensions.config.CapabilityDefinition;
import com.google.gerrit.extensions.restapi.RestApiModule;
import com.google.inject.AbstractModule;

public class Module extends AbstractModule {

  @Override
  protected void configure() {
    bind(CapabilityDefinition.class)
        .annotatedWith(Exports.named(WORK_IN_PROGRESS))
        .to(WorkInProgressCapability.class);
    install(new RestApiModule() {
      @Override
      protected void configure() {
        post(REVISION_KIND, "set-wip")
            .to(WorkInProgressAction.class);
        post(REVISION_KIND, "set-ready")
            .to(ReadyForReviewAction.class);
      }
    });
  }
}
