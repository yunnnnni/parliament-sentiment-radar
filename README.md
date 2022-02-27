# Gruppe_8_Mittwoch_3_Parliamentsentimentradar

**Team members:**

- Lu, Yunni
    s0425513@stud.uni-frankfurt.de
- Zhang, Chufang
    s8295391@stud.uni-frankfurt.de
- Song, Tinghuan
    s6875719@stud.uni-frankfurt.de
- Chan, Yi-Ling
    s9728062@stud.uni-frankfurt.de

## Server

Run following commands in project directory. Make sure that `maven` and `java` are installed on the local machine. Adapt `./backend/pom.xml` to the local setup.

- Read 19. and 20. plenary protocols from https://www.bundestag.de/services/opendata into mongodb

    ```shell
    $ cd ./backend
    $ mvn compile exec:java -Dexec.mainClass="org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper.ProtocolMongoDBWriter"
    ```

    - MongoDB config is defined in `./backend/config/config.json`

- Start REST service, default port = 4567

    ```shell
    $ cd ./backend
    mvn compile exec:java -Dexec.mainClass="org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST.RESTStarter"
    ```

- Run Swagger UI with docker

    ```shell
    docker run -p 80:8080 -e SWAGGER_JSON=/swagger-ui/swagger.yml -v $PWD/swagger-ui:/swagger-ui swaggerapi/swagger-ui
    ```

    Enter http://localhost:8088/ to get access to the swagger-ui and see all available services

    <img src="README.assets/swagger-ui.png" alt="swagger-ui" style="zoom: 67%;" />

## Client

Open `./frontend/index.html` to open client dashboard

### Overview

<img src="README.assets/dashboard-overview.png" alt="dashboard-overview" style="zoom: 67%;" />

### Speeche - fulltext visualization

<img src="README.assets/dashboard-full text.png" alt="dashboard-full text" style="zoom: 67%;" />

## Project organisation

### User stories

![user-stories](documentation/projektdokumentation/projektdokumentation.assets/user-stories.png)

### Gantt Diagram

![user-stories](documentation/projektdokumentation/projektdokumentation.assets/Gantt.png)
