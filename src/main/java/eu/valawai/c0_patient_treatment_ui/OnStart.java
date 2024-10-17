/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui;

import io.vertx.mutiny.ext.web.Router;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * The class that manage when the application has been started.
 *
 * @author VALAWAI
 */
@ApplicationScoped
public class OnStart {

	/**
	 * Called when the application has been started.
	 *
	 * @param router for the webs.
	 */
	public void init(@Observes Router router) {

		router.getWithRegex("/.*").last().handler(rc -> {

			final var path = rc.normalizedPath();
			if (path.matches("/([ca|es|en]/)?index.html")) {

				rc.fail(404);

			} else {

				var lang = "en";
				if (path.matches("/([ca|es|en]/).*")) {

					lang = path.substring(1, 3);
				}
				rc.reroute("/" + lang + "/index.html");
			}

		});
	}

}
