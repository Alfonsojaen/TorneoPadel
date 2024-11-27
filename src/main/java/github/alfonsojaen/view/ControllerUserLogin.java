package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.UserDAO;
import github.alfonsojaen.model.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerUserLogin {

    @FXML
    private TextField tGmail;
    @FXML
    private PasswordField tPass;

    /**
     * Handles the user login process.
     * Retrieves the entered Gmail and password, validates them,
     * and logs the user in if the credentials are correct.
     * Displays appropriate alerts for successful or failed login attempts.
     *
     */
    @FXML
    private void login() throws SQLException, IOException {

        String gmail = tGmail.getText().trim();
        String password = tPass.getText().trim();
        password = Utils.encryptSHA256(password);


        if(gmail.equals("") || password.equals("")) {
            Utils.ShowAlert("Falta algun campo por introducir");
        }else {
            UserDAO mDAO = new UserDAO();
            String nameUser;
            if((nameUser=mDAO.checkLogin(gmail, password))!=null) {
                UserSession.login(gmail, nameUser);
                Utils.ShowAlert("Login exitoso, Se ha logeago el Usuario correctamente.");
                switchToUserPage();
            }else {
                UserSession.logout();
                Utils.ShowAlert("No se ha podido logear, Intentelo de nuevo.");
            }

        }

    }

    /**
     * Switches to the user page upon successful login.
     *
     * @throws IOException if an I/O error occurs during scene transition
     */
    @FXML
    private void switchToUserPage() throws IOException {
       Scenes.setRoot("pantallaMenu", null, null);
    }

    /**
     * Switches to the user registration page.
     *
     * @throws IOException if an I/O error occurs during scene transition
     */
    @FXML
    private void switchToRegister() throws IOException {
        Scenes.setRoot("pantallaRegisterUser", null, null);
    }
}


