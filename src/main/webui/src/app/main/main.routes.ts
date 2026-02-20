/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Routes } from '@angular/router';

export const MAIN_ROUTES: Routes = [
	{
		path: '',
		loadComponent: () => import('./main.component').then(c => c.MainComponent),
		children: [
			{
				path: 'doctor',
				loadChildren: () => import('./doctor/doctor.routes').then(m => m.DOCTOR_ROUTES)
			},
			{
				path: 'status',
				loadComponent: () => import('./status/status.component').then(m => m.StatusComponent)
			},
			{
				path: '',
				pathMatch: 'full',
				redirectTo: 'doctor'
			},
			{
				path: '**',
				loadComponent: () => import('@shared/not-found').then(m => m.NotFoundComponent)
			}
		]
	}
];
