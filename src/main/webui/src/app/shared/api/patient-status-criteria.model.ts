/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

import { AgeRangeOption } from './age-range-option.model';
import { SurvivalOption } from './survival-option.model';
import { YesNoUnknownOption } from './yes-no-unknown-option.model';
import { SPICT_Scale } from './spict-scale.model';
import { ClinicalRiskGroupOption } from './clinical-risk-group-option.model';
import { BarthelIndex } from './barthel-index.model';
import { LawtonIndex } from './lawton-index.model';
import { CognitiveImpairmentLevel } from './cognitive-impairment-level.model';

export type DiscomfortDegree = 'LOW' | 'MEDIUM' | 'HIGH' | 'UNKNOWN' | null;

export type NITLevel = 'ONE' | 'TWO_A' | 'TWO_B' | 'THREE' | 'FOUR' | 'FIVE' | null;

/**
 * The minimal information of a patient.
 *
 * @author VALAWAI
 */
export class PatientStatusCriteria {

	/**
	 * The range of age of the patient status.
	 */
	public ageRange: AgeRangeOption | null = null;

	/**
	 * Check if the patient status has a Complex Cronic Disease (CCD).
	 */
	public ccd: YesNoUnknownOption | null = null;

	/**
	 * A MACA patient status has answered no to the question: Would you be surprised
	 * if this patient died in less than 12 months?
	 */
	public maca: YesNoUnknownOption | null = null;

	/**
	 * The expected survival time for the patient status.
	 */
	public expectedSurvival: SurvivalOption | null = null;

	/**
	 * The fragility index of the patient status.
	 */
	public frailVIG: SPICT_Scale | null = null;

	/**
	 * The clinical risk group of the patient status.
	 */
	public clinicalRiskGroup: ClinicalRiskGroupOption | null = null;

	/**
	 * Check if the patient status has social support.
	 */
	public hasSocialSupport: YesNoUnknownOption | null = null;

	/**
	 * The independence for basic activities of daily living at admission.
	 */
	public independenceAtAdmission: BarthelIndex | null = null;

	/**
	 * The index that measures the independence for instrumental activities.
	 */
	public independenceInstrumentalActivities: LawtonIndex | null = null;

	/**
	 * The answers to the question: Does the patient status have advance directives?
	 */
	public hasAdvanceDirectives: YesNoUnknownOption | null = null;

	/**
	 * The answers to the question: Is the patient status competent to understand
	 * the instructions of health personnel?
	 */
	public isCompetent: YesNoUnknownOption | null = null;

	/**
	 * The answers to the question: To the patient status or his/her referent
	 * authorized has been informed of possible treatments and the consequences of
	 * receiving it or No.
	 */
	public hasBeenInformed: YesNoUnknownOption | null = null;

	/**
	 * The answers to the question: Is it detected that the patient status has seen
	 * coerced/pressured by third parties?
	 */
	public isCoerced: YesNoUnknownOption | null = null;

	/**
	 * Inform if the patient status has cognitive impairment.
	 */
	public hasCognitiveImpairment: CognitiveImpairmentLevel | null = null;

	/**
	 * Inform if the patient status has emotional pain.
	 */
	public hasEmocionalPain: YesNoUnknownOption | null = null;

	/**
	 * Describe the degree of discomfort of the patient status before applying any
	 * action.
	 */
	public discomfortDegree: DiscomfortDegree = null;

	/**
	 * Describe the level of therapeutic intensity of the patient
	 */
	public nitLevel: NITLevel = null;

}
