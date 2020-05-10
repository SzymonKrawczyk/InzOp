package InzOp;

/*
    Kontroler ekranu zarządzania grupami

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    03.05.2020  | Michał Kopałka        |   Stworzenie
                |                       |
    10.05.2020  | Szymon Krawczyk       |   Dodanie funkcjonalności
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

public class ManageGroupsScreenController {

    @FXML
    private Label currrentGroupSelected;

    @FXML private Button editCurrentGroup;

    @FXML private ListView<ChatEntity> allGroupView;
    public static ListView<ChatEntity> allGroupViewStatic;

    public void initialize() {

        currrentGroupSelected.setText("");
        editCurrentGroup.setDisable(true);

        allGroupViewStatic = allGroupView;

        if (Main.currentManagedGroup != null) {

            currrentGroupSelected.setText(Main.currentManagedGroup.getName());
            editCurrentGroup.setDisable(false);
        }

        allGroupView.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        allGroupView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<ChatEntity>() {
                    @Override
                    public void changed(ObservableValue<? extends ChatEntity> observableValue, ChatEntity oldVal, ChatEntity newVal) {

                        if(newVal != null && newVal != Main.currentManagedGroup) {

                            Main.currentManagedGroup = newVal;
                            System.out.println("Current ChatEntity(group) to edit: " + Main.currentManagedGroup.getName());

                            currrentGroupSelected.setText(Main.currentManagedGroup.getName());
                            editCurrentGroup.setDisable(false);
                        }


                    }
                }
        );

        Main.syncAllGroupList();
    }


    public void GoToCreateNewGroupScreen(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("CreateNewGroup.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void GoToEditGroupScreen(ActionEvent event) throws  IOException{
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("EditGroupView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void goBack(ActionEvent event) throws IOException{
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("SettingsView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }
}
