/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { AsyncPipe, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TitleService, UserNotificationService } from '@app/shared';
import { ApiService, Patient, PatientStatusCriteria } from '@app/shared/api';
import { Observable, switchMap, tap } from 'rxjs';
import { AvvvatarsComponent } from '@ngxpert/avvvatars';
import { PatientStatusCriteriaEditorComponent } from '@app/shared/patient-status-criteria-editor';
import { MatButton } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
	standalone: true,
    selector: 'app-doctor-patient-edit',
    imports: [
        AsyncPipe,
        NgIf,
        AvvvatarsComponent,
        PatientStatusCriteriaEditorComponent,
        MatButton,
        MatInputModule,
        ReactiveFormsModule,
        NgIf
    ],
    templateUrl: './edit.component.html',
    styleUrl: './edit.component.css'
})
export class EditComponent implements OnInit {

	/**
	 * The patient to edit.
	 */
	public patient$: Observable<Patient> | null = null;

	/**
	 * The updated status. 
	 */
	public updatedStatus: PatientStatusCriteria | null = null;

	/**
	 * The control to edit the patient name.
	 */
	public name: FormControl<string | null> = this.fb.control<string | null>(null, [Validators.required, Validators.max(1024)]);

	/**
	 * The identifier of the patient that is editing.
	 */
	private patientId: number = 0;

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService,
		private route: ActivatedRoute,
		private fb: FormBuilder,
		private notifier:UserNotificationService
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the edit patient@@main_doctor_patients_edit_code_page-title:Edit patient information`);
		this.patient$ = this.route.paramMap.pipe(
			switchMap(params => {

				this.patientId = Number(params.get('patientId'));
				return this.api.getPatient(this.patientId).pipe(
					tap(
						patient => {
							this.name.setValue(patient.name);
						}
					)
				);

			})
		);
	}

	/**
	 * Called when udate a patient.
	 */
	updatePatient() {

		if (this.name.valid) {

			var patient = new Patient();
			patient.name = this.name.value;
			patient.status = this.updatedStatus;
			this.api.updatePatient(this.patientId, patient).subscribe(
				{
					next: () => {

						this.notifier.showSuccess($localize`:The success notification when the patient is updated@@main_doctor_patients_edit_code_update-success:Updated patient`);

					},
					error: err => {

						this.notifier.showError($localize`:The error notification when the patient is updated@@main_doctor_patients_edit_code_update-error:Patient not updated`);
						console.error(err);
					}
				}
			);

		} else {

			this.name.markAllAsTouched();
		}
	}

}

