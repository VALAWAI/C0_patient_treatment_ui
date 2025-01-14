/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages.mov;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.function.Predicate;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * The assertion methods over the log on the MOV.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public interface LogAsserts {

	/**
	 * Filter to match the log level.
	 *
	 * @param level of the log.
	 *
	 * @return a filter to match the level.
	 */
	public static Predicate<JsonObject> withLogLevel(LogLevel level) {

		return log -> level.name().equals(log.getString("level"));

	}

	/**
	 * Filter to match the log message.
	 *
	 * @param message of the log.
	 *
	 * @return a filter to match the message.
	 */
	public static Predicate<JsonObject> withLogMessage(String message) {

		return log -> message.equals(log.getString("message"));

	}

	/**
	 * Filter to match the log message contains a part.
	 *
	 * @param part to be in the log message.
	 *
	 * @return a filter to match the message.
	 */
	public static Predicate<JsonObject> withLogMessageContains(String part) {

		return log -> {

			final var msg = log.getString("message");
			return msg != null && msg.contains(part);

		};

	}

	/**
	 * Filter to match the log payload.
	 *
	 * @param payload of the log.
	 *
	 * @return a filter to match the payload.
	 */
	public static Predicate<JsonObject> withLogPayload(Object payload) {

		return log -> {
			final var found = log.getString("payload");
			if (found != null) {

				final var decoded = Json.decodeValue(found, payload.getClass());
				return payload.equals(decoded);

			} else {

				return false;
			}
		};

	}

	/**
	 * Return the last log that satisfy the filter.
	 *
	 * @param filter to check the log message.
	 *
	 * @return the log message that satisfy the filter or {@code null} if any found.
	 */
	public default JsonObject findLastLog(Predicate<JsonObject> filter) {

		final var logs = this.findLastLogs();
		if (logs != null) {

			final var max = logs.size();
			for (var i = 0; i < max; i++) {

				final var log = logs.getJsonObject(i);
				if (log != null && filter.test(log)) {

					return log;

				}
			}
		}

		return null;

	}

	/**
	 * Search for the last log messages.
	 *
	 * @return the last log messages.
	 */
	public JsonArray findLastLogs();

	/**
	 * Wait until found a log message that satisfy the filter.
	 *
	 * @param filter to check the log message.
	 *
	 * @return the log message that satisfy the filter or fail if not found in 30
	 *         seconds.
	 */
	public default JsonObject waitUntiLogMatch(Predicate<JsonObject> filter) {

		return this.waitUntiLogMatch(filter, Duration.ofSeconds(30));
	}

	/**
	 * Wait until found a log message that satisfy the filter.
	 *
	 * @param filter   to check the log message.
	 * @param duration maximum time to wait.
	 *
	 * @return the log message that satisfy the filter or fail if not found in the
	 *         specified time.
	 */
	public default JsonObject waitUntiLogMatch(Predicate<JsonObject> filter, Duration duration) {

		final var deadline = System.currentTimeMillis() + duration.toMillis();
		while (System.currentTimeMillis() < deadline) {

			final var log = this.findLastLog(filter);
			if (log != null) {

				return log;
			}
		}
		fail("Not found log message that match the filter.");
		return null;
	}

}
