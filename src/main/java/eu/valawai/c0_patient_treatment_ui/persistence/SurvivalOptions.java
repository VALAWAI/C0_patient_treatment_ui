/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

/**
 * The possible survival options.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum SurvivalOptions {

	/**
	 * The survival is less than 12 month
	 */
	LESS_THAN_12_MONTHS,

	/**
	 * The survival is more than 12 month
	 */
	MORE_THAN_12_MONTHS,

	/**
	 * The survival is unknown.
	 */
	UNKNOWN;

}
