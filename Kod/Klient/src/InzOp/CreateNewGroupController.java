package InzOp;

/*
    Kontroler ekranu tworzenia nowej grupy

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    03.05.2020  | Michał Kopałka        |   Stworzenie
                |                       |
    03.05.2020  | Szymon Krawczyk       |   Stworzenie i rozwinięcie właściwej funkcjonalności
                |                       |
    10.05.2020  | Szymon Krawczyk       |   Rozwinięcie właściwej funkcjonalności
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

public class CreateNewGroupController {

    @FXML private ListView<ChatEntity> allUsersListView;
    public static ListView<ChatEntity> allUsersListViewStatic;
    @FXML private ListView<ChatEntity> membersListView;
    public static ListView<ChatEntity> membersListViewStatic;

    private ArrayList<ChatEntity> joinMembers;
    public static ArrayList<ChatEntity> joinMembersStatic;

    @FXML private Label createNewGroupNameLabel;
    public static Label createNewGroupNameLabelStatic;

    @FXML private Button createNewGroupButton;
    public static Button createNewGroupButtonStatic;

    @FXML private TextField groupnameTextField;
    public static TextField groupnameTextFieldStatic;

    @SuppressWarnings("DuplicatedCode")
    public void initialize() {

        groupnameTextFieldStatic = groupnameTextField;

        allUsersListViewStatic = allUsersListView;
        membersListViewStatic = membersListView;

        joinMembers = new ArrayList<>();
        joinMembersStatic = joinMembers;

        createNewGroupNameLabelStatic = createNewGroupNameLabel;
        createNewGroupButtonStatic = createNewGroupButton;

        createNewGroupNameLabel.setText("");
        createNewGroupNameLabel.setTextFill(Main.ErrorFillColor);

        allUsersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        membersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        allUsersListView.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        membersListView.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        Main.syncChatList();

    }

    public void addMember(ActionEvent event) {
        ObservableList<ChatEntity> selectedGroupsFromAll =  allUsersListView.getSelectionModel().getSelectedItems();

        for(ChatEntity user : selectedGroupsFromAll){
            if (!joinMembers.contains(user)) joinMembers.add(user);
            //System.out.println(group.getName());
        }
        Main.syncChatList();
    }

    public void leaveMember(ActionEvent event) {
        ObservableList<ChatEntity> selectedGroupsFromCurrent =  membersListView.getSelectionModel().getSelectedItems();

        for(ChatEntity user : selectedGroupsFromCurrent){
            joinMembers.remove(user);
        }
        Main.syncChatList();
    }

    public static void addMembers(){
        for(ChatEntity userEntity : joinMembersStatic){
            Main.serwerConnector.write("editUserļ" + userEntity.getName()+ "ļgroupļjoinļ" + groupnameTextFieldStatic.getText().trim() );
        }
    }

    public void createGroup(ActionEvent event) throws IOException {

        createNewGroupButton.setDisable(true);
        Main.serwerConnector.write("addNewGroupļ" + groupnameTextField.getText().trim());
    }

    public void goBack(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("ManageGroupsScreen.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }

    public void checkGroupName (KeyEvent event) {

        String newName = groupnameTextField.getText();

        String errorMessageName = "Nazwy nie można później zmienić";
        boolean errorName = false;


        if (newName.length() < 6) {
            errorName = true;
            errorMessageName ="Nazwa musi mieć przynajmniej 6 znaków";
        } else if (newName.length() > 20) {
            errorName = true;
            errorMessageName = "Nazwa nie może przekraczać 20 znaków";
        }

        if (newName.matches("ļ")) {
            errorName = true;
            errorMessageName = "Nazwa nie może zawierać ļ";
        }


        if (!errorName) {
            createNewGroupButton.setDisable(false);
        } else {
            createNewGroupButton.setDisable(true);
        }
        createNewGroupNameLabel.setText(errorMessageName);
    }
}
