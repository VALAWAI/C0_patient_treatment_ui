/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component, Output, EventEmitter, Input, OnInit, OnDestroy } from '@angular/core';
import {
	PatientStatusCriteria,
	AgeRangeOption,
	YesNoUnknownOption,
	SurvivalOptions,
	SPICT_Scale,
	ClinicalRiskGroupOption,
	BarthelIndex,
	LawtonIndex,
	CognitiveImpairmentLevel,
	DiscomfortDegree
} from '@app/shared/api';
import { MatRadioModule } from '@angular/material/radio';
import { Subscription } from 'rxjs';
import { ReactiveFormsModule, FormControl, FormBuilder } from '@angular/forms';

@Component({
    selector: 'app-patient-status-criteria-editor',
    imports: [
        ReactiveFormsModule,
        MatRadioModule
    ],
    templateUrl: './patient-status-criteria-editor.component.html',
    styleUrls: ['./patient-status-criteria-editor.component.css']
})
export class PatientStatusCriteriaEditorComponent implements OnInit, OnDestroy {

	/**
	 * Notify a paretn component that a patient has bene selected.
	 */
	@Output()
	public dataChanged = new EventEmitter<PatientStatusCriteria>();

	/**
	 * The form control to edit the name.
	 */
	public form = this.fb.group({
		'ageRange': this.fb.control<AgeRangeOption>(null),
		'ccd': this.fb.control<YesNoUnknownOption>(null),
		'clinicalRiskGroup': this.fb.control<ClinicalRiskGroupOption>(null),
		'discomfortDegree': this.fb.control<DiscomfortDegree>(null),
		'expectedSurvival': this.fb.control<SurvivalOptions>(null),
		'frailVIG': this.fb.control<SPICT_Scale>(null),
		'hasAdvanceDirectives': this.fb.control<YesNoUnknownOption>(null),
		'hasBeenInformed': this.fb.control<YesNoUnknownOption>(null),
		'hasCognitiveImpairment': this.fb.control<CognitiveImpairmentLevel>(null),
		'hasEmocionalPain': this.fb.control<YesNoUnknownOption>(null),
		'hasSocialSupport': this.fb.control<YesNoUnknownOption>(null),
		'independenceAtAdmission': this.fb.control<BarthelIndex>(null),
		'independenceInstrumentalActivities': this.fb.control<LawtonIndex>(null),
		'isCoerced': this.fb.control<YesNoUnknownOption>(null),
		'isCompetent': this.fb.control<YesNoUnknownOption>(null),
		'maca': this.fb.control<YesNoUnknownOption>(null)
	});

	/**
	 * Change name subscription.
	 */
	private formChanged: Subscription | null = null;

	/**
	 * Create the component.
	 */
	constructor(
		private fb: FormBuilder
	) { }

	/**
	 * Initialize the component.
	 */
	@Input()
	set data(data: PatientStatusCriteria | null | undefined) {

		if (data) {

			this.form.patchValue(data, { emitEvent: false });

		} else {

			this.form.setValue(new PatientStatusCriteria(), { emitEvent: false });
		}

	}

	/**
	 * Disable or enable the editor.
	 */
	@Input()
	set disable(disabled: boolean) {

		if (disabled) {

			this.form.disable();

		} else {

			this.form.enable();
		}
	}

	/**
	 * Sunbscribe to the changes of the form.
	 */
	ngOnInit(): void {

		this.formChanged = this.form.valueChanges.subscribe(
			{
				next: () => {

					var value = this.form.value;
					var status = new PatientStatusCriteria();
					status.ageRange = value.ageRange || null;
					status.ccd = value.ccd || null;
					status.clinicalRiskGroup = value.clinicalRiskGroup || null;
					status.discomfortDegree = value.discomfortDegree || null;
					status.expectedSurvival = value.expectedSurvival || null;
					status.frailVIG = value.frailVIG || null;
					status.hasAdvanceDirectives = value.hasAdvanceDirectives || null;
					status.hasBeenInformed = value.hasBeenInformed || null;
					status.hasCognitiveImpairment = value.hasCognitiveImpairment || null;
					status.hasEmocionalPain = value.hasEmocionalPain || null;
					status.hasSocialSupport = value.hasSocialSupport || null;
					status.independenceAtAdmission = value.independenceAtAdmission || null;
					status.independenceInstrumentalActivities = value.independenceInstrumentalActivities || null;
					status.isCoerced = value.isCoerced || null;
					status.isCompetent = value.isCompetent || null;
					status.maca = value.maca || null;
					this.dataChanged.emit(status);
				}
			}
		);

	}

	/**
	 * Unsubscribe to the form changes.
	 */
	ngOnDestroy(): void {

		if (this.formChanged != null) {

			this.formChanged.unsubscribe();
			this.formChanged = null;
		}

	}

}
