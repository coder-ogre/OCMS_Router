/*
 * OCMS_Router_Admin
 * @author: Drew Misicko, Truc Chau
 */

import java.io.IOException;
import java.net.InetAddress;
/*
 * Sets up and keeps routers organized with port numbers and IP addresses kept track of
 * @author: Drew Misicko
 */
public class OCMS_Router_Admin {

    private static OCMS_Router_Admin master = new OCMS_Router_Admin();
    private static int amountOfClients;
    private static String[] ipList = {
                                        "127.0.0.1",
                                        "127.0.0.1",
                                        "127.0.0.1",
                                        "127.0.0.1"
                                      };
    private static OCMS_Router[] routerList;
    private static int[] ports;

    // required for singleton pattern, I only want one instance of one file in charge of the routers
    public static OCMS_Router_Admin getInstance()
    {
        return master;
    }


    public static void addIp(int index, String ip)
    {
        ipList[index - 1] = ip;
    }

    public static String getIp(int index)
    {
        return ipList[index - 1];
    }

    public static String[] getIpList()
    {
        return ipList;
    }

    public static int getPort(int index)
    {
        return ports[index - 1];
    }

    public static OCMS_Router getRouter(int routerNumber)
    {
        return routerList[routerNumber - 1];
    }

    public static void println(String message)
    {
        System.out.println(message);
    }

    public int getNumberOfClients()
    {
        return amountOfClients;
    }

    public static void main(String[] args) throws IOException
    {
        amountOfClients = 4;// change this depending on how many clients are being used (default of 4, 2 makes testing easier)
            // The amount of clients should be the only variable for the router that needs to be hardcoded or adjusted,
                                                                        // depending on testing setups

        // allocates memory for however many participants will there will be
        routerList = new OCMS_Router[amountOfClients];
        ports = new int[amountOfClients];

        // distributes port numbers for each client/router pair, should be 6 or less.
        // can be 2345, 3456, 4567, 5678, 6789, 7890.
        // 1234 isn't used because it's too close to 1024, which is a port that's used for important OS stuff... I guess
        int initialPort = 2345; // determines port numbers for each router/client pair
        for(int i = 0; i < amountOfClients; i++)
        {
            ports[i] = initialPort + (i * 1111);
        }
        
        
        for(int i = 0; i < amountOfClients; i++)
        {
            try {
                routerList[i] = new OCMS_Router(1 + i, ports[i], ipList[i]);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //ipList[i] =  ("" + routerList[i].getInetAddress()).substring(("" + routerList[i].getInetAddress()).lastIndexOf("/") + 1);
            routerList[i].start();
            //println("the port just added is " + getRouter(i + 1).getPort());
        }
        
        Client[] clients = new Client[amountOfClients];
        try {
            for(int i = 0; i < amountOfClients; i++)
            {
                clients[i] = new Client((char)(i + 1 + 48), ports[i], ipList[i]);
                //client.run();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while(true)
        {            // selects and runs a client at random
            int runningId = (int)((Math.random()*4)) + 1;
            clients[runningId - 1].run();
        }

    }

}




