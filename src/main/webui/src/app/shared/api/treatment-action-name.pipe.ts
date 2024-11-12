/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { TreatmentAction } from './treatment.model';

@Pipe({
	name: 'treatmentActionName',
	standalone: true,
})
export class TreatmentActionNamePipe implements PipeTransform {

	/**
	 * REturn a human readable name of a treatment action. 
	 */
	transform(value: TreatmentAction | null | undefined): string {

		if (value) {

			switch (value) {

				case 'CPR':
					return $localize`:The human readable name for the treatment action CPR@@shared_treatment-action-name_CPR:Cardiopulmonary resuscitation (CPR)`;
				case 'TRANSPLANT':
					return $localize`:The human readable name for the treatment action TRANSPLANT@@shared_treatment-action-name_TRANSPLANT:Transplant`;
				case 'ICU':
					return $localize`:The human readable name for the treatment action ICU@@shared_treatment-action-name_ICU:Intense care unit (ICU)`;
				case 'NIMV':
				case 'VASOACTIVE_DRUGS':
				case 'DIALYSIS':
				case 'SIMPLE_CLINICAL_TRIAL':
				case 'MEDIUM_CLINICAL_TRIAL':
				case 'ADVANCED_CLINICAL_TRIAL':
				case 'PALLIATIVE_SURGERY':
				case 'CURE_SURGERY':

			}

		}

		// undefined name			
		return '';
	}

}