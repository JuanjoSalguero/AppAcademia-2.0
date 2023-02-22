package appacademia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;

/**
 * @author Alejandro, Guillermo, Juanjo
 */
public class SplashScreenController implements Initializable {

    private EntityManager em;
    StackPane rootMain = new StackPane();
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SplashScreen().start();

    }

    class SplashScreen extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(5000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaPrincipal.fxml"));
                        Pane rootPrincipalView = null;
                        try {
                            rootPrincipalView = fxmlLoader.load();
                        } catch (IOException ex) {
                            Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        rootMain.getChildren().add(rootPrincipalView);

                        // Asocial objeto a la clase SplashScreenController
                        VistaPrincipalController vistaPrincipalController = (VistaPrincipalController) fxmlLoader.getController();
                        vistaPrincipalController.setEntityManager(em);

                        Scene scene = new Scene(rootMain);
                        Stage stage = new Stage();
                        stage.getIcons().add(new Image("/RecursosMenu/IconoInferior.png"));
                        stage.setTitle("App Academia");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.setScene(scene);
                        stage.show();

                        rootPrincipalView.setOnMousePressed(event -> {
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        });
                        rootPrincipalView.setOnMouseDragged(event -> {
                            stage.setX(event.getScreenX() - xOffset);
                            stage.setY(event.getScreenY() - yOffset);
                        });

                        rootPane.getScene().getWindow().hide();

                    }

                });

            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

    public void setEntityManager(EntityManager em) {
        this.em = em;

    }

}
