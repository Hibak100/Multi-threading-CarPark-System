import java.net.*;
import java.io.*;
public class CarParkServer {
	public static int ServerNum=4535;
  public static void main(String[] args) throws IOException {

	ServerSocket CarParkServerSocket = null;
    boolean listening = true;
    String ServerName = "Server";
    int spaces = 40;
    
    //Create the shared object in the global scope...
    SharedState numOfSpaces = new SharedState (spaces);
 
    
    // Make the server socket
    try {
      CarParkServerSocket = new ServerSocket(ServerNum);
    }catch (IOException e) {
      System.err.println("Could not start " +ServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(ServerName + " started");
	System.out.println("Waiting...");

	while (listening){
	      new ThreadControl(CarParkServerSocket.accept(), "EntranceThread1", numOfSpaces).start();	
	      new ThreadControl(CarParkServerSocket.accept(), "EntranceThread2", numOfSpaces).start();
	 	  new ThreadControl(CarParkServerSocket.accept(), "ExitThread1", numOfSpaces).start();
	      new ThreadControl(CarParkServerSocket.accept(), "ExitThread2", numOfSpaces).start();
	      new ThreadControl(CarParkServerSocket.accept(), "ExitThread3", numOfSpaces).start();
	      new ThreadControl(CarParkServerSocket.accept(), "ExitThread4", numOfSpaces).start();
	      System.out.println("New " + ServerName + " thread started.");
	    }
    CarParkServerSocket.close();
  }
}