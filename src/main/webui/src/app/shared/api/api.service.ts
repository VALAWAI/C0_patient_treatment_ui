/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { HttpClient, HttpParams } from "@angular/common/http";
import { Info } from './info.model';
import { HealthInfo } from './health-info.model';
import { environment } from '@environments/environment';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MinPatientPage } from "./min-patient-page.model";
import { Patient } from "./patient.model";

/**
 * The service used to interact with the 
 */
@Injectable({
	providedIn: 'root'
})
export class ApiService {

	/**
	 * Create the service.
	 */
	constructor(
		private http: HttpClient
	) {

	}

	/**
	 * Return the URL to the specified path.
	 */
	protected url(path: string, params: any[] | null = null): string {

		let url: string = environment.componentUrl + path;
		if (params) {

			for (let param of params) {

				if (!url.endsWith('/')) {

					url += '/';
				}
				url += encodeURIComponent(param);
			}

		}

		return url;
	}

	/**
	 * Return the option to a HTTP with some parameters.
	 */
	protected optionsWithParams(params: any | null = null): object {

		if (params) {

			var httpParams = this.toHttpParams(params);
			return { params: httpParams };

		} else {

			return {};
		}
	}

	/**
	 * Convert an object to HTTP params.
	 */
	private toHttpParams(params: any) {

		var httpParams = new HttpParams();
		for (let key of Object.keys(params)) {

			if (
				typeof params[key] === 'string' || params[key] instanceof String
				|| typeof params[key] === 'boolean' || params[key] instanceof Boolean
				|| typeof params[key] === 'number' || params[key] instanceof Number
			) {

				httpParams = httpParams.append(key, params[key]);
			}
		}
		return httpParams;

	}

	/**
	 * Get the help information.
	 */
	public getHelp(): Observable<Info> {

		var url = this.url('/v1/help/info');
		return this.http.get<Info>(url);
	}

	/**
	 * Get the help information.
	 */
	public getHealth(): Observable<HealthInfo> {

		var url = this.url('/q/health/');
		return this.http.get<HealthInfo>(url);
	}

	/**
	 * Get the information for some patients 
	 */
	getPatientsPage(pattern: string = "*", order: string = "+name", offset: number = 0, limit: number = 10): Observable<MinPatientPage> {

		var url = this.url('/v1/patients');
		return this.http.get<MinPatientPage>(
			url,
			this.optionsWithParams(
				{
					'name': pattern,
					'order': order,
					'offset': offset,
					'limit': limit
				}
			)
		);

	}

	/**
	 * Get the information of a patient 
	 */
	getPatient(patientId: number): Observable<Patient> {

		var url = this.url('/v1/patients', [patientId]);
		return this.http.get<Patient>(url);
	}

}