/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

/**
 * This index allow to check the functional independence for basic activities.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum BarthelIndex {

	/**
	 * When the functional independence is between 0 and 20%.
	 */
	TOTAL,

	/**
	 * When the functional independence is between 21 and 60%.
	 */
	SEVERE,

	/**
	 * When the functional independence is between 61 and 90%.
	 */
	MODERATE,

	/**
	 * When the functional independence is between 91 and 99%.
	 */
	MILD,

	/**
	 * When the functional independence is 100%.
	 */
	INDEPENDENT,

	/**
	 * The index is unknown.
	 */
	UNKNOWN;

}
