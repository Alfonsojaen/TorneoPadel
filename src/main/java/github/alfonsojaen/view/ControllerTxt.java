package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.dao.TournamentDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.Tournament;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ControllerTxt {

    @FXML
    private ComboBox<Tournament> tournamentComboBox;

    private Stage stage;
    private TournamentDAO tournamentDAO; // DAOs para acceder a la base de datos
    private TeamDAO teamDAO; // DAO para obtener los equipos
    private PlayerDAO playerDAO; // DAO para obtener los jugadores

    // Este método se ejecuta cuando la vista es inicializada
    @FXML
    private void initialize() {
        // Crear una instancia de TournamentDAO
        tournamentDAO = new TournamentDAO();

        // Obtener la lista de torneos
        List<Tournament> tournaments = tournamentDAO.findAll();

        // Verifica si obtuviste torneos
        System.out.println("Torneos obtenidos: " + tournaments.size());

        // Llenamos el ComboBox con los torneos obtenidos
        if (tournaments != null && !tournaments.isEmpty()) {
            tournamentComboBox.getItems().clear(); // Limpiamos cualquier valor anterior
            tournamentComboBox.getItems().addAll(tournaments); // Agregamos los torneos al ComboBox
        } else {
            showAlert("No hay torneos", "No se encontraron torneos.");
        }

    }

    // Método que se ejecuta cuando el usuario hace clic en "Exportar datos"
    @FXML
    private void handleExportToFile() {
        Tournament selectedTournament = tournamentComboBox.getValue();

        if (selectedTournament == null) {
            showAlert("Selecciona un torneo", "Por favor, selecciona un torneo para exportar.");
            return;
        }

        // Crear un objeto FileChooser para que el usuario seleccione la ubicación y el nombre del archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
        fileChooser.setInitialFileName("torneo_" + selectedTournament.getName() + ".txt");

        // Mostrar el diálogo para seleccionar el archivo
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            // Si el usuario seleccionó un archivo, crea el archivo y guarda los datos
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                // Escribe el nombre del torneo
                writer.write("Torneo: " + selectedTournament.getName() + "\n\n");

                // Obtener los equipos asociados al torneo utilizando TournamentDAO
                List<Team> teams = teamDAO.findByTournament(selectedTournament); // Usamos el DAO para obtener los equipos

                for (Team team : teams) {
                    writer.write("Equipo: " + team.getName() + "\n");
                    writer.write("Entrenador: " + team.getCoach() + "\n");
                    writer.write("Descripción: " + team.getDescription() + "\n");

                    // Obtener los jugadores del equipo utilizando PlayerDAO
                    List<Player> players = playerDAO.findByTeam(team); // Usamos el DAO para obtener los jugadores
                    writer.write("Jugadores:\n");

                    for (Player player : players) {
                        writer.write(" - " + player.getNickname() + " (" + player.getGender() + ", " + player.getAge() + " años)\n");
                    }
                    writer.write("\n"); // Nueva línea entre equipos
                }

                showAlert("Éxito", "Datos exportados correctamente a: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Error al crear el archivo: " + e.getMessage());
            }
        }
    }

    // Método para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}