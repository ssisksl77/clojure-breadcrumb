# kafka-demo2

demo for using kafka-client

## Installation

kafka is essential

```
    version: '2' # 도커파일 버전.                                                                                          
    
    networks:
      test:
    
    services:
      zookeeper:
        image: wurstmeister/zookeeper:3.4.6
        container_name: zookeeper
        ports:
          - "2181:2181"
        networks:
          - test
    
      kafka:
        image: wurstmeister/kafka:2.12-2.0.1
        container_name: kafka
        environment:
          KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
          KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
          KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
          KAFKA_ADVERTISED_PORT: 9092
          KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
          KAFKA_CREATE_TOPICS: "yhnam-topic:1:1"   # Topic명:Partition개수:Replica개수                                     
        volumes:
          - ./volumes:/var/run/docker.sock
        ports:
          - "9092:9092"
        depends_on:
          - zookeeper
        networks:
          - test
```

## Usage

    $ docker-compose up
    $ lein uberjar
    $ java -jar kafka-demo2-0.1.0-standalone.jar

## Options

none

## Examples
### Bugs
### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
