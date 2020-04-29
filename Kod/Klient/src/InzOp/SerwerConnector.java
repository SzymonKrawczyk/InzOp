/*
    Klasa odpowiadajaca za połączenie i komunikację z serwerem

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie
                |                       |

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
import java.util.StringTokenizer;

import static InzOp.ConnectAndLoginViewController.ErrorLabelLoginStatic;

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
                Main.SerwerSocket = null;
                Main.SerwerReader = null;
                Main.SerwerWriter = null;
                isConnected = false;
                throw error;
            }
        }
    }

    public void run() {
        try {
            String msg = "";

            while (!msg.equals("logout")) {
                msg = Main.SerwerReader.readLine();
                System.out.println("Serwer: " + msg);

                String[] tokens = msg.split("ļ");
                //System.out.println(tokens[0]);
                switch (tokens[0]) {
                    case "login":
                        if (tokens[1].equals("true")) {
                            System.out.println(tokens[1]);
                            //nowe okno
                            try {
                                Parent MainWindowView = FXMLLoader.load(getClass().getResource("MainWindowView.fxml"));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        ConnectAndLoginViewController.ErrorLabelLoginStatic.setTextFill(Main.PassFillColor);
                                        ConnectAndLoginViewController.ErrorLabelLoginStatic.setText("Poprawne dane logowania");

                                        Main.window.setScene(new Scene(MainWindowView));
                                        Main.Username = ConnectAndLoginViewController.LoginFieldStatic.getText().trim();
                                        Main.window.show();
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

                        }
                        break;
                }

            }

        } catch (Exception error) {

            Main.SerwerSocket = null;
            Main.SerwerReader = null;
            Main.SerwerWriter = null;
            isConnected = false;
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