/**
 *
 */
package de.terrestris.shoguncore.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents a file which is stored as a bytearray in the database
 *
 * @author Johannes Weskamm
 * @author Daniel Koch
 */
public class File extends PersistentObject {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Boolean active;

    /**
     *
     */
    private String fileName;

    /**
     *
     */
    private String fileType;

    /**
     *
     */
    @JsonIgnore
    private byte[] file;

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the file
     */
    public byte[] getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(byte[] file) {
        this.file = file;
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
        return new HashCodeBuilder(523, 7).appendSuper(super.hashCode())
            .append(getActive())
            .append(getFileName())
            .append(getFileType())
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
        if (!(obj instanceof File))
            return false;
        File other = (File) obj;

        return new EqualsBuilder().appendSuper(super.equals(other))
            .append(getActive(), other.getActive())
            .append(getFileName(), other.getFileName())
            .append(getFileType(), other.getFileType())
            .isEquals();
    }
}
