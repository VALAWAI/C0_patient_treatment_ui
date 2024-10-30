/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { AsyncPipe, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TitleService } from '@app/shared';
import { ApiService, Patient } from '@app/shared/api';
import { Observable, switchMap } from 'rxjs';


@Component({
	selector: 'app-doctor-patient-view',
	standalone: true,
	imports: [
		AsyncPipe,
		NgIf
	],
	templateUrl: './view.component.html',
	styleUrl: './view.component.css'
})
export class ViewComponent implements OnInit {


	/**
	 * The patient to view.
	 */
	public patient$: Observable<Patient> | null = null;

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

		this.title.changeHeaderTitle($localize`:The header title for the treatements@@main_doctor_patients_view_code_page-title:View`);
		this.patient$ = this.route.paramMap.pipe(
			switchMap(params => {

				var patientId = Number(params.get('patientId'));
				return this.api.getPatient(patientId);

			})
		);
	}

}

