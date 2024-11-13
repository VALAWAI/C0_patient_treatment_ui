/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;

/**
 * Test the {@link TreatmentEntity}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
public class TreatmentEntityTest {

	/**
	 * Should retrieve a treatment status criteria.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrieve(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.last(), last -> asserter.putData("LAST", last));
		asserter.assertThat(() -> {

			final TreatmentEntity last = (TreatmentEntity) asserter.getData("LAST");
			return TreatmentEntity.retrieve(last.id);

		}, retrieved -> {

			assertNotNull(retrieved);
			final TreatmentEntity last = (TreatmentEntity) asserter.getData("LAST");
			assertEquals(last.id, retrieved.id);
			assertEquals(last.createdTime, retrieved.createdTime);
			assertNotNull(retrieved.patient);
			assertEquals(last.patient.id, retrieved.patient.id);
			assertNotNull(retrieved.beforeStatus);
			assertEquals(last.beforeStatus.id, retrieved.beforeStatus.id);
			assertEquals(last.treatmentActions, retrieved.treatmentActions);
			assertNotNull(retrieved.expectedStatus);
			assertEquals(last.expectedStatus.id, retrieved.expectedStatus.id);

		});
	}

	/**
	 * Should retrieve a treatment status criteria.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotRetrieve(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.undefined(), undefined -> asserter.putData("UNDEFINED", undefined));
		asserter.assertFailedWith(() -> {

			final Long undefined = (Long) asserter.getData("UNDEFINED");
			return TreatmentEntity.retrieve(undefined);

		}, IllegalArgumentException.class);
	}

	/**
	 * Should delete a treatment.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldDelete(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.nextRandom(), next -> asserter.putData("NEXT", next));
		asserter.assertNull(() -> {

			final TreatmentEntity next = (TreatmentEntity) asserter.getData("NEXT");
			return TreatmentEntity.delete(next.id);

		});
		asserter.assertNull(() -> {
			final TreatmentEntity next = (TreatmentEntity) asserter.getData("NEXT");
			return TreatmentEntity.findById(next.id);
		});
		asserter.assertFailedWith(() -> {

			final TreatmentEntity next = (TreatmentEntity) asserter.getData("NEXT");
			return TreatmentEntity.delete(next.id);

		}, IllegalArgumentException.class);
	}

}
