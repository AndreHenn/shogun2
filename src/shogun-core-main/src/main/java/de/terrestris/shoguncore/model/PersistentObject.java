package de.terrestris.shoguncore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.terrestris.shogun2.model.security.PermissionCollection;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the abstract superclass for all entities that are
 * persisted in the database.
 * <p>
 * Subclasses of this class can further be inherited and there should be no
 * problems with hibernate mappings/database interactions.
 *
 * @author Nils Bühner
 */
public abstract class PersistentObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private final Integer id = null;

    /**
     * The getter of this property {@link #getCreated()} is annotated with
     * {@link JsonIgnore}. This way, the annotation can be overwritten in
     * subclasses.
     */
    private final ReadableDateTime created;

    /**
     * The getter of this property {@link #getModified()} is annotated with
     * {@link JsonIgnore}. This way, the annotation can be overwritten in
     * subclasses.
     */
    private ReadableDateTime modified;

    /**
     *
     */
    private Map<User, PermissionCollection> userPermissions = new HashMap<User, PermissionCollection>();

    /**
     *
     */
    private Map<UserGroup, PermissionCollection> groupPermissions = new HashMap<UserGroup, PermissionCollection>();

    /**
     * Constructor
     */
    protected PersistentObject() {
        this.created = DateTime.now();
        this.modified = DateTime.now();
    }

    /**
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * Ignore the {@link #created} property when de-/serializing.
     * This can be overwritten in subclasses.
     *
     * @return The date of the creation of the entity.
     */
    @JsonIgnore
    public ReadableDateTime getCreated() {
        return created;
    }

    /**
     * Ignore the {@link #modified} property when de-/serializing.
     * This can be overwritten in subclasses.
     *
     * @return The date of the last modification of the entity.
     */
    @JsonIgnore
    public ReadableDateTime getModified() {
        return modified;
    }

    /**
     * @param modified
     */
    public void setModified(ReadableDateTime modified) {
        this.modified = modified;
    }

    /**
     * @return the userPermissions
     */
    public Map<User, PermissionCollection> getUserPermissions() {
        return userPermissions;
    }

    /**
     * @param userPermissions the userPermissions to set
     */
    public void setUserPermissions(Map<User, PermissionCollection> userPermissions) {
        this.userPermissions = userPermissions;
    }

    /**
     * @return the groupPermissions
     */
    public Map<UserGroup, PermissionCollection> getGroupPermissions() {
        return groupPermissions;
    }

    /**
     * @param groupPermissions the groupPermissions to set
     */
    public void setGroupPermissions(
        Map<UserGroup, PermissionCollection> groupPermissions) {
        this.groupPermissions = groupPermissions;
    }

    /**
     * @see java.lang.Object#hashCode()
     * <p>
     * According to
     * http://stackoverflow.com/questions/27581/overriding-equals
     * -and-hashcode-in-java it is recommended only to use getter-methods
     * when using ORM like Hibernate
     */
    public int hashCode() {
        // two randomly chosen prime numbers
        return new HashCodeBuilder(17, 43).
            append(getClass()).
            append(getCreated()).
            toHashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * <p>
     * According to
     * http://stackoverflow.com/questions/27581/overriding-equals
     * -and-hashcode-in-java it is recommended only to use getter-methods
     * when using ORM like Hibernate
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof PersistentObject))
            return false;
        PersistentObject other = (PersistentObject) obj;

        return new EqualsBuilder().
            append(getClass(), other.getClass()).
            append(getCreated(), other.getCreated()).
            isEquals();
    }

    /**
     *
     */
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .append("created", getCreated())
            .append("modified", getModified())
            .toString();
    }

}
