@PLUGIN@ - /changes/ REST API
=============================

This page describes the changes related REST endpoints that are added
by the @PLUGIN@.

Please also take note of the general information on the
[REST API](../../../Documentation/rest-api.html).

<a id="revision-endpoints"> Revision Endpoints
----------------------------------------------

### <a id="set-wip"> Mark Revision as Work In Progress
_POST /changes/[\{change-id\}](../../../Documentation/rest-api-changes.html#change-id)/revisions/[\{revision-id\}](../../../Documentation/rest-api-changes.html#revision-id)/@PLUGIN@~set-wip_

Mark a revision as Work In Progress.

Options for that endpoint can be specified in the request body as a
[Input](#options-input) entity.

Caller must be a member of a group that is granted the 'Work In Progress'
capability (provided by this plugin) or be a member of the Administrators
group.

#### Request

```
  POST /changes/myProject~master~I8473b95934b5732ac55d26311a706c9c2bde9940/revisions/674ac754f91e64a0efb8087e59a176484bd534d1/@PLUGIN@~set-wip HTTP/1.0
  Content-Type: application/json;charset=UTF-8

  {
    "message": "Refactoring needs to be done before we can proceed here."
  }
```

#### Response

```
  HTTP/1.1 204 No Content
```

### <a id="set-ready"> Mark Revision as Ready for Review
_POST /changes/[\{change-id\}](../../../Documentation/rest-api-changes.html#change-id)/revisions/[\{revision-id\}](../../../Documentation/rest-api-changes.html#revision-id)/@PLUGIN@~set-ready_

Mark a revision as Ready For Review.

Options for that endpoint can be specified in the request body as a
[Input](#options-input) entity.

Caller must be a member of a group that is granted the 'Work In Progress'
capability (provided by this plugin) or be a member of the Administrators
group.

#### Request

```
  POST /changes/myProject~master~I8473b95934b5732ac55d26311a706c9c2bde9940/revisions/674ac754f91e64a0efb8087e59a176484bd534d1/@PLUGIN@~set-ready HTTP/1.0
  Content-Type: application/json;charset=UTF-8

  {
    "message": "Refactoring is done."
  }
```

#### Response

```
  HTTP/1.1 204 No Content
```


<a id="json-entities">JSON Entities
-----------------------------------

### <a id="options-info"></a> Input

The `Input` entity can contain a message.

* _message_ (optional): Description why the status of this change is toggled.

SEE ALSO
--------

* [Changes related REST endpoints](../../../Documentation/rest-api-changes.html)

GERRIT
------
Part of [Gerrit Code Review](../../../Documentation/index.html)
