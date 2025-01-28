/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { MinTreatment } from './min-treatment.model';

/**
 * The information of some treatments.
 *
 * @author VALAWAI
 */
export class MinTreatmentPage {

	/**
	 * The maximum number of treatments that satisfy the query.
	 */
	public total: number = 0;

	/**
	 * The treatments that satisfy the query.
	 */
	public treatments: MinTreatment[] | null = null;



}
