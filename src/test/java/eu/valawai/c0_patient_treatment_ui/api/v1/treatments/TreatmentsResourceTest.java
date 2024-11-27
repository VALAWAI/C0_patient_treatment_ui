/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentQueue;
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
			assertEquals(retrieved.actions, entity.treatmentActions);
			asserter.putData("EXPECTED_STATUS_ID", entity.expectedStatus.id);
			asserter.putData("RETRIEVED", retrieved);

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

		final var model = new TreatmentTest().nextModel();
		model.patient = null;
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

			final var model = new TreatmentTest().nextModel();
			model.patient.id = undefined;
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

			final var model = new TreatmentTest().nextModel();
			model.patient.id = last.id;
			model.beforeStatus = null;
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

			final var model = new TreatmentTest().nextModel();
			model.patient.id = last.id;
			model.actions = null;
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

			final var model = new TreatmentTest().nextModel();
			model.patient.id = last.id;
			model.actions.clear();
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

			final var model = new TreatmentTest().nextModel();
			model.patient.id = last.id;
			model.expectedStatus = null;
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
		final var model = new TreatmentTest().nextModel();
		asserter.assertThat(() -> PatientEntities.last(), last -> {

			model.patient.id = last.id;
			final var now = TimeManager.now();
			final var created = given().contentType("application/json").body(model).when().post("/v1/treatments").then()
					.statusCode(Status.CREATED.getStatusCode()).extract().as(Treatment.class);
			model.id = created.id;
			model.patient = last.toMinPatient();
			assertEquals(model.patient, created.patient);
			assertEquals(model.beforeStatus, created.beforeStatus);
			assertEquals(model.actions, created.actions);
			assertEquals(model.expectedStatus, created.expectedStatus);
			assertTrue(now <= created.createdTime);
			model.createdTime = created.createdTime;
		});

		asserter.assertThat(() -> TreatmentEntity.retrieve(model.id), found -> {

			assertEquals(model, found.toTreatment());
			asserter.putData("BEFORE_STATUS_ID", found.beforeStatus.id);
			asserter.putData("EXPECTED_STATUS_ID", found.expectedStatus.id);
			final var payload = this.queue.waitUntilNextTreatment(Duration.ofSeconds(30));
			assertEquals(found.toTreatmentPayload(), payload);

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

}
