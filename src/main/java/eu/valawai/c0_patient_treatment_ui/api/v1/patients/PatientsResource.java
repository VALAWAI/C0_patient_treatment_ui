/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Action to do over a {@link Patient}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Path("/v1/patients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Patient", description = "The services to manage the patients")
public class PatientsResource {

	/**
	 * Return the information of a patient.
	 *
	 * @param id identifier of the patient to get.
	 *
	 * @return the information of a patient.
	 */
	@GET
	@Path("/{id:\\d+}")
	@Operation(description = "Get a patient information.")
	@APIResponse(responseCode = "200", description = "The patient associated to the identifier.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Patient.class)))
	@APIResponse(responseCode = "400", description = "If it does not found a patient with the specified identifier")
	public Uni<Response> retrievePatient(@PathParam("id") long id) {

		return PatientEntity.retrieve(id).map(entity -> {

			if (entity == null) {

				return Response.status(Status.NOT_FOUND)
						.entity("Not found a patient with the identifier %d".formatted(id)).build();
			} else {

				final var model = Patient.from(entity);
				return Response.ok(model).build();
			}
		});

	}

	/**
	 * Delete a patient.
	 *
	 * @param id identifier of the patient to delete.
	 *
	 * @return an empty content id the patient has been deleted.
	 */
	@DELETE
	@Path("/{id:\\d+}")
	@Operation(description = "Delete a patient information.")
	@APIResponse(responseCode = "204", description = "If the patient has been deleted.")
	@APIResponse(responseCode = "400", description = "If it does not found a patient with the specified identifier")
	public Uni<Response> deletePatient(@PathParam("id") long id) {

		return PatientEntity.delete(id).map(deleted -> {

			if (!deleted) {

				return Response.status(Status.NOT_FOUND)
						.entity("Not found a patient with the identifier %d".formatted(id)).build();
			} else {

				return Response.noContent().build();
			}
		});

	}

	/**
	 * Create a patient.
	 *
	 * @param model patient to create.
	 *
	 * @return the information of the created patient.
	 */
	@POST
	@Operation(description = "Create a patient.")
	@APIResponse(responseCode = "201", description = "The created participant.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Patient.class)))
	public Uni<Response> createPatient(
			@RequestBody(description = "The patient to create", required = true, content = @Content(schema = @Schema(implementation = Patient.class))) @Valid final Patient model) {

		final var entity = model.toPatientEntity();
		entity.updateTime = TimeManager.now();
		final Uni<PatientEntity> action = entity.persistAndFlush();
		return action.map(stored -> {

			model.id = stored.id;
			model.updateTime = stored.updateTime;
			return Response.status(Status.CREATED).entity(model).build();

		}).onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot create a patient entity.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Cannot create a patient").build();

		});

	}

}
