/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import java.util.List;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.api.v1.treatments.Treatment;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentPayload;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

/**
 * An entity that stores the information of a treatment.
 *
 * @see Treatment
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity(name = TreatmentEntity.TABLE_NAME)
public class TreatmentEntity extends PanacheEntity {

	/**
	 * The name of the table that will contains the treatments.
	 */
	public static final String TABLE_NAME = "TREATMENTS";

	/**
	 * The epoch time, in seconds, when the treatment has been created.
	 */
	public long createdTime;

	/**
	 * The patient that has this treatment.
	 */
	@ManyToOne(targetEntity = PatientEntity.class, fetch = FetchType.EAGER)
	public PatientEntity patient;

	/**
	 * The status before to apply the treatment.
	 */
	@ManyToOne(targetEntity = PatientStatusCriteriaEntity.class, fetch = FetchType.EAGER)
	public PatientStatusCriteriaEntity beforeStatus;

	/**
	 * The treatment actions to apply over the patient.
	 */
	public List<TreatmentAction> treatmentActions;

	/**
	 * The expected status of the patient after applying the treatment.
	 */
	@ManyToOne(targetEntity = PatientStatusCriteriaEntity.class, fetch = FetchType.EAGER)
	public PatientStatusCriteriaEntity expectedStatus;

	/**
	 * Create a new treatment.
	 */
	public TreatmentEntity() {

		this.createdTime = TimeManager.now();

	}

	/**
	 * Retrieve the treatment with the specified identifier.
	 *
	 * @param id identifier of the treatment to retrieve.
	 *
	 * @return the patient associated to the identifier, or fail if not found.
	 */
	public static Uni<TreatmentEntity> retrieve(long id) {

		final Uni<TreatmentEntity> action = TreatmentEntity.find("id", id).firstResult();
		return action.onItem().ifNull()
				.failWith(new IllegalArgumentException("Not found a treatment with the id " + id));
	}

	/**
	 * Delete the treatment with the specified identifier.
	 *
	 * @param id identifier of the treatment to delete.
	 *
	 * @return nothing if the treatment is deleted or an exception that explains why
	 *         cannot be deleted.
	 */
	public static Uni<Void> delete(long id) {

		return TreatmentEntity.delete("id", id).onItem().transformToUni(updated -> {

			if (Long.valueOf(1l).equals(updated)) {

				return Uni.createFrom().nullItem();

			} else {

				return Uni.createFrom()
						.failure(() -> new IllegalArgumentException("Not found a treatment with the id " + id));
			}

		});
	}

	/**
	 * Retrieve the {@link Treatment} with the specified identifier.
	 *
	 * @param id identifier of the treatment to return.
	 *
	 * @return the treatment associated to the identifier.
	 */
	public static Uni<Treatment> retrieveTreatment(long id) {

		return retrieve(id).map(entity -> entity.toTreatment());
	}

	/**
	 * Convert this entity to a {@link Treatment}.
	 *
	 * @return the treatment with the data of this entity.
	 */
	public Treatment toTreatment() {

		final var treatment = new Treatment();
		treatment.id = this.id;
		treatment.createdTime = this.createdTime;
		if (this.patient != null) {

			treatment.patient = this.patient.toMinPatient();
		}
		if (this.beforeStatus != null) {

			treatment.beforeStatus = this.beforeStatus.status;
		}
		treatment.actions = this.treatmentActions;
		if (this.expectedStatus != null) {

			treatment.expectedStatus = this.expectedStatus.status;
		}
		return treatment;
	}

	/**
	 * Convert this entity to a {@link TreatmentPayload}.
	 *
	 * @return the treatment payload with the data of this
	 */
	public TreatmentPayload toTreatmentPayload() {

		final var payload = new TreatmentPayload();
		payload.id = String.valueOf(this.id);
		if (this.patient != null) {

			payload.patient_id = String.valueOf(this.patient.id);
		}
		payload.created_time = this.createdTime;
		payload.actions = this.treatmentActions;
		if (this.beforeStatus != null) {

			payload.before_status = this.beforeStatus.toPatientStatusCriteriaPayload();
		}

		if (this.expectedStatus != null) {

			payload.expected_status = this.expectedStatus.toPatientStatusCriteriaPayload();
		}
		return payload;

	}

}
