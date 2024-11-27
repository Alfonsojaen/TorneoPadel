package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.TournamentDAO;
import github.alfonsojaen.model.entity.Tournament;
import github.alfonsojaen.model.entity.User;
import github.alfonsojaen.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;


public class ControllerInsertTournament {

    @FXML
    private TextField tournamentName;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField location;

    @FXML
    private TextField prize;

    @FXML
    private Button botonInsertar;

    @FXML
    public void handleInsertarTournament(ActionEvent event) throws SQLException {
        TournamentDAO tournamentDAO = TournamentDAO.build();
        String tournamentNameInput = tournamentName.getText().trim();
        Date startDateInput = Date.valueOf(startDate.getValue());
        Date endDateInput = Date.valueOf(endDate.getValue());
        String locationInput = location.getText().trim();
        String prizeInput = prize.getText().trim();

        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Se encontraron los siguientes errores:\n");

        if (tournamentNameInput.isEmpty() || locationInput.isEmpty() || prizeInput.isEmpty() || startDateInput == null || endDateInput == null) {
            errorMessage.append("- Los campos Nombre del Torneo, Ubicación, Premio, Fecha de Inicio y Fecha de Fin son obligatorios.\n");
            isValid = false;
        }

        if (!tournamentNameInput.matches("[a-zA-Z0-9\\s]+")) {
            errorMessage.append("- El campo Nombre del Torneo solo puede contener letras, números y espacios.\n");
            isValid = false;
        }

        if (tournamentNameInput.length() > 50) {
            errorMessage.append("- El campo Nombre del Torneo no puede tener más de 50 caracteres.\n");
            isValid = false;
        }

        if (tournamentExists(tournamentNameInput)) {
            errorMessage.append("- Ya existe un torneo con ese Nombre.\n");
            isValid = false;
        }

        // Validar ubicación
        if (locationInput.length() > 100) {
            errorMessage.append("- El campo Ubicación no puede tener más de 100 caracteres.\n");
            isValid = false;
        }

        // Validar premio
        if (prizeInput.length() > 50) {
            errorMessage.append("- El campo Premio no puede tener más de 50 caracteres.\n");
            isValid = false;
        }

        // Validar fechas
        if (startDateInput.after(endDateInput)) {
            errorMessage.append("- La Fecha de Inicio no puede ser posterior a la Fecha de Fin.\n");
            isValid = false;
        }

        // Si hay errores, mostrar alerta
        if (!isValid) {
            Utils.ShowAlert(errorMessage.toString());
            return;
        }

        User user = obtenerUsuarioActual();

        Tournament tournament = new Tournament(0, tournamentNameInput, startDateInput, endDateInput, locationInput, prizeInput, user);

        tournamentDAO.save(tournament);
        tournamentName.setText("");
        startDate.setValue(null);
        endDate.setValue(null);
        location.setText("");
        prize.setText("");

        Utils.ShowAlert("Torneo insertado exitosamente.");
    }

    private boolean tournamentExists(String name) throws SQLException {
        Tournament existingTournament = TournamentDAO.build().findByName(name);
        return existingTournament != null && existingTournament.getId() != 0;
    }

    /**
     * Cambia la escena al menú principal de torneos.
     */
    @FXML
    private void switchToMenuTournament() throws IOException {
        Scenes.setRoot("pantallaMenuTournament", null, null);
    }

    /**
     * Obtiene el usuario autenticado actual.
     * @return Instancia de usuario actual.
     */
    private User obtenerUsuarioActual() {
        return new User();
    }
}
