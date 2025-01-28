/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatient;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModel;

/**
 * The information of a treatment.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "Contains the minimal information of a treatment.")
public class MinTreatment extends ReflectionModel {

	/**
	 * The identifier of the treatment or {@code null} if it is not stored.
	 */
	@Schema(title = "The identifier of the treatment.", readOnly = true)
	public Long id;

	/**
	 * The name of the treatment.
	 */
	@Schema(title = "The patient associated to the treatment.")
	public MinPatient patient;

}
