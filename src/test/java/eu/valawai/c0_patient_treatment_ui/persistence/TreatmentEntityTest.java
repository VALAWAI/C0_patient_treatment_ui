/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.api.v1.treatments.MinTreatmentPage;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.smallrye.mutiny.Uni;

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

	/**
	 * Should return an empty min treatment page when no pattern.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinTreatmentPageForPatientNameWhenBadIndex(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		asserter.assertThat(() -> TreatmentEntity.getMinTreatmentPageForPatientName("%", Sort.descending("id"), -1, 10),
				found -> assertEquals(expected, found));

	}

	/**
	 * Should return an empty min treatment page if the index is too high.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinTreatmentPageForPatientNameWhenIndexIsTooHigh(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		asserter.assertThat(() -> TreatmentEntity.count(), total -> expected.total = Math.toIntExact(total))
				.assertThat(() -> TreatmentEntity.getMinTreatmentPageForPatientName("%", Sort.descending("id"),
						expected.total + 1, 10), found -> assertEquals(expected, found));

	}

	/**
	 * Should return an empty min treatment page because any treatment match the
	 * query.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinTreatmentPageForPatientNameWhenAnyMatch(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		asserter.assertThat(() -> TreatmentEntity.getMinTreatmentPageForPatientName(UUID.randomUUID().toString(),
				Sort.descending("id"), 0, 10), found -> assertEquals(expected, found));

	}

	/**
	 * Should get some treatments.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetMinTreatmentPageForPatientNameSome(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		final var limit = 20;
		asserter.execute(() -> TreatmentEntities.populateWith(100))
				.assertThat(() -> TreatmentEntity.count(), total -> expected.total = Math.toIntExact(total)).assertThat(
						() -> TreatmentEntity.getMinTreatmentPageForPatientName("%", Sort.ascending("id"), 10, limit),
						found -> {

							assertEquals(expected.total, found.total);
							assertNotNull(found.treatments);
							assertEquals(limit, found.treatments.size());
							var lastId = 0l;
							for (final var treatment : found.treatments) {

								assertTrue(lastId < treatment.id);
								lastId = treatment.id;
							}
						});

	}

	/**
	 * Should get some treatments.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetMinTreatmentPageForPatientNameMatchingPattern(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		final var sort = Sort.descending("patient.name").and("id", Direction.Descending);
		final var offset = 3;
		final var limit = 7;
		asserter.execute(() -> TreatmentEntities.populateWith(100));
		asserter.assertThat(() -> {

			final Uni<List<TreatmentEntity>> find = TreatmentEntity.find("patient.name ILIKE ?1", sort, "% 1%").list();
			return find;

		}, all -> {

			expected.total = all.size();
			if (expected.total >= offset) {

				expected.treatments = new ArrayList<>();
				final var max = Math.min(offset + limit, expected.total);
				final var treatments = all.subList(offset, max);
				for (final var treatment : treatments) {

					final var found = treatment.toMinTreatment();
					expected.treatments.add(found);
				}
			}

		});
		asserter.assertThat(() -> TreatmentEntity.getMinTreatmentPageForPatientName("% 1%", sort, offset, limit),
				found -> assertEquals(expected, found));

	}

	/**
	 * Should get empty treatments page when offset is too high.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinTreatmentPageForPatientNameHightOffset(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		asserter.assertThat(() -> TreatmentEntity.count(), total -> expected.total = Math.toIntExact(total));
		asserter.assertThat(
				() -> TreatmentEntity.getMinTreatmentPageForPatientName("%", Sort.ascending("id"), expected.total, 10),
				found -> {

					assertEquals(expected.total, found.total);
					assertNull(found.treatments);
				});

	}

	/**
	 * Should retrieve a min treatment.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldToMinTreatment(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.nextRandom(), retrieved -> {

			final var min = retrieved.toMinTreatment();
			assertNotNull(min);
			assertEquals(retrieved.id, min.id);
			assertEquals(retrieved.patient.toMinPatient(), min.patient);

		});
	}

	/**
	 * Should return an empty min treatment page when no pattern.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinTreatmentPageForPatientWhenBadIndex(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		asserter.assertThat(() -> TreatmentEntities.last(), last -> asserter.putData("PATIENT_ID", last.patient.id));
		asserter.assertThat(() -> {

			final var patientId = ((Number) asserter.getData("PATIENT_ID")).longValue();
			return TreatmentEntity.getMinTreatmentPageForPatient(patientId, Sort.descending("id"), -1, 10);

		}, found -> assertEquals(expected, found));

	}

	/**
	 * Should return an empty min treatment page if the index is too high.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinTreatmentPageForPatientWhenIndexIsTooHigh(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		asserter.assertThat(() -> TreatmentEntities.last(), last -> asserter.putData("PATIENT_ID", last.patient.id));
		asserter.assertThat(() -> {

			final var patientId = ((Number) asserter.getData("PATIENT_ID")).longValue();
			return TreatmentEntity.count("patient.id = ?1", patientId);

		}, total -> {

			asserter.putData("TOTAL", total);
			expected.total = Math.toIntExact(total);
		});

		asserter.assertThat(() -> {

			final var patientId = ((Number) asserter.getData("PATIENT_ID")).longValue();
			return TreatmentEntity.getMinTreatmentPageForPatient(patientId, Sort.descending("id"), expected.total, 10);

		}, found -> assertEquals(expected, found));

	}

	/**
	 * Should return an empty min treatment page because any treatment match the
	 * query.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinTreatmentPageForPatientWhenAnyMatch(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		asserter.assertThat(() -> TreatmentEntities.undefined(),
				undefined -> asserter.putData("PATIENT_ID", undefined));
		asserter.assertThat(() -> {

			final var patientId = ((Number) asserter.getData("PATIENT_ID")).longValue();
			return TreatmentEntity.getMinTreatmentPageForPatient(patientId, Sort.descending("id"), 0, 10);

		}, found -> assertEquals(expected, found));

	}

	/**
	 * Should get some treatments.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetMinTreatmentPageForPatientSome(TransactionalUniAsserter asserter) {

		final var expected = new MinTreatmentPage();
		final var offset = 3;
		final var limit = 7;
		final var sort = Sort.ascending("createdTime").and("id", Direction.Descending);
		asserter.assertThat(() -> PatientEntities.nextRandom(), patient -> asserter.putData("PATIENT", patient));
		asserter.execute(() -> {
			final var patient = (PatientEntity) asserter.getData("PATIENT");
			return TreatmentEntities.populateWith(patient, (offset + limit) * 2);
		});
		asserter.assertThat(() -> {

			final var patient = (PatientEntity) asserter.getData("PATIENT");
			final Uni<List<TreatmentEntity>> find = TreatmentEntity.find("patient.id = ?1", sort, patient.id).list();
			return find;

		}, all -> {

			expected.total = all.size();
			expected.treatments = new ArrayList<>();
			final var treatments = all.subList(offset, offset + limit);
			for (final var treatment : treatments) {

				final var found = treatment.toMinTreatment();
				expected.treatments.add(found);
			}

		});

		asserter.assertThat(() -> {

			final var patient = (PatientEntity) asserter.getData("PATIENT");
			return TreatmentEntity.getMinTreatmentPageForPatient(patient.id, sort, offset, limit);

		}, found -> assertEquals(expected, found));

	}

}
