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
import java.time.LocalDate;


public class ControllerInsertTournament {
    @FXML
    private TextField tournamentName;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField locationField;

    @FXML
    private TextField prize;

    @FXML
    private Button botonInsertar;

    @FXML
    public void handleInsertarTournament(ActionEvent event) throws SQLException {
            TournamentDAO tournamentDAO = TournamentDAO.build();
            String tournamentNameInput = tournamentName.getText().trim();
            LocalDate startDateValue = startDate.getValue();
            LocalDate endDateValue = endDate.getValue();
            String locationInput = locationField.getText().trim();
            String prizeInput = prize.getText().trim();

            boolean isValid = true;
            StringBuilder errorMessage = new StringBuilder("Se encontraron los siguientes errores:\n");

            if (tournamentNameInput.isEmpty() || locationInput.isEmpty() || prizeInput.isEmpty() || startDateValue == null || endDateValue == null) {
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

            if (locationInput.length() > 100) {
                errorMessage.append("- El campo Ubicación no puede tener más de 100 caracteres.\n");
                isValid = false;
            }

            if (prizeInput.length() > 50) {
                errorMessage.append("- El campo Premio no puede tener más de 50 caracteres.\n");
                isValid = false;
            }

            if (startDateValue != null && endDateValue != null) {
                if (startDateValue.isAfter(endDateValue)) {
                    errorMessage.append("- La Fecha de Inicio no puede ser posterior a la Fecha de Fin.\n");
                    isValid = false;
                }

                LocalDate currentDate = LocalDate.now();
                if (startDateValue.isBefore(currentDate)) {
                    errorMessage.append("- La Fecha de Inicio no puede ser anterior a la fecha actual.\n");
                    isValid = false;
                }
            } else {
                errorMessage.append("- Las fechas de inicio y fin son obligatorias.\n");
                isValid = false;
            }

            if (!isValid) {
                Utils.ShowAlert(errorMessage.toString());
                return;
            }

            java.sql.Date startSqlDate = null;
            java.sql.Date endSqlDate = null;

            if (startDateValue != null) {
                startSqlDate = java.sql.Date.valueOf(startDateValue);
            }
            if (endDateValue != null) {
                endSqlDate = java.sql.Date.valueOf(endDateValue);
            }

            User user = obtenerUsuarioActual();
            Tournament tournament = new Tournament(0, tournamentNameInput, startSqlDate, endSqlDate, locationInput, prizeInput, user);
            tournament.setUser(user);

            tournamentDAO.save(tournament);

            tournamentName.setText("");
            startDate.setValue(null);
            endDate.setValue(null);
            locationField.setText("");
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
