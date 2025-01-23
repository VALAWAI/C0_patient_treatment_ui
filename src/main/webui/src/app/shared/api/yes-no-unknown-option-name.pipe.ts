/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { YesNoUnknownOption } from './yes-no-unknown-option.model';

@Pipe({
	name: 'yesNoUnknownOptionName',
	standalone: true,
})
export class YesNoUnknownOptionNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of a yes,no or unknown option. 
	 */
	transform(value: YesNoUnknownOption | null | undefined): string {

		if (value) {

			switch (value) {

				case 'YES':
					return $localize`:The human readable name forthe option YES@@shared_yes-no-unknown-name-YES:Yes`;
				case 'NO':
					return $localize`:The human readable name forthe option NO@@shared_yes-no-unknown-name-NO:No`;
				case 'UNKNOWN':
					return $localize`:The human readable name forthe option UNKNOWN@@shared_yes-no-unknown-name-UNKNOWN:Unknown`;
			}

		}

		// undefined name			
		return '';
	}

}