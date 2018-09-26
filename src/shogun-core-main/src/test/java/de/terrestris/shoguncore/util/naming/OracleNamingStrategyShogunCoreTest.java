package de.terrestris.shoguncore.util.naming;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author Andre Henn
 */
public class OracleNamingStrategyShogunCoreTest extends PhysicalNamingStrategyShogunCoreTest {

    private final String TEST_PREFIX = "pref_";

    private OracleNamingStrategyShogunCore oracleNamingStrategyShogunCore;

    @Before
    public void initStrategy() {
        this.oracleNamingStrategyShogunCore = new OracleNamingStrategyShogunCore();
        oracleNamingStrategyShogunCore.setColumnNamePrefix("pref_");
    }

    /**
     * Tests whether physical column are transformed to lowercase.
     *
     * @throws SQLException
     */
    @Test
    public void testPhysicalColumnNamesAreLowercaseForOracleDialect() throws SQLException {
        String columnName = "SomeCamelCaseColumn";
        String expectedPhysicalName = "somecamelcasecolumn";

    }

    /**
     * Tests whether physical column are transformed to lowercase.
     *
     * @throws SQLException
     */
    @Test
    public void testPhysicalColumnNamesAddPrefixToReservedOracleWord() throws SQLException {
        String columnName = "index";
        String expectedPhysicalName = TEST_PREFIX + "index";

    }

}
