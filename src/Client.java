
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
 * @author: Truc Chau
 */
public class Client extends Thread
{
	String IPAddressLocalHost ="157.160.37.90";//local host
	private Socket connect;

	/**
	 * Constructor for Basic Client, includes code to be executed upon instantiation
	 */
	public Client() throws IOException, InterruptedException
	{


		/**
		 * The data of the content will increase by 1.
		 */
		char ID;
		String message;
		char randomDest;
		BufferedReader in;

		for(int dataContent = 1; dataContent<10; dataContent++) //Suppose each client sends out 10 messages
		{


			/**
			 * Making connection
			 * choose randomly port number to connect to
			 */
			connect = new Socket(IPAddressLocalHost,2345); // attempts to ring the bell of this socket address


			 //Receive the ID from the router, @author: Drew Misicko
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			ID = in.readLine().charAt(0);


			//Create random destination
			randomDest = (char)(createRandomDestination()+48);

			//Generate message to send out
			message = generateMessage(ID,randomDest,(char)(dataContent+48),(char)(dataContent+48));

			//Send out message
			PrintWriter out = new PrintWriter(connect.getOutputStream(), true);
			out.println(message); // sends out message to server

			//Print out the message and destination IP Address
			System.out.println("Sending message "+ message+" to "+randomDest);



			//Receive message from another client and do the check sum
			//If the checkChecksum function returns true, then print out the message
//			BufferedReader in1 = new BufferedReader(new InputStreamReader(connect.getInputStream()));
//			String receivedMessage = in1.readLine();
//
//			/**
//			 * When client receives the message, it will make sure the message is not corrupted.
//			 */
//			if(checkChecksum(receivedMessage)==true)
//			{
//				System.out.println("The received message is " + receivedMessage + ".\n");
//			}
//			else
//			{
//				System.out.println("The receive message has been corrupted");
//			}

			TimeUnit.SECONDS.sleep(2); //send out message every 2 seconds


			//System.out.println(dataContent);

				//Close connection
				connect.close();
				out.close();
				in.close();
				//in1.close();

		}

}

public static void main(String args[]) throws IOException, InterruptedException
{
//	for(int i=0; i<4; i++)
//	{
		/**
		 * Each client should keep sending out messages to random clients
		 */
		Client client1 = new Client(); // calls constructor for a new client


//	}

}

/**
 * @author: Truc Chau
 * @return The random destination
 */
public static int createRandomDestination()
{
		/**
		 * Create random destinations
		 * Pick any number from 2-4
		 */
		Random rand = new Random();

		return rand.nextInt((4-2)+1)+2;

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
//	System.out.println(charSum);
//	System.out.println(checksum);

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

//	 for debugging, prints out all binary values
//	System.out.println(addLeadingZeros(Integer.toBinaryString(ID)));
//	System.out.println(addLeadingZeros(Integer.toBinaryString(destination)));
//	System.out.println(addLeadingZeros(Integer.toBinaryString(data1)));
//	System.out.println(addLeadingZeros(Integer.toBinaryString(data2)));
//	System.out.println(checksum);

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


