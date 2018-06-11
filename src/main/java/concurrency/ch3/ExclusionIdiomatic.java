package concurrency.ch3;

public class ExclusionIdiomatic extends Thread {

	public static void main(String ... args) throws InterruptedException{

		int[] counter = new int[1];
				
		System.out.println("in thread 0 :" + counter[0]);

		ExclusionIdiomatic r1=new ExclusionIdiomatic(1,counter);
		ExclusionIdiomatic r2=new ExclusionIdiomatic(2,counter);
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
		System.out.println("in thread 0 :" + counter[0]);
	}
	
	private int[] counter;
	private int id;

	public ExclusionIdiomatic(int id,int[] counter) {
		this.id=id;
		this.counter=counter;
	}

	@Override
	public void run() {

		synchronized (counter) {
			//critical section start
			this.counter[0]++;
			System.out.println("in thread "+id+ " :" +counter[0]);
			//critical section end			
		}
	}
}
