/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component, Inject, Injectable } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBar, MatSnackBarConfig, MatSnackBarLabel } from '@angular/material/snack-bar';

/**
 * The service that is used to notify the user.
 */
@Injectable({
	providedIn: 'root'
})
export class UserNotificationService {



	/**
	 * Create the service.
	 */
	constructor(private _snackBar: MatSnackBar) {

	}

	/**
	 * Create the configuration fro a notification.
	 */
	private createConfig(text: string): MatSnackBarConfig<Notification> {

		return {
			duration: 1500,
			data: { text: text },
			panelClass: 'notification'
		};
	}

	/**
	 * Show an error message.
	 */
	public showError(text: string) {

		this._snackBar.openFromComponent(ErrorNotificationComponent, this.createConfig(text));

	}

	/**
	 * Show an success message.
	 */
	public showSuccess(text: string) {

		this._snackBar.openFromComponent(SuccessNotificationComponent, this.createConfig(text));
	}

	/**
	 * Show an info message.
	 */
	public showInfo(text: string) {

		this._snackBar.openFromComponent(InfoNotificationComponent, this.createConfig(text));
	}


}

export class Notification {

	/**
	 * The text of the notification.
	 */
	public text: string = 'No data';
}

@Component({
    selector: 'snack-bar-error-notification',
    template: `
	<div class="p-4 mb-4 text-sm text-red-800 rounded-lg bg-red-50 dark:bg-gray-800 dark:text-red-400" role="alert">
		<span matSnackBarLabel>{{data.text}}</span>
	</div>
	`,
    imports: [MatSnackBarLabel]
})
export class ErrorNotificationComponent {

	/**
	 * Create the component.
	 */
	constructor(
		@Inject(MAT_SNACK_BAR_DATA) public data: Notification
	) {
	}

}

@Component({
    selector: 'snack-bar-success-notification',
    template: `
	<div class="p-4 mb-4 text-sm text-green-800 rounded-lg bg-green-50 dark:bg-gray-800 dark:text-green-400" role="alert">
		<span matSnackBarLabel>{{data.text}}</span>
	</div>
	`,
    imports: [MatSnackBarLabel]
})
export class SuccessNotificationComponent {

	/**
	 * Create the component.
	 */
	constructor(
		@Inject(MAT_SNACK_BAR_DATA) public data: Notification
	) {
	}

}


@Component({
    selector: 'snack-bar-info-notification',
    template: `
	<div class="p-4 mb-4 text-sm text-blue-800 rounded-lg bg-blue-50 dark:bg-gray-800 dark:text-blue-400" role="alert">
		<span matSnackBarLabel>{{data.text}}</span>
	</div>
	`,
    imports: [MatSnackBarLabel]
})
export class InfoNotificationComponent {

	/**
	 * Create the component.
	 */
	constructor(
		@Inject(MAT_SNACK_BAR_DATA) public data: Notification
	) {
	}

}

