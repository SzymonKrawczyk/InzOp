/*
    Kontroler dla ekranu logowania

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie
                |                       |

 */

package InzOp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import static InzOp.Main.serwerConnector;

public class ConnectAndLoginViewController {

    @FXML public TextField LoginField;
    public static TextField LoginFieldStatic;
    @FXML public PasswordField PasswordField;
    @FXML public TextField SerwerAddressField;
    @FXML public Label myErrorLabel;
    @FXML public Label ErrorLoginLabel;
    public static Label ErrorLabelLoginStatic;
    @FXML public Button ConnectToSerwerButton;
    @FXML public Button LoginButton;
    public static Button LoginButtonStatic;

    public void initialize () {

        myErrorLabel.setText("");
        ErrorLoginLabel.setText("");
        ErrorLabelLoginStatic = ErrorLoginLabel;
        LoginButtonStatic = LoginButton;
        LoginFieldStatic = LoginField;
        //ładowanie adresu serwera z pliku

    }

    public void onConnectToSerwerButtonClick(ActionEvent event) {
        try {

            if (SerwerAddressField.getText().trim().equals("")) {
                throw new Exception();
            }

                //Próba łączenia
            System.out.println(SerwerAddressField.getText().trim());
            serwerConnector.connect(SerwerAddressField.getText().trim());
            //serwerConnector.connect("localhost");

            myErrorLabel.setTextFill(Main.PassFillColor);
            myErrorLabel.setText("Połączono");
            ConnectToSerwerButton.setDisable(true);
            SerwerAddressField.setEditable(false);
            LoginButton.setDisable(false);
            LoginField.setDisable(false);
            LoginField.setEditable(true);
            PasswordField.setDisable(false);
            PasswordField.setEditable(true);
        }
        catch (Exception err) {
            myErrorLabel.setTextFill(Main.ErrorFillColor);
            myErrorLabel.setText("Błąd połączenia");

            err.printStackTrace();
        }
    }

    public void onLoginButtonClick(ActionEvent event) {
        if (LoginField.getText().trim().equals("") || PasswordField.getText().trim().equals("")) {
            ErrorLabelLoginStatic.setText("Podaj dane logowania!");
            ErrorLabelLoginStatic.setTextFill(Main.ErrorFillColor);
            LoginButtonStatic.setDisable(false);
        } else {

            serwerConnector.write("loginļ" + LoginField.getText().trim() + 'ļ' + PasswordField.getText().trim());
            LoginButton.setDisable(true);
        }
    }
}
