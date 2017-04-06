import java.io.IOException;

/*
 * This class is a poor attempt to get the singleton states to be properly loaded, as
 * master.getNumberOfClients() when called froma anywhere else turns up as 0, and should not be.
 * Failed attempt, might as well go back to having the main method inside of the router_admin class
 * On the bright side, the code still works anyway, it just means the client needs the number of clients hard coded.
 * @author: Drew Misicko
 */
public class OCMS_Runner {
//	public static void main(String[] args) throws IOException
//    {
//		int amountOfClients = 4;
//		OCMS_Router_Admin master = OCMS_Router_Admin.getInstance();
//		
//        master.setNumberOfClients(amountOfClients);// change this depending on how many clients are being used (default of 4, 2 makes testing easier)
//            // The amount of clients should be the only variable for the router that needs to be hardcoded or adjusted,
//                                                                        // depending on testing setups
//        // allocates memory for however many participants will there will be
//        master.initIpList(amountOfClients);
//        master.initPortList(amountOfClients);
//        master.initRouterList(amountOfClients);
//
//        // distributes port numbers for each client/router pair, should be 6 or less.
//        // can be 2345, 3456, 4567, 5678, 6789, 7890.
//        // 1234 isn't used because it's too close to 1024, which is a port that's used for important OS stuff... I guess
//        int initialPort = 2345; // determines port numbers for each router/client pair
//        for(int i = 0; i < amountOfClients; i++)
//        {
//            master.setPort(i, (initialPort + (i * 1111)));
//        }
//
//
//        for(int i = 0; i < amountOfClients; i++)
//        {
//            try {
//                master.setRouter(i, new OCMS_Router(1 + i, master.getPort(i)));
//
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            master.setIp(i, ("" + master.getRouter(i + 1).getInetAddress()).substring(("" + master.getRouter(i + 1).getInetAddress()).lastIndexOf("/") + 1));
//            master.getRouter(i + 1).start();
//        }
//    }
}
