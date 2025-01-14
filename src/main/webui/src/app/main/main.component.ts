/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component, OnInit } from '@angular/core';

import { RouterOutlet, RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { MatDivider } from '@angular/material/divider';
import { TitleService } from '@app/shared/title.service';
import { AsyncPipe, NgIf } from '@angular/common';
import { Observable } from 'rxjs';
import { LOCALE_ID, Inject } from '@angular/core';

@Component({
	standalone: true,
	selector: 'app-main',
	imports: [
		RouterOutlet,
		MatIcon,
		MatMenu,
		MatMenuTrigger,
		MatMenuItem,
		MatDivider,
		RouterLink,
		AsyncPipe,
		NgIf
	],
	templateUrl: './main.component.html',
	styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {

	/**
	 * The title for the main page.
	 */
	public title$: Observable<string> | null = null;


	/**
	 * Create the mian component.
	 */
	constructor(
		private title: TitleService,
		@Inject(LOCALE_ID) private locale: string
	) {

	}

	/**
	 * Initilaize the component.
	 */
	public ngOnInit() {

		this.title$ = this.title.headerTitle();
	}

	/**
	 * Change the locale of the application
	 */
	public changeLocaleTo(lang: string) {

		var path = '';
		var host = '';
		var href = window.location.href;
		var index = href.indexOf('/main/');
		if (index > 0) {

			path = href.substring(index);
			host = href.substring(0, index);
		}
		if (host.match(/\/[a-z]{2}$/)) {

			host = host.substring(0, host.length - 2);

		} else if (!host.endsWith('/')) {

			host = host + '/';
		}
		window.location.href = host + lang + path;

	}

	/**
	 * Check if the app is localized in a language.
	 */
	public isLocalizedIn(lang: string) {

		return this.locale != null && this.locale.indexOf(lang) > -1;
	}

}