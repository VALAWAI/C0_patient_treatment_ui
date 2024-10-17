/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Component } from '@angular/core';

import { RouterOutlet } from '@angular/router';

@Component({
	selector: 'app-main',
	standalone: true,
	imports: [RouterOutlet],
	templateUrl: './main.component.html',
	styleUrl: './main.component.css'
})
export class MainComponent {

}