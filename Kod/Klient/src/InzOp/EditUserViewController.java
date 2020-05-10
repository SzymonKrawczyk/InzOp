package InzOp;

/*
    Kontroler ekranu edycji użytkownika

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    10.05.2020  | Szymon Krawczyk       |   Stworzenie
                |                       |

 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("DuplicatedCode")
public class EditUserViewController {

    @FXML
    private ListView<ChatEntity> allGroupsListView;
    public static ListView<ChatEntity> allGroupsListViewStatic;

    @FXML
    private ListView<ChatEntity> isInGroupsListView;
    public static ListView<ChatEntity> isInGroupsListViewStatic;

    @FXML
    private CheckBox isAdminCheckbox;
    public static CheckBox isAdminCheckboxStatic;

    @FXML
    private CheckBox isActiveCheckbox;
    public static CheckBox isActiveCheckboxStatic;

    @FXML
    private Label ErrorLabelEditUser;

    @FXML
    private Label mainLabelEditUser;

    @FXML
    private Label errorLabelEdituserPassword;

    @FXML
    private Button saveUserEditButton;
    @FXML
    private Button deleteUserEditButton;

    @FXML
    private PasswordField editUserPasswordField;

    private ArrayList<ChatEntity> currentGroups;
    private ArrayList<ChatEntity> currentGroupsCopy;
    public static ArrayList<ChatEntity> currentGroupsCopyStatic;
    public static ArrayList<ChatEntity> currentGroupsStatic;

    private ArrayList<ChatEntity> joinGroupList;
    private ArrayList<ChatEntity> leaveGroupList;

    public void initialize() {

        Main.serwerConnector.write("getInfoAboutļ" + Main.currentManagedUser.getName());
        mainLabelEditUser.setText(Main.currentManagedUser.getName());
        errorLabelEdituserPassword.setTextFill(Main.ErrorFillColor);

        isAdminCheckboxStatic = isAdminCheckbox;
        isActiveCheckboxStatic = isActiveCheckbox;

        allGroupsListViewStatic = allGroupsListView;
        isInGroupsListViewStatic = isInGroupsListView;

        ErrorLabelEditUser.setText("");
        errorLabelEdituserPassword.setText("");

        currentGroupsStatic = new ArrayList<>();
        currentGroups = new ArrayList<>();
        currentGroupsCopy = new ArrayList<>();
        currentGroupsCopyStatic = new ArrayList<>();
        currentGroupsStatic = currentGroups;
        currentGroupsCopyStatic = currentGroupsCopy;


        Main.serwerConnector.write("currentUserGroupInfoļ" + Main.currentManagedUser.getName());

        joinGroupList = new ArrayList<>();
        leaveGroupList = new ArrayList<>();


        allGroupsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        isInGroupsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        allGroupsListView.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        isInGroupsListView.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        Main.syncAllGroupList();
        currentGroupsCopy.addAll(currentGroups);
    }

    public void checkPassword(KeyEvent event) {
        String newPassword = editUserPasswordField.getText();
        String errorMessagePswd = "";
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



        if (!errorPswd){

            saveUserEditButton.setDisable(false);
            errorLabelEdituserPassword.setText("");
        } else {

            saveUserEditButton.setDisable(true);
            errorLabelEdituserPassword.setText(errorMessagePswd);
        }
    }

    public void addSelectedGroups(ActionEvent event) {
        ObservableList<ChatEntity> selectedGroupsFromAll =  allGroupsListView.getSelectionModel().getSelectedItems();

        for(ChatEntity group : selectedGroupsFromAll){
            if (!currentGroups.contains(group)) currentGroups.add(group);
        }
        Main.syncAllGroupList();

    }

    public void removeSelectedGroups(ActionEvent event) {
        ObservableList<ChatEntity> selectedGroupsFromCurrent =  isInGroupsListView.getSelectionModel().getSelectedItems();

        for(ChatEntity group : selectedGroupsFromCurrent){
            currentGroups.remove(group);
        }
        Main.syncAllGroupList();
    }

    public void saveUser(ActionEvent event) {

        saveUserEditButton.setDisable(true);

        String newpassword = editUserPasswordField.getText().trim();
        if (newpassword.length() > 0) {

            Main.serwerConnector.write("editUserļ" + Main.currentManagedUser.getName() + "ļpasswordļ" + newpassword);
        }

            Main.serwerConnector.write("editUserļ" + Main.currentManagedUser.getName() + "ļadminļ" + isAdminCheckbox.isSelected());
            Main.serwerConnector.write("editUserļ" + Main.currentManagedUser.getName() + "ļactiveļ" + isActiveCheckbox.isSelected());


        for (ChatEntity group : currentGroups) {
            if (!currentGroupsCopy.contains(group)) joinGroupList.add(group);
        }

        for (ChatEntity group : currentGroupsCopy) {
            if (!currentGroups.contains(group) ) leaveGroupList.add(group);
        }

        for(ChatEntity groupEntity : joinGroupList){
            Main.serwerConnector.write("editUserļ" + Main.currentManagedUser.getName() + "ļgroupļjoinļ" + groupEntity.getName());
        }

        for(ChatEntity groupEntity : leaveGroupList){
            Main.serwerConnector.write("editUserļ" + Main.currentManagedUser.getName() + "ļgroupļleaveļ" + groupEntity.getName());
        }
    }

    public void deleteUser(ActionEvent event) {
        saveUserEditButton.setDisable(true);
        deleteUserEditButton.setDisable(true);
        Main.serwerConnector.write("deleteUserļ" + Main.currentManagedUser.getName());
    }

    public void goBack(ActionEvent event) throws IOException {

        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("ManageUsersView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }
}


