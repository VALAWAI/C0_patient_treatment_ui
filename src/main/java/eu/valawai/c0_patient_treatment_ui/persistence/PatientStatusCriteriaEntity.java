/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import eu.valawai.c0_patient_treatment_ui.messages.PatientStatusCriteriaPayload;
import eu.valawai.c0_patient_treatment_ui.models.LawtonIndex;
import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteria;
import eu.valawai.c0_patient_treatment_ui.models.YesNoUnknownOption;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

/**
 * An entity that stores the information of a patient status.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Entity(name = PatientStatusCriteriaEntity.TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = "PatientStatusCriteriaEntity.retrievByStatus", query = "select p from PATIENT_STATUS_CRITERIA p where p.status = ?1") })
public class PatientStatusCriteriaEntity extends PanacheEntity {

	/**
	 * The name of the table that will contains the patients.
	 */
	public static final String TABLE_NAME = "PATIENT_STATUS_CRITERIA";

	/**
	 * The current status of the patient.
	 */
	@Embedded
	public PatientStatusCriteria status;

	/**
	 * Retrieve the patient status criteria with the specified identifier.
	 *
	 * @param id identifier of the patient status criteria to retrieve.
	 *
	 * @return the patient status criteria associated to the identifier.
	 */
	public static Uni<PatientStatusCriteriaEntity> retrieve(long id) {

		final Uni<PatientStatusCriteriaEntity> action = PatientStatusCriteriaEntity.find("id", id).firstResult();
		return action.onItem().ifNull()
				.failWith(() -> new IllegalArgumentException("Not found a patient status criteria with the id " + id));
	}

	/**
	 * Retrieve the patient status criteria with the specified status.
	 *
	 * @param status of the entity to get.
	 *
	 * @return the patient status criteria associated to the status.
	 */
	public static Uni<PatientStatusCriteriaEntity> retrieveByStatus(PatientStatusCriteria status) {

		final Uni<PatientStatusCriteriaEntity> action = PatientStatusCriteriaEntity
				.find("#PatientStatusCriteriaEntity.retrievByStatus", status).firstResult();
		return action.onItem().ifNull().failWith(
				() -> new IllegalArgumentException("Not found a patient status criteria with the specified status."));
	}

	/**
	 * Retrieve the patient status criteria with the specified status or persist if
	 * not exist.
	 *
	 * @param status of the entity to get.
	 *
	 * @return the patient status criteria associated to the status.
	 */
	public static Uni<PatientStatusCriteriaEntity> retrieveOrPersist(PatientStatusCriteria status) {

		if (status == null) {

			return Uni.createFrom().failure(new IllegalArgumentException("The status is null"));

		} else {

			return retrieveByStatus(status).onFailure().recoverWithUni(() -> {

				final var entity = new PatientStatusCriteriaEntity();
				entity.status = status;
				return entity.persistAndFlush();

			});

		}

	}

	/**
	 * Convert this entity to a {@link PatientStatusCriteriaPayload}.
	 *
	 * @return the patient status criteria payload with the data of this
	 */
	public PatientStatusCriteriaPayload toPatientStatusCriteriaPayload() {

		final var payload = new PatientStatusCriteriaPayload();
		if (this.status != null) {

			payload.age_range = this.status.ageRange;
			payload.ccd = toBoolean(this.status.ccd);
			payload.maca = toBoolean(this.status.maca);
			payload.expected_survival = this.status.expectedSurvival;
			payload.frail_VIG = this.status.frailVIG;
			payload.clinical_risk_group = this.status.clinicalRiskGroup;
			payload.has_social_support = toBoolean(this.status.hasSocialSupport);
			payload.independence_at_admission = this.status.independenceAtAdmission;
			payload.independence_instrumental_activities = toInteger(this.status.independenceInstrumentalActivities);
			payload.has_advance_directives = toBoolean(this.status.hasAdvanceDirectives);
			payload.is_competent = toBoolean(this.status.isCompetent);
			payload.has_been_informed = toBoolean(this.status.hasBeenInformed);
			payload.is_coerced = toBoolean(this.status.isCoerced);
			payload.has_cognitive_impairment = this.status.hasCognitiveImpairment;
			payload.has_emocional_pain = toBoolean(this.status.hasEmocionalPain);
			payload.discomfort_degree = this.status.discomfortDegree;
			payload.nit_level = this.status.nitLevel;

		}
		return payload;

	}

	/**
	 * Convert the index to an integer.
	 *
	 * @param index to convert.
	 *
	 * @return the integer value of the index.
	 */
	private static Integer toInteger(LawtonIndex index) {

		if (index == null) {

			return null;

		} else {

			return switch (index) {
			case ZERO -> 0;
			case ONE -> 1;
			case TWO -> 2;
			case THREE -> 3;
			case FOUR -> 4;
			case FIVE -> 5;
			case SIX -> 6;
			case SEVEN -> 7;
			case EIGHT -> 8;
			default -> null;
			};
		}
	}

	/**
	 * Convert a {@link YesNoUnknownOption} to a boolean.
	 *
	 * @parma option to convert to a boolean value.
	 */
	private static Boolean toBoolean(YesNoUnknownOption option) {

		if (option == YesNoUnknownOption.YES) {

			return Boolean.TRUE;

		} else if (option == YesNoUnknownOption.NO) {

			return Boolean.FALSE;

		} else {

			return null;
		}

	}

}
