/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.patients;

import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatient;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.Patient;

/**
 * Test the {@link Patient}.
 *
 * @see Patient
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class MinPatientTest extends MinPatientTestCase<MinPatient> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MinPatient createEmptyModel() {

		return new MinPatient();
	}

}
