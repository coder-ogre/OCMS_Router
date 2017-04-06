/*
 * @author: Truc Chau
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/*
 * Client class
 * Creates messages and sends them out to routers.  Receives messages from local router when assigned as the designated client.
 * @author Truc Chau
 */
public class Client extends Thread
{
<<<<<<< HEAD
    //String IPAddressLocalHost ="157.160.37.90";//local host
    String IPAddressLocalHost = "127.0.0.1";
    //String IPAddressLocalHost = "157.160.37.89";
=======

    //String IPAddressLocalHost ="157.160.37.90";//local host
    //String IPAddressLocalHost = "127.0.0.1";
    String IPAddressLocalHost = "157.160.37.89";

//    String IPAddressLocalHost ="157.160.37.90";//local host

>>>>>>> origin/master
    int port;
    private Socket connect;
    static OCMS_Router_Admin master = OCMS_Router_Admin.getInstance();

    /**
     * Constructor for Basic Client, includes code to be executed upon instantiation
     */
    public Client(char id, int port) throws IOException, InterruptedException
    {
        this.port = port;

        /**
         * The data of the content will increase by 1.
         */
        char ID = id;
        String message;
        char randomDest;
<<<<<<< HEAD
//        BufferedReader in;
=======
	
       // BufferedReader in;
>>>>>>> origin/master

        for(int dataContent = 1; dataContent<10; dataContent++) //Suppose each client sends out 10 messages
        {
            /**
             * Making connection
             * choose randomly port number to connect to
             */
            connect = new Socket(IPAddressLocalHost, port); // attempts to ring the bell of this socket address

<<<<<<< HEAD
=======

//            /**
//             * Receive the ID of the router
//             */
//            in = new BufferedReader(new InputStreamReader(connect.getInputStream())); // author Drew
//            ID = in.readLine().charAt(0);  // author Drew


>>>>>>> origin/master
            randomDest = (char)(createRandomDestination()+48);

            message = generateMessage(ID,randomDest,(char)(dataContent+48),(char)(dataContent+48));

            //Send out message
            PrintWriter out = new PrintWriter(connect.getOutputStream(), true);
            out.println(message); // sends out message to server

            //Receive message from another client and do the check sum
            //If the checkChecksum function returns true, then print out the message
            BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String receivedMessage = in.readLine();
<<<<<<< HEAD
            
          //Print out the message and destination IP Address
//
//            /**
//             * When client receives the message, it will make sure the message is not corrupted.
//             */
=======

            /**
             * When client receives the message, it will make sure the message is not corrupted.
             */

>>>>>>> origin/master
//            if(checkChecksum(receivedMessage)==true)
//            {
//                master.println("The received message is " + receivedMessage + ".\n");
//            }
//            else
//            {
//                System.out.println("The receive message has been corrupted");
//            }

            TimeUnit.SECONDS.sleep(2); //send out message every 2 seconds

<<<<<<< HEAD

=======
>>>>>>> origin/master
            //System.out.println(dataContent);

                //Close connection
                connect.close();
                out.close();
                in.close();

        }

}

public static void main(String args[]) throws IOException, InterruptedException
{
        /**
         * Each client should keep sending out messages to random clients
         */
<<<<<<< HEAD

         // Help with trying to run this command, please.  Just get null pointer exception. - Drew
        // Client client1 = new Client('2', master.getRouter(2).getPort()); // calls constructor for a new client

        // until I fix null pointer exception, it will need to be hardcoded: - Drew
        Client client1 = new Client('1', 2345);
        Client client2 = new Client('2', 3456);
        Client client3 = new Client('3', 4567);
        Client client4 = new Client('4', 5678);
=======

        Client client1 = new Client('2', 3456); // calls constructor for a new client

//        Client client1 = new Client('1', 2345); // calls constructor for a new client

//    }

}

/**
 * get the port number from one of the routers
 * return that number
 * make new connection to receive the message
 * @return
 */
public static int listeningRequest()
{

		return 0;

>>>>>>> origin/master
}

/**
 * @author: Truc Chau
 * @return The random destination
 */
public static int createRandomDestination()
{
<<<<<<< HEAD
    /**
     * Create random destinations
     * Pick any number from 1-4
     */
	int rand = (new Random()).nextInt((4-1)+1);
    return (rand == 0)? 4:rand;
=======
        /**
         * Create random destinations
         * Pick any number from 1-4
         */
        Random rand = new Random();


        return rand.nextInt((4-1)+1)+4;

//        return rand.nextInt((4-1)+1)+1;


>>>>>>> origin/master
}

/**
 * Ensures the checksum is good, if it isn't it returns false
 * @param message
 * @return
 * @author Jessica Schlesiger
 */
static boolean checkChecksum(String message)
{
    int locationOfChecksum = 2;
    String checksum = addLeadingZeros(Integer.toBinaryString(message.charAt(locationOfChecksum)));

    StringBuilder msg = new StringBuilder(message);
    message = msg.deleteCharAt(locationOfChecksum).toString(); // deletes the checksum character
    char[] chars= new char[4];
    for (int i=0;i<4;i++) // grabs each character individually
    {
        //System.out.println(addLeadingZeros(Integer.toBinaryString((char)message.charAt(i))));
        chars[i] = message.charAt(i);
    }
    String charSum=Integer.toBinaryString(chars[0]+chars[1]+chars[2]+chars[3]);
    if (charSum.length() > 8) // carries the 1
    {
        charSum = Integer.toBinaryString((chars[0]+chars[1]+chars[2]+chars[3])+1);
        charSum = addLeadingZeros(charSum.substring(1,8));
    }
//    System.out.println(charSum);
//    System.out.println(checksum);

    for (int i=0;i<8;i++)
    {
        if (charSum.charAt(i) != '1')
            if (checksum.charAt(i) != '1')
                return false;
    }
    return true;
}
/**
 * Generates the message to be sent to other clients
 * @param ID - the current client's ID
 * @param destination - the destination client's ID
 * @param data1 - Message it's sending
 * @param data2 - message it's sending
 * @return - complete string message it wants to send to server
 * @author Jessica Schlesiger
 */
static String generateMessage(char ID, char destination, char data1, char data2)
{
    // Creates checksum
    String checksum = Integer.toBinaryString((ID+destination+data1+data2)); // adds the two characters then converts to binary
    if (checksum.length()>8) // if we have a 9th 1, then it carries the 1
    {
        checksum = Integer.toBinaryString((ID+destination+data1+data2)+1);
        checksum= checksum.substring(1,8);
    }
    checksum = addLeadingZeros(checksum); // adds leading zeros
    checksum = invertBinary(checksum); // inverts binary
    int parseInt = Integer.parseInt(checksum, 2); // converts to an integer
    char checkSumCharacter = (char)parseInt; // converts to a character

//     for debugging, prints out all binary values
//    System.out.println(addLeadingZeros(Integer.toBinaryString(ID)));
//    System.out.println(addLeadingZeros(Integer.toBinaryString(destination)));
//    System.out.println(addLeadingZeros(Integer.toBinaryString(data1)));
//    System.out.println(addLeadingZeros(Integer.toBinaryString(data2)));
//    System.out.println(checksum);

    String message=""+ID+""+destination+checkSumCharacter+data1+data2; // assembles message
    return message;
}
/**
 * Replaces all 1's with 0's, and 0's with 1's
 * @param checksum binary string we want to swap
 * @return swapped binary string
 * @author Jessica Schlesiger
 */
static String invertBinary(String checksum)
{
    return checksum.replace('0', '2').replace('1', '0').replace('2', '1');
}
/**
 * Returns a binary string of a character
 * @param character we want to convert
 * @return binary representation of character
 * @author Jessica Schlesiger
 */
static String convertToBinary(char character)
{
    return Integer.toBinaryString(character);
}
/**
 * adds the leading zeros to our binary value
 * @param binaryString
 * @return the binary string with leading zeros
 * @author Jessica Schlesiger
 */
static String addLeadingZeros(String binaryString)
{
    int leadingZeros = 8-binaryString.length();
    String zeros="";
    for (int i=0;i<leadingZeros;i++)
    {
        zeros+='0';
    }
    return zeros+binaryString;
}
}


