# [![](https://raw.githubusercontent.com/wiki/zaproxy/zaproxy/images/zap32x32.png)](https://www.zaproxy.org/) OWASP ZAP Desktop User Guide

Welcome to the OWASP Zed Attack Proxy (ZAP) Desktop User Guide.

This is available both as context sensitive help within ZAP and online in the [ZAP website](https://www.zaproxy.org/docs/desktop/).

The English help files are under the [/addOns/help](https://github.com/zaproxy/zap-core-help/tree/main/addOns/help) directory, so if you'd like to make a change, create a pull request against those files, and they will be updated in the site (eventually).

This User Guide has been translated into many languages, all of which are available via the [ZAP Marketplace](https://www.zaproxy.org/addons/).

You can help translate those pages into other languages via the [OWASP ZAP Help Crowdin project](https://crowdin.com/project/owasp-zap-help).

## Building

The help add-ons are built with [Gradle], each add-on has its own project which is located under the `addOns` project/directory.
The add-ons are identified with the prefix `help_` followed by its locale, for example, for French the project name is `help_fr_FR`
while for Japanese it is `help_ja_JP`. The default help add-on, English, does not follow this rule, it's just identified with `help`.

To build all add-ons, simply run:

    ./gradlew build

in the main directory of the project, the add-ons will be placed in the directory `build/zapAddOn/bin/` of each project.

To build an add-on individually run:

    ./gradlew :addOns:<name>:build

replacing `<name>` with the name of the add-on (e.g. `help_es_ES`).


[Gradle]: https://gradle.org/
