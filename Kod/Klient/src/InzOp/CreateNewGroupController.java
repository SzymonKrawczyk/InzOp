package InzOp;

/*
    Kontroler ekranu tworzenia nowej grupy

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    03.05.2020  | Michał Kopałka        |   Stworzenie
                |                       |
    03.05.2020  | Szymon Krawczyk       |   Stworzenie i rozwinięcie właściwej funkcjonalności
                |                       |

 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class CreateNewGroupController {

    @FXML private Label createNewGroupNameLabel;
    public static Label createNewGroupNameLabelStatic;

    @FXML private Button createNewGroupButton;
    public static Button createNewGroupButtonStatic;

    @FXML private TextField groupnameTextField;

    public void initialize() {

        createNewGroupNameLabelStatic = createNewGroupNameLabel;
        createNewGroupButtonStatic = createNewGroupButton;

        createNewGroupNameLabel.setText("");
        createNewGroupNameLabel.setTextFill(Main.ErrorFillColor);

    }

    public void createGroup(ActionEvent event) throws IOException {

        createNewGroupButton.setDisable(true);
        Main.serwerConnector.write("addNewGroupļ" + groupnameTextField.getText().trim());
    }

    public void goBack(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("ManageGroupsScreen.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void checkGroupName (KeyEvent event) {

        String newName = groupnameTextField.getText();

        String errorMessageName = "Nazwy nie można później zmienić";
        boolean errorName = false;


        if (newName.length() < 6) {
            errorName = true;
            errorMessageName ="Nazwa musi mieć przynajmniej 6 znaków";
        } else if (newName.length() > 20) {
            errorName = true;
            errorMessageName = "Nazwa nie może przekraczać 20 znaków";
        }

        if (newName.matches("ļ")) {
            errorName = true;
            errorMessageName = "Nazwa nie może zawierać ļ";
        }


        if (!errorName) {
            createNewGroupButton.setDisable(false);
        } else {
            createNewGroupButton.setDisable(true);
        }
        createNewGroupNameLabel.setText(errorMessageName);
    }
}
