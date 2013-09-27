@PLUGIN@ set
============

NAME
----
@PLUGIN@ set - Mark change as Work In Progres or Ready For Review

SYNOPSIS
--------
```
ssh -p @SSH_PORT@ @SSH_HOST@ @PLUGIN@ set
  [--project <PROJECT> | -p <PROJECT>]
  [--message <MESSAGE> | -m <MESSAGE>]
  [--wip | --ready]
  {COMMIT | CHANGEID,PATCHSET}...
```

DESCRIPTION
-----------
Mark the change as Work In Progress or Ready For Review.

Patch sets should be specified as complete or abbreviated commit
SHA-1s.  If the same commit is available in multiple projects the
--project option may be used to limit where Gerrit searches for
the change to only the contents of the specified project.

For current backward compatibility with user tools patch sets may
also be specified in the legacy 'CHANGEID,PATCHSET' format, such as
'8242,2'.  Support for this legacy format is planned to be removed
in a future edition of @PLUGIN@.  Use of commit SHA-1s is strongly
encouraged.

ACCESS
------
Caller must be a member of a group that is granted the 'Work In Progress'
capability (provided by this plugin) or the 'Administrate Server'
capability.

SCRIPTING
---------
This command is intended to be used in scripts.

OPTIONS
-------

`--project`
:	Name of the project the intended changes are contained
	within.  This option must be supplied before the commit
	SHA-1 in order to take effect.

`--message`
:	Optional description why the state of the change is toggled.

`--wip`
:	Mark the change as Work In Progress
	(option is mutually exclusive with --ready)

`--ready`
:	Mark the change as Ready For Review
	(option is mutually exclusive with --wip)

EXAMPLES
--------
Mark the change as Work In Progress:

```
  $ ssh -p @SSH_PORT@ @SSH_HOST@ @PLUGIN@ set --wip -m '"Needs some refactorig"' c0ff33
```

Mark the change as Ready For Review:

```
  $ ssh -p @SSH_PORT@ @SSH_HOST@ @PLUGIN@ set --ready -m '"Refactorig is done"' c0ff33
```

SEE ALSO
--------

* [Access Control](../../../Documentation/access-control.html)
