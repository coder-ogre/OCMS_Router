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
 * Client_Id = 1
 */

public class Client extends Thread
{	
	String IPAddressLocalHost ="10.0.0.9";//local host
	private Socket connect;
	/**
	 * Constructor for Basic Client, includes code to be executed upon instantiation
	 */
	public Client() throws IOException, InterruptedException
	{	
		/**
		 * Create random destinations
		 * Pick any number from 2-4
		 */
		Random rand = new Random();
		
		int Destination = rand.nextInt((4-2)+1)+2;
		
		/**
		 * Making connection
		 */
		Socket connect = new Socket(IPAddressLocalHost, 4446); // attempts to ring the bell of this socket address, 127.0.0.1:4446
		System.out.println("Welcome to 127.0.0.1!");
		
		/**
		 * The data of the content will increase by 1. 
		 */
		int dataContent = 1;
		String message; 
		while(dataContent!=10) //send out 10 messages
		{
			/*
			 * Create a message to send out
			 */
			message = IPAddressLocalHost; //Source
			message = message + String.valueOf(Destination); //Destination will be randomly chosen
			String destinationAddress = message; 
			//need to add checksum to the message later
			message = message + dataContent; 
			message = message +dataContent;
			
			
			//Send out message
			PrintWriter out = new PrintWriter(connect.getOutputStream(), true); 
			out.println(message); // sends out message to server
			
			//Print out the message and destination IP Address
			System.out.println("Sending message "+ message+" to "+destinationAddress);
			
			//Receive message from another client and print it out
			BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			System.out.println("The received message is " + in.readLine() + ".\n"); // gets the reversed message from the server
			
			TimeUnit.SECONDS.sleep(2);
			if(dataContent==10) //after sending out 10 messages, connection will be closed
			{
				connect.close();   
				out.close();     // closes connections
				in.close();
			}
			dataContent++;
		//	System.out.println("Content = "+dataContent);
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
