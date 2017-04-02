/*
 * Olah, Chau, Misicko, and Schlesiger.
 * OCMS Router
 */

// plan: have clients connect through port 4446, and servers connect through port 3336.
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
	private int id;
	
	private Socket clientConnect; // the socket that will be open for communication with clients
	private ServerSocket clientServer; // listens for doorbells, and gives clientConnect a value if it hears one
	private OCMS_ClientListener clientHandler;
	
	private Socket serverConnect; // the socket that will be open for communication with other servers
	private ServerSocket serverServer; // listens for doorbells, and gives serverConnect a value if it hears one
	private OCMS_ServerListener serverHandler;
	
	OCMS_Master_Runner master = OCMS_Master_Runner.getInstance();
	
	/*
	 * this is the constructor for the java server
	 * it initiates the value for the ServerSocket and number of clients
	 */
	public OCMS_Router(int id, String[] ipList)
	{
		try {		
			this.id = id;
			
			clientServer = new ServerSocket(4446); // listens on port 4446 for doorbells for the clients
			
			
			serverServer = new ServerSocket(3336); // listens on port 3336 for doorbells for other servers
			
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
			
			//clientConnect = clientServer.accept(); // here, commands are repeated outside of the loop to allow the last client to be processed
			//serverConnect = serverServer.accept();
			//ipList.add(serverConnect.getInetAddress());
			//ipList.add(clientConnect.getInetAddress());
			
			// OCMS_Runner thread = new OCMS_Runner(connect);
			// thread.start();
			
			 //clientConnect.close();// after all clients have been accounted for, the connection is closed
			//ipList.remove(clientConnect.getInetAddress());
			
			clientConnect = clientServer.accept();
			clientHandler = new OCMS_ClientListener(clientConnect, id, master.getIpList());
			clientHandler.start();
			
			for(int i = 0; i < 2; i++)
			{
				serverConnect = serverServer.accept();
				serverHandler = new OCMS_ServerListener(serverConnect, id, master.getIpList());
				serverHandler.start();
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * @return number of connected clients
	 */
	public int getNumberOfClients()
	{
		return master.getIpList().length;
	}
}