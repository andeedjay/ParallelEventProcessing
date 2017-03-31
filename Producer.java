import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AndreiStancu
 */

public class Producer extends Thread {

	Buffer buff;
	String fileName;
	int noEvents;
	
	public Producer(Buffer buff, String fileName, int noEvents) {
		this.buff = buff;
		this.fileName = fileName;
		this.noEvents = noEvents;
	}
	
	@Override
	public synchronized void run() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			//citim fiecare eveniment din fisier
			for(int i = 0; i < noEvents; i++) {
				String line = br.readLine();
				String[] elements = line.split(",");
				Event e = new Event(elements[1], Integer.parseInt(elements[2]));
				try {
					Thread.sleep(Integer.parseInt(elements[0]));
				} catch (InterruptedException ex) {
					Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
				}
				//si il adaugam in buffer (coada sincronizata)
				buff.addTask(e);
			}
		br.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}	
}