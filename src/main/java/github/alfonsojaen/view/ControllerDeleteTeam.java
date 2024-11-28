package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ControllerDeleteTeam {

    @FXML
    private ComboBox<Team> playerComboBox;

    @FXML
    private Button deleteButton;
    @FXML
    public void initialize() throws SQLException {
        loadTeamList();

        playerComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setDisable(newValue == null);
        });

        playerComboBox.setCellFactory(param -> new ListCell<Team>() {
            @Override
            protected void updateItem(Team item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        playerComboBox.setButtonCell(new ListCell<Team>() {
            @Override
            protected void updateItem(Team item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void loadTeamList() throws SQLException {
        TeamDAO teamDAO = TeamDAO.build();
        List<Team> teams = teamDAO.findAll();

        playerComboBox.getItems().setAll(teams);

        if (!teams.isEmpty()) {
            playerComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void handleDeleteTeam(ActionEvent event) throws SQLException {
        Team selectedTeam = playerComboBox.getSelectionModel().getSelectedItem();

        if (selectedTeam != null) {
            TeamDAO teamDAO = TeamDAO.build();

            teamDAO.delete(selectedTeam);

            Utils.ShowAlert("El Equipo ha sido borrado correctamente.");

            loadTeamList();
        } else {
            Utils.ShowAlert("Por favor, selecciona un equipo para eliminar.");
        }
    }

    @FXML
    private void switchToMenuTeam() throws IOException {
        Scenes.setRoot("pantallaMenuTeam",null,null);
    }
}