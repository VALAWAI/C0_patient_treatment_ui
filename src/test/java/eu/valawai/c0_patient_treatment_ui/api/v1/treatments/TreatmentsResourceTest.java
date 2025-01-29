/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatient;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentQueue;
import eu.valawai.c0_patient_treatment_ui.messages.mov.MOVTestResource;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntities;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientStatusCriteriaEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PostgreSQLTestResource;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntities;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntity;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

/**
 * Test the {@link TreatmentsResource}.
 *
 * @see TreatmentsResource
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@QuarkusTestResource(MOVTestResource.class)
@QuarkusTestResource(PostgreSQLTestResource.class)
public class TreatmentsResourceTest {

	/**
	 * The queue to listen for the payloads.
	 */
	@Inject
	TreatmentQueue queue;

	/**
	 * Test not retrieve an undefined treatment.
	 *
	 * @see TreatmentEntity#retrieve
	 */
	@Test
	public void shouldNotRetrieveUndefinedTreatment() {

		given().pathParam("id", 0).when().get("/v1/treatments/{id}").then()
				.statusCode(Status.NOT_FOUND.getStatusCode());

	}

	/**
	 * Test retrieve a treatment.
	 *
	 * @param asserter to use in the tests.
	 *
	 * @see TreatmentEntity#retrieve
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrieveTreatment(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.nextRandom(), entity -> {

			final var retrieved = given().pathParam("id", entity.id).when().get("/v1/treatments/{id}").then()
					.statusCode(Status.OK.getStatusCode()).extract().as(Treatment.class);
			assertNotNull(retrieved);
			assertNotNull(retrieved.patient);
			assertEquals(entity.id, retrieved.id);
			asserter.putData("BEFORE_STATUS_ID", entity.beforeStatus.id);
			asserter.putData("EXPECTED_STATUS_ID", entity.expectedStatus.id);
			asserter.putData("RETRIEVED", retrieved);

			final var copy = new ArrayList<>(entity.treatmentActions);
			for (final var action : retrieved.actions) {

				assertTrue("Undefined action", copy.remove(action.action));

			}
			assertTrue("Not obtained all the action", copy.isEmpty());

		});

		asserter.assertThat(() -> {

			final Treatment retrieved = (Treatment) asserter.getData("RETRIEVED");
			return PatientEntity.retrieve(retrieved.patient.id);

		}, found -> {

			final Treatment retrieved = (Treatment) asserter.getData("RETRIEVED");
			assertEquals(retrieved.patient.name, found.name);

		});

		asserter.assertThat(() -> {

			final long statusId = (Long) asserter.getData("BEFORE_STATUS_ID");
			return PatientStatusCriteriaEntity.retrieve(statusId).map(found -> found.status);

		}, found -> {

			final Treatment retrieved = (Treatment) asserter.getData("RETRIEVED");
			assertEquals(retrieved.beforeStatus, found);

		});

		asserter.assertThat(() -> {

			final long statusId = (Long) asserter.getData("EXPECTED_STATUS_ID");
			return PatientStatusCriteriaEntity.retrieve(statusId).map(found -> found.status);

		}, found -> {

			final Treatment retrieved = (Treatment) asserter.getData("RETRIEVED");
			assertEquals(retrieved.expectedStatus, found);

		});

	}

	/**
	 * Test not delete an undefined treatment.
	 */
	@Test
	public void shouldNotDeleteUndefinedTreatment() {

		given().pathParam("id", 0).when().delete("/v1/treatments/{id}").then()
				.statusCode(Status.NOT_FOUND.getStatusCode());

	}

	/**
	 * Test delete a treatment.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldDeleteTreatment(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.last(), last -> {

			asserter.putData("LAST_ID", last.id);
			given().pathParam("id", last.id).when().delete("/v1/treatments/{id}").then()
					.statusCode(Status.NO_CONTENT.getStatusCode());

		}).assertNull(() -> {

			final var lastId = (Long) asserter.getData("LAST_ID");
			return TreatmentEntity.findById(lastId);

		});

	}

	/**
	 * Test not create a treatment without a patient.
	 */
	@Test
	public void shouldNotCreateTreatmentWithoutPatient() {

		final var model = new TreatmentToAddTest().nextModel();
		model.patientId = null;
		given().contentType("application/json").body(model).when().post("/v1/treatments").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode());

	}

	/**
	 * Test not create a treatment with an undefined name.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotCreateTreatmentWithoutUndefinedPatient(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.undefined(), undefined -> {

			final var model = new TreatmentToAddTest().nextModel();
			model.patientId = undefined;
			asserter.putData("MODEL", model);
		});
		asserter.assertThat(() -> Uni.createFrom().nullItem(), any -> {

			final var model = (TreatmentToAdd) asserter.getData("MODEL");
			given().contentType("application/json").body(model).when().post("/v1/treatments").then()
					.statusCode(Status.BAD_REQUEST.getStatusCode());
		});

	}

	/**
	 * Test not create a treatment without before status.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotCreateTreatmentWithoutBeforeStatus(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), last -> {

			final var model = new TreatmentToAddTest().nextModel();
			model.patientId = last.id;
			model.beforeStatus = null;
			asserter.putData("MODEL", model);
		});
		asserter.assertThat(() -> Uni.createFrom().nullItem(), any -> {

			final var model = (TreatmentToAdd) asserter.getData("MODEL");
			given().contentType("application/json").body(model).when().post("/v1/treatments").then()
					.statusCode(Status.BAD_REQUEST.getStatusCode());
		});
	}

	/**
	 * Test not create a treatment without actions.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotCreateTreatmentWithoutActions(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), last -> {

			final var model = new TreatmentToAddTest().nextModel();
			model.patientId = last.id;
			model.actions = null;
			asserter.putData("MODEL", model);
		});
		asserter.assertThat(() -> Uni.createFrom().nullItem(), any -> {

			final var model = (TreatmentToAdd) asserter.getData("MODEL");
			given().contentType("application/json").body(model).when().post("/v1/treatments").then()
					.statusCode(Status.BAD_REQUEST.getStatusCode());
		});
	}

	/**
	 * Test not create a treatment with empty actions.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotCreateTreatmentWithoutEmptyActions(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), last -> {

			final var model = new TreatmentToAddTest().nextModel();
			model.patientId = last.id;
			model.actions.clear();
			asserter.putData("MODEL", model);
		});
		asserter.assertThat(() -> Uni.createFrom().nullItem(), any -> {

			final var model = (TreatmentToAdd) asserter.getData("MODEL");
			given().contentType("application/json").body(model).when().post("/v1/treatments").then()
					.statusCode(Status.BAD_REQUEST.getStatusCode());
		});
	}

	/**
	 * Test not create a treatment without expected status.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotCreateTreatmentWithoutExpectedStatus(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), last -> {

			final var model = new TreatmentToAddTest().nextModel();
			model.patientId = last.id;
			model.expectedStatus = null;
			asserter.putData("MODEL", model);
		});
		asserter.assertThat(() -> Uni.createFrom().nullItem(), any -> {

			final var model = (TreatmentToAdd) asserter.getData("MODEL");
			given().contentType("application/json").body(model).when().post("/v1/treatments").then()
					.statusCode(Status.BAD_REQUEST.getStatusCode());
		});
	}

	/**
	 * Test not create a treatment.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldCreateTreatment(TransactionalUniAsserter asserter) {

		this.queue.clearTreatments();
		final var model = new TreatmentToAddTest().nextModel();
		asserter.assertThat(() -> PatientEntities.last(), last -> {

			model.patientId = last.id;
			final var now = TimeManager.now();
			final var created = given().contentType("application/json").body(model).when().post("/v1/treatments").then()
					.statusCode(Status.CREATED.getStatusCode()).extract().as(Treatment.class);
			assertNotNull(created.id);
			final var patient = last.toMinPatient();
			assertEquals(patient, created.patient);
			assertEquals(model.beforeStatus, created.beforeStatus);

			assertEquals(model.expectedStatus, created.expectedStatus);
			assertTrue(now <= created.createdTime);
			final var copy = new ArrayList<>(model.actions);
			for (final var action : created.actions) {

				assertTrue("Undefined action", copy.remove(action.action));

			}
			assertTrue("Not obtained all the action", copy.isEmpty());
			asserter.putData("TREATMENT_ID", created.id);
			asserter.putData("PATIENT", patient);
			asserter.putData("CREATED_TIME", created.createdTime);

		});

		asserter.assertThat(() -> {

			final var id = (Long) asserter.getData("TREATMENT_ID");
			return TreatmentEntity.retrieve(id);

		}, found -> {

			final var id = (Long) asserter.getData("TREATMENT_ID");
			assertEquals(id, found.id);
			final var patient = (MinPatient) asserter.getData("PATIENT");
			assertEquals(patient, found.patient.toMinPatient());
			final var createdTime = (Long) asserter.getData("CREATED_TIME");
			assertEquals(createdTime, found.createdTime);
			assertEquals(model.beforeStatus, found.beforeStatus.status);
			assertEquals(model.actions, found.treatmentActions);
			assertEquals(model.expectedStatus, found.expectedStatus.status);

			asserter.putData("BEFORE_STATUS_ID", found.beforeStatus.id);
			asserter.putData("EXPECTED_STATUS_ID", found.expectedStatus.id);
			final var expectedPayload = found.toTreatmentPayload();
			final var received = this.queue.waitUntilNextTreatment(Duration.ofSeconds(30));
			assertEquals(expectedPayload, received);
		});

		asserter.assertThat(() -> {

			final long statusId = (Long) asserter.getData("BEFORE_STATUS_ID");
			return PatientStatusCriteriaEntity.retrieve(statusId).map(found -> found.status);

		}, found -> {

			assertEquals(model.beforeStatus, found);

		});

		asserter.assertThat(() -> {

			final long statusId = (Long) asserter.getData("EXPECTED_STATUS_ID");
			return PatientStatusCriteriaEntity.retrieve(statusId).map(found -> found.status);

		}, found -> {

			assertEquals(model.expectedStatus, found);

		});

	}

	/**
	 * Should get a treatment page.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shoulRetrieveTreatmentPage(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.populateWith(50).chain(ignored -> TreatmentEntity.count()),
				total -> {

					final var page = given().when().queryParam("limit", total).get("/v1/treatments").then()
							.statusCode(Status.OK.getStatusCode()).extract().as(MinTreatmentPage.class);
					assertNotNull(page);
					assertEquals(total, page.total);
					assertNotNull(page.treatments);
					assertEquals(total, page.treatments.size());
					final var treatments = page.treatments;
					asserter.putData("TREATMENTS", treatments);
					asserter.putData("TOTAL", total);

				});

		asserter.assertThat(() -> Uni.createFrom().nullItem(), any -> {

			@SuppressWarnings("unchecked")
			final List<MinTreatment> treatments = (List<MinTreatment>) asserter.getData("TREATMENTS");
			final long total = ((Number) asserter.getData("TOTAL")).longValue();
			final var page = given().queryParam("offset", "3").when().get("/v1/treatments").then()
					.statusCode(Status.OK.getStatusCode()).extract().as(MinTreatmentPage.class);
			final var expected = new MinTreatmentPage();
			expected.total = Math.toIntExact(total);
			expected.treatments = treatments.subList(3, 13);
			assertEquals(expected, page);

		});

		asserter.assertThat(() -> Uni.createFrom().nullItem(), any -> {

			final var page = given().queryParam("order", "-patient.name").queryParam("patientName", "P*1*")
					.queryParam("offset", "2").queryParam("limit", "7").when().get("/v1/treatments").then()
					.statusCode(Status.OK.getStatusCode()).extract().as(MinTreatmentPage.class);

			final var expected = new MinTreatmentPage();
			@SuppressWarnings("unchecked")
			final List<MinTreatment> treatments = (List<MinTreatment>) asserter.getData("TREATMENTS");
			expected.treatments = treatments.stream().sorted((t1, t2) -> t2.patient.name.compareTo(t1.patient.name))
					.filter(t -> t.patient.name.matches("P.*1.*")).collect(Collectors.toList());
			expected.total = expected.treatments.size();
			final var maxPattern = expected.treatments.size();
			if (maxPattern < 3) {

				expected.treatments = null;

			} else if (maxPattern < 9) {

				expected.treatments = expected.treatments.subList(2, maxPattern);

			} else {

				expected.treatments = expected.treatments.subList(2, 9);
			}
			assertEquals(expected, page);

		});

	}

	/**
	 * Should get an empty treatment page for an undefined patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shoulRetrieveEmptyTreatmentPageForUndefinedPatient(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.undefined(), undefined -> {

			final var page = given().when().queryParam("patientId", String.valueOf(undefined)).get("/v1/treatments")
					.then().statusCode(Status.OK.getStatusCode()).extract().as(MinTreatmentPage.class);
			assertNotNull(page);
			assertEquals(new MinTreatmentPage(), page);
		});

	}

	/**
	 * Should get a treatment page for a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shoulRetrieveTreatmentPageForAPatient(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.nextRandom(), patient -> asserter.putData("PATIENT", patient));
		asserter.execute(() -> TreatmentEntities.populateWith((PatientEntity) asserter.getData("PATIENT"), 20));
		asserter.assertThat(() -> {

			final var patient = (PatientEntity) asserter.getData("PATIENT");
			final Uni<List<TreatmentEntity>> action = TreatmentEntity.find("patient.id = ?1", patient.id).list();
			return action;

		}, treatments -> {

			final var patient = (PatientEntity) asserter.getData("PATIENT");
			final var page = given().when().queryParam("patientId", String.valueOf(patient.id)).get("/v1/treatments")
					.then().statusCode(Status.OK.getStatusCode()).extract().as(MinTreatmentPage.class);
			assertNotNull(page);
			final var expected = new MinTreatmentPage();
			expected.total = treatments.size();
			expected.treatments = treatments.stream().sorted((t1, t2) -> {

				var cmp = t1.patient.name.compareTo(t2.patient.name);
				if (cmp == 0) {

					cmp = t1.id.compareTo(t2.id);
				}
				return cmp;

			}).limit(10).map(t -> t.toMinTreatment()).collect(Collectors.toList());
			assertEquals(expected, page);
			asserter.putData("TREATMENTS", treatments);

		});

		asserter.assertThat(() -> Uni.createFrom().nullItem(), any -> {

			final var patient = (PatientEntity) asserter.getData("PATIENT");
			@SuppressWarnings("unchecked")
			final var treatments = (List<TreatmentEntity>) asserter.getData("TREATMENTS");
			final var offset = 3;
			final var limit = 7;
			final var page = given().when().queryParam("patientId", String.valueOf(patient.id))
					.queryParam("offset", String.valueOf(offset)).queryParam("limit", String.valueOf(limit))
					.queryParam("order", "-createdTime").get("/v1/treatments").then()
					.statusCode(Status.OK.getStatusCode()).extract().as(MinTreatmentPage.class);
			assertNotNull(page);
			assertNotNull(page);
			final var expected = new MinTreatmentPage();
			expected.total = treatments.size();
			expected.treatments = treatments.stream().sorted((t1, t2) -> {

				var cmp = Long.compare(t2.createdTime, t1.createdTime);
				if (cmp == 0) {

					cmp = t1.id.compareTo(t2.id);
				}
				return cmp;

			}).skip(offset).limit(limit).map(t -> t.toMinTreatment()).collect(Collectors.toList());
			assertEquals(expected, page);

		});

	}
}
