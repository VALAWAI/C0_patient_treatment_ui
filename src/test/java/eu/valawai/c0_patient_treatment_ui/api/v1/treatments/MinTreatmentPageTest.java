/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import java.util.ArrayList;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModelTestCase;

/**
 * Test the {@link MinTreatmentPage}.
 *
 * @see MinTreatmentPage
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class MinTreatmentPageTest extends ReflectionModelTestCase<MinTreatmentPage> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MinTreatmentPage createEmptyModel() {

		return new MinTreatmentPage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(MinTreatmentPage model) {

		model.total = ValueGenerator.rnd().nextInt(1, 100);
		final var max = ValueGenerator.rnd().nextInt(0, 10);
		if (max > 0) {

			model.treatments = new ArrayList<>(max);
			final var builder = new MinTreatmentTest();
			for (int i = 0; i < max; i++) {

				final var treatment = builder.nextModel();
				model.treatments.add(treatment);
			}

		}
	}
}
