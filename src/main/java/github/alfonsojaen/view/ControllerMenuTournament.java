package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.TournamentDAO;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.Tournament;
import github.alfonsojaen.model.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ControllerMenuTournament implements Initializable {
    @FXML
    private TableView<Tournament> tableview;

    @FXML
    private TableColumn<Tournament, String> id;
    @FXML
    private TableColumn<Tournament, String> name;

    @FXML
    private TableColumn<Tournament, String> startDate;

    @FXML
    private TableColumn<Tournament, String> endDate;

    @FXML
    private TableColumn<Tournament, String> location;

    @FXML
    private TableColumn<Tournament, String> prize;

    private ObservableList<Tournament> tournaments;


    @FXML
    private void switchToMenu() throws IOException {
        Scenes.setRoot("pantallaMenu",null,null);
    }


    @FXML
    private void switchToInsertTournament() throws IOException {
        Scenes.setRoot("pantallaInsertTournament",null,null);
    }


    @FXML
    private void switchToDeleteTornament() throws IOException {
        Scenes.setRoot("pantallaDeleteTournament",null,null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            if (UserSession.isLogged()) {
                List<Tournament> tournaments = TournamentDAO.build().findAll();
                this.tournaments = FXCollections.observableArrayList(tournaments);

                tableview.setItems(this.tournaments);
                tableview.setEditable(true);

                tableview.setRowFactory(tv -> {
                    TableRow<Tournament> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 3 && (!row.isEmpty())) {
                            Tournament rowTournament = row.getItem();
                            try {
                                Scenes.setRoot("pantallaAssignTeam", null,rowTournament);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    return row;
                });

                TableColumn<Tournament, Void> actionColumn = new TableColumn<>("Equipos");

                actionColumn.setCellFactory(col -> {
                    TableCell<Tournament, Void> cell = new TableCell<Tournament, Void>() {
                        private final Button btn = new Button("EQUIPOS");
                        {
                            btn.setOnAction((ActionEvent event) -> {
                                Tournament rowTournament = getTableRow().getItem();
                                if (rowTournament != null) {
                                    showTeams(rowTournament);
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty) {
                                setGraphic(btn);
                            } else {
                                setGraphic(null);
                            }
                        }
                    };
                    return cell;
                });

                tableview.getColumns().add(actionColumn);

                id.setCellValueFactory(tournament -> new SimpleStringProperty(String.valueOf(tournament.getValue().getId())));
                name.setCellValueFactory(tournament -> new SimpleStringProperty(tournament.getValue().getName()));
                startDate.setCellValueFactory(tournament -> new SimpleStringProperty(String.valueOf(tournament.getValue().getStartDate())));
                endDate.setCellValueFactory(tournament -> new SimpleStringProperty(String.valueOf(tournament.getValue().getEndDate())));
                location.setCellValueFactory(tournament -> new SimpleStringProperty(tournament.getValue().getLocation()));
                prize.setCellValueFactory(tournament -> new SimpleStringProperty(tournament.getValue().getPrize()));

                name.setCellFactory(TextFieldTableCell.forTableColumn());
                name.setOnEditCommit(event -> {
                    String newName = event.getNewValue();
                    String oldName = event.getOldValue();
                    if (newName.equals(oldName)) {
                        return;
                    }
                    if (newName.length() <= 30) {
                        boolean exists = TournamentDAO.build().findAll().stream()
                                .anyMatch(team -> team.getName().equals(newName));
                        if (exists) {
                            Utils.ShowAlert("El Nombre ya está en uso. Elige otro.");
                        } else {
                            Tournament tournament = event.getRowValue();
                            tournament.setName(newName);
                            TournamentDAO.build().update(tournament);
                        }
                    } else {
                        Utils.ShowAlert("Te has pasado del límite de caracteres!");
                    }
                });
            }
        }

    private void showTeams(Tournament tournament) {
        int tournamentId = tournament.getId();
        List<Team> teams = TournamentDAO.build().getTeamsByTournament(tournamentId);

        if (teams.isEmpty()) {
            Utils.ShowAlert("No hay equipos asignados a este torneo.");
        } else {
            String teamNames = teams.stream()
                    .map(Team::getName)
                    .collect(Collectors.joining(", "));
            Utils.ShowAlert("Equipos asignados a " + tournament.getName() + ":  ( " + teamNames +" )");
        }
    }

}

