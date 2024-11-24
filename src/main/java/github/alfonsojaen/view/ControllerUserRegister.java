package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.UserDAO;
import github.alfonsojaen.model.entity.User;
import github.alfonsojaen.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerUserRegister {
    @FXML
    private TextField tusername;
    @FXML
    private TextField tgmail;
    @FXML
    private TextField tname;
    @FXML
    private PasswordField tpassword;
    @FXML
    private ImageView button;

    @FXML
    public void btRegistrar() {
        addUsuario();
    }


    @FXML
    private void addUsuario() {
        UserDAO userDAO = new UserDAO();
        String username = tusername.getText();
        String gmail = tgmail.getText();
        String name = tname.getText();
        String password = tpassword.getText();
        password = Utils.encryptSHA256(password);

        if (username.isEmpty() || password.isEmpty() || gmail.isEmpty() || name.isEmpty()) {

            Utils.Alert("Error", "Campos vacíos", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
        }else {
            try {
                if (userDAO.findByUserName(username) != null) {
                    Utils.Alert("Error", "USUARIO existente", "El USUARIO ya está en uso.\nPor favor, elija otro.", Alert.AlertType.ERROR);
                } else {

                    User user = new User(username, password, gmail, name);
                    userDAO.save(user);

                    Utils.Alert("Registro de Usuario", "Registro exitoso", "Se ha registrado el Usuario correctamente.", Alert.AlertType.INFORMATION);
                }
            } catch (SQLException e) {
                Utils.Alert("Error", "Error en la consulta", "Ocurrió un error al consultar la existencia del usuario ", Alert.AlertType.ERROR);
                e.printStackTrace();

            }
        }
    }


    @FXML
    private void overButton(){
        button.setOpacity(0.5);
    }


    @FXML
    private void offButton(){
        button.setOpacity(1.0);
    }


    @FXML
    private void switchToLoginUser() {
        try {
            Scenes.setRoot("pantallaLoginUser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

