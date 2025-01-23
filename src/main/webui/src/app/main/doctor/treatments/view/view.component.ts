/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { NgFor, NgIf, NgSwitch, NgSwitchCase, NgSwitchDefault } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TitleService } from '@app/shared';
import { ApiService, Treatment, TreatmentActionNamePipe } from '@app/shared/api';
import { Subscription } from 'rxjs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIcon } from '@angular/material/icon';
import { PatientStatusCriteriaEditorComponent } from '@app/shared/patient-status-criteria-editor';

@Component({
	standalone: true,
	selector: 'app-doctor-treatment-view',
	imports: [
		NgIf,
		MatIcon,
		RouterLink,
		MatExpansionModule,
		PatientStatusCriteriaEditorComponent,
		NgFor,
		TreatmentActionNamePipe,
		NgSwitch,
		NgSwitchCase,
		NgSwitchDefault,
		MatProgressBarModule
	],
	templateUrl: './view.component.html',
	styleUrl: './view.component.css'
})
export class ViewComponent implements OnInit, OnDestroy {

	/**
	 * The identifier of the treatment to show.
	 */
	private treatmentId: number | null = null;

	/**
	 * The subscription to the changes on the treatement id.
	 */
	private treatementIdSubscription: Subscription | null = null;

	/**
	 * The treatment to view.
	 */
	public treatment: Treatment | null = null;
	
	/**
	 * The identifier of the timeout timer.
	 */
	private timerId: any | null = null;

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService,
		private route: ActivatedRoute
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title when view a treatement@@main_doctor_treatments_view_code_page-title:View treatment information`);
		this.treatementIdSubscription = this.route.paramMap.subscribe(
			{
				next: (params) => {
					this.treatmentId = Number(params.get('treatmentId'));
					this.updateTreatment();
				}
			}
		);
	}

	/**
	 * Unsubscribe
	 */
	ngOnDestroy(): void {

		if (this.treatementIdSubscription != null) {

			this.treatementIdSubscription.unsubscribe();
			this.treatementIdSubscription = null;
		}
		if( this.timerId != null ){
			
			clearTimeout(this.timerId);
			this.timerId = null;
		}
	}

	/**
	 * Update the treatement.
	 */
	private updateTreatment() {

		this.api.getTreatment(this.treatmentId || 0).subscribe(
			{
				next: (treatment) => {

					this.treatment = treatment;
					this.timerId = setTimeout(() => this.updateTreatment(), 1500);
				}
			}
		);

	}

}

