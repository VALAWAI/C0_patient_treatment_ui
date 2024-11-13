/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.models;

/**
 * The possible clinical risk groups.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum ClinicalRiskGroupOption {

	/**
	 * The clinical risk group is promotion & prevention.
	 */
	PROMOTION_AND_PREVENTION,

	/**
	 * The clinical risk group is self-management support.
	 */
	SELF_MANAGEMENT_SUPPORT,

	/**
	 * The clinical risk group is illness management.
	 */
	ILLNESS_MANAGEMENT,

	/**
	 * The clinical risk group is case management.
	 */
	CASE_MANAGEMENT,

	/**
	 * The clinical risk group is unknown.
	 */
	UNKNOWN;

}
