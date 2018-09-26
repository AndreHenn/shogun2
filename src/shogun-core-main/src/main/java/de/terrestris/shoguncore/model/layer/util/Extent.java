package de.terrestris.shoguncore.model.layer.util;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.terrestris.shoguncore.model.PersistentObject;

/**
 * Util class representing the extent of a layer or a map.
 * The extent is modelled by the lower left and the upper
 * right point of the bounding rectangle
 * <p>
 * <pre>
 *                UR
 *     +--------o
 *     |        |
 *     o--------+
 *  LL
 * </pre>
 *
 * @author Andre Henn
 * @author terrestris GmbH & Co. KG
 */
public class Extent extends PersistentObject {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Point2D.Double lowerLeft;

    private Point2D.Double upperRight;

    /**
     *
     */
    public Extent() {
        super();
    }

    /**
     * @param lowerLeft
     * @param upperRight
     */
    public Extent(Double lowerLeft, Double upperRight) {
        super();
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    /**
     * @param lowerLeftX
     * @param lowerLeftY
     * @param upperRightX
     * @param upperRightY
     */
    public Extent(double lowerLeftX, double lowerLeftY,
                  double upperRightX, double upperRightY) {
        super();
        this.lowerLeft = new Double(lowerLeftX, lowerLeftY);
        this.upperRight = new Double(upperRightX, upperRightY);
        ;
    }

    /**
     * @return the lowerLeft
     */
    public Point2D.Double getLowerLeft() {
        return lowerLeft;
    }

    /**
     * @param lowerLeft the lowerLeft to set
     */
    public void setLowerLeft(Point2D.Double lowerLeft) {
        this.lowerLeft = lowerLeft;
    }

    /**
     * @return the upperRight
     */
    public Point2D.Double getUpperRight() {
        return upperRight;
    }

    /**
     * @param upperRight the upperRight to set
     */
    public void setUpperRight(Point2D.Double upperRight) {
        this.upperRight = upperRight;
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
        return new HashCodeBuilder(61, 13).
            appendSuper(super.hashCode()).
            append(getLowerLeft()).
            append(getUpperRight()).
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
        if (!(obj instanceof Extent))
            return false;
        Extent other = (Extent) obj;

        return new EqualsBuilder().
            append(getLowerLeft(), other.getLowerLeft()).
            append(getUpperRight(), other.getUpperRight()).
            isEquals();
    }

}
