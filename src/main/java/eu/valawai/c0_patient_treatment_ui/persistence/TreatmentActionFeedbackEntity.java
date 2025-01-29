/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentActionFeedback;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentActionFeedbackPayload;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

/**
 * An entity that has the information of a treatment action feedback.
 *
 * @see TreatmentEntity#actionFeedbacks
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity(name = TreatmentActionFeedbackEntity.TABLE_NAME)
public class TreatmentActionFeedbackEntity extends PanacheEntity {

	/**
	 * The name of the table that will contains the treatments.
	 */
	public static final String TABLE_NAME = "TREATMENT_ACTION_FEEDBACKS";

	/**
	 * The treatment associated to the feedback.
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	public TreatmentEntity treatment;

	/**
	 * The epoch time, in seconds, when the feedback has been reported.
	 */
	public long createdTime;

	/**
	 * The action that this is feedback.
	 */
	public TreatmentAction action;

	/**
	 * If feedback over the action of the treatment.
	 */
	public TreatmentActionFeedback feedback;

	/**
	 * Persist the feedback.
	 *
	 * @param payload with the feedback to persist.
	 *
	 * @return the stored feedback.
	 */
	public static Uni<TreatmentActionFeedbackEntity> store(TreatmentActionFeedbackPayload payload) {

		try {

			final var id = Long.parseLong(payload.treatment_id);
			return TreatmentEntity.retrieve(id).chain(treatment -> {

				if (treatment.treatmentActions == null || !treatment.treatmentActions.contains(payload.action)) {

					return Uni.createFrom()
							.failure(new IllegalArgumentException("The action is not defined in the treatment."));

				} else {

					final var entity = new TreatmentActionFeedbackEntity();
					entity.treatment = treatment;
					entity.createdTime = TimeManager.now();
					entity.action = payload.action;
					entity.feedback = payload.feedback;
					return entity.persistAndFlush();
				}
			});

		} catch (final NumberFormatException badId) {

			return Uni.createFrom().failure(badId);
		}

	}

}
