/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;

/**
 * Test the {@link TreatmentActionFeedbackPayload}.
 *
 * @see TreatmentActionFeedbackPayload
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class TreatmentActionFeedbackPayloadTest extends PayloadTestCase<TreatmentActionFeedbackPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreatmentActionFeedbackPayload createEmptyModel() {

		return new TreatmentActionFeedbackPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(TreatmentActionFeedbackPayload model) {

		model.treatment_id = ValueGenerator.nextPattern("treatment_id_{0}");
		model.action = ValueGenerator.next(TreatmentAction.values());
		model.feedback = ValueGenerator.next(TreatmentActionFeedback.values());
	}

}
