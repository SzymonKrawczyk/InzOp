/*
    Klasa odpowiadajaca za połączenie i komunikację z serwerem

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie
                |                       |
    01.05.2020  | Szymon Krawczyk       |   Dodanie chatEntitesList i jej obsługi (obsługa nowego chatEntity z serwera)
                |                       |   Dodanie wylogowania
                |                       |
    02.05.2020  | Szymon Krawczyk       |   Synchronizacja listy użytkowników z serwerem (status)
                |                       |   Obsługa wiadomości UU
                |                       |
    03.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Dodawanie nowej grupy
                |                       |   Dodawanie nowego użytkownika
                |                       |
    10.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Usuwanie/ modyfikowanie grupy/klienta
                |                       |
    17.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Pobieranie historii chatu
                |                       |   Zmiana systemu wiadomości
                |                       |
 */

package InzOp;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class SerwerConnector extends Thread {

    private boolean isConnected;

    public SerwerConnector() {
        isConnected = false;
    }

    public void connect (String address) throws Exception {
        if (!isConnected) {
            try {

                Main.SerwerSocket = new Socket(address, 29090);
                Main.SerwerReader = new BufferedReader(new InputStreamReader(Main.SerwerSocket.getInputStream()));
                Main.SerwerWriter = new PrintWriter(Main.SerwerSocket.getOutputStream(), true);

                start();

                isConnected = true;

            }
            catch (Exception error) {
                reset();
                throw error;
            }
        }
    }

    private void reset(){

        Main.SerwerSocket = null;
        Main.SerwerReader = null;
        Main.SerwerWriter = null;
        isConnected = false;
        Main.Username = "";
        Main.Privilege = false;
        /*try {

            Parent root = FXMLLoader.load(getClass().getResource("ConnectAndLoginView.fxml"));
            Main.window.setScene(new Scene (root));
            Main.window.show();
            Main.serwerConnector = new SerwerConnector();
        } catch (Exception err) {

        }*/
    }

    public void run() {
        try {
            String msg = "";

            while (true) {
                msg = Main.SerwerReader.readLine();
                System.out.println("Serwer: " + msg);

                String[] tokens = msg.split("ļ");
                //System.out.println(tokens[0]);
                switch (tokens[0].trim()) {
                    case "login": {
                        if (tokens[1].trim().equals("true")) {

                            Main.serwerConnector.write("getAllEntities");
                            Main.serwerConnector.write("getAllGroups");

                            Main.Privilege = tokens[2].equals("true");
                            //System.out.println(tokens[1] + " | " + tokens[2]);
                            //nowe okno
                            try {
                                Parent MainWindowView = FXMLLoader.load(getClass().getResource("MainWindowView.fxml"));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        ConnectAndLoginViewController.ErrorLabelLoginStatic.setTextFill(Main.PassFillColor);
                                        ConnectAndLoginViewController.ErrorLabelLoginStatic.setText("Poprawne dane logowania");

                                        Main.Username = ConnectAndLoginViewController.LoginFieldStatic.getText().trim();
                                        //System.out.println("Username: " + Main.Username);
                                        Main.window.setScene(new Scene(MainWindowView));
                                        Main.window.show();
                                        MainWindowViewController.usernameMainStatic.setText(Main.Username);
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                                System.exit(0);
                            }
                        } else {
                            //blad logowania
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    ConnectAndLoginViewController.ErrorLabelLoginStatic.setText("Niepoprawne dane logowania");
                                    ConnectAndLoginViewController.ErrorLabelLoginStatic.setTextFill(Main.ErrorFillColor);
                                    ConnectAndLoginViewController.LoginButtonStatic.setDisable(false);
                                }
                            });

                            Main.Username = "";
                            Main.Privilege = false;
                        }
                    }
                        break;
                    case "logout": {
                        try {
                            Parent LoginWindowView = FXMLLoader.load(getClass().getResource("ConnectAndLoginView.fxml"));
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Main.reset();
                                    Main.window.setScene(new Scene(LoginWindowView));
                                    Main.window.show();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                    }
                        break;
                    case "newEntity": {

                        String name = tokens[1];
                        boolean group = Boolean.parseBoolean(tokens[2]);
                        String status = tokens[3];
                        boolean active = Boolean.parseBoolean(tokens[4]);
                        boolean newMsg = Boolean.parseBoolean(tokens[5]);

                        boolean error = false;
                        for(ChatEntity entity : Main.chatEntitesList) {
                            if (entity.getName().equals(name)) {
                                error = true;
                                break;
                            }
                        }

                        if (!error){
                            Main.chatEntitesList.add(new ChatEntity(name, group, status, active, newMsg));
                            Main.syncChatList();
                        }
                    }
                        break;
                    case "updateEntity":{
                        String Uname = tokens[1];
                        String Ustatus = tokens[2];
                        boolean Uactive = Boolean.parseBoolean(tokens[3]);
                        boolean UnewMsg = Boolean.parseBoolean(tokens[4]);

                        for(int i = 0; i < Main.chatEntitesList.size(); i++) {

                            ChatEntity temp = Main.chatEntitesList.get(i);
                            if (temp.getName().equals(Uname)) {

                                temp.setStatus(Ustatus);
                                temp.setActive(Uactive);
                                if (Main.currentChatEntity != temp && !temp.isNewMsg()) temp.setNewMsg(UnewMsg);
                                break;
                            }
                        }
                        Main.syncChatList();
                    }
                        break;
                    case "changePassword": {
                        try {
                            if (tokens[1].equals("true")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        PasswordChangeViewController.ErrorCurrentPasswordLabelStatic.setText("Hasło zmienione");
                                        PasswordChangeViewController.ErrorCurrentPasswordLabelStatic.setTextFill(Main.PassFillColor);
                                        PasswordChangeViewController.changePasswordButtonStatic.setDisable(false);
                                    }
                                });

                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        PasswordChangeViewController.ErrorCurrentPasswordLabelStatic.setText("Błąd - hasło niepoprawne");
                                        PasswordChangeViewController.ErrorCurrentPasswordLabelStatic.setTextFill(Main.ErrorFillColor);
                                        PasswordChangeViewController.currentPasswordFieldStatic.setText("");
                                        PasswordChangeViewController.changePasswordButtonStatic.setDisable(false);
                                    }
                                });

                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                        break;
                    case "newMessage": {
                        //newMessage fromuser time message
                        if (Main.currentChatEntity != null && tokens[1].equals(Main.currentChatEntity.getName())) {

                            String newMsg = Main.denormalizeString(tokens[2]);

                            String from = tokens[5];
                            String date = tokens[3];
                            int id = Integer.parseInt(tokens[6]);

                            Main.messageEntitesList.add(new MessageEntity(id, from, "", newMsg, date));
                            Main.synchMessageList();

                            //TODO
                            //Main.setNewMsgForEntity(tokens[1], true);

                        } else {
                            Main.setNewMsgForEntity(tokens[1], true);

                            boolean isFromAdmin = Boolean.parseBoolean(tokens[4]);  // Jak tak to powiadomienie
                            if( isFromAdmin == true ) {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        boolean flag = false;

                                        for (int i = 0; i < Main.chatEntitesList.size(); i++) {
                                            ChatEntity temp = Main.chatEntitesList.get(i);
                                            if (temp.getName().equals(tokens[1]) && temp.isGroup()) {
                                                flag = true;
                                                break;
                                            }
                                        }

                                        Stage window = new Stage();
                                        window.initModality(Modality.APPLICATION_MODAL);
                                        window.setTitle("Nowa wiadomość!");
                                        window.setWidth(400);
                                        window.setHeight(200);

                                        Label label = new Label();
                                        label.setWrapText(true);

                                        TextFlow flow = new TextFlow();
                                        flow.setTextAlignment(TextAlignment.CENTER);

                                        if(flag){
                                            Text text1=new Text("   " + tokens[5]);
                                            text1.setStyle("-fx-font-weight: bold");
                                            Text text2=new Text(" dodał nową wiadomość na czacie grupowym: ");
                                            text2.setStyle("-fx-font-weight: regular");
                                            Text text3=new Text(tokens[1]);
                                            text3.setStyle("-fx-font-weight: bold");
                                            Text text4=new Text(".");
                                            flow.getChildren().addAll(text1, text2, text3, text4);
                                        }
                                        else {
                                            Text text1=new Text("   Dostałeś wiadomość od ");
                                            text1.setStyle("-fx-font-weight: regular");
                                            Text text2=new Text(tokens[5]);
                                            text2.setStyle("-fx-font-weight: bold");
                                            Text text3=new Text(".");
                                            text3.setStyle("-fx-font-weight: regular");
                                            flow.getChildren().addAll(text1, text2, text3);
                                        }

                                        Label label2 = new Label("Jak najszybciej zapoznaj się z jej treścią. \n");

                                        Button button = new Button("rozumiem");
                                        button.setOnAction(e->window.close());

                                        VBox vBox = new VBox( 10 );
                                        vBox.getChildren().addAll(flow, label2, button);

                                        vBox.setAlignment(Pos.CENTER);

                                        Scene scene = new Scene(vBox);
                                        window.setScene(scene);

                                        window.show();
                                    }
                                });
                            }

                        }
                    }
                        break;
                    case "addNewUser": {
                        try {
                            if (tokens[1].equals("true")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        CreateNewUserController.addGroups();

                                        CreateNewUserController.createUserNameErrorLabelStatic.setText("Użytkownik utworzony");
                                        CreateNewUserController.createUserNameErrorLabelStatic.setTextFill(Main.PassFillColor);
                                        CreateNewUserController.createUserButtonStatic.setDisable(false);
                                    }
                                });

                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        CreateNewUserController.createUserNameErrorLabelStatic.setText("Nazwa użytkownika zajęta");
                                        CreateNewUserController.createUserNameErrorLabelStatic.setTextFill(Main.ErrorFillColor);
                                        CreateNewUserController.createUserButtonStatic.setDisable(false);
                                    }
                                });

                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                        break;
                    case "addNewGroup": {
                        try {
                            if (tokens[1].equals("true")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        CreateNewGroupController.addMembers();
                                        CreateNewGroupController.createNewGroupNameLabelStatic.setText("Grupa utworzona");
                                        CreateNewGroupController.createNewGroupNameLabelStatic.setTextFill(Main.PassFillColor);
                                        CreateNewGroupController.createNewGroupButtonStatic.setDisable(false);
                                    }
                                });

                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        CreateNewGroupController.createNewGroupNameLabelStatic.setText("Nazwa grupy zajęta");
                                        CreateNewGroupController.createNewGroupNameLabelStatic.setTextFill(Main.ErrorFillColor);
                                        CreateNewGroupController.createNewGroupButtonStatic.setDisable(false);
                                    }
                                });

                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                        break;
                    case "allGroupInfo": {

                        Main.allGroupsList.add(new ChatEntity(tokens[1], true, "Online", true, false));
                        Main.syncAllGroupList();
                    }
                        break;
                    case "currentUserGroupInfo": {

                        for (ChatEntity groupTemp : Main.allGroupsList) {
                            if (groupTemp.getName().equals(tokens[1]) && EditUserViewController.currentGroupsStatic != null && EditUserViewController.currentGroupsCopyStatic != null) {
                                EditUserViewController.currentGroupsStatic.add(groupTemp);
                                EditUserViewController.currentGroupsCopyStatic.add(groupTemp);
                                break;
                            }
                        }
                        Main.syncAllGroupList();
                    }
                        break;
                    case "currentGroupInfo": {

                        for (ChatEntity userTemp : Main.chatEntitesList) {
                            if (userTemp.getName().equals(tokens[1]) && EditGroupViewController.joinMembersStatic != null && EditGroupViewController.joinMembersCopyStatic != null) {
                                EditGroupViewController.joinMembersStatic.add(userTemp);
                                EditGroupViewController.joinMembersCopyStatic.add(userTemp);
                                break;
                            }
                        }
                        Main.syncChatList();
                    }
                        break;
                    case "currentUserInfo": {

                        if (EditUserViewController.isActiveCheckboxStatic != null) {

                            switch (tokens[1]){
                                case "active":
                                    EditUserViewController.isActiveCheckboxStatic.setSelected(Boolean.parseBoolean(tokens[2]));
                                    EditUserViewController.isActiveCheckboxStaticboolean = Boolean.parseBoolean(tokens[2]);
                                    break;
                                case "privilege":
                                    EditUserViewController.isAdminCheckboxStatic.setSelected(Boolean.parseBoolean(tokens[2]));
                                    EditUserViewController.isAdminCheckboxStaticboolean = Boolean.parseBoolean(tokens[2]);
                                    break;
                            }
                        }
                    }
                        break;
                    case "deleteEntity": {
                        Main.chatEntitesList.removeIf(entity -> entity.getName().equals(tokens[1]));
                        Main.allGroupsList.removeIf(entity -> entity.getName().equals(tokens[1]));
                        Main.syncChatList();
                        Main.syncAllGroupList();
                    }
                        break;
                    case "deleteMessage": {

                        Main.messageEntitesList.removeIf(msgE -> msgE.getId() == Integer.parseInt(tokens[1]));
                        Main.synchMessageList();
                    }
                        break;
                    case "deleteMessageConfirmation": {

                        MainWindowViewController.clickable = true;
                    }
                        break;
                    case "raportsInfo": {

                        String filename = tokens[1];

                        Main.statisticRaportsList.add(filename);
                        Main.syncStatisticsRaports();
                    }
                    break;
                    case "newFile": {

                        String filename = tokens[1];

                        FileWriter fw = new FileWriter(filename,false);
                        fw.write( "");
                        fw.flush();
                        fw.close();

                    }
                    break;
                    case "fileContent": {

                        String filename = tokens[1];

                        FileWriter fw = new FileWriter(filename,true);
                        fw.write( tokens[2].substring(0, tokens[2].length()-1) + "\n");
                        fw.flush();
                        fw.close();

                    }
                    break;



                    // inne komendy
                }

            }

        } catch (Exception error) {

            reset();
        }
    }

    public void write(String message) {
        try {

            Main.SerwerWriter.println(message);

        } catch (Exception err) {
            reset();
        }
    }
}

/*
do modyfikacji kodu fxml z innych miejsc aplikacji
Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        //update application thread
                                    }
                                });
 */
