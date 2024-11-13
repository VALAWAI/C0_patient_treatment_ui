/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.api;

import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;

/**
 * Provides some comon utilities taht can be used when interact with an API
 * method.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public interface QueryParameters {

	/**
	 * Return the sort from a query parameter.
	 *
	 * @param query to generate the sort operation.
	 *
	 * @return the sort specified.
	 */
	public static Sort toSort(String query) {

		var sort = Sort.empty();
		var id = false;
		if (query != null) {

			final var params = query.trim().split(",");
			for (var param : params) {

				param = param.trim();
				var direction = Direction.Ascending;
				if (param.startsWith("-")) {

					direction = Direction.Descending;
					param = param.substring(1).trim();

				} else if (param.startsWith("+")) {

					param = param.substring(1).trim();
				}

				if (!param.isEmpty()) {

					sort = sort.and(param, direction);
					if ("id".equals(param)) {

						id = true;
					}
				}
			}
		}

		if (!id) {

			sort = sort.and("id", Direction.Descending);
		}

		return sort;

	}

	/**
	 * Obtain form a query parameter the pattern that can be used into a find.
	 *
	 * @param query to convert to a pattern.
	 *
	 * @return the pattern from the query.
	 */
	public static String toPattern(String query) {

		final var pattern = new StringBuilder();
		if (query != null) {

			final var input = query.trim();
			final var max = input.length();
			var multi = false;
			for (var i = 0; i < max; i++) {

				final var c = input.charAt(i);
				switch (c) {
				case '*' -> {
					if (!multi) {

						multi = true;
						pattern.append('%');
					}
					continue;
				}
				case '?' -> pattern.append('_');
				case '%' -> pattern.append("\\%");
				case '_' -> pattern.append("\\_");
				default -> pattern.append(c);
				}
				multi = false;
			}
		}

		if (pattern.isEmpty()) {

			return "%";

		} else {

			return pattern.toString();
		}
	}

}
