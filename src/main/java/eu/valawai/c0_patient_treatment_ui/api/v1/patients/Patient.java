/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.patients;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import eu.valawai.c0_patient_treatment_ui.models.PatientStatusCriteria;

/**
 * The information of a patient.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "Contains the information of a patient.")
public class Patient extends MinPatient {

	/**
	 * The epoch time, in seconds, when the patient information has been updated.
	 */
	@Schema(title = "The epoch time, in seconds, when the patient information has been updated.", readOnly = true)
	public Long updateTime;

	/**
	 * The current status of the patient.
	 */
	@Schema(title = "The current status of the patient.")
	public PatientStatusCriteria status;

}
