package appacademia;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import entidades.Cursos;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import nuevotempoaa.Temporizador;

/**
 * @author Alejandro, Guillermo, Juanjo
 */
public class CursoAppAcademiaController implements Initializable {

    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXRadioButton rbOficial;
    @FXML
    private JFXRadioButton rbOnline;
    @FXML
    private JFXRadioButton rbBajoDemanda;
    @FXML
    private JFXTextField tfImporte;
    @FXML
    private JFXCheckBox cbBeca;
    @FXML
    private JFXDatePicker dpFechaIni;
    @FXML
    private JFXDatePicker dpFechaFin;
    @FXML
    private Spinner<Integer> spNumAsistentes;
    @FXML
    private JFXComboBox<String> cbInstructor;
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private JFXButton btnLimpiar;
    @FXML
    private JFXTextField tfNombre;
    @FXML
    private JFXComboBox<String> cbCategoria;
    @FXML
    private JFXTextField tfDuracion;
    @FXML
    private JFXTextField tfCertificacion;
    @FXML
    private JFXTextField tfProveedor;
    @FXML
    private AnchorPane cursosAnchorPane;
    @FXML
    private Temporizador temporizadorCursos;
    @FXML
    private Button btnVerCursos;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAcademiaPU");
    private EntityManager em = emf.createEntityManager();
    private entidades.Cursos curso;

    /*--------------------------------------------------------INICIALIZAR------------------------------------------------------------------*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Método de configuración de los DatePickers
        configurarFecha();
        // Método para crear ToggleGroup
        crearToggleGroup();
        // Método que añade el símbolo € al TextField importe
        Comunes.anadirEuro(tfImporte);
        // Método que añade el sufijo Horas al TextField duración
        anadirHoras(tfDuracion);
        // Cargar ComboBoxes
        cargarComboBoxes();
        // Método para cargar y configurar el spinner
        cargarSpinner();
        // Control y limitacion de los campos
        restriccionesComponentes();
        //Configurar temporizador
        configTemporizador();

    }

    /*--------------------------------------------------------BOTONES------------------------------------------------------------------*/
    @FXML
    private void btnAceptarOnAction(ActionEvent event) {

        curso = new Cursos();
        boolean errorFormat = false;
        camposFiltro();

        if (!tfNombre.getText().isEmpty()) {
            try {
                curso.setNomCurso(tfNombre.getText());
            } catch (NumberFormatException ex) {
                errorFormat = true;
                tfNombre.requestFocus();
            }
        } else {
            errorFormat = true;
        }

        if (cbCategoria.getValue() != null) {
            curso.setCategoria(cbCategoria.getValue());
        } else {
            errorFormat = true;
        }
        if (!tfDuracion.getText().isEmpty()) {
            try {
                curso.setDuracion(Integer.valueOf(tfDuracion.getText().substring(0, tfDuracion.getText().length() - 6)));
            } catch (NumberFormatException ex) {
                errorFormat = true;
                tfDuracion.requestFocus();
            }
        } else {
            errorFormat = true;
        }
        if (!tfCertificacion.getText().isEmpty()) {
            try {
                curso.setCertificacion(tfCertificacion.getText());
            } catch (NumberFormatException ex) {
                errorFormat = true;
                tfCertificacion.requestFocus();
            }
        } else {
            errorFormat = true;
        }

        if (!tfProveedor.getText().isEmpty()) {
            try {
                curso.setProveedor(tfProveedor.getText());
            } catch (NumberFormatException ex) {
                tfProveedor.requestFocus();
            }
        } else {
            errorFormat = true;
        }

        if (dpFechaIni.getValue() != null) {
            LocalDate localDate = dpFechaIni.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            curso.setFechaIni(date);
        } else {
            errorFormat = true;
        }

        if (dpFechaFin.getValue() != null) {
            LocalDate localDate = dpFechaFin.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            curso.setFechaFin(date);
        } else {
            errorFormat = true;
        }

        if (spNumAsistentes.getValue() != null) {
            try {
                curso.setNAsistentes(spNumAsistentes.getValue());
            } catch (NumberFormatException ex) {
                errorFormat = true;
                spNumAsistentes.requestFocus();
            }
        } else {
            errorFormat = true;
        }

        if (cbInstructor.getValue() != null) {
            curso.setInstructor(cbInstructor.getValue());
        } else {
            errorFormat = true;
        }

        curso.setBeca(cbBeca.isSelected());

        if (rbOficial.isSelected()) {
            curso.setTipoCurso("OFICIAL");
        } else if (rbOnline.isSelected()) {
            curso.setTipoCurso("ONLINE");
        } else if (rbBajoDemanda.isSelected()) {
            curso.setTipoCurso("VIDEO BAJO DEMANDA");
        } else {
            errorFormat = true;
        }

        if (!tfImporte.getText().isEmpty()) {
            try {
                curso.setImporteCurso(BigDecimal.valueOf(Double.parseDouble(tfImporte.getText().substring(0, tfImporte.getText().length() - 1))));
            } catch (NumberFormatException ex) {
                errorFormat = true;
                tfImporte.requestFocus();
            }
        } else {
            errorFormat = true;
        }

        if (!errorFormat) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Curso - Confirmación de envio.");
            alert.setContentText("¿Está seguro de que desea enviar los datos de la encuesta? Revise todos los campos antes de proceder con el envio.");
            alert.initOwner(btnAceptar.getScene().getWindow());
            Optional<ButtonType> action = alert.showAndWait();
            // If we accept accept
            if (action.get() == ButtonType.OK) {

                try {
                    em.getTransaction().begin();
                    em.persist(curso);
                    em.getTransaction().commit();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("CONFIRMADO");
                    alert.setContentText("Se han guardado los datos correctamente.");
                    alert.initOwner(btnAceptar.getScene().getWindow());
                    alert.showAndWait();
                    limpiar();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No se han podido guardar los cambios. " + "Compruebe que los datos cumplen los requisitos");
            alert.initOwner(btnAceptar.getScene().getWindow());
            alert.showAndWait();
            camposFiltro();
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
                    "¿Desea cancelar el formulario y volver a la pantalla principal? "
                    + "Esta acción reseteará los campos y la acción será irreversible");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                cursosAnchorPane.getChildren().clear();
                cursosAnchorPane.getChildren().add(InicioAnchorPaneDerecho);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Acción del botón Limpiar
    @FXML
    private void btnLimpiarOnAction(ActionEvent event) {
        limpiar();
    }

    /*--------------------------------------------------------FUNCIONES------------------------------------------------------------------*/
    //  Funcion limpiar
    public void limpiar() {
        Comunes.limpiarJFXTextfield(tfNombre);
        Comunes.limpiarJFXTextfield(tfDuracion);
        Comunes.limpiarJFXTextfield(tfCertificacion);
        Comunes.limpiarJFXTextfield(tfProveedor);
        Comunes.limpiarJFXComboBox(cbCategoria);
        Comunes.limpiarJFXDatePickerInicial(dpFechaIni);
        Comunes.limpiarJFXDatePicker(dpFechaFin);
        Comunes.limpiarJFXComboBox(cbInstructor);
        Comunes.limpiarJFXSpinner(spNumAsistentes);
        Comunes.limpiarJFXTextfield(tfImporte);
        Comunes.limpiarJFXCheckBox(cbBeca);
        Comunes.limpiarJFXRadioButton(rbOficial);
        Comunes.limpiarJFXRadioButton(rbOnline);
        Comunes.limpiarJFXRadioButton(rbBajoDemanda);
    }

    // Método para comprobar si existen errores o está vacio el campo y lo resate de rojo (error)
    private void camposFiltro() {

        Comunes.jFXComboBoxVacio(cbInstructor);
        Comunes.jFXComboBoxVacio(cbCategoria);
        Comunes.jFXDatePickerVacio(dpFechaFin);
        Comunes.jFXTextFieldVacio(tfImporte);
        Comunes.jFXTextFieldVacio(tfCertificacion);
        Comunes.jFXTextFieldVacio(tfNombre);
        Comunes.jFXTextFieldVacio(tfProveedor);
        Comunes.jFXTextFieldVacio(tfDuracion);
        Comunes.spinnerVacio(spNumAsistentes);
    }

    // Método para crear ToggleGroup
    public void crearToggleGroup() {

        ToggleGroup tgTipo = new ToggleGroup();
        rbOficial.setToggleGroup(tgTipo);
        rbOnline.setToggleGroup(tgTipo);
        rbBajoDemanda.setToggleGroup(tgTipo);

        rbOficial.setSelected(true);
    }

    // Método de configuración de las fechas
    public void configurarFecha() {

        Comunes.patronFechaInicio(dpFechaIni);
        Comunes.patronFechaFinal(dpFechaIni, dpFechaFin);
    }

    // Método para cargar los comboboxes
    public void cargarComboBoxes() {

        // Observable List para ComboBox Categoria
        ObservableList<String> listCategoria = observableArrayList(
                "BI/Big Data", "Ciberseguridad", "DevOps", "ERP", "IT", "Int Artificial", "Ofimática", "Programación",
                "Testing");

        // ObservableList para ComboBox Instructor
        ObservableList<String> listInstructor = observableArrayList(
                "Jose Herrero", "Cristian Lozano", "María Ángeles Martinez", "Carmen Cabrera", "Felix Parra",
                "María Jesús Diez", "Isabel Cruz");

        // Añadimos valores para el combobox de categoría e instructor
        cbCategoria.setItems(listCategoria);
        cbInstructor.setItems(listInstructor);
    }

    // Método para cargar el spinner
    public void cargarSpinner() {

        // Valores del spinner NumAsistentes
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0);
        spNumAsistentes.setValueFactory(valueFactory);

        // Listener para que el espiner coja el valor introducido por teclado
        spNumAsistentes.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spNumAsistentes.increment(0); // No va a cambiar el valor pero si lo modificará
            }
        });

        // Listener para que se admita solo números en el spinner
        spNumAsistentes.getEditor().textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    spNumAsistentes.getEditor().setText(oldValue);
                }
            }
        });

        spNumAsistentes.setEditable(true);
    }

    // Método para añadir el sufijo Horas al importe
    public void anadirHoras(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!textField.getText().contains(" Horas") && !textField.getText().isEmpty()) {
                    textField.setText(textField.getText() + " Horas");
                }
            }
        });
    }

    // Método para limitar la longitud de los datos introducidos y restricciones de campos
    public void restriccionesComponentes() {
        // Limitación longitud textfields
        tfNombre.addEventFilter(KeyEvent.KEY_TYPED, Comunes.maxLength(30));
        tfCertificacion.addEventFilter(KeyEvent.KEY_TYPED, Comunes.maxLength(30));
        tfProveedor.addEventFilter(KeyEvent.KEY_TYPED, Comunes.maxLength(50));
        tfImporte.addEventFilter(KeyEvent.KEY_TYPED, Comunes.maxLength(9));
        // Restriccion en los tipos de caracteres insertados
        Comunes.patronNuméticoYComaYEuro(tfImporte);
    }

    // Método para abrir la información de relleno 
    public void imageViewInfoOnAction(MouseEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("InfoCurso.fxml"));
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
                cursosAnchorPane.getChildren().clear();
                cursosAnchorPane.getChildren().add(InicioAnchorPaneDerecho);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        //Condición 
        temporizadorCursos.setOnFinished(onFinished);
        //Tiempo temporizador
        temporizadorCursos.setTime(90);
        //Iniciamos temporizador
        temporizadorCursos.iniciar();
    }

    @FXML
    public void generarReporte() {
        Comunes.generaInformes("ListaCurso.jasper");

    }
}
