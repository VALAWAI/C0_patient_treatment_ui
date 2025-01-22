/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.treatments;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import eu.valawai.c0_patient_treatment_ui.messages.IsAlignValue;
import eu.valawai.c0_patient_treatment_ui.models.ReflectionModel;
import jakarta.validation.constraints.NotNull;

/**
 * A value associated to a treatment.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Schema(title = "A value associated to a treatment.")
public class TreatmentValue extends ReflectionModel {

	/**
	 * The name of the value.
	 */
	@NotNull
	@Schema(title = "The value name.")
	public String name;

	/**
	 * The alignment of the value with the the treatment.
	 */
	@Schema(title = "The value alignment with the treatment.")
	@NotNull
	@IsAlignValue
	public double alignment;

	/**
	 * The epoch time, in second, that his action is updated.
	 */
	@Schema(title = "The epoch time, in seconds, that this feedback is updated.")
	public long updatedTime;

}
