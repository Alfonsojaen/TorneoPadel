package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.dao.TournamentDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.Tournament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ControllerTxt {

    @FXML
    private ComboBox<Tournament> cbTournaments;

    private TournamentDAO tournamentDAO;
    private TeamDAO teamDAO;
    private PlayerDAO playerDAO;

    private ObservableList<Tournament> tournaments;

    public ControllerTxt() {
        this.tournamentDAO = new TournamentDAO();
        this.teamDAO = new TeamDAO();
        this.playerDAO = new PlayerDAO();
    }
    @FXML
    private void switchToMenuTournament() throws IOException {
        Scenes.setRoot("pantallaMenuTournament", null, null);
    }
    @FXML
    public void initialize() {
        loadTournaments();

        cbTournaments.setCellFactory(listView -> new ListCell<>() {
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

        cbTournaments.setButtonCell(new ListCell<Tournament>() {
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

    private void loadTournaments() {
        List<Tournament> tournamentList = tournamentDAO.findAll();
        tournaments = FXCollections.observableArrayList(tournamentList);
        cbTournaments.setItems(tournaments);
    }

    @FXML
    public void generateFile(ActionEvent event) {
        Tournament selectedTournament = cbTournaments.getValue();
        if (selectedTournament == null) {
            System.out.println("Selecciona un torneo.");
            return;
        }

        try {
            List<Team> teams = teamDAO.findByTournament(selectedTournament);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar archivo de torneo");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                writeTournamentToFile(selectedTournament, teams, file);
                System.out.println("Archivo generado exitosamente.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al generar el archivo.");
        }
    }

    private void writeTournamentToFile(Tournament tournament, List<Team> teams, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Torneo: " + tournament.getName() + "\n");
            writer.write("Equipos:\n");

            for (Team team : teams) {
                writer.write("  - " + team.getName() + " (Coach: " + team.getCoach() + ")\n");
                writer.write("    Jugadores:\n");

                List<Player> players = playerDAO.findByTeam(team);

                for (Player player : players) {
                    writer.write("      * " + player.getNickname() + " (Edad: " + player.getAge() + ", Género: " + player.getGender() + ")\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al escribir el archivo.");
        }
    }

}