package de.terrestris.shoguncore.util.naming;

/**
 * @author Nils Bühner
 */
public class PhysicalNamingStrategyShogunCoreTest {


    @Test
    public void passesIdentifiersThrough() {
        Dialect dialect = new H2Dialect();
        JdbcEnvironment context = Mockito.mock(JdbcEnvironment.class);
        when(context.getDialect()).thenReturn(dialect);
        Identifier identifier = Identifier.toIdentifier("someName");
        assertEquals(namingStrategy.toPhysicalColumnName(identifier, context), identifier);
        assertEquals(namingStrategy.toPhysicalTableName(identifier, context), identifier);
        assertEquals(namingStrategy.toPhysicalCatalogName(identifier, context), identifier);
        assertEquals(namingStrategy.toPhysicalSchemaName(identifier, context), identifier);
        assertEquals(namingStrategy.toPhysicalSequenceName(identifier, context), identifier);
    }

}
