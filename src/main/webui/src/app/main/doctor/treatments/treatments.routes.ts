/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { Routes } from '@angular/router';

export const TREATMENTS_ROUTES: Routes = [
	{
		path: '',
		children: [
			{
				path: 'search',
				loadComponent: () => import('./treatments.component').then(m => m.TreatmentsComponent)
			},
			{
				path: ':treatmentId/view',
				loadComponent: () => import('./view/view.component').then(m => m.ViewComponent)
			},
			{
				path: ':treatmentId/delete',
				loadComponent: () => import('./delete/delete.component').then(m => m.DeleteComponent)
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
