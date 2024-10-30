/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TitleService } from '@app/shared';
import { ApiService, MinPatient } from '@app/shared/api';
import { PatientSelectorComponent } from '@app/shared/patient-selector';

@Component({
	selector: 'app-patients',
	standalone: true,
	imports: [
		PatientSelectorComponent,
	],
	templateUrl: './patients.component.html',
	styleUrl: './patients.component.css'
})
export class PatientsComponent implements OnInit {

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService,
		private router: Router
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the treatements@@main_doctor_patients_code_page-title:Patients`);
	}

	/**
	 * Called whne a patient has been selected.
	 */
	public patientSelected(patient: MinPatient) {

		this.router.navigate(['/main/doctor/patients',patient.id,'view'])

	}
}

