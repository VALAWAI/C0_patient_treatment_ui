/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


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