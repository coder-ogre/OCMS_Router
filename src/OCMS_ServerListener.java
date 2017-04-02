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
import java.net.Socket;

	/*
	 * This class uses threads, and each thread process a message sent by the client
	 */
	public class OCMS_ServerListener extends Thread
	{
		private Socket connect; // gets the same ServerSocket to listen for doorbells
		private int id;
		OCMS_Master_Runner master = OCMS_Master_Runner.getInstance();
		
		public OCMS_ServerListener(Socket connect, int id, String[] ipList)
		{
			this.connect = connect; // assigns the port for listening
			this.id = id;
			
		}
		
		@Override
		public void run()
		{
			try {
				//DataOutputStream out2 = new DataOutputStream(connect.getOutputStream());
				PrintWriter out = new PrintWriter(connect.getOutputStream(), true);// sends out to client
				//DataInputStream in2 = new DataInputStream(connect.getInputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream())); // reads in from client
				//String message2 = in2.readLine();
				String message = in.readLine(); // reads message in from the client
				
				if(message.charAt(1) == (id + 48))
				{
					
				}
				else
				{
					
				}
				
				
				//out.println(backwardsMessage); // sends backwards message out to the client
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}