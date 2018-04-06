# Prototype of The Product

The prototype is responsible to build and describe the most basic functionality of how The Product will work and with your most basic architecture. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

#### IntelliJ

For development we recommend using IntelliJ IDEA from JetBrains. You can download the tool [here](https://www.jetbrains.com/idea/download/).

#### Java 8+

Java JRD and Java SDK should be installed in your local machine to run the application.

##### Linux

First update your package index

```bash
$ sudo apt-get update
```

Than install the JRE

```bash
$ sudo apt-get install default-jre
```

And finally install the JDK

```bash
$ sudo apt-get install default-jdk
```

##### Mac

Update the brew

```bash
brew update
```

Install java using homebrew

```bash
brew cask install java
```

#### Scala

Scala is the Language for the application based in the JVM.

##### Linux

```bash
$ sudo apt-get install scala
```

##### Mac

```bash
brew install scala
```

#### Docker

Docker for this prototype is the main source of our architecture, the only app outside docker is Kafka.

##### Linux

```bash
$ sudo apt-get install docker-ce
```

##### Mac

```bash
brew install docker
```

#### Kafka

Download Kafka Binary in [this](https://www.apache.org/dyn/closer.cgi?path=/kafka/1.1.0/kafka_2.12-1.1.0.tgz) link.


## Running the Prototype

Execute the shell script *build_env.sh*

```bash
sh build_env.sh
```

Start the Kafka and create the topics running the commands below:

```bash
~/kafka/bin/zookeeper-server-start.sh config/zookeeper.properties
~/kafka/bin/kafka-server-start.sh config/server.properties
~/kafka/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic hero
```
And you can run the application using the command inside the project folder:

```bash
sbt run
```

You can check the results in the databases, or in the kafka topic using this command line:

```bash
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hero --from-beginning
```

### Break down into end to end tests

TODO


## Deployment

No Deployment method yet.

## Built With

* [SBT](https://www.scala-sbt.org/0.13/docs/index.html) - Scala Iterative Building Tool

## Contributing

TODO

## Versioning

TODO 

## Authors

* **Thiago Bladim** - *Initial work* - [TRBaldim](https://github.com/TRBaldim)

See also the list of [contributors](https://github.com/TheBrAuOrganization/Prototype/graphs/contributors) who participated in this project.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details
