/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { MinPatient } from "./min-patient.model";
import { PatientStatusCriteria } from './patient-status-criteria.model';

/**
 * The minimal information of a patient.
 *
 * @author VALAWAI
 */
export class Patient extends MinPatient {

	/**
	 * The epoch time, in seconds, when the patient information has been updated.
	 */
	public updateTime: number | null = null;

	/**
	 * The current status of the patient.
	 */
	public status: PatientStatusCriteria | null = null;


}
