/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/


export type AgeRangeOption = 'AGE_BETWEEN_0_AND_19'
	| 'AGE_BETWEEN_20_AND_29'
	| 'AGE_BETWEEN_30_AND_39'
	| 'AGE_BETWEEN_40_AND_49'
	| 'AGE_BETWEEN_50_AND_59'
	| 'AGE_BETWEEN_60_AND_69'
	| 'AGE_BETWEEN_70_AND_79'
	| 'AGE_BETWEEN_80_AND_89'
	| 'AGE_BETWEEN_90_AND_99'
	| 'AGE_MORE_THAN_99'
	| null;

export type YesNoUnknownOption = 'YES' | 'NO' | 'UNKNOWN' | null;

export type SurvivalOptions = 'LESS_THAN_12_MONTHS' | 'MORE_THAN_12_MONTHS' | 'UNKNOWN' | null;

export type SPICT_Scale = 'LOW' | 'MODERATE' | 'HIGH' | 'UNKNOWN' | null;

export type ClinicalRiskGroupOption = 'PROMOTION_AND_PREVENTION'
	| 'SELF_MANAGEMENT_SUPPORT'
	| 'ILLNESS_MANAGEMENT'
	| 'CASE_MANAGEMENT'
	| 'UNKNOWN'
	| null;

export type BarthelIndex = 'TOTAL' | 'SEVERE' | 'MODERATE' | 'MILD' | 'INDEPENDENT' | 'UNKNOWN' | null;

export type LawtonIndex = 'ZERO' | 'ONE' | 'TWO' | 'THREE' | 'FOUR' | 'FIVE' | 'SIX' | 'SEVEN' | 'EIGHT' | 'UNKNOWN' | null;

export type CognitiveImpairmentLevel = 'LOABSENTW' | 'MILD_MODERATE' | 'SEVERE' | 'UNKNOWN' | null;

export type DiscomfortDegree = 'LOW' | 'MEDIUM' | 'HIGH' | 'UNKNOWN' | null;

/**
 * The minimal information of a patient.
 *
 * @author VALAWAI
 */
export class PatientStatusCriteria {

	/**
	 * The range of age of the patient status.
	 */
	public ageRange: AgeRangeOption = null;

	/**
	 * Check if the patient status has a Complex Cronic Disease (CCD).
	 */
	public ccd: YesNoUnknownOption = null;

	/**
	 * A MACA patient status has answered no to the question: Would you be surprised
	 * if this patient died in less than 12 months?
	 */
	public maca: YesNoUnknownOption = null;

	/**
	 * The expected survival time for the patient status.
	 */
	public expectedSurvival: SurvivalOptions = null;

	/**
	 * The fragility index of the patient status.
	 */
	public frailVIG: SPICT_Scale = null;

	/**
	 * The clinical risk group of the patient status.
	 */
	public clinicalRiskGroup: ClinicalRiskGroupOption = null;

	/**
	 * Check if the patient status has social support.
	 */
	public hasSocialSupport: YesNoUnknownOption = null;

	/**
	 * The independence for basic activities of daily living at admission.
	 */
	public independenceAtAdmission: BarthelIndex = null;

	/**
	 * The index that measures the independence for instrumental activities.
	 */
	public independenceInstrumentalActivities: LawtonIndex = null;

	/**
	 * The answers to the question: Does the patient status have advance directives?
	 */
	public hasAdvanceDirectives: YesNoUnknownOption = null;

	/**
	 * The answers to the question: Is the patient status competent to understand
	 * the instructions of health personnel?
	 */
	public isCompotent: YesNoUnknownOption = null;

	/**
	 * The answers to the question: To the patient status or his/her referent
	 * authorized has been informed of possible treatments and the consequences of
	 * receiving it or No.
	 */
	public hasBeenInformed: YesNoUnknownOption = null;

	/**
	 * The answers to the question: Is it detected that the patient status has seen
	 * coerced/pressured by third parties?
	 */
	public isCoerced: YesNoUnknownOption = null;

	/**
	 * Inform if the patient status has cognitive impairment.
	 */
	public hasCognitiveImpairment: CognitiveImpairmentLevel = null;

	/**
	 * Inform if the patient status has emotional pain.
	 */
	public hasEmocionalPain: YesNoUnknownOption = null;

	/**
	 * Describe the degree of discomfort of the patient status before applying any
	 * action.
	 */
	public discomfortDegree: DiscomfortDegree = null;


}
