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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
        Scenes.setRoot("pantallaMenu", null, null);
    }
    @FXML
    private void switchToTXT() throws IOException {
        Scenes.setRoot("pantallatxt", null, null);
    }

    @FXML
    private void switchToInsertTournament() throws IOException {
        Scenes.setRoot("pantallaInsertTournament", null, null);
    }


    @FXML
    private void switchToDeleteTornament() throws IOException {
        Scenes.setRoot("pantallaDeleteTournament", null, null);
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
                            Scenes.setRoot("pantallaAssignTeam", null, rowTournament);
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
                    boolean exists = TournamentDAO.build().findAll().stream().anyMatch(team -> team.getName().equals(newName));
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


            startDate.setCellFactory(new Callback<TableColumn<Tournament, String>, TableCell<Tournament, String>>() {
                @Override
                public TableCell<Tournament, String> call(TableColumn<Tournament, String> param) {
                    return new TableCell<Tournament, String>() {
                        private final DatePicker datePicker = new DatePicker();
                        private String previousValue = "";

                        @Override
                        public void startEdit() {
                            super.startEdit();
                            if (getItem() != null) {
                                previousValue = getItem();
                                datePicker.setValue(LocalDate.parse(getItem()));
                            }
                            setGraphic(datePicker);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            datePicker.setOnAction(event -> commitEdit(datePicker.getValue() != null ? datePicker.getValue().toString() : previousValue));
                        }

                        @Override
                        public void cancelEdit() {
                            super.cancelEdit();
                            setGraphic(null);
                            setContentDisplay(ContentDisplay.TEXT_ONLY);
                            setText(previousValue);
                        }

                        @Override
                        public void commitEdit(String newDate) {
                            super.commitEdit(newDate);
                            if (datePicker.getValue() != null) {
                                try {
                                    LocalDate localStartDate = datePicker.getValue();
                                    java.sql.Date startDateParsed = java.sql.Date.valueOf(localStartDate);

                                    if (startDateParsed.after(new java.sql.Date(System.currentTimeMillis()))) {
                                        Tournament tournament = getTableRow().getItem();
                                        tournament.setStartDate(startDateParsed);
                                        TournamentDAO.build().update(tournament);

                                        setText(newDate);
                                    } else {
                                        Utils.ShowAlert("La Fecha de Inicio no puede ser anterior a la fecha actual.");
                                        setText(previousValue);
                                    }

                                    getTableView().refresh();
                                } catch (IllegalArgumentException e) {
                                    Utils.ShowAlert("Formato de Fecha de Inicio incorrecto.");
                                    setText(previousValue);
                                }
                            } else {
                                setText(previousValue);
                            }

                            setGraphic(null);
                        }

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                setText(item);
                                setGraphic(null);
                            }
                        }
                    };
                }
            });

            endDate.setCellFactory(new Callback<TableColumn<Tournament, String>, TableCell<Tournament, String>>() {
                @Override
                public TableCell<Tournament, String> call(TableColumn<Tournament, String> param) {
                    return new TableCell<Tournament, String>() {
                        private final DatePicker datePicker = new DatePicker();
                        private String previousValue = "";

                        @Override
                        public void startEdit() {
                            super.startEdit();
                            if (getItem() != null) {
                                previousValue = getItem();
                                datePicker.setValue(LocalDate.parse(getItem()));
                            }
                            setGraphic(datePicker);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                            datePicker.setOnAction(event -> commitEdit(datePicker.getValue() != null ? datePicker.getValue().toString() : previousValue));
                        }

                        @Override
                        public void cancelEdit() {
                            super.cancelEdit();
                            setGraphic(null);
                            setContentDisplay(ContentDisplay.TEXT_ONLY);
                            setText(previousValue);
                        }

                        @Override
                        public void commitEdit(String newDate) {
                            super.commitEdit(newDate);
                            if (datePicker.getValue() != null) {
                                try {
                                    LocalDate localEndDate = datePicker.getValue();
                                    java.sql.Date endDateParsed = java.sql.Date.valueOf(localEndDate);

                                    Tournament tournament = getTableRow().getItem();

                                    if (endDateParsed.after(tournament.getStartDate())) {
                                        tournament.setEndDate(endDateParsed);
                                        TournamentDAO.build().update(tournament);

                                        setText(newDate);
                                    } else {
                                        Utils.ShowAlert("La Fecha de Fin no puede ser anterior a la Fecha de Inicio.");
                                        setText(previousValue);
                                    }
                                } catch (IllegalArgumentException e) {
                                    Utils.ShowAlert("Formato de Fecha de Fin incorrecto.");
                                    setText(previousValue);
                                }
                            } else {
                                setText(previousValue);
                            }

                            setGraphic(null);
                        }

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                setText(item);
                                setGraphic(null);
                            }
                        }
                    };
                }
            });



            location.setCellFactory(TextFieldTableCell.forTableColumn());
            location.setOnEditCommit(event -> {
                String newLocation = event.getNewValue();
                if (newLocation.length() <= 100) {
                    Tournament tournament = event.getRowValue();
                    tournament.setLocation(newLocation);
                    TournamentDAO.build().update(tournament);
                } else {
                    Utils.ShowAlert("El campo Ubicación no puede exceder los 100 caracteres.");
                }
            });

            prize.setCellFactory(TextFieldTableCell.forTableColumn());
            prize.setOnEditCommit(event -> {
                String newPrize = event.getNewValue();
                if (newPrize.length() <= 50) {
                    Tournament tournament = event.getRowValue();
                    tournament.setPrize(newPrize);
                    TournamentDAO.build().update(tournament);
                } else {
                    Utils.ShowAlert("El campo Premio no puede exceder los 50 caracteres.");
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
            String teamNames = teams.stream().map(Team::getName).collect(Collectors.joining(", "));
            Utils.ShowAlert("Equipos asignados a " + tournament.getName() + ":  ( " + teamNames + " )");
        }
    }

}

