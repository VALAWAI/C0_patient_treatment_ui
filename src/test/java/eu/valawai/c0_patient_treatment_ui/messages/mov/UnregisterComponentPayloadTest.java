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
 * Test the {@link UnregisterComponentPayload}.
 *
 * @see UnregisterComponentPayload
 *
 * @author VALAWAI
 */
public class UnregisterComponentPayloadTest extends PayloadTestCase<UnregisterComponentPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UnregisterComponentPayload createEmptyModel() {

		return new UnregisterComponentPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(UnregisterComponentPayload payload) {

		payload.component_id = ValueGenerator.nextPattern("component_{0}");
	}

}
