/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.patients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import eu.valawai.c0_patient_treatment_ui.ReflectionModelTestCase;
import eu.valawai.c0_patient_treatment_ui.TimeManager;
import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.api.v1.patients.Patient;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntities;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientEntity;
import eu.valawai.c0_patient_treatment_ui.persistence.PatientStatusCriteriaTest;

/**
 * Test the {@link Patient}.
 *
 * @see Patient
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class PatientTest extends ReflectionModelTestCase<Patient> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Patient createEmptyModel() {

		return new Patient();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(Patient model) {

		model.id = ValueGenerator.rnd().nextLong();
		model.updateTime = ValueGenerator.rnd().nextLong(0, TimeManager.now() - 360000);
		model.name = ValueGenerator.nextPattern("Patient name {0}");
		model.status = new PatientStatusCriteriaTest().nextModel();

	}

	/**
	 * Check that can convert from/to {@link PatientEntity}.
	 *
	 * @see PatientEntity
	 */
	@Test
	public void shouldFromToPatientEntity() {

		final var entity = PatientEntities.nextRandom();
		final var model = Patient.from(entity);
		final var entity2 = model.toPatientEntity();
		assertNull(entity2.id);
		assertEquals(0l, entity2.updateTime);
		final var model2 = Patient.from(entity2);
		assertNotEquals(model, model2);
		model.id = null;
		model.updateTime = 0l;
		assertEquals(model, model2);

	}

	/**
	 * Check that can convert to/from {@link PatientEntity}.
	 *
	 * @see PatientEntity
	 */
	@Test
	public void shouldToFromPatient() {

		final var model = this.nextModel();
		final var entity = model.toPatientEntity();
		final var model2 = Patient.from(entity);
		assertNotEquals(model, model2);
		model.id = null;
		model.updateTime = 0l;
		assertEquals(model, model2);

	}

}
