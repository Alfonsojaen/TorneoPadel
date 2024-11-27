package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.TournamentDAO;
import github.alfonsojaen.model.entity.Tournament;
import github.alfonsojaen.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ControllerDeleteTournament {

    @FXML
    private ComboBox<Tournament> tournamentComboBox;

    @FXML
    private Button deleteButton;
    @FXML
    public void initialize() throws SQLException {
        loadTournamentList();

        tournamentComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setDisable(newValue == null);
        });

        tournamentComboBox.setCellFactory(param -> new ListCell<Tournament>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        tournamentComboBox.setButtonCell(new ListCell<Tournament>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void loadTournamentList() throws SQLException {
        TournamentDAO tournamentDAO = TournamentDAO.build();
        List<Tournament> tournaments = tournamentDAO.findAll();

        tournamentComboBox.getItems().setAll(tournaments);

        if (!tournaments.isEmpty()) {
            tournamentComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void handleDeleteTournament(ActionEvent event) throws SQLException {
        Tournament selectedTournament = tournamentComboBox.getSelectionModel().getSelectedItem();

        if (selectedTournament != null) {
            TournamentDAO tournamentDAO = TournamentDAO.build();

            tournamentDAO.delete(selectedTournament);

            Utils.ShowAlert("El Equipo ha sido borrado correctamente.");

            loadTournamentList();
        } else {
            Utils.ShowAlert("Por favor, selecciona un equipo para eliminar.");
        }
    }

    @FXML
    private void switchToMenu() throws IOException {
        Scenes.setRoot("pantallaMenuTournament",null,null);
    }
}