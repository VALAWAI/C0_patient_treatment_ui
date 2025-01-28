/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { TreatmentValue } from './treatment-value.model';

@Pipe({
	name: 'treatmentValueName',
	standalone: true,
})
export class TreatmentValueNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of a treatment action. 
	 */
	transform(value: TreatmentValue | string | null | undefined): string {

		var name = '';
		if (value) {

			if (typeof value == 'string') {

				name = value.toUpperCase();

			} else if (value.name) {

				name = value.name.toUpperCase();
			}

			switch (value) {

				case 'BENEFICENCE':
					return $localize`:The human readable name for the treatment value beneficence@@shared_treatment-value-name_beneficence:Beneficence`;
				case 'NONMALEFICENCE':
					return $localize`:The human readable name for the treatment value nonmaleficence@@shared_treatment-value-name_nonmaleficence:Nonmaleficence`;
				case 'AUTONOMY':
					return $localize`:The human readable name for the treatment value autonomy@@shared_treatment-value-name_autonomy:Autonomy`;
				case 'JUSTICE':
					return $localize`:The human readable name for the treatment value justice@@shared_treatment-value-name_justice:Justice`;
			}


			if (name.length > 1) {

				return name.substring(0, 1) + name.substring(1).toLowerCase();
			}
		}

		return name;
	}

}