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
        Scenes.setRoot("pantallaMenu");
    }

    @FXML
    private void switchToInsertPaso() throws IOException {
        Scenes.setRoot("pantallaInsertPaso");
    }

    @FXML
    private void switchToDeletePaso() throws IOException {
        Scenes.setRoot("pantallaDeletePaso");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Usuario autenticado al inicializar: " + (UserSession.isLogged() ? UserSession.getUser() : "Ninguno"));

        // Verifica si el login se realizó correctamente antes de obtener el usuario
        if (UserSession.isLogged()) {
            System.out.println("Usuario autenticado correctamente: " + UserSession.getUser().getUsername());  // Muestra el nombre del usuario
            // Aquí, asegúrate de obtener correctamente los jugadores asociados
            List<Player> pasos = PlayerDAO.build().findAll();
            this.players = FXCollections.observableArrayList(pasos);

            // Mostrar jugadores obtenidos
            System.out.println("Jugadores obtenidos al inicializar:");
            pasos.forEach(System.out::println);

            tableview.setItems(this.players);
            tableview.setEditable(true);

            id.setCellValueFactory(paso -> new SimpleStringProperty(String.valueOf(paso.getValue().getId())));
            nickname.setCellValueFactory(paso -> new SimpleStringProperty(paso.getValue().getNickname()));
            gender.setCellValueFactory(paso -> new SimpleStringProperty(String.valueOf(paso.getValue().getGender())));
            age.setCellValueFactory(paso -> new SimpleStringProperty(String.valueOf(paso.getValue().getAge())));

            nickname.setCellFactory(TextFieldTableCell.forTableColumn());
            nickname.setOnEditCommit(event -> {
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                if (event.getNewValue().length() <= 25) {
                    Player player = event.getRowValue();
                    player.setNickname(event.getNewValue());
                    PlayerDAO.build().update(player);
                } else {
                    Utils.ShowAlert("Te has pasado del límite de caracteres!");
                }
            });
            gender.setCellFactory(TextFieldTableCell.forTableColumn());
            gender.setOnEditCommit(event -> {
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                if (event.getNewValue().length() <= 3) {
                    Player player = event.getRowValue();
                    player.setGender(event.getNewValue());
                    PlayerDAO.build().update(player);
                } else {
                    Utils.ShowAlert("Te has pasado del límite de caracteres!");
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