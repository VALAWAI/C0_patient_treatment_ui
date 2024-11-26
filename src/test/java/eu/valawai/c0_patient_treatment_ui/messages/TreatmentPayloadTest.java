/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import java.util.ArrayList;
import java.util.Arrays;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;

/**
 * Test the {@link TreatmentPayload}.
 *
 * @see TreatmentPayload
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class TreatmentPayloadTest extends PayloadTestCase<TreatmentPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreatmentPayload createEmptyModel() {

		return new TreatmentPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(TreatmentPayload model) {

		model.id = ValueGenerator.nextPattern("treatment_id_{0}");
		model.patient_id = ValueGenerator.nextPattern("patient_id_{0}");
		model.created_time = ValueGenerator.nextPastTime();
		model.before_status = new PatientStatusCriteriaPayloadTest().nextModel();
		model.actions = new ArrayList<>(Arrays.asList(TreatmentAction.values()));
		ValueGenerator.shuffle(model.actions);
		final var max = ValueGenerator.rnd().nextInt(1, model.actions.size());
		model.actions = model.actions.subList(0, max);
		model.expected_status = new PatientStatusCriteriaPayloadTest().nextModel();
	}

}
