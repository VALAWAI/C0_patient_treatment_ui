/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;

/**
 * Test the {@link EnvironmentResource}.
 *
 * @see EnvironmentResource
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
public class EnvironmentResourceTest {

	/**
	 * The URL of the application.
	 */
	@ConfigProperty(name = "c0.patient_treatment_ui.url", defaultValue = "http://localhost:8080")
	String componentUrl;

	/**
	 * Check that get the environment script.
	 */
	@Test
	public void shouldGetEnvironmentScript() {

		final var script = given().get("/env.js").then().statusCode(Status.OK.getStatusCode()).extract().asString();
		assertTrue(script.contains(this.componentUrl));

	}

}
