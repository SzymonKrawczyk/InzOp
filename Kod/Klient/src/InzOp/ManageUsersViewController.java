package InzOp;

/*
    Kontroler ekranu zarządzania uzytkownikami

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    03.05.2020  | Michał Kopałka        |   Stworzenie
                |                       |

 */


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageUsersViewController {

    public void goBack(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("SettingsView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }


    public void GoToEditUserScreen(ActionEvent event) throws IOException{
        /*Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("/sample/edit_user_screen.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.show();*/
    }

    public void GoToCreateNewUserScreen(ActionEvent event) throws IOException{
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("CreateNewUser.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }
}
