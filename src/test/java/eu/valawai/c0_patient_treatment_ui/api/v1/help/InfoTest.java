/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api.v1.help;

import static eu.valawai.c0_patient_treatment_ui.ValueGenerator.next;
import static eu.valawai.c0_patient_treatment_ui.ValueGenerator.nextPattern;

import eu.valawai.c0_patient_treatment_ui.api.ModelTestCase;

/**
 * Test the {@link Info}.
 *
 * @see Info
 *
 * @author VALAWAI
 */
public class InfoTest extends ModelTestCase<Info> {

	/**
	 * {@inheritDoc}
	 *
	 * @see Info#Info()
	 */
	@Override
	public Info createEmptyModel() {

		return new Info();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(Info model) {

		model.profile = next("prod", "dev", "test");
		model.version = nextPattern("{0}.{1}.{2}", 3);

	}

}
