/**
 *
 */
package de.terrestris.shogun2.model.module;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.terrestris.shogun2.model.layer.Layer;

/**
 * A module to search features of a WFS.
 *
 * @author Kai Volland
 *
 */
@Entity
@Table
public class WfsSearch extends Module {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A list of EPSG-Codes the should be available in the module.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "WFSSEARCH_LAYERS", joinColumns = @JoinColumn(name = "WFSSEARCH_ID") )
	@Column(name = "LAYER")
	@OrderColumn(name = "INDEX")
	private List<Layer> layers = new ArrayList<Layer>();

	private String wfsServerUrl;

	/*
	 * Characters needed to send a request.
	 */
	private Integer minSearchTextChars;

	/**
	 * The delay between hitting a key and sending the request in ms.
	 */
	private Integer typeDelay;

	/**
	 * A list of EPSG-Codes the should be available in the module.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "WFSSEARCH_FEATUREDATATYPES", joinColumns = @JoinColumn(name = "WFSSEARCH_ID") )
	@Column(name = "FEATUREDATATYPES")
	@OrderColumn(name = "INDEX")
	private List<String> allowedFeatureTypeDataTypes = new ArrayList<String>();

	/**
	 * The template of the grouping Header.
	 * See: http://docs.sencha.com/extjs/6.0/6.0.0-classic/#!/api/Ext.grid.feature.Grouping-cfg-groupHeaderTpl
	 */
	private String groupHeaderTpl;

	/**
	 * Explicitly adding the default constructor as this is important, e.g. for
	 * Hibernate: http://goo.gl/3Cr1pw
	 */
	public WfsSearch() {
	}

	/**
	 * @return the wfsServerUrl
	 */
	public String getWfsServerUrl() {
		return wfsServerUrl;
	}

	/**
	 * @param wfsServerUrl the wfsServerUrl to set
	 */
	public void setWfsServerUrl(String wfsServerUrl) {
		this.wfsServerUrl = wfsServerUrl;
	}

	/**
	 * @return the minSearchTextChars
	 */
	public Integer getMinSearchTextChars() {
		return minSearchTextChars;
	}

	/**
	 * @param minSearchTextChars the minSearchTextChars to set
	 */
	public void setMinSearchTextChars(Integer minSearchTextChars) {
		this.minSearchTextChars = minSearchTextChars;
	}

	/**
	 * @return the typeDelay
	 */
	public Integer getTypeDelay() {
		return typeDelay;
	}

	/**
	 * @param typeDelay the typeDelay to set
	 */
	public void setTypeDelay(Integer typeDelay) {
		this.typeDelay = typeDelay;
	}

	/**
	 * @return the allowedFeatureTypeDataTypes
	 */
	public List<String> getAllowedFeatureTypeDataTypes() {
		return allowedFeatureTypeDataTypes;
	}

	/**
	 * @param allowedFeatureTypeDataTypes the allowedFeatureTypeDataTypes to set
	 */
	public void setAllowedFeatureTypeDataTypes(List<String> allowedFeatureTypeDataTypes) {
		this.allowedFeatureTypeDataTypes = allowedFeatureTypeDataTypes;
	}

	/**
	 * @return the groupHeaderTpl
	 */
	public String getGroupHeaderTpl() {
		return groupHeaderTpl;
	}

	/**
	 * @param groupHeaderTpl the groupHeaderTpl to set
	 */
	public void setGroupHeaderTpl(String groupHeaderTpl) {
		this.groupHeaderTpl = groupHeaderTpl;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 *
	 *      According to
	 *      http://stackoverflow.com/questions/27581/overriding-equals
	 *      -and-hashcode-in-java it is recommended only to use getter-methods
	 *      when using ORM like Hibernate
	 */
	public int hashCode() {
		// two randomly chosen prime numbers
		return new HashCodeBuilder(37, 3).
				appendSuper(super.hashCode()).
				append(getWfsServerUrl()).
				append(getMinSearchTextChars()).
				append(getTypeDelay()).
				append(getAllowedFeatureTypeDataTypes()).
				append(getGroupHeaderTpl()).
				toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 *
	 *      According to
	 *      http://stackoverflow.com/questions/27581/overriding-equals
	 *      -and-hashcode-in-java it is recommended only to use getter-methods
	 *      when using ORM like Hibernate
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof WfsSearch))
			return false;
		WfsSearch other = (WfsSearch) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getWfsServerUrl(), other.getWfsServerUrl()).
				append(getMinSearchTextChars(), other.getMinSearchTextChars()).
				append(getTypeDelay(), other.getTypeDelay()).
				append(getAllowedFeatureTypeDataTypes(), other.getAllowedFeatureTypeDataTypes()).
				append(getGroupHeaderTpl(), other.getGroupHeaderTpl()).
				isEquals();
	}

	/**
	 *
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				appendSuper(super.toString()).
				append(getWfsServerUrl()).
				append(getMinSearchTextChars()).
				append(getTypeDelay()).
				append(getAllowedFeatureTypeDataTypes()).
				append(getGroupHeaderTpl()).
				toString();
	}

}
