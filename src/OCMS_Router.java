/*
 * Olaf, Chau, Misicko, and Schlesiger.
 * OCMS Router
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * server class
 */
public class OCMS_Router extends Thread
{
	private Socket connect; // the socket that will be open for communication
	private ServerSocket server; // listens for doorbells, and gives connect a value if it hears one
	private static ArrayList ipList;
	
	/*
	 * this is the constructor for the java server
	 * it initiates the value for the ServerSocket and number of clients
	 */
	public OCMS_Router()
	{
		try {
			server = new ServerSocket(4446); // listens on port 4446 for doorbells
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * This overrides the default thread run method, so that when our Server runs with a thread,
	 * it processes our client's requests, and sends them to the thread that handles message reversal.
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		try {            // cycles through clients
			
			/*
			for(int numberOfClients = 0; numberOfClients < 10; ++numberOfClients)
			{
				connect = server.accept();// listens for doorbell
				OCMS_Runner thread = new OCMS_Runner(connect); // creates a new thread
				thread.start(); // starts the new thread
			}
			*/
			
			connect = server.accept(); // here, commands are repeated outside of the loop to allow the last client to be processed
			ipList.add(connect.getInetAddress());
			
			// OCMS_Runner thread = new OCMS_Runner(connect);
			// thread.start();
			
			 connect.close();// after all clients have been accounted for, the connection is closed
			ipList.remove(connect.getInetAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * @return number of connected clients
	 */
	public int getNumberOfClients()
	{
		return ipList.size();
	}
	
	/*
	 * This executes the Server
	 */
	public static void main(String args[]) throws IOException
	{
		OCMS_Router r = new OCMS_Router();
		r.start();
	}
}