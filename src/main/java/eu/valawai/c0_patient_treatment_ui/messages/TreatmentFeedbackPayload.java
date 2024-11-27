/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.validation.constraints.NotEmpty;

/**
 * The feedback for a treatment.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@JsonRootName("treatment_feedback_payload")
public class TreatmentFeedbackPayload extends Payload {

	/**
	 * The id of the treatment that this is the feedback.
	 */
	@NotEmpty
	public String treatment_id;

}
