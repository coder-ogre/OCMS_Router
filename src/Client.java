/*
 * @authors Drew Misicko and Truc Chau
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
 * Client_ID = 1
 *
 */
public class Client extends Thread
{
	String IPAddressLocalHost ="127.0.0.1";//local host
	private Socket connect;
	/**
	 * Constructor for Basic Client, includes code to be executed upon instantiation
	 */
	public Client() throws IOException, InterruptedException
	{
		/**
		 * Making connection
		 */
		Socket connect = new Socket(IPAddressLocalHost, 4446); // attempts to ring the bell of this socket address, 127.0.0.1:4446
		System.out.println("Welcome to 127.0.0.1!");

		/**
		 * The data of the content will increase by 1.
		 */
		int dataContent = 1;
		char ID='1';
		String message;
		char randomDest;
		while(dataContent!=10) //each client sends.km out 10 messages
		{

			randomDest = (char)(createRandomDestination());
			message = generateMessage(ID,randomDest , (char)(dataContent),(char)(dataContent));
			//Send out message
			PrintWriter out = new PrintWriter(connect.getOutputStream(), true);
			out.println(message); // sends out message to server

			//Print out the message and destination IP Address
			System.out.println("Sending message "+ message+" to "+randomDest);

			//Receive message from another client and print it out
			BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			System.out.println("The received message is " + in.readLine() + ".\n");

			TimeUnit.SECONDS.sleep(2); //send out message every 2 seconds
			if(dataContent==10) //after sending out 10 messages, connection will be closed
			{
				connect.close();
				out.close();     // closes connections
				in.close();
			}


			dataContent++;
		}

	}

public static void main(String args[]) throws IOException, InterruptedException
{
	Client client = new Client(); // calls constructor for a new client
}

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


