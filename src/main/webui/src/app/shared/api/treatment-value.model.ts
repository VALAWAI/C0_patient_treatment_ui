/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

/**
 * The information of a patient treatment.
 *
 * @author VALAWAI
 */
export class TreatmentValue {

	/**
	 * The name of the value.
	 */
	public name: string = '';

	/**
	 * The alignment of the value with the the treatment.
	 */
	public alignment: number = 0;;

	/**
	 * The epoch time, in second, that his action is updated.
	 */
	public updatedTime: number = 0;

}