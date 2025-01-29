/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { MinPatient } from "./min-patient.model";

/**
 * The minimal information of a treatment.
 *
 * @author VALAWAI
 */
export class MinTreatment {

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
}
