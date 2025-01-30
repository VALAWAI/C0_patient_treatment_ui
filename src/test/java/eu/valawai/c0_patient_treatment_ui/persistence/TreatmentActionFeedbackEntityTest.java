/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentActionFeedbackPayload;
import eu.valawai.c0_patient_treatment_ui.messages.TreatmentActionFeedbackPayloadTest;
import eu.valawai.c0_patient_treatment_ui.models.TreatmentAction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;

/**
 * Test the {@link TreatmentActionFeedbackEntity}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
public class TreatmentActionFeedbackEntityTest {

	/**
	 * Should not store a feedback with a bad treatment identifier.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotStoreWithBadIdentifier(TransactionalUniAsserter asserter) {

		asserter.assertFailedWith(() -> {

			final var payload = new TreatmentActionFeedbackPayloadTest().nextModel();
			return TreatmentActionFeedbackEntity.store(payload);

		}, NumberFormatException.class);
	}

	/**
	 * Should not store a feedback for undefined treatment.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotStoreWithUndefinedTreatment(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.undefined(), undefined -> asserter.putData("UNDEFINED", undefined));
		asserter.assertFailedWith(() -> {

			final Long undefined = (Long) asserter.getData("UNDEFINED");
			final var payload = new TreatmentActionFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(undefined);
			return TreatmentActionFeedbackEntity.store(payload);

		}, IllegalArgumentException.class);
	}

	/**
	 * Should not store with undefined action in treatment.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldNotStoreWithActionNotInTreatment(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.nextRandom(1), last -> asserter.putData("LAST", last));
		asserter.assertFailedWith(() -> {

			final TreatmentEntity last = (TreatmentEntity) asserter.getData("LAST");
			final var payload = new TreatmentActionFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(last.id);
			for (final var action : TreatmentAction.values()) {

				if (!last.treatmentActions.contains(action)) {

					payload.action = action;
					break;
				}
			}
			return TreatmentActionFeedbackEntity.store(payload);

		}, IllegalArgumentException.class);
	}

	/**
	 * Should store a feedback
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldPersist(TransactionalUniAsserter asserter) {

		asserter.assertThat(() -> TreatmentEntities.last(), last -> asserter.putData("LAST", last));
		asserter.assertThat(() -> {

			final TreatmentEntity last = (TreatmentEntity) asserter.getData("LAST");
			final var payload = new TreatmentActionFeedbackPayloadTest().nextModel();
			payload.treatment_id = String.valueOf(last.id);
			payload.action = ValueGenerator.next(last.treatmentActions);
			asserter.putData("PAYLOAD", payload);
			asserter.putData("NOW", TimeManager.now());
			return TreatmentActionFeedbackEntity.store(payload);

		}, stored -> {

			assertNotNull(stored);
			assertNotNull(stored.id);
			asserter.putData("PERSISTED", stored);

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

			final TreatmentActionFeedbackEntity stored = (TreatmentActionFeedbackEntity) asserter.getData("PERSISTED");
			var found = false;
			for (final var feedback : updated.actionFeedbacks) {

				if (feedback.id == stored.id) {
					found = true;
					final TreatmentActionFeedbackPayload payload = (TreatmentActionFeedbackPayload) asserter
							.getData("PAYLOAD");
					final long now = (Long) asserter.getData("NOW");
					assertTrue("Unexpected reported feedback time", now <= feedback.createdTime);
					assertEquals(payload.action, feedback.action);
					assertEquals(payload.feedback, feedback.feedback);
					break;
				}
			}

			assertTrue("Not found stored action in the treatment", found);
		});
	}

}
