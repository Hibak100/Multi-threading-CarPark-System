import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.io.*;

public class SharedState {
	private int spacesLeft;
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers
	static ArrayList<String> CarsInGF = new ArrayList<String> (20); 
	static ArrayList<String> CarsInFF= new ArrayList<String> (20);
	Queue<String> queue = new LinkedList<String> ();
	Queue<String> queue2= new LinkedList<String> ();
	String car ="car";
	boolean q =true;

	SharedState( int sharedVariable)
	{
		spacesLeft= sharedVariable;
	}
	public synchronized void acquireLock() throws InterruptedException{
		Thread me = Thread.currentThread(); // get a ref to the current thread
		System.out.println(me.getName()+" is attempting to acquire a lock!");	
		++threadsWaiting;
		while (accessing) {  // while someone else is accessing or threadsWaiting > 0
			System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
			//wait for the lock to be released - see releaseLock() below
			wait();
		}
		// nobody has got a lock so get one
		--threadsWaiting;
		accessing = true;
		System.out.println(me.getName()+" got a lock!"); 
	}

	// Releases a lock to when a thread is finished

	public synchronized void releaseLock() {
		//release the lock and tell everyone
		accessing = false;
		notifyAll();
		Thread me = Thread.currentThread(); // get a ref to the current thread
		System.out.println(me.getName()+" released a lock!");
	}
	public synchronized String processInput(String myThreadName, String theInput) {
		System.out.println(myThreadName + " received "+ theInput);
		String theOutput = null;

		if (theInput.equalsIgnoreCase("Y")) {

			if (myThreadName.equals("EntranceThread1")|| myThreadName.equals("EntranceThread2")) {
				//if the queue is empty
				if(queue.size()==0 || queue2.size()==0){
					//if cars in GF have spaces
					if(CarsInGF.size()<20){
						//add car into car park
						CarsInGF.add(car);
						//reduce number of spaces available
						spacesLeft -=1;
						theOutput = "Action completed. The amount of spaces left = " 
							+ spacesLeft + ". CarPark=" + CarsInGF;
					}
					//if spaces in GF is full
					else if (CarsInGF.size()==20 && CarsInFF.size()<20){
						//add car into first floor
						CarsInFF.add(car);
						//reduce number of spaces available
						spacesLeft -=1;
						theOutput = "Action completed. The amount of spaces left = " 
							+ spacesLeft+ ". First floor=" + CarsInFF;
					}
				}
					//if there are no spaces left
					if(CarsInGF.size()==20 && CarsInFF.size()==20)
					{
						//queue from either entrance 1 or 2
						if(myThreadName.equals("EntranceThread1"))
						{
							//add car to queue
							queue.add(car);
							theOutput = "No spaces available. "
								+ "Entering entrance1 queue... " + queue;
						}
						else if(myThreadName.equals("EntranceThread2"))
						{
							//add car to queue
							queue2.add(car);
							theOutput = "No spaces available. "
								+ "Entering entrance2 queue... " + queue2;
						}
					}
				
			}
			//wrong client requesting
			if(myThreadName.equals("ExitThread1")||myThreadName.equals("ExitThread2")
					||myThreadName.equals("ExitThread3")||myThreadName.equals("ExitThread4")){
				theOutput="INPUT Y TO ENTER!!";
			}

		}
		else if(theInput.equalsIgnoreCase("X")){
 
			if (myThreadName.equals("ExitThread1")|| myThreadName.equals("ExitThread2")) 
			{
				//if there are cars in the ground floor
				if(spacesLeft<40 && spacesLeft>20)
				{
					//remove car from car park
					CarsInGF.remove(car);
					//add number of spaces available
					spacesLeft+=1;
					theOutput = "Action completed. The amount of spaces left = " 
						+ spacesLeft+ ". CarPark=" + CarsInGF;

					//if there are cars in the queue
					if(queue.size()!=0 || queue2.size() !=0)
					{
						if(q== true)
						{
							//remove car from queue
							queue.remove(car);
							//add it to car park
							CarsInGF.add(car);
							//reduce number of spaces available
							spacesLeft-=1;
							theOutput = "Entrance 1 Queue= " + queue+ ". CarPark=" + CarsInGF + 
							" Amount of spaces left=" + spacesLeft;
							q=false;
						}
						else
						{
							//remove car from queue
							queue2.remove(car);
							//add it to car park
							CarsInGF.add(car);
							//reduce number of spaces available
							spacesLeft-=1;
							theOutput = "Entrance 2 Queue= " + queue2+ ". CarPark=" + CarsInGF + 
							" Amount of spaces left=" + spacesLeft; 
							q=true;
						}
					} 
				}
				else
				{
					theOutput= "There are no cars left!";
				}
			}

			else if(myThreadName.equals("ExitThread3") || myThreadName.equals("ExitThread4"))
			{
				//if there are cars in car park 
				if(spacesLeft<20 && spacesLeft>0)
				{
					//remove car from car park
					CarsInFF.remove(car);
					System.out.println(CarsInFF);
					//add number of spaces available
					spacesLeft+=1;
					theOutput = "Action completed. The amount of spaces left = " 
						+ spacesLeft+ ". First floor=" + CarsInFF;

					//if there are cars in the queue
					if(queue.size()!=0 || queue2.size() !=0)
					{
						//if ground floor is full
						if(CarsInGF.size()==20)
						{
							//randomly choose which car from both queues to enter car park
							if(q== true)
							{
								//remove car from queue
								queue.remove(car);
								//add it to car park
								CarsInFF.add(car);
								//reduce number of spaces available
								spacesLeft-=1;
								theOutput = "Entrance 1 Queue= " + queue+ ". First floor=" + CarsInGF + 
								" Amount of spaces left=" + spacesLeft;
								q=false;
							}
							else
							{ 
								queue2.remove(car);
								//add it to car park
								CarsInFF.add(car);
								//reduce number of spaces available
								spacesLeft-=1;
								theOutput = "Entrance 2 Queue= " + queue+ ". First floor=" + CarsInFF + 
								" Amount of spaces left=" + spacesLeft;
								q=true;
							}
						}
					} 
				}
				//if there isn't any cars in the first floor
					else
					{
						theOutput= "First floor is empty";
					}
				
			}
			//wrong client requesting
			if(myThreadName.equals("EntranceThread1")|| myThreadName.equals("EntranceThread2")){
				theOutput="INPUT X TO EXIT!!";
			}
		}
		else 
		{
			theOutput= "Wrong input. Enter X to exit car park or Y to enter car park!";
				}
		return theOutput;
	}
}
