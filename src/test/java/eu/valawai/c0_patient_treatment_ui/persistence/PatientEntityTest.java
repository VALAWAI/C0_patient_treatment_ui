/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;

/**
 * Test the {@link PatientEntity}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
public class PatientEntityTest {

	/**
	 * Should update a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdate(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newPatient = PatientEntities.nextRandom();
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			newPatient.id = patient.id;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(newPatient.name, found.name);
			assertEquals(newPatient.status, found.status);
			assertTrue(found.updateTime >= now);

		});
	}

	/**
	 * Should retrieve a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldRetrieve(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		asserter.execute(() -> patient.persist()).assertThat(() -> PatientEntity.retrieve(patient.id), retrieved -> {

			assertNotNull(retrieved);
			assertEquals(patient.id, retrieved.id);
			assertEquals(patient.name, retrieved.name);
			assertEquals(patient.status, retrieved.status);
			assertEquals(patient.updateTime, retrieved.updateTime);
		});
	}

	/**
	 * Should delete a patient.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldDelete(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		asserter.execute(() -> patient.persist()).assertEquals(() -> PatientEntity.delete(patient.id), true)
				.assertNull(() -> PatientEntity.findById(patient.id));
	}

	/**
	 * Should update the {@link Status#ageRange} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateAgeRange(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newAgeRange = ValueGenerator.next(AgeRangeOption.values());
		final var now = TimeManager.now();
		while (patient.status.ageRange.equals(newAgeRange)) {

			patient.status.ageRange = ValueGenerator.next(AgeRangeOption.values());
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.ageRange = newAgeRange;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newAgeRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientEntity#name}.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateName(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newName = ValueGenerator.nextPattern("Patient name {0}");
		final var now = TimeManager.now();
		while (patient.name.equals(newName)) {

			patient.name = ValueGenerator.nextPattern("Patient name {0}");
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.name = newName;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(newName, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#ccd} from the {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateCcd(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newCcd = ValueGenerator.next(YesNoUnknownOption.values());
		final var now = TimeManager.now();
		while (patient.status.ccd.equals(newCcd)) {

			patient.status.ccd = ValueGenerator.next(YesNoUnknownOption.values());
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.ccd = newCcd;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(newCcd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#clinicalRiskGroup} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateClinicalRiskGroup(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newClinicalRiskGroup = ValueGenerator.next(ClinicalRiskGroupOption.values());
		final var now = TimeManager.now();
		while (patient.status.clinicalRiskGroup.equals(newClinicalRiskGroup)) {

			patient.status.clinicalRiskGroup = ValueGenerator.next(ClinicalRiskGroupOption.values());
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.clinicalRiskGroup = newClinicalRiskGroup;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newClinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#discomfortDegree} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateDiscomfortDegree(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newDiscomfortDegree = ValueGenerator.next(DiscomfortDegree.values());
		while (patient.status.discomfortDegree.equals(newDiscomfortDegree)) {

			patient.status.discomfortDegree = ValueGenerator.next(DiscomfortDegree.values());
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.discomfortDegree = newDiscomfortDegree;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(newDiscomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#expectedSurvival} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateExpectedSurvival(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newExpectedSurvival = ValueGenerator.next(SurvivalOptions.values());
		final var now = TimeManager.now();
		while (patient.status.expectedSurvival.equals(newExpectedSurvival)) {

			patient.status.expectedSurvival = ValueGenerator.next(SurvivalOptions.values());
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.expectedSurvival = newExpectedSurvival;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newExpectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#frailVIG} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateFrailVIG(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newFrailVIG = ValueGenerator.next(SPICT_Scale.values());
		final var now = TimeManager.now();
		while (patient.status.frailVIG.equals(newFrailVIG)) {

			patient.status.frailVIG = ValueGenerator.next(SPICT_Scale.values());
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.frailVIG = newFrailVIG;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newFrailVIG, found.status.frailVIG);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#hasAdvanceDirectives} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateHasAdvanceDirectives(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newhasAdvanceDirectives = ValueGenerator.next(YesNoUnknownOption.values());
		final var now = TimeManager.now();
		while (patient.status.hasAdvanceDirectives.equals(newhasAdvanceDirectives)) {

			patient.status.hasAdvanceDirectives = ValueGenerator.next(YesNoUnknownOption.values());
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.hasAdvanceDirectives = newhasAdvanceDirectives;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newhasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#hasBeenInformed} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateHasBeenInformed(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newHasBeenInformed = ValueGenerator.next(YesNoUnknownOption.values());
		final var now = TimeManager.now();
		while (patient.status.hasBeenInformed.equals(newHasBeenInformed)) {

			patient.status.hasBeenInformed = ValueGenerator.next(YesNoUnknownOption.values());
		}
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.hasBeenInformed = newHasBeenInformed;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newHasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#hasCognitiveImpairment} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateHasCognitiveImpairment(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newHasCognitiveImpairment = ValueGenerator.next(CognitiveImpairmentLevel.values());
		while (patient.status.hasCognitiveImpairment.equals(newHasCognitiveImpairment)) {

			patient.status.hasCognitiveImpairment = ValueGenerator.next(CognitiveImpairmentLevel.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.hasCognitiveImpairment = newHasCognitiveImpairment;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newHasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#hasEmocionalPain} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateHasEmocionalPain(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newHasEmocionalPain = ValueGenerator.next(YesNoUnknownOption.values());
		while (patient.status.hasEmocionalPain.equals(newHasEmocionalPain)) {

			patient.status.hasEmocionalPain = ValueGenerator.next(YesNoUnknownOption.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.hasEmocionalPain = newHasEmocionalPain;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newHasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#hasSocialSupport} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateHasSocialSupport(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newHasSocialSupport = ValueGenerator.next(YesNoUnknownOption.values());
		while (patient.status.hasSocialSupport.equals(newHasSocialSupport)) {

			patient.status.hasSocialSupport = ValueGenerator.next(YesNoUnknownOption.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.hasSocialSupport = newHasSocialSupport;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newHasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#independenceAtAdmission} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateIndependenceAtAdmission(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newIndependenceAtAdmission = ValueGenerator.next(BarthelIndex.values());
		while (patient.status.independenceAtAdmission.equals(newIndependenceAtAdmission)) {

			patient.status.independenceAtAdmission = ValueGenerator.next(BarthelIndex.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.independenceAtAdmission = newIndependenceAtAdmission;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newIndependenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#independenceInstrumentalActivities} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateIndependenceInstrumentalActivities(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newIndependenceInstrumentalActivities = ValueGenerator.next(LawtonIndex.values());
		while (patient.status.independenceInstrumentalActivities.equals(newIndependenceInstrumentalActivities)) {

			patient.status.independenceInstrumentalActivities = ValueGenerator.next(LawtonIndex.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.independenceInstrumentalActivities = newIndependenceInstrumentalActivities;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newIndependenceInstrumentalActivities, found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#isCoerced} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateIsCoerced(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newIsCoerced = ValueGenerator.next(YesNoUnknownOption.values());
		while (patient.status.isCoerced.equals(newIsCoerced)) {

			patient.status.isCoerced = ValueGenerator.next(YesNoUnknownOption.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.isCoerced = newIsCoerced;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newIsCoerced, found.status.isCoerced);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#isCompotent} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateIsCompotent(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newIsCompotent = ValueGenerator.next(YesNoUnknownOption.values());
		while (patient.status.isCompotent.equals(newIsCompotent)) {

			patient.status.isCompotent = ValueGenerator.next(YesNoUnknownOption.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.isCompotent = newIsCompotent;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newIsCompotent, found.status.isCompotent);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link Status#maca} from the {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateMaca(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newMaca = ValueGenerator.next(YesNoUnknownOption.values());
		while (patient.status.maca.equals(newMaca)) {

			patient.status.maca = ValueGenerator.next(YesNoUnknownOption.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new Status();
			newPatient.status.maca = newMaca;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newMaca, found.status.maca);
			assertEquals(patient.status.ageRange, found.status.ageRange);
			assertEquals(patient.status.ccd, found.status.ccd);
			assertEquals(patient.status.clinicalRiskGroup, found.status.clinicalRiskGroup);
			assertEquals(patient.status.discomfortDegree, found.status.discomfortDegree);
			assertEquals(patient.status.expectedSurvival, found.status.expectedSurvival);
			assertEquals(patient.status.frailVIG, found.status.frailVIG);
			assertEquals(patient.status.hasAdvanceDirectives, found.status.hasAdvanceDirectives);
			assertEquals(patient.status.hasBeenInformed, found.status.hasBeenInformed);
			assertEquals(patient.status.hasCognitiveImpairment, found.status.hasCognitiveImpairment);
			assertEquals(patient.status.hasEmocionalPain, found.status.hasEmocionalPain);
			assertEquals(patient.status.hasSocialSupport, found.status.hasSocialSupport);
			assertEquals(patient.status.independenceAtAdmission, found.status.independenceAtAdmission);
			assertEquals(patient.status.independenceInstrumentalActivities,
					found.status.independenceInstrumentalActivities);
			assertEquals(patient.status.isCoerced, found.status.isCoerced);
			assertEquals(patient.status.isCompotent, found.status.isCompotent);

		});
	}

}
