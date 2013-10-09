Installation instructions
=========================

Supported Gerrit version
------------------------

Gerrit 2.8

Patching Gerrit core
--------------------

* apply provided Gerrit core patch: 0001-WIP-core-extension.patch
* buck build gerrit
* buck build api_install

Compiling wip-plugin
--------------------

* git clone https://github.com/davido/gerrit-wip-plugin
* cd gerrit-wip-plugin
* mvn package

Deploying wip-plugin
--------------------
* cp target/wip-plugin-1.0.jar $site_path/plugins

