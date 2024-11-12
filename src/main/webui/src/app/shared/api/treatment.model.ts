/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { MinPatient } from "./min-patient.model";
import { PatientStatusCriteria } from "./patient-status-criteria.model";


export const TREATMENT_ACTION_NAMES = ['CPR',
	'TRANSPLANT',
	'ICU',
	'NIMV',
	'VASOACTIVE_DRUGS',
	'DIALYSIS',
	'SIMPLE_CLINICAL_TRIAL',
	'MEDIUM_CLINICAL_TRIAL',
	'ADVANCED_CLINICAL_TRIAL',
	'PALLIATIVE_SURGERY',
	'CURE_SURGERY',
] as const;

export type TreatmentAction = typeof TREATMENT_ACTION_NAMES[number];

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
	 * The patient over the treatment must be applied.
	 */
	public patient: MinPatient | null = null;

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
