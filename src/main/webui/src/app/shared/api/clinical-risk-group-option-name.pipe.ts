/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Pipe, PipeTransform } from '@angular/core';
import { ClinicalRiskGroupOption } from './clinical-risk-group-option.model';

@Pipe({
	name: 'clinicalRiskGroupOptionName',
	standalone: true,
})
export class ClinicalRiskGroupOptionNamePipe implements PipeTransform {

	/**
	 * Return a human readable name of a clinic risk group option. 
	 */
	transform(value: ClinicalRiskGroupOption | null | undefined): string {

		if (value) {

			switch (value) {

				case 'PROMOTION_AND_PREVENTION':
					return $localize`:The human readable name for the clinicla risk group option PROMOTION_AND_PREVENTION@@shared_clinical-risk-group-option-name-PROMOTION_AND_PREVENTION:0 Promotion & Prevention`;
				case 'SELF_MANAGEMENT_SUPPORT':
					return $localize`:The human readable name for the clinicla risk group option SELF_MANAGEMENT_SUPPORT@@shared_clinical-risk-group-option-name-SELF_MANAGEMENT_SUPPORT:1 Self-management support`;
				case 'ILLNESS_MANAGEMENT':
					return $localize`:The human readable name for the clinicla risk group option ILLNESS_MANAGEMENT@@shared_clinical-risk-group-option-name-ILLNESS_MANAGEMENT:2 Illness management`;
				case 'CASE_MANAGEMENT':
					return $localize`:The human readable name for the clinicla risk group option CASE_MANAGEMENT@@shared_clinical-risk-group-option-name-CASE_MANAGEMENT:3 Case management`;
				case 'UNKNOWN':
					return $localize`:The human readable name for the clinicla risk group option UNKNOWN@@shared_clinical-risk-group-option-name-UNKNOWN:Unknown`;
			}
		}

		// undefined name			
		return '';
	}

}