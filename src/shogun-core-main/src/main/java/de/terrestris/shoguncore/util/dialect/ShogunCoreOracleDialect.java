package de.terrestris.shoguncore.util.dialect;


import java.sql.Types;

/**
 * SQL {@link Dialect} extending {@link Oracle12cDialect} to register
 * column mapping for LOB datatypes used in SHOGun-Core (e.g. file content
 * in {@link de.terrestris.shoguncore.model.File})
 *
 * @author Andre Henn
 * @author terrestris GmbH & co. KG
 */
public class ShogunCoreOracleDialect {

    /**
     *
     */
    public ShogunCoreOracleDialect() {
        super();
    }

    /**
     *

    protected void registerLargeObjectTypeMappings() {
        super.registerLargeObjectTypeMappings();

        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.LONGVARCHAR, "clob");
        registerColumnType(Types.LONGVARBINARY, "long raw");
    }
     */
}
