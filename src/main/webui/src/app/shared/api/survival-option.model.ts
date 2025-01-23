/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


export const SURVIVAL_OPTION_NAMES = ['LESS_THAN_12_MONTHS','MORE_THAN_12_MONTHS','UNKNOWN'] as const;

export type SurvivalOption = typeof SURVIVAL_OPTION_NAMES[number];