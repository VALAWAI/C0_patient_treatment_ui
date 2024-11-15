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

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientStatusCriteriaEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PostgreSQLTestResource;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntities;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntity;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
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
			assertEquals(retrieved.treatmentActions, entity.treatmentActions);
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

}
