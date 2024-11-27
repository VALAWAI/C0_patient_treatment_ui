/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.quarkus.logging.Log;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * The service to publish a {@link TreatmentFeedbackPayload}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class TreatmentFeedbackService {

	/**
	 * The component to send the treatment messages.
	 */
	@Channel("publish_treatment_feedback")
	@Inject
	Emitter<JsonObject> service;

	/**
	 * Send a feedback for a treatment.
	 *
	 * @param feedback to apply.
	 */
	public void send(TreatmentFeedbackPayload feedback) {

		final var encode = Json.encode(feedback);
		final var json = Json.decodeValue(encode, JsonObject.class);
		this.send(json);
	}

	/**
	 * Send a feedback for a treatment.
	 *
	 * @param feedback to apply.
	 */
	public void send(JsonObject feedback) {

		this.service.send(feedback).handle((success, error) -> {

			if (error == null) {

				Log.debugv("Sent {0}.", feedback);

			} else {

				Log.errorv(error, "Cannot send {0}.", feedback);
			}
			return null;
		});
	}

}
