/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import com.fasterxml.jackson.annotation.JsonRootName;

import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
import jakarta.validation.constraints.NotEmpty;

/**
 * The feedback for an action in a treatment.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@JsonRootName("treatment_action_feedback_payload")
public class TreatmentActionFeedbackPayload extends Payload {

	/**
	 * The id of the treatment that this is the feedback.
	 */
	@NotEmpty
	public String treatment_id;

	/**
	 * The action that this is feedback.
	 */
	@NotEmpty
	public TreatmentAction action;

	/**
	 * If feedback over the action of the treatment.
	 */
	@NotEmpty
	public TreatmentActionFeedback feedback;

}