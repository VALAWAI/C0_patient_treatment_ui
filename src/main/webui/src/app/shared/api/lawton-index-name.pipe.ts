/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { LawtonIndex } from './lawton-index.model';

@Pipe({
	name: 'lawtonIndexName',
	standalone: true,
})
export class LawtonIndexNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of a lawton index value. 
	 */
	transform(value: LawtonIndex | null | undefined): string {

		if (value) {

			switch (value) {

				case 'ZERO':
					return $localize`:The human readable name for the lawton index ZERO@@shared_lawton-index-name-ZERO:0 points`;
				case 'ONE':
					return $localize`:The human readable name for the lawton index ONE@@shared_lawton-index-name-ONE:1 point`;
				case 'TWO':
					return $localize`:The human readable name for the lawton index TWO@@shared_lawton-index-name-TWO:2 points`;
				case 'THREE':
					return $localize`:The human readable name for the lawton index THREE@@shared_lawton-index-name-THREE:3 points`;
				case 'FOUR':
					return $localize`:The human readable name for the lawton index FOUR@@shared_lawton-index-name-FOUR:4 points`;
				case 'FIVE':
					return $localize`:The human readable name for the lawton index FIVE@@shared_lawton-index-name-FIVE:5 points`;
				case 'SIX':
					return $localize`:The human readable name for the lawton index SIX@@shared_lawton-index-name-SIX:6 points`;
				case 'SEVEN':
					return $localize`:The human readable name for the lawton index SEVEN@@shared_lawton-index-name-SEVEN:7 points`;
				case 'EIGHT':
					return $localize`:The human readable name for the lawton index EIGHT@@shared_lawton-index-name-EIGHT:8 points`;
				case 'UNKNOWN':
					return $localize`:The human readable name for the lawton index UNKNOWN@@shared_lawton-index-name-UNKNOWN:Unknown`;
			}
		}

		// undefined name			
		return '';
	}

}