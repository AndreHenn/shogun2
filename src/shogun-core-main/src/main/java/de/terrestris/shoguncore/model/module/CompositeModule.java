/**
 *
 */
package de.terrestris.shoguncore.model.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class represents a (simple) composite {@link Module}, i.e. a module
 * having children/submodules.
 *
 * @author Nils Bühner
 */
public class CompositeModule extends Module {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private List<Module> subModules = new ArrayList<Module>();

    /**
     * Explicitly adding the default constructor as this is important, e.g. for
     * Hibernate: http://goo.gl/3Cr1pw
     */
    public CompositeModule() {
    }

    /**
     * @param module
     */
    public void addModule(Module module) {
        this.subModules.add(module);
    }

    /**
     * @param module
     */
    public void remove(Module module) {
        this.subModules.remove(module);
    }

    /**
     * @return the subModules
     */
    public List<Module> getSubModules() {
        return subModules;
    }

    /**
     * @param subModules the subModules to set
     */
    public void setSubModules(List<Module> subModules) {
        this.subModules = subModules;
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
        return new HashCodeBuilder(17, 19)
            .appendSuper(super.hashCode())
            .append(getSubModules())
            .toHashCode();
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
        if (!(obj instanceof CompositeModule))
            return false;
        CompositeModule other = (CompositeModule) obj;

        return new EqualsBuilder()
            .appendSuper(super.equals(other))
            .append(getSubModules(), other.getSubModules())
            .isEquals();
    }
}
