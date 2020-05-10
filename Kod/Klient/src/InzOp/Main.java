/*
    Główna klasa aplikacji kleinta

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie
                |                       |
    01.05.2020  | Szymon Krawczyk       |   Dodanie chatEntitesList
                |                       |
    01.05.2020  | Szymon Krawczyk       |   Synchronizacja chatEntitesList z ListView aplikacji
                |                       |
    03.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Zamknięcie normalizacji i denormalizacji wiadomości w metodach statycznych
                |                       |
    08.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Zmiana QoL: Sortowanie listy użytkowników i grup na liście (nowaWiadomość > czyGrupa > status > alfabetycznie)
                |                       |
    10.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Lista grup i jej synchronizacja
                |                       |
 */

package InzOp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;

public class Main extends Application {

    public static String WindowTile = "Chat";

    public static Paint ErrorFillColor = Color.valueOf("#ff782b");
    public static Paint PassFillColor = Color.valueOf("#006D14");

    public static Stage window;
    public Scene ConnectAndLoginView;

    public static BufferedReader SerwerReader;
    public static PrintWriter SerwerWriter;
    public static Socket SerwerSocket;
    public static SerwerConnector serwerConnector;

    public static String Username = "";
    public static String Status = "Offline";
    public static boolean Privilege = false;

    public static ChatEntity currentChatEntity = null;

    public static ChatEntity currentManagedUser = null;
    public static ChatEntity currentManagedGroup = null;

    public static ArrayList<ChatEntity> chatEntitesList;
    public static ArrayList<ChatEntity> allGroupsList;



    @Override
    public void start(Stage primaryStage) throws Exception {

        chatEntitesList = new ArrayList<ChatEntity>();
        allGroupsList = new ArrayList<ChatEntity>();

        ChatEntity.imageOnline = new Image(getClass().getResource("media/green.png").toExternalForm());
        ChatEntity.imageBusy = new Image(getClass().getResource("media/red.png").toExternalForm());
        ChatEntity.imageOffline = new Image(getClass().getResource("media/grey.png").toExternalForm());

        window = primaryStage;
        window.setMinWidth(700);
        window.setMinHeight(400);
        Parent root = FXMLLoader.load(getClass().getResource("ConnectAndLoginView.fxml"));
        ConnectAndLoginView = new Scene (root);
        window.setTitle(Main.WindowTile);

        window.setScene(ConnectAndLoginView);
        window.show();
    }


    public static void main(String[] args) {
        serwerConnector = new SerwerConnector();

        launch(args);
    }

    public static void reset() {
        chatEntitesList = new ArrayList<ChatEntity>();
        allGroupsList = new ArrayList<ChatEntity>();
        Username = "";
        Status = "Offline";
        Privilege = false;
        currentChatEntity = null;
    }

    static Comparator<ChatEntity> mainComparator = (ChatEntity ch1, ChatEntity ch2) -> {

        if (ch1.isNewMsg() && !ch2.isNewMsg()) return -1;
        if (!ch1.isNewMsg() && ch2.isNewMsg()) return 1;

        if (ch1.isGroup() && !ch2.isGroup()) return -1;
        if (!ch1.isGroup() && ch2.isGroup()) return 1;

        if (ch1.getStatus().equals("Online") && !ch2.getStatus().equals("Online")) return -1;
        if (!ch1.getStatus().equals("Online") && ch2.getStatus().equals("Online")) return 1;

        if (ch1.getStatus().equals("Zajęty") && ch2.getStatus().equals("Offline")) return -1;
        if (ch1.getStatus().equals("Offline") && ch2.getStatus().equals("Zajęty")) return 1;

        return ch1.getName().toLowerCase().compareTo(ch2.getName().toLowerCase());
    };

    static Comparator<ChatEntity> simpleComparator = (ChatEntity ch1, ChatEntity ch2) -> {

        if (ch1.isActive() && !ch2.isActive()) return -1;
        if (!ch1.isActive() && ch2.isActive()) return 1;

        return ch1.getName().toLowerCase().compareTo(ch2.getName().toLowerCase());
    };



    public static void syncChatList() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Main.chatEntitesList.sort(mainComparator);
                final ObservableList<ChatEntity> chatEntities = FXCollections.observableArrayList();
                chatEntities.addAll(Main.chatEntitesList);

                //Listy do synchronizacji
                if (MainWindowViewController.ChatEntitiesListViewStatic != null) MainWindowViewController.ChatEntitiesListViewStatic.setItems(chatEntities);


                ArrayList<ChatEntity> chatEntitesListToEdit = new ArrayList<>();

                for (int i = 0; i < Main.chatEntitesList.size(); i++) {
                    ChatEntity currentEntity = Main.chatEntitesList.get(i);
                    if (!currentEntity.isGroup()) chatEntitesListToEdit.add(currentEntity);
                }

                chatEntitesListToEdit.sort(simpleComparator);
                final ObservableList<ChatEntity> chatEntitiesToEdit = FXCollections.observableArrayList();
                chatEntitiesToEdit.addAll(chatEntitesListToEdit);



                if (ManageUsersViewController.manageUsersListViewStatic != null) {



                    ManageUsersViewController.manageUsersListViewStatic.setItems(chatEntitiesToEdit);
                }





                if (EditGroupViewController.allUsersListViewStatic != null) {

                    EditGroupViewController.allUsersListViewStatic.setItems(chatEntitiesToEdit);
                }

                if (EditGroupViewController.membersListViewStatic != null && EditGroupViewController.joinMembersStatic != null) {

                    EditGroupViewController.joinMembersStatic.sort(simpleComparator);
                    final ObservableList<ChatEntity> groupEntities = FXCollections.observableArrayList();
                    groupEntities.addAll(EditGroupViewController.joinMembersStatic);

                    EditGroupViewController.membersListViewStatic.setItems(groupEntities);
                }




                if (CreateNewGroupController.allUsersListViewStatic != null) {

                    CreateNewGroupController.allUsersListViewStatic.setItems(chatEntitiesToEdit);
                }

                if (CreateNewGroupController.membersListViewStatic != null && CreateNewGroupController.joinMembersStatic != null) {

                    CreateNewGroupController.joinMembersStatic.sort(simpleComparator);
                    final ObservableList<ChatEntity> groupEntities = FXCollections.observableArrayList();
                    groupEntities.addAll(CreateNewGroupController.joinMembersStatic);

                    CreateNewGroupController.membersListViewStatic.setItems(groupEntities);
                }



            }
        });
    }

    public static void syncAllGroupList() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (ManageGroupsScreenController.allGroupViewStatic != null) {

                    Main.allGroupsList.sort(simpleComparator);
                    final ObservableList<ChatEntity> groupEntities = FXCollections.observableArrayList();
                    groupEntities.addAll(Main.allGroupsList);

                    ManageGroupsScreenController.allGroupViewStatic.setItems(groupEntities);
                }






                if (EditUserViewController.allGroupsListViewStatic != null) {

                    Main.allGroupsList.sort(simpleComparator);
                    final ObservableList<ChatEntity> groupEntities = FXCollections.observableArrayList();
                    groupEntities.addAll(Main.allGroupsList);

                    EditUserViewController.allGroupsListViewStatic.setItems(groupEntities);
                }

                if (EditUserViewController.isInGroupsListViewStatic != null && EditUserViewController.currentGroupsStatic != null) {

                    EditUserViewController.currentGroupsStatic.sort(simpleComparator);
                    final ObservableList<ChatEntity> groupEntities = FXCollections.observableArrayList();
                    groupEntities.addAll(EditUserViewController.currentGroupsStatic);

                    EditUserViewController.isInGroupsListViewStatic.setItems(groupEntities);
                }





                if (CreateNewUserController.allGroupsListViewStatic != null) {

                    Main.allGroupsList.sort(simpleComparator);
                    final ObservableList<ChatEntity> groupEntities = FXCollections.observableArrayList();
                    groupEntities.addAll(Main.allGroupsList);

                    CreateNewUserController.allGroupsListViewStatic.setItems(groupEntities);
                }

                if (CreateNewUserController.userCreateGroupListStatic != null && CreateNewUserController.joinGroupListStatic != null) {

                    CreateNewUserController.joinGroupListStatic.sort(simpleComparator);
                    final ObservableList<ChatEntity> groupEntities = FXCollections.observableArrayList();
                    groupEntities.addAll(CreateNewUserController.joinGroupListStatic);

                    CreateNewUserController.userCreateGroupListStatic.setItems(groupEntities);
                }

            }
        });
    }


    public static void setNewMsgForEntity(String name, boolean b) {

        for (int i = 0; i < Main.chatEntitesList.size(); i++) {
            ChatEntity temp = Main.chatEntitesList.get(i);
            if (temp.getName().equals(name)) {
                temp.setNewMsg(b);
                //syncChatList(); //TODO ?????????????????
                break;
            }
        }
    }

    public static ChatEntity findEntityByName(String name) {
        for (int i = 0; i < chatEntitesList.size(); i++) {
            ChatEntity temp = chatEntitesList.get(i);

            if (temp.getName().equals(name)) {
                return temp;
            }
        }
        return null;
    }

    public static String normalizeString (String toProcess) {
        char[] tempTab = toProcess.toCharArray();

        for (int i = 0; i < tempTab.length; i++) {
            switch (tempTab[i]) {
                case 'ļ': tempTab[i] = '_';
                    break;
                case '\n': tempTab[i] = 'ñ';
                    break;
                case '\'': tempTab[i] = 'á';
                    break;

            }
        }

        return new String(tempTab);
    }

    public static String denormalizeString (String toProcess) {
        char[] tempTab = toProcess.toCharArray();

        for (int i = 0; i < tempTab.length; i++) {
            if (tempTab[i] == 'ñ') {
                tempTab[i] = '\n';
            }
            switch (tempTab[i]) {
                case 'ñ': tempTab[i] = '\n';
                    break;
                case 'á': tempTab[i] = '\'';
                    break;

            }
        }

        return new String(tempTab);
    }
}