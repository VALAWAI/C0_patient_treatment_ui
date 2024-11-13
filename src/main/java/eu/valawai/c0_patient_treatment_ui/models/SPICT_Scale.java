/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.models;

/**
 * It helps identify the most fragile people who have one or more health
 * problems. It is based on the comprehensive geriatric assessment (applicable
 * to non-geriatric patients) that evaluates areas such as functional
 * independence, nutritional status, cognitive, emotional, social, geriatric
 * syndromes (confusion syndrome, falls, ulcers, polypharmacy, dysphagia),
 * symptoms (pain or dyspnea) and diseases oncological, respiratory, cardiac,
 * neurological, digestive or renal).
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum SPICT_Scale {

	/**
	 * The low option of the SPICT scale.
	 */
	LOW,

	/**
	 * The moderate option of the SPICT scale.
	 */
	MODERATE,

	/**
	 * The high option of the SPICT scale.
	 */
	HIGH,

	/**
	 * The level in the SPICT scale.
	 */
	UNKNOWN;

}
