/*
        Klasa-fabryka wizualizująca chatEntity w listView

        Data        | Autor zmian           | Zmiany
        ------------|-----------------------|---------------------------------------------------
        01.05.2020  | Szymon Krawczyk       |   Stworzenie
                    |                       |
        02.05.2020  | Szymon Krawczyk       |   Dodanie obrazka dla statusu Offline
                    |                       |

*/

package InzOp;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;

public class ChatEntityCellFactorySimple extends ListCell<ChatEntity> {

    private HBox hbox = new HBox(4.0); // 8 points of gap between controls
    private Label label = new Label();


    public ChatEntityCellFactorySimple() {


        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(30);

        label.setWrapText(false);
        label.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(label);

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


            String name = "";
            name += item.getName();

            label.setText(name); // configure Label's text

            setGraphic(hbox); // attach custom layout to ListView cell
        }
    }
}