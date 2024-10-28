/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import org.hibernate.validator.constraints.Length;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

/**
 * An entity that stores the information of a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity(name = PatientEntity.TABLE_NAME)
public class PatientEntity extends PanacheEntity {

	/**
	 * The name of the table that will contains the patients.
	 */
	public static final String TABLE_NAME = "PATIENTS";

	/**
	 * The name of the patient.
	 */
	@Length(max = 1024)
	public String name;

	/**
	 * The current status of the patient.
	 */
	@Embedded
	public Status status;

	/**
	 * The time when the patient has been updated.
	 */
	public long updateTime;

	/**
	 * Create a new patient.
	 */
	public PatientEntity() {

		this.name = null;
		this.status = null;
		this.updateTime = TimeManager.now();

	}

	/**
	 * Store the current patient.
	 *
	 * @return the identifier of the stored entity.
	 */
	public static Uni<PatientEntity> create() {

		final Uni<PatientEntity> action = new PatientEntity().persist();
		return action.onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot create a patient entity");
			return null;
		});
	}

	/**
	 * Update the current patient.
	 *
	 * @return if it has been updated return {@code true}, otherwise return
	 *         {@code false}.
	 */
	public Uni<Boolean> update() {

		var builder = UpdateQueryBuilder.withEntity(this).withParam("name", this.name).withParam("updateTime",
				TimeManager.now());
		if (this.status != null) {

			builder = builder.withParam("status.ageRange", this.status.ageRange)
					.withParam("status.ccd", this.status.ccd)
					.withParam("status.clinicalRiskGroup", this.status.clinicalRiskGroup)
					.withParam("status.discomfortDegree", this.status.discomfortDegree)
					.withParam("status.expectedSurvival", this.status.expectedSurvival)
					.withParam("status.frailVIG", this.status.frailVIG)
					.withParam("status.hasAdvanceDirectives", this.status.hasAdvanceDirectives)
					.withParam("status.hasBeenInformed", this.status.hasBeenInformed)
					.withParam("status.hasCognitiveImpairment", this.status.hasCognitiveImpairment)
					.withParam("status.hasEmocionalPain", this.status.hasEmocionalPain)
					.withParam("status.hasSocialSupport", this.status.hasSocialSupport)
					.withParam("status.independenceAtAdmission", this.status.independenceAtAdmission)
					.withParam("status.independenceInstrumentalActivities",
							this.status.independenceInstrumentalActivities)
					.withParam("status.isCoerced", this.status.isCoerced)
					.withParam("status.isCompotent", this.status.isCompotent)
					.withParam("status.maca", this.status.maca);
		}
		final var query = builder.query();
		final var params = builder.parameters();
		return PatientEntity.update(query, params).onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot create update the patient for {0}", this.id);
			return null;

		}).map(updated -> updated != null && updated == 1);

	}

	/**
	 * Retrieve the patient with the specified identifier.
	 *
	 * @param id identifier of the patient to retrieve.
	 *
	 * @return the patient associated to the identifier, or {@code null} if cannot
	 *         retrieve it.
	 */
	public static Uni<PatientEntity> retrieve(long id) {

		final Uni<PatientEntity> action = PatientEntity.find("id", id).firstResult();
		return action.onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot get a patient with the id {0}", id);
			return null;

		});
	}

	/**
	 * Delete the patient with the specified identifier.
	 *
	 * @param id identifier of the patient to delete.
	 *
	 * @return the patient associated to the identifier, or {@code null} if cannot
	 *         delete it.
	 */
	public static Uni<Boolean> delete(long id) {

		return PatientEntity.delete("id", id).onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot get a patient with the id {0}", id);
			return null;

		}).map(deleted -> deleted != null && deleted == 1);
	}

}
