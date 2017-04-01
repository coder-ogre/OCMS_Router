/*
 * @authors Drew Misicko and Truc Chau
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/*
 * client class
 */
public class BasicClient
{
	private Socket connect;

	/*
	 * constructor for Basic Client, includes code to be executed upon instantiation
	 */
	public BasicClient() throws IOException
	{
		Socket connect = new Socket("127.0.0.1", 4446); // attempts to ring the bell of this socket address, 127.0.0.1:4446
		System.out.println("Welcome to 127.0.0.1! Please enter exactly 10 alphanumeric characters. "); // greets user
		Scanner scan = new Scanner(System.in); // client takes input from keyboard
		String message;
		message = scan.nextLine(); // stores input into message
		while(!(message.length() == 10)) // only accepts strings 10 characters in length
		{
			System.out.print("You've entered " + message.length() + " characters as input.\nMake sure your input is exactly 10 characters long, please retry entering. \n");
			message = scan.nextLine(); // tries to get input again
		}
		PrintWriter out = new PrintWriter(connect.getOutputStream(), true);
		out.println(message); // sends out message to server
		BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
		System.out.println("Your string reversed is " + in.readLine() + ".\n"); // gets the reversed message from the server

		connect.close();
		out.close();     // closes connections
		in.close();
	}


	/*
	 * main method
	 */
	public static void main(String args[]) throws IOException
	{
//		BasicClient client = null;
//		for(int numberOfClients = 0; numberOfClients < 10; numberOfClients++)
//		{
//			client = new BasicClient(); // calls constructor for a new client
//		}
//		System.out.println("The server will not handle more clients.");


		int ID=1; // The client's ID
		System.out.println(generateMessage(ID, 3, 'v', 't'));
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
	static String generateMessage(int ID, int destination, char data1, char data2)
	{
		// Creates checksum
		String checksum = Integer.toBinaryString((ID+destination+data1+data2)); // adds the two characters then converts to binary
		checksum = addLeadingZeros(checksum); // adds leading zeros
		checksum = invertBinary(checksum); // inverts binary
		int parseInt = Integer.parseInt(checksum, 2); // converts to an integer
		char checkSumCharacter = (char)parseInt; // converts to a character

//		 for debugging, prints out all binary values
//		System.out.println(addLeadingZeros(Integer.toBinaryString(ID)));
//		System.out.println(addLeadingZeros(Integer.toBinaryString(destination)));
//		System.out.println(addLeadingZeros(Integer.toBinaryString(data1)));
//		System.out.println(addLeadingZeros(Integer.toBinaryString(data2)));
//		System.out.println(checksum);

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
