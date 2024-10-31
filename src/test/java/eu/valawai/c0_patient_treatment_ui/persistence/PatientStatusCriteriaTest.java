/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import eu.valawai.c0_patient_treatment_ui.ReflectionModelTestCase;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;

/**
 * Test the {@link PatientStatusCriteria}.
 *
 * @see PatientStatusCriteria
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class PatientStatusCriteriaTest extends ReflectionModelTestCase<PatientStatusCriteria> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PatientStatusCriteria createEmptyModel() {

		return new PatientStatusCriteria();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(PatientStatusCriteria model) {

		model.ageRange = ValueGenerator.next(AgeRangeOption.values());
		model.ccd = ValueGenerator.next(YesNoUnknownOption.values());
		model.clinicalRiskGroup = ValueGenerator.next(ClinicalRiskGroupOption.values());
		model.discomfortDegree = ValueGenerator.next(DiscomfortDegree.values());
		model.expectedSurvival = ValueGenerator.next(SurvivalOptions.values());
		model.frailVIG = ValueGenerator.next(SPICT_Scale.values());
		model.hasAdvanceDirectives = ValueGenerator.next(YesNoUnknownOption.values());
		model.hasBeenInformed = ValueGenerator.next(YesNoUnknownOption.values());
		model.hasCognitiveImpairment = ValueGenerator.next(CognitiveImpairmentLevel.values());
		model.hasEmocionalPain = ValueGenerator.next(YesNoUnknownOption.values());
		model.hasSocialSupport = ValueGenerator.next(YesNoUnknownOption.values());
		model.independenceAtAdmission = ValueGenerator.next(BarthelIndex.values());
		model.independenceInstrumentalActivities = ValueGenerator.next(LawtonIndex.values());
		model.isCoerced = ValueGenerator.next(YesNoUnknownOption.values());
		model.isCompetent = ValueGenerator.next(YesNoUnknownOption.values());
		model.maca = ValueGenerator.next(YesNoUnknownOption.values());

	}

}
