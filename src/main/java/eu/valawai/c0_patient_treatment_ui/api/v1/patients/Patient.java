/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import eu.valawai.c0_patient_treatment_ui.ReflectionModel;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import jakarta.validation.constraints.NotEmpty;

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
	@NotEmpty
	@Length(max = 1024)
	public String name;

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
		return model;
	}

	/**
	 * Create an entity with the data of a model.
	 *
	 * @return entity with the information of this model.
	 */
	public PatientEntity toPatientEntity() {

		final var entity = new PatientEntity();
		entity.id = this.id;
		if (this.updateTime != null) {

			entity.updateTime = this.updateTime;
		}
		entity.name = this.name;
		return entity;

	}

}
