package InzOp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class MessageEntityCellFactory extends ListCell<MessageEntity> {

    private HBox hbox = new HBox(4.0); // 8 points of gap between controls
    private VBox vbox = new VBox();
    private Label label_id = new Label();
    private Label label_from = new Label();
    private Label label_time = new Label();
    private TextArea textArea = new TextArea();
    //private Label textArea = new Label();

    private ImageView deleteImageView = new ImageView(); // initially empty

    private Button button = new Button(); //

    public MessageEntityCellFactory(){

        vbox.setAlignment(Pos.CENTER_LEFT);

        label_id.setWrapText(false);
        label_from.setWrapText(false);
        label_time.setWrapText(false);


        deleteImageView.setPreserveRatio(true);
        deleteImageView.setFitHeight(15);
        deleteImageView.setFitWidth(15);

        button = new Button("", deleteImageView);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(MainWindowViewController.clickable) {

                    //tutaj wywołać zapytanie o usunięcie
                    //System.out.println("Id wiadomosci: " + getItem().getId());
                    MainWindowViewController.clickable = false;
                    Main.serwerConnector.write("deleteMessageļ" + Main.currentChatEntity.isGroup() + 'ļ' + Main.currentChatEntity.getName() + 'ļ' + getItem().getId());
                }
            }
        });

        textArea.setDisable(true);
        textArea.setStyle("-fx-opacity: 1;");
        textArea.setWrapText(true);

        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

                String text = textArea.getText();
                int lines=2;
                int maxNumberOfCharactersInLine=0;
                int lineHeight = 18;

                for(int i=0; i<text.length(); i++) {

                    maxNumberOfCharactersInLine++;
                    if( maxNumberOfCharactersInLine >= 45 ) { lines++; maxNumberOfCharactersInLine=0; }
                    else if(text.charAt(i) == '\n') { lines++; maxNumberOfCharactersInLine=0; }
                }

                textArea.setPrefHeight( lineHeight*lines );
            }
        });



        hbox.getChildren().addAll(button, label_time, label_from);
        vbox.getChildren().addAll(hbox, textArea);


        HBox.setMargin(label_id,new Insets(0,4,0,4));


        setPrefWidth(USE_PREF_SIZE);
    }

    @Override
    protected void updateItem(MessageEntity item, boolean empty) {
        // required to ensure that cell displays properly
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null); // don't display anything
        }
        else {
            label_id.setText(   Integer.toString( item.getId() )   );
            label_from.setText( item.getFrom() );
            label_time.setText( item.getTimestamp() );

            deleteImageView.setImage(MessageEntity.imageDelete);

            button = item.button;

            textArea.setText( item.getMessageText() );

            setGraphic(vbox); // attach custom layout to ListView cell
        }
    }
}
