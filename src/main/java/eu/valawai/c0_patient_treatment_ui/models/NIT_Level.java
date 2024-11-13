/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.models;

/**
 * The Nit level.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum NIT_Level {

	/**
	 * The NIT level 1 is when the patient can have unlimited treatments.
	 */
	LEVEL_1,

	/**
	 * The NIT level 2A is when the patient can have intense treatment except RCP.
	 */
	LEVEL_2A,

	/**
	 * The NIT level 2B is when the patient can have intense treatments.
	 */
	LEVEL_2B,

	/**
	 * The NIT level 3 is when the patient can have intermediate intensity
	 * treatments.
	 */
	LEVEL_3,

	/**
	 * The NIT level 4 is when the patient can have symptomatic conservative
	 * treatments.
	 */
	LEVEL_4,

	/**
	 * The NIT level 5 is when the patient can have exclusively comfort measures.
	 */
	LEVEL_5;
}
