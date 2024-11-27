package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ControllerDeletePlayer {

    @FXML
    private ComboBox<Player> playerComboBox;

    @FXML
    private Button deleteButton;
    @FXML
    public void initialize() throws SQLException {
        loadPlayerList();

        playerComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setDisable(newValue == null);
        });

        playerComboBox.setCellFactory(param -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNickname());
                }
            }
        });

        playerComboBox.setButtonCell(new ListCell<Player>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNickname());
                }
            }
        });
    }

    private void loadPlayerList() throws SQLException {
        PlayerDAO playerDAO = PlayerDAO.build();
        List<Player> players = playerDAO.findAll(); 

        playerComboBox.getItems().setAll(players);

        if (!players.isEmpty()) {
            playerComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void handleDeletePlayer(ActionEvent event) throws SQLException {
        Player selectedPlayer = playerComboBox.getSelectionModel().getSelectedItem();

        if (selectedPlayer != null) {
            PlayerDAO playerDAO = PlayerDAO.build();

            playerDAO.delete(selectedPlayer);

            Utils.ShowAlert("El jugador ha sido borrado correctamente.");

            loadPlayerList();
        } else {
            Utils.ShowAlert("Por favor, selecciona un jugador para eliminar.");
        }
    }

    @FXML
    private void switchToMenu() throws IOException {
        Scenes.setRoot("pantallaMenuPlayer",null,null);
    }
}