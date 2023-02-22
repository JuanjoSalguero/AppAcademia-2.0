package appacademia;

import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class Main extends Application {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
        Scene scene = new Scene(root);

        stage.getIcons().add(new Image("/RecursosMenu/IconoInferior.png"));
        stage.setTitle("App Academia");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        // Al cerrar la aplicación de "golpe" desde la barra superior, asegura cerrar la
        // conexión con la base de datos
        if (emf != null) {
            em.close();
            emf.close();
        }
        try {
            DriverManager.getConnection("jdbc:hsql://localhost/BDAcademia;shutdown=true");
        } catch (SQLException ex) {
        }
    }
     
    public static void main(String[] args) {
        launch(args);
    }

}