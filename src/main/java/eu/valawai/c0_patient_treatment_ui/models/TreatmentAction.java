/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.models;

/**
 * The possible action to do for a treatment.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum TreatmentAction {

	/**
	 * The patient can receive a Cardiopulmonary resuscitation.
	 */
	CPR,

	/**
	 * The patient need an organ transplant.
	 */
	TRANSPLANT,

	/**
	 * The patient can be in the intensive curing unit.
	 */
	ICU,

	/**
	 * The patient can have non-invasive mechanical ventilation.
	 */
	NIMV,

	/**
	 * The patient can receive vasoactive drugs.
	 */
	VASOACTIVE_DRUGS,

	/**
	 * The patient can have dialysis.
	 */
	DIALYSIS,

	/**
	 * The patient can receive simple clinical trials.
	 */
	SIMPLE_CLINICAL_TRIAL,

	/**
	 * The patient can receive medium clinical trials.
	 */
	MEDIUM_CLINICAL_TRIAL,

	/**
	 * The patient can receive advanced clinical trials.
	 */
	ADVANCED_CLINICAL_TRIAL,

	/**
	 * The patient can have palliative surgery.
	 */
	PALLIATIVE_SURGERY,

	/**
	 * The patient can have surgery with the intention of curing.
	 */
	CURE_SURGERY;
}
