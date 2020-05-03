package InzOp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;

public class ChatEntityCellFactory extends ListCell<ChatEntity> {

   /* private HBox hbox = new HBox(4.0); // 8 points of gap between controls
    private Label label = new Label();
    private ImageView thumbImageView = new ImageView(); // initially empty


    public ChatEntityCellFactory() {


        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(100);

        label.setWrapText(false);
        label.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(label);

        HBox.setMargin(label,new Insets(0,4,0,4));

        thumbImageView.setPreserveRatio(true);
        thumbImageView.setFitHeight(100); // thumbnail 100 points tall
        thumbImageView.setFitWidth(100); // thumbnail 100 points tall
        hbox.getChildren().add(thumbImageView);

        setPrefWidth(USE_PREF_SIZE); // use preferred size for cell width
    }

    // called to configure each custom ListView cell
    @Override
    protected void updateItem(ChatEntity item, boolean empty) {
        // required to ensure that cell displays properly
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null); // don't display anything
        }
        else {

            if (item.getStatus().trim().equals("Online")) {

                thumbImageView.setImage(ChatEntity.imageOnline);
            } else if (item.getStatus().trim().equals("Zajęty")) {

                thumbImageView.setImage(ChatEntity.imageBusy);
            } else {

            }

            String name = "";
            if (item.isGroup()) {
                name += "_";
            }
            name += item.getName();
            label.setUnderline(item.isNewMsg());

            label.setText(name); // configure Label's text

            setGraphic(hbox); // attach custom layout to ListView cell
        }
    }*/
}