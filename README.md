# C0_patient_treatment_ui

The C0 Patient Treatment UI is a web-based component within a larger demonstration. 
This tool showcases functionalities that would be beneficial in a real-world patient
care setting, including:

 - **Patient data management**: Efficient storage and access to patient information.
 - **Personalized treatment plan creation**: Tailoring treatments to individual patient needs.
 - **Display of NIT protocol verification results**: Presentation of the results of NIT protocol
  verification.
 - **Treatment value visualization**: Clear and concise display of the specific values associated
  with treatment options.

This component aims to demonstrate the potential value of a user interface that could assist doctors
in improving patient care by streamlining access to patient information, facilitating personalized
treatment plans, and providing clear and concise presentation of critical information related to patient care.


## Summary

 - Type: C0
 - Name: Patient treatment UI
 - Version: 1.0.0 (January 28,2025)
 - API: [1.0.0 (January 28,2025)](https://raw.githubusercontent.com/VALAWAI/C0_patient_treatment_ui/ASYNCAPI_1.0.0/asyncapi.yml)
 - VALAWAI API: [1.2.0 (March 9, 2024)](https://raw.githubusercontent.com/valawai/MOV/ASYNCAPI_1.2.0/asyncapi.yml)
 - Developed By: [IIIA-CSIC](https://www.iiia.csic.es)
 - License: [GPL 3](LICENSE)
 
 
## Generate Docker image

The recommended way to create a Docker image for this component is to run the script:
 
 ```
./buildDockerImages.sh
```

This script will build the image and tag it with the component's version 
(e.g., `valawai/c0_patient_treatment_ui:1.0.1`).

The script offers several options for customization:

* **Specify tag:** Use `-t <tag>` or `--tag <tag>` to assign a custom tag name 
to the image (e.g., `./buildDockerImages.sh -t my-custom-image-name`).
* **Help message:** Use `-h` or `--help` to display a detailed explanation 
of all available options.

For example, to build an image with the tag `latest`, run:

```bash
./buildDockerImages.sh -t latest
```

This will create the container named `valawai/c0_patient_treatment_ui:latest`.


### Docker environment variables

The most useful environment variables on the docker image are:

 - **RABBITMQ_HOST** is the host where the RabbitMQ is available.
 The default value is **mov-mq**.
 - **RABBITMQ_PORT** defines the port of the RabbitMQ.
 The default value is **5672**.
 - **RABBITMQ_USERNAME** contains the user's name that can access the RabbitMQ.
 The default value is **mov**.
 - **RABBITMQ_PASSWORD** is the password used to authenticate the user who can access the RabbitMQ.
 The default value is **password**.
 - **LOG_LEVEL** defines the level of the log messages to be stored.
 The default value is **INFO**.
 - **QUARKUS_HTTP_HOST** contains the server host that will expose the REST health endpoints.
 The default value is __0.0.0.0__.
 - **QUARKUS_HTTP_PORT** defines the server port that will expose the REST health endpoints.
 The default value is __8080__.
 - **C0_PATIENT_TREATMENT_UI_URL** defines the URL where the component web UI is accessible.
 The default value is __http://localhost:8080__.
 
 

### Docker health check

This component provides several REST endpoints for monitoring its health status, 
enabling external systems and orchestration tools to assess its operational state.

The following endpoints are available:

*   **/q/health/live**: This endpoint indicates whether the component is currently
 running. A successful response signifies that the component's process is active.

*   **/q/health/ready**: This endpoint indicates whether the component is ready 
to process messages from the VALAWAI infrastructure. A successful response signifies 
that the component's dependencies and internal services are initialized and functioning 
correctly.

*   **/q/health/started**: This endpoint indicates whether the component has completed 
its startup sequence. A successful response signifies that the component's initialization
 procedures have finished.

*   **/q/health**: This endpoint provides a comprehensive health status report, encompassing
 the results of all the aforementioned checks.

Each endpoint returns a JSON payload containing a `status` field (with values of `UP` or `DOWN`)
and a `checks` array detailing the individual health checks performed. The following example 
illustrates the response from a `GET` request to the `/q/health` endpoint:

 
 ```json
 {
    "status": "UP",
    "checks": [
        {
            "name": "SmallRye Reactive Messaging - liveness check",
            "status": "UP",
            "data": {
                "registered": "[OK]",
                "change_parameters": "[OK]",
                "send_log": "[OK]",
                "send_unregister_component": "[OK]",
                "send_register_component": "[OK]",
                "send_email": "[OK]"
            }
        },
        {
            "name": "Registered C0 patient treatment ui",
            "status": "UP"
        },
        {
            "name": "SmallRye Reactive Messaging - readiness check",
            "status": "UP",
            "data": {
                "registered": "[OK]",
                "change_parameters": "[OK]",
                "send_log": "[OK]",
                "send_unregister_component": "[OK]",
                "send_register_component": "[OK]",
                "send_email": "[OK]"
            }
        },
        {
            "name": "SmallRye Reactive Messaging - startup check",
            "status": "UP"
        }
    ]
}
 ```
 
A user interface is also available for visualizing the component's health status at 
[http://localhost:8080/q/health-ui/](http://localhost:8080/q/health-ui/). This interface 
provides a more user-friendly representation of the health check data.

These endpoints are particularly useful for configuring health checks within orchestration 
tools such as Docker Compose. The following example demonstrates a Docker Compose health 
check configuration for this component:

```
    healthcheck:
      test: ["CMD-SHELL", "curl -s http://localhost:8080/q/health | grep -m 1 -P \"^[\\s|\\{|\\\"]+status[\\s|\\:|\\\"]+.+\\\"\" |grep -q \"\\\"UP\\\"\""]
      interval: 1m
      timeout: 10s
      retries: 5
      start_period: 1m
      start_interval: 5s
```

It is important to note that the host and port on which these REST health endpoints 
are exposed can be configured using the Docker environment variables **QUARKUS_HTTP_HOST**
and **QUARKUS_HTTP_PORT**, respectively.


## Deploy

On the file [docker-compose.yml](docker-compose.yml), you can see how the docker image
of this component can be deployed on a valawai environment. On this file are defined
the profiles **mov** and **mail**. The first one is to launch
the [Master Of Valawai (MOV)](https://github.com/VALAWAI/MOV) and the second one is to start
a [mocked e-mail server](https://github.com/dbck/docker-mailtrap). You can use the next
command to start this component with the MOV and the mail server.

```
COMPOSE_PROFILES=mov docker compose up -d
```

After that, if you open a browser and go to [http://localhost:8080](http://localhost:8080)
to view the user interface of the component. At [http://localhost:8081](http://localhost:8081)
you can view the MOV user interface. Also, you can access the RabbitMQ user interface
at [http://localhost:8082](http://localhost:8082) with the credentials **mov:password**.

The docker compose defines some variables that can be modified by creating a file named
[**.env**](https://docs.docker.com/compose/environment-variables/env-file/) where 
you write the name of the variable plus equals plus the value.  As you can see in
the next example.

```
MQ_HOST=rabbitmq.valawai.eu
MQ_USERNAME=c0_patient_treatment_ui
MQ_PASSWORD=lkjagb_ro82t¿134
```

The defined variables are:


 - **C0_PATIENT_TREATMENT_UI_TAG** is the tag of the C0 patient treatment ui docker image to use.
 The default value is **latest**.
 - **MQ_HOST** is the hostname of the message queue broker that is available.
 The default value is **mq**.
 - **MQ_PORT** is the port of the message queue broker is available.
 The default value is **5672**.
 - **MQ_UI_PORT** is the port of the message queue broker user interface is available.
 The default value is **8081**.
 - **MQ_USER** is the name of the user that can access the message queue broker.
 The default value is **mov**.
 - **MQ_PASSWORD** is the password used to authenticate the user who can access the message queue broker.
 The default value is **password**.
 - **RABBITMQ_TAG** is the tag of the RabbitMQ docker image to use.
 The default value is **management**.
 - **MONGODB_TAG** is the tag of the MongoDB docker image to use.
 The default value is **latest**.
 - **MONGO_PORT** is the port where MongoDB is available.
 The default value is **27017**.
 - **MONGO_ROOT_USER** is the name of the root user for the MongoDB.
 The default value is **root**.
 - **MONGO_ROOT_PASSWORD** is the password of the root user for the MongoDB.
 The default value is **password**.
 - **MONGO_LOCAL_DATA** is the local directory where the MongoDB will be stored.
 The default value is **~/mongo_data/movDB**.
 - **MOV_DB_NAME** is the name of the database used by the MOV.
 The default value is **movDB**.
 - **MOV_DB_USER_NAME** is the name of the user used by the MOV to access the database.
 The default value is **mov**.
 - **MOV_DB_USER_PASSWORD** is the password of the user used by the MOV to access the database.
 The default value is **password**.
 - **MOV_TAG** is the tag of the MOV docker image to use.
 The default value is **latest**.
 - **MOV_UI_PORT** is the port where the MOV user interface is available.
 The default value is **8080**.

The database is only created the first time where script is called. So, if you modify
any of the database parameters you must create again the database. For this, you must
remove the directory defined by the parameter **MONGO_LOCAL_DATA** and start again
the **docker compose**.

You can stop all the started containers with the command:

```
COMPOSE_PROFILES=mov docker compose down
```
  
## Development

You can start the development environment with the script:

```shell script
./startDevelopmentEnvironment.sh
```

After that, you have a bash shell where you can interact with
the Quarkus development environment. You can start the development
server with the command:

```shell script
startServer
```

Alternatively, to run the test using the started Quarkus client, you can use Maven.

 * **mvn test**  to run all the tests
 * **mvnd test**  to run all the tests on debugging mode.
 * **mvn -DuseDevMOV=true test**  to run all the tests using the started Master of VALAWAI,
 	instead of an independent container.

Also, this starts the tools:

 * **RabbitMQ**  the server to manage the messages to interchange with the components.
 The management web interface can be opened at [http://localhost:8081](http://localhost:8081) with the credential
 **mov**:**password**.
 * **MongoDB**  the database to store the data used by the MOV. The database is named as **movDB** and the user credentials **mov:password**.
 * **Mongo express**  the web interface to interact with the MongoDB. The web interface
  can be opened at [http://localhost:8082](http://localhost:8082) with the credential
 **mov**:**password**.
 * **PG admin**  the web interface to interact with the PostgreSQL.
  The web interface can be opened at [http://localhost:8083](http://localhost:8083).
  The login credentials are **pg_admin@valawai.eu:password**, and for the server use the next parameters.

    - **database name**: c0_patient_treatment_ui_db
    - **database host**: host.docker.interbal
    - **database port**: 5432
    - **user name**: c0_patient_treatment_ui
    - **user password**: password
    
  * **Master of VALAWAI**  the component that mantains the topology connections between components.
  The web interface can be opened at [http://localhost:8084](http://localhost:8084).


## Links

 - [C0 E-mail actuator documentation](https://valawai.github.io/docs/components/C0/email_actuator)
 - [Master Of VALAWAI tutorial](https://valawai.github.io/docs/tutorials/mov)
 - [VALWAI documentation](https://valawai.github.io/docs/)
 - [VALAWAI project web site](https://valawai.eu/)
 - [Twitter](https://twitter.com/ValawaiEU)
 - [GitHub](https://github.com/VALAWAI)