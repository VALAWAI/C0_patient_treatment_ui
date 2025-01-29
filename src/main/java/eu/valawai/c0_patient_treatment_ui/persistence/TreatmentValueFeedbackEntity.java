/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentValueFeedbackPayload;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

/**
 * An entity that has the information of a treatment value feedback.
 *
 * @see TreatmentEntity#valueFeedbacks
 *
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity(name = TreatmentValueFeedbackEntity.TABLE_NAME)
public class TreatmentValueFeedbackEntity extends PanacheEntity {

	/**
	 * The name of the table that will contains the treatments.
	 */
	public static final String TABLE_NAME = "TREATMENT_VALUE_FEEDBACKS";

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
	 * The name of the value that the treatment is aligned.
	 */
	public String valueName;

	/**
	 * The alignment of the value with the the treatment.
	 */
	public double alignment;

	/**
	 * Persist the feedback.
	 *
	 * @param payload with the feedback to persist.
	 *
	 * @return the stored feedback.
	 */
	public static Uni<TreatmentValueFeedbackEntity> store(TreatmentValueFeedbackPayload payload) {

		try {

			final var id = Long.parseLong(payload.treatment_id);
			return TreatmentEntity.retrieve(id).chain(treatment -> {

				final var entity = new TreatmentValueFeedbackEntity();
				entity.treatment = treatment;
				entity.createdTime = TimeManager.now();
				entity.valueName = payload.value_name;
				entity.alignment = payload.alignment;
				return entity.persistAndFlush();
			});

		} catch (final NumberFormatException badId) {

			return Uni.createFrom().failure(badId);
		}

	}

}
