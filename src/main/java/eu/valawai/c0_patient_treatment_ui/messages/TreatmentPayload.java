/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;

/**
 * The treatment to apply to a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@JsonRootName("treatment_payload")
public class TreatmentPayload extends Payload {

	/**
	 * The id of the treatment.
	 */
	public String id;

	/**
	 * The id of the patient.
	 */
	public String patient_id;

	/**
	 * The epoch time, in seconds, when the patient treatment is created.
	 */
	public long created_time;

	/**
	 * The status before to apply the treatment.
	 */
	public PatientStatusCriteriaPayload before_status;

	/**
	 * The treatment actions to apply over the patient.
	 */
	public List<TreatmentAction> actions;

	/**
	 * The expected status of the patient after applying the treatment.
	 */
	public PatientStatusCriteriaPayload expected_status;

}
