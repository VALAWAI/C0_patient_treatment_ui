/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/
package eu.valawai.c0_patient_treatment_ui.persistence;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

/**
 * The resource that start a docker container with a PostgreSQL database.
 *
 * @author VALAWAI
 */
public class PostgreSQLTestResource implements QuarkusTestResourceLifecycleManager {

	/**
	 * The name of the PostgreSQL docker container to use.
	 */
	public static final String POSTGRESQL_DOCKER_NAME = "postgres:17";

	/**
	 * The PostgreSQL service container.
	 */
	@SuppressWarnings("resource")
	static GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse(POSTGRESQL_DOCKER_NAME))
			.withStartupAttempts(1).withEnv("POSTGRES_USER", "c0_patient_treatment_ui")
			.withEnv("POSTGRES_PASSWORD", "password").withEnv("POSTGRES_DB", "c0_patient_treatment_ui_db")
			.withExposedPorts(5432).waitingFor(Wait.forListeningPort());

	/**
	 * Start the mocked server.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> start() {

		if (Boolean.parseBoolean(System.getProperty("useDevDatabase"))) {

			return Collections.singletonMap("quarkus.datasource.reactive.url",
					"vertx-reactive:postgresql://host.docker.internal:5432/c0_patient_treatment_ui_db");

		} else {

			container.start();
			return Collections.singletonMap("quarkus.datasource.reactive.url", "vertx-reactive:postgresql://"
					+ container.getHost() + ":" + container.getMappedPort(5432) + "/c0_patient_treatment_ui_db");

		}

	}

	/**
	 * Stop the mocked server.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {

		if (container.isRunning()) {

			container.close();

		}
	}

}
