/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteria;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModel;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * A treatment that is apply to a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "A treatment that is apply to a patient")
public class TreatmentToAdd extends ReflectionModel {

	/**
	 * The information of the patient that the treatment has to be applied.
	 */
	@NotNull
	@Schema(title = "The identifier of the patient to add the treatment.", required = true)
	public Long patientId;

	/**
	 * The status before to apply the treatment.
	 */
	@NotNull
	@Schema(title = "The status of the patient before to apply the treatment.", required = true)
	public PatientStatusCriteria beforeStatus;

	/**
	 * The treatment actions to apply over the patient.
	 */
	@NotEmpty
	@Schema(title = "The treatment actions to apply over the patient.")
	public List<TreatmentAction> actions;

	/**
	 * The expected status of the patient after applying the treatment.
	 */
	@NotNull
	@Schema(title = "The expected status of the patient after applying the treatment.", required = true)
	public PatientStatusCriteria expectedStatus;

}
