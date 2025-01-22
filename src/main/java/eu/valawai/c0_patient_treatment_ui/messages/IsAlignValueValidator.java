/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validate that a {@link Number} is an align value.
 *
 * @see IsAlignValue
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class IsAlignValueValidator implements ConstraintValidator<IsAlignValue, Number> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid(final Number value, final ConstraintValidatorContext context) {

		if (value == null) {

			return false;

		} else {

			return Double.compare(-1.0d, value.doubleValue()) <= 0

					&& Double.compare(value.doubleValue(), 1.0d) <= 0;
		}
	}

}
