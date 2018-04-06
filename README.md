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

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
