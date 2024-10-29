/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import eu.valawai.c0_patient_treatment_ui.ReflectionModel;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientStatusCriteria;

/**
 * The information of a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "Contains the information of a patient.")
public class Patient extends ReflectionModel {

	/**
	 * The identifier of the patient or {@code null} if it is not stored.
	 */
	@Schema(title = "The identifier of the patient.", readOnly = true)
	public Long id;

	/**
	 * The epoch time, in seconds, when the patient information has been updated.
	 */
	@Schema(title = "The epoch time, in seconds, when the patient information has been updated.", readOnly = true)
	public Long updateTime;

	/**
	 * The name of the patient.
	 */
	@Schema(title = "The name of the patient.")
	@Length(max = 1024)
	public String name;

	/**
	 * The current status of the patient.
	 */
	@Schema(title = "The current status of the patient.")
	public PatientStatusCriteria status;

	/**
	 * Retrieve a {@link Patient} with the data of a {@link PatientEntity}.
	 *
	 * @param entity to get the patient information.
	 *
	 * @return the patient with the data of the entity.
	 */
	public static Patient from(PatientEntity entity) {

		final var model = new Patient();
		model.id = entity.id;
		model.updateTime = entity.updateTime;
		model.name = entity.name;
		model.status = entity.status;
		return model;
	}

	/**
	 * Create an entity with the data of a model.
	 *
	 * @return entity with the information of this model.
	 */
	public PatientEntity toPatientEntity() {

		final var entity = new PatientEntity();
		entity.name = this.name;
		entity.status = this.status;
		return entity;

	}

}
