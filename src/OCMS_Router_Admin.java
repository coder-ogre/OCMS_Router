import java.io.IOException;
import java.net.InetAddress;
/*
 * Sets up and keeps routers organized with port numbers and IP addresses kept track of
 * @author: Drew Misicko
 */
public class OCMS_Router_Admin {

    private volatile static OCMS_Router_Admin master;
    private static int amountOfClients;
    private static String[] ipList;
    private static OCMS_Router[] routerList;
    private static int[] ports;
    
    private OCMS_Router_Admin()
    {
    	// sometimes I put stuff here, then I remove it and put it in main.  It's hard to decide.
    }

    // required for singleton pattern, I only want one instance of one file in charge of the routers
    public static OCMS_Router_Admin getInstance()
    {
    	if(master == null) {
    		synchronized (OCMS_Router_Admin.class) 
    		{
    			if(master == null) 
    			{
    				master = new OCMS_Router_Admin();
    			}
    		}
            master = new OCMS_Router_Admin();
         }
         return master;
    }

    /*
     * adds an ip address
     */
    public static void addIp(int index, String ip)
    {
        ipList[index - 1] = ip;
    }

    /*
     * gets an ip address
     */
    public static String getIp(int index)
    {
        return ipList[index - 1];
    }

    /*
     * gets the array of ip addresses
     */
    public static String[] getIpList()
    {
        return ipList;
    }
    
    /*
     * sets a port number
     */
    public static void setPort(int index, int port)
    {
    	ports[index] = port;
    }
    
    /*
     * sets an ip address to the array of ip addresses
     */
    public static void setIp(int index, String ip)
    {
    	ipList[index] = ip;
    }
    
    /*
     * gets a port number
     */
    public static int getPort(int index)
    {
    	return ports[index];
    }
    
    /*
     * sets a router to the router array
     */
    public static void setRouter(int index, OCMS_Router router)
    {
    	routerList[index] = router;
    }
    
    /*
     * initiates the array of ip addresses with the specified allotment of memory
     */
    public static void initIpList(int size)
    {
    	if(ipList == null) {
    		ipList = new String[size];
    	}
    }
    
    /*
     * initiates the array of ports with the specified allotment of memory
     */
    public static void initPortList(int size)
    {
    	if(ports == null) {
    		ports = new int[size];
    	}
    }
    
    /*
     * initiates the array of routers with the specified allotment of memory
     */
    public static void initRouterList(int size)
    {
    	if(routerList == null) {
    		routerList = new OCMS_Router[size];
    	}
    }

    /*
     * gets a router from the router array
     */
    public static OCMS_Router getRouter(int routerNumber)
    {
        return routerList[routerNumber - 1];
    }

    /*
     * prints
     */
    public static void println(String message)
    {
        System.out.println(message);
    }

    /*
     * gets the number of clients
     */
    public int getNumberOfClients()
    {
        return amountOfClients;
    }
    
    /*
     * sets the number of clients
     */
    public void setNumberOfClients(int amount) 
    {
    	amountOfClients = amount;
    }
    
    
    
    // I'm keeping this stuff down there commented out.  I might want it later!
    
    public static void main(String[] args) throws IOException
    {
        amountOfClients = 4;// change this depending on how many clients are being used (default of 4, 2 makes testing easier)
            // The amount of clients should be the only variable for the router that needs to be hardcoded or adjusted,
                                                                        // depending on testing setups

        // allocates memory for however many participants will there will be
        ipList = new String[master.amountOfClients];
        routerList = new OCMS_Router[master.amountOfClients];
        ports = new int[master.amountOfClients];

        // distributes port numbers for each client/router pair, should be 6 or less.
        // can be 2345, 3456, 4567, 5678, 6789, 7890.
        // 1234 isn't used because it's too close to 1024, which is a port that's used for important OS stuff... I guess
        int initialPort = 2345; // determines port numbers for each router/client pair
        for(int i = 0; i < master.amountOfClients; i++)
        {
            ports[i] = initialPort + (i * 1111);
        }


        for(int i = 0; i < master.amountOfClients; i++)
        {
            try {
                routerList[i] = new OCMS_Router(1 + i, ports[i]);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ipList[i] =  ("" + routerList[i].getInetAddress()).substring(("" + routerList[i].getInetAddress()).lastIndexOf("/") + 1);
            routerList[i].start();
        }
    }

}
