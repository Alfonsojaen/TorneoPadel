package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.User;
import github.alfonsojaen.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ControllerInsertTeam {

    @FXML
    private TextField teamName;

    @FXML
    private TextField coach;

    @FXML
    private TextArea description;

    @FXML
    private Button botonInsertar;

    @FXML
    public void handleInsertarTeam(ActionEvent event) throws SQLException {
        TeamDAO teamDAO = TeamDAO.build();
        String teamNameInput = teamName.getText().trim();
        String coachInput = coach.getText().trim();
        String descriptionInput = description.getText().trim();

        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Se encontraron los siguientes errores:\n");

        // Validar campos obligatorios
        if (teamNameInput.isEmpty() || coachInput.isEmpty() || descriptionInput.isEmpty()) {
            errorMessage.append("- Los campos Nombre del Equipo, Entrenador y Descripción son obligatorios.\n");
            isValid = false;
        }

        // Validar nombre del equipo
        if (!teamNameInput.matches("[a-zA-Z\\s]+")) {
            errorMessage.append("- El campo Nombre del Equipo solo puede contener letras y espacios.\n");
            isValid = false;
        }

        if (teamNameInput.length() > 50) {
            errorMessage.append("- El campo Nombre del Equipo no puede tener más de 50 caracteres.\n");
            isValid = false;
        }

        if (teamExists(teamNameInput)) {
            errorMessage.append("- Ya existe un equipo con ese Nombre.\n");
            isValid = false;
        }

        // Validar nombre del entrenador
        if (!coachInput.matches("[a-zA-Z\\s]+")) {
            errorMessage.append("- El campo Entrenador solo puede contener letras y espacios.\n");
            isValid = false;
        }

        if (coachInput.length() > 50) {
            errorMessage.append("- El campo Entrenador no puede tener más de 50 caracteres.\n");
            isValid = false;
        }

        // Validar descripción
        if (descriptionInput.length() > 250) {
            errorMessage.append("- El campo Descripción no puede tener más de 250 caracteres.\n");
            isValid = false;
        }

        // Si hay errores, mostrar alerta
        if (!isValid) {
            Utils.ShowAlert(errorMessage.toString());
            return;
        }

        // Obtener usuario actual
        User user = obtenerUsuarioActual();

        // Crear instancia de Team
        Team team = new Team(0, teamNameInput, coachInput, descriptionInput, null, null);
        team.setUser(user);

        // Guardar equipo en la base de datos
        teamDAO.save(team);

        // Limpiar campos
        teamName.setText("");
        coach.setText("");
        description.setText("");

        Utils.ShowAlert("Equipo insertado exitosamente.");
    }

    /**
     * Verifica si ya existe un equipo con el nombre especificado.
     * @param teamName Nombre del equipo a verificar.
     * @return true si el equipo ya existe, false en caso contrario.
     */
    private boolean teamExists(String teamName) throws SQLException {
        Team existingTeam = TeamDAO.build().findByName(teamName);
        return existingTeam != null && existingTeam.getId() != 0;
    }

    /**
     * Cambia la escena al menú principal de equipos.
     */
    @FXML
    private void switchToMenuTeam() throws IOException {
        Scenes.setRoot("pantallaMenuTeam",null);
    }

    /**
     * Obtiene el usuario autenticado actual.
     * @return Instancia de usuario actual.
     */
    private User obtenerUsuarioActual() {
        return new User();
    }
}