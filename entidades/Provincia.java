/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author guillermogallegogonzalez
 */
@Entity
@Table(name = "PROVINCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provincia.findAll", query = "SELECT p FROM Provincia p"),
    @NamedQuery(name = "Provincia.findByProvinciaId", query = "SELECT p FROM Provincia p WHERE p.provinciaId = :provinciaId"),
    @NamedQuery(name = "Provincia.findByNombreProvincia", query = "SELECT p FROM Provincia p WHERE p.nombreProvincia = :nombreProvincia")})
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROVINCIA_ID")
    private Integer provinciaId;
    @Basic(optional = false)
    @Column(name = "NOMBRE_PROVINCIA")
    private String nombreProvincia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provincia")
    private Collection<Alumnos> alumnosCollection;

    public Provincia() {
    }

    public Provincia(Integer provinciaId) {
        this.provinciaId = provinciaId;
    }

    public Provincia(Integer provinciaId, String nombreProvincia) {
        this.provinciaId = provinciaId;
        this.nombreProvincia = nombreProvincia;
    }

    public Integer getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Integer provinciaId) {
        this.provinciaId = provinciaId;
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }

    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }

    @XmlTransient
    public Collection<Alumnos> getAlumnosCollection() {
        return alumnosCollection;
    }

    public void setAlumnosCollection(Collection<Alumnos> alumnosCollection) {
        this.alumnosCollection = alumnosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (provinciaId != null ? provinciaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.provinciaId == null && other.provinciaId != null) || (this.provinciaId != null && !this.provinciaId.equals(other.provinciaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Provincia[ provinciaId=" + provinciaId + " ]";
    }
    
}
