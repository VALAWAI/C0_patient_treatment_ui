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
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.messages.mov.LogAsserts;
import eu.valawai.c0_patient_treatment_ui.messages.mov.LogLevel;
import eu.valawai.c0_patient_treatment_ui.messages.mov.MOVTestResource;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
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
 * Test the {@link TreatmentActionFeedbackManager}.
 *
 * @see TreatmentActionFeedbackManager
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@WithTestResource(value = MOVTestResource.class)
@QuarkusTestResource(PostgreSQLTestResource.class)
public class TreatmentActionFeedbackManagerTest {

	/**
	 * The service to publish treatment feedbacks.
	 */
	@Inject
	TreatmentActionFeedbackService service;

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

		final var feedback = new TreatmentActionFeedbackPayloadTest().nextModel();
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

			final var payload = new TreatmentActionFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(undefinedId);
			asserter.putData("PAYLOAD", payload);
			this.service.send(payload);

		});

		asserter.assertThat(() -> {

			final TreatmentActionFeedbackPayload payload = (TreatmentActionFeedbackPayload) asserter.getData("PAYLOAD");
			return Uni.createFrom().item(payload);

		}, payload -> {

			this.logAsserts
					.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.ERROR).and(LogAsserts.withLogPayload(payload)));

		});

	}

	/**
	 * Should not add a feedback for an undefined treatment action.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotAddFeedbackForUndefinedTreatmentAction(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.nextRandom(1), last -> {

			asserter.putData("LAST", last);
			final var payload = new TreatmentActionFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(last.id);
			for (final var action : TreatmentAction.values()) {

				if (!last.treatmentActions.contains(action)) {

					payload.action = action;
					break;
				}

			}
			asserter.putData("PAYLOAD", payload);
			this.service.send(payload);

		});

		asserter.assertThat(() -> {

			final TreatmentActionFeedbackPayload payload = (TreatmentActionFeedbackPayload) asserter.getData("PAYLOAD");
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
			final var payload = new TreatmentActionFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(last.id);
			payload.action = ValueGenerator.next(last.treatmentActions);
			asserter.putData("PAYLOAD", payload);
			asserter.putData("NOW", TimeManager.now());
			this.service.send(payload);

		});

		asserter.assertThat(() -> {

			final TreatmentActionFeedbackPayload payload = (TreatmentActionFeedbackPayload) asserter.getData("PAYLOAD");
			return Uni.createFrom().item(payload);

		}, payload -> {

			this.logAsserts
					.waitUntiLogMatch(LogAsserts.withLogLevel(LogLevel.INFO).and(LogAsserts.withLogPayload(payload)));

		});

		asserter.assertThat(() -> {

			final TreatmentEntity last = (TreatmentEntity) asserter.getData("LAST");
			return TreatmentEntity.retrieve(last.id);

		}, updated -> {

			assertNotNull(updated.actionFeedbacks);
			final TreatmentEntity last = (TreatmentEntity) asserter.getData("LAST");
			if (last.actionFeedbacks == null) {

				assertEquals(1, updated.actionFeedbacks.size());

			} else {

				assertEquals(last.actionFeedbacks.size() + 1, updated.actionFeedbacks.size());
			}

			final long now = (Long) asserter.getData("NOW");
			final TreatmentActionFeedbackPayload payload = (TreatmentActionFeedbackPayload) asserter.getData("PAYLOAD");
			var found = false;
			for (final var actionFeedback : updated.actionFeedbacks) {

				if (now <= actionFeedback.createdTime && payload.action == actionFeedback.action) {
					found = true;
					assertTrue("Unexpected reported feedback time", now <= actionFeedback.createdTime);
					assertEquals(payload.feedback, actionFeedback.feedback);
					break;
				}
			}

			assertTrue("Not found stored action in the treatment", found);
		});

	}

}
