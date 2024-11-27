/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentService;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientStatusCriteriaEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntity;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
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
 * Action to do over a {@link Treatment}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Path("/v1/treatments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Treatment", description = "The services to manage the treatments")
public class TreatmentsResource {

	/**
	 * The service to publish treatments.
	 */
	@Inject
	protected TreatmentService service;

	/**
	 * Return the information of a treatment.
	 *
	 * @param id identifier of the treatment to get.
	 *
	 * @return the information of a treatment.
	 */
	@GET
	@Path("/{id:\\d+}")
	@Operation(description = "Get a treatment information.")
	@APIResponse(responseCode = "200", description = "The treatment associated to the identifier.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Treatment.class)))
	@APIResponse(responseCode = "400", description = "If it does not found a treatment with the specified identifier")
	public Uni<Response> retrieveTreatment(
			@PathParam("id") @Parameter(in = ParameterIn.PATH, description = "The identifier of the treatment to retrieve") long id) {

		return TreatmentEntity.retrieveTreatment(id).map(treatment -> Response.ok(treatment).build()).onFailure()
				.recoverWithItem(error -> {

					Log.errorv(error, "Cannot found a treatment with the id {0}.", id);
					return Response.status(Status.NOT_FOUND)
							.entity("Not found a treatment with the identifier %d".formatted(id)).build();

				});
	}

	/**
	 * Delete a treatment.
	 *
	 * @param id identifier of the treatment to delete.
	 *
	 * @return an empty content id the treatment has been deleted.
	 */
	@DELETE
	@Path("/{id:\\d+}")
	@Operation(description = "Delete a treatment information.")
	@APIResponse(responseCode = "204", description = "If the treatment has been deleted.")
	@APIResponse(responseCode = "400", description = "If it does not found a treatment with the specified identifier")
	public Uni<Response> deleteTreatment(
			@PathParam("id") @Parameter(in = ParameterIn.PATH, description = "The identifier of the treatment to delete") long id) {

		return TreatmentEntity.delete(id).map(deleted -> Response.noContent().build()).onFailure()
				.recoverWithItem(error -> {

					Log.errorv(error, "Cannot delete the treatment {0}.", id);
					return Response.status(Status.NOT_FOUND)
							.entity("Not found a treatment with the identifier %d".formatted(id)).build();
				});

	}

	/**
	 * Create a treatment.
	 *
	 * @param model treatment to create.
	 *
	 * @return the information of the created treatment.
	 */
	@POST
	@Operation(description = "Create a treatment.")
	@APIResponse(responseCode = "201", description = "The created treatment.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Treatment.class)))
	public Uni<Response> createTreatment(
			@RequestBody(description = "The treatment to create", required = true, content = @Content(schema = @Schema(implementation = Treatment.class))) @Valid final Treatment model) {

		return PatientEntity.retrieve(model.patient.id).chain(patient -> {

			return PatientStatusCriteriaEntity.retrieveOrPersist(model.beforeStatus).chain(before -> {

				return PatientStatusCriteriaEntity.retrieveOrPersist(model.expectedStatus).chain(expected -> {

					final var entity = new TreatmentEntity();
					entity.createdTime = TimeManager.now();
					entity.beforeStatus = before;
					entity.treatmentActions = model.actions;
					entity.expectedStatus = expected;
					entity.patient = patient;
					final Uni<TreatmentEntity> persist = entity.persistAndFlush();
					return persist.map(stored -> {

						final var treatment = stored.toTreatment();
						final var payload = stored.toTreatmentPayload();
						this.service.send(payload);
						return Response.status(Status.CREATED).entity(treatment).build();
					});

				});
			});

		}).onFailure().recoverWithItem(error -> {

			Log.errorv(error, "Cannot create a treatment.");
			return Response.status(Status.BAD_REQUEST).entity("Bad treatment parameters.").build();

		});

	}

//	/**
//	 * Return the information of some treatments.
//	 *
//	 * @param name   of the treatments to return.
//	 * @param offset the index of the first treatment to retrieve.
//	 * @param limit  the number maximum of treatments to retrieve.
//	 * @param order  the order to return the treatments.
//	 *
//	 * @return the page with the treatments.
//	 */
//	@GET
//	@Operation(description = "Get a treatment information.")
//	@APIResponse(responseCode = "200", description = "The treatments that satisfy the query.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MinTreatmentPage.class)))
//	public Uni<Response> retrieveTreatmentPage(
//			@QueryParam("name") @DefaultValue("*") @Parameter(in = ParameterIn.QUERY, description = "The pattern to match  of the treatment to retrieve") String name,
//			@QueryParam("offset") @DefaultValue("0") @Parameter(in = ParameterIn.QUERY, description = "The index of the first treatment to retrieve") @Min(0) int offset,
//			@QueryParam("limit") @DefaultValue("10") @Parameter(in = ParameterIn.QUERY, description = "The number maximum of treatments to retrieve") @Min(1) int limit,
//			@QueryParam("order") @DefaultValue("") @Parameter(in = ParameterIn.QUERY, description = "The order to return the treatments. You can define the fields name or id with the prefix + to ascending order and - to descending order.") String order) {
//
//		final var pattern = QueryParameters.toPattern(name);
//		final var sort = QueryParameters.toSort(order);
//		return TreatmentEntity.getMinTreatmentPageFor(pattern, sort, offset, limit)
//				.map(page -> Response.ok(page).build());
//
//	}
//
}
