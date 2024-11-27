/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages.mov;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
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
	 * The service to assert that the log.
	 */
	@Inject
	LogAsserts logAsserts;

	/**
	 * Test debug message.
	 */
	@Test
	public void shouldSendDebugMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 1";
		this.service.debug("Test {0} {1}", value, 1);
		this.logAsserts
				.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.DEBUG).and(LogAsserts.withLogMessage(message)));

	}

	/**
	 * Test warn message.
	 */
	@Test
	public void shouldSendWarnMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 1";
		this.service.warning("Test {0} {1}", value, 1);
		this.logAsserts
				.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.WARN).and(LogAsserts.withLogMessage(message)));

	}

	/**
	 * Test error message.
	 */
	@Test
	public void shouldSendErrorMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 2";
		this.service.error("Test {0} {1}", value, 2);
		this.logAsserts
				.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.ERROR).and(LogAsserts.withLogMessage(message)));

	}

	/**
	 * Test info message.
	 */
	@Test
	public void shouldSendInfoMessage() {

		final var value = UUID.randomUUID().toString();
		final var message = "Test " + value + " 3";
		this.service.info("Test {0} {1}", value, 3);
		this.logAsserts
				.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.INFO).and(LogAsserts.withLogMessage(message)));

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
		this.logAsserts.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.DEBUG)
				.and(LogAsserts.withLogMessage(message).and(LogAsserts.withLogPayload(payload))));

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
		this.logAsserts.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.INFO)
				.and(LogAsserts.withLogMessage(message).and(LogAsserts.withLogPayload(payload))));

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
		this.logAsserts.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.WARN)
				.and(LogAsserts.withLogMessage(message).and(LogAsserts.withLogPayload(payload))));

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
		this.logAsserts.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.ERROR)
				.and(LogAsserts.withLogMessage(message).and(LogAsserts.withLogPayload(payload))));

	}

}
