/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { CognitiveImpairmentLevel } from './cognitive-impairment-level.model';
export { CognitiveImpairmentLevel } from './cognitive-impairment-level.model';

@Pipe({
	name: 'cognitiveImpairmentLevelName',
	standalone: true,
})
export class CognitiveImpairmentLevelNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of a cognitive impairment level option. 
	 */
	transform(value: CognitiveImpairmentLevel | null | undefined): string {

		if (value) {

			switch (value) {

				case 'ABSENT':
					return $localize`:The human readable name for the cognitive impairment level ABSENT@@shared_cognitive-impairment-level-name-ABSENT:Absent`;
				case 'MILD_MODERATE':
					return $localize`:The human readable name for the cognitive impairment level MILD_MODERATE@@shared_cognitive-impairment-level-name-MILD_MODERATE:Mild moderate`;
				case 'SEVERE':
					return $localize`:The human readable name for the cognitive impairment level SEVERE@@shared_cognitive-impairment-level-name-SEVERE:Severe`;
				case 'UNKNOWN':
					return $localize`:The human readable name for the cognitive impairment level UNKNOWN@@shared_cognitive-impairment-level-name-UNKNOWN:Unknown`;
			}
		}

		// undefined name			
		return '';
	}

}