/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.api.QueryParameters;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientStatusCriteriaEntity;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
	public Uni<Response> retrievePatient(
			@PathParam("id") @Parameter(in = ParameterIn.PATH, description = "The identifier of the patient to retrieve") long id) {

		return PatientEntity.retrievePatient(id).map(patient -> Response.ok(patient).build()).onFailure()
				.recoverWithItem(error -> {

					Log.errorv(error, "Cannot found a patient with the id {0}.", id);
					return Response.status(Status.NOT_FOUND)
							.entity("Not found a patient with the identifier %d".formatted(id)).build();

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
	public Uni<Response> deletePatient(
			@PathParam("id") @Parameter(in = ParameterIn.PATH, description = "The identifier of the patient to delete") long id) {

		return PatientEntity.delete(id).map(deleted -> Response.noContent().build()).onFailure()
				.recoverWithItem(error -> {

					Log.errorv(error, "Cannot delete the patient {0}.", id);
					return Response.status(Status.NOT_FOUND)
							.entity("Not found a patient with the identifier %d".formatted(id)).build();
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
	@APIResponse(responseCode = "201", description = "The created patient.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Patient.class)))
	public Uni<Response> createPatient(
			@RequestBody(description = "The patient to create", required = true, content = @Content(schema = @Schema(implementation = Patient.class))) @Valid final Patient model) {

		final Uni<PatientEntity> store = PatientStatusCriteriaEntity.retrieveOrPersist(model.status).chain(status -> {

			final var entity = new PatientEntity();
			entity.updateTime = TimeManager.now();
			entity.name = model.name;
			entity.status = status;
			return entity.persistAndFlush();

		});
		return store.map(stored -> {

			model.id = stored.id;
			model.updateTime = stored.updateTime;
			return Response.status(Status.CREATED).entity(model).build();

		}).onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot create a patient entity.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Cannot create a patient").build();

		});

	}

	/**
	 * Update a patient.
	 *
	 * @param id    identifier of the patient to update.
	 * @param model the data to the patient to update.
	 *
	 * @return the information of the updated patient.
	 */
	@PATCH
	@Path("/{id:\\d+}")
	@Operation(description = "Update a patient.")
	@APIResponse(responseCode = "200", description = "The updated patient.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Patient.class)))
	@APIResponse(responseCode = "400", description = "If it does not found a patient with the specified identifier")
	public Uni<Response> updatePatient(
			@PathParam("id") @Parameter(in = ParameterIn.PATH, description = "The identifier of the patient to update") long id,
			@RequestBody(description = "The patient to update", required = true, content = @Content(schema = @Schema(implementation = Patient.class))) @Valid final Patient model) {

		Uni<PatientStatusCriteriaEntity> definedStatus = null;
		if (model.status == null) {

			definedStatus = Uni.createFrom().nullItem();

		} else {

			definedStatus = PatientStatusCriteriaEntity.retrieveOrPersist(model.status);
		}
		return definedStatus.chain(status -> {

			final var entity = new PatientEntity();
			entity.id = id;
			entity.name = model.name;
			entity.status = status;
			return entity.update().chain(empty -> this.retrievePatient(id));

		}).onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot update the patient {0}.", id);
			return Response.status(Status.NOT_FOUND).entity("Not found a patient with the identifier %d".formatted(id))
					.build();

		});

	}

	/**
	 * Return the information of some patients.
	 *
	 * @param name   of the patients to return.
	 * @param offset the index of the first patient to retrieve.
	 * @param limit  the number maximum of patients to retrieve.
	 * @param order  the order to return the patients.
	 *
	 * @return the page with the patients.
	 */
	@GET
	@Operation(description = "Get a patient information.")
	@APIResponse(responseCode = "200", description = "The patients that satisfy the query.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MinPatientPage.class)))
	public Uni<Response> retrievePatientPage(
			@QueryParam("name") @DefaultValue("*") @Parameter(in = ParameterIn.QUERY, description = "The pattern to match  of the patient to retrieve") String name,
			@QueryParam("offset") @DefaultValue("0") @Parameter(in = ParameterIn.QUERY, description = "The index of the first patient to retrieve") @Min(0) int offset,
			@QueryParam("limit") @DefaultValue("10") @Parameter(in = ParameterIn.QUERY, description = "The number maximum of patients to retrieve") @Min(1) int limit,
			@QueryParam("order") @DefaultValue("") @Parameter(in = ParameterIn.QUERY, description = "The order to return the patients. You can define the fields name or id with the prefix + to ascending order and - to descending order.") String order) {

		final var pattern = QueryParameters.toPattern(name);
		final var sort = QueryParameters.toSort(order);
		return PatientEntity.getMinPatientPageFor(pattern, sort, offset, limit).map(page -> Response.ok(page).build());

	}

//	/**
//	 * Create a patient treatment.
//	 *
//	 * @param patientId identifier of the patient to update.
//	 * @param model     treatment for the patient.
//	 *
//	 * @return the information of the created patient treatment.
//	 */
//	@POST
//	@Path("/{patientId:\\d+}/treatments")
//	@Operation(description = "Create a patient treatment.")
//	@APIResponse(responseCode = "201", description = "The created patient treatment.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PatientTreatment.class)))
//	public Uni<Response> createPatientTreatment(
//			@PathParam("patientId") @Parameter(in = ParameterIn.PATH, description = "The identifier of the patient to add the treatment") long patientId,
//			@RequestBody(description = "The treatment to apply to the patient", required = true, content = @Content(schema = @Schema(implementation = PatientTreatment.class))) @Valid final PatientTreatment model) {
//
//		return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Not implemented yet").build();
//
////		final var entity = model.toPatientEntity();
////		entity.updateTime = TimeManager.now();
////		final Uni<PatientEntity> action = entity.persistAndFlush();
////		return action.map(stored -> {
////
////			model.id = stored.id;
////			model.updateTime = stored.updateTime;
////			return Response.status(Status.CREATED).entity(model).build();
////
////		}).onFailure().recoverWithItem(error -> {
////
////			Log.errorv(error, "Cannot create a patient entity.");
////			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Cannot create a patient").build();
////
////		});
//
//	}
//
//	/**
//	 * Get the treatments of a patient.
//	 *
//	 * @param patientId   identifier of the patient to get the treatment.
//	 * @param treatmentId identifier of the patient treatment to get.
//	 *
//	 * @return the information of the patient treatment.
//	 */
//	@GET
//	@Path("/{patientId:\\d+}/treatments")
//	@Operation(description = "Get a patient treatment.")
//	@APIResponse(responseCode = "200", description = "The patient treatment.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PatientTreatment.class)))
//	public Uni<Response> getPatientTreatment(
//			@PathParam("patientId") @Parameter(in = ParameterIn.PATH, description = "The identifier of the patient to get the treatment") long patientId,
//			@PathParam("treatmentId") @Parameter(in = ParameterIn.PATH, description = "The identifier of the patient treatment") long treatmentId) {
//
//		return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Not implemented yet").build();
//
////		final var entity = model.toPatientEntity();
////		entity.updateTime = TimeManager.now();
////		final Uni<PatientEntity> action = entity.persistAndFlush();
////		return action.map(stored -> {
////
////			model.id = stored.id;
////			model.updateTime = stored.updateTime;
////			return Response.status(Status.CREATED).entity(model).build();
////
////		}).onFailure().recoverWithItem(error -> {
////
////			Log.errorv(error, "Cannot create a patient entity.");
////			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Cannot create a patient").build();
////
////		});
//
//	}

}
