/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


export const CLINICAL_RISK_GROUP_OPTION_NAMES = ['PROMOTION_AND_PREVENTION',
	'SELF_MANAGEMENT_SUPPORT',
	'ILLNESS_MANAGEMENT',
	'CASE_MANAGEMENT',
	'UNKNOWN'
] as const;

export type ClinicalRiskGroupOption = typeof CLINICAL_RISK_GROUP_OPTION_NAMES[number];