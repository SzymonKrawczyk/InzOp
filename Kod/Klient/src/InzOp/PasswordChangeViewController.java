/*
    Kontroler ekranu zmiany hasła

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    01.05.2020  | Michał Kopałka        |   Stworzenie (bez funcjonalności)
                |                       |
    02.05.2020  | Szymon Krawczyk       |   Zmiana hasła (wymaganie złożoności, sprawdzenia poprawności obecnego hasła)
                |                       |

 */

package InzOp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PasswordChangeViewController {

    @FXML private Label ErrorCurrentPasswordLabel;
    public static Label ErrorCurrentPasswordLabelStatic;
    @FXML private Label ErrorNewPasswordLabel;

    @FXML private PasswordField currentPasswordField;
    public static PasswordField currentPasswordFieldStatic;
    @FXML private PasswordField newPasswordField;

    @FXML private Button changePasswordButton;
    public static Button changePasswordButtonStatic;

    public void initialize() {

        ErrorCurrentPasswordLabelStatic = ErrorCurrentPasswordLabel;
        currentPasswordFieldStatic = currentPasswordField;
        changePasswordButtonStatic = changePasswordButton;

        ErrorCurrentPasswordLabel.setText("");
        ErrorCurrentPasswordLabel.setTextFill(Main.ErrorFillColor);
        ErrorNewPasswordLabel.setText("");
        ErrorNewPasswordLabel.setTextFill(Main.ErrorFillColor);

        changePasswordButton.setDisable(true);
    }

    public void checkNewPassword (KeyEvent event) {

        String newPassword = newPasswordField.getText();
        String errorMessage = "";
        boolean error = false;



        if (newPassword.contains(" ")) {
            error = true;
            errorMessage = "Nowe hasło nie może zawierać spacji";
        }

        if (!newPassword.matches("(.*[0-9].*)")) {
            error = true;
            errorMessage = "Nowe hasło musi zawierać cyfrę!";
        }
        if (!newPassword.matches("(.*[A-Z].*)")) {
            error = true;
            errorMessage = "Nowe hasło musi zawierać dużą literę!";
        }
        if (!newPassword.matches("(.*[a-z].*)")) {
            error = true;
            errorMessage = "Nowe hasło musi zawierać małą literę!";
        }


        if (newPassword.length() < 8) {
            error = true;
            errorMessage ="Nowe hasło musi mieć przynajmniej 8 znaków";
        } else if (newPassword.length() > 20) {
            error = true;
            errorMessage = "Nowe hasło nie może przekraczać 20 znaków";
        }

        if (newPassword.matches("ļ")) {
            error = true;
            errorMessage = "Nowe hasło nie może zawierać ļ";
        }

        if (error) {
            changePasswordButton.setDisable(true);
            ErrorNewPasswordLabel.setText(errorMessage);
        } else {
            changePasswordButton.setDisable(false);
            ErrorNewPasswordLabel.setText("");
        }
    }

    public void goBack(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("SettingsView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }



    public void saveNewPassword(ActionEvent event) {
        changePasswordButton.setDisable(true);
        Main.serwerConnector.write("changePasswordļ" + currentPasswordField.getText().trim() + "ļ" + newPasswordField.getText().trim());
    }
}

