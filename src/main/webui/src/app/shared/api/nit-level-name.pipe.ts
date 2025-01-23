/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { NITLevel } from './nit-level.model';

@Pipe({
	name: 'nitLevelName',
	standalone: true,
})
export class NITLevelNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of a NIT level. 
	 */
	transform(value: NITLevel | null | undefined): string {

		if (value) {

			switch (value) {

				case 'ONE':
					return $localize`:The human readable name for the NIT level ONE@@shared_nit-level-name-ONE:1 Treatment without limits`;
				case 'TWO_A':
					return $localize`:The human readable name for the NIT level TWO_A@@shared_nit-level-name-TWO_A:2A Intense treatment`;
				case 'TWO_B':
					return $localize`:The human readable name for the NIT level TWO_B@@shared_nit-level-name-TWO_B:2B Intense treatment`;
				case 'THREE':
					return $localize`:The human readable name for the NIT level THREE@@shared_nit-level-name-THREE:3 Treatment of intermediate intensity`;
				case 'FOUR':
					return $localize`:The human readable name for the NIT level FOUR@@shared_nit-level-name-FOUR:4 Symptomatic conservative treatment`;
				case 'FIVE':
					return $localize`:The human readable name for the NIT level FIVE@@shared_nit-level-name-FIVE:5 Exclusively comfort measures`;
			}

		}

		// undefined name			
		return '';
	}

}