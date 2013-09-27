Gerrit Work in Progress plugin.

This plugin is based on previous work of David Shrewsbury:
https://gerrit-review.googlesource.com/36091

It adds a new button that allows an authorized user to set a
change to Work In Progress, and a button to change from WIP back
to a "Ready For Review" state. Any change in the WIP state will not
show up in anyone's Review Requests. Pushing a new patchset will
reset the change to Review In Progress.

This plugin depends on Gerit 2.8 + one patch:
https://gerrit-review.googlesource.com/50250

It uses the following features that were introduced in Gerrit 2.8:

* Plugin owned capabilities
* UiAction with JS API

TODO:

* Add SSH commands:
* Add documentation
* Send mail

To see it in action (on new and shiny Change Screen 2):

Turn normal change to WIP and provide description why:
http://imgur.com/tMNkyjM

Status changed dialog appears:
http://imgur.com/0I9ciVJ

In comments appears the message:
http://i.imgur.com/h2hQQT4.png

New patch set is reloaded and Ready for Review button apears instead od WIP:
http://imgur.com/JGCSyLO

Mark it as ready for review:
http://imgur.com/tnMGg16

Status change to NEW dialog appears:
http://imgur.com/ha3mFqA

Comments are updates correspondingly:
http://imgur.com/l4E8evp

