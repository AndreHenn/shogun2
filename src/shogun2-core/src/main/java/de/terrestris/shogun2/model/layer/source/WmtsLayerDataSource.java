package de.terrestris.shogun2.model.layer.source;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.terrestris.shogun2.model.layer.util.TileGrid;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Class representing a layer source for tile data from WMTS servers.
 *
 * @author Andre Henn
 * @author terrestris GmbH & Co. KG
 */
@Entity
@Table
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WmtsLayerDataSource extends LayerDataSource {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int cacheSize;
    private String crossOrigin;
    private TileGrid tileGrid;
    private String projection;
    private String layer;
    private String style;

    /**
     *
     */
    public WmtsLayerDataSource() {
        super();
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
        return new HashCodeBuilder(11, 19).
            appendSuper(super.hashCode()).
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
        if (!(obj instanceof WmtsLayerDataSource))
            return false;
        WmtsLayerDataSource other = (WmtsLayerDataSource) obj;

        return new EqualsBuilder().
            appendSuper(super.equals(other)).
            isEquals();
    }

}
