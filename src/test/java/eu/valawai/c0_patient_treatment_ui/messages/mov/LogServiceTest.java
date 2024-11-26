/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages.mov;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;

/**
 * Test the log service.
 *
 * @see LogService
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@WithTestResource(value = MOVTestResource.class)
public class LogServiceTest {

	/**
	 * The service to test.
	 */
	@Inject
	LogService service;

	/**
	 * The port where teh Master Of VALAWAI is listening.
	 */
	@ConfigProperty(name = MOVTestResource.MOV_URL_CONFIG_PROPERTY_NAME, defaultValue = "http://host.docker.internal:8084")
	String movUrl;

	/**
	 * Test debug message.
	 */
	@Test
	public void shouldSendDebugMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 1";
		this.service.debug("Test {0} {1}", value, 1);
		this.checkLogStored(LogLevel.DEBUG, message, null, Duration.ofSeconds(30));

	}

	/**
	 * Test warn message.
	 */
	@Test
	public void shouldSendWarnMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 1";
		this.service.warning("Test {0} {1}", value, 1);
		this.checkLogStored(LogLevel.WARN, message, null, Duration.ofSeconds(30));

	}

	/**
	 * Test error message.
	 */
	@Test
	public void shouldSendErrorMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 2";
		this.service.error("Test {0} {1}", value, 2);
		this.checkLogStored(LogLevel.ERROR, message, null, Duration.ofSeconds(30));

	}

	/**
	 * Test info message.
	 */
	@Test
	public void shouldSendInfoMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 3";
		this.service.info("Test {0} {1}", value, 3);
		this.checkLogStored(LogLevel.INFO, message, null, Duration.ofSeconds(30));

	}

	/**
	 * Test debug message with payload.
	 */
	@Test
	public void shouldSendDebugWithPayloadMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 1";
		final var payload = new QueryComponentsPayload();
		this.service.debugWithPayload(payload, "Test {0} {1}", value, 1);
		this.checkLogStored(LogLevel.DEBUG, message, payload, Duration.ofSeconds(30));

	}

	/**
	 * Test info message with payload.
	 */
	@Test
	public void shouldSendInfoWithPayloadMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 1";
		final var payload = new QueryComponentsPayload();
		this.service.infoWithPayload(payload, "Test {0} {1}", value, 1);
		this.checkLogStored(LogLevel.INFO, message, payload, Duration.ofSeconds(30));

	}

	/**
	 * Test warning message with payload.
	 */
	@Test
	public void shouldSendWarningWithPayloadMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 1";
		final var payload = new QueryComponentsPayload();
		this.service.warningWithPayload(payload, "Test {0} {1}", value, 1);
		this.checkLogStored(LogLevel.WARN, message, payload, Duration.ofSeconds(30));

	}

	/**
	 * Test error message with payload.
	 */
	@Test
	public void shouldSendErrorWithPayloadMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 1";
		final var payload = new QueryComponentsPayload();
		this.service.errorWithPayload(payload, "Test {0} {1}", value, 1);
		this.checkLogStored(LogLevel.ERROR, message, payload, Duration.ofSeconds(30));

	}

	/**
	 * Check the log message is stored.
	 *
	 * @param level    of the log message.
	 * @param message  of teh log message.
	 * @param payload  of the log message.
	 * @param duration maximum time to wait the log is stored.
	 */
	protected void checkLogStored(LogLevel level, String message, Object payload, Duration duration) {

		final var deadline = System.currentTimeMillis() + duration.toMillis();
		while (System.currentTimeMillis() < deadline) {

			final var content = given().queryParam("order", "-timestamp").queryParam("limit", "50").when()
					.get(this.movUrl + "/v1/logs").then().statusCode(200).extract().asString();
			final var page = Json.decodeValue(content, JsonObject.class);
			final var logs = page.getJsonArray("logs");
			if (logs != null) {

				final var max = logs.size();
				for (var i = 0; i < max; i++) {

					final var log = logs.getJsonObject(i);
					if (log != null) {

						if (level == null || level.name().equals(log.getString("level"))) {

							if (message == null || message.equals(log.getString("message"))) {

								if (payload == null) {

									return;

								} else {

									final var found = log.getString("payload");
									if (found != null) {

										final var decoded = Json.decodeValue(found, payload.getClass());
										if (payload.equals(decoded)) {

											return;
										}
									}
								}
							}

						}

					}
				}
			}
		}

		fail("The log is not stored.");

	}

}
