/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author guillermogallegogonzalez
 */
@Entity
@Table(name = "MATRICULA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m"),
    @NamedQuery(name = "Matricula.findByIdMat", query = "SELECT m FROM Matricula m WHERE m.idMat = :idMat"),
    @NamedQuery(name = "Matricula.findByImporte", query = "SELECT m FROM Matricula m WHERE m.importe = :importe"),
    @NamedQuery(name = "Matricula.findByNecesitaCert", query = "SELECT m FROM Matricula m WHERE m.necesitaCert = :necesitaCert"),
    @NamedQuery(name = "Matricula.findByNecesitaDoc", query = "SELECT m FROM Matricula m WHERE m.necesitaDoc = :necesitaDoc"),
    @NamedQuery(name = "Matricula.findByTipoMat", query = "SELECT m FROM Matricula m WHERE m.tipoMat = :tipoMat"),
    @NamedQuery(name = "Matricula.findByFMat", query = "SELECT m FROM Matricula m WHERE m.fMat = :fMat")})
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_MAT")
    private Integer idMat;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "IMPORTE")
    private BigDecimal importe;
    @Basic(optional = false)
    @Column(name = "NECESITA_CERT")
    private Boolean necesitaCert;
    @Basic(optional = false)
    @Column(name = "NECESITA_DOC")
    private Boolean necesitaDoc;
    @Basic(optional = false)
    @Column(name = "TIPO_MAT")
    private String tipoMat;
    @Basic(optional = false)
    @Column(name = "F_MAT")
    @Temporal(TemporalType.DATE)
    private Date fMat;
    @JoinColumn(name = "ALUMNO", referencedColumnName = "DNI")
    @ManyToOne(optional = false)
    private Alumnos alumno;
    @JoinColumn(name = "CURSO", referencedColumnName = "ID_CURSO")
    @ManyToOne(optional = false)
    private Cursos curso;

    public Matricula() {
    }

    public Matricula(Integer idMat) {
        this.idMat = idMat;
    }

    public Matricula(Integer idMat, Boolean necesitaCert, Boolean necesitaDoc, String tipoMat, Date fMat) {
        this.idMat = idMat;
        this.necesitaCert = necesitaCert;
        this.necesitaDoc = necesitaDoc;
        this.tipoMat = tipoMat;
        this.fMat = fMat;
    }

    public Integer getIdMat() {
        return idMat;
    }

    public void setIdMat(Integer idMat) {
        this.idMat = idMat;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Boolean getNecesitaCert() {
        return necesitaCert;
    }

    public void setNecesitaCert(Boolean necesitaCert) {
        this.necesitaCert = necesitaCert;
    }

    public Boolean getNecesitaDoc() {
        return necesitaDoc;
    }

    public void setNecesitaDoc(Boolean necesitaDoc) {
        this.necesitaDoc = necesitaDoc;
    }

    public String getTipoMat() {
        return tipoMat;
    }

    public void setTipoMat(String tipoMat) {
        this.tipoMat = tipoMat;
    }

    public Date getFMat() {
        return fMat;
    }

    public void setFMat(Date fMat) {
        this.fMat = fMat;
    }

    public Alumnos getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumnos alumno) {
        this.alumno = alumno;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMat != null ? idMat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.idMat == null && other.idMat != null) || (this.idMat != null && !this.idMat.equals(other.idMat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Matricula[ idMat=" + idMat + " ]";
    }
    
}
