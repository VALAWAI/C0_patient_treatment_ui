/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component, OnInit } from '@angular/core';

import { RouterOutlet } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { MatButton } from '@angular/material/button';
import { MatDivider } from '@angular/material/divider';
import { RouterLink } from '@angular/router';
import { TitleService } from '@app/shared/title.service';
import { AsyncPipe } from '@angular/common';
import { Observable } from 'rxjs';

@Component({
	standalone: true,
    selector: 'app-main',
    imports: [
        RouterOutlet,
        MatIcon,
        MatMenu,
        MatMenuTrigger,
        MatMenuItem,
        MatButton,
        MatDivider,
        RouterLink,
        AsyncPipe
    ],
    templateUrl: './main.component.html',
    styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {

	/**
	 * The title for the main page.
	 */
	public title$: Observable<string>|null = null;


	/**
	 * Create the mian component.
	 */
	constructor(
		private title: TitleService
	) {

	}

	/**
	 * Initilaize the component.
	 */
	public ngOnInit() {


		this.title$ = this.title.headerTitle();
	}


}