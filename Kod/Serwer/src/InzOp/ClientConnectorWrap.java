/*
    Klasa-wrapper potrzebna, by oczekiwanie na klientów było w oddzielnym wątku

    Data        | Autor zmian           | Zmiany
    ------------|-----------------------|---------------------------------------------------
    26.04.2020  | Szymon Krawczyk       |   Stworzenie klasy
                |                       |

 */

package InzOp;

import java.net.ServerSocket;

public class ClientConnectorWrap extends Thread {

    public ServerSocket serverSocket;
    public int Port = 29090;

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(Port);

            System.out.println("Serwer otwarty.");

            try {
                while (true) {
                    new ClientConnector(serverSocket.accept()).start();
                }
            }
            finally {
                serverSocket.close();
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }

    }

}
