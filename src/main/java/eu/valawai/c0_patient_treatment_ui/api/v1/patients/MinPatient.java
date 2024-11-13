/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import eu.valawai.c0_patient_treatment_ui.models.ReflectionModel;

/**
 * The information of a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "Contains the minimal information of a patient.")
public class MinPatient extends ReflectionModel {

	/**
	 * The identifier of the patient or {@code null} if it is not stored.
	 */
	@Schema(title = "The identifier of the patient.", readOnly = true)
	public Long id;

	/**
	 * The name of the patient.
	 */
	@Schema(title = "The name of the patient.")
	@Length(max = 1024)
	public String name;

}
