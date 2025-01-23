/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { AsyncPipe, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TitleService, UserNotificationService } from '@app/shared';
import { ApiService, Treatment } from '@app/shared/api';
import { Observable, switchMap, tap } from 'rxjs';

@Component({
	standalone: true,
    selector: 'app-doctor-treatment-delete',
    imports: [
        AsyncPipe,
        RouterLink,
        NgIf
    ],
    templateUrl: './delete.component.html',
    styleUrl: './delete.component.css'
})
export class DeleteComponent implements OnInit {

	/**
	 * The treatment to delete.
	 */
	public treatment$: Observable<Treatment> | null = null;

	/**
	 * The identifier of the treatment that is deleteing.
	 */
	private treatmentId: number = 0;

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService,
		private route: ActivatedRoute,
		private notifier: UserNotificationService,
		private router: Router
	) {

	}


	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the delete a treatment@@main_doctor_treatments_delete_code_page-title:Delete treatment`);
		this.treatment$ = this.route.paramMap.pipe(
			switchMap(params => {

				this.treatmentId = Number(params.get('treatmentId'));
				return this.api.getTreatment(this.treatmentId);

			})
		);
	}

	/**
	 * Called when udate a treatment.
	 */
	deleteTreatment() {

		this.api.deleteTreatment(this.treatmentId).subscribe(
			{
				next: () => {

					this.notifier.showSuccess($localize`:The success notification when the treatment is deleted@@main_doctor_treatments_delete_code_success-msg:Treatment deleted`);
					this.router.navigate(['/main/doctor/treatments'])
				},
				error: err => {

					this.notifier.showError($localize`:The error notification when the treatment can not be removed@@main_doctor_treatments_delete_code_error-msg:Treatment cannot be deleted`);
					console.error(err);
				}
			}
		);
	}

}

