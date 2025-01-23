/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { AsyncPipe, NgClass, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TitleService, UserNotificationService } from '@app/shared';
import { ApiService, TreatmentToAdd, Patient, PatientStatusCriteria, TREATMENT_ACTION_NAMES, TreatmentActionNamePipe } from '@app/shared/api';
import { Observable, switchMap, tap } from 'rxjs';
import { PatientStatusCriteriaEditorComponent } from '@app/shared/patient-status-criteria-editor';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatStepperModule } from '@angular/material/stepper';
import { MatSlideToggle } from '@angular/material/slide-toggle';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';

/**
 * Validator to check that at least one action is selected.
 */
export const AtLeastOneActionValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {

	const values = control.value;
	for (const key in values) {

		const value = values[key];
		if (value === true) {
			// go to next
			return null;
		}
	}
	return {
		required: true
	};
}

@Component({
	standalone: true,
	selector: 'app-doctor-patient-edit',
	imports: [
		AsyncPipe,
		NgIf,
		MatStepperModule,
		PatientStatusCriteriaEditorComponent,
		ReactiveFormsModule,
		NgFor,
		MatSlideToggle,
		TreatmentActionNamePipe,
		NgClass
	],
	templateUrl: './treatment.component.html',
	styleUrl: './treatment.component.css',
	providers: [
		{
			provide: STEPPER_GLOBAL_OPTIONS,
			useValue: { showError: true },
		},
	]
})
export class TreatmentComponent implements OnInit {

	/**
	 * The names for the treatment actions.
	 */
	public TREATMENT_ACTIONS = TREATMENT_ACTION_NAMES;

	/**
	 * The patient to edit.
	 */
	public patient$: Observable<Patient> | null = null;

	/**
	 * The identifier of the patient that is editing.
	 */
	private patientId: number = 0;

	/**
	 * The actions to apply in the treatment.
	 */
	public form: FormGroup =
		this.fb.group({
			beforeStatus: this.fb.control<PatientStatusCriteria | null>(null, Validators.required),
			actions: this.fb.group({}, { validators: AtLeastOneActionValidator }),
			expectedStatus: this.fb.control<PatientStatusCriteria | null>(null, Validators.required),
		});

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService,
		private route: ActivatedRoute,
		private router: Router,
		private fb: FormBuilder,
		private notifier: UserNotificationService
	) {

		for (var name of TREATMENT_ACTION_NAMES) {

			this.actions.addControl(name, this.fb.control<boolean>(false));
		}

	}

	/**
	 * Get the before status form.
	 */
	public get beforeStatus(): FormControl<PatientStatusCriteria | null> {


		return this.form.controls['beforeStatus'] as FormControl<PatientStatusCriteria | null>;

	}

	/**
	 * Get the actions form.
	 */
	public get actions(): FormGroup {


		return this.form.controls['actions'] as FormGroup;

	}

	/**
	 * Get the after status form.
	 */
	public get expectedStatus(): FormControl<PatientStatusCriteria | null> {


		return this.form.controls['expectedStatus'] as FormControl<PatientStatusCriteria | null>;

	}

	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the patient treatment@@main_doctor_patients_treatment_code_page-title:Add treatment to a patient`);
		this.patient$ = this.route.paramMap.pipe(
			switchMap(params => {

				this.patientId = Number(params.get('patientId'));
				return this.api.getPatient(this.patientId).pipe(
					tap(
						patient => {
							this.form.patchValue(
								{
									beforeStatus: patient.status,
									expectedStatus: patient.status
								}
							);
						}
					)
				);

			})
		);
	}

	/**
	 * Return the contorl for an action name.
	 */
	getControl(name: string): FormControl<boolean> {

		return this.actions.get(name) as FormControl<boolean>;

	}


	/**
	 * Add a treatment for a patient.
	 */
	addTreatment() {

		if (this.form.valid) {

			var treatment = new TreatmentToAdd();
			treatment.patientId = this.patientId;
			treatment.actions = [];
			for (var name of TREATMENT_ACTION_NAMES) {

				if (this.getControl(name).value === true) {

					treatment.actions.push(name);
				}
			}
			treatment.beforeStatus = this.beforeStatus.value;
			treatment.expectedStatus = this.expectedStatus.value;
			this.api.addTreatment(treatment).subscribe({
				next: (added) => {

					this.router.navigate(['/main/doctor/treatments/', added.id, '/view']);

				},
				error: err => {

					this.notifier.showError($localize`:The error message when can not add the treatment@@main_doctor_patients_edit_code_add-error:Canot add the treatment`);
					console.error(err);
				}
			});
		} else {

			this.form.markAllAsTouched();
		}
	}

}

