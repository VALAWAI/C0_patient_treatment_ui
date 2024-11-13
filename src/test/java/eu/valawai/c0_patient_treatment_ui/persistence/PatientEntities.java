/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import java.util.Collections;
import java.util.List;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

/**
 * Utility classes to manage the {@link PatientEntity} over tests.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class PatientEntities {

	/**
	 * Create a random patient.
	 *
	 * @return the random status.
	 */
	public static Uni<PatientEntity> nextRandom() {

		return PatientStatusCriteriaEntities.nextRandom().chain(status -> {

			final var entity = new PatientEntity();
			entity.updateTime = ValueGenerator.rnd().nextLong(0, TimeManager.now() - 360000);
			entity.name = ValueGenerator.nextPattern("Patient name {0}");
			entity.status = status;
			return entity.persistAndFlush();
		});

	}

	/**
	 * Populate the database with the a minimum of patients.
	 *
	 * @param min number minimum of patients to be defined on the database.
	 *
	 * @return the created patients.
	 */
	public static Uni<List<PatientEntity>> populateWith(int min) {

		return PatientEntity.count().chain(total -> {

			final var max = min - Math.toIntExact(total);
			if (max > 0) {

				return Multi.createFrom().range(0, max).onItem().transformToUniAndMerge(index -> {

					return nextRandom();

				}).collect().asList();

			} else {

				return Uni.createFrom().item(Collections.emptyList());
			}

		});
	}

	/**
	 * Obtain the last patient or create one if not exist.
	 *
	 * @return the last patient.
	 */
	public static Uni<PatientEntity> last() {

		final Uni<PatientEntity> find = PatientEntity.findAll(Sort.descending("id")).firstResult();
		return find.onItem().ifNull().switchTo(() -> nextRandom());

	}

	/**
	 * Return the identifier of a non defined patient.
	 *
	 * @return the identifier of a non defined patient.
	 */
	public static Uni<Long> undefined() {

		final var id = ValueGenerator.rnd().nextLong();
		return PatientEntity.find("id", id).firstResult().onItem().ifNotNull()
				.transformToUni(any -> PatientEntities.undefined()).onItem().ifNull().continueWith(id);

	}

}
