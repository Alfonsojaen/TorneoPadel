package github.alfonsojaen.view;

import github.alfonsojaen.App;
import github.alfonsojaen.model.entity.Team;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class Scenes {


    public static void setRoot(String fxml, Team team) throws IOException {
        Parent p = App.loadFXML(fxml);
        Scene newScene;
            //LOGIN
        if (fxml.equals("pantallaLoginUser")) {
            newScene = App.createScene(fxml, 640, 460, null);
        } else if (fxml.equals("pantallaRegisterUser")) {
            newScene = App.createScene(fxml, 640, 480, null);
            //MENUS
        } else if (fxml.equals("pantallaMenu")) {
            newScene = App.createScene(fxml, 640, 480, null);
        } else if (fxml.equals("pantallaMenuPlayer")) {
            newScene = App.createScene(fxml, 640, 480, null);
        } else if (fxml.equals("pantallaMenuTeam")) {
            newScene = App.createScene(fxml, 640, 480, null);
        } else if (fxml.equals("pantallaMenuTournoment")) {
            newScene = App.createScene(fxml, 640, 480, null);
            //INSERT
        } else if (fxml.equals("pantallaInsertPlayer")) {
            newScene = App.createScene(fxml, 640, 480, null);
        } else if (fxml.equals("pantallaInsertTeam")) {
            newScene = App.createScene(fxml, 640, 480, null);
        } else if (fxml.equals("pantallaInsertTournoment")) {
            newScene = App.createScene(fxml, 640, 480, null);
            //DELETE
        } else if (fxml.equals("pantallaDeleteTeam")) {
            newScene = App.createScene(fxml, 640, 480, null);
        } else if (fxml.equals("pantallaDeletePlayer")) {
            newScene = App.createScene(fxml, 640, 480, null);
        } else if (fxml.equals("pantallaDeleteTournoment")) {
            newScene = App.createScene(fxml, 640, 480, null);
            //ASSIGN
        } else if (fxml.equals("pantallaAssignPlayer")) {
            newScene = App.createScene(fxml, 640, 480,team);
        } else if (fxml.equals("pantallaAssignTeam")) {
            newScene = App.createScene(fxml, 640, 480, null);
        } else {
            newScene = App.createScene(fxml, 640, 480, null);
        }
        App.primaryStage.setScene(newScene);
        App.scene.setRoot(p);


    }
}
