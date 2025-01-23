/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { SurvivalOption } from './survival-option.model';

@Pipe({
	name: 'survivalOptionName',
	standalone: true,
})
export class SurvivalOptionNamePipe implements PipeTransform {

	/**
	 * REturn a human readable name of a treatment action. 
	 */
	transform(value: SurvivalOption | null | undefined): string {

		if (value) {

			switch (value) {

				case 'LESS_THAN_12_MONTHS':
					return $localize`:The human readable name forthe option LESS_THAN_12_MONTHS@@shared_survival-name-LESS_THAN_12_MONTHS:< 12 months`;
				case 'MORE_THAN_12_MONTHS':
					return $localize`:The human readable name forthe option MORE_THAN_12_MONTHS@@shared_survival-name-MORE_THAN_12_MONTHS:> 12 months`;
				case 'UNKNOWN':
					return $localize`:The human readable name forthe option UNKNOWN@@shared_survival-name-UNKNOWN:Unknown`;
			}

		}

		// undefined name			
		return '';
	}

}