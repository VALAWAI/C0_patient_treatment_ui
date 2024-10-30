/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Routes } from '@angular/router';

export const DOCTOR_ROUTES: Routes = [
	{
		path: '',
		children: [
			{
				path: 'patients',
				loadChildren: () => import('./patients/patients.routes').then(m => m.PATIENTS_ROUTES)
			},
			{
				path: 'treatments',
				loadChildren: () => import('./treatments/treatments.routes').then(m => m.TREATMENTS_ROUTES)
			},
			{
				path: '',
				pathMatch: 'full',
				redirectTo: 'patients'
			},
			{
				path: '**',
				loadComponent: () => import('src/app/shared/not-found').then(m => m.NotFoundComponent)
			}
		]
	}
];
