package github.alfonsojaen.view;

import javafx.fxml.FXML;

import java.io.IOException;


public class ControllerMenu {

    @FXML
    private void MenuTorneo() throws IOException {
        Scenes.setRoot("pantallaMenuTournament",null,null);
    }

    @FXML
    private void MenuEquipo() throws IOException {
        Scenes.setRoot("pantallaMenuTeam",null,null);
    }

    @FXML
    private void MenuPlayer() throws IOException {
        Scenes.setRoot("pantallaMenuPlayer",null,null);
    }

    @FXML
    private void MenuLogin() throws IOException {
        Scenes.setRoot("pantallaLoginUser",null,null);
    }

}


