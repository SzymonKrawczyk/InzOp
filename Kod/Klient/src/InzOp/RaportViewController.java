/*
    Kontroler ekranu raportów

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    23.05.2020  | Szymon Krawczyk       |   Stworzenie
                |                       |

 */

package InzOp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;


public class RaportViewController {

    @FXML
    private Label currentRaportUser;

    @FXML
    private Label currentStatisticsRaport;

    @FXML
    private Button userdownButton;

    @FXML
    private Button raportdownButton;

    @FXML
    private ListView<ChatEntity> usersListRaports;
    public static ListView<ChatEntity> usersListRaportsStatic;

    @FXML
    private ListView<String> statisticsListRaports;
    public static ListView<String> statisticsListRaportsStatic;


    public void initialize() {

        usersListRaportsStatic = usersListRaports;
        statisticsListRaportsStatic = statisticsListRaports;

        currentRaportUser.setText("");
        currentStatisticsRaport.setText("");

        if (Main.currentRaportUser != null) {
            currentRaportUser.setText(Main.currentRaportUser.getName());
        }

        if (Main.currentStatisticsRaport != null) {
            currentStatisticsRaport.setText(Main.currentStatisticsRaport);
        }

        usersListRaports.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactorySimple();
            }
        });

        usersListRaports.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<ChatEntity>() {
                    @Override
                    public void changed(ObservableValue<? extends ChatEntity> observableValue, ChatEntity oldVal, ChatEntity newVal) {

                        if(newVal != null && newVal != Main.currentRaportUser) {

                            Main.currentRaportUser = newVal;
                            System.out.println("Current User Raport: " + Main.currentRaportUser.getName());
                            currentRaportUser.setText(Main.currentRaportUser.getName());

                            userdownButton.setDisable(false);
                            Main.syncChatList();
                        }
                    }
                }
        );

        statisticsListRaports.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {

                        if(newVal != null && !newVal.equals(Main.currentStatisticsRaport)) {

                            Main.currentStatisticsRaport = newVal;
                            System.out.println("Current Statistics Raport: " + Main.currentStatisticsRaport);
                            currentStatisticsRaport.setText(Main.currentStatisticsRaport);

                            raportdownButton.setDisable(false);
                            Main.syncStatisticsRaports();
                        }
                    }
                }
        );

        Main.serwerConnector.write("getRaports");
        Main.syncChatList();
        Main.syncStatisticsRaports();
    }

    public void userRaport(ActionEvent actionEvent) throws IOException {

        Main.serwerConnector.write("getUserRaportFileļ" + Main.currentRaportUser.getName());
    }

    public void statisticsRaport(ActionEvent actionEvent) throws IOException {

        Main.serwerConnector.write("getStatisticsFileļ" + Main.currentStatisticsRaport);
    }

    public void goBack(ActionEvent actionEvent) throws IOException {

        Parent SettingsView = FXMLLoader.load(getClass().getResource("SettingsView.fxml"));
        Main.window.setScene(new Scene(SettingsView));
        Main.window.show();
    }
}
