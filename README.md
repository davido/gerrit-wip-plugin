Gerrit Work in Progress plugin.

This plugin is based on previous work of David Shrewsbury:
https://gerrit-review.googlesource.com/36091

It adds a new button that allows an authorized user to set a
change to Work In Progress, and a button to change from WIP back
to a "Ready For Review" state. Any change in the WIP state will not
show up in anyone's Review Requests. Pushing a new patchset will
reset the change to Review In Progress.

This plugin depends on Gerit 2.8 and two changes:

* https://gerrit-review.googlesource.com/50250
* https://gerrit-review.googlesource.com/48254

Optionally it can be used in combination with new "Change Owners"
group. The plugin owned capability "Work In Progress" can be granted
to that group, so that only "Change Owners" can toggle the WIP state.

It uses the following features that were introduced in Gerrit 2.8:

* Plugin can contribute buttons (UiAction) with JavaScript API
* Plugin owned capabilities
* Plugin can provide its name in MANIFEST file 

TODO:

* Add SSH commands:
* Add documentation
* Send mail

To see it in action:

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

Install

# Patch upstream Gerrit
# Cherry pick the dependent changes mentioned above
# buck build gerrit
# buck build api_install
# deploy the patched Gerrit buck-out/gen/gerrit.war
# git clone https://github.com/davido/gerrit-wip-plugin
# cd gerrit-wip-plugin
# mvn package
# deploy target/wip-plugin-1.0.jar $site_path/plugins

Configure

* Grant the Work In Progress global capability to a group
* Done

Known problems

Old change screen doesn't support JS API. So that the popup dialog is not shown
if "Work In Progress" and "Ready In Review" buttons are useda and no comment
can be provided. New change screen should be used for best experience with
WIP Workflow feature. 
