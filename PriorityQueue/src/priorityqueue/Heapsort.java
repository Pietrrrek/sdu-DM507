package priorityqueue;

import java.util.Scanner;

/**
 * Program used to test the PQHeap implementation
 * Provided in project description
 * Modified to include DEBUG flag and functionality, and to fail on wrong sort
 * @author Rolf Fagerberg
 */
public class Heapsort { 
    private static final boolean DEBUG = false;
    
    public static void main(String[] args) {
	PQ pq = new PQHeap(1000);

	int n = 0;
	Scanner sc = new Scanner(System.in);
	while (sc.hasNextInt()) {
            int i = sc.nextInt();
            if (DEBUG) { System.out.println("Inserting "+i); }
	    pq.insert(new Element(i,null));
            if (DEBUG) { System.out.println(pq); }
	    n++;
        }
        
        int lastSeen = Integer.MIN_VALUE;
	while (n > 0){
            int min = pq.extractMin().key;
            if (lastSeen > min) { // fail on wrong sort
                System.out.println("== "+min);
                System.exit(1);
            }
	    System.out.println(min);
            lastSeen = min;
	    n--;
            if (DEBUG) { System.out.println(pq); }
	}
   }
}
