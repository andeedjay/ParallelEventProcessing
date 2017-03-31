import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AndreiStancu
 */

public class Buffer {

	int N;
	BlockingQueue<Event> tasks;

	public Buffer(int N) {
		this.N = N;
		tasks = new ArrayBlockingQueue<>(N);
	}

	public Event getTask() {
		try {
			return tasks.take();
		} catch (InterruptedException ex) {
			Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public void addTask(Event e) {
		try {
			tasks.put(e);
		} catch (InterruptedException ex) {
			Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public String toString() {
		return tasks.toString();
	}
}