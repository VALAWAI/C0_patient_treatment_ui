/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0_patient_treatment_ui.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.panache.common.Sort.Direction;

/**
 * Test the {@link QueryParameters}.
 *
 * @see QueryParameters
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class QueryParametersTest {

	/**
	 * Should generate a sort for an empty query.
	 *
	 * @param query that is empty.
	 */
	@ParameterizedTest(name = "Should create sort for {0}")
	@NullSource
	@EmptySource
	@ValueSource(strings = { "   ", ",", "   ,   ,   " })
	public void shouldGenerateSortForEmptyQuery(String query) {

		final var sort = QueryParameters.toSort(query);
		final var columns = sort.getColumns();
		assertNotNull(columns);
		assertEquals(1, columns.size());

		final var column = columns.get(0);
		assertEquals("id", column.getName());
		assertEquals(Direction.Descending, column.getDirection());
	}

	/**
	 * Should generate a sort for a query.
	 *
	 * @param query that is empty.
	 */
	@ParameterizedTest(name = "Should create sort for {0}")
	@ValueSource(strings = { "+name,-description", "name,-description,-id", "  name  , - description " })
	public void shouldGenerateSortForQuery(String query) {

		final var sort = QueryParameters.toSort(query);
		final var columns = sort.getColumns();
		assertNotNull(columns);
		assertEquals(3, columns.size());

		var column = columns.get(0);
		assertEquals("name", column.getName());
		assertEquals(Direction.Ascending, column.getDirection());

		column = columns.get(1);
		assertEquals("description", column.getName());
		assertEquals(Direction.Descending, column.getDirection());

		column = columns.get(2);
		assertEquals("id", column.getName());
		assertEquals(Direction.Descending, column.getDirection());

	}

	/**
	 * Should generate a sort for a query with the 'id' field.
	 *
	 * @param query that is empty.
	 */
	@ParameterizedTest(name = "Should create sort for {0}")
	@ValueSource(strings = { "-name,id,+description", "-name,+id,description", " - name  , +  id  ,   description " })
	public void shouldGenerateSortForQueryWithId(String query) {

		final var sort = QueryParameters.toSort(query);
		final var columns = sort.getColumns();
		assertNotNull(columns);
		assertEquals(3, columns.size());

		var column = columns.get(0);
		assertEquals("name", column.getName());
		assertEquals(Direction.Descending, column.getDirection());

		column = columns.get(1);
		assertEquals("id", column.getName());
		assertEquals(Direction.Ascending, column.getDirection());

		column = columns.get(2);
		assertEquals("description", column.getName());
		assertEquals(Direction.Ascending, column.getDirection());

	}

	/**
	 * Should generate a pattern for an empty query.
	 *
	 * @param query that is empty.
	 */
	@ParameterizedTest(name = "Should create a pattern for {0}")
	@NullSource
	@EmptySource
	@ValueSource(strings = { "   " })
	public void shouldGeneratePatternForEmptyQuery(String query) {

		final var pattern = QueryParameters.toPattern(query);
		assertEquals("%", pattern);
	}

	/**
	 * Should generate a pattern from a query.
	 *
	 * @param query    to obtain the pattern.
	 * @param expected pattern to be returned.
	 */
	@ParameterizedTest(name = "Should create the pattern {1} for {0}")
	@CsvSource(textBlock = """
			,%
			*,%
			?,_
			%,\\%
			_,\\_
			*   *   ,%   %
			*******   ,%
			*a  ,%a
			a?,a_
			*a?,%a_
			?a?b?c,_a_b_c
			*a*b*c,%a%b%c
			?a,_a
			_a,\\_a
			?a*,_a%
			_a?,\\_a_
			?a*_?b*%,_a%\\__b%\\%
			""")
	public void shouldGeneratePatternForEmptyQuery(String query, String expected) {

		final var pattern = QueryParameters.toPattern(query);
		assertEquals(expected, pattern);
	}

}
