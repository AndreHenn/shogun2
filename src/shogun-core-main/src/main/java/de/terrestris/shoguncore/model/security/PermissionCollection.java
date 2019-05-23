package de.terrestris.shoguncore.model.security;

import de.terrestris.shoguncore.model.PersistentObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nils Bühner
 */
public class PermissionCollection extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private Set<Permission> permissions = new HashSet<Permission>();

    /**
     * Explicitly adding the default constructor as this is important, e.g. for
     * Hibernate: http://goo.gl/3Cr1pw
     */
    public PermissionCollection() {
    }

    /**
     * @param permissions
     */
    public PermissionCollection(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the permissions
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
