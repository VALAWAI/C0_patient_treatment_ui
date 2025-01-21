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
					return $localize`:The human readable name for the treatment action NIMV@@shared_treatment-action-name_NIMV:Non-invasive mechanical ventilation (NIMV)`;
				case 'VASOACTIVE_DRUGS':
					return $localize`:The human readable name for the treatment action VASOACTIVE_DRUGS@@shared_treatment-action-name_VASOACTIVE_DRUGS:Vasoactive drugs`;
				case 'DIALYSIS':
					return $localize`:The human readable name for the treatment action DIALYSIS@@shared_treatment-action-name_DIALYSIS:Dialysis`;
				case 'SIMPLE_CLINICAL_TRIAL':
					return $localize`:The human readable name for the treatment action SIMPLE_CLINICAL_TRIAL@@shared_treatment-action-name_SIMPLE_CLINICAL_TRIAL:Simple clinical trials: X-rays / Analytics / Cultures / Antibodies`;
				case 'MEDIUM_CLINICAL_TRIAL':
					return $localize`:The human readable name for the treatment action MEDIUM_CLINICAL_TRIAL@@shared_treatment-action-name_MEDIUM_CLINICAL_TRIAL:Medium clinical trials: Computed tomography / Transfusion / Peripherally placed central catheter / Enteral administration`;
				case 'ADVANCED_CLINICAL_TRIAL':
					return $localize`:The human readable name for the treatment action ADVANCED_CLINICAL_TRIAL@@shared_treatment-action-name_ADVANCED_CLINICAL_TRIAL:Advanced clinical trials: MRI / Endoscopy / Parenteral nutrition`;
				case 'PALLIATIVE_SURGERY':
					return $localize`:The human readable name for the treatment action PALLIATIVE_SURGERY@@shared_treatment-action-name_PALLIATIVE_SURGERY:Palliative surgery`;
				case 'CURE_SURGERY':
					return $localize`:The human readable name for the treatment action CURE_SURGERY@@shared_treatment-action-name_CURE_SURGERY:Cure surgery`;
			}

		}

		// undefined name			
		return '';
	}

}