//package hw4;

/*
 * author: Helena
 * version: 1
 * This class is used to sort array by using batcher's bitonic sort 
 * and calculate how many arrays could be sorted by using 1 thread/
 * 2 thread/4 thread/ 8 thread/ 16 thread when the size of array is 
 * up to 1<<22
 */
//import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeoutException;

public class hw4 {
	public static final int N = 1<<22;                  // the final size of array
	public static final int THREAD_MAX = 16;			// the max size of thread
	public static int TIME_ALLOWED = 10;                // seconds  
	
	public static void main(String[] args) throws BrokenBarrierException, TimeoutException, InterruptedException {
		// filled in array
		Random rand = new Random();
		int[] array = new int[N];
		for (int i = 0; i < N; i++)
			array[i] = rand.nextInt(100);
		//System.out.println("Original array: " + Arrays.toString(array));
		
		// process thread		
		for (int j = 1; j <= THREAD_MAX; j *= 2) {
			// calculate throughput
			long start = System.currentTimeMillis();
			int work = 0;
			while (System.currentTimeMillis() < (start + TIME_ALLOWED * 1000)) {
				CyclicBarrier barrier  = new CyclicBarrier(j);	
				Thread[] threads = new Thread[j];
				for(int i = 0; i < j; i++) {				
					threads[i] = new Thread(new BitonicSortSynchronized(array, N, N/j, i, barrier));
					threads[i].start();
				}
				// join
				for(Thread thread: threads) {
		        	thread.join();
		        }
				//System.out.println(Arrays.toString(array));								
										
				// check
				if (!isSorted(array))
					System.out.println("failed");
				work++;
			}
			// print
			System.out.println("sorted " + work + " arrays (each: " + N + " int) in " 
			                   + TIME_ALLOWED + " seconds with " + j + " threads");           
		}
					
	}
	
	/*
	 * This method is used to check if the array is sorted or not
	 */
	public static boolean isSorted(int[] ult) {
		if (ult == null)
			return true;
		int last = ult[0];
		for (int i = 1; i < ult.length; i++) {
			if (ult[i] < last) {
				//System.out.println(last + ":" + ult[i]);
				return false;
			}
			last = ult[i];
			//System.out.print(a[i] + " ");
		}
		//System.out.println("ok");
		return true;
	}
	
}
