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

		this.service.send(treatment).handle((success, error) -> {

			if (error == null) {

				this.log.debugWithPayload(treatment, "Publish treatment.");

			} else {

				this.log.errorWithPayload(treatment, "Cannot publish treatment.");
			}
			return null;
		});
	}

}
