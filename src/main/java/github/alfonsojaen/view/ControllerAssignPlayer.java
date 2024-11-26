package github.alfonsojaen.view;


import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.utils.Utils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ControllerAssignPlayer {
    private Team team;
    private List<Player> selected = new ArrayList<>();
    private ObservableList<Player> players;
    private List<Player> Selected = new ArrayList<>(selected);

    @FXML
    private TableView<Player> tableview;

    @FXML
    private TableColumn<Player, String> idColumn;

    @FXML
    private TableColumn<Player, String> nicknameColumn;

    @FXML
    private TableColumn<Player, String> genderColumn;

    @FXML
    private  TableColumn<Player, String> ageColumn;

    @FXML
    private TableColumn<Player, Boolean> selectionColumn;


    @FXML
    private void switchToMenuCuadrilla() throws IOException {
        Scenes.setRoot("pantallaMenuTeam",null);
    }


    public void start () {
        idColumn.setCellValueFactory(player -> new SimpleStringProperty(String.valueOf(player.getValue().getId())));

        nicknameColumn.setCellValueFactory(player -> new SimpleStringProperty(String.valueOf(player.getValue().getNickname())));

        genderColumn.setCellValueFactory(player -> new SimpleStringProperty(String.valueOf(player.getValue().getGender())));

        ageColumn.setCellValueFactory(player -> new SimpleStringProperty(String.valueOf(player.getValue().getAge())));

        selectionColumn.setCellFactory(CheckBoxTableCell.forTableColumn(
                (Integer i) ->{
                    SimpleBooleanProperty selectedProperty= new SimpleBooleanProperty(players.get(i).isSelected());
                    selectedProperty.addListener((obs, oldValue, newValue) -> {
                        Player player = players.get(i);
                        player.setSelected(newValue);
                        if(newValue){
                            this.team.addPlayer(player);
                        }else{
                            this.team.removePlayer(player);
                        }
                    });
                    return selectedProperty;
                } ));


        selectionColumn.setEditable(true);
        this.players = FXCollections.observableArrayList(PlayerDAO.build().findAll());

        PlayerDAO pdao = new PlayerDAO();
        this.selected=pdao.findByTeam(team);
        for(Player player : this.players){
            if(this.selected.contains(player)){
                player.setSelected(true);
            }else{
                player.setSelected(false);
            }
        }

        tableview.setItems(this.players);
    }


    public void setTeam(Team team) {
        this.team = team;
        start();
    }

    public void assing() {
        try {
            List<Player> pasosSelected = new ArrayList<>();
            for (Player player : players) {
                if (player.isSelected()) {
                    pasosSelected.add(player);
                }
            }

            for (Player player : Selected) {
                if (!pasosSelected.contains(player)) {
                    team.removePlayer(player);
                }
            }

            for (Player player : pasosSelected) {
                if (!Selected.contains(player)) {
                    team.addPlayer(player);
                }
            }

            TeamDAO.build().setPlayer(this.team);
            Utils.ShowAlert("Se ha asignado con éxito");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
