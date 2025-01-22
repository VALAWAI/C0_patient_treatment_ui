/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModelTestCase;

/**
 * Test the {@link TreatmentValue}.
 *
 * @see TreatmentValue
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class TreatmentValueTest extends ReflectionModelTestCase<TreatmentValue> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreatmentValue createEmptyModel() {

		return new TreatmentValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(TreatmentValue model) {

		model.name = ValueGenerator.nextPattern("value name {0}");
		model.alignment = ValueGenerator.rnd().nextDouble(-1d, 1d);
		model.updatedTime = ValueGenerator.rnd().nextLong(0, TimeManager.now() - 360000);

	}

}
