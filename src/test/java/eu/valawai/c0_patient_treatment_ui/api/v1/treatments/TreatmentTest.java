/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import java.util.ArrayList;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatientTest;
import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteriaTest;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModelTestCase;

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
		model.patient = new MinPatientTest().nextModel();
		model.beforeStatus = new PatientStatusCriteriaTest().nextModel();
		model.expectedStatus = new PatientStatusCriteriaTest().nextModel();

		final int maxActions = ValueGenerator.rnd().nextInt(5);
		if (maxActions > 0) {

			model.actions = new ArrayList<>(maxActions);

			final var builder = new TreatmentActionWithFeedbackTest();
			for (int i = 0; i < maxActions; i++) {

				final var action = builder.nextModel();
				model.actions.add(action);

			}

		}

		final int maxValues = ValueGenerator.rnd().nextInt(5);
		if (maxValues > 0) {

			model.values = new ArrayList<>(maxValues);

			final var builder = new TreatmentValueTest();
			for (int i = 0; i < maxValues; i++) {

				final var value = builder.nextModel();
				model.values.add(value);

			}

		}

	}

}
