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
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentActionFeedbackEntity;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

/**
 * The component that manage the feedback for a treatment action.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class TreatmentActionFeedbackManager {

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
	 * Called when a new feedback for a treatment action has been received.
	 *
	 * @param msg with the feedback for a treatment action.
	 *
	 * @return the result if the message process.
	 */
	@Incoming("received_treatment_action_feedback")
	public CompletionStage<Void> receivedTreatmentActionFeedback(Message<JsonObject> msg) {

		return this.toValiedTreatmentActionFeedbackPayload(msg).chain(payload -> this.store(payload)).onFailure()
				.recoverWithItem(error -> error).subscribeAsCompletionStage().thenCompose(error -> {

					if (error != null) {

						this.log.errorWithPayload(msg.getPayload(),
								"Unexpected treatment action feedback message, because {0}", error.getMessage());
						return msg.nack(error);

					} else {

						this.log.debugWithPayload(msg.getPayload(), "Treatment action feedback received");
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
	protected Uni<TreatmentActionFeedbackPayload> toValiedTreatmentActionFeedbackPayload(Message<JsonObject> msg) {

		try {

			final var payload = msg.getPayload().mapTo(TreatmentActionFeedbackPayload.class);
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
	 * Store the feedback.
	 *
	 * @param payload with the feedback to store.
	 */
	@WithTransaction
	protected Uni<Throwable> store(TreatmentActionFeedbackPayload payload) {

		return TreatmentActionFeedbackEntity.store(payload).map(any -> (Throwable) null).onFailure()
				.recoverWithItem(error -> error);

	};

}
