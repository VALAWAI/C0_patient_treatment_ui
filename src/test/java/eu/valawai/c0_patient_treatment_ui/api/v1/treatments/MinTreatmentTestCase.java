/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatientTest;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModelTestCase;

/**
 * Test the classes that extends the {@link MinTreatment}.
 *
 * @see MinTreatment
 *
 * @param <T> type of model to test.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public abstract class MinTreatmentTestCase<T extends MinTreatment> extends ReflectionModelTestCase<T> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(T model) {

		model.id = ValueGenerator.rnd().nextLong();
		model.patient = new MinPatientTest().nextModel();

	}

}
