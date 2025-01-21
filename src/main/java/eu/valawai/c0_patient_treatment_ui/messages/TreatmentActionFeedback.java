/*
  Copyright 2025 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;

/**
 * The possible feedback over a {@link TreatmentAction}.
 *
 * @see TreatmentPayload
 * @see TreatmentAction
 * @see TreatmentActionFeedbackPayload
 *
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum TreatmentActionFeedback {

	/**
	 * When the action is allowed to do.
	 */
	ALLOW,

	/**
	 * When the action is not allowed to do.
	 */
	DENY,

	/**
	 * When is unknown if the action can be done/ or not because is missing some
	 * information.
	 */
	UNKNOWN;

}
