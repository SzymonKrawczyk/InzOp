/*
    Kontroler ekranu ustawień

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    02.05.2020  | Michał Kopałka        |   Stworzenie
                |                       |

 */

package InzOp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class SettingsViewController {

    @FXML private Button userManagmentButton;

    @FXML private Button groupManagmentButton;

    @FXML private Button raportButton;

    public void initialize() {

        if (!Main.Privilege) {
            userManagmentButton.setVisible(false);
            groupManagmentButton.setVisible(false);
            raportButton.setVisible(false);
        }
    }

    public void goBack(ActionEvent event) throws IOException {
        //System.err.println("Back");
        Parent MainindowView = FXMLLoader.load(getClass().getResource("MainWindowView.fxml"));
        Scene newScene = new Scene(MainindowView);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void goToChangePasswordScreen(ActionEvent event) throws IOException{
        //System.err.println("PasswordScreen");
        Parent PasswordChangeView = FXMLLoader.load(getClass().getResource("PasswordChangeView.fxml"));
        Scene newScene = new Scene(PasswordChangeView);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void goToManageUsersScreen(ActionEvent event) throws IOException {
        //System.err.println("UsersScreen");
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("ManageUsersView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }


    public void goToManageGroupsScreen(ActionEvent event) throws IOException{
        //System.err.println("GroupsScreen");
        Parent groupSettingsScreen = FXMLLoader.load(getClass().getResource("ManageGroupsScreen.fxml"));
        Scene newScene = new Scene(groupSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }
}
