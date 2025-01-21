/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { MinPatient } from './min-patient.model';

/**
 * The information of some patients.
 *
 * @author VALAWAI
 */
export class MinPatientPage {

	/**
	 * The maximum number of patients that satisfy the query.
	 */
	public total: number = 0;

	/**
	 * The patients that satisfy the query.
	 */
	public patients: MinPatient[] | null = null;



}
