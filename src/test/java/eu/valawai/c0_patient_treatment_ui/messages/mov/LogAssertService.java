/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages.mov;

import static io.restassured.RestAssured.given;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service to check if has generated some logs.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class LogAssertService implements LogAsserts {

	/**
	 * The port where the Master Of VALAWAI is listening.
	 */
	@ConfigProperty(name = MOVTestResource.MOV_URL_CONFIG_PROPERTY_NAME, defaultValue = "http://host.docker.internal:8084")
	String movUrl;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JsonArray findLastLogs() {

		final var content = given().queryParam("order", "-timestamp").queryParam("limit", "50").when()
				.get(this.movUrl + "/v1/logs").then().statusCode(200).extract().asString();
		final var page = Json.decodeValue(content, JsonObject.class);
		return page.getJsonArray("logs");
	}

}
