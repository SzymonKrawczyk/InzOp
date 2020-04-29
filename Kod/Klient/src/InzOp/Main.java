/*
    Główna klasa aplikacji kleinta

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie
                |                       |

 */

package InzOp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main extends Application {

    public static String WindowTile = "Chat";

    public static Paint ErrorFillColor = Color.valueOf("#ff782b");
    public static Paint PassFillColor = Color.valueOf("#006D14");

    public static Stage window;
    public Scene ConnectAndLoginView;

    public static String Username = "";
    public static BufferedReader SerwerReader;
    public static PrintWriter SerwerWriter;
    public static Socket SerwerSocket;
    public static SerwerConnector serwerConnector;

    @Override
    public void start(Stage primaryStage) throws Exception {

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
}
