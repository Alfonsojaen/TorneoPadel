package github.alfonsojaen.view;

import java.io.IOException;

import github.alfonsojaen.App;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
