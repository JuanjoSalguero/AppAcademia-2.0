package appacademia;

import com.jfoenix.controls.JFXTextField;
import entidades.Alumnos;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * @author Alejandro, Guillermo, Juanjo
 */
public class AlumnosAppAcademiaController implements Initializable {

    private final ObservableList<Alumnos> dataList = FXCollections.observableArrayList();
    private EntityManagerFactory emf;
    private EntityManager em;
    private Alumnos alumnoSeleccionado;
    private List<Alumnos> listaAlumnos;
    @FXML
    private TableView<Alumnos> AlumnosTableView;
    @FXML
    private TableColumn<Alumnos, String> dniColumn;
    @FXML
    private TableColumn<Alumnos, String> nombreColumn;
    @FXML
    private TableColumn<Alumnos, String> direccionColumn;
    @FXML
    private TableColumn<Alumnos, String> localidadColumn;
    @FXML
    private TableColumn<Alumnos, String> provinciaColumn;
    @FXML
    private TableColumn<Alumnos, String> telefonoColumn;
    @FXML
    private AnchorPane alumnosAnchorPane;
    @FXML
    private JFXTextField barraBusqueda;
    @FXML
    private Button btnVerAlumnos;

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    // Función menú contextual AlumnosTableView
    @FXML
    public void contextMenu() {
        // Context Menu

        ContextMenu contextMenu = new ContextMenu();

        // Creación items menu
        MenuItem editar = new MenuItem("Editar");
        MenuItem eliminar = new MenuItem("Eliminar");

        editar.setOnAction((ActionEvent event) -> {
            try {
                editar();
            } catch (IOException ex) {
                Logger.getLogger(AlumnosAppAcademiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        eliminar.setOnAction((ActionEvent event) -> {
            try {
                eliminar();
            } catch (IOException ex) {
                Logger.getLogger(AlumnosAppAcademiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Aañadimos los items al menu
        contextMenu.getItems().add(editar);
        contextMenu.getItems().add(eliminar);

        // Creación Window event
        EventHandler<WindowEvent> event = (WindowEvent e) -> {
            if (contextMenu.isShowing()) {

            } else {

            }
        };

        // Añadimos los eventos
        contextMenu.setOnShowing(event);
        contextMenu.setOnHiding(event);

        // Añadimos el contextmenu al tableview
        AlumnosTableView.setContextMenu(contextMenu);

    }

    // ContextMenu Editar
    public void editar() throws IOException {
        if (AlumnosTableView.getFocusModel().getFocusedItem() != null) {

            // Acciones a realizar si el usuario acepta
            Alumnos consulta = AlumnosTableView.getFocusModel().getFocusedItem();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("EditarAlumno.fxml"));
            DialogPane dialogPane = fxmlLoader.load();

            emf = Persistence.createEntityManagerFactory("AppAcademiaPU");
            em = emf.createEntityManager();

            EditarAlumnoController editarAlumnoViewController = (EditarAlumnoController) fxmlLoader
                    .getController();
            editarAlumnoViewController.setEntityManager(em);
            editarAlumnoViewController.cargarComboBoxProvincia();
            editarAlumnoViewController.cargarDatosAlumno(consulta);

            Dialog<ButtonType> dialog = new Dialog();
            dialog.setDialogPane(dialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.CLOSE) {

                dialog.close();
                VistaPrincipalController vista = new VistaPrincipalController();
                vista.OnActionButtonAlumnoSeccion();

            } else if (clickedButton.get() == ButtonType.APPLY) {

                editarAlumnoViewController.introducirDatosAlumno(consulta);
                dialog.close();
                VistaPrincipalController vista2 = new VistaPrincipalController();
                vista2.OnActionButtonAlumnoSeccion();

            }

            try {
                alumnosAnchorPane.getChildren().clear();
                alumnosAnchorPane.getChildren().add(dialogPane);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void eliminar() throws IOException {
        if (AlumnosTableView.getFocusModel().getFocusedItem() != null) {

            // Acciones a realizar si el usuario acepta
            Alumnos consulta = AlumnosTableView.getFocusModel().getFocusedItem();

            em.getTransaction().begin();
            em.merge(consulta);
            em.remove(consulta);
            em.getTransaction().commit();
            AlumnosTableView.getItems().remove(consulta);
            AlumnosTableView.getFocusModel().focus(null);
            AlumnosTableView.requestFocus();

        }
    }

    @FXML
    public void busquedaAlumnos() {
        // Busqueda alumno
        FilteredList<Alumnos> filteredData = new FilteredList<>(dataList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        barraBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (alumnoSeleccionado.getNomAlumno().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } else if (alumnoSeleccionado.getDni().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (alumnoSeleccionado.getLocalidad().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false; // Does not match.
                }
            });
        });

        SortedList<Alumnos> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(AlumnosTableView.comparatorProperty());
        AlumnosTableView.setItems(sortedData);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        AlumnosTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            alumnoSeleccionado = newValue;
        });

        barraBusqueda.textProperty().addListener((observable, oldValue, newValue)
                -> AlumnosTableView.setItems(listaFiltraAlumnos(listaAlumnos, newValue)));

        // TODO
        dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nomAlumno"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        localidadColumn.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        provinciaColumn.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        provinciaColumn.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getProvincia() != null) {
                        property.setValue(cellData.getValue().getProvincia().getNombreProvincia());
                    }
                    return property;
                });
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    }

    public void cargarTodasPersonas() {

        Query queryMatriculaFindAll = em.createNamedQuery("Alumnos.findAll");
        List<Alumnos> listMatricula = queryMatriculaFindAll.getResultList();
        AlumnosTableView.setItems(FXCollections.observableArrayList(listMatricula));

    }

    /*llamar vistra principal controler*/
    public void cargarPersonas() {
        Query alumnoFindAll = em.createNamedQuery("Alumnos.findAll");
        listaAlumnos = alumnoFindAll.getResultList();
    }

    //Busca las coincidencias con los campos especificados en la base de datos
    private boolean busqueda(Alumnos alumno, String searchText) {
        return (alumno.getDni().toLowerCase().contains(searchText.toLowerCase())
                || (alumno.getNomAlumno().toLowerCase().contains(searchText.toLowerCase()))
                || (alumno.getDireccion().toLowerCase().contains(searchText.toLowerCase()))
                || (alumno.getLocalidad().toLowerCase().contains(searchText.toLowerCase()))
                || (alumno.getProvincia().getNombreProvincia().toLowerCase().contains(searchText.toLowerCase()))
                || (alumno.getTelefono().toLowerCase().contains(searchText.toLowerCase())));
    }

    //Filtro con el texto introducido
    private ObservableList<Alumnos> listaFiltraAlumnos(List<Alumnos> list, String searchText) {
        List<Alumnos> filteredList = new ArrayList<>();
        for (Alumnos alumno : list) {
            if (busqueda(alumno, searchText)) {
                filteredList.add(alumno);
            }
        }
        return FXCollections.observableList(filteredList);
    }

    @FXML
    public void generarReporte() {
        Comunes.generaInformes("Alumnos.jasper");
    }
}
