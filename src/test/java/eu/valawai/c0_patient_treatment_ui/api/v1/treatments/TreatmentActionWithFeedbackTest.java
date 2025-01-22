/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentActionFeedback;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModelTestCase;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;

/**
 * Test the {@link TreatmentActionWithFeedback}.
 *
 * @see TreatmentActionWithFeedback
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class TreatmentActionWithFeedbackTest extends ReflectionModelTestCase<TreatmentActionWithFeedback> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreatmentActionWithFeedback createEmptyModel() {

		return new TreatmentActionWithFeedback();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(TreatmentActionWithFeedback model) {

		model.action = ValueGenerator.next(TreatmentAction.values());
		model.feedback = ValueGenerator.next(TreatmentActionFeedback.values());
		model.updatedTime = ValueGenerator.rnd().nextLong(0, TimeManager.now() - 360000);

	}

}
