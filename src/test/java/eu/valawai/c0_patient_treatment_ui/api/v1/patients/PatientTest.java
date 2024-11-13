/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteriaTest;

/**
 * Test the {@link Patient}.
 *
 * @see Patient
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class PatientTest extends MinPatientTestCase<Patient> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Patient createEmptyModel() {

		return new Patient();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(Patient model) {

		super.fillIn(model);
		model.updateTime = ValueGenerator.rnd().nextLong(0, TimeManager.now() - 360000);
		model.status = new PatientStatusCriteriaTest().nextModel();

	}

}
