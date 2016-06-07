Emaze Maple
===================

Emaze Maple is a generic Java object mapper library using reflection.

Documentation
--------------------------

Questions related to the usage of Emaze Maple should be posted to the [user mailing list][ml].

Where can I get the latest release?
-----------------------------------

You can pull it from the central Maven repositories:

```xml
<dependency>
  <groupId>net.emaze</groupId>
  <artifactId>emaze-maple</artifactId>
  <version>2.2</version>
</dependency>
```

Contributing
------------

We accept PRs via github. The [developer mailing list][ml] is the main channel of communication for contributors.
There are some guidelines which will make applying PRs easier for us:
+ Respect the code style.
+ Create minimal diffs - disable on save actions like reformat source code or organize imports. If you feel the source code should be reformatted create a separate PR for this change.
+ Provide JUnit tests for your changes and make sure your changes don't break any existing tests by running ```mvn clean test```.


License
-------
Code is under the [Creative Commons](https://creativecommons.org/licenses/by-sa/4.0/legalcode) public license.

