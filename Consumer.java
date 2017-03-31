import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AndreiStancu
 */

public class Consumer extends Thread {
	Buffer buff;
	static AtomicInteger executedTasks;
	ExecutorService execService;
	
	static {
		executedTasks = new AtomicInteger(0);
	}
	
	public Consumer(Buffer buff, ExecutorService execService) {
		this.buff = buff;
		this.execService = execService;
	}
	
	@Override
	public void run() {
		/*conditia de terminare a executiei consumatorilor: cand numarul de 
		task-uri procesate devine egal cu numarul total de task-uri*/
		if(executedTasks.get() < Main.noTasks) {
			//daca nu am terminat de executat task-urile scoatem un task din coada si il procesam
			executedTasks.getAndAdd(1);
			Event e = buff.getTask();
			switch(e.name) {
				case "PRIME":
					try {
						Main.primeResults.put(e.calculate());
					} catch (InterruptedException ex) {
						Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
					}
					break;
				case "FACT":
					try {
						Main.factResults.put(e.calculate());
					} catch (InterruptedException ex) {
						Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
					}
					break;
				case "SQUARE":
					try {
						Main.squareResults.put(e.calculate());
					}
					catch(InterruptedException ex) {
						Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
					}
					break;
				case "FIB":
					try {
						Main.fibResults.put(e.calculate());
					} catch (InterruptedException ex) {
						Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
					}
					break;
			}
			//si trimitem un nou consumator
			execService.submit(new Consumer(buff, execService));
		}
		else {
			//altfel inchidem executorul
			execService.shutdown();
		}
	}
}