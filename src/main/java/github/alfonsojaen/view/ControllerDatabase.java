package github.alfonsojaen.view;

import github.alfonsojaen.model.connection.DataBaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerDatabase {
    @FXML
    private void useSQLite() {
        try {
            DataBaseManager.getInstance().setDatabase("SQLite");
            showAlert("Base de datos seleccionada", "Usarás SQLite.");
            Scenes.setRoot("pantallaLoginUser", null, null);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo establecer la conexión con SQLite.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void useMariaDB() {
        try {
            DataBaseManager.getInstance().setDatabase("MariaDB");
            showAlert("Base de datos seleccionada", "Usarás MariaDB.");
            Scenes.setRoot("pantallaLoginUser", null, null);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo establecer la conexión con MariaDB.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
