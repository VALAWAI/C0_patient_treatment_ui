/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import eu.valawai.c0_patient_treatment_ui.ReflectionModel;
import jakarta.persistence.Embeddable;

/**
 * An entity that stores the status of a patient by some criteria.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Embeddable
@Schema(title = "The status of a patient by some criteria.")
public class PatientStatusCriteria extends ReflectionModel {

	/**
	 * The range of age of the patient status.
	 */
	@Schema(title = "The range of age of the patient status.")
	public AgeRangeOption ageRange;

	/**
	 * Check if the patient status has a Complex Cronic Disease (CCD).
	 */
	@Schema(title = "Check if the patient status has a Complex Cronic Disease (CCD).")
	public YesNoUnknownOption ccd;

	/**
	 * A MACA patient status has answered no to the question: Would you be surprised
	 * if this patient died in less than 12 months?
	 */
	@Schema(title = "A MACA patient status has answered no to the question: Would you be surprised if this patient died in less than 12 months?")
	public YesNoUnknownOption maca;

	/**
	 * The expected survival time for the patient status.
	 */
	@Schema(title = "The expected survival time for the patient status.")
	public SurvivalOptions expectedSurvival;

	/**
	 * The fragility index of the patient status.
	 */
	@Schema(title = "The fragility index of the patient status.")
	public SPICT_Scale frailVIG;

	/**
	 * The clinical risk group of the patient status.
	 */
	@Schema(title = "The clinical risk group of the patient status.")
	public ClinicalRiskGroupOption clinicalRiskGroup;

	/**
	 * Check if the patient status has social support.
	 */
	@Schema(title = "Check if the patient status has social support.")
	public YesNoUnknownOption hasSocialSupport;

	/**
	 * The independence for basic activities of daily living at admission.
	 */
	@Schema(title = "The independence for basic activities of daily living at admission.")
	public BarthelIndex independenceAtAdmission;

	/**
	 * The index that measures the independence for instrumental activities.
	 */
	@Schema(title = "The index that measures the independence for instrumental activities.")
	public LawtonIndex independenceInstrumentalActivities;

	/**
	 * The answers to the question: Does the patient status have advance directives?
	 */
	@Schema(title = "The answers to the question: Does the patient status have advance directives?")
	public YesNoUnknownOption hasAdvanceDirectives;

	/**
	 * The answers to the question: Is the patient status competent to understand
	 * the instructions of health personnel?
	 */
	@Schema(title = "The answers to the question: Is the patient status competent to understand the instructions of health personnel?")
	public YesNoUnknownOption isCompetent;

	/**
	 * The answers to the question: To the patient status or his/her referent
	 * authorized has been informed of possible treatments and the consequences of
	 * receiving it or No.
	 */
	@Schema(title = "The answers to the question: To the patient status or his/her referent authorized has been informed of possible treatments and the consequences of receiving it or No.")
	public YesNoUnknownOption hasBeenInformed;

	/**
	 * The answers to the question: Is it detected that the patient status has seen
	 * coerced/pressured by third parties?
	 */
	@Schema(title = "The answers to the question: Is it detected that the patient status has seen coerced/pressured by third parties?")
	public YesNoUnknownOption isCoerced;

	/**
	 * Inform if the patient status has cognitive impairment.
	 */
	@Schema(title = "Inform if the patient status has cognitive impairment.")
	public CognitiveImpairmentLevel hasCognitiveImpairment;

	/**
	 * Inform if the patient status has emotional pain.
	 */
	@Schema(title = "Inform if the patient status has emotional pain.")
	public YesNoUnknownOption hasEmocionalPain;

	/**
	 * Describe the degree of discomfort of the patient status before applying any
	 * action.
	 */
	@Schema(title = "Describe the degree of discomfort of the patient status before applying any action.")
	public DiscomfortDegree discomfortDegree;

}
