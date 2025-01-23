/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


export const AGE_RANGE_OPTION_NAMES = ['AGE_BETWEEN_0_AND_19'
	, 'AGE_BETWEEN_20_AND_29'
	, 'AGE_BETWEEN_30_AND_39'
	, 'AGE_BETWEEN_40_AND_49'
	, 'AGE_BETWEEN_50_AND_59'
	, 'AGE_BETWEEN_60_AND_69'
	, 'AGE_BETWEEN_70_AND_79'
	, 'AGE_BETWEEN_80_AND_89'
	, 'AGE_BETWEEN_90_AND_99'
	, 'AGE_MORE_THAN_99'
] as const;

export type AgeRangeOption = typeof AGE_RANGE_OPTION_NAMES[number];