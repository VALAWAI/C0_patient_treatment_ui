/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { TreatmentAction } from "./treatment-action.model";
import { TreatmentActionFeedback } from "./treatment-action-feedback.model";

/**
 * Contains the status of a treatment action.
 *
 * @author VALAWAI
 */
export class TreatmentActionWithFeedback {

	/**
	 * The action that has to do over the patient.
	 */
	public action: TreatmentAction | null = null;

	/**
	 * The feedback associated to the action.
	 */
	public feedback: TreatmentActionFeedback | null = null;

	/**
	 * The epoch time, in second, that his action is updated.
	 */
	public updatedTime: number | null = null;

}