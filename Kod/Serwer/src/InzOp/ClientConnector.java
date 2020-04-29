/*
    Klasa reprezentująca klienta w serwerze

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie klasy
                |                       |

 */

package InzOp;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static InzOp.MainSerwer.ClientList;

public class ClientConnector extends Thread {

    private BufferedReader Reader;
    private PrintWriter Writer;
    private String username;
    private Socket socket;

    ClientConnector (Socket sk) {
        socket = sk;
        username = "<NoName>";
        System.out.println("Nowy klient!");
    }


    @Override
    public void run () {
        try {
            Reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Writer = new PrintWriter(socket.getOutputStream(), true);

            String msg = "";
            boolean exit = false;
            while (!exit) {
                msg = Reader.readLine();
                System.out.println(username + ": " + msg);

                String[] tokens = msg.split("ļ");
                //System.out.println(tokens[0]);
                switch (tokens[0]) {
                    case "login":
                        //sprawdz czy dane poprawne i wyslij login true lub login false
                        if(MainSerwer.tryLogin(tokens[1], tokens[2])) {
                            Writer.println("loginļtrue");
                            username = tokens[1];
                            //dodaj to tablicy klientow
                            MainSerwer.ClientList.add(this);
                        } else {
                            Writer.println("loginļfalse");
                        }
                        break;
                }

            }
        } catch (Exception err) {
            err.printStackTrace();
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

}
