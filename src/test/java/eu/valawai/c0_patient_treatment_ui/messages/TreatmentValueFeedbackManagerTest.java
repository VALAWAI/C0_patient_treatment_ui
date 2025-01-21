/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.messages.mov.LogAsserts;
import eu.valawai.c0_patient_treatment_ui.messages.mov.LogLevel;
import eu.valawai.c0_patient_treatment_ui.messages.mov.MOVTestResource;
import eu.valawai.c0_patient_treatment_ui.persistence.PostgreSQLTestResource;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntities;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;

/**
 * Test the {@link TreatmentValueFeedbackManager}.
 *
 * @see TreatmentValueFeedbackManager
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@WithTestResource(value = MOVTestResource.class)
@QuarkusTestResource(PostgreSQLTestResource.class)
public class TreatmentValueFeedbackManagerTest {

	/**
	 * The service to publish treatment feedbacks.
	 */
	@Inject
	TreatmentValueFeedbackService service;

	/**
	 * The service to assert that the log.
	 */
	@Inject
	LogAsserts logAsserts;

	/**
	 * Should not add a bad feedback message.
	 */
	@Test
	public void shouldNotAddFeedbackForBadMessage() {

		final var emptyFeedback = new JsonObject();
		this.service.send(emptyFeedback);
		this.logAsserts.waitUntiLogMatch(
				LogAsserts.withLogLevel(LogLevel.ERROR).and(LogAsserts.withLogPayload(emptyFeedback)));

	}

	/**
	 * Should not add a feedback for bad treatment identifier.
	 */
	@Test
	public void shouldNotAddFeedbackWithBadTreatmentId() {

		final var feedback = new TreatmentValueFeedbackPayloadTest().nextModel();
		this.service.send(feedback);
		this.logAsserts.waitUntiLogMatch(
				LogAsserts.withLogLevel(LogLevel.ERROR).and(LogAsserts.withLogMessageContains(feedback.treatment_id)));

	}

	/**
	 * Should not add a feedback for undefined treatment.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotAddFeedbackForUndefinedTreatment(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.undefined(), undefinedId -> {

			asserter.putData("UNDEFINED_ID", undefinedId);
			final var feedback = new TreatmentValueFeedbackPayloadTest().nextModel();
			feedback.treatment_id = String.valueOf(undefinedId);
			this.service.send(feedback);

		});

		asserter.assertThat(() -> {

			final var undefinedId = String.valueOf(asserter.getData("UNDEFINED_ID"));
			return Uni.createFrom().item(undefinedId);

		}, undefinedId -> {

			this.logAsserts.waitUntiLogMatch(
					LogAsserts.withLogLevel(LogLevel.ERROR).and(LogAsserts.withLogMessageContains(undefinedId)));

		});

	}

}
