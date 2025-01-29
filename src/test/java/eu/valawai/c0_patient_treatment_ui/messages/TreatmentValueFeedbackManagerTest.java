/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.messages.mov.LogAsserts;
import eu.valawai.c0_patient_treatment_ui.messages.mov.LogLevel;
import eu.valawai.c0_patient_treatment_ui.messages.mov.MOVTestResource;
import eu.valawai.c0_patient_treatment_ui.persistence.PostgreSQLTestResource;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntities;
import eu.valawai.c0_patient_treatment_ui.persistence.TreatmentEntity;
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

			final var payload = new TreatmentValueFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(undefinedId);
			asserter.putData("PAYLOAD", payload);
			this.service.send(payload);

		});

		asserter.assertThat(() -> {

			final TreatmentValueFeedbackPayload payload = (TreatmentValueFeedbackPayload) asserter.getData("PAYLOAD");
			return Uni.createFrom().item(payload);

		}, payload -> {

			this.logAsserts
					.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.ERROR).and(LogAsserts.withLogPayload(payload)));

		});

	}

	/**
	 * Should not add a feedback for a bad alignment value.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotAddFeedbackForUndefinedTreatmentValue(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.nextRandom(), last -> {

			asserter.putData("LAST", last);
			final var payload = new TreatmentValueFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(last.id);
			payload.alignment = -2d;
			asserter.putData("PAYLOAD", payload);
			this.service.send(payload);

		});

		asserter.assertThat(() -> {

			final TreatmentValueFeedbackPayload payload = (TreatmentValueFeedbackPayload) asserter.getData("PAYLOAD");
			return Uni.createFrom().item(payload);

		}, payload -> {

			this.logAsserts
					.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.ERROR).and(LogAsserts.withLogPayload(payload)));

		});

	}

	/**
	 * Should add a feedback for a treatment.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldAddFeedbackForTreatment(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.nextRandom(), last -> {

			asserter.putData("LAST", last);
			final var payload = new TreatmentValueFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(last.id);
			asserter.putData("PAYLOAD", payload);
			asserter.putData("NOW", TimeManager.now());
			this.service.send(payload);

		});

		asserter.assertThat(() -> {

			final TreatmentValueFeedbackPayload payload = (TreatmentValueFeedbackPayload) asserter.getData("PAYLOAD");
			return Uni.createFrom().item(payload);

		}, payload -> {

			this.logAsserts
					.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.DEBUG).and(LogAsserts.withLogPayload(payload)));

		});

		asserter.assertThat(() -> {

			final TreatmentEntity last = (TreatmentEntity) asserter.getData("LAST");
			return TreatmentEntity.retrieve(last.id);

		}, updated -> {

			assertNotNull(updated.valueFeedbacks);
			final TreatmentEntity last = (TreatmentEntity) asserter.getData("LAST");
			if (last.valueFeedbacks == null) {

				assertEquals(1, updated.valueFeedbacks.size());

			} else {

				assertEquals(last.valueFeedbacks.size() + 1, updated.valueFeedbacks.size());
			}

			var found = false;
			final long now = (Long) asserter.getData("NOW");
			final TreatmentValueFeedbackPayload payload = (TreatmentValueFeedbackPayload) asserter.getData("PAYLOAD");
			for (final var feedback : updated.valueFeedbacks) {

				if (now <= feedback.createdTime && payload.value_name.equals(feedback.valueName)) {
					found = true;
					assertEquals(payload.alignment, feedback.alignment, 0.0000001d);
					break;
				}
			}

			assertTrue("Not found stored action in the treatment", found);
		});

	}

}
