/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { TreatmentAction } from "./treatment-action.model";
import { PatientStatusCriteria } from "./patient-status-criteria.model";


/**
 * The information of a treatment to add for a patient.
 *
 * @author VALAWAI
 */
export class TreatmentToAdd {

	/**
	 * The identifier of the treatment.
	 */
	public id: number | null = null;

	/**
	 * The patient over the treatment must be applied.
	 */
	public patientId: number | null = null;

	/**
	 * The range of age of the patient status.
	 */
	public beforeStatus: PatientStatusCriteria | null = null;

	/**
	 * The actions that to apply to the patient.
	 */
	public actions: TreatmentAction[] = [];

	/**
	 * The expected status after the treatment actions.
	 */
	public expectedStatus: PatientStatusCriteria | null = null;


}
