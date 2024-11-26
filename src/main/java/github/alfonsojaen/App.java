package github.alfonsojaen;

import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.view.ControllerAssignPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static Stage primaryStage;


    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        scene = new Scene(loadFXML("pantallaLoginUser"), 640, 460);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static Scene createScene(String fxml, double width, double height, Team team) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        if(fxml.equals("pantallaAssignPlayer")){
            ControllerAssignPlayer controller=fxmlLoader.getController();
            if (controller.getClass().equals(ControllerAssignPlayer.class)) {
                System.out.println(controller.getClass());
            }
            controller.setTeam(team);
        }
        Scene scene = new Scene(root, width, height);
        return scene;
    }


    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch();
    }


}