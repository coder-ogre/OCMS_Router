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
		BasicClient client = null;
		for(int numberOfClients = 0; numberOfClients < 10; numberOfClients++)
		{
			client = new BasicClient(); // calls constructor for a new client
		}
		System.out.println("The server will not handle more clients.");
	}
}
