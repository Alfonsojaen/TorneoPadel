package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMenuTeam implements Initializable {
    @FXML
    private TableView<Team> tableview;

    @FXML
    private TableColumn<Team, String> id;
    @FXML
    private TableColumn<Team, String> name;

    @FXML
    private TableColumn<Team, String> coach;

    @FXML
    private TableColumn<Team, String> description;

    private ObservableList<Team> teams;


    @FXML
    private void switchToMenu() throws IOException {
        Scenes.setRoot("pantallaMenu",null);
    }


    @FXML
    private void switchToInsertTeam() throws IOException {
        Scenes.setRoot("pantallaInsertTeam",null);
    }


    @FXML
    private void switchToDeleteCuadrilla() throws IOException {
        Scenes.setRoot("pantallaDeleteCuadrilla",null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (UserSession.isLogged()) {
        List<Team> teams = TeamDAO.build().findAll();
            System.out.println(teams);
        this.teams = FXCollections.observableArrayList(teams);

        tableview.setItems(this.teams);
        tableview.setEditable(true);

        tableview.setRowFactory(tv -> {
            TableRow<Team> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 3 && (! row.isEmpty()) ) {
                    Team rowTeam = row.getItem();
                    try {
                        Scenes.setRoot("pantallaAssignPlayer",rowTeam);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            });
            return row ;
        });

        id.setCellValueFactory(cuadrilla -> new SimpleStringProperty(String.valueOf(cuadrilla.getValue().getId())));
        name.setCellValueFactory(cuadrilla -> new SimpleStringProperty(cuadrilla.getValue().getName()));
        coach.setCellValueFactory(cuadrilla -> new SimpleStringProperty(cuadrilla.getValue().getCoach()));
        description.setCellValueFactory(cuadrilla -> new SimpleStringProperty(cuadrilla.getValue().getDescription()));


        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(event -> {
            String newName = event.getNewValue();
            String oldName = event.getOldValue();

            if (newName.equals(oldName)) {
                return;
            }
            if (event.getNewValue().length() <= 30) {
                boolean exists = TeamDAO.build().findAll().stream()
                        .anyMatch(team -> team.getName().equals(newName));
                if (exists) {
                    Utils.ShowAlert("El Nombre ya está en uso. Elige otro.");
                } else {
                    Team team = event.getRowValue();
                    team.setName(newName);
                    TeamDAO.build().update(team);
                }
            } else {
                Utils.ShowAlert("Te has pasado del límite de caracteres!");
            }
        });
        coach.setCellFactory(TextFieldTableCell.forTableColumn());
        coach.setOnEditCommit(event -> {
            if (event.getNewValue() == event.getOldValue()) {
                return;
            }
            if (event.getNewValue().length() <= 20) {
                Team cuadrilla = event.getRowValue();
                cuadrilla.setCoach(event.getNewValue());
                TeamDAO.build().update(cuadrilla);
            } else {
                Utils.ShowAlert("Te has pasado del limtite de caracteres!");
            }
        });
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(event -> {
            if (event.getNewValue() == event.getOldValue()) {
                return;
            }
            if (event.getNewValue().length() <= 60) {
                Team cuadrilla = event.getRowValue();
                cuadrilla.setDescription(event.getNewValue());
                TeamDAO.build().update(cuadrilla);
            } else {
                Utils.ShowAlert("Te has pasado del limtite de caracteres!");
            }
        });

        }
    }


}

