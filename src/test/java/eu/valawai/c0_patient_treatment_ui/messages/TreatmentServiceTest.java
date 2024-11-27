/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.messages.mov.MOVTestResource;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * Test the {@link TreatmentService}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@WithTestResource(value = MOVTestResource.class)
public class TreatmentServiceTest {

	/**
	 * The queue to the treatments.
	 */
	@Inject
	protected TreatmentQueue queue;

	/**
	 * The service to publish treatments.
	 */
	@Inject
	protected TreatmentService service;

	/**
	 * Clear the previous treatments.
	 */
	@BeforeEach
	public void clear() {

		this.queue.clearTreatments();
	}

	/**
	 * Should sense some treatments.
	 */
	@Test
	public void shouldSenseTreatments() {

		final var treatment = new TreatmentPayloadTest().nextModel();
		this.service.send(treatment);
		final var received = this.queue.waitUntilNextTreatment(Duration.ofSeconds(30));
		assertEquals(treatment, received);

	}

}
