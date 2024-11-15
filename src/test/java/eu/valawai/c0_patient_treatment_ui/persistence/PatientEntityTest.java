/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatientPage;
import io.quarkus.panache.common.Sort;
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
	 * Should retrieve a patient status criteria.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrieve(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), last -> asserter.putData("LAST", last));
		asserter.assertThat(() -> {

			final PatientEntity last = (PatientEntity) asserter.getData("LAST");
			return PatientEntity.retrieve(last.id);

		}, retrieved -> {

			assertNotNull(retrieved);
			final PatientEntity last = (PatientEntity) asserter.getData("LAST");
			assertEquals(last.id, retrieved.id);
			assertEquals(last.name, retrieved.name);
			assertNotNull(retrieved.status);
			assertEquals(last.status.id, retrieved.status.id);
			assertEquals(last.updateTime, retrieved.updateTime);

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

		asserter.assertThat(() -> PatientEntities.undefined(), undefined -> asserter.putData("UNDEFINED", undefined));
		asserter.assertFailedWith(() -> {

			final Long undefined = (Long) asserter.getData("UNDEFINED");
			return PatientEntity.retrieve(undefined);

		}, IllegalArgumentException.class);
	}

	/**
	 * Should delete a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldDelete(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.nextRandom(), next -> asserter.putData("NEXT", next));
		asserter.assertNull(() -> {

			final PatientEntity next = (PatientEntity) asserter.getData("NEXT");
			return PatientEntity.delete(next.id);

		});
		asserter.assertNull(() -> {
			final PatientEntity next = (PatientEntity) asserter.getData("NEXT");
			return PatientEntity.findById(next.id);
		});
		asserter.assertFailedWith(() -> {

			final PatientEntity next = (PatientEntity) asserter.getData("NEXT");
			return PatientEntity.delete(next.id);

		}, IllegalArgumentException.class);
	}

	/**
	 * Should update a patient name.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdatePatientName(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.nextRandom(), next -> asserter.putData("PATIENT", next));
		asserter.execute(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			asserter.putData("BEFORE_NAME", patient.name);
			final var name = patient.name;
			do {

				patient.name = ValueGenerator.nextPattern("Patient name {0}");

			} while (patient.name.equals(name));
			asserter.putData("AFTER_NAME", patient.name);
			asserter.putData("NOW", TimeManager.now());
			return patient.update();

		}).assertThat(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			return PatientEntity.retrieve(patient.id);

		}, found -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			final long now = (long) asserter.getData("NOW");
			final String beforeName = (String) asserter.getData("BEFORE_NAME");
			final String afterName = (String) asserter.getData("AFTER_NAME");
			assertNotEquals(beforeName, found.name);
			assertEquals(afterName, found.name);
			if (patient.status != null) {

				assertNotNull(found.status);
				assertEquals(patient.status.id, found.status.id);
			}
			assertTrue(found.updateTime >= now);

		});
	}

	/**
	 * Should update only a patient name.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateOnlyPatientName(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), last -> {

			asserter.putData("PATIENT", last);
			asserter.putData("PATIENT_ID", last.id);
			asserter.putData("BEFORE_NAME", last.name);
			String name = null;
			do {

				name = ValueGenerator.nextPattern("Patient name {0}");

			} while (last.name.equals(name));
			asserter.putData("AFTER_NAME", name);

		});
		asserter.execute(() -> {

			final long patientId = (Long) asserter.getData("PATIENT_ID");
			final String afterName = (String) asserter.getData("AFTER_NAME");
			final var patient = new PatientEntity();
			patient.id = patientId;
			patient.name = afterName;
			asserter.putData("NOW", TimeManager.now());
			return patient.update();

		});
		asserter.assertThat(() -> {

			final long patientId = (Long) asserter.getData("PATIENT_ID");
			return PatientEntity.retrieve(patientId);

		}, found -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			final long now = (long) asserter.getData("NOW");
			final String beforeName = (String) asserter.getData("BEFORE_NAME");
			final String afterName = (String) asserter.getData("AFTER_NAME");
			assertNotEquals(beforeName, found.name);
			assertEquals(afterName, found.name);
			if (patient.status != null) {

				assertNotNull(found.status);
				assertEquals(patient.status.id, found.status.id);
			}
			assertTrue(found.updateTime >= now);

		});
	}

	/**
	 * Should update a patient status.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdatePatientStatus(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.nextRandom(), next -> asserter.putData("PATIENT", next));
		asserter.assertThat(() -> PatientStatusCriteriaEntities.nextRandom(),
				next -> asserter.putData("AFTER_STATUS", next));
		asserter.execute(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			asserter.putData("BEFORE_STATUS", patient.status);
			patient.status = (PatientStatusCriteriaEntity) asserter.getData("AFTER_STATUS");
			asserter.putData("NOW", TimeManager.now());
			return patient.update();

		}).assertThat(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			return PatientEntity.retrieve(patient.id);

		}, found -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			final long now = (long) asserter.getData("NOW");
			final PatientStatusCriteriaEntity beforeStatus = (PatientStatusCriteriaEntity) asserter
					.getData("BEFORE_STATUS");
			final PatientStatusCriteriaEntity afterStatus = (PatientStatusCriteriaEntity) asserter
					.getData("AFTER_STATUS");
			assertNotNull(found.status);
			if (patient.status != null) {

				assertNotEquals(beforeStatus, found.status);

			}
			assertEquals(afterStatus.id, found.status.id);
			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);

		});
	}

	/**
	 * Should update only a patient status.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateOnlyPatientStatus(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), next -> asserter.putData("PATIENT", next));
		asserter.assertThat(() -> PatientStatusCriteriaEntities.nextRandom(),
				next -> asserter.putData("AFTER_STATUS", next));
		asserter.execute(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = (PatientStatusCriteriaEntity) asserter.getData("AFTER_STATUS");
			asserter.putData("NOW", TimeManager.now());
			return newPatient.update();

		});
		asserter.assertThat(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			return PatientEntity.retrieve(patient.id);

		}, found -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			final long now = (long) asserter.getData("NOW");
			final PatientStatusCriteriaEntity beforeStatus = (PatientStatusCriteriaEntity) asserter
					.getData("BEFORE_STATUS");
			final PatientStatusCriteriaEntity afterStatus = (PatientStatusCriteriaEntity) asserter
					.getData("AFTER_STATUS");
			assertNotNull(found.status);
			if (patient.status != null) {

				assertNotEquals(beforeStatus, found.status);

			}
			assertEquals(afterStatus.id, found.status.id);
			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);

		});
	}

	/**
	 * Should update a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdate(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), last -> {

			asserter.putData("PATIENT", last);
			asserter.putData("PATIENT_ID", last.id);
			asserter.putData("BEFORE_NAME", last.name);
			asserter.putData("BEFORE_STATUS", last.status);
			String name = null;
			do {

				name = ValueGenerator.nextPattern("Patient name {0}");

			} while (last.name.equals(name));
			asserter.putData("AFTER_NAME", name);

		});
		asserter.assertThat(() -> PatientStatusCriteriaEntities.nextRandom(),
				next -> asserter.putData("AFTER_STATUS", next));
		asserter.execute(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			patient.name = (String) asserter.getData("AFTER_NAME");
			patient.status = (PatientStatusCriteriaEntity) asserter.getData("AFTER_STATUS");
			asserter.putData("NOW", TimeManager.now());
			return patient.update();

		}).assertThat(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			return PatientEntity.retrieve(patient.id);

		}, found -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			final long now = (long) asserter.getData("NOW");
			final PatientStatusCriteriaEntity beforeStatus = (PatientStatusCriteriaEntity) asserter
					.getData("BEFORE_STATUS");
			final PatientStatusCriteriaEntity afterStatus = (PatientStatusCriteriaEntity) asserter
					.getData("AFTER_STATUS");
			final String beforeName = (String) asserter.getData("BEFORE_NAME");
			final String afterName = (String) asserter.getData("AFTER_NAME");
			assertNotEquals(beforeName, found.name);
			assertEquals(afterName, found.name);
			assertNotNull(found.status);
			if (patient.status != null) {

				assertNotEquals(beforeStatus, found.status);

			}
			assertEquals(afterStatus.id, found.status.id);
			assertTrue(found.updateTime >= now);

		});
	}

	/**
	 * Should not update an undefined patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotUpdateUndefined(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.undefined(), undefined -> asserter.putData("UNDEFINED", undefined));
		asserter.assertFailedWith(() -> {

			final Long undefined = (Long) asserter.getData("UNDEFINED");
			final var patient = new PatientEntity();
			patient.id = undefined;
			patient.name = ValueGenerator.nextPattern("Patient name {0}");
			return patient.update();

		}, IllegalArgumentException.class);
	}

	/**
	 * Should not update an undefined patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotUpdateWithUndefinedStatus(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), next -> asserter.putData("PATIENT", next));
		asserter.assertThat(() -> PatientStatusCriteriaEntities.undefined(),
				undefined -> asserter.putData("UNDEFINED", undefined));
		asserter.assertFailedWith(() -> {

			final PatientEntity patient = (PatientEntity) asserter.getData("PATIENT");
			patient.status = new PatientStatusCriteriaEntity();
			patient.status.id = (Long) asserter.getData("UNDEFINED");
			return patient.update();

		}, ConstraintViolationException.class);
	}

	/**
	 * Should return an empty min patient page when no pattern.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinPatientPageForWhenBadIndex(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.assertThat(() -> PatientEntity.getMinPatientPageFor("%", Sort.descending("id"), -1, 10),
				found -> assertEquals(expected, found));

	}

	/**
	 * Should return an empty min patient page if the index is too high.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinPatientPageForWhenIndexIsTooHigh(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.assertThat(() -> PatientEntity.count(), total -> expected.total = Math.toIntExact(total)).assertThat(
				() -> PatientEntity.getMinPatientPageFor("%", Sort.descending("id"), expected.total + 1, 10),
				found -> assertEquals(expected, found));

	}

	/**
	 * Should return an empty min patient page because any patient match the query.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinPatientPageForWhenAnyMatch(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.assertThat(
				() -> PatientEntity.getMinPatientPageFor(UUID.randomUUID().toString(), Sort.descending("id"), 0, 10),
				found -> assertEquals(expected, found));

	}

	/**
	 * Should get some patients.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetMinPatientPageForSome(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		final var limit = 20;
		asserter.execute(() -> PatientEntities.populateWith(100))
				.assertThat(() -> PatientEntity.count(), total -> expected.total = Math.toIntExact(total))
				.assertThat(() -> PatientEntity.getMinPatientPageFor("%", Sort.ascending("id"), 10, limit), found -> {

					assertEquals(expected.total, found.total);
					assertNotNull(found.patients);
					assertEquals(limit, found.patients.size());
					var lastId = 0l;
					for (final var patient : found.patients) {

						assertTrue(lastId < patient.id);
						lastId = patient.id;
					}
				});

	}

	/**
	 * Should get some patients.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetMinPatientPageForMatchingPattern(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.execute(() -> PatientEntities.populateWith(100));
		asserter.assertThat(() -> PatientEntity.count("name LIKE '% 1%'"),
				total -> expected.total = Math.toIntExact(total));
		asserter.assertThat(() -> PatientEntity.getMinPatientPageFor("% 1%", Sort.descending("name"), 0, 10), found -> {

			assertEquals(expected.total, found.total);
			assertNotNull(found.patients);
			assertTrue(found.patients.size() <= 10);
			var lastName = "ZZZZZZZZZZZZZZZZZZZZZZZZZZ";
			for (final var patient : found.patients) {

				assertTrue(patient.name.compareTo(lastName) <= 0);
				lastName = patient.name;
			}
		});

	}

	/**
	 * Should get empty patients page when offset is too high.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinPatientPageForHightOffset(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.assertThat(() -> PatientEntity.count(), total -> expected.total = Math.toIntExact(total));
		asserter.assertThat(() -> PatientEntity.getMinPatientPageFor("%", Sort.ascending("id"), expected.total, 10),
				found -> {

					assertEquals(expected.total, found.total);
					assertNull(found.patients);
				});

	}

}
