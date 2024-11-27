/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import eu.valawai.c0_patient_treatment_ui.messages.mov.LogService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * The service to publish a treatment to apply over a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class TreatmentService {

	/**
	 * The component to send the treatment messages.
	 */
	@Channel("publish_treatment")
	@Inject
	Emitter<TreatmentPayload> service;

	/**
	 * The component to send log messages.
	 */
	@Inject
	LogService log;

	/**
	 * Send a treatment to apply to a patient.
	 *
	 * @param treatment to apply.
	 */
	public void send(TreatmentPayload treatment) {

		this.log.debugWithPayload(treatment, "Publish treatment.");
		this.service.send(treatment).handle((success, error) -> {

			if (error == null) {

				Log.debugv("Sent {0}.", treatment);

			} else {

				Log.errorv(error, "Cannot send {0}.", treatment);
			}
			return null;
		});
	}

}
