/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component } from '@angular/core';
import { TitleService } from '@app/shared';

@Component({
	selector: 'app-patients',
	standalone: true,
	imports: [],
	templateUrl: './patients.component.html',
	styleUrl: './patients.component.css'
})
export class PatientsComponent {


	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the treatements@@main_doctor_treatements_code_page-title:Patients`);

	}

}

