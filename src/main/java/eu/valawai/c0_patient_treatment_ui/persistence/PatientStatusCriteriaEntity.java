/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteria;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

/**
 * An entity that stores the information of a patient status.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity(name = PatientStatusCriteriaEntity.TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = "PatientStatusCriteriaEntity.retrievByStatus", query = "select p from PATIENT_STATUS_CRITERIA p where p.status = ?1") })
public class PatientStatusCriteriaEntity extends PanacheEntity {

	/**
	 * The name of the table that will contains the patients.
	 */
	public static final String TABLE_NAME = "PATIENT_STATUS_CRITERIA";

	/**
	 * The current status of the patient.
	 */
	@Embedded
	public PatientStatusCriteria status;

	/**
	 * Retrieve the patient status criteria with the specified identifier.
	 *
	 * @param id identifier of the patient status criteria to retrieve.
	 *
	 * @return the patient status criteria associated to the identifier.
	 */
	public static Uni<PatientStatusCriteriaEntity> retrieve(long id) {

		final Uni<PatientStatusCriteriaEntity> action = PatientStatusCriteriaEntity.find("id", id).firstResult();
		return action.onItem().ifNull()
				.failWith(() -> new IllegalArgumentException("Not found a patient status criteria with the id " + id));
	}

	/**
	 * Retrieve the patient status criteria with the specified status.
	 *
	 * @param status of the entity to get.
	 *
	 * @return the patient status criteria associated to the status.
	 */
	public static Uni<PatientStatusCriteriaEntity> retrieveByStatus(PatientStatusCriteria status) {

		final Uni<PatientStatusCriteriaEntity> action = PatientStatusCriteriaEntity
				.find("#PatientStatusCriteriaEntity.retrievByStatus", status).firstResult();
		return action.onItem().ifNull().failWith(
				() -> new IllegalArgumentException("Not found a patient status criteria with the specified status."));
	}

	/**
	 * Retrieve the patient status criteria with the specified status or persist if
	 * not exist.
	 *
	 * @param status of the entity to get.
	 *
	 * @return the patient status criteria associated to the status.
	 */
	public static Uni<PatientStatusCriteriaEntity> retrieveOrPersist(PatientStatusCriteria status) {

		if (status == null) {

			return Uni.createFrom().failure(new IllegalArgumentException("The status is null"));

		} else {

			return retrieveByStatus(status).onFailure().recoverWithUni(() -> {

				final var entity = new PatientStatusCriteriaEntity();
				entity.status = status;
				return entity.persistAndFlush();

			});

		}

	}
}
