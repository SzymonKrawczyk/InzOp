package InzOp;

/*
    Kontroler ekranu zarządzania grupami

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

public class ManageGroupsScreenController {


    public void GoToCreateNewGroupScreen(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("CreateNewGroup.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void GoToEditGroupScreen(ActionEvent event) throws  IOException{
        /*Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("/sample/create_new_group_screen.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.show();*/
    }

    public void goBack(ActionEvent event) throws IOException{
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("SettingsView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }
}
