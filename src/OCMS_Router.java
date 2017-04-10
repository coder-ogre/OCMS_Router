/*
 * OCMS_Router
 * @author: Drew Misicko
 */

// plan: have clients connect through port 4446, and servers connect through port 3336.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

/*
 * router class to listen to clients and handle communication in parallel using threads
 * @author: Drew Misicko
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
    int port;
    int id;
    String ip;

    private OCMS_AcceptConnectionThread clientHandler;

    OCMS_Router_Admin master = OCMS_Router_Admin.getInstance();

    /*
     * initiates parameters for my router
     * @author Drew Misicko
     */
    public OCMS_Router(int id, int port, String ip) throws IOException
    {
        clientServer = new ServerSocket(port); // listens on port for doorbells for the clients
        this.port = port;
        this.id = id;
        this.ip = ip;
    }

    /*
     * Overrides run method so that the router accepts connections from the clients.
     * This NEEDS to be threaded, because if it is not, then only one connection can be listened to at a time.
     * I tried taking advice saying this didn't need to be threaded, and when I did, it did not work as it should have.
     * @author Drew Misicko
     */
    @Override
    public void run()
    {
        while (true) {
            //master.println("Router " + id + " listening");
            try {
                clientConnect = clientServer.accept();
                (new OCMS_AcceptConnectionThread(clientConnect, id, this)).start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     * sometimes other parts of the application need to know a router's port number it's listening on (especially the client)
     * When they need to, they just ask.
     * @author: Drew Misicko
     */
    public int getPort()
    {
        return port;
    }

    /*
     * Asks a router for its IP address
     * @author: Drew Misicko
     */
    public String getInetAddress()
    {
        
        // ATTEMPT 1 AT GETTING THE CORRECT IP
        //return clientServer.getInetAddress();
        //return clientServer.getLocalSocketAddress();
        //return clientConnect.getInetAddress();
//        try {
//            return NetworkInterface;
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;


        //ATTEMPT 2 AT GETTING THE CORRECT IP
//        Enumeration e = null;
//        try {
//            e = NetworkInterface.getNetworkInterfaces();
//        } catch (SocketException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        int ctr=0;
//
//        NetworkInterface n = (NetworkInterface)e.nextElement();
//        n = (NetworkInterface)e.nextElement();
//        n = (NetworkInterface)e.nextElement();
//        Enumeration ee = n.getInetAddresses();
//        InetAddress inet = (InetAddress) ee.nextElement();
//        inet = (InetAddress)ee.nextElement();
//        return inet.getHostAddress();
        
        //RESORTING TO THE HARDCODED METHOD (take IP as parameter in router_admin class)
        return ip;
    }
}





