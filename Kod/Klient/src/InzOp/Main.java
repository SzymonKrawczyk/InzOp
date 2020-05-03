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

    public static ArrayList<ChatEntity> chatEntitesList;


    @Override
    public void start(Stage primaryStage) throws Exception {

        chatEntitesList = new ArrayList<ChatEntity>();

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
        Username = "";
        Status = "Offline";
        Privilege = false;
        currentChatEntity = null;
    }

    public static void syncChatList() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final ObservableList<ChatEntity> chatEntities = FXCollections.observableArrayList();
                chatEntities.addAll(Main.chatEntitesList);

                //Listy do synchronizacji
                MainWindowViewController.ChatEntitiesListViewStatic.setItems(chatEntities);

                //
                /*if (Main.currentChatEntity == null && Main.chatEntitesList.size() > 0) {
                    Main.currentChatEntity = Main.chatEntitesList.get(0);
                    //if (MainWindowViewController.targetUserNameStatic != null) MainWindowViewController.targetUserNameStatic.setText(Main.currentChatEntity.getName());
                }*/
                //if (Main.currentChatEntity != null && MainWindowViewController.targetUserNameStatic != null) MainWindowViewController.targetUserNameStatic.setText(Main.currentChatEntity.getName());
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
            if (tempTab[i] == 'ļ') {
                tempTab[i] = '_';
            } else if (tempTab[i] == '\n') {
                tempTab[i] = 'ñ';
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
        }

        return new String(tempTab);
    }
}
