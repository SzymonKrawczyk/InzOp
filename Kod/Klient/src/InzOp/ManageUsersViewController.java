package InzOp;

/*
    Kontroler ekranu zarządzania uzytkownikami

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    03.05.2020  | Michał Kopałka        |   Stworzenie
                |                       |

 */


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;


public class ManageUsersViewController {

    @FXML private Label currentManageUserLabel;

    @FXML private Button editCurrentUser;

    @FXML private ListView<ChatEntity> manageUsersListView;
    public static ListView<ChatEntity> manageUsersListViewStatic;

    public void initialize() {

        currentManageUserLabel.setText("");
        editCurrentUser.setDisable(true);

        manageUsersListViewStatic = manageUsersListView;

        if (Main.currentManagedUser != null) {

            currentManageUserLabel.setText(Main.currentManagedUser.getName());
            editCurrentUser.setDisable(false);
        }

        manageUsersListView.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        manageUsersListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<ChatEntity>() {
                    @Override
                    public void changed(ObservableValue<? extends ChatEntity> observableValue, ChatEntity oldVal, ChatEntity newVal) {

                        if(newVal != null && newVal != Main.currentManagedUser) {

                            Main.currentManagedUser = newVal;
                            System.out.println("Current ChatEntity to edit: " + Main.currentManagedUser.getName());

                            currentManageUserLabel.setText(Main.currentManagedUser.getName());
                            editCurrentUser.setDisable(false);
                        }


                    }
                }
        );

        Main.syncChatList();
    }

    public void goBack(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("SettingsView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }


    public void GoToEditUserScreen(ActionEvent event) throws IOException{
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("EditUserView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void GoToCreateNewUserScreen(ActionEvent event) throws IOException{
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("CreateNewUser.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }
}
