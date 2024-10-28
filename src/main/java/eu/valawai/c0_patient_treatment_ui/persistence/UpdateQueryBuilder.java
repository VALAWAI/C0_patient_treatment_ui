/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0_patient_treatment_ui.persistence;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

/**
 * A component used to build an update query.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class UpdateQueryBuilder {

	/**
	 * The update query that is building.
	 */
	protected static final String QUERY_PATTERN = "update %s t set %s where t.id = :id";

	/**
	 * The name of the table with the entity.
	 */
	protected String tableName;

	/**
	 * The parameters to set on the query.
	 */
	protected StringBuilder queryParameters;

	/**
	 * The parameters to the query.
	 */
	protected Parameters parameters;

	/**
	 * Create a builder.
	 *
	 * @param tableName name of the table that contains the entity.
	 * @param id        to the entity to update.
	 */
	private UpdateQueryBuilder(String tableName, long id) {

		this.tableName = tableName;
		this.queryParameters = new StringBuilder();
		this.parameters = Parameters.with("id", id);
	}

	/**
	 * Create the builder for the specified entity.
	 *
	 * @param entity to update.
	 *
	 * @return the builder to create an update to an entity.
	 */
	public static UpdateQueryBuilder withEntity(@NotNull PanacheEntity entity) {

		final String tableName = entity.getClass().getAnnotation(Entity.class).name();
		return withEntity(tableName, entity.id);

	}

	/**
	 * Create the builder for the specified identifier.
	 *
	 * @param tableName name of the table that contains the entity.
	 * @param id        to the entity to update.
	 *
	 * @return the builder to create an update to an entity.
	 */
	public static UpdateQueryBuilder withEntity(@NotNull String tableName, @NotNull Long id) {

		return new UpdateQueryBuilder(tableName, id);
	}

	/**
	 * Specify a parameter of the update query.
	 *
	 * @param key   that identify the parameter.
	 * @param value of the parameter.
	 *
	 * @return the builder to create an update to an entity.
	 */
	public UpdateQueryBuilder withParam(String key, Object value) {

		if (value != null) {

			if (!this.queryParameters.isEmpty()) {

				this.queryParameters.append(", ");
			}
			this.queryParameters.append("t.");
			this.queryParameters.append(key);
			this.queryParameters.append(" = :");
			final var paramKey = key.replaceAll("\\W", "_");
			this.queryParameters.append(paramKey);
			this.parameters = this.parameters.and(paramKey, value);

		}
		return this;
	}

	/**
	 * The query to execute.
	 *
	 * @return the update query.
	 */
	public String query() {

		return QUERY_PATTERN.formatted(this.tableName, this.queryParameters.toString());

	}

	/**
	 * The parameters to use.
	 *
	 * @return the query parameters.
	 */
	public Parameters parameters() {

		return this.parameters;
	}

}
