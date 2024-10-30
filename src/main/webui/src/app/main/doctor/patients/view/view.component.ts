/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component, OnInit } from '@angular/core';
import { TitleService } from '@app/shared';
import { ApiService, Patient } from '@app/shared/api';


@Component({
	selector: 'app-doctor-patient-view',
	standalone: true,
	imports: [
	],
	templateUrl: './view.component.html',
	styleUrl: './view.component.css'
})
export class ViewComponent implements OnInit {

	/**
	 * The patient to view.
	 */
	public patient: Patient | null = null;

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the treatements@@main_doctor_patients_view_code_page-title:View`);
	}

}

