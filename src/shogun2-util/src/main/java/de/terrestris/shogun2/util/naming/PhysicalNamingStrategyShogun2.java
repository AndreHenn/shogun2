package de.terrestris.shogun2.util.naming;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Limits identifier length if necessary.
 *
 * @author Nils Bühner
 *
 */
public class PhysicalNamingStrategyShogun2 extends PhysicalNamingStrategyStandardImpl {

	private static final long serialVersionUID = 1L;

	protected static final int LENGTH_LIMIT_ORACLE = 30;

	protected static final int LENGTH_LIMIT_POSTGRESQL = 63;

	/**
	 * Converts table names to lower case and limits the length if necessary.
	 */
	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {

		// call superclass and get string value
		Identifier tableIdentifier = super.toPhysicalTableName(name, context);

		return convertToLimitedLowerCase(context, tableIdentifier);
	}

	/**
	 * Converts column names to lower case and limits the length if necessary.
	 */
	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {

		// call superclass and get string value
		Identifier columnIdentifier = super.toPhysicalColumnName(name, context);

		return convertToLimitedLowerCase(context, columnIdentifier);
	}

	/**
	 * @param context
	 * @param identifier
	 * @return
	 */
	private Identifier convertToLimitedLowerCase(JdbcEnvironment context, Identifier identifier) {
		// always convert to lowercase
		String identifierText = identifier.getText().toLowerCase();

		// determine the length limit based on the JDBC context
		Integer lengthLimit = getIdentifierLengthLimit(context);

		// limit length if necessary
		if (lengthLimit != null && identifierText.length() > lengthLimit) {
			identifierText = StringUtils.substring(identifierText, 0, lengthLimit);
		}

		return Identifier.toIdentifier(identifierText);
	}

	/**
	 *
	 * Determines the identifier length limit for the given JDBC context.
	 * Returns null if no limitation is necessary.
	 *
	 * @param context
	 *            The current JDBC context
	 * @return The identifier length limit for the given context. null
	 *         otherwise.
	 */
	private Integer getIdentifierLengthLimit(JdbcEnvironment context) {

		// https://docs.jboss.org/hibernate/orm/5.0/javadocs/org/hibernate/dialect/package-summary.html
		String dialectName = context.getDialect().getClass().getSimpleName();

		if (dialectName.startsWith("Oracle")) {
			// identifier limit of 30 chars -->
			// http://stackoverflow.com/a/756569
			return LENGTH_LIMIT_ORACLE;

		} else if (dialectName.startsWith("PostgreSQL")) {
			// identifier limit of 63 chars -->
			// http://stackoverflow.com/a/8218026
			return LENGTH_LIMIT_POSTGRESQL;
		}
		// H2 has no limit --> http://stackoverflow.com/a/30477403

		return null;
	}

}
