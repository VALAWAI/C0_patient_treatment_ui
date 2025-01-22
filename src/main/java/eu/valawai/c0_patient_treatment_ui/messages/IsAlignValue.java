/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Annotation to mark a number is a valid alignment value.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Target({ FIELD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { IsAlignValueValidator.class })
@Documented
public @interface IsAlignValue {

	/**
	 * Return the error message to show if it is not valid.
	 *
	 * @return the error message.
	 */
	String message() default "The value has to be in the range [-1, 1].";

	/**
	 * Return the validator groups.
	 *
	 * @return the groups associated to the validator.
	 */
	Class<?>[] groups() default {};

	/**
	 * Return the validator payloads.
	 *
	 * @return the payloads associated to the validator.
	 */
	Class<? extends Payload>[] payload() default {};

}
