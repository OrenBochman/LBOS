package concurrency.ch3;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

@DisplayName("Little book of semaphores - Rendevous")
public class RendevousTest extends Thread  {

	Semaphore aDone;
	Semaphore bDone;
	Rendezvous r1;
	Rendezvous r2;
	List<String> output=new Vector<String>();
	
	@BeforeEach
	public void init() throws InterruptedException {
		output.clear();
		aDone = new Semaphore(0);
		bDone = new Semaphore(0);
		r1=new Rendezvous("a",aDone,bDone,output);
		r2=new Rendezvous("b",bDone,aDone,output);
	}
	//@Test
	@RepeatedTest(90)
	@DisplayName("Rendevous a before b")
	public void rendevousabTest() throws InterruptedException{

		if(Math.random()>0.5) {
			r1.start();
			r2.start();

		}else {
			r2.start();	
			r1.start();
		}
		r1.join();
		r2.join();

		assertTrue(output.size()==4, output.toString());
		assertTrue(output.indexOf("a2") > output.indexOf("a1"), output.toString());
		assertTrue(output.indexOf("a2") > output.indexOf("b1"), output.toString());
		assertTrue(output.indexOf("b2") > output.indexOf("a1"), output.toString());
		assertTrue(output.indexOf("b2") > output.indexOf("b1"), output.toString());
	}

}
