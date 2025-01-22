/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

/**
 * Utility classes to manage the {@link TreatmentEntity} over tests.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class TreatmentEntities {

	/**
	 * Create a random treatment status.
	 *
	 * @return the random status.
	 */
	public static Uni<TreatmentEntity> nextRandom() {

		final var max = ValueGenerator.rnd().nextInt(1, TreatmentAction.values().length);
		return nextRandom(max);

	}

	/**
	 * Create a random treatment status.
	 *
	 * @param max number maximum of actions for the treatment.
	 *
	 * @return the random status.
	 */
	public static Uni<TreatmentEntity> nextRandom(int max) {

		return PatientEntities.nextRandom().chain(patient -> {
			return PatientStatusCriteriaEntities.nextRandom().chain(before -> {

				return PatientStatusCriteriaEntities.nextRandom().chain(after -> {

					final var entity = new TreatmentEntity();
					entity.patient = patient;
					entity.createdTime = ValueGenerator.rnd().nextLong(0, TimeManager.now() - 360000);
					entity.beforeStatus = before;
					entity.treatmentActions = new ArrayList<>(Arrays.asList(TreatmentAction.values()));
					Collections.shuffle(entity.treatmentActions, ValueGenerator.rnd());
					entity.treatmentActions = entity.treatmentActions.subList(0, Math.max(1, max));
					entity.expectedStatus = after;
					return entity.persistAndFlush();
				});
			});
		});

	}

	/**
	 * Populate the database with the a minimum of treatments.
	 *
	 * @param min number minimum of treatments to be defined on the database.
	 *
	 * @return an exception if can not create the required population.
	 */
	public static Uni<Void> populateWith(int min) {

		return TreatmentEntity.count().chain(total -> {

			if (total < min) {

				return nextRandom().chain(any -> populateWith(min));

			} else {

				return Uni.createFrom().nullItem();
			}

		});
	}

	/**
	 * Obtain the last treatment or create one if not exist.
	 *
	 * @return the last treatment.
	 */
	public static Uni<TreatmentEntity> last() {

		final Uni<TreatmentEntity> find = TreatmentEntity.findAll(Sort.descending("id")).firstResult();
		return find.onItem().ifNull().switchTo(() -> nextRandom());

	}

	/**
	 * Return the identifier of a non defined treatment.
	 *
	 * @return the identifier of a non defined treatment.
	 */
	public static Uni<Long> undefined() {

		final var id = ValueGenerator.rnd().nextLong();
		return TreatmentEntity.find("id", id).firstResult().onItem().ifNotNull()
				.transformToUni(any -> TreatmentEntities.undefined()).onItem().ifNull().continueWith(id);

	}

}
