package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author guillermogallegogonzalez
 */
@Entity
@Table(name = "CURSOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cursos.findAll", query = "SELECT c FROM Cursos c"),
    @NamedQuery(name = "Cursos.findByIdCurso", query = "SELECT c FROM Cursos c WHERE c.idCurso = :idCurso"),
    @NamedQuery(name = "Cursos.findByImporteCurso", query = "SELECT c FROM Cursos c WHERE c.importeCurso = :importeCurso"),
    @NamedQuery(name = "Cursos.findByNomCurso", query = "SELECT c FROM Cursos c WHERE c.nomCurso = :nomCurso"),
    @NamedQuery(name = "Cursos.findByCategoria", query = "SELECT c FROM Cursos c WHERE c.categoria = :categoria"),
    @NamedQuery(name = "Cursos.findByDuracion", query = "SELECT c FROM Cursos c WHERE c.duracion = :duracion"),
    @NamedQuery(name = "Cursos.findByProveedor", query = "SELECT c FROM Cursos c WHERE c.proveedor = :proveedor"),
    @NamedQuery(name = "Cursos.findByCertificacion", query = "SELECT c FROM Cursos c WHERE c.certificacion = :certificacion"),
    @NamedQuery(name = "Cursos.findByBeca", query = "SELECT c FROM Cursos c WHERE c.beca = :beca"),
    @NamedQuery(name = "Cursos.findByTipoCurso", query = "SELECT c FROM Cursos c WHERE c.tipoCurso = :tipoCurso"),
    @NamedQuery(name = "Cursos.findByNAsistentes", query = "SELECT c FROM Cursos c WHERE c.nAsistentes = :nAsistentes"),
    @NamedQuery(name = "Cursos.findByFechaIni", query = "SELECT c FROM Cursos c WHERE c.fechaIni = :fechaIni"),
    @NamedQuery(name = "Cursos.findByFechaFin", query = "SELECT c FROM Cursos c WHERE c.fechaFin = :fechaFin"),
    @NamedQuery(name = "Cursos.findByInstructor", query = "SELECT c FROM Cursos c WHERE c.instructor = :instructor")})
public class Cursos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CURSO")
    private Integer idCurso;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "IMPORTE_CURSO")
    private BigDecimal importeCurso;
    @Basic(optional = false)
    @Column(name = "NOM_CURSO")
    private String nomCurso;
    @Basic(optional = false)
    @Column(name = "CATEGORIA")
    private String categoria;
    @Column(name = "DURACION")
    private Integer duracion;
    @Column(name = "PROVEEDOR")
    private String proveedor;
    @Column(name = "CERTIFICACION")
    private String certificacion;
    @Column(name = "BECA")
    private Boolean beca;
    @Basic(optional = false)
    @Column(name = "TIPO_CURSO")
    private String tipoCurso;
    @Column(name = "N_ASISTENTES")
    private Integer nAsistentes;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "INSTRUCTOR")
    private String instructor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso")
    private Collection<Matricula> matriculaCollection;

    public Cursos() {
    }

    public Cursos(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Cursos(Integer idCurso, BigDecimal importeCurso, String nomCurso, String categoria, String tipoCurso) {
        this.idCurso = idCurso;
        this.importeCurso = importeCurso;
        this.nomCurso = nomCurso;
        this.categoria = categoria;
        this.tipoCurso = tipoCurso;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public BigDecimal getImporteCurso() {
        return importeCurso;
    }

    public void setImporteCurso(BigDecimal importeCurso) {
        this.importeCurso = importeCurso;
    }

    public String getNomCurso() {
        return nomCurso;
    }

    public void setNomCurso(String nomCurso) {
        this.nomCurso = nomCurso;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(String certificacion) {
        this.certificacion = certificacion;
    }

    public Boolean getBeca() {
        return beca;
    }

    public void setBeca(Boolean beca) {
        this.beca = beca;
    }

    public String getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public Integer getNAsistentes() {
        return nAsistentes;
    }

    public void setNAsistentes(Integer nAsistentes) {
        this.nAsistentes = nAsistentes;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @XmlTransient
    public Collection<Matricula> getMatriculaCollection() {
        return matriculaCollection;
    }

    public void setMatriculaCollection(Collection<Matricula> matriculaCollection) {
        this.matriculaCollection = matriculaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCurso != null ? idCurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cursos)) {
            return false;
        }
        Cursos other = (Cursos) object;
        if ((this.idCurso == null && other.idCurso != null) || (this.idCurso != null && !this.idCurso.equals(other.idCurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Cursos[ idCurso=" + idCurso + " ]";
    }
    
}
