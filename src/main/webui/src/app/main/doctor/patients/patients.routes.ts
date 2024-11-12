/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Routes } from '@angular/router';

export const PATIENTS_ROUTES: Routes = [
	{
		path: '',
		children: [
			{
				path: 'search',
				loadComponent: () => import('./patients.component').then(m => m.PatientsComponent)
			},
			{
				path: ':patientId/view',
				loadComponent: () => import('./view/view.component').then(m => m.ViewComponent)
			},
			{
				path: ':patientId/edit',
				loadComponent: () => import('./edit/edit.component').then(m => m.EditComponent)
			},
			{
				path: '',
				pathMatch: 'full',
				redirectTo: 'search'
			},
			{
				path: '**',
				loadComponent: () => import('src/app/shared/not-found').then(m => m.NotFoundComponent)
			}
		]
	}
];
