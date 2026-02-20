/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui;

import java.util.regex.Pattern;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
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
	private static final Pattern INDEX_PATTERN = Pattern.compile("(?:\\/|^)([a-z]{2})(?:[_\\/-]|$)");

	/**
	 * The name of the context variable that is used to mark that the request is
	 * re-routing.
	 */
	private static final String REROUTING_SOURCE = "re-routing-source";

		/**
	 * The UI root path.
	 */
	@ConfigProperty(name = "quarkus.http.root-path", defaultValue = "/")
	String uiRootPath;


	/**
	 * Called when the application has been started.
	 *
	 * @param router for the webs.
	 */
	public void init(@Observes Router router) {

		router.get().last().handler(rc -> {

			final var path = rc.normalizedPath();
			if (path.endsWith("env.js")) {
				// redirect to the API resource
				rc.reroute("/env.js");

			} else if (rc.get(REROUTING_SOURCE) == null && this.isHtmlRequest(rc)) {

				final var matcher = INDEX_PATTERN.matcher(path);
				var lang = "en";
				if (matcher.find()) {

					final var group = matcher.group();
					lang = group.substring(1, 3);
				}
				// Redirect for one Page Angular
				rc.put(REROUTING_SOURCE, path);
				Log.infov("Rerouting {0} to {1}{2}/index.html", path, this.uiRootPath, lang);
				rc.reroute(this.uiRootPath + lang + "/index.html");

			} else {
				// Must be handled by another
				Log.warnv("Unexpected routing to {0}", path);
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
