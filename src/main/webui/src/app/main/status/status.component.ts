/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/
import { Component } from '@angular/core';
import { Info, HealthInfo, ApiService } from '@app/shared/api';
import { TitleService } from '@app/shared';
import { HealthStatusComponent } from './health-status.component';
import { NgFor } from '@angular/common';

@Component({
	selector: 'app-status',
	standalone: true,
	imports: [
		HealthStatusComponent,
		NgFor
	],
	templateUrl: './status.component.html',
	styleUrl: './status.component.css'
})
export class StatusComponent {

	/**
	 * The informaiton of the started MOV.
	 */
	public info: Info | null = null;

	/**
	 * The informaiton of the started MOV.
	 */
	public health: HealthInfo | null = null;

	/**
	 * The identifier of the timer.
	 */
	private timeoutID: ReturnType<typeof setTimeout> | null = null;

	/**
	 *  Create the component.
	 */
	constructor(
		private title: TitleService,
		private api: ApiService
	) {

	}

	/**
	 * Initialize the component.
	 */
	ngOnInit(): void {

		this.title.changeHeaderTitle($localize`:The header title for the status@@main_status_code_page-title:Status`);
		this.api.getHelp().subscribe(
			{
				next: info => this.info = info
			}

		);
		this.updateHealth();

	}

	/**
	 * Called when has to update the health information.
	 */
	public updateHealth() {


		this.api.getHealth().subscribe(
			{
				next: health => {

					this.health = health;
					this.timeoutID = setTimeout(() => this.updateHealth(), 30000);

				}
			}
		)
	}

	/**
	 * Finalizes the component.
	 */
	ngOnDestroy(): void {

		if (this.timeoutID != null) {

			clearTimeout(this.timeoutID);
		}
	}

}
