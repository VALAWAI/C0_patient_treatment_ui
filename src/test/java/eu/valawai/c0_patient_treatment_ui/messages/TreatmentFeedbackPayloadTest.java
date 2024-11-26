/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;

/**
 * Test the {@link TreatmentFeedbackPayload}.
 *
 * @see TreatmentFeedbackPayload
 *
 * @author UDT-IA, IIIA-CSIC
 */
class TreatmentFeedbackPayloadTest extends PayloadTestCase<TreatmentFeedbackPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreatmentFeedbackPayload createEmptyModel() {

		return new TreatmentFeedbackPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(TreatmentFeedbackPayload model) {

		model.treatment_id = ValueGenerator.nextPattern("treatment_id_{0}");
	}

}
