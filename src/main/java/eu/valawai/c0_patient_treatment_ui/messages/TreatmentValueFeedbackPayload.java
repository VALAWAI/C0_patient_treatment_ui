/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * TProvides the alignment of a treatment with a value.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@JsonRootName("treatment_value_feedback_payload")
public class TreatmentValueFeedbackPayload extends Payload {

	/**
	 * The id of the treatment that this is the feedback.
	 */
	@NotEmpty
	public String treatment_id;

	/**
	 * The name of the value that the treatment is aligned.
	 */
	@NotEmpty
	public String value_name;

	/**
	 * The alignment of the value with the the treatment.
	 */
	@NotNull
	@IsAlignValue
	public double alignment;

}