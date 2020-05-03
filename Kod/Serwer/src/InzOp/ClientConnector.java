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

 */

package InzOp;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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




    private void reset() {

        username = "<NoName>";
        privilege = false;
        chatEntity = null;
    }

    private void addToChatEntity (String name) {

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

    private void removeFromChatEntity () {

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
                //System.out.println(tokens[0]);
                switch (tokens[0]) {
                    case "login": {
                        //sprawdz czy dane poprawne i wyslij odpowiedni komunikat
                        switch (MainSerwer.tryLogin(tokens[1], tokens[2])) {
                            case 2: // admin
                                write("loginļtrueļtrue");
                                privilege = true;
                                //dodaj to tablicy klientow
                                addToChatEntity(tokens[1]);
                                break;
                            case 1: // user
                                write("loginļtrueļfalse");
                                privilege = false;
                                //dodaj to tablicy klientow
                                addToChatEntity(tokens[1]);
                                break;
                            case 0: // error login
                                write("loginļfalse");
                                break;
                        }
                        /*if(MainSerwer.tryLogin(tokens[1], tokens[2])) {
                            Writer.println("loginļtrue");
                            username = tokens[1];
                            //dodaj to tablicy klientow
                            MainSerwer.ClientList.add(this);
                        } else {
                            Writer.println("loginļfalse");
                        }*/
                    }
                        break;
                    case "logout": {
                        //dodaj to tablicy klientow
                        //MainSerwer.ClientList.remove(this);
                        chatEntity.setStatus("Offline");

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

                                write("newEntityļ"
                                        + current.getName() + "ļ"
                                        + current.isGroup() + "ļ"
                                        + current.getStatus() + "ļ"
                                        + current.isActive() + "ļ"
                                        + "false"); //TODO
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

                        ChatEntity temp = MainSerwer.findEntityByName(tokens[1]);

                        if (temp != null) {
                            if (temp.isGroup()) {

                                MainSerwer.addNewUGMessageToDB(chatEntity.getName(), temp.getName(), tokens[2]);

                                MainSerwer.sendToGroup("updateEntityļ"
                                        + temp.getName() + "ļ"
                                        + temp.getStatus() + "ļ"
                                        + temp.isActive() + "ļ"
                                        + "true", temp.getName());

                                MainSerwer.sendToGroup("newMessageļ" + temp.getName() + 'ļ' + tokens[2]+ 'ļ' + chatEntity.getName(), temp.getName());
                            } else {
                                MainSerwer.addNewUUMessageToDB(chatEntity.getName(), temp.getName(), tokens[2]);

                                //wyslij do odpowiedniego odbiorcy
                                MainSerwer.sendOnlyTo("updateEntityļ"
                                        + chatEntity.getName() + "ļ"
                                        + chatEntity.getStatus() + "ļ"
                                        + chatEntity.isActive() + "ļ"
                                        + "true", temp.getName());
                                MainSerwer.sendOnlyTo("newMessageļ" + chatEntity.getName() + 'ļ' + tokens[2], temp.getName());
                            }
                        }


                    }
                        break;
                    case "addNewUser": {
                        if (!MainSerwer.checkIfUsernameExists(tokens[1])) {

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
                        if (!MainSerwer.checkIfGroupExists(tokens[1])) {

                            MainSerwer.addNewGroup(tokens[1]);
                            write("addNewGroupļtrue");

                            MainSerwer.ClientList.add(new ChatEntity(tokens[1], true, "Online", true, null));
                            MainSerwer.sendToEveryUser("newEntityļ" + tokens[1] + "ļ" + "true" + "ļ" + "Online" + "ļ" + "true" + "ļ" + "false");

                        } else {

                            write("addNewGroupļfalse");
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
