package appacademia;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import entidades.Alumnos;
import entidades.Cursos;
import entidades.Matricula;
import entidades.Provincia;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import nuevotempoaa.Temporizador;

/**
 * @author Alejandro, Guillermo, Juanjo
 */
public class MatriculaAppAcademiaController implements Initializable {

    private EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("AppAcademiaPU");
    private EntityManager em2 = emf2.createEntityManager();
    private EntityManagerFactory emf3 = Persistence.createEntityManagerFactory("AppAcademiaPU");
    private Alumnos alumno = new Alumnos();
    private boolean nuevoAlumno;
    private Matricula matricula = new Matricula();
    private boolean nuevaMatricula;

    // Variables Interfaz
    @FXML
    private JFXTextField dniTextField;
    @FXML
    private JFXTextField nombreTextField;
    @FXML
    private JFXTextField direccionTextField;
    @FXML
    private JFXTextField telefonoTextField;
    @FXML
    private JFXComboBox<Provincia> provinciaComboBox;
    @FXML
    private JFXRadioButton ordinariaRadioButton;
    @FXML
    private JFXRadioButton repartidorRadioButton;
    @FXML
    private JFXRadioButton familiaNumerosaRadioButton;
    @FXML
    private JFXDatePicker fechaMatriculaDatePicker;
    @FXML
    private JFXTextField importeTextField;
    @FXML
    private JFXComboBox<Cursos> cursoComboBox;
    @FXML
    private JFXCheckBox documentaciónCheckBox;
    @FXML
    private JFXCheckBox certificadoCheckBox;
    @FXML
    private JFXTextField localidadTextField;
    // Variable Imágen
    public static final String CARPETA_FOTOS = "src/appacademia/Fotos";
    @FXML
    private AnchorPane matriculasAnchorPane;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private Label textoLabel;
    public static final String ORDINARIA = "ORDINARIA";
    public static final String REPETIDOR = "REPETIDOR";
    public static final String FAMILIANUMEROSA = "FAMILIA NUMEROSA";
    private boolean imagenIntroducida = false;
    @FXML
    private ToggleGroup tipoMatriculaToogleGroup;
    @FXML
    private JFXTextField busquedaAlumnos;
    @FXML
    private Temporizador temporizadorMatricula;
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private Button btnVerCursos;

    /*-------------------------------------------------INICIALIZAR-----------------------------------------------------------*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Carga de valores en ComboBox
        cargarComboBoxProvincia();
        cargarComboBoxCursos();

        // Por defecto aparecerá la fecha actual seleccionada
        fechaMatriculaDatePicker.setValue(LocalDate.now());

        //Dni Pattern
        Comunes.dniPattern(dniTextField);

        //Restricción longitud teléfono
        telefonoTextField.addEventFilter(KeyEvent.KEY_TYPED, Comunes.maxLength(40));

        //Recalcular Importe
        calcularImporte();

        // DNI Disable
        dniDisable();

        //Listener DNI
        listenerDNI();

        //Configurar temporizador
        configTemporizador();

    }

    /*-------------------------------------------------FUNCIONES-----------------------------------------------------------*/
    public void setEntityManager(EntityManager entityManager) {
        this.em2 = entityManager;
    }

    // Set alumno
    public void setAlumno(EntityManager entityManager, Alumnos alumno, Boolean nuevoAlumno) {
        this.em2 = entityManager;
        entityManager.getTransaction().begin();
        this.nuevoAlumno = nuevoAlumno;
    }

    // Set matricula
    public void setMatricula(EntityManager entityManager, Matricula matricula, Boolean nuevaMatricula) {
        this.em2 = entityManager;
        entityManager.getTransaction().begin();
        this.nuevaMatricula = nuevaMatricula;
    }

    public void dniDisable() {

        // Si se encuentra vacío el textfield se deshabilitan el resto
        if (dniTextField.getText().length() == 0) {
            Image cargaDeVuelta = new Image("/RecursosMenu/perfil.png");
            imageViewFoto.setImage(cargaDeVuelta);
            textoLabel.setText("Matrículas");
            nombreTextField.setDisable(true);
            direccionTextField.setDisable(true);
            telefonoTextField.setDisable(true);
            provinciaComboBox.setDisable(true);
            ordinariaRadioButton.setDisable(true);
            repartidorRadioButton.setDisable(true);
            familiaNumerosaRadioButton.setDisable(true);
            fechaMatriculaDatePicker.setDisable(true);
            importeTextField.setDisable(true);
            cursoComboBox.setDisable(true);
            documentaciónCheckBox.setDisable(true);
            certificadoCheckBox.setDisable(true);
            localidadTextField.setDisable(true);

        } else {

            nombreTextField.setDisable(false);
            direccionTextField.setDisable(false);
            telefonoTextField.setDisable(false);
            provinciaComboBox.setDisable(false);
            ordinariaRadioButton.setDisable(false);
            repartidorRadioButton.setDisable(false);
            familiaNumerosaRadioButton.setDisable(false);
            fechaMatriculaDatePicker.setDisable(false);
            cursoComboBox.setDisable(false);
            documentaciónCheckBox.setDisable(false);
            certificadoCheckBox.setDisable(false);
            localidadTextField.setDisable(false);

        }
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

    // Carga en cursosComboBox datos
    public void cargarComboBoxCursos() {

        Query queryProvinciaFindAll = em2.createNamedQuery("Cursos.findAll");
        List listCursos = queryProvinciaFindAll.getResultList();
        cursoComboBox.setItems(FXCollections.observableList(listCursos));

        cursoComboBox.setCellFactory(
                (ListView<Cursos> l) -> new ListCell<Cursos>() {
            @Override
            protected void updateItem(Cursos cursos, boolean empty) {
                super.updateItem(cursos, empty);
                if (cursos == null || empty) {
                    setText("");
                } else {
                    setText(cursos.getNomCurso());
                }
            }
        });

        cursoComboBox.setConverter(new StringConverter<Cursos>() {
            @Override
            public String toString(Cursos curso) {
                if (curso == null) {
                    return null;
                } else {
                    return curso.getNomCurso();
                }
            }

            @Override
            public Cursos fromString(String userId) {
                return null;
            }
        });

    }

    //Función Limpiar
    private void limpiar() {

        Comunes.limpiarJFXTextfield(dniTextField);
        Comunes.limpiarJFXTextfield(nombreTextField);
        Comunes.limpiarJFXTextfield(direccionTextField);
        Comunes.limpiarJFXTextfield(telefonoTextField);
        Comunes.limpiarJFXTextfield(localidadTextField);
        Comunes.limpiarJFXTextfield(importeTextField);
        Comunes.limpiarJFXComboBox(provinciaComboBox);
        Comunes.limpiarJFXComboBox(cursoComboBox);
        Comunes.limpiarJFXCheckBox(documentaciónCheckBox);
        Comunes.limpiarJFXCheckBox(certificadoCheckBox);
        Comunes.limpiarJFXRadioButton(ordinariaRadioButton);
        Comunes.limpiarJFXRadioButton(repartidorRadioButton);
        Comunes.limpiarJFXRadioButton(familiaNumerosaRadioButton);

    }

    // Botón Limpiar
    @FXML
    private void OnActionButtonLimpiar() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Limpiar");
        alert.setHeaderText(
                "¿Desea limpiar el formulario? Esta acción reseteará los campos y la acción será irreversible");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            limpiar();
        }
    }

    @FXML
    private void CambioImagenPosicionRaton() {
        if (this.imagenIntroducida == false) {
            Image cargaDeVuelta = new Image("/RecursosMenu/seleccionPerfil.png");
            imageViewFoto.setImage(cargaDeVuelta);
        }
    }

    @FXML
    private void CambioImagenPosicionRatonSalida() {
        if (this.imagenIntroducida == false) {
            Image cargaDeVuelta = new Image("/RecursosMenu/perfil.png");
            imageViewFoto.setImage(cargaDeVuelta);
        }
    }

    // Busqueda Nombre Alumno
    @FXML
    public void busquedaAlumnosNombre() {

        Query queryProvinciaCadiz = em2.createNamedQuery("Alumnos.findByNomAlumno");
        queryProvinciaCadiz.setParameter("nomAlumno", busquedaAlumnos.getText());
        List<Alumnos> nomSearch = queryProvinciaCadiz.getResultList();
        for (Alumnos alumnoEncontrado : nomSearch) {
            if (nomSearch != null) {
                cargarDatosAlumno(alumnoEncontrado);
                dniTextField.setText(alumnoEncontrado.getDni());
                dniDisable();
                this.nuevoAlumno = false;
                busquedaAlumnos.setText("");
            } else {
                Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Aceptar");
                alerta.setHeaderText(
                        "No se ha encontrado el alumno " + busquedaAlumnos.getText() + " en la base de datos");
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.get() == ButtonType.OK) {
                    busquedaAlumnos.setText("");
                }

            }

        }

    }

    // Busqueda Dni
    public void busquedaDNI() {

        Query queryProvinciaCadiz = em2.createNamedQuery("Alumnos.findByDni");
        queryProvinciaCadiz.setParameter("dni", dniTextField.getText());
        List<Alumnos> dniSearch = queryProvinciaCadiz.getResultList();
        for (Alumnos alumnoEncontrado : dniSearch) {
            if (dniSearch != null) {

                cargarDatosAlumno(alumnoEncontrado);
                this.nuevoAlumno = false;
            } else {

                this.nuevoAlumno = true;

            }

        }

    }

    // Busqueda Matricula
    public void busquedaMatricula() {
        Query queryProvinciaCadiz = em2.createNamedQuery("Matricula.");
        queryProvinciaCadiz.setParameter("alumno", alumno);
        List<Matricula> dniSearch = queryProvinciaCadiz.getResultList();
        for (Matricula alumnoEncontrado : dniSearch) {
            if (dniSearch != null) {

                cargarDatosMatricula(alumnoEncontrado);
                this.nuevoAlumno = false;
            } else {
                this.nuevoAlumno = true;

            }

        }
    }

    // Carga de datos
    public void cargarDatosAlumno(Alumnos alumno) {
        dniTextField.setText(alumno.getDni());
        nombreTextField.setText(alumno.getNomAlumno());
        direccionTextField.setText(alumno.getDireccion());
        telefonoTextField.setText(alumno.getTelefono());
        localidadTextField.setText(alumno.getLocalidad());
        provinciaComboBox.setValue(alumno.getProvincia());
        if (alumno.getFoto() != null) {
            String imageFileName = alumno.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION,
                        "No se encuentra la imagen en " + file.toURI().toString());
                alert.showAndWait();
            }
        }
        textoLabel.setText(alumno.getNomAlumno());
    }

    // Cargar datos matricula
    public void cargarDatosMatricula(Matricula matricula) {
        cursoComboBox.setValue(matricula.getCurso());
    }

    // Introducir datos Alumno
    private void introducirDatosAlumno() {

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

            if (nuevoAlumno == true) {
                setAlumno(em2, alumno, nuevoAlumno);
                em2.persist(alumno);
                em2.getTransaction().commit();
            } else {
                setAlumno(em2, alumno, nuevoAlumno);
                em2.merge(alumno);
                em2.getTransaction().commit();
            }

        } else {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error Alumnos");
            alerta.setHeaderText("No se han podido guardar los cambios de alumno. "
                    + "Compruebe que los datos cumplen los requisitos");
            Optional<ButtonType> result = alerta.showAndWait();
        }
    }

    private void introducirDatosMatricula() {

        boolean error = false;

        // Curso combo box
        if (cursoComboBox.getValue() != null) {
            matricula.setCurso(cursoComboBox.getValue());
        } else {
            cursoComboBox.setStyle("-jfx-unfocus-color:red");
            error = true;
        }

        // Set Alumno
        matricula.setAlumno(alumno);

        // Fecha check box
        if (fechaMatriculaDatePicker.getValue() != null) {
            LocalDate localDate = fechaMatriculaDatePicker.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            matricula.setFMat(date);
        } else {
            matricula.setFMat(null);
        }

        // Certificado check box
        if (certificadoCheckBox.isSelected()) {
            matricula.setNecesitaCert(true);
        } else {
            matricula.setNecesitaCert(false);
        }

        // Documentación check box
        if (documentaciónCheckBox.isSelected()) {
            matricula.setNecesitaDoc(true);
        } else {
            matricula.setNecesitaDoc(false);
        }

        // Tipo de matricula
        if (ordinariaRadioButton.isSelected()) {
            matricula.setTipoMat(ORDINARIA);
            repartidorRadioButton.setStyle("-jfx-selected-color:#4059a9;");
            familiaNumerosaRadioButton.setStyle("-jfx-selected-color:#4059a9;");
        } else if (repartidorRadioButton.isSelected()) {
            matricula.setTipoMat(REPETIDOR);
            ordinariaRadioButton.setStyle("-jfx-selected-color:#4059a9;");
            familiaNumerosaRadioButton.setStyle("-jfx-selected-color:#4059a9;");
        } else if (familiaNumerosaRadioButton.isSelected()) {
            matricula.setTipoMat(FAMILIANUMEROSA);
            ordinariaRadioButton.setStyle("-jfx-selected-color:#4059a9;");
            repartidorRadioButton.setStyle("-jfx-selected-color:#4059a9;");
        } else {
            ordinariaRadioButton.setStyle("-jfx-unselected-color:red; -jfx-selected-color:#4059a9;");
            repartidorRadioButton.setStyle("-jfx-unselected-color:red; -jfx-selected-color:#4059a9;");
            familiaNumerosaRadioButton.setStyle("-jfx-unselected-color:red; -jfx-selected-color:#4059a9;");
            error = true;
        }

        // TextField
        if (cursoComboBox.getValue() != null) {
            matricula.setImporte(new BigDecimal(importeTextField.getText()));
        } else {
            importeTextField.setStyle("-jfx-unselected-color:red;");
            error = true;
        }

        if (error == false) {
            // Envío de datos
            setMatricula(em2, matricula, nuevaMatricula);
            em2.persist(matricula);
            em2.getTransaction().commit();
            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("Datos Introducidos correctamente");
            alerta.setHeaderText("Se han guardado los cambios de matricula"
                    + "dentro de la base de datos.");
            Optional<ButtonType> result = alerta.showAndWait();
            limpiar();

        } else {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error Matricula");
            alerta.setHeaderText("No se han podido guardar los cambios de matricula. "
                    + "Compruebe que los datos cumplen los requisitos");
            Optional<ButtonType> result = alerta.showAndWait();
        }
    }

    private void calcularImporte() {
        // Acitualización del campo importe con el incremento del 5% de la documentación
        documentaciónCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (documentaciónCheckBox.isSelected()) {
                    BigDecimal importeCurso = BigDecimal.valueOf(Double.parseDouble(importeTextField.getText()));
                    BigDecimal multiplicador = new BigDecimal("1.05");
                    importeCurso = importeCurso.multiply(multiplicador);
                    BigDecimal roundValue = importeCurso.setScale(2, RoundingMode.HALF_UP);
                    importeTextField.setText(roundValue.toString());
                } else {
                    BigDecimal multiplicador = new BigDecimal("0.05");
                    BigDecimal importeCursoRestado = cursoComboBox.getValue().getImporteCurso();
                    importeCursoRestado = importeCursoRestado.multiply(multiplicador);
                    BigDecimal importe = BigDecimal.valueOf(Double.parseDouble(importeTextField.getText()));
                    importe = importe.subtract(importeCursoRestado);
                    BigDecimal roundValue = importe.setScale(2, RoundingMode.HALF_UP);
                    importeTextField.setText(roundValue.toString());
                }
            }
        });

        // Acitualización del campo importe con el decremento del 10% de la familia
        // numerosa
        familiaNumerosaRadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (familiaNumerosaRadioButton.isSelected()) {
                    BigDecimal importeCurso = BigDecimal.valueOf(Double.parseDouble(importeTextField.getText()));
                    BigDecimal multiplicador = new BigDecimal("0.90");
                    importeCurso = importeCurso.multiply(multiplicador);
                    BigDecimal roundValue = importeCurso.setScale(2, RoundingMode.HALF_UP);
                    importeTextField.setText(roundValue.toString());
                } else {
                    BigDecimal multiplicador = new BigDecimal("0.10");
                    BigDecimal importeCursoRestado = cursoComboBox.getValue().getImporteCurso();
                    importeCursoRestado = importeCursoRestado.multiply(multiplicador);
                    BigDecimal importe = BigDecimal.valueOf(Double.parseDouble(importeTextField.getText()));
                    importe = importe.add(importeCursoRestado);
                    BigDecimal roundValue = importe.setScale(2, RoundingMode.HALF_UP);
                    importeTextField.setText(roundValue.toString());
                }
            }
        });

        // Acitualización del campo importe con el decremento del 10% del repetidor
        repartidorRadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (repartidorRadioButton.isSelected()) {
                    BigDecimal importeCurso = BigDecimal.valueOf(Double.parseDouble(importeTextField.getText()));
                    BigDecimal multiplicador = new BigDecimal("1.10");
                    importeCurso = importeCurso.multiply(multiplicador);
                    BigDecimal roundValue = importeCurso.setScale(2, RoundingMode.HALF_UP);
                    importeTextField.setText(roundValue.toString());
                } else {
                    BigDecimal multiplicador = new BigDecimal("0.10");
                    BigDecimal importeCursoRestado = cursoComboBox.getValue().getImporteCurso();
                    importeCursoRestado = importeCursoRestado.multiply(multiplicador);
                    BigDecimal importe = BigDecimal.valueOf(Double.parseDouble(importeTextField.getText()));
                    importe = importe.subtract(importeCursoRestado);
                    BigDecimal roundValue = importe.setScale(2, RoundingMode.HALF_UP);
                    importeTextField.setText(roundValue.toString());
                }
            }
        });

        // Acitualización del campo importe con el valor del curso al seleccionar una de
        // las opciones del combo box curso
        cursoComboBox.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (oldPropertyValue) {
                if (cursoComboBox.isPickOnBounds()) {
                    if (cursoComboBox.getValue().getImporteCurso() != null) {
                        importeTextField.setText(cursoComboBox.getValue().getImporteCurso().toString());
                    }
                }
            }
        });
    }

    private void fechaMáxima() {
        // Asignación de fecha máxima para el campo DatePicker
        DatePicker maxDate = new DatePicker();
        maxDate.setValue(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory;

        dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Deshabilita todas las fechas posteriores a la fecha requerida
                if (item.isAfter(maxDate.getValue())) {
                    setDisable(true);
                }
            }
        };

        // Actualiza el formato del campo DatePicker
        fechaMatriculaDatePicker.setDayCellFactory(dayCellFactory);

    }

    private void listenerDNI() {
        // Busqueda en la base de datos con listener
        dniTextField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                dniDisable();
                busquedaDNI();
            } else {
                dniDisable();
                busquedaDNI();
            }
        });
    }

    /*-------------------------------------------------BOTONES-----------------------------------------------------------*/
    // Botón Aceptar
    @FXML
    private void OnActionButtonAceptar() throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Aceptar");
        alert.setHeaderText("¿Desea indexar/editar el formulario? Esta acción comprobará y validará\n"
                + "los campos y la acción será irreversible una vez aplicada\n");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                introducirDatosAlumno();
                introducirDatosMatricula();
                Alert alerta = new Alert(AlertType.CONFIRMATION);
                alerta.setTitle("Formulario Enviado Correctamente");
                alerta.setHeaderText("La matrícula nueva se ha ingresado correctamente\n" + "en la base de datos\n");
            } catch (Exception e) {
                Alert alerta = new Alert(AlertType.CONFIRMATION);
                alerta.setTitle("Error Envío Formulario");
                alerta.setHeaderText("La matrícula nueva no se ha ingresado correctamente\n"
                        + "en la base de datos, intentelo de nuevo\n");
                e.printStackTrace();
            }
        }
    }

    // Botón Examinar
    @FXML
    private void onActionButtonExaminar(MouseEvent event) {
        File carpetaFotos = new File(CARPETA_FOTOS);
        if (!carpetaFotos.exists()) {
            carpetaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.jpg",
                        "*.png"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
        File file = fileChooser.showOpenDialog(matriculasAnchorPane.getScene().getWindow());
        if (file != null) {
            try {
                Files.copy(file.toPath(), new File(CARPETA_FOTOS
                        + "/" + file.getName()).toPath());
                alumno.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
                this.imagenIntroducida = true;
            } catch (FileAlreadyExistsException ex) {
                Alert alert = new Alert(AlertType.WARNING, "Nombre de archivo duplicado");
                this.imagenIntroducida = false;
                alert.showAndWait();
            } catch (IOException ex) {
                Alert alert = new Alert(AlertType.WARNING, "No se ha podidoguardar la imagen");
                this.imagenIntroducida = false;
                alert.showAndWait();
            }
        }
    }

    // Botón Cancelar
    @FXML
    private void OnActionButtonCancelar(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InicioAppAcademia.fxml"));
        AnchorPane InicioAnchorPaneDerecho = (AnchorPane) fxmlLoader.load();
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cancelar");
            alert.setHeaderText(
                    "¿Desea cancelar el formulario y volver a la pantalla principal? Esta acción reseteará los campos y la acción será irreversible");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                matriculasAnchorPane.getChildren().clear();
                matriculasAnchorPane.getChildren().add(InicioAnchorPaneDerecho);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para abrir la información de relleno 
    @FXML
    public void imageViewInfoOnAction(MouseEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("InfoMatricula.fxml"));
            DialogPane dialogPane = fxmlLoader.load();

            Dialog<ButtonType> dialog = new Dialog();
            dialog.setDialogPane(dialogPane);
            //dialog.initStyle(StageStyle.TRANSPARENT);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.get() == ButtonType.CLOSE) {
                dialog.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(CursoAppAcademiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para configurar el temporizador
    public void configTemporizador() {

        EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Tiempo máximo superado");
            alert.setContentText("El tiempo máximo de espera ha sido superado.");
            alert.initOwner(btnAceptar.getScene().getWindow());
            alert.show();

            // Reseteamos la encuesta volviendo atrás para que se tenga que reacer la inserción de datos
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InicioAppAcademia.fxml"));
                AnchorPane InicioAnchorPaneDerecho = (AnchorPane) fxmlLoader.load();
                matriculasAnchorPane.getChildren().clear();
                matriculasAnchorPane.getChildren().add(InicioAnchorPaneDerecho);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        //Condición 
        temporizadorMatricula.setOnFinished(onFinished);
        //Tiempo temporizador
        temporizadorMatricula.setTime(90);
        //Iniciamos temporizador
        temporizadorMatricula.iniciar();
    }

    // Genera el reporte de esta clase
    @FXML
    public void generarReporte() {
        Comunes.generaInformes("AppAcademiaMatriucla.jasper");
    }
}
