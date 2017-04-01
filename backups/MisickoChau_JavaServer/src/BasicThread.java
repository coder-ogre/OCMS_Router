/*
 * @authors Drew Misicko and Truc Chau
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
	public class BasicThread extends Thread
	{
		private Socket connect; // gets the same ServerSocket to listen for doorbells
		public BasicThread(Socket connect)
		{
			this.connect = connect;// assigns the port for listening
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
				String backwardsMessage = "";
				int stringIndex = 0; // begins reversal
				while(stringIndex < 10)
				{
					backwardsMessage += message.charAt(10 - stringIndex - 1); // creates backwards message
					++stringIndex;
				}
				//out2.writeChars(backwardsMessage);
				out.println(backwardsMessage); // sends backwards message out to the client
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}