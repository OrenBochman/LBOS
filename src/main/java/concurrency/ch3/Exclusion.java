package concurrency.ch3;

import java.util.concurrent.Semaphore;
/**
 * Exclusion is to use semaphores to create a critical section
 * @author oren
 *
 */
public class Exclusion extends Thread {

	public static void main(String ... args) throws InterruptedException{

		Semaphore myMutex = new Semaphore(1);
		int[] counter = new int[1];
		Exclusion r1=new Exclusion(1,counter,myMutex);
		Exclusion r2=new Exclusion(2,counter,myMutex);
		if(Math.random()>=0.5) {
			System.out.println("in thread 0 : 1 first");
			r1.start();
			r2.start();
		}else {
			System.out.println("in thread 0 : 2 first");

			r2.start();	
			r1.start();
		}
		
		r1.join();
		r2.join();
		
		myMutex.acquire();
		System.out.println("in thread 0 :" + counter[0]);
		myMutex.release();
	}
	
	private int[] counter;
	private Semaphore myMutex;
	private int id;

	public Exclusion(int id,int[] counter,Semaphore myMutex) {
		this.id=id;
		this.counter=counter;
		this.myMutex=myMutex;
	}

	@Override
	public void run() {
		try {
			this.myMutex.acquire(); //block if second
			//critical section start
			this.counter[0]++;
			System.out.println("in thread "+id+ " :" +counter[0]);
			this.myMutex.release();
			//critical section end
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
}
