/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component, OnInit } from '@angular/core';
import { TitleService } from '@app/shared';

@Component({
	selector: 'app-treatments',
	standalone: true,
	imports: [],
	templateUrl: './treatments.component.html',
	styleUrl: './treatments.component.css'
})
export class TreatmentsComponent implements OnInit {


	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the treatements@@main_doctor_treatements_code_page-title:Treatments`);

	}

}
