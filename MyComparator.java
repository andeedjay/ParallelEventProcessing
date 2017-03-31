import java.util.Comparator;

/**
 *
 * @author AndreiStancu
 */

public class MyComparator implements Comparator <Integer> {

	@Override
	public int compare(Integer o1, Integer o2) {
		return o1 - o2;
	}	
}