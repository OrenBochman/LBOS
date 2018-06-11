package concurrency.ch3;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;


/**
 * Rendezvous is about using semaphores for signaling to get all threads to a logical point in the code 
 * before allowing any to continue. 
 * E.g. we split up an aggregate computation N ways and we need the computation to complete before moving 
 * to the next step in the algorithm.  
 * 
 * Originally I had a semaphore for printing but I researched java 
 * 
 * note: adding join at the end of main simplified the handling of the output.
 *  
 * @author Oren Bochman
 *
 */
public class Rendezvous extends Thread {

	//note that vector is supposed to be thread safe collection.
	public static void main(String[] args) throws InterruptedException {
		Semaphore aDone = new Semaphore(0);
		Semaphore bDone = new Semaphore(0);
		List<String> output=new Vector<String>();
		Rendezvous r1=new Rendezvous("a",aDone,bDone,output);
		Rendezvous r2=new Rendezvous("b",bDone,aDone,output);

		if(Math.random()>0.5) {
			r1.start();
			r2.start();

		}else {
			r2.start();	
			r1.start();
		}

		r1.join();
		r2.join();
		System.out.println(output.toString());

	}

	//making this static creates bugs during testing!?
	public List<String> output;
	private String letter;
	private Semaphore iArrived;
	private Semaphore uArrived;

	Rendezvous(String letter,Semaphore myMutex,Semaphore otherMutex,List<String> output){
		this.letter=letter;
		this.iArrived=myMutex;
		this.uArrived=otherMutex;
		this.output=output;
	}

	@Override
	public void run() {
		step1();
		try {
			iArrived.release(); //block
			uArrived.acquire(); //signal
		} catch (InterruptedException e) {
			System.out.println(letter+" interrupted");
		}
		step2();

	}

	public void step1(){
		synchronized(this.output){
			this.output.add(this.letter + "1");
		}
	}

	public void step2() {
		synchronized(this.output){
			this.output.add(this.letter + "2");
		}
	}
}
