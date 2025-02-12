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
 - Version: 1.0.0 (February 4,2025)
 - API: [1.0.0 (February 4,2025)](https://raw.githubusercontent.com/VALAWAI/C0_patient_treatment_ui/ASYNCAPI_1.0.0/asyncapi.yml)
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

The following environment variables are used to configure the Docker container. 
These variables allow for customization of the application's runtime behavior 
without requiring modification of the Docker image itself.

*   **`RABBITMQ_HOST`**: Specifies the hostname or IP address of the RabbitMQ server. 
This variable determines the location where the application connects to the message broker.
 The default value is `mov-mq`.

*   **`RABBITMQ_PORT`**: Specifies the port number used for communication with the RabbitMQ
 server. The default value is `5672`.

*   **`RABBITMQ_USERNAME`**: Specifies the username used for authenticating with the RabbitMQ
 server. The default value is `mov`.

*   **`RABBITMQ_PASSWORD`**: Specifies the password used for authentication with the RabbitMQ
server. **Caution:** Exercise caution when managing this variable. Avoid hardcoding sensitive 
information. The default value is `password`.

*   **`LOG_LEVEL`**: Defines the verbosity of log messages generated by the application. Common 
log levels, in increasing order of verbosity, include `TRACE`, `DEBUG`, `INFO`, `WARN`, and `ERROR`. 
The default value is `INFO`.

*   **`QUARKUS_HTTP_HOST`**: Specifies the network interface the embedded HTTP server binds to 
for exposing REST health endpoints. A value of `0.0.0.0` indicates binding to all available interfaces
. The default value is `0.0.0.0`.

*   **`QUARKUS_HTTP_PORT`**: Specifies the port number the embedded HTTP server listens on for 
REST health endpoint requests. The default value is `8080`.

*   **`C0_PATIENT_TREATMENT_UI_URL`**: Specifies the URL where the component web UI is accessible.
The default value is `http://localhost:8080`.
 
 
**Important Considerations:**

*   Default values are used if corresponding environment variables are not explicitly set.
*   For enhanced security, it is strongly recommended to utilize Docker secrets or other secure 
configuration management mechanisms to handle sensitive information such as passwords. Avoid 
storing passwords directly in Dockerfiles or scripts.


### Docker Health Checks

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


## Deployment on a VALAWAI Environment

The `docker-compose.yml` file defines how to deploy the C0 Patient Treatment UI component within
a VALAWAI environment.  It includes profiles for the Master of VALAWAI (MOV) and a mocked email server.

To start the component with MOV and the mail server, use the following command:

```bash
COMPOSE_PROFILES=mov docker compose up -d
```

After deployment, access the user interface at [http://localhost:8080](http://localhost:8080).
The MOV user interface is available at [http://localhost:8081](http://localhost:8081), 
and the RabbitMQ management interface at [http://localhost:8082](http://localhost:8082) 
with credentials `mov:password`.

### Configuration 

Environment variables can be configured by creating a `.env` file (see 
[Docker Compose documentation](https://docs.docker.com/compose/environment-variables/env-file/)).  
Define variables in the `.env` file using the format `VARIABLE_NAME=value`.  For example:

On the file [docker-compose.yml](docker-compose.yml), you can see how the docker image
of this component can be deployed on a valawai environment. On this file are defined
the profiles **mov** and **mail**. The first one is to launch
the [Master Of Valawai (MOV)](https://github.com/VALAWAI/MOV) and the second one is to start
a [mocked e-mail server](https://github.com/dbck/docker-mailtrap). You can use the next
command to start this component with the MOV and the mail server.

```
MQ_HOST=rabbitmq.valawai.eu
MQ_USERNAME=c0_patient_treatment_ui
MQ_PASSWORD=lkjagb_ro82t¿134
```

The following environment variables are supported:

* **`C0_PATIENT_TREATMENT_UI_TAG`:** Tag for the C0 Patient Treatment UI Docker image. Default: `latest`
* **`MQ_HOST`:** Hostname of the message queue broker. Default: `mq`
* **`MQ_PORT`:** Port of the message queue broker. Default: `5672`
* **`MQ_UI_PORT`:** Port of the message queue broker UI. Default: `8081`
* **`MQ_USER`:** Username for accessing the message queue broker. Default: `mov`
* **`MQ_PASSWORD`:** Password for accessing the message queue broker. Default: `password`
* **`RABBITMQ_TAG`:** Tag for the RabbitMQ Docker image. Default: `management`
* **`MONGODB_TAG`:** Tag for the MongoDB Docker image. Default: `latest`
* **`MONGO_PORT`:** Port where MongoDB is accessible. Default: `27017`
* **`MONGO_ROOT_USER`:** Root username for MongoDB. Default: `root`
* **`MONGO_ROOT_PASSWORD`:** Root password for MongoDB. Default: `password`
* **`MONGO_LOCAL_DATA`:** Local directory for MongoDB data. Default: `~/.mongo_data/patienttreatmentuiMovDB`
* **`MOV_DB_NAME`:** Name of the database used by MOV. Default: `movDB`
* **`MOV_DB_USER_NAME`:** Username used by MOV to access the database. Default: `mov`
* **`MOV_DB_USER_PASSWORD`:** Password used by MOV to access the database. Default: `password`
* **`MOV_TAG`:** Tag for the MOV Docker image. Default: `latest`
* **`MOV_UI_PORT`:** Port where the MOV UI is accessible. Default: `8081`
* **`C0_PATIENT_TREATMENT_UI_PORT`:** Port for the C0 Patient Treatment UI web interface. Default: `8080`
* **`PG_LOCAL_DATA`:** Local directory for PostgreSQL data. Default: `~/.pg_data/c0_patient_treatment_ui_db`
* **`POSTGRES_DB`:** Name of the PostgreSQL database used by the UI. Default: `c0_patient_treatment_ui_db`
* **`PG_USER_NAME`:** Username used by the UI to access the PostgreSQL database. Default: `c0_patient_treatment_ui`
* **`POSTGRES_PASSWORD`:** Password used by the UI to access the PostgreSQL database. Default: `password`

### Database Considerations

The databases are created only during the initial deployment. If you modify 
any database parameters, you must recreate the databases. To do this, remove 
the directories specified by the `MONGO_LOCAL_DATA` and `PG_LOCAL_DATA` environment variables and 
restart the Docker Compose deployment.

### Stopping the Deployment

To stop all started containers, use the following command:

```bash
COMPOSE_PROFILES=mov docker-compose down
```

## Development Environment

This section details how to set up and interact with the development environment
for the C0 Patient treatment UI component.

### Setting Up the Environment

1.  **Start the Development Environment:** Open your terminal and execute the 
following script:

```bash
./startDevelopmentEnvironment.sh
```

This script launches a Bash shell configured for development.  All subsequent commands 
should be run within this development shell.

2.  **Start the development server (optional):** Inside the development shell, start 
the development server:

```bash
startServer
```

This command starts the C0 Patient Treatment UI in development mode, enabling hot reloading 
for code changes. Access the UI at [http://localhost:8080](http://localhost:8080).


### Running Tests

You can run tests using Maven:

* **Run all tests:**

```bash
mvn test
```

* **Run tests with debugging enabled:**

```bash
vnd tes
```

* **Run tests using the existing Master of VALAWAI (MOV) instance:**

```bash
mvn -DuseDevMOV=true test
```

* **Run tests using the existing PostgreSQL instance:**

```bash
mvn -DuseDevDatabase=true test
```

### Development Environment Components

The development environment script launches the following services:

* **RabbitMQ:** A message broker facilitating communication between components. Access 
the management interface at [http://localhost:8081](http://localhost:8081) using the 
credentials `mov:password`.

* **MongoDB:** A NoSQL database used by the MOV. The database name is `movDB`, and 
the default credentials are `mov:password`.

* **Mongo Express:** A web interface for managing the MongoDB database. Access it at 
[http://localhost:8082](http://localhost:8082) using credentials `mov:password`.

* **Master of VALAWAI (MOV):** Manages network topology and component connections. If running,
 access the MOV UI at [http://localhost:8084](http://localhost:8084).

* **PGAdmin (PostgreSQL Administration):** A web interface for managing the PostgreSQL database. 
Access it at [http://localhost:8083](http://localhost:8083). Use the following credentials:

    * **Login:** `pg_admin@valawai.eu:password`
    * **Server Configuration:**
        * **Database Name:** `c0_patient_treatment_ui_db`
        * **Host:** `host.docker.interbal`
        * **Port:** `5432`
        * **Username:** `c0_patient_treatment_ui`
        * **Password:** `password`

This development environment provides a pre-configured infrastructure for developing, testing, 
and debugging the C0 Patient Treatment UI component, streamlining the development process 
and enabling efficient iteration.

## Links

 - [C0 Patient treatment UI](https://valawai.github.io/docs/components/C0/patient_treatment_ui)
 - [Master Of VALAWAI](https://valawai.github.io/docs/architecture/implementations/mov/)
 - [VALWAI documentation](https://valawai.github.io/docs/)
 - [VALAWAI project web site](https://valawai.eu/)
 - [Twitter](https://twitter.com/ValawaiEU)
 - [GitHub](https://github.com/VALAWAI)