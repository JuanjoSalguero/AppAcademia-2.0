package appacademia;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import entidades.Alumnos;
import entidades.Provincia;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author guillermogallegogonzalez
 */
public class EditarAlumnoController implements Initializable {

    private EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("AppAcademiaPU");
    private EntityManager em2 = emf2.createEntityManager();

    @FXML
    private JFXTextField dniTextField;
    @FXML
    private JFXTextField nombreTextField;
    @FXML
    private JFXTextField direccionTextField;
    @FXML
    private JFXTextField telefonoTextField;
    @FXML
    private JFXTextField localidadTextField;
    @FXML
    private JFXComboBox<Provincia> provinciaComboBox;
    private boolean nuevoAlumno = false;
    
    //Seteo entity manager
    public void setEntityManager(EntityManager entityManager) {
        this.em2 = entityManager;
    }
    
    //Seteo entity manager alumno
    public void setAlumno(EntityManager entityManager, Alumnos alumno, Boolean nuevoAlumno) {
        this.em2 = entityManager;
        entityManager.getTransaction().begin();
        this.nuevoAlumno = nuevoAlumno;
    }
    
    // Carga en provinciaCombobox datos
    public void cargarComboBoxProvincia() {

        Query queryProvinciaFindAll = em2.createNamedQuery("Provincia.findAll");
        List listProvincia = queryProvinciaFindAll.getResultList();
        provinciaComboBox.setItems(FXCollections.observableList(listProvincia));

        provinciaComboBox.setCellFactory(
                (ListView<Provincia> l) -> new ListCell<Provincia>() {
            @Override
            protected void updateItem(Provincia provincia, boolean empty) {
                super.updateItem(provincia, empty);
                if (provincia == null || empty) {
                    setText("");
                } else {
                    setText(provincia.getNombreProvincia());
                }
            }
        });

        provinciaComboBox.setConverter(new StringConverter<Provincia>() {
            @Override
            public String toString(Provincia provincia) {
                if (provincia == null) {
                    return null;
                } else {
                    return provincia.getNombreProvincia();
                }
            }

            @Override
            public Provincia fromString(String userId) {
                return null;
            }
        });

    }
    
    // Carga de datos
    public void cargarDatosAlumno(Alumnos alumno) {
        dniTextField.setText(alumno.getDni());
        nombreTextField.setText(alumno.getNomAlumno());
        direccionTextField.setText(alumno.getDireccion());
        telefonoTextField.setText(alumno.getTelefono());
        localidadTextField.setText(alumno.getLocalidad());
        provinciaComboBox.setValue(alumno.getProvincia());
    }

    // Introducir datos Alumno
    public void introducirDatosAlumno(Alumnos alumno) {

        boolean error = false;
        // DNI
        alumno.setDni(dniTextField.getText());

        // Nombre
        if (nombreTextField.getText().length() <= 30 && nombreTextField.getText().length() > 0) {
            alumno.setNomAlumno(nombreTextField.getText());
        } else {
            nombreTextField.setStyle("-jfx-unfocus-color: red;");
            error = true;
        }
        // Dirección
        if (direccionTextField.getText().length() <= 50 && direccionTextField.getText().length() > 0) {
            alumno.setDireccion(direccionTextField.getText());
        } else {
            direccionTextField.setStyle("-jfx-unfocus-color: red;");
            error = true;
        }
        // Teléfono
        if (telefonoTextField.getText().length() == 9) {
            alumno.setTelefono(telefonoTextField.getText());
        } else {
            telefonoTextField.setStyle("-jfx-unfocus-color: red;");
            error = true;
        }
        // Localidad
        if (localidadTextField.getText().length() <= 30 && localidadTextField.getText().length() > 0) {
            alumno.setLocalidad(localidadTextField.getText());
        } else {
            localidadTextField.setStyle("-jfx-unfocus-color: red;");
            error = true;
        }
        // Provincia
        if (provinciaComboBox.getValue() != null) {
            alumno.setProvincia(provinciaComboBox.getValue());
        } else {
            provinciaComboBox.setStyle("-jfx-unfocus-color:red");
            error = true;
        }

        if (error == false) {

            setAlumno(em2, alumno, nuevoAlumno);
            em2.merge(alumno);
            em2.getTransaction().commit();

        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error Alumnos");
            alerta.setHeaderText("No se han podido guardar los cambios de alumno. "
                    + "Compruebe que los datos cumplen los requisitos");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}