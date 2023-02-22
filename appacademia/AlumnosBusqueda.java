package appacademia;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author guillermogallegogonzalez
 */
public class AlumnosBusqueda {
    
    private  final SimpleStringProperty dniBusqueda;
    private  final SimpleStringProperty nombreBusqueda;
    private  final SimpleStringProperty direccionBusqueda;
    private  final SimpleStringProperty localidadBusqueda;
    private  final SimpleStringProperty provinciaBusqueda;
    private  final SimpleStringProperty telefonoBusqueda;
    

    public AlumnosBusqueda(String dni,String nombre,String direccion,String localidad,String provincia,String telefono) {
        
        this.dniBusqueda = new SimpleStringProperty(dni);
        this.nombreBusqueda = new SimpleStringProperty(nombre);
        this.direccionBusqueda = new SimpleStringProperty(direccion);
        this.localidadBusqueda = new SimpleStringProperty(localidad);
        this.provinciaBusqueda = new SimpleStringProperty(provincia);
        this.telefonoBusqueda = new SimpleStringProperty(telefono);
        
    }
    
    public String getDniBusqueda(){
        return dniBusqueda.get();
    }
    
    public void setDniBusqueda(String dni){
        this.dniBusqueda.set(dni);
    }
    
    public String getNombreBusqueda(){
        return nombreBusqueda.get();
    }
    
    public void setNombreBusqueda(String nombre){
        this.nombreBusqueda.set(nombre);
    }
    
    public String getDireccionBusqueda(){
        return direccionBusqueda.get();
    }
    
    public void setDireccionBusqueda(String direccion){
        this.direccionBusqueda.set(direccion);
    }
    
    public String getLocalidadBusqueda(){
        return localidadBusqueda.get();
    }
    
    public void setLocalidadBusqueda(String localidad){
        this.localidadBusqueda.set(localidad);
    }
    
    public String getProvinciaBusqueda(){
        return provinciaBusqueda.get();
    }
    
    public void setProvinciaBusqueda(String provincia){
        this.provinciaBusqueda.set(provincia);
    }
    
    public String getTelefonoBusqueda(){
        return telefonoBusqueda.get();
    }
    
    public void setTelefonoBusqueda(String telefono){
        this.telefonoBusqueda.set(telefono);
    }
    
}