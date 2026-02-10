# C0_patient_treatment_ui

The Patient Treatment UI (C0) is a web-based interface that simulates doctor-hospital interactions, enabling medical professionals to manage patient records and define treatment plans. It serves as a combined sensor and actuator that receives real-time feedback on NIT protocol adherence (status: ALLOW, DENY, or UNKNOWN) and displays treatment alignment with core values such as autonomy, beneficence, justice, and non-maleficence. This integrated feedback loop allows doctors to monitor clinical compliance and ethical trade-offs directly within the simulated environment.

## Summary

- **Type**: [C0](https://valawai.github.io/docs/components/C0/)
- **Name**: Patient treatment UI
- **Documentation**: [https://valawai.github.io/docs/components/C0/patient_treatment_ui](https://valawai.github.io/docs/components/C0/patient_treatment_ui)
- **Versions**:
  - **Stable version**: [1.2.0 (February 10, 2026)](https://github.com/VALAWAI/C0_patient_treatment_ui/tree/1.2.0)
  - **API**: [1.0.1 (April 30,2025)](https://raw.githubusercontent.com/VALAWAI/C0_patient_treatment_ui/ASYNCAPI_1.0.1/asyncapi.yml)
  - **Required MOV API**: [1.2.0 (March 9, 2024)](https://raw.githubusercontent.com/valawai/MOV/ASYNCAPI_1.2.0/asyncapi.yml)
- **Developed By**: [IIIA-CSIC](https://www.iiia.csic.es)
- **License**: [GPL v3](LICENSE)
- **Technology Readiness Level (TLR)**: [3](https://valawai.github.io/docs/components/C0/patient_treatment_ui/tlr)


## Usage

To use the Patient Treatment UI (C0) as a medical professional, 
follow these steps to manage care and monitor ethical alignment:

- **Patient Management**: Use the interface to register new patients, 
search the database, or update medical histories and current conditions.
- **Define Treatment**: Create a personalized treatment plan by selecting 
specific medical actions (e.g., CPR, dialysis, or surgery) and documenting 
the patient's status via a clinical questionnaire.
- **Monitor NIT Protocol**: Review the real-time "Action Feedback" to verify 
if your proposed treatment complies with medical standards. The system will 
flag actions as ALLOW (compliant), DENY (prohibited), or UNKNOWN (insufficient
data) based on the patient's assigned NIT (Therapeutic Intensity Level).
- **Evaluate Ethical Values**: Check the "Value Feedback" section to see how 
the treatment aligns with core principles like Autonomy, Beneficence, Justice, 
and Non-maleficence. The UI displays alignment scores between -1 and 1 to help 
you identify and balance potential ethical trade-offs.

 
## Deployment

The **C0 Patient treatment UI** is designed to run as a Docker container, working within 
the [Master Of VALAWAI (MOV)](https://valawai.github.io/docs/architecture/implementations/mov) ecosystem. 
For a complete guide, including advanced setups, refer to 
the [component's full deployment documentation](https://valawai.github.io/docs/components/C0/patient_treatment_ui/deploy).

Here's how to quickly get it running:

1. ### Build the Docker Image

    First, you need to build the Docker image. Go to the project's root directory and run:

    ```bash
    ./buildDockerImages.sh -t latest
    ```

    This creates the `valawai/c0_patient_treatment_ui:latest` Docker image, which is referenced in the `docker-compose.yml` file.

2. ### Start the Component

    You have two main ways to start the component:

    A. **With MOV and Mail Catcher (for testing):**
    To run the C0 E-mail Actuator with the MOV and a local email testing tool (Mail Catcher), use:

    ```bash
    COMPOSE_PROFILES=all docker compose up -d
    ```

    Once started, you can access:

    - **MOV:** [http://localhost:8081](http://localhost:8081)
    - **RabbitMQ UI:** [http://localhost:8082](http://localhost:8082) (credentials: `mov:password`)
    - **Mail Catcher UI:** [http://localhost:8083](http://localhost/8083)

    B. **As a Standalone Component (connecting to an existing MOV/RabbitMQ):**
    If you already have MOV running or want to connect to a remote RabbitMQ, you'll need a [`.env` file](https://docs.docker.com/compose/environment-variables/env-file/) with connection details. Create a `.env` file in the same directory as your `docker-compose.yml` like this:

    ```properties
    MOV_MQ_HOST=host.docker.internal
    MOV_MQ_USERNAME=mov
    MOV_MQ_PASSWORD=password
    C0_patient_treatment_ui_PORT=9080
    MAIL_WEB=9083
    ```

    Find full details on these and other variables in the [component's dedicated deployment documentation](https://valawai.github.io/docs/components/C0/patient_treatment_ui/deploy).
    Once your `.env` file is configured, start only the email actuator and mail catcher (without MOV) using:

    ```bash
    COMPOSE_PROFILES=mail,component docker compose up -d
    ```

## Development environment

To ensure a consistent and isolated development experience, this component is configured
to use Docker. This approach creates a self-contained environment with all the necessary
software and tools for building and testing, minimizing conflicts with your local system
and ensuring reproducible results.

You can launch the development environment by running this script:

```bash
./startDevelopmentEnvironment.sh
```

Once the environment starts, you'll find yourself in a bash shell, ready to interact with
the Quarkus development environment. You'll also have access to the following integrated tools:

- **Master of VALAWAI**: The central component managing topology connections between services.
 Its web interface is accessible at [http://localhost:8081](http://localhost:8081).
- **RabbitMQ** The message broker for inter-component communication. The management web interface
 is at [http://localhost:8082](http://localhost:8082), with credentials `mov**:**password`.
- **MongoDB**: The database used by the MOV, named `movDB`, with user credentials `mov:password`.
- **Mongo express**: A web interface for interacting with MongoDB, available at
 [http://localhost:8084](http://localhost:8084), also with credentials `mov**:**password`.
- **PostgreSQL**: The database used by the Patient Treatment UI, named `c0_patient_treatment_ui_db`, 
with user credentials `c0_patient_treatment_ui:password`.
- **PGAdmin (PostgreSQL Administration):** A web interface for managing the PostgreSQL database. 
Access it at [http://localhost:8083](http://localhost:8083). Use the following credentials:

    * **Login:** `pg_admin@valawai.eu:password`
    * **Server Configuration:**
        * **Database Name:** `c0_patient_treatment_ui_db`
        * **Host:** `host.docker.interbal`
        * **Port:** `5432`
        * **Username:** `c0_patient_treatment_ui`
        * **Password:** `password`
  

Within this console, you can use the official [`quarkus` client](https://quarkus.io/guides/cli-tooling#using-the-cli)
or any of these convenient commands:

- `startServer`: To initiate the development server.
- `mvn clean`: To clean the project (compiled and generated code).
- `mvn test`: To run all project tests.
- `mvn -DuseDevMOV=true test`: To execute tests using the already started Master of VALAWAI instance,
 rather than an independent container.
  
To exit the development environment, simply type `exit` in the bash shell or run the following script:

```bash
./stopDevelopmentEnvironment.sh
```

In either case, the development environment will gracefully shut down, including all activated services
like MOV, RabbitMQ, MongoDB, Mongo Express, PostgreSQL, and the PGAdmin.


## Helpful Links

Here's a collection of useful links related to this component and the VALAWAI ecosystem:

- **C0 Patient treatement UI Documentation**: [https://valawai.github.io/docs/components/C0/patient_treatment_ui](https://valawai.github.io/docs/components/C0/patient_treatment_ui)
- **Master Of VALAWAI (MOV)**: [https://valawai.github.io/docs/architecture/implementations/mov/](https://valawai.github.io/docs/architecture/implementations/mov/)
- **VALAWAI Main Documentation**: [https://valawai.github.io/docs/](https://valawai.github.io/docs/)
- **VALAWAI on GitHub**: [https://github.com/VALAWAI](https://github.com/VALAWAI)
- **VALAWAI Official Website**: [https://valawai.eu/](https://valawai.eu/)
- **VALAWAI on X (formerly Twitter)**: [https://x.com/ValawaiEU](https://x.com/ValawaiEU)
