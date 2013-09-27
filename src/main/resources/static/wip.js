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
    function onWip(c) {
      var f = c.textarea();
      var b = c.button('WIP', {onclick: function(){
        c.call(
          {message: f.value},
          function(r) {
            c.hide();
            window.alert(r);
            c.refresh();
          });
      }});
      c.popup(c.div(
        f,
        c.br(),
        b));
      f.focus();
    }
    self.onAction('revision', 'wip', onWip);
  });

Gerrit.install(function(self) {
    function onR4r(c) {
      var f = c.textarea();
      var b = c.button('Ready', {onclick: function(){
        c.call(
          {message: f.value},
          function(r) {
            c.hide();
            window.alert(r);
            c.refresh();
          });
      }});
      c.popup(c.div(
        f,
        c.br(),
        b));
      f.focus();
    }
    self.onAction('revision', 'r4r', onR4r);
  });
