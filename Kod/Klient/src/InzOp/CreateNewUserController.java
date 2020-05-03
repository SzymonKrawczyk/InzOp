package InzOp;

/*
    Kontroler ekranu tworzenia nowego uzytkownika

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
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class CreateNewUserController {

    @FXML public ListView<ChatEntity> userCreateGroupList;

    @FXML public Label createUserNameErrorLabel;
    public static Label createUserNameErrorLabelStatic;
    @FXML public Label createUserPasswordErrorLabel;

    @FXML public TextField createUserUsernameField;
    @FXML public PasswordField createUserPasswordField;

    @FXML public Button createUserButton;
    public static Button createUserButtonStatic;

    @FXML public CheckBox createUserAdministratorCheckBox;

    public void initialize () {
        createUserButtonStatic = createUserButton;
        createUserNameErrorLabelStatic = createUserNameErrorLabel;


        createUserPasswordErrorLabel.setText("");
        createUserNameErrorLabel.setTextFill(Main.ErrorFillColor);
        createUserPasswordErrorLabel.setTextFill(Main.ErrorFillColor);
    }


    public void goBack(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("ManageUsersView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void createUser(ActionEvent event) throws IOException {

        String priv = "0";
        if (createUserAdministratorCheckBox.isSelected()) priv = "1";

        createUserButton.setDisable(true);
        Main.serwerConnector.write("addNewUserļ" + createUserUsernameField.getText().trim() + "ļ" + createUserPasswordField.getText().trim() + "ļ" + priv);
    }

    public void checkData (KeyEvent event) {

        String newPassword = createUserPasswordField.getText();
        String newName = createUserUsernameField.getText();
        String errorMessageName = "Nazwy użytkownika nie można później zmienić";
        String errorMessagePswd = "";
        boolean errorName = false;
        boolean errorPswd = false;



        if (newPassword.contains(" ")) {
            errorPswd = true;
            errorMessagePswd = "Hasło nie może zawierać spacji";
        }

        if (!newPassword.matches("(.*[0-9].*)")) {
            errorPswd = true;
            errorMessagePswd = "Hasło musi zawierać cyfrę!";
        }
        if (!newPassword.matches("(.*[A-Z].*)")) {
            errorPswd = true;
            errorMessagePswd = "Hasło musi zawierać dużą literę!";
        }
        if (!newPassword.matches("(.*[a-z].*)")) {
            errorPswd = true;
            errorMessagePswd = "Hasło musi zawierać małą literę!";
        }


        if (newPassword.length() < 8) {
            errorPswd = true;
            errorMessagePswd ="Hasło musi mieć przynajmniej 8 znaków";
        } else if (newPassword.length() > 20) {
            errorPswd = true;
            errorMessagePswd = "Hasło nie może przekraczać 20 znaków";
        }

        if (newPassword.matches("ļ")) {
            errorPswd = true;
            errorMessagePswd = "Hasło nie może zawierać ļ";
        }

        if (newName.length() < 5) {
            errorName = true;
            errorMessageName ="Nazwa musi mieć przynajmniej 5 znaków";
        } else if (newName.length() > 20) {
            errorName = true;
            errorMessageName = "Nazwa nie może przekraczać 20 znaków";
        }

        if (newName.matches("ļ")) {
            errorName = true;
            errorMessageName = "Nazwa nie może zawierać ļ";
        }


        if (!errorName && !errorPswd){
            createUserButton.setDisable(false);
            createUserNameErrorLabel.setText(errorMessageName);
            createUserPasswordErrorLabel.setText("");
        } else {
            createUserButton.setDisable(true);
            createUserNameErrorLabel.setText(errorMessageName);
            createUserPasswordErrorLabel.setText(errorMessagePswd);
        }
    }
}
