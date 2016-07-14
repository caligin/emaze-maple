Emaze Maple
===================

Emaze Maple is a generic Java object mapper library using reflection.

Basic usage example
-------------------

```java
public class BasicUsageExample {

    public static class PersonDAO {
        public PersonTO getPerson() {
            return new PersonTO("John", "Doe", 45);
        }
        public List<PersonTO> getPersons() {
            return Arrays.asList(
                    new PersonTO("John", "Doe", 45),
                    new PersonTO("Jane", "Doe", 35));
        }

        public void save(PersonTO person) {
            /* persist person to database */
        }
    }

    public static class PersonBO {
        private String firstName;
        private String lastName;
        private long age;

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setAge(long age) {
            this.age = age;
        }
    }

    public static class PersonTO {
        private String firstName;
        private String lastName;
        private long age;

        public PersonTO(String firstName, String lastName, long age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
    }

    public final static void main(String args[]) {

        final Mapper mapper = ResolvingMapper.Builder.defaults().build();

        final PersonDAO personDAO = new PersonDAO();
        final PersonTO personTO = personDAO.getPerson();
        final PersonBO personBO = mapper.map(personTO, PersonBO.class);

        Assert.assertEquals(personTO.firstName, personBO.firstName);
        Assert.assertEquals(personTO.lastName, personBO.lastName);
        Assert.assertEquals(personTO.age, personBO.age);

        /* mapping arrays of objects too... */
        final List<PersonTO> personsTO = personDAO.getPersons();
        final List<PersonBO> personsBO = mapper.map(personsTO, PersonBO.class);

        Assert.assertEquals(personsTO.get(0).firstName, personsBO.get(0).firstName);
        Assert.assertEquals(personsTO.get(0).lastName, personsBO.get(0).lastName);
        Assert.assertEquals(personsTO.get(0).age, personsBO.get(0).age);

        Assert.assertEquals(personsTO.get(1).firstName, personsBO.get(1).firstName);
        Assert.assertEquals(personsTO.get(1).lastName, personsBO.get(1).lastName);
        Assert.assertEquals(personsTO.get(1).age, personsBO.get(1).age);

    }
}
```


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
<a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-sa/4.0/88x31.png" />
<!-- </a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/">Creative Commons Attribution-ShareAlike 4.0 International License</a>. -->

