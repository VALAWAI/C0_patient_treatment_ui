/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { AsyncPipe, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TitleService } from '@app/shared';
import { ApiService, Patient } from '@app/shared/api';
import { MessagesService } from '@app/shared/messages';
import { Observable, switchMap, tap } from 'rxjs';

@Component({
	standalone: true,
    selector: 'app-doctor-patient-delete',
    imports: [
        AsyncPipe,
        RouterLink,
        NgIf
    ],
    templateUrl: './delete.component.html',
    styleUrl: './delete.component.css'
})
export class DeleteComponent implements OnInit {

	/**
	 * The patient to delete.
	 */
	public patient$: Observable<Patient> | null = null;

	/**
	 * The identifier of the patient that is deleteing.
	 */
	private patientId: number = 0;

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService,
		private route: ActivatedRoute,
		private notifier: MessagesService,
		private router: Router
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the delete patient@@main_doctor_patients_delete_code_page-title:Delete patient`);
		this.patient$ = this.route.paramMap.pipe(
			switchMap(params => {

				this.patientId = Number(params.get('patientId'));
				return this.api.getPatient(this.patientId);

			})
		);
	}

	/**
	 * Called when udate a patient.
	 */
	deletePatient() {

		this.api.deletePatient(this.patientId).subscribe(
			{
				next: () => {

					this.notifier.showSuccess($localize`:The success notification when the patient is deleted@@main_doctor_patients_delete_code_success-msg:Patient deleted`);
					this.router.navigate(['/main/doctor/patients'])
				},
				error: err => {

					this.notifier.showError($localize`:The error notification when the patient can nto be deleted@@main_doctor_patients_delete_code_error-msg:Patient cannot be deleted`);
					console.error(err);
				}
			}
		);
	}

}

