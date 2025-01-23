/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { AgeRangeOption } from './age-range-option.model';

@Pipe({
	name: 'ageRangeOptionName',
	standalone: true,
})
export class AgeRangeOptionNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of an age range option. 
	 */
	transform(value: AgeRangeOption | null | undefined): string {

		if (value) {

			switch (value) {

				case 'AGE_BETWEEN_0_AND_19':
					return $localize`:The human readable name for the age range AGE_BETWEEN_0_AND_19@@shared_age-range-name-AGE_BETWEEN_0_AND_19:0 - 19`;
				case 'AGE_BETWEEN_20_AND_29':
					return $localize`:The human readable name for the age range AGE_BETWEEN_20_AND_29@@shared_age-range-name-AGE_BETWEEN_20_AND_29:20 - 29`;
				case 'AGE_BETWEEN_30_AND_39':
					return $localize`:The human readable name for the age range AGE_BETWEEN_30_AND_39@@shared_age-range-name-AGE_BETWEEN_30_AND_39:30 - 39`;
				case 'AGE_BETWEEN_40_AND_49':
					return $localize`:The human readable name for the age range AGE_BETWEEN_40_AND_49@@shared_age-range-name-AGE_BETWEEN_40_AND_49:40 - 49`;
				case 'AGE_BETWEEN_50_AND_59':
					return $localize`:The human readable name for the age range AGE_BETWEEN_50_AND_59@@shared_age-range-name-AGE_BETWEEN_50_AND_59:50 - 59`;
				case 'AGE_BETWEEN_60_AND_69':
					return $localize`:The human readable name for the age range AGE_BETWEEN_60_AND_69@@shared_age-range-name-AGE_BETWEEN_60_AND_69:60 - 69`;
				case 'AGE_BETWEEN_70_AND_79':
					return $localize`:The human readable name for the age range AGE_BETWEEN_70_AND_79@@shared_age-range-name-AGE_BETWEEN_70_AND_79:70 - 79`;
				case 'AGE_BETWEEN_80_AND_89':
					return $localize`:The human readable name for the age range AGE_BETWEEN_80_AND_89@@shared_age-range-name-AGE_BETWEEN_80_AND_89:80 - 89`;
				case 'AGE_BETWEEN_90_AND_99':
					return $localize`:The human readable name for the age range AGE_BETWEEN_90_AND_99@@shared_age-range-name-AGE_BETWEEN_90_AND_99:90 - 99`;
				case 'AGE_MORE_THAN_99':
					return $localize`:The human readable name for the age range AGE_MORE_THAN_99@@shared_age-range-name-AGE_MORE_THAN_99:+99`;
			}

		}

		// undefined name			
		return '';
	}

}