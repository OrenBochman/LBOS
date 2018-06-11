package concurrency.ch3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import concurrency.utils.Ref;

/**
 * Rendezvous is about using semaphores for signaling to get all threads to a logical point in the code 
 * before allowing any to continue. 
 * E.g. we split up an aggregate computation N ways and we need the computation to complete before moving 
 * to the next step in the algorithm.  
 * 
 * Originally I had a semaphore for printing but I researched java 
 * 
 * note: adding join at the end of main simplified the handling of the output.
 * note: using Ref<Integer> solves pass by value issues while avoid testing static issues.  
 * @author Oren Bochman
 *
 */
public class Barrier extends Thread {

	static final int THREAD_COUNT = 5;

	//note that vector is supposed to be thread safe collection.
	public static void main(String[] args) throws InterruptedException {
		Semaphore barrierSemaphore = new Semaphore(0);
		Semaphore counterMutex = new Semaphore(1);
		Thread[] threads= new Barrier[THREAD_COUNT];
		List<String> output=new ArrayList<>();
		Ref<Integer> counter= new Ref<>(0);

		for (int i = 0; i < THREAD_COUNT; i++) {
			threads[i]=new Barrier(i,barrierSemaphore,counterMutex,output,counter);
			threads[i].start();
		}
		
		for (Thread thread: threads) {
			thread.join();
		}
		System.out.println(output.toString());
	}

	private List<String> output;
	private int id;
	private Semaphore counterMutex;
	private Ref<Integer> counter;
	private Semaphore barrierSemaphore;


	Barrier(int id,Semaphore barrierSemaphore,Semaphore counterMutex,List<String> output,
			Ref<Integer> counter){
		this.id=id;
		this.counter=counter;
		this.barrierSemaphore=barrierSemaphore;
		this.counterMutex=counterMutex;
		this.output=output;
	}

	@Override
	public void run() {
		try {
			counterMutex.acquire();
			this.counter.Value++;
			counterMutex.release();
			if(this.counter.Value==Barrier.THREAD_COUNT) 
				this.barrierSemaphore.release();
			step1();
			//Turnstile
			this.barrierSemaphore.acquire();
			///////////////////////
			this.barrierSemaphore.release();
			step2();

		} catch (InterruptedException e) {
			System.out.println(this.id+" interrupted");
		}
	}

	public void step1(){
		synchronized(this.output){
			this.output.add("a"+this.id);//b for before
		}
	}

	public void step2() {
		synchronized(this.output){
			this.output.add("b"+this.id);//a for after
		}
	}
}