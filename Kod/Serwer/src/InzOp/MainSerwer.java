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

 */

package InzOp;

        import java.nio.charset.StandardCharsets;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.sql.*;
        import java.util.ArrayList;

public class MainSerwer {

    private static Connection connect = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;
    private static String url = "jdbc:mysql://localhost:3306/?useLegacyDatetimeCode=false&serverTimezone=CET&useUnicode=true&characterEncoding=UTF-8";
    private static String user = "root", pswd = "";

    public static ArrayList<ChatEntity> ClientList;


    public static void main(String[] args) {

        try {
            ClientConnectorWrap clientConnectorWrap = new ClientConnectorWrap();
            clientConnectorWrap.start();
            //System.out.println("Po start");



            //Class.forName("com.mysql.jdbc.Driver");
            //Class.forName("com.mysql.cj.jdbc.Driver");




            connect = DriverManager.getConnection(url, user, pswd);
            statement = connect.createStatement();


            ClientList = new ArrayList<ChatEntity>();
            getAllEntitiesFromDB();

            //showAllEntites();

            //System.out.println(hashPassword("Sekretariat1", "Qwerty123"));
            //System.out.println(hashPassword("user1", "user1!"));
           // System.out.println(hashPassword("user2", "user2!"));



            //statement.executeUpdate("INSERT INTO chat.`users` (`username`, `password`, `privilege`, `active`) VALUES ('user2', 'user2!', '0', '1')");

            //statement.executeUpdate("INSERT INTO chat.`groups` (`groupname`) VALUES ('HentaiAddicts')");

            //statement.executeUpdate("INSERT INTO chat.`membership` (`id_user`, `id_group`) VALUES ('user1', 'HentaiAddicts')");
            //.executeUpdate("INSERT INTO chat.`membership` (`id_user`, `id_group`) VALUES ('user2', 'HentaiAddicts')");

            //statement.executeUpdate("DELETE FROM chat.`messagesuu`");

            //statement.executeUpdate("INSERT INTO chat.`messagesuu` (`fromUser`, `toUser`,`messageText`) VALUES ('user1', 'user2', 'Odpisz jak będziesz, onii-chan~<3')");

            //statement.executeUpdate("DELETE FROM chat.`messagesug`");

            //statement.executeUpdate("INSERT INTO chat.`messagesug` (`fromUser`, `toGroup`, `messageText`) VALUES ('user1', 'HentaiAddicts', \"Witam na grupce! :3\")");
            //statement.executeUpdate("INSERT INTO chat.`messagesug` (`fromUser`, `toGroup`, `messageText`) VALUES ('user2', 'HentaiAddicts', \"O! No cześć :D\nCo tam??\")");
            //statement.executeUpdate("INSERT INTO chat.`messagesug` (`fromUser`, `toGroup`, `messageText`) VALUES ('user1', 'HentaiAddicts', 'Wszystko spoczko. Oglądamy razem jakieś hentai po pracy?')");
            //statement.executeUpdate("INSERT INTO chat.`messagesug` (`fromUser`, `toGroup`, `messageText`) VALUES ('user1', 'HentaiAddicts', 'Jak nie, jak tak :D')");
            //statement.executeUpdate("INSERT INTO chat.`messagesug` (`fromUser`, `toGroup`, `messageText`) VALUES ('user2', 'HentaiAddicts', 'ñļJak nie, jak tak :D')");
//ñļ
            //showDB();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void showDB(){
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
    }

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

    private static void getAllEntitiesFromDB() {
        try {
            resultSet = statement.executeQuery("select * from chat.groups;");
            while (resultSet.next()) {
                String groupname = resultSet.getString(1);
                ClientList.add(new ChatEntity(groupname, true, "Online", true, null));
            }

            resultSet = statement.executeQuery("select * from chat.users;");
            while (resultSet.next()) {
                String username = resultSet.getString(1);
                boolean active = resultSet.getBoolean(4);
                ClientList.add(new ChatEntity(username, false, "Offline", active, null));
            }


        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void addNewUUMessageToDB(String from, String to, String msg) {
        try {
            String update = "INSERT INTO chat.messagesuu (chat.messagesuu.id_wUU, chat.messagesuu.fromUser, chat.messagesuu.toUser, chat.messagesuu.messageTime, chat.messagesuu.messageText) VALUES (NULL, '" + from + "', '" + to + "', CURRENT_TIMESTAMP, '"+msg+"');";
            statement.executeUpdate(update);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void addNewUGMessageToDB(String from, String to, String msg) {
        try {
            String update = "INSERT INTO chat.messagesug (chat.messagesug.id_wUG, chat.messagesug.fromUser, chat.messagesug.toGroup, chat.messagesug.messageTime, chat.messagesug.messageText) VALUES (NULL, '" + from + "', '" + to + "', CURRENT_TIMESTAMP, '"+msg+"');";
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

    public static void sendToEveryone(String msg) {
        for (int i = 0; i < ClientList.size(); i++) {

            if (ClientList.get(i).getClientConnector() != null) {
                ClientList.get(i).getClientConnector().write(msg);
            }
        }
    }

    public static void sendToEveryUser(String msg) {
        for (int i = 0; i < ClientList.size(); i++) {

            ChatEntity temp = ClientList.get(i);

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

        //TODO jeśli grupa do do wszystkich!

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

        ArrayList<String> groupMembers = getGroupMembers(groupname);

        for (String groupMember : groupMembers) {
            ClientConnector temp = findEntityByName(groupMember).getClientConnector();
            if (temp != null) {
                temp.write(msg);
            }
        }

    }

    public static ArrayList<String> getGroupMembers(String groupname) {
        ArrayList<String> usernameList = new ArrayList<>();

        try {
            resultSet = statement.executeQuery("select * from chat.membership where chat.membership.id_group = '"+groupname+"';");
            while (resultSet.next()) {
                usernameList.add(resultSet.getString(2));
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return usernameList;
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
}
