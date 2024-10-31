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

import java.util.UUID;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.MinPatientPage;
import io.quarkus.panache.common.Sort;
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
	 * Should update the {@link PatientStatusCriteria#ageRange} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#ccd} from the
	 * {@link PatientEntity#status} .
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#clinicalRiskGroup} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#discomfortDegree} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#expectedSurvival} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#frailVIG} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#hasAdvanceDirectives} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#hasBeenInformed} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#hasCognitiveImpairment} from
	 * the {@link PatientEntity#status} .
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#hasEmocionalPain} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#hasSocialSupport} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#independenceAtAdmission} from
	 * the {@link PatientEntity#status} .
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the
	 * {@link PatientStatusCriteria#independenceInstrumentalActivities} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#isCoerced} from the
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);
			assertEquals(patient.status.maca, found.status.maca);

		});
	}

	/**
	 * Should update the {@link PatientStatusCriteria#isCompetent} from the
	 * {@link PatientEntity#status} .
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldUpdateIsCompotent(TransactionalUniAsserter asserter) {

		final var patient = PatientEntities.nextRandom();
		final var newIsCompotent = ValueGenerator.next(YesNoUnknownOption.values());
		while (patient.status.isCompetent.equals(newIsCompotent)) {

			patient.status.isCompetent = ValueGenerator.next(YesNoUnknownOption.values());
		}
		final var now = TimeManager.now();
		asserter.execute(() -> patient.persist()).assertTrue(() -> {

			final var newPatient = new PatientEntity();
			newPatient.id = patient.id;
			newPatient.status = new PatientStatusCriteria();
			newPatient.status.isCompetent = newIsCompotent;
			return newPatient.update();

		}).assertThat(() -> PatientEntities.byId(patient.id), found -> {

			assertEquals(patient.name, found.name);
			assertTrue(found.updateTime >= now);
			assertNotNull(found.status);
			assertEquals(newIsCompotent, found.status.isCompetent);
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
	 * Should update the {@link PatientStatusCriteria#maca} from the
	 * {@link PatientEntity#status} .
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
			newPatient.status = new PatientStatusCriteria();
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
			assertEquals(patient.status.isCompetent, found.status.isCompetent);

		});
	}

	/**
	 * Should return an empty min patient page when no pattern.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinPatientPageForWhenBadIndex(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.assertThat(() -> PatientEntity.getMinPatientPageFor("%", Sort.descending("id"), -1, 10),
				found -> assertEquals(expected, found));

	}

	/**
	 * Should return an empty min patient page if the index is too high.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinPatientPageForWhenIndexIsTooHigh(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.assertThat(() -> PatientEntity.count(), total -> expected.total = Math.toIntExact(total)).assertThat(
				() -> PatientEntity.getMinPatientPageFor("%", Sort.descending("id"), expected.total + 1, 10),
				found -> assertEquals(expected, found));

	}

	/**
	 * Should return an empty min patient page because any patient match the query.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetEmptyMinPatientPageForWhenAnyMatch(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.assertThat(
				() -> PatientEntity.getMinPatientPageFor(UUID.randomUUID().toString(), Sort.descending("id"), 0, 10),
				found -> assertEquals(expected, found));

	}

	/**
	 * Should get some patients.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetMinPatientPageForSome(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		final var limit = 20;
		asserter.execute(() -> PatientEntities.populateWith(100))
				.assertThat(() -> PatientEntity.count(), total -> expected.total = Math.toIntExact(total))
				.assertThat(() -> PatientEntity.getMinPatientPageFor("%", Sort.ascending("id"), 10, limit), found -> {

					assertEquals(expected.total, found.total);
					assertNotNull(found.patients);
					assertEquals(limit, found.patients.size());
					var lastId = 0l;
					for (final var patient : found.patients) {

						assertTrue(lastId < patient.id);
						lastId = patient.id;
					}
				});

	}

	/**
	 * Should get some patients.
	 *
	 * @param asserter to use in the tests.
	 */
	@Test
	@RunOnVertxContext
	public void shouldGetMinPatientPageForMatchingPattern(TransactionalUniAsserter asserter) {

		final var expected = new MinPatientPage();
		asserter.execute(() -> PatientEntities.populateWith(100))
				.assertThat(() -> PatientEntity.count("name LIKE '% 1%'"),
						total -> expected.total = Math.toIntExact(total))
				.assertThat(() -> PatientEntity.getMinPatientPageFor("% 1%", Sort.descending("name"), 0, 10), found -> {

					assertEquals(expected.total, found.total);
					assertNotNull(found.patients);
					assertTrue(found.patients.size() <= 10);
					var lastName = "ZZZZZZZZZZZZZZZZZZZZZZZZZZ";
					for (final var patient : found.patients) {

						assertTrue(patient.name.compareTo(lastName) <= 0);
						lastName = patient.name;
					}
				});

	}

}
