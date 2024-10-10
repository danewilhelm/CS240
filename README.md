# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

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
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

Diagram for Phase 2
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatLlqOJpEuGFocjAABmPK2ka2hCiKRHiq60pJpeMHlAagrdEglEwIqyqzH63jJDAKAAB4qNgBCJI6zoEjhLLlJ63pBgGQYhix5qRhRwA2jAsbBtocmJn+kElshPLZrmmD-r+RTUABAxMfcfRfNOs7Nv87b2dkPaVP2TjNMOtyjrs7lBp587ecuq7eH4gReCg6B7gevjMMe6SZJgfkXg5JTlBU0gAKK7sV9TFc0LQPqoT7dB5jboD5bJ2eUDVzrZ5n2ZhcFpb6SF9WAqEYhhcpYQppG4TA5JgIZ6l1o1aAkUybrkeU1G8tO0BIAAXvEiRUd4wwwNOkAdVpEbQU68onUG217TJSQ8XxAkIEJqQiWJkkoNJT4meNMiKaSRgoNwmRze16DLWaEaFJaINg4YW1QLt+1oP9LVdeUyHpdZCB5l1bI-lc35Xr555gIVgXBUunBxeugSQrau7QjAADio6splp45RTRNk4VbPlVV9ijvVkWLc1yZY7dC0dXZ-McTAyCxBzoyqEh0Jq2ow3oRj2GTUp00UhDEtztDZE6etNGy6k91o4dEDHadksXZ2PW2-bj0wM9-FKm9MDCRAokSVJj36xNK1GyrYDaxrFurVbMCQhYqA0CdyrTZzoaG9p7HXbBMAGJtmei+rEdnECFla5zeME1X3UC6Wzd9GXajjD7aAVP0bcAJLSB3AAsvazN0fQnpkBoVp8o-zjoCCgA2U9hTPnet6O-f-DAjSk455M5JTAUDsFUxt6oHfdN36+jP3Q8j2vE-6i54Wz308+L8vowv2vffSFvO9mDpp4eKG5sA+CgNgbg8BdSZHZqOFIWUzwH0VgVSotQGgizFsEM26Ahy-13scaWDc2o4OfL0fBnUG4oPBDAFSmRtawjgDAlA2tdZYgrhdPCs01KQyWjnKOed2TW02ndFGD0nyO2dkGM6TU3ZsXyjQ5GqNva+1eu9T6ocfrh0whqIG7pjax1HLCX+mIYD8JhmtZOFIUYoHTm3cxlsro0Nep3XiMAAByNR6h+0EoHD6wcvphz+jog2Aj9F0JYUYkxZi5FwworEaAhh7EhLMsQ6BXp6GjjrpQ1MKCnK-w7gARl7AAZkHgQjspxcqHwqNTUs18UC33KMUsptMVzAIZgESwoN4KiQAFIQB5HA0YgR34gAbLzZBV1rxVEpHeFobdxZy1wb0SBwBulQDgBAeCUBZgUJ-JjNJvChxrI2VsnZeyN7SAglQpxN0ABWgyHBGMeTyVhKA0QjQ4Xorhptll8NiUIqiNslHiIOpRI6tsZHozkXcwuoKHaqP9uogJmjfqyRSYDXO+iZoMOiQ4xOcSITWLTkk0cBLWJwrJPA1xL1kV+I0d9dF6NMWcJgH4LQmTRiwhAKkFkZBU4OEuTfaQCdWJEvZdgTlhg-RkvFJi1qMABlvKyWoGyCtplph6BU-e-lanH1LG0+mCUAheHWV2L0sBgDYEgYQB23NsrVLyWmIqpVyqVVaMYKWqTUwkxyVBfONCQDcDwIyPQBhYSjRgro7FykQ1QDDfoFAkaKWCPhsGy1RhgBZvkOGsQCqZaIEtYmgw2SNX5wAjqtk1SqYGu1YAlcQA
