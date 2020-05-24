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
import java.util.Date;

public class ChatEntity {

    private String name;
    private boolean group;
    private String status;
    private boolean active;
    private ClientConnector clientConnector;
    private ArrayList<ChatEntity> groupMembers;

    public long timeOnline;
    public long logInTime;

    //TODO
    private long online;
    private long loginTime;

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

        this.logInTime = 0;
        this.timeOnline = 0;
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

    // dodaje czas z aktulanej sesji do bufora i zeruje pomocniczy czs logowania, wywołać kiedy użytkownik się wyloguje ( zmieni status na offline )
    public void setTimeWhenUserLogOut(){

        if (this.logInTime != 0){

            Date date = new Date();
            timeOnline += date.getTime() - logInTime;
            logInTime = 0;
        }
    }

    // ustawia aktualną datę jako czas logowania, wywołać jeśli użytkownik się zaloguje ( zmieni status z offline na cokolwiek innego )
    public void setTimeWhenUserLogIn(){

        Date date = new Date();
        logInTime = date.getTime();
    }

    // zczytuje łączny czas ( to co było w buforze + czas aktualnej sesji ) i zeruje liczniki. wywołać w save() w UserRaportGenerator (już tam jest)
    public long getTimeOnlineForRaport(){

        setTimeWhenUserLogOut();

        if( clientConnector != null ) {

            setTimeWhenUserLogIn();
        }

        long bufor = timeOnline;
        timeOnline = 0;
        return bufor;
    }
}
