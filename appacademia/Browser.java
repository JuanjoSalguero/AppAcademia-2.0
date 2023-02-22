package appacademia;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import netscape.javascript.JSObject;

/**
 *
 * @author Juanjo
 */

class Browser extends Region {

    private HBox toolBar;

    private static String[] imageFiles = new String[]{
        "/webresources/Home.png",
        "/webresources/Curso.png",
        "/webresources/Matricula.png",
        "/webresources/Alumno.png",
        "/webresources/Informe1.png",
        "/webresources/Informe2.png",
        "/webresources/Informe3.png",
        "/webresources/video.png"
    };

    private static String[] captions = new String[]{
        " Inicio ",
        " Curso ",
        " Matrícula ",
        " Alumno ",
        " Informe Curso ",
        " Informe Matricula ",
        " Informe Alumno ",
        "Video Tutorial"
    };

    private static String[] urls = new String[]{
        Browser.class.getResource("/topics/Home_Topics.xhtml").toExternalForm(),
        Browser.class.getResource("/topics/Curso_Topic.xhtml").toExternalForm(),
        Browser.class.getResource("/topics/Matricula_Topic.xhtml").toExternalForm(),
        Browser.class.getResource("/topics/Alumnos_Topic.xhtml").toExternalForm(),
        Browser.class.getResource("/topics/Curso_Report.xhtml").toExternalForm(),
        Browser.class.getResource("/topics/Matricula_Report_Topic.xhtml").toExternalForm(),
        Browser.class.getResource("/topics/Alumnos_Jasper_Topic.xhtml").toExternalForm(),
        "https://youtu.be/IqoUUIApx4Y"
    };

    final ImageView selectedImage = new ImageView();
    final Hyperlink[] hpls = new Hyperlink[captions.length];
    final Image[] images = new Image[imageFiles.length];

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    final WebView smallView = new WebView();
    private boolean needDocumentationButton = false;

    public Browser() {
        //Para tratar lo tres enlaces
        for (int i = 0; i < captions.length; i++) {
            Hyperlink hpl = hpls[i] = new Hyperlink(captions[i]);
            Image image = images[i]
                    = new Image(getClass().getResourceAsStream(imageFiles[i]));
            hpl.setGraphic(new ImageView(image));
            final String url = urls[i];
            final boolean addButton = (hpl.getText().equals("Help"));

            //proccess event
            hpl.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    needDocumentationButton = addButton;
                    webEngine.load(url);
                }
            });
        }

        // create the toolbar
        toolBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        //toolBar.getStyleClass().add("browser-toolbar");
        toolBar.getChildren().addAll(hpls);
        toolBar.getChildren().add(createSpacer());

        smallView.setPrefSize(120, 80);
        //handle popup windows
        webEngine.setCreatePopupHandler(
                new Callback<PopupFeatures, WebEngine>() {
            @Override
            public WebEngine call(PopupFeatures config) {
                smallView.setFontScale(0.8);
                if (!toolBar.getChildren().contains(smallView)) {
                    toolBar.getChildren().add(smallView);
                }
                return smallView.getEngine();
            }
        }
        );

        // process page loading
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    JSObject win
                            = (JSObject) webEngine.executeScript("window");
                    win.setMember("app", new JavaApp());
                }
            }
        }
        );

        // load the web page
        webEngine.load(Browser.class.getResource("/topics/Home_Topics.xhtml").toExternalForm());

        //add components
        getChildren().add(toolBar);
        getChildren().add(browser);
    }

    // JavaScript interface object
    public class JavaApp {

        public void exit() {
            Platform.exit();
        }
    }

    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return spacer;
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        double tbHeight = toolBar.prefHeight(w);
        layoutInArea(browser, 0, 0, w, h - tbHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(toolBar, 0, h
                - tbHeight, w, tbHeight, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 500;
    }
}
