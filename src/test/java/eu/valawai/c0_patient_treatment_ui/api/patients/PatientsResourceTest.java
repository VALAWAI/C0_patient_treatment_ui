/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.patients;

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
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatientPage;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.Patient;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.PatientsResource;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntities;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
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
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrievePatient(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> PatientEntities.nextAndPersist(), entity -> {

			final var expected = Patient.from(entity);
			final var retrieved = given().pathParam("id", entity.id).when().get("/v1/patients/{id}").then()
					.statusCode(Status.OK.getStatusCode()).extract().as(Patient.class);
			assertNotNull(retrieved);
			assertEquals(expected, retrieved);

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

		final var entity = PatientEntities.nextRandom();
		asserter.assertThat(() -> entity.persist(), ignored -> {

			given().pathParam("id", entity.id).when().delete("/v1/patients/{id}").then()
					.statusCode(Status.NO_CONTENT.getStatusCode());

		}).assertNull(() -> PatientEntity.findById(entity.id));

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

		asserter.assertThat(() -> PatientEntities.byId(patient.id), entity -> {

			assertNotNull(entity, "Not found created entity.");
			assertEquals(patient, Patient.from(entity));

		});

	}

	/**
	 * Test not update an undefined patient.
	 */
	@Test
	public void shouldNotUpdateUndefinedPatient() {

		final var model = new PatientTest().nextModel();
		given().contentType("application/json").body(model).pathParam("id", 0).when().patch("/v1/patients/{id}").then()
				.statusCode(Status.NOT_FOUND.getStatusCode());

	}

	/**
	 * Test update a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdatePatient(TransactionalUniAsserter asserter) {

		final var newModel = new PatientTest().nextModel();
		asserter.assertThat(() -> PatientEntities.nextAndPersist(), patient -> {

			final var before = Patient.from(patient);
			final var now = TimeManager.now();
			final var updated = given().contentType("application/json").body(newModel).pathParam("id", patient.id)
					.when().patch("/v1/patients/{id}").then().statusCode(Status.OK.getStatusCode()).extract()
					.as(Patient.class);
			assertNotEquals(before, updated);
			assertNotEquals(newModel, updated);
			newModel.id = patient.id;
			newModel.updateTime = updated.updateTime;
			assertEquals(newModel, updated);
			assertTrue(now <= updated.updateTime);

		}).assertThat(() -> PatientEntities.byId(newModel.id), found -> {

			assertNotNull(found);
			final var after = Patient.from(found);
			assertEquals(newModel, after);

		});

	}

	/**
	 * Test update only patient name.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateOnlyPatientName(TransactionalUniAsserter asserter) {

		final var newModel = new Patient();
		newModel.name = ValueGenerator.nextPattern("Patient name {0}");
		asserter.assertThat(() -> PatientEntities.nextAndPersist(), patient -> {

			final var before = Patient.from(patient);
			final var now = TimeManager.now();
			final var updated = given().contentType("application/json").body(newModel).pathParam("id", patient.id)
					.when().patch("/v1/patients/{id}").then().statusCode(Status.OK.getStatusCode()).extract()
					.as(Patient.class);
			assertNotEquals(patient.name, updated.name);
			newModel.id = patient.id;
			newModel.updateTime = updated.updateTime;
			assertEquals(newModel.name, updated.name);
			assertTrue(now <= updated.updateTime);
			before.name = newModel.name;
			before.updateTime = updated.updateTime;
			assertEquals(before, updated);

		}).assertThat(() -> PatientEntities.byId(newModel.id), found -> {

			assertNotNull(found);
			assertEquals(newModel.name, found.name);
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
