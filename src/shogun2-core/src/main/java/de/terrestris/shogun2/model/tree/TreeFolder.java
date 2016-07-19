/**
 *
 */
package de.terrestris.shogun2.model.tree;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * This class represents a (simple) composite {@link TreeNode}, i.e. a folder
 * having {@link TreeNode}-children.
 *
 * @author Nils Bühner
 * @author Kai Volland
 * @author terrestris GmbH & Co. KG
 *
 */
@Entity
@Table
public class TreeFolder extends TreeNode {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@ManyToMany
	@JoinTable(
		name = "TREEFOLDERS_TREENODES",
		joinColumns = { @JoinColumn(name = "TREEFOLDER_ID") },
		inverseJoinColumns = { @JoinColumn(name = "TREENODE_ID") }
	)
	@OrderColumn(name = "IDX")
	private List<TreeNode> children = new ArrayList<TreeNode>();

	/**
	 * Explicitly adding the default constructor as this is important, e.g. for
	 * Hibernate: http://goo.gl/3Cr1pw
	 */
	public TreeFolder() {
		super();

		// folders are not leafs...
		this.setLeaf(false);

		// folders are usually expandable
		this.setExpandable(true);
	}

	/**
	 *
	 * @param node
	 */
	public void addNode(TreeNode node) {
		this.children.add(node);
	}

	/**
	 *
	 * @param node
	 */
	public void remove(TreeNode node) {
		this.children.remove(node);
	}

	/**
	 * @return the children
	 */
	public List<TreeNode> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 *
	 *      According to
	 *      http://stackoverflow.com/questions/27581/overriding-equals
	 *      -and-hashcode-in-java it is recommended only to use getter-methods
	 *      when using ORM like Hibernate
	 */
	@Override
	public int hashCode() {
		// two randomly chosen prime numbers
		return new HashCodeBuilder(19, 17)
				.appendSuper(super.hashCode())
				.append(getChildren())
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 *
	 *      According to
	 *      http://stackoverflow.com/questions/27581/overriding-equals
	 *      -and-hashcode-in-java it is recommended only to use getter-methods
	 *      when using ORM like Hibernate
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TreeFolder))
			return false;
		TreeFolder other = (TreeFolder) obj;

		return new EqualsBuilder()
				.appendSuper(super.equals(other))
				.append(getChildren(), other.getChildren())
				.isEquals();
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
