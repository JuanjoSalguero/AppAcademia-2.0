package appacademia;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author Alejandro, Guillermo, Juanjo
 */
public class Comunes {
    
    private static Connection conexion;

    /*-----------------------------------------Patrón alfabético----------------------------------------------------------*/
    public static void patronAlfabetico(JFXTextField textField) {
        //Pattern creation
        Pattern pattern = Pattern.compile("[a-z A-Z \\u00E0-\\u00FC]*");
        //Filtratiton with the previous pattern
        UnaryOperator<TextFormatter.Change> alphabetFilter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                return c;
            } else {
                return null;
            }
        };
        TextFormatter<Integer> alphabeticFormat = new TextFormatter<>(alphabetFilter);
        //Alphabet filter application
        textField.setTextFormatter(alphabeticFormat);
    }

    /*-----------------------------------------------Patrón numérico --------------------------------------------------------*/
    public static void patronNumerico(JFXTextField textField) {
        //Pattern creation
        Pattern pattern = Pattern.compile("^[0-9]*$");
        //Filtration with the previous pattern
        UnaryOperator<TextFormatter.Change> filtroNumerico = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                return c;
            } else {
                return null;
            }
        };

        TextFormatter<Integer> numberFormat = new TextFormatter<>(filtroNumerico);
        //Aplicamos el filtro
        textField.setTextFormatter(numberFormat);
    }

    /*---------------------------------------------Patrón fecha de inicio--------------------------------------------------------------*/
    public static void patronFechaInicio(JFXDatePicker date) {
        date.setValue(LocalDate.now());
        LocalDate minDate = LocalDate.now();

        date.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(minDate));
            }
        });
    }

    /*-------------------------------------------Patrón fecha de fin----------------------------------------------------------------------*/
    public static void patronFechaFinal(JFXDatePicker fechaInicial, JFXDatePicker fechaFinal) {
        fechaFinal.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(fechaInicial.getValue()));
            }
        });
    }

    /*----------------------------------------Patrón fecha de matrícula--------------------------------------------------------------*/
    public static void patronFechaMatricula(JFXDatePicker actualDateDP) {
        actualDateDP.setValue(LocalDate.now());
        actualDateDP.setDayCellFactory((DatePicker picker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isAfter(LocalDate.now())) {
                    this.setDisable(true);
                }
                if (date.isBefore(LocalDate.of(1923, Month.JANUARY, 1))) {
                    this.setDisable(true);
                }
            }
        });
    }

    /*-----------------------------Evento que controla la longitud de los campos-------------------------------------------*/
    public static EventHandler<KeyEvent> maxLength(final int i) {
        return (KeyEvent arg0) -> {
            JFXTextField textfield = (JFXTextField) arg0.getSource();
            if (textfield.getText().length() >= i) {
                arg0.consume();
            }
        };

    }

    /*------------------------------------------------------Patrón DNI---------------------------------------------------*/
    public static void dniPattern(JFXTextField textField) {

        textField.addEventFilter(KeyEvent.KEY_TYPED, maxLength(8));
        textField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue) {
                if (!textField.getText().isEmpty()) {

                    //Pattern creation
                    Pattern pattern = Pattern.compile("^\\d{8}$");
                    char[] alfabeto = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

                    String valorDNI = textField.getText();
                    Matcher matcher = pattern.matcher(valorDNI);

                    if (matcher.matches()) {
                        int dniNumber = Integer.parseInt(valorDNI);
                        //Cálculo letra de DNI
                        dniNumber %= 23;

                        if (valorDNI.length() == 8) {
                            textField.setText(valorDNI + alfabeto[dniNumber]);
                        }
                    } else {
                        textField.clear();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setTitle("¡ERROR!");
                        alert.setContentText("El DNI introducido no contiene 8 digitos rellénelo de nuevo");
                        alert.showAndWait();
                    }

                }
            }
        });
    }

    /*-----------------------------------------Método para limpiar los textField----------------------------------------------------*/
    public static void limpiarJFXTextfield(JFXTextField jfxTextfield) {
        jfxTextfield.clear();
        jfxTextfield.setStyle("-jfx-unfocus-color:4d4d4d;");
    }

    /*-----------------------------------------Método para limpiar datePicker-------------------------------------------------------*/
    public static void limpiarJFXDatePicker(JFXDatePicker jfxDatePicker) {
        jfxDatePicker.setValue(null);
        jfxDatePicker.setStyle("-jfx-unfocus-color:4d4d4d;");
    }

    /*-----------------------------------------Método para limpiar DatePicker inicisl(curso)----------------------------------------*/
    public static void limpiarJFXDatePickerInicial(JFXDatePicker jfxDatePicker) {
        jfxDatePicker.setValue(LocalDate.now());
        jfxDatePicker.setStyle("-jfx-unfocus-color:4d4d4d;");
    }

    /*-----------------------------------------Método para limpiar RadioButton-------------------------------------------------------*/
    public static void limpiarJFXRadioButton(JFXRadioButton jfxRadioButton) {
        jfxRadioButton.setSelected(false);
        jfxRadioButton.setStyle("-jfx-unfocus-color:4d4d4d;");
    }

    /*-----------------------------------------Método para limpiar Spinner-----------------------------------------------------------*/
    public static void limpiarJFXSpinner(Spinner jfxSpinner) {
        jfxSpinner.getValueFactory().setValue(0);
        jfxSpinner.setStyle("-fx-border-color:#4d4d4d;");
    }

    /*-----------------------------------------Método para limpiar ComboBox----------------------------------------------------------*/
    public static void limpiarJFXComboBox(JFXComboBox jfxComboBox) {
        jfxComboBox.valueProperty().set(null);
        jfxComboBox.setStyle("-jfx-unfocus-color:#4d4d4d;");
    }

    /*-----------------------------------------Método para limpiar CheckBox----------------------------------------------------------*/
    public static void limpiarJFXCheckBox(JFXCheckBox jfxCheckBox) {
        jfxCheckBox.setSelected(false);
        jfxCheckBox.setStyle("-jfx-unfocus-color:#4d4d4d;");
    }

    /*-------------------------------------------Métodos de campos vacios-----------------------------------------------------------------*/
    //Método para comprobar Textfield Vacio
    public static void jFXTextFieldVacio(JFXTextField jfxTextField) {

        if (jfxTextField.getText().isEmpty()) {
            jfxTextField.setStyle("-jfx-unfocus-color:#FF0000;");
        } else {
            jfxTextField.setStyle("-jfx-unfocus-color:#4d4d4d;");
        }
    }

    //Método para comprobar DatePicker Vacio
    public static void jFXDatePickerVacio(JFXDatePicker datePicker) {

        if (datePicker.getValue() == null) {
            datePicker.setStyle("-jfx-default-color:#FF0000;");
        } else {
            datePicker.setStyle("-jfx-default-color:#4d4d4d;");
        }
    }

    //Método para comprobar ComboBox Vacio
    public static void jFXComboBoxVacio(JFXComboBox jfxComboBox) {

        if (jfxComboBox.getSelectionModel().getSelectedItem() == null) {
            jfxComboBox.setStyle("-jfx-unfocus-color:#FF0000;");
        } else {
            jfxComboBox.setStyle("-jfx-unfocus-color:#4d4d4d;");
        }
    }

    //Método para comprobar Spinner Vacio
    public static void spinnerVacio(Spinner spinner) {

        if (Integer.parseInt(spinner.getValue().toString()) <= 0) {
            spinner.setStyle("-fx-border-color:#FF0000;");
        } else {
            spinner.setStyle("-jfx-unfocus-color:#4d4d4d;");
        }
    }

    // Método para restringir TextField a solo números con coma
    public static void patronNuméticoYComaYEuro(JFXTextField textField) {

        textField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[0-9.€*]")) {
                    textField.setText(newValue.replaceAll("[^0-9.€]", ""));
                }
            }
        });
    }

    // Método para añadir el sufijo € al importe 
    public static void anadirEuro(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!textField.getText().contains("€") && !textField.getText().isEmpty()) {
                    textField.setText(textField.getText() + "€");
                }
            }
        });
    }

    // Método para generar los informese de JasperReport
    public static void generaInformes(String nombreJasperReport) {

        // Establecemos Conexión con la base de datos
        conexion = conectar();

        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(Comunes.class.getResource("/jaspers/" + nombreJasperReport));
            // Map de parámetros
            String image = Comunes.class.getResource("/jaspers/janus.jpg").toString();
            Map parametros = new HashMap();
            parametros.put("Image", image);
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,
                    parametros, conexion);
            // **  Al establecer false, tras cerrar el jasper creado, no se terminará la ejecución de la aplicación
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    // Método para la conexión con la base de datos
    public static Connection conectar() {
        conexion = null; // Variable de tipo Connection para establecer la conexión

        // Establecemos conexión con la BD 
        String baseDatos = "jdbc:hsqldb:hsql://localhost/BDAcademia";
        String usuario = "SA";
        String clave = "";

        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            conexion = DriverManager.getConnection(baseDatos, usuario, clave);

        } catch (ClassNotFoundException cnfe) {
            System.err.println("Fallo al cargar JDBC");
            System.exit(1);
        } catch (SQLException sqle) {
            System.err.println("No se pudo conectar a BD");
            System.exit(1);
        } catch (java.lang.InstantiationException sqlex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }
        
        return conexion;
    }
}
