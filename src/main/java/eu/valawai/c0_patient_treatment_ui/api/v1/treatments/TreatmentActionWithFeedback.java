/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import eu.valawai.c0_patient_treatment_ui.messages.TreatmentActionFeedback;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModel;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
import jakarta.validation.constraints.NotNull;

/**
 * Contains the status of a treatment action.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "An action to apply into a patient to follow a treatment with its feedback.")
public class TreatmentActionWithFeedback extends ReflectionModel {

	/**
	 * The action that has to do over the patient.
	 */
	@NotNull
	@Schema(title = "The action to apply over a patient.")
	public TreatmentAction action;

	/**
	 * The feedback associated to the action.
	 */
	@Schema(title = "The feedback  to apply over a patient.")
	public TreatmentActionFeedback feedback;

	/**
	 * The epoch time, in second, that his action is updated.
	 */
	@Schema(title = "The epoch time, in seconds, that this feedback is updated.")
	public long updatedTime;

}
