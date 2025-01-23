/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


export const NIT_LEVEL_NAMES = ['ONE', 'TWO_A', 'TWO_B', 'THREE', 'FOUR', 'FIVE'] as const;

export type NITLevel = typeof NIT_LEVEL_NAMES[number];
