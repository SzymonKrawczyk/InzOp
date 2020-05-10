/*
        Klasa opisujaca użytkownika/ grupę

        Data        | Autor zmian           | Zmiany
        ------------|-----------------------|---------------------------------------------------
        01.05.2020  | Szymon Krawczyk       |   Stworzenie
                    |                       |
        08.05.2020  | Szymon Krawczyk       |   Dodanie listy członków grupy, jeżeli obiekt jest grupy
                    |                       |

*/


package InzOp;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class ChatEntity {

    private String name;
    private boolean group;
    private String status;
    private boolean active;
    private ClientConnector clientConnector;
    private ArrayList<ChatEntity> groupMembers;

    public static Image imageOnline;
    public static Image imageBusy;
    public static Image imageOffline;

    public ChatEntity(String name, boolean group, String status, boolean active, ClientConnector clientConneector) {
        this.name = name;
        this.group = group;
        this.status = status;
        this.active = active;
        this.clientConnector = clientConneector;
        if (this.group) groupMembers = new ArrayList<>();
    }

    public void reset() {

        this.status = "Offline";
        this.clientConnector = null;
    }

    public ArrayList<ChatEntity> getGroupMembers() {
        return groupMembers;
    }

    public boolean isGroupMember(String user) {
        for (ChatEntity groupMember : groupMembers) {
            if (groupMember.getName().equals(user)) return true;
        }
        return false;
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

    public ClientConnector getClientConnector() {
        return clientConnector;
    }

    public void setClientConnector(ClientConnector clientConnector) {
        this.clientConnector = clientConnector;
    }
}
