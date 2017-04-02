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
 */
public class Client extends Thread
{	
	private Socket connect;
	/**
	 * Constructor for Basic Client, includes code to be executed upon instantiation
	 */
	public Client() throws IOException, InterruptedException
	{	
		//Suppose the client knows all the IP addresses of all routers
		//May need to hard code the IP address and write the random selection code?
		String IPAddressDestination ="127.0.0.1";
		
		
		Socket connect = new Socket(IPAddressDestination, 4446); // attempts to ring the bell of this socket address, 127.0.0.1:4446
		System.out.println("Welcome to 127.0.0.1!");
		//Scanner scan = new Scanner(System.in); // client takes input from keyboard
		//String message;
		//message = scan.nextLine(); // stores input into message
//		while(!(message.length() == 10)) // only accepts strings 10 characters in length
//		{
//			System.out.print("You've entered " + message.length() + " characters as input.\nMake sure your input is exactly 10 characters long, please retry entering. \n");
//			message = scan.nextLine(); // tries to get input again
//		}
		
		int dataContent = 1;
		while(dataContent!=10001)
		{
			TimeUnit.SECONDS.sleep(2);
			//Send out message
			PrintWriter out = new PrintWriter(connect.getOutputStream(), true); 
			out.println(dataContent); // sends out message to server
			
			//Print out the message and destination IP Address
			System.out.println("Sending data content is "+ dataContent+" to"+ IPAddressDestination);
			
			//Receive message back and print it out
			BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			System.out.println("The received message is " + in.readLine() + ".\n"); // gets the reversed message from the server
			
			if(dataContent==10000)
			{
				connect.close();   
				out.close();     // closes connections
				in.close();
			}
			dataContent++;
		}
		
	}
	
	/*
	 * main method
	 */
	public static void main(String args[]) throws IOException, InterruptedException
	{
		Client client = null;
		/**
		 * Assume we only have 4 clients
		 */
		
		int numberOfClient = 0;
			while(numberOfClient !=4) //can we change it to infinite number of clients???
			{
				client = new Client(); // calls constructor for a new client
				numberOfClient++;
			}
			
		
		
	}
}
