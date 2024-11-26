/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import com.fasterxml.jackson.annotation.JsonRootName;

import eu.valawai.c0_patient_treatment_ui.models.AgeRangeOption;
import eu.valawai.c0_patient_treatment_ui.models.BarthelIndex;
import eu.valawai.c0_patient_treatment_ui.models.ClinicalRiskGroupOption;
import eu.valawai.c0_patient_treatment_ui.models.CognitiveImpairmentLevel;
import eu.valawai.c0_patient_treatment_ui.models.DiscomfortDegree;
import eu.valawai.c0_patient_treatment_ui.models.NITLevel;
import eu.valawai.c0_patient_treatment_ui.models.SPICT_Scale;
import eu.valawai.c0_patient_treatment_ui.models.SurvivalOptions;

/**
 * The status of a patient by some criteria.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@JsonRootName("patient_status_criteria_payload")
public class PatientStatusCriteriaPayload extends Payload {

	/**
	 * The range of age of the patient status.
	 */
	public AgeRangeOption age_range;

	/**
	 * Check if the patient status has a Complex Cronic Disease (CCD).
	 */
	public Boolean ccd;

	/**
	 * A MACA patient status has answered no to the question: Would you be surprised
	 * if this patient died in less than 12 months?
	 */
	public Boolean maca;

	/**
	 * The expected survival time for the patient status.
	 */
	public SurvivalOptions expected_survival;

	/**
	 * The fragility index of the patient status.
	 */
	public SPICT_Scale frail_VIG;

	/**
	 * The clinical risk group of the patient status.
	 */
	public ClinicalRiskGroupOption clinical_risk_group;

	/**
	 * Check if the patient status has social support.
	 */
	public Boolean has_social_support;

	/**
	 * The independence for basic activities of daily living at admission.
	 */
	public BarthelIndex independence_at_admission;

	/**
	 * The index that measures the independence for instrumental activities.
	 */
	public Integer independence_instrumental_activities;

	/**
	 * The answers to the question: Does the patient status have advance directives?
	 */
	public Boolean has_advance_directives;

	/**
	 * The answers to the question: Is the patient status competent to understand
	 * the instructions of health personnel?
	 */
	public Boolean is_competent;

	/**
	 * The answers to the question: To the patient status or his/her referent
	 * authorized has been informed of possible treatments and the consequences of
	 * receiving it or No.
	 */
	public Boolean has_been_informed;

	/**
	 * The answers to the question: Is it detected that the patient status has seen
	 * coerced/pressured by third parties?
	 */
	public Boolean is_coerced;

	/**
	 * Inform if the patient status has cognitive impairment.
	 */
	public CognitiveImpairmentLevel has_cognitive_impairment;

	/**
	 * Inform if the patient status has emotional pain.
	 */
	public Boolean has_emocional_pain;

	/**
	 * Describe the degree of discomfort of the patient status before applying any
	 * action.
	 */
	public DiscomfortDegree discomfort_degree;

	/**
	 * Describe the level of therapeutic intensity of the patient
	 */
	public NITLevel nit_level;

}
