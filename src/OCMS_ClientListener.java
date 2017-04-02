/*
 * OCMS: Olaf, Chau, Misicko, Schlesiger
 * OCMS Runner
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

	/*
	 * This class uses threads, and each thread process a message sent by the client
	 */
	public class OCMS_ClientListener extends Thread
	{
		private ServerSocket outSocket;
		private Socket connectOut;
		
		private Socket connect; // gets the same ServerSocket to listen for doorbells
		private int id;
		OCMS_Master_Runner master = OCMS_Master_Runner.getInstance();
		
		
		public OCMS_ClientListener(Socket connect, int id, String[] ipList)
		{
			this.connect = connect; // assigns the port for listening
			
			try {
				connectOut = new Socket(master.getIp(((id + 1)%4)), 4446);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // listens on port 4446 for doorbells for the clients
			
			
			this.id = id;
		}
		
		@Override
		public void run()
		{
			try {
				//DataOutputStream out2 = new DataOutputStream(connect.getOutputStream());
				PrintWriter out = new PrintWriter(connectOut.getOutputStream(), true);// sends out to client
				//DataInputStream in2 = new DataInputStream(connect.getInputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream())); // reads in from client
				//String message2 = in2.readLine();
				String message = in.readLine(); // reads message in from the client
				
				
				out.println(message); // sends to client's own server, to be sent out to another server
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}