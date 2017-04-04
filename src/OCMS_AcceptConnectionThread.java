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

                connect.close();
                router.run();
                this.stop();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
