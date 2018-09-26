package de.terrestris.shoguncore.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nils Bühner
 */
public class UserGroup extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private String name;

    private User owner;

    private Set<User> members = new HashSet<User>();

    private Set<Role> roles = new HashSet<Role>();

    /**
     * Default Constructor
     */
    public UserGroup() {
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * @return the members
     */
    public Set<User> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(Set<User> members) {
        this.members = members;
    }

    /**
     * @return the roles
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * @see java.lang.Object#hashCode()
     * <p>
     * According to
     * http://stackoverflow.com/questions/27581/overriding-equals
     * -and-hashcode-in-java it is recommended only to use getter-methods
     * when using ORM like Hibernate
     */
    @Override
    public int hashCode() {
        // two randomly chosen prime numbers
        return new HashCodeBuilder(53, 19).
            appendSuper(super.hashCode()).
            append(getName()).
            append(getOwner()).
            append(getMembers()).
            append(getRoles()).
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
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserGroup))
            return false;
        UserGroup other = (UserGroup) obj;

        return new EqualsBuilder().
            appendSuper(super.equals(other)).
            append(getName(), other.getName()).
            append(getOwner(), other.getOwner()).
            append(getMembers(), other.getMembers()).
            append(getRoles(), other.getRoles()).
            isEquals();
    }
}
