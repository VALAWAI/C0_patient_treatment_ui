/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.models;

/**
 * The level of therapeutic intensity.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum NITLevel {

	/**
	 * It includes all possible measures to prolong survival
	 */
	ONE,

	/**
	 * Includes all possible measures except CPR.
	 */
	TWO_A,

	/**
	 * Includes all possible measures except CPR and ICU.
	 */
	TWO_B,

	/**
	 * Includes complementary scans and non-invasive treatments.
	 */
	THREE,

	/**
	 * It includes empiric symptomatic treatments according to clinical suspicion,
	 * which can be agreed as temporary.
	 */
	FOUR,

	/**
	 * No complementary examinations or etiological treatments are carried out, only
	 * treatments for comfort.
	 */
	FIVE;

}
