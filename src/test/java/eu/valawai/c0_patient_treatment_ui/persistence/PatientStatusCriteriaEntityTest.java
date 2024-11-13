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

import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteria;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;

/**
 * Test the {@link PatientStatusCriteriaEntity}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
public class PatientStatusCriteriaEntityTest {

	/**
	 * Should retrieve a patient status criteria.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrieve(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientStatusCriteriaEntities.last(), last -> asserter.putData("LAST", last));
		asserter.assertThat(() -> {

			final PatientStatusCriteriaEntity last = (PatientStatusCriteriaEntity) asserter.getData("LAST");
			return PatientStatusCriteriaEntity.retrieve(last.id);

		}, retrieved -> {

			assertNotNull(retrieved);
			final PatientStatusCriteriaEntity last = (PatientStatusCriteriaEntity) asserter.getData("LAST");
			assertEquals(last.id, retrieved.id);
			assertEquals(last.status, retrieved.status);

		});
	}

	/**
	 * Should retrieve a patient status criteria.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotRetrieve(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientStatusCriteriaEntities.undefined(),
				undefined -> asserter.putData("UNDEFINED", undefined));
		asserter.assertFailedWith(() -> {

			final Long undefined = (Long) asserter.getData("UNDEFINED");
			return PatientStatusCriteriaEntity.retrieve(undefined);

		}, IllegalArgumentException.class);
	}

	/**
	 * Should retrieve by a status.
	 *
	 * @param asserter to use in the tests.
	 *
	 * @see PatientStatusCriteriaEntity#retrieveByStatus
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrieveByStatus(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientStatusCriteriaEntities.last(), last -> asserter.putData("LAST", last));
		asserter.assertThat(() -> {

			final PatientStatusCriteriaEntity last = (PatientStatusCriteriaEntity) asserter.getData("LAST");
			return PatientStatusCriteriaEntity.retrieveByStatus(last.status);

		}, retrieved -> {

			assertNotNull(retrieved);
			final PatientStatusCriteriaEntity last = (PatientStatusCriteriaEntity) asserter.getData("LAST");
			assertEquals(last.id, retrieved.id);
			assertEquals(last.status, retrieved.status);

		});
	}

	/**
	 * Should retrieve or persist a status.
	 *
	 * @param asserter to use in the tests.
	 *
	 * @see PatientStatusCriteriaEntity#retrieveOrPersist
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrieveOrPersist(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientStatusCriteriaEntities.nextUndefinedPatientStatusCriteria(),
				last -> asserter.putData("UNDEFINED", last));
		asserter.assertThat(() -> {

			final PatientStatusCriteria undefined = (PatientStatusCriteria) asserter.getData("UNDEFINED");
			return PatientStatusCriteriaEntity.retrieveOrPersist(undefined);

		}, retrieved -> {

			assertNotNull(retrieved);
			asserter.putData("RETRIEVED", retrieved);
			final PatientStatusCriteria undefined = (PatientStatusCriteria) asserter.getData("UNDEFINED");
			assertEquals(undefined, retrieved.status);

		});
		asserter.assertThat(() -> {

			final PatientStatusCriteria undefined = (PatientStatusCriteria) asserter.getData("UNDEFINED");
			return PatientStatusCriteriaEntity.retrieveOrPersist(undefined);

		}, retrieved -> {

			assertNotNull(retrieved);
			final PatientStatusCriteriaEntity expected = (PatientStatusCriteriaEntity) asserter.getData("RETRIEVED");
			final PatientStatusCriteria undefined = (PatientStatusCriteria) asserter.getData("UNDEFINED");
			assertEquals(expected.id, retrieved.id);
			assertEquals(undefined, retrieved.status);

		});
	}

}
