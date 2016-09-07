import java.io.*;
import java.net.*;

public class ExitClient2 {
    public static void main(String[] args) throws IOException {

        // Set up the socket, in and out variables

        Socket ActionClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int SocketNumber = CarParkServer.ServerNum;
        String ActionServerName = "localhost";
        String ActionClientID = "ExitClient2";

        try {
            ActionClientSocket = new Socket(ActionServerName, SocketNumber);
            out = new PrintWriter(ActionClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ActionClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ SocketNumber);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        System.out.println("Initialised " + ActionClientID + " client and IO connections");
        
        // This is modified as it's the client that speaks first

        while (true) {
        	System.out.println("Would you like to leave the Car Park? (enter X)"); 
            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println(ActionClientID + " sending " + fromUser + " to ActionServer");
                out.println(fromUser);
            }
            fromServer = in.readLine();
            System.out.println(ActionClientID + " received " + fromServer + " from ActionServer");
        }
            
        
       // Tidy up - not really needed due to true condition in while loop
      //  out.close();
       // in.close();
       // stdIn.close();
       // ActionClientSocket.close();
    }
}
