/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntities;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientStatusCriteriaEntities;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientStatusCriteriaEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PostgreSQLTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Test the {@link PatientsResource}.
 *
 * @see PatientsResource
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
public class PatientsResourceTest {

	/**
	 * Test not retrieve an undefined patient.
	 */
	@Test
	public void shouldNotRetrieveUndefinedPatient() {

		given().pathParam("id", 0).when().get("/v1/patients/{id}").then().statusCode(Status.NOT_FOUND.getStatusCode());

	}

	/**
	 * Test retrieve a patient.
	 *
	 * @param asserter to use in the tests.
	 *
	 * @see PatientsResource#retrievePatient(long)
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrievePatient(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.nextRandom().map(entity -> {

			final var expected = new Patient();
			expected.id = entity.id;
			expected.name = entity.name;
			expected.updateTime = entity.updateTime;
			expected.status = entity.status.status;
			asserter.putData("STATUS_ID", entity.status.id);
			return expected;

		}), expected -> {

			final var retrieved = given().pathParam("id", expected.id).when().get("/v1/patients/{id}").then()
					.statusCode(Status.OK.getStatusCode()).extract().as(Patient.class);
			assertNotNull(retrieved);
			assertEquals(expected, retrieved);
			asserter.putData("RETRIEVED", retrieved);

		});

		asserter.assertThat(() -> {

			final long statusId = (Long) asserter.getData("STATUS_ID");
			return PatientStatusCriteriaEntity.retrieve(statusId).map(found -> found.status);

		}, found -> {

			final Patient retrieved = (Patient) asserter.getData("RETRIEVED");
			assertEquals(retrieved.status, found);

		});

	}

	/**
	 * Test not delete an undefined patient.
	 */
	@Test
	public void shouldNotDeleteUndefinedPatient() {

		given().pathParam("id", 0).when().delete("/v1/patients/{id}").then()
				.statusCode(Status.NOT_FOUND.getStatusCode());

	}

	/**
	 * Test delete a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldDeletePatient(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.last(), last -> {

			asserter.putData("LAST_ID", last.id);
			given().pathParam("id", last.id).when().delete("/v1/patients/{id}").then()
					.statusCode(Status.NO_CONTENT.getStatusCode());

		}).assertNull(() -> {

			final var lastId = (Long) asserter.getData("LAST_ID");
			return PatientEntity.findById(lastId);

		});

	}

	/**
	 * Test not create a patient with a large name.
	 */
	@Test
	public void shouldNotCreatePatientWithLargeName() {

		final var model = new PatientTest().nextModel();
		while (model.name.length() < 1024) {

			model.name += ValueGenerator.nextPattern(" and {0}");
		}

		given().contentType("application/json").body(model).when().post("/v1/patients").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode());

	}

	/**
	 * Test create a patient.
	 *
	 * @param asserter to use in the tests.
	 *
	 * @see PatientsResource#createPatient(Patient)
	 */
	@Test
	@RunOnVertxContext
	public void shouldCreatePatient(TransactionalUniAsserter asserter) {

		final var model = new PatientTest().nextModel();
		final var now = TimeManager.now();
		final var patient = given().contentType("application/json").body(model).when().post("/v1/patients").then()
				.statusCode(Status.CREATED.getStatusCode()).extract().as(Patient.class);
		assertNotEquals(model, patient);
		assertNotNull(patient.id);
		assertTrue(patient.updateTime >= now);
		model.id = patient.id;
		model.updateTime = patient.updateTime;
		assertEquals(model, patient);

		asserter.assertThat(() -> PatientEntity.retrieve(patient.id), found -> {

			assertNotNull(found, "Not found created entity.");
			assertEquals(patient.name, found.name);
			assertEquals(patient.updateTime, found.updateTime);
			assertNotNull(found.status);
			asserter.putData("STATUS_ID", found.status.id);

		});
		asserter.assertThat(() -> {

			final long statusId = (Long) asserter.getData("STATUS_ID");
			return PatientStatusCriteriaEntity.retrieve(statusId);

		}, found -> {

			assertEquals(model.status, found.status);

		});

	}

	/**
	 * Test not update an undefined patient.
	 *
	 * @see PatientsResource#updatePatient(long, Patient)
	 */
	@Test
	public void shouldNotUpdateUndefinedPatient() {

		final var newModel = new PatientTest().nextModel();
		newModel.id = null;
		newModel.updateTime = null;
		given().contentType("application/json").body(newModel).when().patch("/v1/patients/0").then()
				.statusCode(Status.NOT_FOUND.getStatusCode());

	}

	/**
	 * Test update a patient.
	 *
	 * @param asserter to use in the tests.
	 *
	 * @see PatientsResource#updatePatient(long, Patient)
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdatePatient(TransactionalUniAsserter asserter) {

		final var newModel = new PatientTest().nextModel();
		newModel.id = null;
		newModel.updateTime = null;
		asserter.assertThat(() -> PatientEntities.nextRandom(), patient -> {

			final var now = TimeManager.now();
			final var updated = given().contentType("application/json").body(newModel).pathParam("id", patient.id)
					.when().patch("/v1/patients/{id}").then().statusCode(Status.OK.getStatusCode()).extract()
					.as(Patient.class);
			newModel.id = patient.id;
			newModel.updateTime = updated.updateTime;
			assertEquals(newModel, updated);
			assertTrue(now <= updated.updateTime);

		});
		asserter.assertThat(() -> PatientEntity.retrieve(newModel.id), found -> {

			assertEquals(newModel.name, found.name);
			assertEquals(newModel.updateTime, found.updateTime);
			assertNotNull(found.status);
			asserter.putData("STATUS_ID", found.status.id);

		});
		asserter.assertThat(() -> {

			final long statusId = (Long) asserter.getData("STATUS_ID");
			return PatientStatusCriteriaEntity.retrieve(statusId);

		}, found -> {

			assertEquals(newModel.status, found.status);

		});
	}

	/**
	 * Test update only patient name.
	 *
	 * @param asserter to use in the tests.
	 *
	 * @see PatientsResource#updatePatient(long, Patient)
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateOnlyPatientName(TransactionalUniAsserter asserter) {

		final var newModel = new Patient();
		newModel.name = ValueGenerator.nextPattern("Patient name {0}");
		asserter.assertThat(() -> PatientEntities.nextRandom(), patient -> {

			final var now = TimeManager.now();
			final var updated = given().contentType("application/json").body(newModel).pathParam("id", patient.id)
					.when().patch("/v1/patients/{id}").then().statusCode(Status.OK.getStatusCode()).extract()
					.as(Patient.class);
			assertNotEquals(patient.name, updated.name);
			newModel.id = patient.id;
			newModel.updateTime = updated.updateTime;
			newModel.status = updated.status;
			assertEquals(newModel.name, updated.name);
			assertTrue(now <= updated.updateTime);
			asserter.putData("BEFORE", patient);

		});
		asserter.assertThat(() -> PatientEntity.retrieve(newModel.id), found -> {

			final PatientEntity before = (PatientEntity) asserter.getData("BEFORE");
			if (before.status != null) {

				assertNotNull(found.status);
				assertEquals(before.status.id, found.status.id);
			}
			assertEquals(newModel.name, found.name);
			assertEquals(newModel.updateTime, found.updateTime);

		});

	}

	/**
	 * Test update only patient status.
	 *
	 * @param asserter to use in the tests.
	 *
	 * @see PatientsResource#updatePatient(long, Patient)
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateOnlyPatientStatus(TransactionalUniAsserter asserter) {

		final var newModel = new Patient();
		asserter.assertThat(() -> PatientStatusCriteriaEntities.nextRandom(), entity -> {
			newModel.status = entity.status;
			asserter.putData("NEXT_STATUS", entity);
		});
		asserter.assertThat(() -> PatientEntities.nextRandom(), patient -> {

			final var now = TimeManager.now();
			final var updated = given().contentType("application/json").body(newModel).pathParam("id", patient.id)
					.when().patch("/v1/patients/{id}").then().statusCode(Status.OK.getStatusCode()).extract()
					.as(Patient.class);
			assertNotNull(updated.status);
			if (patient.status != null) {

				assertNotEquals(patient.status.status, updated.status);
			}
			assertEquals(newModel.status, updated.status);
			assertEquals(patient.name, updated.name);

			newModel.id = patient.id;
			newModel.updateTime = updated.updateTime;
			assertTrue(now <= updated.updateTime);
			asserter.putData("BEFORE", patient);

		});
		asserter.assertThat(() -> PatientEntity.retrieve(newModel.id), found -> {

			final PatientEntity before = (PatientEntity) asserter.getData("BEFORE");
			assertNotNull(found.status);
			final PatientStatusCriteriaEntity nextStatus = (PatientStatusCriteriaEntity) asserter
					.getData("NEXT_STATUS");
			assertEquals(nextStatus.id, found.status.id);
			assertEquals(before.name, found.name);
			assertEquals(newModel.updateTime, found.updateTime);

		});

	}

	/**
	 * Should get a patient page.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shoulRetrievePatientPage(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.populateWith(50).chain(ignored -> PatientEntity.count()), total -> {

			final var page = given().when().queryParam("limit", total).get("/v1/patients").then()
					.statusCode(Status.OK.getStatusCode()).extract().as(MinPatientPage.class);
			assertNotNull(page);
			assertEquals(total, page.total);
			assertNotNull(page.patients);
			assertEquals(total, page.patients.size());

			final var page2 = given().queryParam("offset", "3").when().get("/v1/patients").then()
					.statusCode(Status.OK.getStatusCode()).extract().as(MinPatientPage.class);
			final var expected = new MinPatientPage();
			expected.total = page.total;
			expected.patients = page.patients.subList(3, 13);
			assertEquals(expected, page2);

			final var page3 = given().queryParam("order", "-name").queryParam("name", "P*1*").queryParam("offset", "2")
					.queryParam("limit", "7").when().get("/v1/patients").then().statusCode(Status.OK.getStatusCode())
					.extract().as(MinPatientPage.class);

			expected.patients = page.patients.stream().filter(p -> p.name.matches("P.*1.*"))
					.collect(Collectors.toList());
			expected.total = expected.patients.size();
			Collections.sort(expected.patients, (p1, p2) -> p2.name.compareTo(p1.name));
			final var maxPattern = expected.patients.size();
			if (maxPattern < 3) {

				expected.patients = null;

			} else if (maxPattern < 9) {

				expected.patients = expected.patients.subList(2, maxPattern);

			} else {

				expected.patients = expected.patients.subList(2, 9);
			}
			assertEquals(expected, page3);

		});
	}

}
