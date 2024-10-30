/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/
import { Component, Input } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { HealthStatus } from '@app/shared/api';
import { NgIf } from '@angular/common';

@Component({
	selector: 'app-health-status',
	standalone: true,
	imports: [
		MatIcon,
		NgIf
	],
	templateUrl: './health-status.component.html',
	styleUrl: './health-status.component.css'
})
export class HealthStatusComponent {

	/**
	 * This is {@code true} if the service is running.
	 */
	public isUp: boolean = false;


	/**
	 *  Create the component.
	 */
	constructor() {

	}

	/**
	 * Updtae the status of the component.
	 */
	@Input()
	public set status(status: HealthStatus | undefined) {

		this.isUp = status == 'UP';
	}

}
