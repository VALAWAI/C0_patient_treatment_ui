/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import eu.valawai.c0_patient_treatment_ui.messages.mov.LogService;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntity;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

/**
 * The component that manage the treatment feedback.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class TreatmentFeedbackManager {

	/**
	 * The service to generate log messages.
	 */
	@Inject
	LogService log;

	/**
	 * The component to validate a {@code Payload}.
	 */
	@Inject
	Validator validator;

	/**
	 * Called when a new e-mail is sensed by the component.
	 *
	 * @param msg with the e-mail that has been sensed.
	 *
	 * @return the result if the message process.
	 */
	@Incoming("received_treatment_feedback")
	public CompletionStage<Void> receivedTreatmentFeedback(Message<JsonObject> msg) {

		return this.toValiedTreatmentFeedbackPayload(msg).chain(payload -> this.store(payload)).onFailure()
				.recoverWithItem(error -> error).subscribeAsCompletionStage().thenCompose(error -> {

					if (error != null) {

						this.log.errorWithPayload(msg.getPayload(),
								"Unexpected treatment feedback message, because {0}", error.getMessage());
						return msg.nack(error);

					} else {

						return msg.ack();
					}

				});

	}

	/**
	 * Obtain a valid treatment feedback payload from a message.
	 *
	 * @param msg top get the treatment feedback payload.
	 *
	 * @return the treatment feedback payload.
	 */
	protected Uni<TreatmentFeedbackPayload> toValiedTreatmentFeedbackPayload(Message<JsonObject> msg) {

		try {

			final var payload = msg.getPayload().mapTo(TreatmentFeedbackPayload.class);
			final var violations = this.validator.validate(payload);
			if (!violations.isEmpty()) {

				return Uni.createFrom().failure(new ConstraintViolationException(violations));

			} else {

				return Uni.createFrom().item(payload);
			}

		} catch (final Throwable error) {

			return Uni.createFrom().failure(error);
		}
	}

	/**
	 *
	 */
	@WithTransaction
	protected Uni<Throwable> store(TreatmentFeedbackPayload payload) {

		try {

			final var id = Long.parseLong(payload.treatment_id);
			final Uni<TreatmentEntity> find = TreatmentEntity.findById(id);
			return find.onFailure().recoverWithNull().chain(entity -> {

				if (entity == null) {

					return Uni.createFrom()
							.failure(new IllegalArgumentException("Not defined treatment with the id " + id));

				} else {

					return Uni.createFrom().nullItem();
				}

			});

		} catch (final Throwable error) {
			// bad identifier
			return Uni.createFrom().failure(
					new IllegalArgumentException("Not defined treatment with the id " + payload.treatment_id, error));
		}

	};

}
