/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatient;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatientPage;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.Patient;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

/**
 * An entity that stores the information of a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity(name = PatientEntity.TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = "PatientEntity.MinPatientTotal", query = "select count(*) from PATIENTS p where p.name ILIKE ?1") })
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
	@ManyToOne(targetEntity = PatientStatusCriteriaEntity.class, fetch = FetchType.EAGER)
	public PatientStatusCriteriaEntity status;

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
		this.updateTime = 0;

	}

	/**
	 * Update the current patient.
	 *
	 * @return nothing if the patient has been updated or an exception that explain
	 *         why can not be updated.
	 */
	public Uni<Void> update() {

		this.updateTime = TimeManager.now();
		final var builder = UpdateQueryBuilder.withEntity(this).withParam("name", this.name)
				.withParam("updateTime", this.updateTime).withParam("status", this.status);
		final var query = builder.query();
		final var params = builder.parameters();
		return PatientEntity.update(query, params).onItem().transformToUni(updated -> {

			if (Integer.valueOf(1).equals(updated)) {

				return Uni.createFrom().nullItem();

			} else {

				return Uni.createFrom()
						.failure(() -> new IllegalArgumentException("Not found a patient with the id " + this.id));
			}

		});

	}

	/**
	 * Retrieve the patient with the specified identifier.
	 *
	 * @param id identifier of the patient to retrieve.
	 *
	 * @return the patient associated to the identifier, or fail if not found.
	 */
	public static Uni<PatientEntity> retrieve(long id) {

		final Uni<PatientEntity> action = PatientEntity.find("id", id).firstResult();
		return action.onItem().ifNull().failWith(new IllegalArgumentException("Not found a patient with the id " + id));
	}

	/**
	 * Delete the patient with the specified identifier.
	 *
	 * @param id identifier of the patient to delete.
	 *
	 * @return nothing if the patient is deleted or an exception that explains why
	 *         cannot be deleted.
	 */
	public static Uni<Void> delete(long id) {

		return PatientEntity.delete("id", id).onItem().transformToUni(updated -> {

			if (Long.valueOf(1l).equals(updated)) {

				return Uni.createFrom().nullItem();

			} else {

				return Uni.createFrom()
						.failure(() -> new IllegalArgumentException("Not found a patient with the id " + id));
			}

		});
	}

	/**
	 * Return the {@link MinPatientPage} that satisfy the query.
	 *
	 * @param pattern to match the name of the patients.
	 * @param sort    how to order the patients to return.
	 * @param from    index of the first patient to return.
	 * @param limit   maximum number of patients to return.
	 *
	 * @return the page that satisfy the query.
	 */
	public static Uni<MinPatientPage> getMinPatientPageFor(String pattern, Sort sort, int from, int limit) {

		return count("#PatientEntity.MinPatientTotal", pattern).chain(total -> {

			final var page = new MinPatientPage();
			page.total = Math.toIntExact(total);
			if (from >= total) {

				return Uni.createFrom().item(page);

			} else {

				final var to = Math.toIntExact(Math.min(from + limit, total)) - 1;
				final Uni<List<PatientEntity>> find = PatientEntity.find("name ILIKE ?1", sort, pattern).range(from, to)
						.list();
				return find.map(patients -> {

					final var size = patients.size();
					page.patients = new ArrayList<>(size);
					for (final var patient : patients) {

						final var minPatient = patient.toMinPatient();
						page.patients.add(minPatient);
					}

					return page;
				});

			}

		}).onFailure().recoverWithItem(error ->

		{

			Log.errorv(error, "Cannot find for the patients.");
			return new MinPatientPage();
		});

	}

	/**
	 * Return the information of a {@link Patient}.
	 *
	 * @param id of the patient to get.
	 *
	 * @return the patient associated to the identifier or an exception that
	 *         explains why can not obtain it.
	 */
	public static Uni<Patient> retrievePatient(long id) {

		return retrieve(id).map(patientEntity -> {

			final var patient = new Patient();
			patient.id = patientEntity.id;
			patient.name = patientEntity.name;
			patient.status = patientEntity.status.status;
			patient.updateTime = patientEntity.updateTime;
			return patient;

		});
	}

	/**
	 * Rwtun the min patient for this entity.
	 *
	 * @return the min patient associated to this patient.
	 */
	public MinPatient toMinPatient() {

		final var model = new MinPatient();
		model.id = this.id;
		model.name = this.name;
		return model;
	}
}
