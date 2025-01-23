/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


export const YES_NO_UNKNOWN_OPTION_NAMES = ['YES', 'NO', 'UNKNOWN'] as const;

export type YesNoUnknownOption = typeof YES_NO_UNKNOWN_OPTION_NAMES[number];