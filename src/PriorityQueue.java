
/**
 * Implements a priority queue with two arrays based on a binary heap. Keys are integers and objects are
 * associated with a particular key in the same binary heap position. 
 * 
 * @author Jesse Dahir-Kanehl
 *
 */
public class PriorityQueue {
	
	private Integer[] arr;
	private int size;
	private Object[] obj;
	
	public PriorityQueue(int qsize) {
		arr = new Integer[qsize];
		size = 0;
		obj = new Object[qsize];
	}
	
	/*
	 * Returns the object associated with the minimum frequency. The last frequency replaces the minimum
	 * at the beginning of the array and then tests the binary heap for order 
	 * and changes it to meet the definition of a binary heap if necessary.
	 */
	public Object deleteMin() {
	//PRE: !empty()
		Object minObj = obj[1];
		arr[1] = arr[size];
		obj[1] = obj[size];
		size--;
		changeOrder (1);
		return minObj;
	}

	public Integer getMinKey() {
	//PRE: !empty()
		return arr[1];
	}
	
	/*
	 * Tests whether the binary tree is a binary heap and changes it if necessary by swapping integers
	 * if what is below it is less. Objects associated with swapped integers are also swapped to maintain
	 * association.
	 */
	private void changeOrder (int a) {
		int b;
		Integer tmp = arr[a];
		Object tmpO = obj[a];
		for(; a*2 <= size; a = b) {
			b = a*2;
			if (b != size && arr[b+1].compareTo(arr[b]) < 0)
				b++;
			if (arr[b].compareTo(tmp) < 0) {
				arr[a] = arr[b];
				obj[a] = obj[b];
			}
			else
				break;
		}
		arr[a] = tmp;
		obj[a] = tmpO;
	}

	public boolean empty() {
		return size == 0;
	}

	public Object getMinData() {
	//PRE: !empty()
		return obj[1];
	}

	public boolean full() {
		return size == arr.length;
	}

	/*
	 * Adds a new integer to the end and checks if above nodes are greater to maintain the binary heap.
	 * Objects associated with the integer are swapped as well to maintain correct association.
	 */
	public void insert(Integer k, Object d) {
	//PRE !full()
		int a = ++size;
		for (arr[0] = k, obj[0] = d; k.compareTo(arr[a/2]) < 0; a /= 2) {
			arr[a] = arr[a/2];
			obj[a] = obj[a/2];
		}
		arr[a] = k;
		obj[a] = d;
	}

	public int getSize() {
		return size;
	}
		
}
