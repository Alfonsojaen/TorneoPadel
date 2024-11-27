package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.User;
import github.alfonsojaen.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;


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

        if (teamNameInput.isEmpty() || coachInput.isEmpty() || descriptionInput.isEmpty()) {
            errorMessage.append("- Los campos Nombre del Equipo, Entrenador y Descripción son obligatorios.\n");
            isValid = false;
        }

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

        User user = obtenerUsuarioActual();

        Team team = new Team(0, teamNameInput, coachInput, descriptionInput, null, null,null);
        team.setUser(user);

        teamDAO.save(team);
        teamName.setText("");
        coach.setText("");
        description.setText("");

        Utils.ShowAlert("Equipo insertado exitosamente.");
    }


    private boolean teamExists(String name) throws SQLException {
        Team existingTeam = TeamDAO.build().findByName(name);
        return existingTeam != null && existingTeam.getId() != 0;
    }

    /**
     * Cambia la escena al menú principal de equipos.
     */
    @FXML
    private void switchToMenuTeam() throws IOException {
        Scenes.setRoot("pantallaMenuTeam",null,null);
    }

    /**
     * Obtiene el usuario autenticado actual.
     * @return Instancia de usuario actual.
     */
    private User obtenerUsuarioActual() {
        return new User();
    }
}