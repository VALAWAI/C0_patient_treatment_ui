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
	SurvivalOption,
	SPICT_Scale,
	ClinicalRiskGroupOption,
	BarthelIndex,
	LawtonIndex,
	CognitiveImpairmentLevel,
	DiscomfortDegree,
	AgeRangeOptionNamePipe,
	AGE_RANGE_OPTION_NAMES,
	YesNoUnknownOptionNamePipe,
	YES_NO_UNKNOWN_OPTION_NAMES,
	SurvivalOptionNamePipe,
	SURVIVAL_OPTION_NAMES,
	SPICT_ScaleNamePipe,
	SPICT_SCALE_NAMES,
	ClinicalRiskGroupOptionNamePipe,
	CLINICAL_RISK_GROUP_OPTION_NAMES,
	BarthelIndexNamePipe,
	BARTHEL_INDEX_NAMES
} from '@app/shared/api';
import { MatRadioModule } from '@angular/material/radio';
import { Subscription } from 'rxjs';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';

@Component({
	standalone: true,
	selector: 'app-patient-status-criteria-editor',
	imports: [
		ReactiveFormsModule,
		MatRadioModule,
		NgIf,
		NgFor,
		AgeRangeOptionNamePipe,
		YesNoUnknownOptionNamePipe,
		SurvivalOptionNamePipe,
		SPICT_ScaleNamePipe,
		ClinicalRiskGroupOptionNamePipe,
		BarthelIndexNamePipe
	],
	templateUrl: './patient-status-criteria-editor.component.html',
	styleUrls: ['./patient-status-criteria-editor.component.css']
})
export class PatientStatusCriteriaEditorComponent implements OnInit, OnDestroy {

	/**
	 * The posible age range option names.
	 */
	public AGE_RANGE_OPTION_NAMES = AGE_RANGE_OPTION_NAMES;

	/**
	 * The posible yes, no and unknown option names.
	 */
	public YES_NO_UNKNOWN_OPTION_NAMES = YES_NO_UNKNOWN_OPTION_NAMES;

	/**
	 * The posible survival option names.
	 */
	public SURVIVAL_OPTION_NAMES = SURVIVAL_OPTION_NAMES;

	/**
	 * The posible SPICT scale names.
	 */
	public SPICT_SCALE_NAMES = SPICT_SCALE_NAMES;

	/**
	 * The posible clinic risk group option names.
	 */
	public CLINICAL_RISK_GROUP_OPTION_NAMES = CLINICAL_RISK_GROUP_OPTION_NAMES;

	/**
	 * The posible Barthel index option names.
	 */
	public BARTHEL_INDEX_NAMES = BARTHEL_INDEX_NAMES;

	/**
	 * Notify a paretn component that a patient has bene selected.
	 */
	@Output()
	public dataChanged = new EventEmitter<PatientStatusCriteria>();

	/**
	 * The form control to edit the name.
	 */
	public form = this.fb.group({
		'ageRange': this.fb.control<AgeRangeOption | null>(null),
		'ccd': this.fb.control<YesNoUnknownOption | null>(null),
		'clinicalRiskGroup': this.fb.control<ClinicalRiskGroupOption | null>(null),
		'discomfortDegree': this.fb.control<DiscomfortDegree>(null),
		'expectedSurvival': this.fb.control<SurvivalOption | null>(null),
		'frailVIG': this.fb.control<SPICT_Scale | null>(null),
		'hasAdvanceDirectives': this.fb.control<YesNoUnknownOption | null>(null),
		'hasBeenInformed': this.fb.control<YesNoUnknownOption | null>(null),
		'hasCognitiveImpairment': this.fb.control<CognitiveImpairmentLevel>(null),
		'hasEmocionalPain': this.fb.control<YesNoUnknownOption | null>(null),
		'hasSocialSupport': this.fb.control<YesNoUnknownOption | null>(null),
		'independenceAtAdmission': this.fb.control<BarthelIndex | null>(null),
		'independenceInstrumentalActivities': this.fb.control<LawtonIndex>(null),
		'isCoerced': this.fb.control<YesNoUnknownOption | null>(null),
		'isCompetent': this.fb.control<YesNoUnknownOption | null>(null),
		'maca': this.fb.control<YesNoUnknownOption | null>(null)
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
	) {

	}

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
	 * Disable or enable only the criteria that can not be modified by a treatment.
	 */
	@Input()
	set disableNotTreatmentCriteria(disabled: boolean) {

		if (disabled) {

			this.form.controls.ageRange.disable();
			this.form.controls.ccd.enable();
			this.form.controls.maca.enable();
			this.form.controls.expectedSurvival.enable();
			this.form.controls.frailVIG.enable();
			this.form.controls.clinicalRiskGroup.enable();
			this.form.controls.hasSocialSupport.disable();
			this.form.controls.independenceAtAdmission.disable();
			this.form.controls.independenceInstrumentalActivities.enable();
			this.form.controls.hasAdvanceDirectives.disable();
			this.form.controls.isCompetent.disable();
			this.form.controls.hasBeenInformed.disable();
			this.form.controls.isCoerced.disable();
			this.form.controls.hasCognitiveImpairment.disable();
			this.form.controls.hasEmocionalPain.enable();
			this.form.controls.discomfortDegree.enable();

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
