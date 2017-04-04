/*
 * OCMS: Olaf, Chau, Misicko, Schlesiger
 * OCMS AcceptConnectionThread
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
    public class OCMS_AcceptConnectionThread extends Thread
    {
        private Socket connectOut;
        private Socket connect; // gets the same ServerSocket to listen for doorbells
        OCMS_Router router;

        private int id;
        OCMS_Master_Runner master = OCMS_Master_Runner.getInstance();

        public OCMS_AcceptConnectionThread(Socket connect, int id, OCMS_Router router)
        {
            this.connect = connect; // assigns the port for listening
            this.id = id;
            this.router = router;
        }

        @Override
        public void run()
        {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream())); // reads packet

                String message = in.readLine(); // reads message in from the client
                System.out.println(message);

                int nextRouter;
                
                if(checkChecksum(message))
                {
                	if((int)message.charAt(2) == id)
                    {
                        nextRouter = id;
                        connectOut = new Socket(master.getIp(nextRouter), 4446); // attempts to ring the bell of this socket address
                    }
                    else if((int)message.charAt(2) == master.getLeftConnection(id))
                    {
                        nextRouter = master.getLeftConnection(id);
                        connectOut = new Socket(master.getIp(nextRouter), 3336); // attempts to ring the bell of this socket address
                    }
                    else
                    {
                        nextRouter = master.getRightConnection(id);
                        connectOut = new Socket(master.getIp(nextRouter), 4446); // attempts to ring the bell of this socket address
                    }

                    PrintWriter out = new PrintWriter(connectOut.getOutputStream(), true);// sends out to client

                    out.println(message); // sends to client's own server, to be sent out to another server
                }

                connect.close();
                router.run();
                this.stop();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        /**
    	 * Ensures the checksum is good, if it isn't it returns false
    	 * @param message
    	 * @return
    	 * @author Jessica Schlesiger
    	 */
    	static boolean checkChecksum(String message)
    	{
    		int locationOfChecksum = 2;
    		String checksum = addLeadingZeros(Integer.toBinaryString(message.charAt(locationOfChecksum)));

    		StringBuilder msg = new StringBuilder(message);
    		message = msg.deleteCharAt(locationOfChecksum).toString(); // deletes the checksum character
    		char[] chars= new char[4];
    		for (int i=0;i<4;i++) // grabs each character individually
    		{
    			//System.out.println(addLeadingZeros(Integer.toBinaryString((char)message.charAt(i))));
    			chars[i] = message.charAt(i);
    		}
    		String charSum=Integer.toBinaryString(chars[0]+chars[1]+chars[2]+chars[3]);
    		if (charSum.length() > 8) // carries the 1
    		{
    			charSum = Integer.toBinaryString((chars[0]+chars[1]+chars[2]+chars[3])+1);
    			charSum = addLeadingZeros(charSum.substring(1,8));
    		}
//    		System.out.println(charSum);
//    		System.out.println(checksum);

    		for (int i=0;i<8;i++)
    		{
    			if (charSum.charAt(i) != '1')
    				if (checksum.charAt(i) != '1')
    					return false;
    		}
    		return true;
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
