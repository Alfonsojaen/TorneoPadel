package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.User;
import github.alfonsojaen.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ControllerInsertPlayer {
    @FXML
    private TextField nickname;

    @FXML
    private TextField gender;

    @FXML
    private TextField age;

    @FXML
    private ImageView boton;

    @FXML
    private Button botonInsertar;

    @FXML
    public void handleInsertarPlayer(ActionEvent event) throws SQLException {
        PlayerDAO playerDAO = PlayerDAO.build();
        String nicknameInput = nickname.getText().trim();
        String genderInput = gender.getText().trim();
        String ageInput = age.getText().trim();

        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Se encontraron los siguientes errores:\n");

        if (nicknameInput.isEmpty() || genderInput.isEmpty() || ageInput.isEmpty()) {
            errorMessage.append("- Los campos Nombre , Genero y Edad son obligatorios.\n");
            isValid = false;
        }

        if (!nicknameInput.matches("[a-zA-Z\\s]+")) {
            errorMessage.append("- El campo Nombre solo puede contener letras y espacios.\n");
            isValid = false;
        }

        if (nicknameInput.length() > 25) {
            errorMessage.append("- El campo Nombre no puede tener más de 25 caracteres.\n");
            isValid = false;
        }

        if (playerExists(nicknameInput)) {
            errorMessage.append("- Ya existe un jugador con ese Nombre.\n");
            isValid = false;
        }

        if (!ageInput.matches("\\d+")) {
            errorMessage.append("- El campo Edad debe ser un número válido.\n");
            isValid = false;
        } else {
            int ageValue = Integer.parseInt(ageInput);
            if (ageValue < 0 || ageValue > 150) {
                errorMessage.append("- El campo Edad debe estar entre 0 y 150.\n");
                isValid = false;
            }
        }

        if (!genderInput.matches("(?i)masculino|femenino")) {
            errorMessage.append("- El campo Genero debe ser 'Masculino' o 'Femenino'.\n");
            isValid = false;
        }

        if (nicknameInput.isBlank() || genderInput.isBlank() || ageInput.isBlank()) {
            errorMessage.append("- Los campos Nombre, Genero y Edad no pueden contener solo espacios en blanco.\n");
            isValid = false;
        }

        if (!isValid) {
            Utils.ShowAlert(errorMessage.toString());
            return;
        }

        int ageInt = Integer.parseInt(ageInput);

        User user = obtenerUsuarioActual();
        List<Team> teams = obtenerEquiposSeleccionados();

        Player player = new Player(0, nicknameInput, genderInput, ageInt, user, teams);

        playerDAO.save(player);

        nickname.setText("");
        gender.setText("");
        age.setText("");

        Utils.ShowAlert("Jugador insertado exitosamente.");
    }

    private boolean playerExists(String nickname) throws SQLException {
        Player existingPlayer = PlayerDAO.build().findByNickname(nickname);
        return existingPlayer != null && existingPlayer.getId() != 0;
    }


    @FXML
    private void switchToMenuPlayer() throws IOException {
        Scenes.setRoot("pantallaMenuPlayer",null,null);
    }

    private User obtenerUsuarioActual() {
        return new User();
    }

    private List<Team> obtenerEquiposSeleccionados() {
        return new ArrayList<>();
    }
}