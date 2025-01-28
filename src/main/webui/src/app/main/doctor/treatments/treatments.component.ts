/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RouterLink } from '@angular/router';
import { TitleService } from '@app/shared';
import { ApiService, MinPatientPage, MinTreatmentPage } from '@app/shared/api';
import { Subscription } from 'rxjs';

@Component({
	standalone: true,
	selector: 'app-treatments',
	imports: [
		ReactiveFormsModule,
		MatPaginatorModule,
		MatPaginatorModule,
		MatMenuModule,
		MatIconModule,
		RouterLink,
		MatProgressBarModule,
		NgIf,
		NgFor
	],
	templateUrl: './treatments.component.html',
	styleUrl: './treatments.component.css'
})
export class TreatmentsComponent implements OnInit {

	/**
	 * The form control to edit the name.
	 */
	public name: FormControl<string | null> = this.fb.control<string | null>(null);

	/**
	 * Change name subscription.
	 */
	public nameChanged: Subscription | null = null;

	/**
	 * T is {@code true} if it is updating.
	 */
	public updating: boolean = false;

	/**
	 * The pattern to mathc the name of the patients to search.
	 */
	public pattern: string = "*";

	/**
	 * The last pattern taht has been search.
	 */
	public lastPattern: string | null = null;

	/**
	 * The index to the fisrt patient to return.
	 */
	public pageIndex: number = 0;

	/**
	 * The number maximum of patients to show.
	 */
	public pageSize: number = 10;

	/**
	 * The page with the found treatments.
	 */
	public page: MinTreatmentPage | null = null;

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private fb: FormBuilder,
		private api: ApiService
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the treatments@@main_doctor_treatments_code_page-title:Treatments`);
		this.updatePatients();
		this.nameChanged = this.name.valueChanges.subscribe(
			{
				next: value => {

					var pattern = "*";
					if (value != null) {

						pattern += value.trim() + "*";
					}
					if (this.lastPattern != pattern) {

						this.pattern = pattern;
						this.pageIndex = 0;
						this.updatePatients();
					}
				}
			}
		);

	}

	/**
	 * Destroy the component.
	 */
	ngOnDestroy(): void {

		if (this.nameChanged != null) {

			this.nameChanged.unsubscribe();
			this.nameChanged = null;
		}
	}




	/**
	 * Update the patients to show.
	 */
	private updatePatients() {

		if (this.updating) {
			//Try later
			var time = Math.round(500 + Math.random() * 500);
			setTimeout(() => this.updatePatients(), time);

		} else {

			this.updating = true;
			var offset = this.pageIndex * this.pageSize;
			this.api.getTreatmentsPage(this.pattern, "+name", offset, this.pageSize).subscribe({
				next: page => {

					this.page = page;
					this.updating = false;
					this.lastPattern = this.pattern;

				},
				error: err => {

					this.updating = false;
					console.error(err);
				}
			});

		}

	}

	/**
	 * Manage an event of the paginator.
	 */
	public handlePageEvent(event: PageEvent) {

		if (this.pageSize != event.pageSize) {

			var offset = this.pageSize * this.pageIndex;
			this.pageIndex = Math.floor(offset / event.pageSize);
			this.pageSize = event.pageSize;
			this.lastPattern = null;
			this.updatePatients();

		} else if (this.pageIndex != event.pageIndex) {

			this.pageIndex = event.pageIndex;
			this.lastPattern = null;
			this.updatePatients();
		}

	}


}
