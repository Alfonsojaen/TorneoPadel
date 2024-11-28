package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMenuPlayer implements Initializable {
    @FXML
    private TableView<Player> tableview;

    @FXML
    private TableColumn<Player, String> id;

    @FXML
    private TableColumn<Player, String> nickname;

    @FXML
    private TableColumn<Player, String> gender;

    @FXML
    private TableColumn<Player, String> age;

    private ObservableList<Player> players;

    @FXML
    private void switchToMenu() throws IOException {
        Scenes.setRoot("pantallaMenu",null,null);
    }

    @FXML
    private void switchToInsertPlayer() throws IOException {
        Scenes.setRoot("pantallaInsertPlayer",null,null);
    }

    @FXML
    private void switchToDeletePlayer() throws IOException {
        Scenes.setRoot("pantallaDeletePlayer",null,null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (UserSession.isLogged()) {
            List<Player> pasos = PlayerDAO.build().findAll();
            this.players = FXCollections.observableArrayList(pasos);

            tableview.setItems(this.players);
            tableview.setEditable(true);

            id.setCellValueFactory(player -> new SimpleStringProperty(String.valueOf(player.getValue().getId())));
            nickname.setCellValueFactory(player -> new SimpleStringProperty(player.getValue().getNickname()));
            gender.setCellValueFactory(player -> new SimpleStringProperty(String.valueOf(player.getValue().getGender())));
            age.setCellValueFactory(player -> new SimpleStringProperty(String.valueOf(player.getValue().getAge())));

            nickname.setCellFactory(TextFieldTableCell.forTableColumn());
            nickname.setOnEditCommit(event -> {
                String newNickname = event.getNewValue();
                String oldNickname = event.getOldValue();

                if (newNickname.equals(oldNickname)) {
                    return;
                }

                if (newNickname.length() <= 25) {
                    boolean exists = PlayerDAO.build().findAll().stream()
                            .anyMatch(player -> player.getNickname().equals(newNickname));
                    if (exists) {
                        Utils.ShowAlert("El Nombre ya está en uso. Elige otro.");
                    } else {
                        Player player = event.getRowValue();
                        player.setNickname(newNickname);
                        PlayerDAO.build().update(player);
                    }
                } else {
                    Utils.ShowAlert("Te has pasado del límite de caracteres!");
                }
            });

            gender.setCellFactory(TextFieldTableCell.forTableColumn());
            gender.setOnEditCommit(event -> {
                String newGender = event.getNewValue();
                String oldGender = event.getOldValue();

                if (newGender.equals(oldGender)) {
                    return;
                }

                if (newGender.equalsIgnoreCase("Masculino") || newGender.equalsIgnoreCase("Femenino")) {
                    Player player = event.getRowValue();
                    player.setGender(newGender);
                    PlayerDAO.build().update(player);
                } else {
                    Utils.ShowAlert("Género inválido. Solo se permite Masculino o Femenino.");
                }
            });

            age.setCellFactory(TextFieldTableCell.forTableColumn());
            age.setOnEditCommit(event -> {
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                if (event.getNewValue().length() <= 3) {
                    Player player = event.getRowValue();
                    player.setAge(Integer.parseInt(event.getNewValue()));
                    PlayerDAO.build().update(player);
                } else {
                    Utils.ShowAlert("Te has pasado del límite de caracteres!");
                }
            });
        }
    }

}