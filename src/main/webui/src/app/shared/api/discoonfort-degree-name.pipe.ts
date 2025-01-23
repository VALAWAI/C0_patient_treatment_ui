/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { DiscomfortDegree } from './disconfort-degree.model';

@Pipe({
	name: 'discomfortDegreeName',
	standalone: true,
})
export class DiscomfortDegreeNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of an age range option. 
	 */
	transform(value: DiscomfortDegree | null | undefined): string {

		if (value) {

			switch (value) {

				case 'LOW':
					return $localize`:The human readable name for the disconfort degree LOW@@shared_disconfort-degree-name-LOW:Low`;
				case 'MEDIUM':
					return $localize`:The human readable name for the disconfort degree MEDIUM@@shared_disconfort-degree-name-MEDIUM:Medium`;
				case 'HIGH':
					return $localize`:The human readable name for the disconfort degree HIGH@@shared_disconfort-degree-name-HIGH:High`;
				case 'UNKNOWN':
					return $localize`:The human readable name for the disconfort degree UNKNOWN@@shared_disconfort-degree-name-UNKNOWN:Unknown`;
			}

		}

		// undefined name			
		return '';
	}

}