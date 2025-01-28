/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

/**
 * Test the {@link MinTreatment}.
 *
 * @see MinTreatment
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class MinTreatmentTest extends MinTreatmentTestCase<MinTreatment> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MinTreatment createEmptyModel() {

		return new MinTreatment();
	}

}
