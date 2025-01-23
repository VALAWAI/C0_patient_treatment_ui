/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { MinPatient } from "./min-patient.model";
import { PatientStatusCriteria } from "./patient-status-criteria.model";
import { TreatmentActionWithFeedback } from "./treatment-action-with-feedback.model";
import { TreatmentValue } from "./treatment-value.model";

/**
 * The information of a patient treatment.
 *
 * @author VALAWAI
 */
export class Treatment {

	/**
	 * The identifier of the treatment.
	 */
	public id: number | null = null;

	/**
	 * The epoch time, in seconds, when the patient treatment is created.
	 */
	public createdTime: number | null = null;

	/**
	 * The patient over the treatment must be applied.
	 */
	public patient: MinPatient | null = null;

	/**
	 * The status of the patient before the treatment actions.
	 */
	public beforeStatus: PatientStatusCriteria | null = null;

	/**
	 * The actions that to apply to the patient.
	 */
	public actions: TreatmentActionWithFeedback[] = [];

	/**
	 * The expected status after the treatment actions.
	 */
	public expectedStatus: PatientStatusCriteria | null = null;

	/**
	 * The values associated to the treatment.
	 */
	public values: TreatmentValue[] = [];


}
