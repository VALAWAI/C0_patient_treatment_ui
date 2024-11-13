/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import eu.valawai.c0_patient_treatment_ui.models.ReflectionModel;

/**
 * The page that contains some {@link MinPatient}s.
 *
 * @see MinPatient
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "A page with the information of some patients.")
public class MinPatientPage extends ReflectionModel {

	/**
	 * The patients that satisfy the query.
	 */
	@Schema(title = "The patients that satisfy the query.")
	public List<MinPatient> patients;

	/**
	 * The number of patients that satisfy the query.
	 */
	@Schema(title = "The number of patients that satisfy the query.")
	public int total;

}
