/*
 * This software is Copyright by the Board of Trustees of Michigan
 * State University (c) Copyright 2012.
 * 
 * You may use this software under the terms of the GNU public license
 *  (GPL). The terms of this license are described at:
 *       http://www.gnu.org/licenses/gpl.txt
 * 
 * Contact Information:
 *   Facilitty for Rare Isotope Beam
 *   Michigan State University
 *   East Lansing, MI 48824-1321
 *   http://frib.msu.edu
 * 
 */
package org.openepics.names;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Entity
@Table(name = "name_release")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NameRelease.findAll", query = "SELECT n FROM NameRelease n"),
    @NamedQuery(name = "NameRelease.findById", query = "SELECT n FROM NameRelease n WHERE n.id = :id"),
    @NamedQuery(name = "NameRelease.findByDescription", query = "SELECT n FROM NameRelease n WHERE n.description = :description"),
    @NamedQuery(name = "NameRelease.findByDocUrl", query = "SELECT n FROM NameRelease n WHERE n.docUrl = :docUrl"),
    @NamedQuery(name = "NameRelease.findByReleaseDate", query = "SELECT n FROM NameRelease n WHERE n.releaseDate = :releaseDate"),
    @NamedQuery(name = "NameRelease.findByVersion", query = "SELECT n FROM NameRelease n WHERE n.version = :version")})
public class NameRelease implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "id")
    private String id;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "doc_url")
    private String docUrl;
    @Column(name = "release_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version private int version;

    public NameRelease() {
    }

    public NameRelease(String id) {
        this.id = id;
    }

    public NameRelease(String id, int version) {
        this.id = id;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NameRelease)) {
            return false;
        }
        NameRelease other = (NameRelease) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.openepics.names.NameRelease[ id=" + id + " ]";
    }
    
}
