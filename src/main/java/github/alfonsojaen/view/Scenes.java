package github.alfonsojaen.view;

import github.alfonsojaen.App;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.Tournament;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class Scenes {


    public static void setRoot(String fxml, Team team, Tournament tournament) throws IOException {
        Parent p = App.loadFXML(fxml);
        Scene newScene;
            //LOGIN
        if (fxml.equals("pantallaLoginUser")) {
            newScene = App.createScene(fxml, 600, 420, null, null);
        } else if (fxml.equals("pantallaRegisterUser")) {
            newScene = App.createScene(fxml, 600, 420, null, null);
            //MENUS
        } else if (fxml.equals("pantallaMenu")) {
            newScene = App.createScene(fxml, 600, 400, null, null);
        } else if (fxml.equals("pantallaMenuPlayer")) {
            newScene = App.createScene(fxml, 800, 400, null, null);
        } else if (fxml.equals("pantallaMenuTeam")) {
            newScene = App.createScene(fxml, 800, 400, null, null);
        } else if (fxml.equals("pantallaMenuTournament")) {
            newScene = App.createScene(fxml, 800, 400, null, null);
            //INSERT
        } else if (fxml.equals("pantallaInsertPlayer")) {
            newScene = App.createScene(fxml, 600, 400, null, null);
        } else if (fxml.equals("pantallaInsertTeam")) {
            newScene = App.createScene(fxml, 600, 400, null, null);
        } else if (fxml.equals("pantallaInsertTournament")) {
            newScene = App.createScene(fxml, 600, 400, null, null);
            //DELETE
        } else if (fxml.equals("pantallaDeleteTeam")) {
            newScene = App.createScene(fxml, 600, 400, null, null);
        } else if (fxml.equals("pantallaDeletePlayer")) {
            newScene = App.createScene(fxml, 600, 400, null, null);
        } else if (fxml.equals("pantallaDeleteTournament")) {
            newScene = App.createScene(fxml, 600, 400, null, null);
            //ASSIGN
        } else if (fxml.equals("pantallaAssignPlayer")) {
            newScene = App.createScene(fxml, 600, 400,team, null);
        } else if (fxml.equals("pantallaAssignTeam")) {
            newScene = App.createScene(fxml, 600, 400, null,tournament);
        } else if (fxml.equals("pantallaDatabase")) {
            newScene = App.createScene(fxml, 600, 400, null, null);
        } else {
            newScene = App.createScene(fxml, 600, 400, null, null);
        }
        App.primaryStage.setScene(newScene);
        App.scene.setRoot(p);


    }
}
