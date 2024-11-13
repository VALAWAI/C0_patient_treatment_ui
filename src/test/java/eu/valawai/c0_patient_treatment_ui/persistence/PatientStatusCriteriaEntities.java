/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteria;
import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteriaTest;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

/**
 * Utility classes to manage the {@link PatientStatusCriteriaEntity} over tests.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class PatientStatusCriteriaEntities {

	/**
	 * Create a random patient status criteria.
	 *
	 * @return the random status.
	 */
	public static Uni<PatientStatusCriteriaEntity> nextRandom() {

		return PatientStatusCriteriaEntities.nextUndefinedPatientStatusCriteria().chain(status -> {

			final var entity = new PatientStatusCriteriaEntity();
			entity.status = status;
			return entity.persistAndFlush();

		});

	}

	/**
	 * Return the identifier of a non defined patient status criteria.
	 *
	 * @return the identifier of a non defined patient status criteria.
	 */
	public static Uni<Long> undefined() {

		final var id = ValueGenerator.rnd().nextLong();
		return PatientStatusCriteriaEntity.find("id", id).firstResult().onItem().ifNotNull()
				.transformToUni(any -> PatientStatusCriteriaEntities.undefined()).onItem().ifNull().continueWith(id);

	}

	/**
	 * Obtain the last patient status criteria or create one if not exist.
	 *
	 * @return the last patient status criteria.
	 */
	public static Uni<PatientStatusCriteriaEntity> last() {

		final Uni<PatientStatusCriteriaEntity> find = PatientStatusCriteriaEntity.findAll(Sort.descending("id"))
				.firstResult();
		return find.onItem().ifNull().switchTo(() -> nextRandom());

	}

	/**
	 * Create a random patient status criteria that is not defined.
	 *
	 * @return the random undefined status.
	 */
	public static Uni<PatientStatusCriteria> nextUndefinedPatientStatusCriteria() {

		final var status = new PatientStatusCriteriaTest().nextModel();
		return PatientStatusCriteriaEntity.retrieveByStatus(status).onItem()
				.transformToUni(entity -> nextUndefinedPatientStatusCriteria()).onFailure().recoverWithItem(status);

	}

}
