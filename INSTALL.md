
Prerequisites: Gerrit 2.8

* apply provided Gerrit core patch: 0001-WIP-core-extension.patch
* buck build gerrit
* buck build api_install
* deploy the patched Gerrit buck-out/gen/gerrit.war
* git clone https://github.com/davido/gerrit-wip-plugin
* cd gerrit-wip-plugin
* mvn package
* deploy target/wip-plugin-1.0.jar $site_path/plugins

FAQ:

Q:
Why we need to patch Gerrit core?

A:
Because it was rejected: https://gerrit-review.googlesource.com/50250

Q:
Why it was rejected?

A:
Because a comunity driven project is not a debate club and someone has
to make tough decisions.
