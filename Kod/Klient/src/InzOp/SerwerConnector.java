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
                |                       |s

 */

package InzOp;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

                        Main.chatEntitesList.add(new ChatEntity(name, group, status, active, newMsg));
                        Main.syncChatList();
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
                                if (Main.currentChatEntity != temp) temp.setNewMsg(UnewMsg);
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

                            String from;
                            if(Main.findEntityByName(tokens[1]) != null && Main.findEntityByName(tokens[1]).isGroup()) {
                                from = tokens[3];
                            } else {
                                from = tokens[1];
                            }

                            String newText = MainWindowViewController.chatHistoryStatic.getText() + "\n" + from + " >>> " + newMsg;
                            MainWindowViewController.chatHistoryStatic.setText(newText);

                            //TODO
                            //Main.setNewMsgForEntity(tokens[1], true);

                        } else {

                            Main.setNewMsgForEntity(tokens[1], true);
                        }


                    }
                        break;
                    case "addNewUser": {
                        try {
                            if (tokens[1].equals("true")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
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

                    // inne komendy
                }

            }

        } catch (Exception error) {

            reset();
        }
    }

    public void write(String message) {
        Main.SerwerWriter.println(message);
    }
}

/*

Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        //update application thread
                                    }
                                });
 */