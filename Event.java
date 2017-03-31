/**
 *
 * @author AndreiStancu
 */

public class Event {
	String name;
	int number;
	
	public Event(String name, int number) {
		this.name = name;
		this.number = number;
	}
	
	public boolean isPrime(int n) {
		if(n % 2 == 0)
			return false;
		for(int i = 3; i * i <= n; i += 2)
			if(n % i == 0)
				return false;
		return true;
	}
	
	public int fibonacci(int n) {
		int[] fibonacciSeries = new int[n + 1];
		fibonacciSeries[0] = 0;
		fibonacciSeries[1] = 1;
		for(int i = 2; i <= n; i++)
			fibonacciSeries[i] = fibonacciSeries[i - 1] + fibonacciSeries[i - 2];
		return fibonacciSeries[n];
	}
	
	/*functia de calculare algoritm in functie de numele lui, se va apela pe obiectul curent*/
	public int calculate() {
		switch(name) {
			case "PRIME":
				while (number >= 0)
					if (isPrime(number))
						return number;
					else
						number--;
			case "FACT":
				int i = 2;
				int p = 1;
				while((p = p * i) <= number)
					i++;
				return i - 1;
			case "SQUARE":
				return (int) Math.sqrt(number);
			case "FIB":
				int j = 1;
				while(fibonacci(j) <= number)
					j++;
				return j - 1;
			default:
				return 1;

		}
	}
}
