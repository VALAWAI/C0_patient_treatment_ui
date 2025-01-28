/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import java.util.ArrayList;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModelTestCase;

/**
 * Test the {@link MinPatientPage}.
 *
 * @see MinPatientPage
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class MinPatientPageTest extends ReflectionModelTestCase<MinPatientPage> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MinPatientPage createEmptyModel() {

		return new MinPatientPage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(MinPatientPage model) {

		model.total = ValueGenerator.rnd().nextInt(1, 100);
		final var max = ValueGenerator.rnd().nextInt(0, 10);
		if (max > 0) {

			model.patients = new ArrayList<>(max);
			final var builder = new MinPatientTest();
			for (int i = 0; i < max; i++) {

				final var patient = builder.nextModel();
				model.patients.add(patient);
			}

		}
	}
}
