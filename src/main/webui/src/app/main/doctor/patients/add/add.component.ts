/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { AsyncPipe, NgIf } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TitleService, UserNotificationService } from '@app/shared';
import { ApiService, Patient, PatientStatusCriteria } from '@app/shared/api';
import { Observable, Subscription, switchMap, tap } from 'rxjs';
import { AvvvatarsComponent } from '@ngxpert/avvvatars';
import { PatientStatusCriteriaEditorComponent } from '@app/shared/patient-status-criteria-editor';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
    selector: 'app-doctor-patient-add',
    imports: [
        AsyncPipe,
        NgIf,
        AvvvatarsComponent,
        PatientStatusCriteriaEditorComponent,
        MatIcon,
        RouterLink,
        MatButton,
        MatInputModule,
        ReactiveFormsModule,
        NgIf
    ],
    templateUrl: './add.component.html',
    styleUrl: './add.component.css'
})
export class AddComponent implements OnInit, OnDestroy {

	/**
	 * The patient to add.
	 */
	private querySubscription: Subscription | null = null;

	/**
	 * The status of the patient to add. 
	 */
	public status: PatientStatusCriteria | null = null;

	/**
	 * The control to add the patient name.
	 */
	public name: FormControl<string | null> = this.fb.control<string | null>(null, [Validators.required, Validators.max(1024)]);

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService,
		private route: ActivatedRoute,
		private fb: FormBuilder,
		private notifier: UserNotificationService,
		private router: Router
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the add patient page@@main_doctor_patients_add_code_page-title:Add patient`);
		this.querySubscription = this.route.queryParams.subscribe(
			{
				next: params => {

					this.name.setValue(params["name"]);

				}
			}
		);

	}

	/**
	 * Destroy the component.
	 */
	ngOnDestroy(): void {

		if (this.querySubscription != null) {

			this.querySubscription.unsubscribe();
			this.querySubscription = null;
		}
	}

	/**
	 * Called when has to add a patient.
	 */
	addPatient() {

		if (this.name.valid) {

			var patient = new Patient();
			patient.name = this.name.value;
			patient.status = this.status;
			this.api.addPatient(patient).subscribe(
				{
					next: patient => {

						this.notifier.showSuccess($localize`:The success notification when the patient has been added@@main_doctor_patients_add_code_added-success:Patient has been added`);
						this.router.navigate(['/main/doctor/patients', patient.id, 'view'])
					},
					error: err => {

						this.notifier.showError($localize`:The error notification when the patient cannot be added@@main_doctor_patients_add_code_add-error:Patient not add the patient`);
						console.error(err);
					}
				}
			);

		} else {

			this.name.markAllAsTouched();
		}
	}

}

