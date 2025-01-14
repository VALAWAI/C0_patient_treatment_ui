/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui;

import java.util.regex.Pattern;

import io.vertx.mutiny.core.http.HttpHeaders;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
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
	 * The pattern to check the on page resource.
	 */
	private static final Pattern INDEX_PATTERN = Pattern.compile(".*(/[a-z]{2})(/.*)?");

	/**
	 * Called when the application has been started.
	 *
	 * @param router for the webs.
	 */
	public void init(@Observes Router router) {

		router.getWithRegex("/.*").last().handler(rc -> {

			final var path = rc.normalizedPath();
			if (path.endsWith("env.js")) {
				// redirect to the API resource
				rc.reroute("/env.js");

			} else if (this.isHtmlRequest(rc)) {

				final var matcher = INDEX_PATTERN.matcher(path);
				var lang = "en";
				if (matcher.find()) {

					final var group = matcher.group();
					lang = group.substring(1, 3);
				}
				// Redirect for one Page Angular
				rc.reroute("/" + lang + "/index.html");

			} else {
				// Must be handled by another
				rc.next();
			}

		});

	}

	/**
	 * Check if the request accept HTML page.
	 *
	 * @param context of the request.
	 *
	 * @return {@code true} if the request accept HTML content.
	 */
	private boolean isHtmlRequest(RoutingContext context) {

		final var accept = context.request().getHeader(HttpHeaders.ACCEPT);
		return accept == null || accept.contains("text/html");
	}

}
