/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test the {@link IsAlignValueValidator}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class IsAlignValueValidatorTest {

	/**
	 * Check that some values are not valid.
	 *
	 * @param value to validate.
	 */
	@ParameterizedTest(name = "The {0} should not be valid.")
	@NullSource
	@ValueSource(doubles = { -1.000000001, -100, 1.00000001, 100 })
	public void shouldNotBeValid(Number value) {

		final var validator = new IsAlignValueValidator();
		assertFalse(validator.isValid(value, null), "Unexpected that " + value + " will be not valid");

	}

	/**
	 * Check that some values are valid.
	 *
	 * @param value to validate.
	 */
	@ParameterizedTest(name = "The {0} should be valid.")
	@ValueSource(doubles = { -1.0, -0.9999999999, -0.0, 0.0, 0.9999999999, 1.0 })
	public void shouldBeValid(Number value) {

		final var validator = new IsAlignValueValidator();
		assertTrue(validator.isValid(value, null), "Unexpected that " + value + " will be valid");

	}

}
