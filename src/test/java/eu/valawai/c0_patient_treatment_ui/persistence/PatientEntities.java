/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import io.smallrye.mutiny.Uni;

/**
 * Utility classes to manage the {@link PatientEntity} over tests.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class PatientEntities {

	/**
	 * Create a random patient status.
	 *
	 * @return the random status.
	 */
	public static PatientEntity nextRandom() {

		final var entity = new PatientEntity();
		entity.updateTime = ValueGenerator.rnd().nextLong(0, TimeManager.now() - 360000);
		entity.name = ValueGenerator.nextPattern("Patient name {0}");
		entity.status = new PatientStatusCriteriaTest().nextModel();
		return entity;

	}

	/**
	 * Create a new {@link PatientEntity} and store it.
	 *
	 * @return the created patient.
	 */
	public static Uni<PatientEntity> nextAndPersist() {

		final var entity = nextRandom();
		entity.id = null;
		return entity.persist();

	}

	/**
	 * Return the {@link PatientEntity}] associated to an identifier.
	 *
	 * @param id of the patient entity.
	 *
	 * @return the patient entity associated to the id or an error if it is not
	 *         found.
	 */
	public static Uni<PatientEntity> byId(long id) {

		final Uni<PatientEntity> find = PatientEntity.findById(id);
		return find.onItem().ifNull().failWith(new IllegalArgumentException("Not found a patient with the id " + id));
	}

}
