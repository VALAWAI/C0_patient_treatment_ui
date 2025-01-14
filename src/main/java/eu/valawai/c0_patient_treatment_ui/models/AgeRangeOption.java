/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.models;

import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;

/**
 * The different ranges of ages for a {@link PatientEntity}.
 *
 * @see PatientStatusCriteria#ageRange
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum AgeRangeOption {

	/**
	 * The age is between 0 and 19 years old.
	 */
	AGE_BETWEEN_0_AND_19,

	/**
	 * The age is between 20 and 29 years old.
	 */
	AGE_BETWEEN_20_AND_29,

	/**
	 * The age is between 30 and 39 years old.
	 */
	AGE_BETWEEN_30_AND_39,

	/**
	 * The age is between 40 and 49 years old.
	 */
	AGE_BETWEEN_40_AND_49,

	/**
	 * The age is between 50 and 59 years old.
	 */
	AGE_BETWEEN_50_AND_59,

	/**
	 * The age is between 60 and 69 years old.
	 */
	AGE_BETWEEN_60_AND_69,

	/**
	 * The age is between 70 and 79 years old.
	 */
	AGE_BETWEEN_70_AND_79,

	/**
	 * The age is between 80 and 89 years old.
	 */
	AGE_BETWEEN_80_AND_89,

	/**
	 * The age is between 90 and 99 years old.
	 */
	AGE_BETWEEN_90_AND_99,

	/**
	 * The age greater than 99 years old.
	 */
	AGE_MORE_THAN_99;
}
