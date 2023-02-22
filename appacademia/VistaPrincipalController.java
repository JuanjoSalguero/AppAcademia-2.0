package appacademia;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Alejandro, Guillermo, Juanjo
 */
public class VistaPrincipalController implements Initializable {

    // Variables
    private EntityManagerFactory emf;
    private EntityManager em;

    @FXML
    private AnchorPane inicioAnchorPane;
    @FXML
    private AnchorPane anchorPanePrincipal;

    // Cambio AnchorPane Inicio
    @FXML
    private void OnActionButtonInicioSeccion() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InicioAppAcademia.fxml"));
        AnchorPane InicioAnchorPaneDerecho = (AnchorPane) fxmlLoader.load();
        try {
            inicioAnchorPane.getChildren().clear();
            inicioAnchorPane.getChildren().add(InicioAnchorPaneDerecho);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cambio AnchorPane About
    @FXML
    private void OnActionButtonAboutSeccion() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutAppAcademia.fxml"));
        AnchorPane aboutAnchorPane = (AnchorPane) fxmlLoader.load();
        try {
            inicioAnchorPane.getChildren().clear();
            inicioAnchorPane.getChildren().add(aboutAnchorPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cambio AnchorPane Matricula
    @FXML
    private void OnActionButtonMatriculaSeccion() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MatriculaAppAcademia.fxml"));
        AnchorPane matriculaAnchorPane;
        try {
            matriculaAnchorPane = (AnchorPane) fxmlLoader.load();
            inicioAnchorPane.getChildren().clear();
            inicioAnchorPane.getChildren().add(matriculaAnchorPane);
        } catch (IOException ex) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR BASE DE DATOS");
            alert.setHeaderText(
                    "La base de datos no se encuentra actualmente en servicio. ¿Desea reintentar la conexión? \n "
                    + "Contacte con el administrador janusteam@ieslosmontecillos.com si el problema persiste");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                OnActionButtonMatriculaSeccion();
            }

        }

        emf = Persistence.createEntityManagerFactory("AppAcademiaPU");
        em = emf.createEntityManager();

        MatriculaAppAcademiaController matriculaViewController = (MatriculaAppAcademiaController) fxmlLoader
                .getController();
        matriculaViewController.setEntityManager(em);
    }

    // Cambio AnchorPane Alumno
    @FXML
    public void OnActionButtonAlumnoSeccion() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AlumnosAppAcademia.fxml"));
        try {
            AnchorPane alumnoAnchorPane = (AnchorPane) fxmlLoader.load();
            inicioAnchorPane.getChildren().clear();
            inicioAnchorPane.getChildren().add(alumnoAnchorPane);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR BASE DE DATOS");
            alert.setHeaderText(
                    "La base de datos no se encuentra actualmente en servicio. ¿Desea reintentar la conexión? \n "
                    + "Contacte con el administrador janusteam@ieslosmontecillos.com si el problema persiste");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                OnActionButtonAlumnoSeccion();
            }
        }

        emf = Persistence.createEntityManagerFactory("AppAcademiaPU");
        em = emf.createEntityManager();

        AlumnosAppAcademiaController alumnosViewController = (AlumnosAppAcademiaController) fxmlLoader.getController();
        alumnosViewController.setEntityManager(em);
        alumnosViewController.cargarTodasPersonas();
        alumnosViewController.cargarPersonas();

    }

    // Cambio AnchorPane Curso
    @FXML
    private void OnActionButtonCursoSeccion() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CursoAppAcademia.fxml"));
        try {
            AnchorPane cursoAnchorPane = (AnchorPane) fxmlLoader.load();
            inicioAnchorPane.getChildren().clear();
            inicioAnchorPane.getChildren().add(cursoAnchorPane);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR BASE DE DATOS");
            alert.setHeaderText(
                    "La base de datos no se encuentra actualmente en servicio. ¿Desea reintentar la conexión? \n "
                    + "Contacte con el administrador janusteam@ieslosmontecillos.com si el problema persiste");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                OnActionButtonCursoSeccion();
            }
        }

    }

    @FXML
    public void OnActionButtonAyuda() throws IOException {

        Stage stage;
        Scene scene;
        Image image;
        
        stage = new Stage();
        image = new Image("/webresources/ayuda_black.png");


        // create the scene
        stage.setTitle( "Ayuda App Academia");
        stage.getIcons().add(image);
        scene = new Scene(new Browser(), 1080, 750, Color.web("#666970"));
        stage.setScene(scene);

        stage.show();
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
