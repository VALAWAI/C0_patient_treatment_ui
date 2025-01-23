/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { BarthelIndex } from './barthel-index.model';

@Pipe({
	name: 'barthelIndexName',
	standalone: true,
})
export class BarthelIndexNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of a bartel index option. 
	 */
	transform(value: BarthelIndex | null | undefined): string {

		if (value) {

			switch (value) {

				case 'TOTAL':
					return $localize`:The human readable name for the barthel index TOTAL@@shared_barthel-index-name-TOTAL:0 - 20% Total`;
				case 'SEVERE':
					return $localize`:The human readable name for the barthel index SEVERE@@shared_barthel-index-name-SEVERE:21 - 60% Severe`;
				case 'MODERATE':
					return $localize`:The human readable name for the barthel index MODERATE@@shared_barthel-index-name-MODERATE:61 - 90% Moderate`;
				case 'MILD':
					return $localize`:The human readable name for the barthel index MILD@@shared_barthel-index-name-MILD:91 - 99% Mild`;
				case 'INDEPENDENT':
					return $localize`:The human readable name for the barthel index INDEPENDENT@@shared_barthel-index-name-INDEPENDENT:100% Independent`;
				case 'UNKNOWN':
					return $localize`:The human readable name for the barthel index UNKNOWN@@shared_barthel-index-name-UNKNOWN:Unknown`;
			}
		}

		// undefined name			
		return '';
	}

}