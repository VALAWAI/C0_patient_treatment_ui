/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { SPICT_Scale } from './spict-scale.model';

@Pipe({
	name: 'spictScaleName',
	standalone: true,
})
export class SPICT_ScaleNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of a SPICT scale value. 
	 */
	transform(value: SPICT_Scale | null | undefined): string {

		if (value) {

			switch (value) {

				case 'LOW':
					return $localize`:The human readable name for the SPICT scale LOW@@shared_spict-scale-name-LOW:Low`;
				case 'MODERATE':
					return $localize`:The human readable name for the SPICT scale MODERATE@@shared_spict-scale-name-MODERATE:Moderate`;
				case 'HIGH':
					return $localize`:The human readable name for the SPICT scale HIGH@@shared_spict-scale-name-HIGH:High`;
				case 'UNKNOWN':
					return $localize`:The human readable name for the SPICT scale UNKNOWN@@shared_spict-scale-name-UNKNOWN:Unknown`;
			}
		}

		// undefined name			
		return '';
	}

}