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
import jakarta.validation.constraints.NotNull;

/**
 * The minimal information of a treatment.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "Contains the minimal information of a treatment.")
public class MinTreatment extends ReflectionModel {

	/**
	 * The identifier of the treatment or {@code null} if it is not stored.
	 */
	@Schema(title = "The identifier of the patient treatment.", readOnly = true)
	public Long id;

	/**
	 * The epoch time, in seconds, when the patient treatment is created.
	 */
	@Schema(title = "The epoch time, in seconds, when the patient treatment is created.", readOnly = true)
	public Long createdTime;

	/**
	 * The information of the patient that the treatment has to be applied.
	 */
	@NotNull
	@Schema(title = "The information of the patient that the treatment has to be applied.", required = true)
	public MinPatient patient;

}
