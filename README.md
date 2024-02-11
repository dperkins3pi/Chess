# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.


Design of client/server structure

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSpnfoccKQCLAwwAIIgQKAM6TMAEyHAARsEkoYsxZwDmUCAFdsMAMRpgVAJ4wASii1JJYKEKQQ0xgO4ALJGA6JUpAC0AHws1JQAXDAA2gAKAPJkACoAujAA9HqqUAA6aADeWZSmALYoADQwuNLu0LKVKCXASAgAvpgU4bAhYWwSUVC29r5QABRFUKUVVSqStVD1MI3NCACUHazsMD2CwmIS0lFaKGAAqtnj2VPru6LiUpLboRpRZACiADJvcEkwE1MwABiVniAFk-tlMLd9g9tj1On0UFE0HoEAgNtQtjsFDDDjAQIMhChzpRLsVgGVKtU5nUbjj7tInupFFEAJIAOXeVl+-wp02p80WyxaMA5SXiENoCKxoWhDMkUQJKCJIj0YE8ZMmfLpe3lTJeos5b25kq1lJgwDVniSEAA1uhDeKLVaofSDo9AvDNv1neqbfa0BjEXDQgjIr7rXb0EHKCH4Mh0GAogAmAAMqby+UtfqjaHa6Fk2l0BmM0D4xxgHwgdjcRi8Pj8CaCodY4biiVSGVU0hcaEzvPNArq7TD3RbmJ9CGrSDQmqmVNmgvW0okTLl7qOJxJYwHKB1d3d+pZ5E+3x5Vz5QJB4Imrt1h8948RUV3QZlMHXD03YFV6rn2rvA9YR6A0OS5c9yXNbNIwDR0JWgwDcQ9L0JyRCN-WjFc1EfXpw2gjDA1HON-ETFN00zfDc3zNBC0wHR9EMIwdBQB0qy0fRmDrbxfEwEjm1wqAomifhTySN40nSbtJF7PJKIDGMx16dgoindi1VGZdvWw4JPzxWQUAQE4UF-DV9yQo8oisN5QXiAA1N50NzK8wQjRC9RwvikxgNNU0wAsiwY4xBlkSthhgABxPlHi4hteKbZgUJoQSYnCt5O3SLQ+Vkq0CIUpksJU4ZIrKSQNLfVdsXvL8YGQBxiqkMrdI9Z5j3eL4fhgfJMpKoTupQVl+EqetfG3ecYEUBBQFtUa+UqPr2T5FJWmc8E+rch9EuUmA+pkLC41HI4oryjz4rIjM0B2vyaICksjGwPQoGwQz4EJXwIsvGKeM8-LW2S9tkgkvrspzANM3mvkR1+n7UMVV6UHq0Z6oWspNNQp4mth5VfARpGAKaizDXA7a+V6vkBrZQaYGG4kLzKZFUQQSoJqmma6ZgFE0TSMUJTW-GQNa08Or6gaVuJsp1uAnpPLOvIxf6-grtouji0Y8wDKndwYAAKQgGd3rKYxmZAW04oCBKnzbWJTnSoHTBy3NM08uAICnKA5rJ-hIa6J4CpgAArXW0ARp2Xegd2ygG1HgxCDHtpOBHhf4Mz3Ja15Bd+RPRd5t1JafLa1r2mOc7xAx5GxvlRhIvgwGd13w-l5OH1TwnjV+EPXbguWJcZE6zbOxWbsYnRgEsRBlVgYBsCewhnFcDxuMbM3oaSoSRK+MSJI0Y7gl9kBDLwMrC504uFXxfexkbh5+cs6y7Icnas75bvJGv6xb-s00pkeYEXNfJrX6sjZD+u5JCVDkugb+15XJ8ylqdby6YB5AA


## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
