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

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class CreateNewUserController {

    @FXML private ListView<ChatEntity> allGroupList;
    public static ListView<ChatEntity> allGroupsListViewStatic;
    @FXML private ListView<ChatEntity> userCreateGroupList;
    public static ListView<ChatEntity> userCreateGroupListStatic;

    private ArrayList<ChatEntity> joinGroupList;
    public static ArrayList<ChatEntity> joinGroupListStatic;


    @FXML private Label createUserNameErrorLabel;
    public static Label createUserNameErrorLabelStatic;
    @FXML private Label createUserPasswordErrorLabel;

    @FXML private TextField createUserUsernameField;
    public static TextField createUserUsernameFieldStatic;
    @FXML private PasswordField createUserPasswordField;

    @FXML private Button createUserButton;
    public static Button createUserButtonStatic;

    @FXML private CheckBox createUserAdministratorCheckBox;

    public void initialize () {

        joinGroupList = new ArrayList<>();
        joinGroupListStatic = joinGroupList;
        userCreateGroupListStatic = userCreateGroupList;
        allGroupsListViewStatic = allGroupList;
        createUserButtonStatic = createUserButton;
        createUserUsernameFieldStatic = createUserUsernameField;
        createUserNameErrorLabelStatic = createUserNameErrorLabel;


        createUserPasswordErrorLabel.setText("");
        createUserNameErrorLabel.setTextFill(Main.ErrorFillColor);
        createUserPasswordErrorLabel.setTextFill(Main.ErrorFillColor);



        allGroupList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        userCreateGroupList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        allGroupList.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        userCreateGroupList.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        Main.syncAllGroupList();
    }




    public void goBack(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("ManageUsersView.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void addGroup(ActionEvent event) {
        ObservableList<ChatEntity> selectedGroupsFromAll =  allGroupList.getSelectionModel().getSelectedItems();

        for(ChatEntity group : selectedGroupsFromAll){
            if (!joinGroupList.contains(group)) joinGroupList.add(group);
        }
        Main.syncAllGroupList();
    }

    public void leaveGroup(ActionEvent event) {
        ObservableList<ChatEntity> selectedGroupsFromCurrent =  userCreateGroupList.getSelectionModel().getSelectedItems();

        for(ChatEntity group : selectedGroupsFromCurrent){
            joinGroupList.remove(group);
        }
        Main.syncAllGroupList();
    }

    public static void addGroups(){
        for(ChatEntity groupEntity : joinGroupListStatic){
            Main.serwerConnector.write("editUserļ" + createUserUsernameFieldStatic.getText().trim() + "ļgroupļjoinļ" + groupEntity.getName());
        }
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
