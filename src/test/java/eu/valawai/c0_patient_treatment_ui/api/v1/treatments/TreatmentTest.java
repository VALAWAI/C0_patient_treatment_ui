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

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteriaTest;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModelTestCase;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;

/**
 * Test the {@link Treatment}.
 *
 * @see Treatment
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class TreatmentTest extends ReflectionModelTestCase<Treatment> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Treatment createEmptyModel() {

		return new Treatment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(Treatment model) {

		model.id = ValueGenerator.rnd().nextLong();
		model.createdTime = ValueGenerator.rnd().nextLong(0, TimeManager.now() - 360000);
		model.beforeStatus = new PatientStatusCriteriaTest().nextModel();
		model.treatmentActions = new ArrayList<>(Arrays.asList(TreatmentAction.values()));
		Collections.shuffle(model.treatmentActions, ValueGenerator.rnd());
		final var max = ValueGenerator.rnd().nextInt(0, model.treatmentActions.size());
		if (max > 0) {

			model.treatmentActions = model.treatmentActions.subList(0, max);

		} else {

			model.treatmentActions = null;
		}
		model.expectedStatus = new PatientStatusCriteriaTest().nextModel();
	}

}
