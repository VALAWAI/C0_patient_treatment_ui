/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteriaTest;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModelTestCase;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;

/**
 * Test the {@link TreatmentToAdd}.
 *
 * @see TreatmentToAdd
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class TreatmentToAddTest extends ReflectionModelTestCase<TreatmentToAdd> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreatmentToAdd createEmptyModel() {

		return new TreatmentToAdd();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(TreatmentToAdd model) {

		model.patientId = ValueGenerator.rnd().nextLong();
		model.beforeStatus = new PatientStatusCriteriaTest().nextModel();
		model.actions = new ArrayList<>(Arrays.asList(TreatmentAction.values()));
		Collections.shuffle(model.actions, ValueGenerator.rnd());
		final var max = ValueGenerator.rnd().nextInt(1, model.actions.size());
		model.actions = model.actions.subList(0, max);
		model.expectedStatus = new PatientStatusCriteriaTest().nextModel();
	}

}
