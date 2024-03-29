# Financial Planner #
*Home finance management.  A simple web app to manage finances.*

## Features ##

* Inserting income data with next fields: date, sum, type, account, category, tags
* Balance monitoring by date, category, expense and income
* OAuth2 authentication with Google+

## Screenshots
![Navigation bar](https://i.gyazo.com/c144f492fbcae186ed16ed9e06f836fc.png)

![Add transaction](https://i.gyazo.com/e72cad57019d08ff1f3c19164a02bfd5.png)

![Add account](https://i.gyazo.com/f5b7db7c7b1f0c54998023a2bc22d15a.png)

![List of accounts](https://i.gyazo.com/388425d28230124018b9dafb06b427d3.png)

![Login page](https://i.gyazo.com/80176342a52aeba17f81ab8a3fa5ea0d.png)

## Technology stack
* [MySql](https://www.mysql.com) - database
* [JUnit5](https://github.com/junit-team/junit5) - unit tests
* [Mockito](https://github.com/mockito/mockito) - mocking framework for unit tests
* [Lombok](https://github.com/rzwitserloot/lombok) - reduces amount of routine code
* [Hibernate](https://github.com/hibernate/hibernate-orm) - ORM support
* [Thymeleaf](https://github.com/thymeleaf) - server-side Java template engine
* [Spring](https://github.com/spring-projects/spring-framework) - Core, Data, MVC, Security

## Setup ##
* Requires Java 8 or better
* Requires Maven 3

"cd" to the directory you installed into and issue the command:

    mvn install

Alternatively, you can import the source into Eclipse or another IDE and make the project a Maven project, then build.

## License

This project is released under the the MIT License. 
Please refer to the [LICENSE file](LICENSE) for full details.