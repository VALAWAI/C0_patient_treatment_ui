/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { NgFor, NgIf, NgSwitch, NgSwitchCase, NgSwitchDefault } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TitleService } from '@app/shared';
import { ApiService, Treatment, TreatmentActionNamePipe, TreatmentValueNamePipe } from '@app/shared/api';
import { Subscription } from 'rxjs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIcon } from '@angular/material/icon';
import { PatientStatusCriteriaEditorComponent } from '@app/shared/patient-status-criteria-editor';
import { NgApexchartsModule, ApexAxisChartSeries, ApexChart, ApexXAxis, ApexPlotOptions, ApexYAxis } from 'ng-apexcharts';
import { MessagesService } from '@app/shared/messages';

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
		MatProgressBarModule,
		NgApexchartsModule,
	],
	providers: [
		TreatmentValueNamePipe
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
	 * The treatement value chart series. 
	 */
	public valuesChartSeries: ApexAxisChartSeries = [
		{
			data: []
		}
	];

	/**
	 * The treatement value chart. 
	 */
	public valuesChart: ApexChart = {
		type: "bar"
	};

	/**
	 * The treatement value chart axis. 
	 */
	public valuesChartXaxis: ApexXAxis = {
		categories: []
	};

	/**
	 * The treatment value chart options.
	 */
	public valuesChartOptions: ApexPlotOptions = {
		bar: {
			distributed: true,
			horizontal: true,
			borderRadius: 5,
			borderRadiusApplication: 'end', // 'around', 'end'
			borderRadiusWhenStacked: 'all', // 'all', 'last'
			barHeight: '80%',
		}
	};

	/**
	 * The treatement value chart axis. 
	 */
	public valuesChartYaxis: ApexYAxis = {
		min: -1.0,
		max: 1.0
	};

	/**
	 * The treatement value chart colors.
	 */
	public valuesChartColors: string[] = [
		"#33b2df",
		"#546E7A",
		"#d4526e",
		"#13d8aa",
		"#A5978B",
		"#2b908f",
		"#f9a3a4",
		"#90ee7e",
		"#f48024",
		"#69d2e7"
	];

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService,
		private route: ActivatedRoute,
		private valueNamePipe: TreatmentValueNamePipe,
		private router: Router,
		private message: MessagesService
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
		if (this.timerId != null) {

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
					this.treatmentUpdated();
					this.timerId = setTimeout(() => this.updateTreatment(), 1500);
				}
			}
		);

	}

	/**
	 * Called when the treatement has been updated. 
	 */
	private treatmentUpdated() {

		if (this.treatment && this.treatment.values.length > 0) {

			var data: number[] = [];
			var categories: string[] = [];
			for (var value of this.treatment.values) {

				categories.push(this.valueNamePipe.transform(value));
				data.push(value.alignment);
			}
			this.valuesChartSeries[0].data = data;
			this.valuesChartXaxis.categories = categories;

		}

	}

	/**
	 * Do again a treatment.
	 */
	public doItAgain() {

		if (this.treatment) {
			this.api.doAgainTreatment(this.treatment).subscribe({

				next: added => this.router.navigate(['/main/doctor/treatments', added.id, 'view']),
				error: (err) => {

					this.message.showError($localize`:The error message when can not do agin the treatment@@main_doctor_treatments_view_code_do-again-error:Cannot do again this treatment.`);
					console.error(err);
				}
			});
		}
	}


}

