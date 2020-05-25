/*
    Klasa reprezentująca klienta w serwerze

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie klasy
                |                       |
    01.05.2020  | Szymon Krawczyk       |   Dodanie powiązania z ChatEntity
                |                       |   Dodanie obsługi żądania pobrania listy chatEntities
                |                       |
    02.05.2020  | Szymon Krawczyk       |   Wiadomości UU
                |                       |   Modyfikacja własnego hasła użytkownika
                |                       |   Widoczność zmiany statusu użytkownika u innych
                |                       |
    03.05.2020  | Szymon Krawczyk       |   Wiadomości UG
                |                       |   Dodawanie grupy
                |                       |   Dodawanie użytkownika
                |                       |   Synchronizacja nowo dodanych użytkowników i grup
                |                       |   Debugowanie
                |                       |
    10.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Usuwanie/ modyfikowanie grupy/klienta
                |                       |
    17.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Pobieranie historii chatu
                |                       |   Zmiana systemu wiadomości
                |                       |

 */

package InzOp;


import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ClientConnector extends Thread {

    private BufferedReader Reader;
    private PrintWriter Writer;
    private Socket socket;

    private String username;
    private boolean privilege;
    private ChatEntity chatEntity;

    ClientConnector (Socket sk) {
        socket = sk;
        reset();
        System.out.println("Nowy klient!");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPrivilege() {
        return privilege;
    }

    public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }

    public ChatEntity getChatEntity() {
        return chatEntity;
    }

    public void setChatEntity(ChatEntity chatEntity) {
        this.chatEntity = chatEntity;
    }




    public void reset() {

        username = "<NoName>";
        privilege = false;
        chatEntity = null;
    }

    public void addToChatEntity (String name) {

        username = name;
        for (int i = 0; i < MainSerwer.ClientList.size(); i++) {

            ChatEntity currentobj = MainSerwer.ClientList.get(i);

            if ( !currentobj.isGroup() && currentobj.getName().equals(username)) {
                setChatEntity(currentobj);
                currentobj.setClientConnector(this);
                currentobj.setStatus("Online");

                String msgU = "updateEntityļ"
                        + chatEntity.getName() + "ļ"
                        + "Online" + "ļ"
                        + chatEntity.isActive() + "ļ"
                        + "false";
                MainSerwer.sendToEveryoneExcept(msgU, chatEntity.getName());
                break;
            }
        }
    }

    public void removeFromChatEntity () {

        chatEntity.reset();
        setChatEntity(null);
    }

    @Override
    public void run () {
        try {
            Reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Writer = new PrintWriter(socket.getOutputStream(), true);

            String msg = "";
            //noinspection InfiniteLoopStatement
            while (true) {
                msg = Reader.readLine();
                System.out.println(username + ": " + msg);

                String[] tokens = msg.split("ļ");
                switch (tokens[0]) {
                    case "login": {
                        //sprawdz czy dane poprawne i wyslij odpowiedni komunikat
                        switch (MainSerwer.tryLogin(tokens[1], tokens[2])) {
                            case 2: // admin
                                write("loginļtrueļtrue");
                                privilege = true;
                                //dodaj to tablicy klientow
                                addToChatEntity(tokens[1]);
                                ++MainSerwer.logInCounter;
                                chatEntity.setTimeWhenUserLogIn();
                                break;
                            case 1: // user
                                write("loginļtrueļfalse");
                                privilege = false;
                                //dodaj to tablicy klientow
                                addToChatEntity(tokens[1]);
                                ++MainSerwer.logInCounter;
                                chatEntity.setTimeWhenUserLogIn();
                                break;
                            case 0: // error login
                                write("loginļfalse");
                                break;
                        }
                    }
                        break;
                    case "logout": {
                        //MainSerwer.ClientList.remove(this);
                        chatEntity.setStatus("Offline");
                        chatEntity.setTimeWhenUserLogOut();

                        String msgU = "updateEntityļ"
                                + chatEntity.getName() + "ļ"
                                + "Offline" + "ļ"
                                + chatEntity.isActive() + "ļ"
                                + "false";
                        MainSerwer.sendToEveryoneExcept(msgU, chatEntity.getName());

                        write("logout");
                        removeFromChatEntity();
                        reset();
                    }
                        break;
                    case "getAllEntities": {
                        for (int i = 0; i < MainSerwer.ClientList.size(); i++) {

                            ChatEntity current = MainSerwer.ClientList.get(i);

                            if (!current.getName().equals(getUsername())) {

                                if (current.isGroup()) {
                                    if (current.isGroupMember(getUsername())) {
                                        write("newEntityļ"
                                                + current.getName() + "ļ"
                                                + current.isGroup() + "ļ"
                                                + current.getStatus() + "ļ"
                                                + current.isActive() + "ļ"
                                                + "false"); //TODO
                                    }
                                } else {

                                    write("newEntityļ"
                                            + current.getName() + "ļ"
                                            + current.isGroup() + "ļ"
                                            + current.getStatus() + "ļ"
                                            + current.isActive() + "ļ"
                                            + "false"); //TODO
                                }
                            }
                        }
                    }
                        break;
                    case "status":{

                        chatEntity.setStatus(tokens[1]);

                        String msgU = "updateEntityļ"
                                + chatEntity.getName() + "ļ"
                                + chatEntity.getStatus() + "ļ"
                                + chatEntity.isActive() + "ļ"
                                + "false";
                        MainSerwer.sendToEveryoneExcept(msgU, chatEntity.getName());
                    }
                        break;
                    case "changePassword": {
                        if (MainSerwer.checkIfCorrectPassword(getChatEntity().getName(), tokens[1])) {

                            MainSerwer.changePassword(getChatEntity().getName(), tokens[2]);
                            write("changePasswordļtrue");

                        } else {

                            write("changePasswordļfalse");
                        }
                    }
                        break;
                    case "sendMessage":{

                        ChatEntity temp = MainSerwer.findEntityByName(tokens[2]);
                        MainSerwer.messageCounter++;
                        if (temp != null) {

                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            String date = dtf.format(now);

                            if (Boolean.parseBoolean(tokens[1])) {

                                int id = ++MainSerwer.currentMsgUGId;
                                MainSerwer.addNewUGMessageToDB(id, chatEntity.getName(), temp.getName(), tokens[3]);

                                MainSerwer.sendToGroup("updateEntityļ"
                                        + temp.getName() + "ļ"
                                        + temp.getStatus() + "ļ"
                                        + temp.isActive() + "ļ"
                                        + "true", temp.getName());
                                MainSerwer.sendToGroup("newMessageļ" + temp.getName() + 'ļ' + tokens[3]  + 'ļ' + date + 'ļ' + privilege + 'ļ' + chatEntity.getName() + 'ļ' + id, temp.getName());
                            } else {

                                int id = ++MainSerwer.currentMsgUUId;
                                MainSerwer.addNewUUMessageToDB(id, chatEntity.getName(), temp.getName(), tokens[3]);

                                //wyslij do odpowiedniego odbiorcy
                                MainSerwer.sendOnlyTo("updateEntityļ"
                                        + chatEntity.getName() + "ļ"
                                        + chatEntity.getStatus() + "ļ"
                                        + chatEntity.isActive() + "ļ"
                                        + "true", temp.getName());
                                MainSerwer.sendOnlyTo("newMessageļ" + chatEntity.getName() + 'ļ' + tokens[3]  + 'ļ' + date + 'ļ' + privilege + 'ļ'  + chatEntity.getName() + 'ļ' + id, temp.getName());
                                write("newMessageļ" + temp.getName() + 'ļ' + tokens[3]  + 'ļ' + date + 'ļ' + "false" + 'ļ'  + chatEntity.getName() + 'ļ' + id);
                            }
                        }


                    }
                        break;
                    case "addNewUser": {
                        if (!MainSerwer.checkIfUsernameExists(tokens[1]) && !MainSerwer.checkIfGroupExists(tokens[1])) {

                            MainSerwer.addNewUser(tokens[1], tokens[2], tokens[3]);
                            write("addNewUserļtrue");

                            MainSerwer.ClientList.add(new ChatEntity(tokens[1], false, "Offline", true, null));
                            MainSerwer.sendToEveryUser("newEntityļ" + tokens[1] + "ļ" + "false" + "ļ" + "Offline" + "ļ" + "true" + "ļ" + "false");

                        } else {

                            write("addNewUserļfalse");
                        }
                    }
                        break;
                    case "addNewGroup": {
                        if (!MainSerwer.checkIfUsernameExists(tokens[1]) && !MainSerwer.checkIfGroupExists(tokens[1])) {

                            MainSerwer.addNewGroup(tokens[1]);
                            write("addNewGroupļtrue");

                            MainSerwer.ClientList.add(new ChatEntity(tokens[1], true, "Online", true, null));
                            MainSerwer.sendToEveryUser("allGroupInfoļ" + tokens[1]);

                        } else {

                            write("addNewGroupļfalse");
                        }
                    }
                        break;
                    case "editUser":{
                        String user = tokens[1];
                        switch (tokens[2]) {
                            case "password": MainSerwer.changePassword(user, tokens[3]); break;
                            case "admin": MainSerwer.changePrivilege(user, tokens[3]); break;
                            case "active": MainSerwer.changeActive(user, tokens[3]); break;
                            case "group":
                                if (tokens[3].equals("join")) {
                                    MainSerwer.joinGroup(user, tokens[4]);
                                } else {
                                    MainSerwer.leaveGroup(user, tokens[4]);
                                }
                                break;
                        }
                        /*ChatEntity userC = MainSerwer.findEntityByName(user);
                        if (userC.getClientConnector() != null) {

                            //MainSerwer.ClientList.remove(this);
                            chatEntity.setStatus("Offline");
                            chatEntity.setTimeWhenUserLogOut();

                            String msgU = "updateEntityļ"
                                    + chatEntity.getName() + "ļ"
                                    + "Offline" + "ļ"
                                    + chatEntity.isActive() + "ļ"
                                    + "false";
                            MainSerwer.sendToEveryoneExcept(msgU, chatEntity.getName());

                            write("logout");
                            removeFromChatEntity();
                            reset();
                        }*/

                    }
                        break;
                    case "getAllGroups": {
                        for (int i = 0; i < MainSerwer.ClientList.size(); i++) {

                            ChatEntity current = MainSerwer.ClientList.get(i);
                                if (current.isGroup()) {
                                        write("allGroupInfoļ" + current.getName());
                                }
                        }
                    }
                        break;
                    case "currentUserGroupInfo": {

                        for (ChatEntity entity : MainSerwer.ClientList) {
                            if (entity.isGroup()) {
                                ArrayList<ChatEntity> tempList = entity.getGroupMembers();

                                for (ChatEntity entity2 : tempList) {
                                    if (entity2.getName().equals(tokens[1])) {
                                        write("currentUserGroupInfoļ" + entity.getName());
                                    }
                                }
                            }
                        }
                    }
                        break;
                    case "getInfoAbout": {

                        ChatEntity temp = MainSerwer.findEntityByName(tokens[1]);
                        write("currentUserInfoļactiveļ" + temp.isActive());
                        write("currentUserInfoļprivilegeļ" + MainSerwer.isAdmin(temp.getName()));
                    }
                        break;
                    case "getInfoAboutGroup": {

                        ChatEntity temp = MainSerwer.findEntityByName(tokens[1]);
                        for (ChatEntity membeer : temp.getGroupMembers()) {
                            write("currentGroupInfoļ" + membeer.getName());
                        }
                    }
                        break;
                    case "deleteUser": {
                        MainSerwer.deleteUser(tokens[1]);
                    }
                        break;
                    case "deleteGroup": {
                        MainSerwer.deleteGroup(tokens[1]);
                    }
                        break;
                    case "getChat": {

                        ArrayList<MessageEntity> tempM;

                        if (Boolean.parseBoolean(tokens[1])) {

                            tempM = MainSerwer.getUGChatHistory(tokens[2]);

                        } else {

                            tempM = MainSerwer.getUUChatHistory(chatEntity.getName(), tokens[2]);
                        }

                        if (tempM != null) {

                            for (MessageEntity currentMsg : tempM) {

                                write("newMessageļ" + tokens[2] + 'ļ' + currentMsg.getMessageText() + 'ļ' + currentMsg.getTimestamp() + 'ļ' + "false" + 'ļ'  + currentMsg.getFrom() + 'ļ' + currentMsg.getId());
                            }
                        }
                    }
                        break;
                    case "deleteMessage": {

                        try {

                            if (privilege) {

                                if (Boolean.parseBoolean(tokens[1])) {

                                    MainSerwer.deleteMsgG(Integer.parseInt(tokens[3]));
                                    MainSerwer.sendToGroup("deleteMessageļ" + tokens[3], tokens[2]);

                                } else {

                                    MainSerwer.deleteMsgU(Integer.parseInt(tokens[3]));
                                    MainSerwer.sendOnlyTo("deleteMessageļ" + tokens[3], tokens[2]);
                                    write("deleteMessageļ" + tokens[3]);
                                }
                            } else {

                                MessageEntity msgTemp = null;
                                int minTime = 72;

                                if (Boolean.parseBoolean(tokens[1])) {

                                    msgTemp = MainSerwer.getMessageByIDG(Integer.parseInt(tokens[3]));

                                    if (msgTemp.getFrom().equals(chatEntity.getName()) && MainSerwer.getTimePassedFromMessage(msgTemp) >= minTime) {

                                        MainSerwer.deleteMsgG(Integer.parseInt(tokens[3]));
                                        MainSerwer.sendToGroup("deleteMessageļ" + tokens[3], tokens[2]);
                                    }

                                } else {

                                    msgTemp = MainSerwer.getMessageByIDU(Integer.parseInt(tokens[3]));

                                    if (msgTemp.getFrom().equals(chatEntity.getName()) && MainSerwer.getTimePassedFromMessage(msgTemp) >= minTime) {

                                        MainSerwer.deleteMsgU(Integer.parseInt(tokens[3]));
                                        MainSerwer.sendOnlyTo("deleteMessageļ" + tokens[3], tokens[2]);
                                        write("deleteMessageļ" + tokens[3]);
                                    }
                                }
                            }
                            write("deleteMessageConfirmation");

                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                        break;
                    case "getRaports": {

                        for(int i=0; i < MainSerwer.fileListStatistics.size(); i++) {

                            write("raportsInfoļ" + MainSerwer.fileListStatistics.get(i));
                        }
                    }
                        break;
                    case "getUserRaportFile": {

                        write("newFileļ" + tokens[1] + ".txt");
                        BufferedReader BR;

                        try {
                            BR = new BufferedReader(new FileReader("raports/users/" + tokens[1] + ".txt"));
                            String oneLine;

                            while ((oneLine = BR.readLine()) != null) {
                                write("fileContentļ" +  tokens[1] + ".txt" + "ļ" + oneLine + 'ñ');
                                System.out.println(oneLine);
                            }

                            BR.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                        break;
                    case "getStatisticsFile": {

                        write("newFileļ" + tokens[1]);
                        BufferedReader BR;

                        try {
                            BR = new BufferedReader(new FileReader("raports/statistics/" + tokens[1]));
                            String oneLine;

                            while ((oneLine = BR.readLine()) != null) {
                                write("fileContentļ" +  tokens[1] + "ļ" + oneLine + 'ñ');
                                System.out.println(oneLine);
                            }

                            BR.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                        break;

                    //inne polecenia
                }

            }
        } catch (Exception err) {
            //err.printStackTrace();
        }
        finally {
            try {
                System.out.println(username + " rozlaczony!");

                if (chatEntity != null) {

                    chatEntity.setStatus("Offline");

                    String msgU = "updateEntityļ"
                            + chatEntity.getName() + "ļ"
                            + "Offline" + "ļ"
                            + chatEntity.isActive() + "ļ"
                            + "false";
                    MainSerwer.sendToEveryoneExcept(msgU, chatEntity.getName());
                    removeFromChatEntity();
                    reset();
                }
                socket.close();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    public void write(String msg) {
        Writer.println(msg);
    }

}
