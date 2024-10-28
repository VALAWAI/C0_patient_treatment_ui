/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.api.patients;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.Patient;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.PatientsResource;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntityTest;
import eu.valawai.c0_patient_treatment_ui.persistence.PostgreSQLTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.smallrye.mutiny.Uni;
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

		final var entity = PatientEntityTest.nextRandom();
		asserter.assertThat(() -> entity.persist(), patient -> {

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

		final var entity = PatientEntityTest.nextRandom();
		asserter.assertThat(() -> entity.persist(), patient -> {

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

		asserter.assertThat(() -> {

			final Uni<PatientEntity> action = PatientEntity.findById(model.id);
			return action;

		}, entity -> {

			assertNotNull(entity, "Not found created entity.");
			assertEquals(patient, Patient.from(entity));

		});

	}

}
