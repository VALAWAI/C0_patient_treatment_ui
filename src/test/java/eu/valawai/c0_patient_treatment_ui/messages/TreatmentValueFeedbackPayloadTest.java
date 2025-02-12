/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;

/**
 * Test the {@link TreatmentValueFeedbackPayload}.
 *
 * @see TreatmentValueFeedbackPayload
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class TreatmentValueFeedbackPayloadTest extends PayloadTestCase<TreatmentValueFeedbackPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreatmentValueFeedbackPayload createEmptyModel() {

		return new TreatmentValueFeedbackPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(TreatmentValueFeedbackPayload model) {

		model.treatment_id = ValueGenerator.nextPattern("treatment_id_{0}");
		model.value_name = ValueGenerator.nextPattern("value_name_{0}");
		model.alignment = ValueGenerator.nextDouble() * 2.0 - 1.0;
	}

}
