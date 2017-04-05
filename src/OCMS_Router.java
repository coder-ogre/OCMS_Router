/*
 * Olah, Chau, Misicko, and Schlesiger.
 * OCMS Router
 */

// plan: have clients connect through port 4446, and servers connect through port 3336.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * server class
 */
public class OCMS_Router extends Thread
{
    /*
     * NOTES: Dr. Girard says that I only need one listener.  When dialing to other routers, I'll need their IP addresses, which should be provided by the routing table
     * However, when listening, I don't need to have separate listeners.  The behavior that occurs when Router listens to client should be
     * the same as when it listens to another router.  It just takes the message and sends it to wherever the destination is specified.
     *
     * FURTHERMORE: The connection between routers is not constant.  A router is not ever connected to ANY other routers, UNTIL a client sends a message.
     * When a client sends a message, then the router makes a connection the destination router, and then sends the message to that router.
     * That is why routers have to be able to accept connections from other routers, too.
     * So yes, only one listener is needed.  This is not some kind of linked list router chain.
     */



    private Socket clientConnect; // the socket that will be open for communication with clients
    private ServerSocket clientServer; // listens for doorbells, and gives clientConnect a value if it hears one
    private Socket connectOut;
    int port;
    int id;

    private OCMS_AcceptConnectionThread clientHandler;

    OCMS_Master_Runner master = OCMS_Master_Runner.getInstance();

    /*
     * this is the constructor for the java server
     * it initiates the value for the ServerSocket and number of clients
     * @author Drew & Jessica
     */
    public OCMS_Router(int id, int port) throws IOException
    {
        clientServer = new ServerSocket(port); // listens on port for doorbells for the clients
        this.port = port;
        this.id = id;

    }

    @Override
    public void run()
    {
        while (true) {
            master.println("Router " + id + " listening...");
            try {
                clientConnect = clientServer.accept();
                (new OCMS_AcceptConnectionThread(clientConnect, id, this)).start();

                // sends to client the ID of the router, for the client to use it as their own ID
                //PrintWriter out = new PrintWriter(clientConnect.getOutputStream(), true);
                //char idChar = (char)(id + 48);
                //out.println(id);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }
    }

    public int getPort()
    {
        return port;
    }

    public InetAddress getInetAddress()
    {
        return clientServer.getInetAddress();
    }

    public void println(String message)
    {
        System.out.println(message);
    }

}


