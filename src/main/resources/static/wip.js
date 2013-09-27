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

Gerrit.install(function(self) {
    function onSetWip(c) {
      var t = c.textarea();
      var b = c.button('WIP', {onclick: function() {
        c.call(
          {message: t.value},
          function(r) {
            c.hide();
            c.refresh();
          });
      }});
      c.popup(c.div(
        t,
        c.br(),
        b));
      t.focus();
    }
    function onSetReady(c) {
        var t = c.textarea();
        var b = c.button('Ready', {onclick: function() {
          c.call(
            {message: t.value},
            function(r) {
              c.hide();
              c.refresh();
            });
        }});
        c.popup(c.div(
          t,
          c.br(),
          b));
        t.focus();
      }
      self.onAction('revision', 'set-ready', onSetReady);
      self.onAction('revision', 'set-wip', onSetWip);
  });
