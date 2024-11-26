/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.AgeRangeOption;
import eu.valawai.c0_patient_treatment_ui.models.BarthelIndex;
import eu.valawai.c0_patient_treatment_ui.models.ClinicalRiskGroupOption;
import eu.valawai.c0_patient_treatment_ui.models.CognitiveImpairmentLevel;
import eu.valawai.c0_patient_treatment_ui.models.DiscomfortDegree;
import eu.valawai.c0_patient_treatment_ui.models.NITLevel;
import eu.valawai.c0_patient_treatment_ui.models.SPICT_Scale;
import eu.valawai.c0_patient_treatment_ui.models.SurvivalOptions;

/**
 * Test the {@link PatientStatusCriteriaPayload}.
 *
 * @see PatientStatusCriteriaPayload
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class PatientStatusCriteriaPayloadTest extends PayloadTestCase<PatientStatusCriteriaPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PatientStatusCriteriaPayload createEmptyModel() {

		return new PatientStatusCriteriaPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(PatientStatusCriteriaPayload model) {

		model.age_range = ValueGenerator.next(AgeRangeOption.values());
		model.ccd = ValueGenerator.nextBoolean();
		model.clinical_risk_group = ValueGenerator.next(ClinicalRiskGroupOption.values());
		model.discomfort_degree = ValueGenerator.next(DiscomfortDegree.values());
		model.expected_survival = ValueGenerator.next(SurvivalOptions.values());
		model.frail_VIG = ValueGenerator.next(SPICT_Scale.values());
		model.has_advance_directives = ValueGenerator.nextBoolean();
		model.has_been_informed = ValueGenerator.nextBoolean();
		model.has_cognitive_impairment = ValueGenerator.next(CognitiveImpairmentLevel.values());
		model.has_emocional_pain = ValueGenerator.nextBoolean();
		model.has_social_support = ValueGenerator.nextBoolean();
		model.independence_at_admission = ValueGenerator.next(BarthelIndex.values());
		model.independence_instrumental_activities = ValueGenerator.nextInteger(8);
		model.is_coerced = ValueGenerator.nextBoolean();
		model.is_competent = ValueGenerator.nextBoolean();
		model.maca = ValueGenerator.nextBoolean();
		model.nit_level = ValueGenerator.next(NITLevel.values());
	}

}
