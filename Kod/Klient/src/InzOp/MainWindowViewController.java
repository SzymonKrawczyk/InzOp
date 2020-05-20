/*
    Kontroler dla ekranu głównego

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie (bez funkcjonalności)
                |                       |
    01.05.2020  | Szymon Krawczyk       |   Stworzenie funkcjonalności:
                |                       |       shift+enter twoży nową linię
                |                       |       enter wysyła wiadomość jeżeli pole "Enter wysyła" jest zaznaczone
                |                       |       pokazanie limitu znaków
                |                       |       wczytywanie i zapisywanie ustawień konfiguracyjnych do pliku
                |                       |
    02.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Sprawdzanie wiadomości przed wysłaniem (zamiana znaków \n, ļ  na inne, dla bezpieczeństwa i poprawności działania)
                |                       |   Wysyłanie wiadomości UU
                |                       |

 */

package InzOp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;


import java.io.*;
import java.util.ArrayList;

public class MainWindowViewController extends ListView<MessageEntity> {

    private int maxMessageLength = 400;
    public static boolean clickable = true;


    @FXML
    public Label targetUserName;

    public static Label targetUserNameStatic;

    @FXML
    public ListView<ChatEntity> ChatEntitiesListView;

    public static ListView<ChatEntity> ChatEntitiesListViewStatic;

    @FXML
    public ListView<MessageEntity> MessageEntitiesListView;

    public static ListView<MessageEntity> MessageEntitiesListViewStatic;

    @FXML
    public Button logoutButton;

    @FXML
    public ChoiceBox<String> statusChoiceBox;

    @FXML
    public Label usernameMain;

    public static Label usernameMainStatic;

    @FXML
    public ImageView settingsButton;

    @FXML
    public TextArea currentMessage;

    @FXML
    public Button sendMessage;

    @FXML
    public CheckBox enterCheckbox;

    @FXML
    public Label currentMessageCharacterCount;


    public void initialize() {

        targetUserName.setText("");

        usernameMain.setText(Main.Username);

        ChatEntitiesListViewStatic = ChatEntitiesListView;


        usernameMainStatic = usernameMain;

        Main.syncChatList();

        MessageEntitiesListViewStatic = MessageEntitiesListView;

        Main.synchMessageList();

        MessageEntitiesListView.setCellFactory(new Callback<ListView<MessageEntity>, ListCell<MessageEntity>>() {
            @Override
            public ListCell<MessageEntity> call(ListView<MessageEntity> messageEntityListView) {
                return new MessageEntityCellFactory();
            }
        });

        MessageEntitiesListView.setSelectionModel(new NoSelectionModel<MessageEntity>());




        ChatEntitiesListView.setCellFactory(new Callback<ListView<ChatEntity>, ListCell<ChatEntity>>() {
            @Override
            public ListCell<ChatEntity> call(ListView<ChatEntity> employeeListView) {
                return new ChatEntityCellFactory();
            }
        });

        ChatEntitiesListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<ChatEntity>() {
                    @Override
                    public void changed(ObservableValue<? extends ChatEntity> observableValue, ChatEntity oldVal, ChatEntity newVal) {

                        if(newVal != null && newVal != Main.currentChatEntity) {

                            Main.currentChatEntity = newVal;
                            System.out.println("Current ChatEntity: " + Main.currentChatEntity.getName());

                            targetUserName.setText(Main.currentChatEntity.getName());
                            Main.setNewMsgForEntity(Main.currentChatEntity.getName(), false);
                            sendMessage.setDisable(!Main.currentChatEntity.isActive());
                            Main.syncChatList();
                            Main.serwerConnector.write("getChatļ" + Main.currentChatEntity.isGroup() + "ļ" + Main.currentChatEntity.getName());
                            Main.messageEntitesList = new ArrayList<>();
                            Main.synchMessageList();
                        }
                    }
                }
        );


        boolean sendEnter = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("userPreferences.txt")))) {
            String userPref1;
            if ((  userPref1 = reader.readLine()) != null) {
                if (userPref1.equals("true")) {
                    sendEnter = true;
                }
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.err.println("Nie ma pliku inicjalizacyjnego userPreferences.txt");;
        }

        enterCheckbox.setSelected(sendEnter);

        currentMessageCharacterCount.setTextFill(Main.PassFillColor);
        currentMessageCharacterCount.setText(currentMessage.getText().length() + "/" + maxMessageLength);
        sendMessage.setDisable(false);

        //usernameMain.setText(Main.Username);
        System.out.println(Main.Username);
        Main.Status = "Online";
        statusChoiceBox.getItems().addAll("Online", "Zajęty");
        statusChoiceBox.setValue(Main.Status);

        statusChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Main.Status = statusChoiceBox.getItems().get((Integer) t1);
                System.out.println("Status: " +  Main.Status);
                Main.serwerConnector.write("statusļ"+  Main.Status);
            }
        });


        if (Main.currentChatEntity != null) targetUserName.setText(Main.currentChatEntity.getName());

    }



    public void openSettings(MouseEvent mouseEvent) throws IOException {

        Parent SettingsView = FXMLLoader.load(getClass().getResource("SettingsView.fxml"));
        Main.window.setScene(new Scene(SettingsView));
        Main.window.show();

        //System.err.println("settingsd");
    }

    public void specialModifierWhenPressed (KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (event.isShiftDown()) {
                int position = currentMessage.getCaretPosition();
                String newMsg = currentMessage.getText().substring(0, position) + "\n" + currentMessage.getText().substring(position);
                currentMessage.setText(newMsg);
                currentMessage.positionCaret(position+1);
            } else if (enterCheckbox.isSelected()) {

                sendMessage(currentMessage.getText().trim());
            }
        }
    }


    public void checkMessage (KeyEvent event) {



        currentMessageCharacterCount.setText(currentMessage.getText().trim().length() + "/" + maxMessageLength);
        if (currentMessage.getText().length() > maxMessageLength) {
            currentMessageCharacterCount.setTextFill(Main.ErrorFillColor);
            sendMessage.setDisable(true);

            if (currentMessage.getText().length() > 1000 || currentMessage.getText().length() < 0) {
                currentMessage.setText("");
            }

        } else {
            currentMessageCharacterCount.setTextFill(Main.PassFillColor);
            sendMessage.setDisable(false);

        }

    }

    public void sendMessageButton (ActionEvent event) {
        if (!sendMessage.isDisabled()) sendMessage(currentMessage.getText().trim());
    }

    public void sendMessage (String msg) {
        if (msg.length() < 1) {

            System.err.println("Niepoprawna wielkość wiadomości: " + msg.length());
        } else {


            //zamiana znaków na inne, wysyłanie
            String newMsg = Main.normalizeString(msg);;
            try {

                if (Main.currentChatEntity != null) {
                    Main.serwerConnector.write("sendMessageļ" + Main.currentChatEntity.isGroup() + 'ļ' + Main.currentChatEntity.getName() + 'ļ' + newMsg);

                    currentMessage.setText("");
                } else {
                    throw new NullPointerException();
                }

            } catch (NullPointerException err) {
                //err.printStackTrace();
                System.err.println("Wybierz odbiorcę!");
            }

        }
    }

    public void saveSettings(ActionEvent event) {
        try (FileWriter writer = new FileWriter("userPreferences.txt");
             BufferedWriter bw = new BufferedWriter(writer)) {

            if (enterCheckbox.isSelected()) {
                bw.write("true");
            } else {
                bw.write("false");
            }
        } catch (IOException e1) { }
    }

    public void logOut(ActionEvent event) {
        Main.serwerConnector.write("logout");
    }

    public class NoSelectionModel<T> extends MultipleSelectionModel<T> {

        @Override
        public ObservableList<Integer> getSelectedIndices() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public ObservableList<T> getSelectedItems() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public void selectIndices(int index, int... indices) {
        }

        @Override
        public void selectAll() {
        }

        @Override
        public void selectFirst() {
        }

        @Override
        public void selectLast() {
        }

        @Override
        public void clearAndSelect(int index) {
        }

        @Override
        public void select(int index) {
        }

        @Override
        public void select(T obj) {
        }

        @Override
        public void clearSelection(int index) {
        }

        @Override
        public void clearSelection() {
        }

        @Override
        public boolean isSelected(int index) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void selectPrevious() {
        }

        @Override
        public void selectNext() {
        }
    }


}
