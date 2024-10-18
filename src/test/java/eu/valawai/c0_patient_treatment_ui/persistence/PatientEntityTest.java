/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;

/**
 * Test the {@link PatientEntity}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
public class PatientEntityTest {

	/**
	 * Generate a random patient entity.
	 *
	 * @return a random patient entity.
	 */
	public static PatientEntity nextPatient() {

		final var patient = new PatientEntity();
		patient.name = ValueGenerator.nextPattern("Patient name {0}");
		return patient;
	}

	/**
	 * Do a CRUD operation over a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldDoCRUD(TransactionalUniAsserter asserter) {

		final var patient = nextPatient();
		asserter.assertThat(() -> patient.store(), patientId -> {

			assertNotNull(patientId);
			asserter.assertThat(() -> PatientEntity.findById(patientId), found -> {
				assertNotNull(found);
				assertEquals(patient.name, ((PatientEntity) found).name);
			});

			final var newName = "New name " + patient.name;
			asserter.assertThat(() -> PatientEntity.update("name= ?1 where id = ?2", newName, patient.id),
					updated -> assertEquals(1, updated));

			asserter.assertThat(() -> PatientEntity.findById(patientId), found -> {
				assertNotNull(found);
				assertNotEquals(patient.name, ((PatientEntity) found).name);
				assertEquals(newName, ((PatientEntity) found).name);
			});

			asserter.execute(() -> patient.delete());

			asserter.assertThat(() -> PatientEntity.findById(patientId), found -> {
				assertNull(found);
			});
		});
	}

}
