import java.io.IOException;
import java.net.InetAddress;
/*
 * @author Drew & Jessica
 */
public class OCMS_Master_Runner {

    private static OCMS_Master_Runner master = new OCMS_Master_Runner();
    private static InetAddress[] ipList = new InetAddress[4];// hardcode this when you know the IP values, this will function as your "routing table"
    private static OCMS_Router[] routerList;
    private static int amountOfClients;

    //if routers are called 1, 2, 3, and 4 by their client id's...
    //private static int[] rightConnection4 = {2, 3, 4, 1};
    //private static int[] leftConnection4 = {4, 1, 2, 3};


    // UPDATE: Routing table is now dynamically made, depending on number of routers being used.  Code for figuring out routes is in the AcceptConnectionThread
    // specifically in AcceptConnectionThread, integers leftConnectionId and rightConnectionId determine the appropriate destination id's.

    /*
     * This is the "routing table".  This means that:
     *     1 is connected to 2 and 4.
     *  2 is connected to 3 and 1
     *  3 is connected to 4 and 2
     *  and 4 is connected to 1 and 3
     */



    public static OCMS_Master_Runner getInstance()
    {
        return master;
    }

    public static void addIp(int index, InetAddress ip)
    {
        ipList[index - 1] = ip;
    }

    public static InetAddress getIp(int index)
    {
        return ipList[index - 1];
    }

    /*public static int getRightConnection(int id, int amountOfClients)
    {
        return rightConnection[id];
    }

    public static int getLeftConnection(int id, int amountOfClients)
    {
        return leftConnection[id];
    }*/

    public static InetAddress[] getIpList()
    {
        return ipList;
    }

    public static OCMS_Router getRouter(int routerNumber)
    {
        return routerList[routerNumber - 1];
    }

    public void println(String message)
    {
        System.out.println(message);
    }

    public int getNumberOfClients()
    {
        return amountOfClients;
    }

    public static void main(String[] args) throws IOException
    {
        amountOfClients = 2;// change this depending on how many clients are being used (default of 4, 2 makes testing easier)


        int id = 2;
        int port = 3456;
        routerList = new OCMS_Router[4];
        OCMS_Router r1;
        /*OCMS_Router r2;
        OCMS_Router r3;
        OCMS_Router r4;*/

        r1 = new OCMS_Router(id, port);
        ipList[0] = r1.getInetAddress();
                routerList[0] = r1;

        /*
        r2 = new OCMS_Router(2, 3456);
        ipList[1] = r2.getInetAddress();
                routerList[1] = r2;

        r3 = new OCMS_Router(3, 4567);
        ipList[2] = r3.getInetAddress();
                routerList[2] = r3;

        r4 = new OCMS_Router(4, 5678);
        ipList[3] = r4.getInetAddress();
                routerList[3] = r4;
                */


        r1.run();
        /*
        try {
            Client client = new Client((char)(id + 48), port);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        /*
        r2.run();
        r3.run();
        r4.run();*/
    }
}
