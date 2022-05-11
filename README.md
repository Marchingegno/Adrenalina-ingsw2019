# Adrenaline

<img src="https://cf.geekdo-images.com/opengraph/img/ac8k6cuJA8cf6jnRlPjzkDd_YuQ=/fit-in/1200x630/pic3476604.jpg" width="450" height="315"></img>


## Description
The assignment was to build a digital version of the board game "Adrenaline" using Java, becoming familiar with some of the most common design patterns like Model-View-Controller, Strategy, parallelism and more.

#### Requirements reached

Complete rules + CLI + GUI + Socket + RMI + 1 Advanced Functionality

- Advanced functionality
    - **Multiple matches**: the ability of the server to handle multiple independent matches.

## Requirements

The game requires [Java 8] or later versions to run.

#### Client

```sh
$ java -jar Client-jar-with-dependencies.jar [cli/gui]
```

#### Parameters
You can set various parameters for the connection to the server in a file called `"server-config.json"`, which needs to be located in the same folder as the jar.
The parameters you can set are:
- `waitingTimeInLobby` - the time the server waits in the lobby before starting the game
- `answerTimeLimit` - the time each player has to give an answer before it is considered inactive.
- `host` - the IP address of the server
- `rmiPort` - port used by RMI
- `socketPort` - port used by Socket

##### Options
- `[cli/gui]` - type `cli` or `gui` to chose between Command Line Interface and Graphical User Interface. If nothing is selected the game will default to Command Line Interface, and then will ask which one the user wants to use.

#### Server
```sh
$ java -jar Server-jar-with-dependencies.jar
```
    
    
## Developers

- Matteo Marchisciana (https://github.com/Marchingegno)
- Dennis Motta (https://github.com/Desno365)
- Andrea Marcer (https://github.com/AndreaMarcer)



