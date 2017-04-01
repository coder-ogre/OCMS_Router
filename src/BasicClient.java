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

	static int generateChecksum(String s)
	{
		String hex_value = new String();
		// 'hex_value' will be used to store various hex values as a string
		int x, i, checksum = 0;
		// 'x' will be used for general purpose storage of integer values
		// 'i' is used for loops
		// 'checksum' will store the final checksum
		for (i = 0; i < s.length() - 2; i = i + 2)
		{
			x = (int) (s.charAt(i));
			hex_value = Integer.toHexString(x);
			x = (int) (s.charAt(i + 1));
			hex_value = hex_value + Integer.toHexString(x);
			// Extract two characters and get their hexadecimal ASCII values
			System.out.println(s.charAt(i) + "" + s.charAt(i + 1) + " : "
					+ hex_value);
			x = Integer.parseInt(hex_value, 16);
			// Convert the hex_value into int and store it
			checksum += x;
			// Add 'x' into 'checksum'
		}
		if (s.length() % 2 == 0)
		{
			// If number of characters is even, then repeat above loop's steps
			// one more time.
			x = (int) (s.charAt(i));
			hex_value = Integer.toHexString(x);
			x = (int) (s.charAt(i + 1));
			hex_value = hex_value + Integer.toHexString(x);
			System.out.println(s.charAt(i) + "" + s.charAt(i + 1) + " : "
					+ hex_value);
			x = Integer.parseInt(hex_value, 16);
		}
		else
		{
			// If number of characters is odd, last 2 digits will be 00.
			x = (int) (s.charAt(i));
			hex_value = "00" + Integer.toHexString(x);
			x = Integer.parseInt(hex_value, 16);
			System.out.println(s.charAt(i) + " : " + hex_value);
		}
		checksum += x;
		// Add the generated value of 'x' from the if-else case into 'checksum'
		hex_value = Integer.toHexString(checksum);
		// Convert into hexadecimal string
		if (hex_value.length() > 4)
		{
			// If a carry is generated, then we wrap the carry
			int carry = Integer.parseInt(("" + hex_value.charAt(0)), 16);
			// Get the value of the carry bit
			hex_value = hex_value.substring(1, 5);
			// Remove it from the string
			checksum = Integer.parseInt(hex_value, 16);
			// Convert it into an int
			checksum += carry;
			// Add it to the checksum
		}
		checksum = generateComplement(checksum);
		// Get the complement
		return checksum;
	}

	 static int generateComplement(int checksum)
	    {
	        // Generates 15's complement of a hexadecimal value
	        checksum = Integer.parseInt("FFFF", 16) - checksum;
	        return checksum;
	    }

	/*
	 * main method
	 */
	public static void main(String args[]) throws IOException
	{
		BasicClient client = null;
		for(int numberOfClients = 0; numberOfClients < 10; numberOfClients++)
		{
			client = new BasicClient(); // calls constructor for a new client
		}
		System.out.println("The server will not handle more clients.");


		int ID=1; // The client's ID
		System.out.println(generateMessage(ID, 3, 'a', 't'));
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
		String checksum = Integer.toBinaryString((ID+destination)); // adds the two characters then converts to binary
		checksum = addLeadingZeros(checksum); // adds leading zeros
		checksum = invertBinary(checksum); // inverts binary
		int parseInt = Integer.parseInt(checksum, 2); // converts to an integer
		char checkSumCharacter = (char)parseInt; // converts to a character

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
