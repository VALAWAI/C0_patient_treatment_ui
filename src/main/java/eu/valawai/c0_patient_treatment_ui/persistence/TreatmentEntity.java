/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.api.v1.treatments.MinTreatment;
import eu.valawai.c0_patient_treatment_ui.api.v1.treatments.MinTreatmentPage;
import eu.valawai.c0_patient_treatment_ui.api.v1.treatments.Treatment;
import eu.valawai.c0_patient_treatment_ui.api.v1.treatments.TreatmentActionWithFeedback;
import eu.valawai.c0_patient_treatment_ui.api.v1.treatments.TreatmentValue;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentPayload;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;

/**
 * An entity that stores the information of a treatment.
 *
 * @see Treatment
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity(name = TreatmentEntity.TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = "TreatmentEntity.MinTreatmentTotalPerPatientName", query = "select count(*) from TREATMENTS t where t.patient.name ILIKE ?1"),
		@NamedQuery(name = "TreatmentEntity.MinTreatmentTotalPerPatientId", query = "select count(*) from TREATMENTS t where t.patient.id = ?1")

})
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
	 * The received feedbacks for the treatment actions.
	 */
	@OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	public List<TreatmentActionFeedbackEntity> actionFeedbacks;

	/**
	 * The received value feedbacks for the treatment.
	 */
	@OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	public List<TreatmentValueFeedbackEntity> valueFeedbacks;

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
		if (this.expectedStatus != null) {

			treatment.expectedStatus = this.expectedStatus.status;
		}
		if (this.treatmentActions != null) {

			treatment.actions = new ArrayList<>();
			for (final var action : this.treatmentActions) {

				final var feedback = new TreatmentActionWithFeedback();
				feedback.action = action;
				treatment.actions.add(feedback);
			}

			if (this.actionFeedbacks != null) {

				Collections.sort(this.actionFeedbacks, (a1, a2) -> Long.compare(a1.createdTime, a2.createdTime));
				for (final var actionFeedback : this.actionFeedbacks) {

					for (final var action : treatment.actions) {

						if (action.action == actionFeedback.action) {

							action.feedback = actionFeedback.feedback;
							action.updatedTime = actionFeedback.createdTime;
							break;
						}
					}
				}
			}

		}

		if (this.valueFeedbacks != null) {

			treatment.values = new ArrayList<>();
			Collections.sort(this.valueFeedbacks, (a1, a2) -> Long.compare(a1.createdTime, a2.createdTime));
			for (final var valueFeedback : this.valueFeedbacks) {

				var found = false;
				for (final var value : treatment.values) {

					if (value.name.equalsIgnoreCase(valueFeedback.valueName)) {

						value.alignment = valueFeedback.alignment;
						value.updatedTime = valueFeedback.createdTime;
						found = true;
						break;
					}
				}

				if (!found) {

					final var value = new TreatmentValue();
					value.name = valueFeedback.valueName.toUpperCase();
					value.alignment = valueFeedback.alignment;
					value.updatedTime = valueFeedback.createdTime;
					treatment.values.add(value);
				}
			}
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

	/**
	 * Return the min treatment for this entity.
	 *
	 * @return the min treatment associated to this treatment.
	 */
	public MinTreatment toMinTreatment() {

		final var model = new MinTreatment();
		model.id = this.id;
		model.createdTime = this.createdTime;
		if (this.patient != null) {

			model.patient = this.patient.toMinPatient();
		}
		return model;
	}

	/**
	 * Return the {@link MinTreatmentPage} that satisfy the patient name.
	 *
	 * @param pattern to match the name of the treatments.
	 * @param sort    how to order the treatments to return.
	 * @param from    index of the first treatment to return.
	 * @param limit   maximum number of treatments to return.
	 *
	 * @return the page that satisfy the query.
	 */
	public static Uni<MinTreatmentPage> getMinTreatmentPageForPatientName(String pattern, Sort sort, int from,
			int limit) {

		return getMinTreatmentPageFor(
				() -> TreatmentEntity.count("#TreatmentEntity.MinTreatmentTotalPerPatientName", pattern),
				() -> TreatmentEntity.find("patient.name ILIKE ?1", sort, pattern), from, limit);
	}

	/**
	 * Return the {@link MinTreatmentPage} that satisfy the patient id.
	 *
	 * @param patientId identifier of the patient to get the treatments.
	 * @param sort      how to order the treatments to return.
	 * @param from      index of the first treatment to return.
	 * @param limit     maximum number of treatments to return.
	 *
	 * @return the page that satisfy the query.
	 */
	public static Uni<MinTreatmentPage> getMinTreatmentPageForPatient(long patientId, Sort sort, int from, int limit) {

		return getMinTreatmentPageFor(
				() -> TreatmentEntity.count("#TreatmentEntity.MinTreatmentTotalPerPatientId", patientId),
				() -> TreatmentEntity.find("patient.id = ?1", sort, patientId), from, limit);
	}

	/**
	 * Return the {@link MinTreatmentPage} that satisfy a query.
	 *
	 * @param counter query to obtain the total number of treatments.
	 * @param finder  query to obtain the treatments.
	 * @param from    index of the first treatment to return.
	 * @param limit   maximum number of treatments to return.
	 *
	 * @return the page that satisfy the query.
	 */
	private static Uni<MinTreatmentPage> getMinTreatmentPageFor(Supplier<Uni<Long>> counter,
			Supplier<PanacheQuery<TreatmentEntity>> finder, int from, int limit) {

		return counter.get().chain(total -> {

			final var page = new MinTreatmentPage();
			page.total = Math.toIntExact(total);
			if (from >= total) {

				return Uni.createFrom().item(page);

			} else {

				final var to = Math.toIntExact(Math.min(from + limit, total)) - 1;
				final Uni<List<TreatmentEntity>> find = finder.get().range(from, to).list();
				return find.map(treatments -> {

					final var size = treatments.size();
					page.treatments = new ArrayList<>(size);
					for (final var treatment : treatments) {

						final var minTreatment = treatment.toMinTreatment();
						page.treatments.add(minTreatment);
					}

					return page;
				});

			}

		}).onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot find for the treatments.");
			return new MinTreatmentPage();
		});

	}

}
