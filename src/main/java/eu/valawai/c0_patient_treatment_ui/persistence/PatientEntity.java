/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;

/**
 * An entity that stores the information of a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity
public class PatientEntity extends PanacheEntity {

	/**
	 * The name of the patient.
	 */
	public String name;

	/**
	 * Store the current patient.
	 *
	 * @return the identifier of the stored entity.
	 */
	public Uni<Long> store() {

		final Uni<PatientEntity> action = this.persist();
		return action.map(entity -> {

			this.id = entity.id;
			return this.id;
		});
	}

}
