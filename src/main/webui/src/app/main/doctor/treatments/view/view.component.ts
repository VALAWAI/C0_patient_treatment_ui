/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { AsyncPipe, NgFor, NgIf, NgSwitch, NgSwitchCase, NgSwitchDefault } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TitleService } from '@app/shared';
import { ApiService, Treatment, TreatmentActionNamePipe } from '@app/shared/api';
import { Observable, switchMap } from 'rxjs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIcon } from '@angular/material/icon';
import { PatientStatusCriteriaEditorComponent } from '@app/shared/patient-status-criteria-editor';

@Component({
	standalone: true,
	selector: 'app-doctor-treatment-view',
	imports: [
		AsyncPipe,
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
export class ViewComponent implements OnInit {


	/**
	 * The treatment to view.
	 */
	public treatment$: Observable<Treatment> | null = null;

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
		this.treatment$ = this.route.paramMap.pipe(
			switchMap(params => {

				var treatmentId = Number(params.get('treatmentId'));
				return this.api.getTreatment(treatmentId);

			})
		);
	}

}

