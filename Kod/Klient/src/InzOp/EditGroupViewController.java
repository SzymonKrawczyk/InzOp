package InzOp;

/*
    Kontroler ekranu edycji grupy

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    10.05.2020  | Szymon Krawczyk       |   Stworzenie
                |                       |

 */

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("DuplicatedCode")
public class EditGroupViewController {

    @FXML
    private ListView<ChatEntity> allUsersListView;
    public static ListView<ChatEntity> allUsersListViewStatic;
    @FXML private ListView<ChatEntity> membersListView;
    public static ListView<ChatEntity> membersListViewStatic;

    private ArrayList<ChatEntity> joinMembers;
    private ArrayList<ChatEntity> joinMembersCopy;
    public static ArrayList<ChatEntity> joinMembersCopyStatic;
    public static ArrayList<ChatEntity> joinMembersStatic;

    private ArrayList<ChatEntity> joinGroupList;
    private ArrayList<ChatEntity> leaveGroupList;

    @FXML private Label currentGroup;


    @FXML private Button saveGroupButton;
    public static Button saveGroupButtonStatic;

    @FXML private Button deleteGroupButton;
    public static Button deleteGroupButtonStatic;


    public void initialize() {

        Main.serwerConnector.write("getInfoAboutGroupļ" + Main.currentManagedGroup.getName());
        currentGroup.setText(Main.currentManagedGroup.getName());

        saveGroupButtonStatic = saveGroupButton;
        deleteGroupButtonStatic = deleteGroupButton;

        allUsersListViewStatic = allUsersListView;
        membersListViewStatic = membersListView;

        joinMembersStatic = new ArrayList<>();
        joinMembers = new ArrayList<>();
        joinMembersStatic = joinMembers;
        joinMembersCopy = new ArrayList<>();
        joinMembersCopyStatic = new ArrayList<>();
        joinMembersCopyStatic = joinMembersCopy;

        joinGroupList = new ArrayList<>();
        leaveGroupList = new ArrayList<>();


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
        joinMembersCopy.addAll(joinMembers);

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

    public void saveGroup(ActionEvent event) throws IOException {

        saveGroupButton.setDisable(true);

        for (ChatEntity group : joinMembers) {
            if (!joinMembersCopy.contains(group)) joinGroupList.add(group);
        }

        for (ChatEntity group : joinMembersCopy) {
            if (!joinMembers.contains(group) ) leaveGroupList.add(group);
        }

        for(ChatEntity userEntity : joinGroupList){
            Main.serwerConnector.write("editUserļ" + userEntity.getName()+ "ļgroupļjoinļ" + Main.currentManagedGroup.getName());
        }

        for(ChatEntity userEntity : leaveGroupList){
            Main.serwerConnector.write("editUserļ" + userEntity.getName()+ "ļgroupļleaveļ" + Main.currentManagedGroup.getName());
        }
    }

    public void deleteGroup(ActionEvent event) {
        saveGroupButton.setDisable(true);
        deleteGroupButton.setDisable(true);
        Main.serwerConnector.write("deleteGroupļ" + Main.currentManagedGroup.getName());
    }


    public void goBack(ActionEvent event) throws IOException {
        Parent userSettingsScreen = FXMLLoader.load(getClass().getResource("ManageGroupsScreen.fxml"));
        Scene newScene = new Scene(userSettingsScreen);
        Main.window.setScene(newScene);
        Main.window.show();
    }
}
