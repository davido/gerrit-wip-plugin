Gerrit Work in Progress plugin
==============================

This plugin adds a new button that allows an authorized user to set a
change to Work In Progress, and a button to change from WIP back
to a "Ready For Review" state.

Any change in the WIP state will not show up in anyone's Review
Requests. Pushing a new patchset will reset the change to Review
In Progress.

In addition this plugin exposes this functionality as REST endpoints
and SSH command.

It is intended to be used in combination with the new "Change Owners" group. The
plugin owned capability "Work In Progress" can be granted to that group,
so that only change owners can toggle the WIP state.

Work In Progress Workflow:
--------------------------

Turn a change to WIP
--------------------

![Turn normal change to WIP and provide description why](images/mark_as_wip.png)

New comment message
-------------------

![New comment message](images/wip_comment.png)

Ready For Review button is shown for WIP change
-----------------------------------------------

![The patch set is reloaded and Ready for Review is shown](images/ready_for_review.png)

The change is shown with status "Work In Progress" on change list
-----------------------------------------------------------------

![The change is shown with status "Work In Progress" on change list](images/wip_on_change_list.png)

Reviewers Dashboard filters the WIP changes
-------------------------------------------

![Reviewers Dashboard filters that WIP changes](images/filtered_wip_changes.png)

Mark it as Ready For Review
---------------------------

![Mark it as ready for review](images/mark_as_ready.png)

Comments are updated correspondingly
------------------------------------

![Comments are updated correspondingly](images/updated_comments.png)

Reviewers Dashboard shows that change again
-------------------------------------------

![Reviewers Dashboard shows that change again](images/changes_are_shown.png)

Known limitations
-----------------

Old change screen doesn't support JS API. So that the popup dialog is not shown
if "Work In Progress" and "Ready In Review" buttons are used and no comments
can be provided. New change screen should be used for best experience with
WIP Workflow plugin.

Authorship
----------

[This plugin is based on previous work of David Shrewsbury](https://gerrit-review.googlesource.com/36091)

