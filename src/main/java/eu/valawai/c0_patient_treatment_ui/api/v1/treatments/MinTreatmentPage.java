/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import eu.valawai.c0_patient_treatment_ui.models.ReflectionModel;

/**
 * The page that contains some {@link MinTreatment}s.
 *
 * @see MinTreatment
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "A page with the information of some treatments.")
public class MinTreatmentPage extends ReflectionModel {

	/**
	 * The treatments that satisfy the query.
	 */
	@Schema(title = "The treatments that satisfy the query.")
	public List<MinTreatment> treatments;

	/**
	 * The number of treatments that satisfy the query.
	 */
	@Schema(title = "The number of treatments that satisfy the query.")
	public int total;

}
