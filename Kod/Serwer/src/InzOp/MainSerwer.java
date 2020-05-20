/*
    Główna klasa serwera

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    17.04.2020  | Szymon Krawczyk       |   Połączenie z bazą danych i zapytania
                |                       |
    26.04.2020  | Szymon Krawczyk       |   Infrastruktury połączeń z klientami
                |                       |   Metoda sprawdzająca dane logowania
                |                       |
    01.05.2020  | Michał Kopałka        |   Metoda hashPassword
                |                       |
    01.05.2020  | Szymon Krawczyk       |   Lista ClientList
                |                       |
    02.05.2020  | Szymon Krawczyk       |   Zapisywanie wiadomości do bazy
                |                       |   Modyfikacja użytkownika w bazie - hasło
                |                       |   System do wysyłania wiadomości wybiórczego - do wszystkich, do wszystkich poza kimś, do jednego użykownika
                |                       |
    03.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Dodawanie grupy
                |                       |   Dodawanie użytkownika
                |                       |   System do wysyłania wiadomości wybiórczego - rozwinięcie
                |                       |   Debugowanie
                |                       |
    08.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Dużo bardziej efektywne zarządzanie listą członków grupy
                |                       |   Klient dostaje tylko grupy do których należy
                |                       |
    10.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |
    17.05.2020  | Szymon Krawczyk       |   Debugowanie
                |                       |   Pobieranie historii chatu
                |                       |   Usuwanie wiadomości
                |                       |


 */

package InzOp;

        import java.nio.charset.StandardCharsets;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.sql.*;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.time.LocalDateTime;
        import java.time.format.DateTimeFormatter;
        import java.util.ArrayList;
        import java.util.Date;

public class MainSerwer {

    private static Connection connect = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;
    private static String url = "jdbc:mysql://localhost:3306/?useLegacyDatetimeCode=false&serverTimezone=CET&useUnicode=true&characterEncoding=UTF-8";
    private static String user = "root", pswd = "";

    public static ArrayList<ChatEntity> ClientList;

    public static int currentMsgUUId = 0;
    public static int currentMsgUGId = 0;


    public static void main(String[] args) {

        try {
            ClientConnectorWrap clientConnectorWrap = new ClientConnectorWrap();
            clientConnectorWrap.start();

            connect = DriverManager.getConnection(url, user, pswd);
            statement = connect.createStatement();


            ClientList = new ArrayList<ChatEntity>();
            getAllEntitiesFromDB();

            for (ChatEntity temp : ClientList) {
                if (temp.isGroup()) loadGroupMembers(temp);
            }
            getCurrentMsgIds();

//ñļ
            //showDB();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /*public static void showDB(){
        try {
            connect = DriverManager.getConnection(url, user, pswd);
            statement = connect.createStatement();

            resultSet = statement.executeQuery("select * from chat.users;");
            System.out.println("Users: ");
            while (resultSet.next()) {
                String username = resultSet.getString(1);
                String password = resultSet.getString(2);
                boolean privilege = resultSet.getBoolean(3);
                boolean active = resultSet.getBoolean(4);
                System.out.println(username + " | " + password + " | " + privilege + " | " + active);
            }
            System.out.println();

            resultSet = statement.executeQuery("select * from chat.groups;");
            System.out.println("Groups: ");
            while (resultSet.next()) {
                String groupname = resultSet.getString(1);
                System.out.println(groupname);
            }
            System.out.println();

            resultSet = statement.executeQuery("select * from chat.membership;");
            System.out.println("Membership: ");
            while (resultSet.next()) {
                String id_user = resultSet.getString(2);
                String id_group = resultSet.getString(3);
                System.out.println(id_user + " < > " + id_group);
            }
            System.out.println();

            resultSet = statement.executeQuery("select * from chat.messagesuu;");
            System.out.println("MessegesUU: ");
            while (resultSet.next()) {
                String from = resultSet.getString(2);
                String to = resultSet.getString(3);
                String time = resultSet.getString(4);
                String message = resultSet.getString(5);
                System.out.println(from + " -> " + to + " | " + time + "\n\"" + message + "\"");
            }
            System.out.println();

            resultSet = statement.executeQuery("select * from chat.messagesug;");
            System.out.println("MessegesUG: ");
            while (resultSet.next()) {
                String from = resultSet.getString(2);
                String to = resultSet.getString(3);
                String time = resultSet.getString(4);
                String message = resultSet.getString(5);
                System.out.println(from + " -> " + to + " | " + time + "\n\"" + message + "\"");
            }
            System.out.println();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }*/

    public static int tryLogin(String username_, String password_) {
        try {
            resultSet = statement.executeQuery("select * from chat.users where chat.users.username='" + username_ +"' and chat.users.password='" + hashPassword(username_, password_) + "';");
            //System.out.println(resultSet);
            if (resultSet.next()) {
                if (!resultSet.getBoolean(4)) return 0; // not active
                if (resultSet.getBoolean(3)) return 2;  // admin
                return 1;   // normal user
            } else {
                return 0;   // no user
            }

        } catch (Exception err) {
            return 0;
        }

    }

    public static boolean isAdmin(String username_) {
        try {
            resultSet = statement.executeQuery("select * from chat.users where chat.users.username='" + username_ +"';");
            //System.out.println(resultSet);
            if (resultSet.next()) {
                return (resultSet.getBoolean(3));
            }
            return false;
        } catch (Exception err) {
            return false;
        }

    }

    public static boolean checkIfCorrectPassword(String username_, String password_) {
        try {
            resultSet = statement.executeQuery("select * from chat.users where chat.users.username=('" + username_ +"') and chat.users.password='" + hashPassword(username_, password_) + "';");
            //System.out.println(resultSet);
            return resultSet.next();

        } catch (Exception err) {
            return false;
        }
    }

    public static void changePassword(String name, String newPassword) {
        try {
            statement.executeUpdate("UPDATE chat.users SET chat.users.password = '" + hashPassword(name, newPassword) + "' WHERE chat.users.username = '" + name + "';");

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void changeActive(String name, String newActive) {
        try {
            int IntnewActive = 0;
            if (newActive.equals("true")) IntnewActive = 1;
            statement.executeUpdate("UPDATE chat.users SET chat.users.active = '" + IntnewActive + "' WHERE chat.users.username = '" + name + "';");

            ChatEntity temp = findEntityByName(name);
            temp.setActive(Boolean.parseBoolean(newActive));
            String msgU = "updateEntityļ"
                    + temp.getName() + "ļ"
                    + temp.getStatus() + "ļ"
                    + temp.isActive() + "ļ"
                    + "false";
            MainSerwer.sendToEveryoneExcept(msgU, temp.getName());

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void changePrivilege(String name, String newPrivilege) {
        try {
            int IntnewPrivilege = 0;
            if (newPrivilege.equals("true")) IntnewPrivilege = 1;
            statement.executeUpdate("UPDATE chat.users SET chat.users.privilege = '" + IntnewPrivilege + "' WHERE chat.users.username = '" + name + "';");

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void joinGroup(String name, String groupname) {
        try {
            resultSet = statement.executeQuery("select * from chat.membership where chat.membership.id_group = '" + groupname + "' and chat.membership.id_user = '" + name + "';");

            if (!resultSet.next()) {
                statement.executeUpdate("INSERT INTO chat.membership (chat.membership.id_membership, chat.membership.id_user, chat.membership.id_group) VALUES (NULL, '" + name + "', '" + groupname + "');");

                findEntityByName(groupname).getGroupMembers().add(findEntityByName(name));
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void leaveGroup(String name, String groupname) {
        try {
            resultSet = statement.executeQuery("select * from chat.membership where chat.membership.id_group = '" + groupname + "' and chat.membership.id_user = '" + name + "';");

            if (resultSet.next()) {
                statement.executeUpdate("DELETE FROM chat.membership WHERE chat.membership.id_user = '" + name + "'and chat.membership.id_group = '" + groupname + "';");

                findEntityByName(groupname).getGroupMembers().remove(findEntityByName(name));
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void deleteUser(String name) {
        try {

            if (checkIfUsernameExists(name)) {
                statement.executeUpdate("DELETE FROM chat.users WHERE chat.users.username = '" + name + "';");

                ChatEntity toDelete = findEntityByName(name);

                for (ChatEntity temp : ClientList) {

                    if (temp.isGroup()) {
                        temp.getGroupMembers().remove(toDelete);
                    }
                }
                ClientList.remove(toDelete);
                sendToEveryUser("deleteEntityļ" + name);
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void deleteGroup(String name) {
        try {

            if (checkIfGroupExists(name)) {
                statement.executeUpdate("DELETE FROM chat.groups WHERE chat.groups.groupname = '" + name + "';");

                ChatEntity toDelete = findEntityByName(name);
                ClientList.remove(toDelete);
                sendToEveryUser("deleteEntityļ" + name);
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static void getAllEntitiesFromDB() {
        try {

            resultSet = statement.executeQuery("select * from chat.users;");
            while (resultSet.next()) {
                String username = resultSet.getString(1);
                boolean active = resultSet.getBoolean(4);
                ClientList.add(new ChatEntity(username, false, "Offline", active, null));
            }

            resultSet = statement.executeQuery("select * from chat.groups;");
            while (resultSet.next()) {
                String groupname = resultSet.getString(1);
                ClientList.add(new ChatEntity(groupname, true, "Online", true, null));
            }


        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static void loadGroupMembers(ChatEntity group) {
        try {
            if (!group.isGroup()) throw new IllegalArgumentException();

            resultSet = statement.executeQuery("select * from chat.membership where chat.membership.id_group = '" + group.getName() + "';");

            while (resultSet.next()) {
                String username = resultSet.getString(2);
                ChatEntity member = findEntityByName(username);
                group.getGroupMembers().add(member);
                //System.out.println(group.getName() + " | " + member.getName());
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void addNewUUMessageToDB(int id, String from, String to, String msg) {
        try {
            String update = "INSERT INTO chat.messagesuu (chat.messagesuu.id_wUU, chat.messagesuu.fromUser, chat.messagesuu.toUser, chat.messagesuu.messageTime, chat.messagesuu.messageText) VALUES ("+id+", '" + from + "', '" + to + "', CURRENT_TIMESTAMP, '"+msg+"');";
            statement.executeUpdate(update);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void addNewUGMessageToDB(int id, String from, String to, String msg) {
        try {
            String update = "INSERT INTO chat.messagesug (chat.messagesug.id_wUG, chat.messagesug.fromUser, chat.messagesug.toGroup, chat.messagesug.messageTime, chat.messagesug.messageText) VALUES ("+id+", '" + from + "', '" + to + "', CURRENT_TIMESTAMP, '"+msg+"');";
            statement.executeUpdate(update);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static void showAllEntites() {
        for (ChatEntity x: ClientList) {
            System.out.println(x.getName() + " " + x.getStatus());
        }
    }

    public static String hashPassword (String username, String password) {

        String in = password.substring(0, 1) + username + password.substring(1);

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] haslo_w_bitach = digest.digest(in.getBytes(StandardCharsets.UTF_8));

            // z bitow na hexy
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : haslo_w_bitach) {
                stringBuilder.append(String.format("%02x", b));
            }
            //System.out.println(stringBuilder.toString());
            return stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {
            return "null";
        }
    }

    public static void sendToEveryUser(String msg) {
        for (ChatEntity temp : ClientList) {

            if (!temp.isGroup() && temp.getClientConnector() != null) {
                temp.getClientConnector().write(msg);
            }
        }
    }

    public static void sendToEveryoneExcept(String msg, String exception) {

        for (int i = 0; i < ClientList.size(); i++) {
            ChatEntity temp = ClientList.get(i);

            if (!temp.getName().equals(exception)) {
                if (temp.getClientConnector() != null) {
                    temp.getClientConnector().write(msg);
                }
            }
        }

    }

    public static void sendOnlyTo(String msg, String toUsername) {

        for (int i = 0; i < ClientList.size(); i++) {
            ChatEntity temp = ClientList.get(i);

            if (temp.getName().equals(toUsername)) {
                if (temp.getClientConnector() != null) {
                    temp.getClientConnector().write(msg);
                }
                break;
            }
        }

    }

    public static ChatEntity findEntityByName(String name) {
        for (int i = 0; i < ClientList.size(); i++) {
            ChatEntity temp = ClientList.get(i);

            if (temp.getName().equals(name)) {
                return temp;
            }
        }
        return null;
    }

    public static void sendToGroup(String msg, String groupname) {

        ArrayList<ChatEntity> groupMembers = getGroupMembers(groupname);

        for (ChatEntity groupMember : groupMembers) {
            ClientConnector temp = groupMember.getClientConnector();
            if (temp != null) {
                temp.write(msg);
            }
        }

    }

    public static ArrayList<ChatEntity> getGroupMembers(String groupname) {

        return findEntityByName(groupname).getGroupMembers();
    }

    public static void addNewUser (String name, String password, String privilege) {    //privilege - 0 / 1
        try {
            String query = "INSERT INTO chat.users (chat.users.username, chat.users.password, chat.users.privilege) VALUES ('" + name + "', '" + hashPassword(name,password) + "', '" + privilege + "');";
            statement.executeUpdate(query);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static boolean checkIfUsernameExists(String username_) {
        try {
            resultSet = statement.executeQuery("select * from chat.users where chat.users.username='" + username_ +"';");
            //System.out.println(resultSet);
            return resultSet.next();

        } catch (Exception err) {
            return false;
        }
    }

    public static boolean checkIfGroupExists(String groupname) {

        try {
            resultSet = statement.executeQuery("select * from chat.groups where chat.groups.groupname='" + groupname +"';");
            //System.out.println(resultSet);
            return resultSet.next();

        } catch (Exception err) {
            return false;
        }
    }

    public static void addNewGroup(String groupname) {
        try {
            String query = "INSERT INTO chat.groups (chat.groups.groupname) VALUES ('" + groupname + "');";
            statement.executeUpdate(query);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static ArrayList<MessageEntity> getUUChatHistory(String user1, String user2) {
        //

        try {

            ArrayList<MessageEntity> temp = new ArrayList<>();

            resultSet = statement.executeQuery("SELECT * FROM chat.messagesuu WHERE chat.messagesuu.fromUser = '"+user1+"' AND chat.messagesuu.toUser = '"+user2+"' OR chat.messagesuu.fromUser = '"+user2+"' AND chat.messagesuu.toUser = '"+user1+"';");

            //noinspection DuplicatedCode
            while (resultSet.next()) {
                //System.out.println("SELECT * FROM chat.messagesuu WHERE chat.messagesuu.fromUser = '"+user1 + user2);
                int id = resultSet.getInt(1);
                //System.out.println(id);
                String from = resultSet.getString(2);
                String to = resultSet.getString(3);
                String text = resultSet.getString(5);
                Timestamp time = resultSet.getTimestamp(4);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tempTime = time.toLocalDateTime();
                String date = dtf.format(tempTime);

                temp.add(new MessageEntity(id, from, to, text, date));
                //System.out.println(date);
            }
            return temp;

        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    public static ArrayList<MessageEntity> getUGChatHistory(String group) {
        try {

            ArrayList<MessageEntity> temp = new ArrayList<>();
            resultSet = statement.executeQuery("SELECT * FROM chat.messagesug WHERE chat.messagesug.toGroup = '"+group+"';");

            //noinspection DuplicatedCode
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String from = resultSet.getString(2);
                String to = resultSet.getString(3);
                String text = resultSet.getString(5);
                Timestamp time = resultSet.getTimestamp(4);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tempTime = time.toLocalDateTime();
                String date = dtf.format(tempTime);

                temp.add(new MessageEntity(id, from, to, text, date));
                //System.out.println(date);
            }
            return temp;

        } catch (Exception err) {
            return null;
        }
    }

    public static int getTimePassedFromMessage(MessageEntity msg_) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        long millis=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateInMs = sdf.parse(date);
            millis = dateInMs.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) ((millis - msg_.gettimestampConvertedToMilliSeconds())/3600000);
    }

    public static MessageEntity getMessageByIDU (int id_) {

        try {

            MessageEntity temp = null;

            resultSet = statement.executeQuery("SELECT * FROM chat.messagesuu WHERE chat.messagesuu.id_wUU = " + id_ + ";");

                //noinspection DuplicatedCode
            if (resultSet.next()) {
                String from = resultSet.getString(2);
                String to = resultSet.getString(3);
                String text = resultSet.getString(5);
                Timestamp time = resultSet.getTimestamp(4);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tempTime = time.toLocalDateTime();
                String date = dtf.format(tempTime);

                temp = new MessageEntity(id_, from, to, text, date);
            }
            return temp;

        } catch (Exception err) {

            err.printStackTrace();
            return null;
        }
    }

    public static MessageEntity getMessageByIDG (int id_) {

        try {

            MessageEntity temp = null;

            resultSet = statement.executeQuery("SELECT * FROM chat.messagesug WHERE chat.messagesug.id_wUG = " + id_ + ";");

                //noinspection DuplicatedCode
            if (resultSet.next()) {
                String from = resultSet.getString(2);
                String to = resultSet.getString(3);
                String text = resultSet.getString(5);
                Timestamp time = resultSet.getTimestamp(4);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tempTime = time.toLocalDateTime();
                String date = dtf.format(tempTime);

                temp = new MessageEntity(id_, from, to, text, date);
            }
            return temp;

        } catch (Exception err) {

            err.printStackTrace();
            return null;
        }
    }

    private static void getCurrentMsgIds() {

        try {

            resultSet = statement.executeQuery("SELECT * FROM chat.messagesuu ORDER BY chat.messagesuu.id_wUU DESC");
            if (resultSet.next()) {
                currentMsgUUId = resultSet.getInt(1);
            }
            resultSet = statement.executeQuery("SELECT * FROM chat.messagesug ORDER BY chat.messagesug.id_wUG DESC");
            if (resultSet.next()) {
                currentMsgUGId = resultSet.getInt(1);
            }

        } catch (Exception err) { }
    }

    public static void deleteMsgU (int id) {
        try {

                statement.executeUpdate("DELETE FROM chat.messagesuu WHERE chat.messagesuu.id_wUU = '" + id + "';");

                //send to users "msgdeleted"

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void deleteMsgG (int id) {
        try {

            statement.executeUpdate("DELETE FROM chat.messagesug WHERE chat.messagesug.id_wUG = '" + id + "';");

            //send to users "msgdeleted"

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
