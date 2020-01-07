//package hw4;
/*
 * This class is used to do the batcher's bitonicSort 
 */

//import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BitonicSortSynchronized implements Runnable {
	public static int[] arr;                    // the arr which need to be sorted 
	private int partitionArrSize;				// the size of each thread responsible for
	private int startIndex;						// the start index  of each thread
	private int wholeSize;                      // the size of arr which need to be sorted 
	private CyclicBarrier cyclicBarrier = null; // the barrier which used to manipulate thread

	public BitonicSortSynchronized(int[] array, int n, int size, int start, CyclicBarrier barrier) {		
		arr = array;		
		partitionArrSize = size ;	
		wholeSize = n;
		startIndex = start * size;
		cyclicBarrier = barrier;
	}
	
	@Override
	public void run() {				
		try {
			for (int k = 2; k <= wholeSize; k *= 2) {
				//System.out.println("k: " + k);
				for (int j = k/2; j > 0; j /= 2) {
					//System.out.println("j: " + j);		
					for (int i = startIndex; i < partitionArrSize + startIndex; i++) {
						//System.out.println("i: " + i);						
						int ixj = i ^ j;
						//System.out.println("ixj: " + ixj);
						if (ixj > i) {
							if ((i & k) == 0 && arr[i] > arr[ixj])
								swap(arr, i, ixj);
							if ((i & k) != 0 && arr[i] < arr[ixj])
								swap(arr, i, ixj);
						}
					}
					//System.out.println(Arrays.toString(arr));	
					cyclicBarrier.await();
				}
			}	               		                                   
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch(BrokenBarrierException e){
            e.printStackTrace();
        }					
	}

		
	/*
	 * This method is used to swap the wire
	 */
	private void swap (int[]arr, int i, int j) {
		int temp;
		temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}
