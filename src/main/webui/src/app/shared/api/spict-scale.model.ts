/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


export const SPICT_SCALE_NAMES = ['LOW', 'MODERATE', 'HIGH', 'UNKNOWN'] as const;

export type SPICT_Scale = typeof SPICT_SCALE_NAMES[number];