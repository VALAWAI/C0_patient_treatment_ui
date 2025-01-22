/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


export const TREATMENT_ACTION_FEEDBACK = ['ALLOW', 'DENY', 'UNKNOWN'] as const;

export type TreatmentActionFeedback = typeof TREATMENT_ACTION_FEEDBACK[number];