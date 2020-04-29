/*
    Główna klasa serwera

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    17.04.2020  | Szymon Krawczyk       |   Połączenie z bazą danych i zapytania
                |                       |
    26.04.2020  | Szymon Krawczyk       |   Infrastruktury połączeń z klientami
                |                       |   Metoda sprawdzająca dane logowania
                |                       |

 */

package InzOp;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.Statement;
        import java.util.ArrayList;

public class MainSerwer {

    private static Connection connect = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;
    private static String url = "jdbc:mysql://localhost:3306/?useLegacyDatetimeCode=false&serverTimezone=CET&useUnicode=true&characterEncoding=UTF-8";
    private static String user = "root", pswd = "";

    public static ArrayList<ClientConnector> ClientList;


    public static void main(String[] args) {

        try {
            ClientList = new ArrayList<ClientConnector>();
            ClientConnectorWrap clientConnectorWrap = new ClientConnectorWrap();
            clientConnectorWrap.start();
            //System.out.println("Po start");



            //Class.forName("com.mysql.jdbc.Driver");
            //Class.forName("com.mysql.cj.jdbc.Driver");




            connect = DriverManager.getConnection(url, user, pswd);
            statement = connect.createStatement();





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

    public static boolean tryLogin(String username_, String password_) {
        try {
            resultSet = statement.executeQuery("select * from chat.users where chat.users.username='" + username_ +"' and chat.users.password='" + password_ + "';");
            System.out.println(resultSet);
            //SELECT * FROM users WHERE username='user1' and password='user1!';
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception err) {
            return false;
        }

    }

}
