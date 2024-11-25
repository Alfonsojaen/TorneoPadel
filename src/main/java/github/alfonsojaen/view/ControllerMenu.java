package github.alfonsojaen.view;

import javafx.fxml.FXML;

import java.io.IOException;


public class ControllerMenu {

    @FXML
    private void MenuTorneo() throws IOException {
        Scenes.setRoot("pantallaMenuCuadrilla");
    }

    @FXML
    private void MenuEquipo() throws IOException {
        Scenes.setRoot("pantallaMenuCostalero");
    }

    @FXML
    private void MenuPlayer() throws IOException {
        Scenes.setRoot("pantallaMenuPlayer");
    }

    @FXML
    private void MenuLogin() throws IOException {
        Scenes.setRoot("pantallaLoginUser");
    }

}


