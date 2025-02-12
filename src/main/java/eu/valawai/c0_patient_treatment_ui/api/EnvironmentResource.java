/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

/**
 * Return the values that define the environment of the user interface.
 *
 * @author VALAWAI
 */
@Path("/env.js")
@Tag(name = "Extra", description = "The services to provide extra information")
public class EnvironmentResource {

	/**
	 * The URL of the application.
	 */
	@ConfigProperty(name = "c0.patient_treatment_ui.url", defaultValue = "http://localhost:8080")
	String componentUrl;

	/**
	 * Obtain the environment to use on the user interface.
	 *
	 * @return the environment file.
	 */
	@GET
	@Operation(description = "Provide information about the running API.")
	@APIResponse(responseCode = "200", content = @Content(mediaType = "text/javascript"))
	@Produces("text/javascript")
	public Uni<Response> getEnvironment() {

		final var env = new StringBuilder();
		env.append("(function(window) {\n");
		env.append("window[\"env\"] = window[\"env\"] || {};\n");
		env.append("window[\"env\"][\"componentUrl\"] = \"");
		env.append(this.componentUrl);
		env.append("\";\n");
		env.append("})(this);");
		return Uni.createFrom().item(Response.ok(env).build());
	}

}
