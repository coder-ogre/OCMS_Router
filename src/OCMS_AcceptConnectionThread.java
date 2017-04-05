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

                String data = in.readLine(); // reads message in from the client
                System.out.println(data);


                int nextRouter;
                int leftConnectionId = (
                        (((id - 1)%master.getNumberOfClients()) == 0)?   4: ((id - 1)%master.getNumberOfClients()
                        ));
                int rightConnectionId = (
                        (((id + 1)%master.getNumberOfClients()) == 0)?   4: ((id - 1)%master.getNumberOfClients()
                        ));


                if(checkChecksum(data))
                {
                    if((int)data.charAt(2) == id)
                    {
                        nextRouter = id;
                        connectOut = new Socket(master.getIp(nextRouter), master.getRouter(id).getPort()); // attempts to ring the bell of this socket address
                        String source = "";
                        String destination = "";
                        String message = "" + data.charAt(3) + data.charAt(4);
                        switch(data.charAt(0))
                        {
                        case 1:
                            source = "IP: " + master.getRouter(1).getInetAddress() + ", Port: " + master.getRouter(1).getPort() + "(1)";
                            break;
                        case 2:
                            source = "IP: " + master.getRouter(2).getInetAddress() + ", Port: " + master.getRouter(2).getPort() + "(2)";
                            break;
                        case 3:
                            source = "IP: " + master.getRouter(3).getInetAddress() + ", Port: " + master.getRouter(3).getPort() + "(3)";
                            break;
                        case 4:
                            source = "IP: " + master.getRouter(4).getInetAddress() + ", Port: " + master.getRouter(4).getPort() + "(4)";
                            break;
                        }

                        switch(data.charAt(1))
                        {
                        case 1:
                            destination = "IP: " + master.getRouter(1).getInetAddress() + ", Port: " + master.getRouter(1).getPort() + "(1)";
                            break;
                        case 2:
                            destination = "IP: " + master.getRouter(2).getInetAddress() + ", Port: " + master.getRouter(2).getPort() + "(2)";
                            break;
                        case 3:
                            destination = "IP: " + master.getRouter(3).getInetAddress() + ", Port: " + master.getRouter(3).getPort() + "(3)";
                            break;
                        case 4:
                            destination = "IP: " + master.getRouter(4).getInetAddress() + ", Port: " + master.getRouter(4).getPort() + "(4)";
                            break;
                        }

                        master.println("Delivered Message: " + "\n\tSource: " + source + "\n\tDestination: " + "\n\tMessage: " + message);

                    }
                    else if((int)data.charAt(2) == leftConnectionId)
                    {
                        nextRouter = leftConnectionId;
                        connectOut = new Socket(master.getIp(nextRouter), master.getRouter(leftConnectionId).getPort()); // attempts to ring the bell of this socket address
                        PrintWriter out = new PrintWriter(connectOut.getOutputStream(), true);// sends out to client
                        out.println(data); // sends to client's own server, to be sent out to another server
                    }
                    else
                    {
                        nextRouter = rightConnectionId;
                        //master.println("Router number is " + rightConnectionId);
                        connectOut = new Socket(master.getIp(nextRouter), master.getRouter(rightConnectionId).getPort()); // attempts to ring the bell of this socket address
                        PrintWriter out = new PrintWriter(connectOut.getOutputStream(), true);// sends out to client
                        out.println(data); // sends to client's own server, to be sent out to another server
                    }




                }

              //  connect.close();

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
//            System.out.println(charSum);
//            System.out.println(checksum);

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


