/*
 * Olah, Chau, Misicko, and Schlesiger.
 * OCMS Router
 */

// plan: have clients connect through port 4446, and servers connect through port 3336.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    private int id;

    private Socket clientConnect; // the socket that will be open for communication with clients
    private ServerSocket clientServer; // listens for doorbells, and gives clientConnect a value if it hears one
    private Socket connectOut;

    private OCMS_AcceptConnectionThread clientHandler;

    OCMS_Master_Runner master = OCMS_Master_Runner.getInstance();

    /*
     * this is the constructor for the java server
     * it initiates the value for the ServerSocket and number of clients
     */
    public OCMS_Router(int id, String[] ipList)
    {
        try {
            this.id = id;

            clientServer = new ServerSocket(4446); // listens on port 4446 for doorbells for the clients

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /*
     * This overrides the default thread run method, so that when our Server runs with a thread,
     * it processes our client's requests, and sends them to the thread that handles message reversal.
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run()
    {
        try {
            System.out.println("listening...");
            clientConnect = clientServer.accept();
            //clientHandler = new OCMS_AcceptConnectionThread(clientConnect, id, this);
            //clientHandler.start();





            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            BufferedReader in = new BufferedReader(new InputStreamReader(clientConnect.getInputStream())); // reads packet

            String message = in.readLine(); // reads message in from the client
            System.out.println(message);

            int nextRouter;

            if((int)message.charAt(2) == id)
            {
                nextRouter = id;
                connectOut = new Socket(master.getIp(nextRouter), 4446); // attempts to ring the bell of this socket address
            }
            else if((int)message.charAt(2) == master.getLeftConnection(id))
            {
                nextRouter = master.getLeftConnection(id);
                connectOut = new Socket(master.getIp(nextRouter), 3336); // attempts to ring the bell of this socket address
            }
            else
            {
                nextRouter = master.getRightConnection(id);
                connectOut = new Socket(master.getIp(nextRouter), 4446); // attempts to ring the bell of this socket address
            }

            PrintWriter out = new PrintWriter(connectOut.getOutputStream(), true);// sends out to client

            out.println(message); // sends to client's own server, to be sent out to another server

            //clientConnect.close();


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
