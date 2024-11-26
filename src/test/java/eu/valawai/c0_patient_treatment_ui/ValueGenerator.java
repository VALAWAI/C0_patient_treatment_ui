/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * Used to generate values for the tests.
 *
 * @author VALAWAI
 */
public class ValueGenerator {

	/**
	 * Return the current version of the generator.
	 */
	private static final Random CURRENT = new Random(0);

	/**
	 * Create a new value generator.
	 */
	private ValueGenerator() {
	}

	/**
	 * Return the current generator.
	 *
	 * @return the current value generator.
	 */
	public static final Random rnd() {

		return CURRENT;
	}

	/**
	 * Return the next enumeration value to use.
	 *
	 * @param values to select the next value.
	 * @param <E>    the enumeration type that the values are defined.
	 *
	 * @return the next random enumeration value.
	 *
	 */
	public static final <E> E next(final E[] values) {

		final var index = CURRENT.nextInt(values.length);
		return values[index];

	}

	/**
	 * Return the next value from a string array.
	 *
	 * @param values to select the next value.
	 *
	 * @return the next random string value.
	 */
	public static final String next(final String... values) {

		final var index = CURRENT.nextInt(values.length);
		return values[index];

	}

	/**
	 * Return the next value from a list.
	 *
	 * @param values to select the next value.
	 * @param <E>    the element type that the values are defined.
	 *
	 * @return the next random element value.
	 *
	 */
	public static final <E> E next(final List<E> values) {

		final var index = CURRENT.nextInt(values.size());
		return values.get(index);

	}

	/**
	 * Return the next pattern with new values.
	 *
	 * @param pattern to instantiate.
	 * @param max     number maximum of values to replace.
	 *
	 * @return the pattern that has been replaced.
	 */
	public static String nextPattern(String pattern, int max) {

		final var args = new Object[max];
		for (var i = 0; i < max; i++) {

			final var value = CURRENT.nextInt(0, 999999);
			args[i] = String.format("%06d", value);

		}
		return MessageFormat.format(pattern, args);

	}

	/**
	 * Return the next single pattern.
	 *
	 * @param pattern to instantiate.
	 *
	 * @return the pattern that has been replaced.
	 */
	public static String nextPattern(String pattern) {

		return nextPattern(pattern, 1);

	}

	/**
	 * Return the next UUID.
	 *
	 * @return the next random UUID.
	 */
	public static UUID nextUUID() {

		return new UUID(CURRENT.nextLong(), CURRENT.nextLong());

	}

	/**
	 * Generate a new boolean value.
	 *
	 * @return the flip coin result..
	 */
	public static final boolean flipCoin() {

		return CURRENT.nextBoolean();

	}

	/**
	 * Return the next Json object.
	 *
	 * @return a random json object.
	 */
	public static JsonObject nextJsonObject() {

		final var json = new JsonObject();
		final var max = rnd().nextInt(0, 5);
		for (var i = 0; i < max; i++) {

			json.put("field_" + i, rnd().nextInt());
		}
		return json;

	}

	/**
	 * Return the next Json string.
	 *
	 * @return a random json encoded prettily in json.
	 */
	public static String nextJsonPretty() {

		final var json = nextJsonObject();
		return Json.encodePrettily(json);

	}

	/**
	 * Generate a random integer in the specified range.
	 *
	 * @param min value for the integer (inclusive).
	 * @param max value for the integer (exclusive).
	 *
	 * @return a random integer value in the range.
	 */
	public static int nextInt(int min, int max) {

		return CURRENT.nextInt(min, max);
	}

	/**
	 * Generate a random double value in the range [0,1].
	 *
	 * @return new double value in the range [0,1].
	 */
	public static Double nextDouble() {

		return CURRENT.nextDouble();
	}

	/**
	 * Return a random past time.
	 *
	 * @return a past time.
	 */
	public static long nextPastTime() {

		return CURRENT.nextLong(0, TimeManager.now());
	}

	/**
	 * Shuffle a list of values.
	 *
	 * @param values to be shuffled.
	 */
	public static void shuffle(List<?> values) {

		Collections.shuffle(values, CURRENT);
	}

	/**
	 * Return a boolean value or {@code null}.
	 *
	 * @return a boolean value or a {@code null}.
	 */
	public static Boolean nextBoolean() {

		return switch (CURRENT.nextInt(0, 3)) {
		case 0 -> Boolean.TRUE;
		case 1 -> Boolean.FALSE;
		default -> null;
		};
	}

	/**
	 * Return an integer value in the range [0,max] or {@code null}.
	 *
	 * @param max maximum integer value (exclusive) to return.
	 *
	 * @return the random integer in the range [0,max] or {@code null}.
	 */
	public static Integer nextInteger(int max) {

		final var value = CURRENT.nextInt(0, max + 2);
		if (value > max) {

			return null;

		} else {

			return value;
		}

	}

}
