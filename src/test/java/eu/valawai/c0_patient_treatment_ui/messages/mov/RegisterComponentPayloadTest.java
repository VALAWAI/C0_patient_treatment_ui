/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.messages.mov;

import eu.valawai.c0_patient_treatment_ui.ValueGenerator;
import eu.valawai.c0_patient_treatment_ui.messages.PayloadTestCase;

/**
 * Test the {@link RegisterComponentPayload}.
 *
 * @see RegisterComponentPayload
 *
 * @author VALAWAI
 */
public class RegisterComponentPayloadTest extends PayloadTestCase<RegisterComponentPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RegisterComponentPayload createEmptyModel() {

		return new RegisterComponentPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(RegisterComponentPayload payload) {

		payload.version = ValueGenerator.nextInt(0, 10) + "." + ValueGenerator.nextInt(0, 100) + "."
				+ ValueGenerator.nextInt(0, 100);
	}

}
