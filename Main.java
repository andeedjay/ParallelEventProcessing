import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AndreiStancu
 */

public class Main {
	
	static int noTasks;
	static BlockingQueue <Integer> primeResults;
	static BlockingQueue <Integer> factResults;
	static BlockingQueue <Integer> squareResults;
	static BlockingQueue <Integer> fibResults;
	
	static {
		primeResults = new LinkedBlockingQueue<>();
		factResults = new LinkedBlockingQueue<>();
		squareResults = new LinkedBlockingQueue<>();
		fibResults  = new LinkedBlockingQueue<>();
	}
	
	//functie ce primeste o colectie si o returneaza sortata
	public static List <Integer> sortCollection(Collection c, Comparator comp) {
		List <Integer> sortedList = new ArrayList<>(c);
		Collections.sort(sortedList, comp);
		return sortedList;
	} 
	
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Prea putine argumente");
			System.exit(0);
		}
		
		int noThreads = Runtime.getRuntime().availableProcessors();
		int buffSize = Integer.parseInt(args[0]);
		Buffer buff = new Buffer(buffSize);
		int noEvents = Integer.parseInt(args[1]);
		int noInputFiles = args.length - 2;
		noTasks = noEvents * noInputFiles;
		
		//cream un numar de producatori egal cu numarul de fisiere de input
		Thread[] producers = new Producer[noInputFiles];
		for(int i = 0; i < producers.length; i++) {
			producers[i] = new Producer(buff, args[i + 2], noEvents);
			producers[i].start();
		}

		/*cream un executor service de dimensiune egala cu 
		numarul de core-uri de pe masina fizica*/
		ExecutorService execService = Executors.newFixedThreadPool(noThreads);
		
		/*trimitem initial un numar de consumatori egal cu numarul
		 de evenimente din fiecare fisier*/
		for(int i = 0; i < noEvents; i++)
			execService.submit(new Consumer(buff, execService));
		
		/*producatorii trebuie sa se uneasca cu thread-ul main 
		pentru ca nu cumva acesta sa se termine inaintea producatorilor*/
		for(int i = 0; i < producers.length; i++)
			try {
				producers[i].join();
			} catch (InterruptedException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		
		//sortarea rezultatelor din fiecare lista crescator
		Comparator myComp = new MyComparator();
		ArrayList<List<Integer>> results = new ArrayList<>();
		results.add(Main.sortCollection(primeResults, myComp));
		results.add(Main.sortCollection(factResults, myComp));
		results.add(Main.sortCollection(squareResults, myComp));
		results.add(Main.sortCollection(fibResults, myComp));
		
		//si scrierea in fisierele de output
		String outputFiles[] = {"PRIME.out", "FACT.out", "SQUARE.out", "FIB.out"};
		for(int j = 0; j < outputFiles.length; j++) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFiles[j]));
				for(Integer i : results.get(j))
					bw.write(i.toString() + "\n");
				bw.close();
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}