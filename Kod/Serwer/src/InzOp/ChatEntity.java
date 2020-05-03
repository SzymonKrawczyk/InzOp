/*
        Klasa opisujaca użytkownika/ grupę

        Data        | Autor zmian           | Zmiany
        ------------|-----------------------|---------------------------------------------------
        01.05.2020  | Szymon Krawczyk       |   Stworzenie
                    |                       |

*/


package InzOp;

import javafx.scene.image.Image;

public class ChatEntity {

    private String name;
    private boolean group;
    private String status;
    private boolean active;
    private ClientConnector clientConnector;

    public static Image imageOnline;
    public static Image imageBusy;
    public static Image imageOffline;

    public ChatEntity(String name, boolean group, String status, boolean active, ClientConnector clientConneector) {
        this.name = name;
        this.group = group;
        this.status = status;
        this.active = active;
        this.clientConnector = clientConneector;
    }

    public void reset() {

        this.status = "Offline";
        this.clientConnector = null;
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
