
import java.net.*;
import java.io.*;


public class ThreadControl extends Thread {

	
  private Socket actionSocket = null;
  private SharedState mySharedStateObject;
  private String myServerThreadName;
  //private double spaces;

   
  //Setup the thread
  	public ThreadControl(Socket actionSocket, String ServerThreadName, SharedState numOfSpaces) {
	
	  super(ServerThreadName);
	  this.actionSocket = actionSocket;
	  mySharedStateObject = numOfSpaces;
	  myServerThreadName = ServerThreadName;
	 
	}

  public void run() {
    try {
      System.out.println(myServerThreadName + " initialising.");
      PrintWriter out = new PrintWriter(actionSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(actionSocket.getInputStream()));
      String inputLine, outputLine;

      while ((inputLine = in.readLine()) != null) {
    	  
    	  // Get a lock first
    	  try { 
    		  mySharedStateObject.acquireLock();  
    		  outputLine = mySharedStateObject.processInput(myServerThreadName, inputLine);
    		  out.println(outputLine);
    		  mySharedStateObject.releaseLock();
    		  
    	  } 
    	  catch(InterruptedException e) {
    		  System.err.println("Failed to get lock when reading:"+e);
    	  }
      }
       out.close();
       in.close();
       actionSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}