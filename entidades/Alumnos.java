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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ALUMNOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumnos.findAll", query = "SELECT a FROM Alumnos a"),
    @NamedQuery(name = "Alumnos.findByDni", query = "SELECT a FROM Alumnos a WHERE a.dni = :dni"),
    @NamedQuery(name = "Alumnos.findByNomAlumno", query = "SELECT a FROM Alumnos a WHERE a.nomAlumno = :nomAlumno"),
    @NamedQuery(name = "Alumnos.findByFoto", query = "SELECT a FROM Alumnos a WHERE a.foto = :foto"),
    @NamedQuery(name = "Alumnos.findByDireccion", query = "SELECT a FROM Alumnos a WHERE a.direccion = :direccion"),
    @NamedQuery(name = "Alumnos.findByLocalidad", query = "SELECT a FROM Alumnos a WHERE a.localidad = :localidad"),
    @NamedQuery(name = "Alumnos.findByTelefono", query = "SELECT a FROM Alumnos a WHERE a.telefono = :telefono")})
public class Alumnos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DNI")
    private String dni;
    @Basic(optional = false)
    @Column(name = "NOM_ALUMNO")
    private String nomAlumno;
    @Column(name = "FOTO")
    private String foto;
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "LOCALIDAD")
    private String localidad;
    @Column(name = "TELEFONO")
    private String telefono;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alumno")
    private Collection<Matricula> matriculaCollection;
    @JoinColumn(name = "PROVINCIA", referencedColumnName = "PROVINCIA_ID")
    @ManyToOne(optional = false)
    private Provincia provincia;

    public Alumnos() {
    }

    public Alumnos(String dni) {
        this.dni = dni;
    }

    public Alumnos(String dni, String nomAlumno) {
        this.dni = dni;
        this.nomAlumno = nomAlumno;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNomAlumno() {
        return nomAlumno;
    }

    public void setNomAlumno(String nomAlumno) {
        this.nomAlumno = nomAlumno;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @XmlTransient
    public Collection<Matricula> getMatriculaCollection() {
        return matriculaCollection;
    }

    public void setMatriculaCollection(Collection<Matricula> matriculaCollection) {
        this.matriculaCollection = matriculaCollection;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dni != null ? dni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumnos)) {
            return false;
        }
        Alumnos other = (Alumnos) object;
        if ((this.dni == null && other.dni != null) || (this.dni != null && !this.dni.equals(other.dni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Alumnos[ dni=" + dni + " ]";
    }
    
}
