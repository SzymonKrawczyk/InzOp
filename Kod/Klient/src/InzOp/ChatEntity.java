/*
        Klasa opisujaca użytkownika/ grupę

        Data        | Autor zmian           | Zmiany
        ------------|-----------------------|---------------------------------------------------
        01.05.2020  | Szymon Krawczyk       |   Stworzenie
                    |                       |
        02.05.2020  | Szymon Krawczyk       |   Dodanie imageOffline
                    |                       |

*/

package InzOp;

import javafx.scene.image.Image;

public class ChatEntity {

    private String name;
    private boolean group;
    private String status;
    private boolean active;
    private boolean newMsg;

    public static Image imageOnline;
    public static Image imageBusy;
    public static Image imageOffline;

    public ChatEntity(String name, boolean group, String status, boolean active, boolean newMsg) {
        this.name = name;
        this.group = group;
        this.status = status;
        this.active = active;
        this.newMsg = newMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isNewMsg() {
        return newMsg;
    }

    public void setNewMsg(boolean newMsg) {
        this.newMsg = newMsg;
    }
}
