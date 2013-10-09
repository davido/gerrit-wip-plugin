Gerrit Work in Progress plugin
==============================

It adds a new button that allows an authorized user to set a
change to Work In Progress, and a button to change from WIP back
to a "Ready For Review" state. Any change in the WIP state will not
show up in anyone's Review Requests. Pushing a new patchset will
reset the change to Review In Progress.

Optionally it can be used in combination with new "Change Owners"
group. The plugin owned capability "Work In Progress" can be granted
to that group, so that only "Change Owners" can toggle the WIP state.

WIP Workflow
------------

Turn normal change to WIP and provide description why:

![Turn normal change to WIP and provide description why](src/main/resources/Documentation/images/mark_as_wip.png)

New comment message:

![New comment message](src/main/resources/Documentation/images/wip_comment.png)

Ready For Review button is shown for WIP change:

![Ready For Review button is shown for WIP change](src/main/resources/Documentation/images/ready_for_review.png)

The change is shown with status "Work In Progress" on change list:

![The change is shown with status "Work In Progress" on change list](src/main/resources/Documentation/images/wip_on_change_list.png)

Reviewers Dashboard filters the WIP changes:

![Reviewers Dashboard filters the WIP changes](src/main/resources/Documentation/images/filtered_wip_changes.png)

Mark it as Ready For Review:

![Mark it as Ready For Review](src/main/resources/Documentation/images/mark_as_ready.png)

Comments are updated correspondingly:

![Comments are updated correspondingly](src/main/resources/Documentation/images/updated_comments.png)

Reviewers Dashboard shows that change again:

![Reviewers Dashboard shows that change again](src/main/resources/Documentation/images/changes_are_shown.png)

Known limitations
-----------------

Old change screen doesn't support JS API. So that the popup dialog is not shown
if "Work In Progress" and "Ready In Review" buttons are used and no comments
can be provided. New change screen should be used for best experience with
WIP Workflow plugin. 

Authorship
----------

This plugin is based on previous work of David Shrewsbury:
https://gerrit-review.googlesource.com/36091
