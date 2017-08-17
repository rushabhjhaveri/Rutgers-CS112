package structures;

import java.util.ArrayList;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {

	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;

	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {

		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}

		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;

		// sort intervals on left and right end points
		sortIntervals(intervalsLeft, 'l');

		//debug method to print sorted leftEndPoints
		//printIntervals(intervalsLeft);

		sortIntervals(intervalsRight,'r');

		//debug method to print sorted rightEndPoints
		//printIntervals(intervalsRight);

		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = 
				getSortedEndPoints(intervalsLeft, intervalsRight);

		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		
		//debug - to see if tree created properly
		//printTree(root);

		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
		
		//printTree(root);
	}

	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}

	private void printIntervals(ArrayList<Interval> A){
		for(Interval iv: A){
			System.out.print("[" + iv + "] ");
		}
		System.out.println();
	}

	private static void printList(ArrayList<Integer> A){
		for(Integer iv: A){
			System.out.print(iv + " ");
		}
		System.out.println();
	}
	
	private static void printTree(IntervalTreeNode root){
		IntervalTreeNode T = root;
		if(T == null){
			return;
		}
		else{
			printTree(T.leftChild);
			System.out.println(T.toString() + " ");
			printTree(T.rightChild);
		}
	}

	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		// COMPLETE THIS METHOD
		char ch;
		int size = 0;
		//ArrayList<Interval> sortedIntervals = new ArrayList<Interval>();
		ch = lr;
		size = intervals.size();
		if(ch == 'l'){
			//sort intervals in ascending order of leftEndpoints
			quickSortLeftEndPoints(intervals, 0, (size-1));
		}
		else if(ch == 'r'){
			//sort intervals in ascending order of rightEndPoints
			quickSortRightEndPoints(intervals, 0, (size-1));
		}
		else{
			//throw exception
			System.out.println("Invalid character. Enter 'l' or 'r'.");
			return;
		}
	}

	private static ArrayList<Interval> quickSortLeftEndPoints(ArrayList<Interval> intervals, int lowerIndex, int higherIndex){
		int i = 0;
		int j = 0;
		int pivot = 0;
		int pivotIndex = 0;

		i = lowerIndex; j = higherIndex;

		//calculate pivot number - taking it as value at middle index of the arraylist
		pivotIndex = lowerIndex+(higherIndex-lowerIndex)/2;
		pivot = intervals.get(pivotIndex).leftEndPoint;

		//divide into two arraylists
		while(i <= j){
			/*
			 * In each iteration identify a number from the left side which
			 * is greater than the pivot value, and also identify a number 
			 * from the right side which is less than the pivot value.
			 * once the search is done, swap both numbers
			 */
			while(intervals.get(i).leftEndPoint < pivot){
				i++;
			}

			while(intervals.get(j).leftEndPoint > pivot){
				j--;
			}
			if(i <= j){
				exchangeIntervals(intervals, i,j);
				//move index to next position on both sides
				i++;
				j--;
			}
		}
		//call quickSort method recursively
		if(lowerIndex < j){
			quickSortLeftEndPoints(intervals, lowerIndex, j);
		}
		if(i < higherIndex){
			quickSortLeftEndPoints(intervals, i, higherIndex);
		}

		return intervals;
	}

	private static ArrayList<Interval> quickSortRightEndPoints(ArrayList<Interval> intervals, int lowerIndex, int higherIndex){
		int i = 0;
		int j = 0;
		int pivot = 0;
		int pivotIndex = 0;

		i = lowerIndex; j = higherIndex;

		//calculate pivot number - taking it as value at middle index of the arraylist
		pivotIndex = lowerIndex+(higherIndex-lowerIndex)/2;
		pivot = intervals.get(pivotIndex).rightEndPoint;

		//divide into two arraylists
		while(i <= j){
			/*
			 * In each iteration identify a number from the left side which
			 * is greater than the pivot value, and also identify a number 
			 * from the right side which is less than the pivot value.
			 * once the search is done, swap both numbers
			 */
			while(intervals.get(i).rightEndPoint < pivot){
				i++;
			}

			while(intervals.get(j).rightEndPoint > pivot){
				j--;
			}
			if(i <= j){
				exchangeIntervals(intervals, i,j);
				//move index to next position on both sides
				i++;
				j--;
			}
		}
		//call quickSort method recursively
		if(lowerIndex < j){
			quickSortRightEndPoints(intervals, lowerIndex, j);
		}
		if(i < higherIndex){
			quickSortRightEndPoints(intervals, i, higherIndex);
		}
		return intervals;
	}

	private static void exchangeIntervals(ArrayList<Interval> intervals, int i, int j){
		Interval temp = intervals.get(j);
		intervals.set(j, intervals.get(i));
		intervals.set(i, temp);
	}

	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE PROGRAM COMPILE
		ArrayList<Integer> sortedEndPoints = new ArrayList<Integer>();
		//first, add all distinct left end points into array list sortedEndPoints
		for(Interval iv : leftSortedIntervals){
			if(sortedEndPoints.contains(iv.leftEndPoint)){
				continue;
			}
			else{
				sortedEndPoints.add(iv.leftEndPoint);
			}
		}

		//at this point the all the distinct left endpoints have been added into sortedEndPoints, and are in a sorted (ascending) order
		//now, for the right endpoints
		//for every distinct right endpoint not already in the array list of integers, insertion sort them into the integer array list
		for(Interval iv: rightSortedIntervals){
			if(sortedEndPoints.contains(iv.rightEndPoint)){//duplicate; move on
				continue;
			}
			else{//add and insertion sort to maintain sorted order of array list sortedEndPoints
				sortedEndPoints.add(iv.rightEndPoint);
				insertionSort(sortedEndPoints);

			}
		}

		//printList(sortedEndPoints);

		return sortedEndPoints;
	}

	private static void insertionSort(ArrayList<Integer>sortedEndPoints){
		int size = 0;
		int key = 0;
		int i = 0;
		int j = 0;
		//int temp = 0;

		size = sortedEndPoints.size();
		for(i = 1; i < size; i++){
			key = sortedEndPoints.get(i);
			j = i-1;
			while(j > -1 && sortedEndPoints.get(j) > key){
				//temp = sortedEndPoints.get(j+1);
				sortedEndPoints.set(j+1, sortedEndPoints.get(j));
				j--;
			}
			sortedEndPoints.set(j+1, key);
		}
	}

	/**
	 * Builds the interval tree structure given a sorted array list of end points
	 * without duplicates.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE PROGRAM COMPILE
		int s = 0;
		int temps = 0;
		Queue<IntervalTreeNode> Q = new Queue<IntervalTreeNode>();
		IntervalTreeNode T;
		IntervalTreeNode T1;
		IntervalTreeNode T2;
		IntervalTreeNode N;
		float v1 = 0;
		float v2 = 0;
		float x = 0;

		for(Integer i : endPoints){
			T = new IntervalTreeNode(i, -1, -1);
			Q.enqueue(T);
		}

		
		while(true){
			s = Q.size;
			if(s == 1){
				T = Q.dequeue();
				break;
			}
			temps = s;
			while(temps > 1){
				T1 = Q.dequeue();
				T2 = Q.dequeue();
				v1 = getMaxSplitVal(T1);
				v2 = getMinSplitVal(T2);
				x = (v1+v2)/2;
				N = new IntervalTreeNode(x, v2, v1);
				T = N;
				N.leftChild = T1;
				N.rightChild = T2;
				Q.enqueue(N);
				temps -= 2;
			}
			if(temps == 1){
				Q.enqueue(Q.dequeue());
			}
		}
		return T;
	}

	private static float getMaxSplitVal(IntervalTreeNode T){
		float maxSplitVal = 0;
		if(isLeafNode(T)){
			maxSplitVal = T.splitValue;
		}
		IntervalTreeNode ptr = T;
		//maxSplitValue Maximum of all split values in this node's subtree, which is the split 
		//value of the "rightmost" endpoint leaf node
		
		//go to left subtree
		/*
		if(ptr.leftChild != null){
			ptr = ptr.leftChild;
		}
		else{
			
		}
		*/
		
		//now we are in the left subtree
		while(ptr.rightChild != null){
			ptr = ptr.rightChild;
		}
		
		//at this point, we are at the rightmost node
		maxSplitVal = ptr.splitValue;
		
		return maxSplitVal;
	}
	
	private static boolean isLeafNode(IntervalTreeNode T){
		return (T.leftChild == null && T.rightChild == null);
	}

	private static float getMinSplitVal(IntervalTreeNode T){
		float minSplitVal = 0;
		if(isLeafNode(T)){
			minSplitVal = T.splitValue;
		}
		IntervalTreeNode ptr = T;
		//go to the right subtree
		/*
		if(ptr.rightChild != null){
			ptr = ptr.rightChild;
		}
		else{
			
		}
		*/
		
		while(ptr.leftChild != null){
			ptr = ptr.leftChild;
		}
		
		minSplitVal = ptr.splitValue;
		return minSplitVal;
	}

	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		// COMPLETE THIS METHOD
		IntervalTreeNode T = root;
		IntervalTreeNode N = T;
		for(Interval iv:leftSortedIntervals){
			mapLeftInterval(iv, N);
		}
		for(Interval iv:rightSortedIntervals){
			mapRightInterval(iv, N);
		}
		T = root;
	}
	
	private static void mapLeftInterval(Interval iv, IntervalTreeNode N){
		if(N == null){
			return;
		}
		if(N.leftIntervals == null){
			N.leftIntervals = new ArrayList<Interval>();
		}
		if(N.splitValue >= iv.leftEndPoint && N.splitValue <= iv.rightEndPoint){
			N.leftIntervals.add(iv);
			return;
		}
		else{
			if(N.splitValue < iv.leftEndPoint){
				//if split val < left end point, go into right subtree
				mapLeftInterval(iv, N.rightChild);
			}
			else if(N.splitValue > iv.rightEndPoint){
				//if split val > right end point, go into left subtree
				mapLeftInterval(iv, N.leftChild);
			}
		}
	}
	
	private static void mapRightInterval(Interval iv, IntervalTreeNode N){
		if(N == null){
			return;
		}
		if(N.rightIntervals == null){
			N.rightIntervals = new ArrayList<Interval>();
		}
		if(N.splitValue >= iv.leftEndPoint && N.splitValue <= iv.rightEndPoint){
			N.rightIntervals.add(iv);
			return;
		}
		else{
			if(N.splitValue < iv.leftEndPoint){
				mapRightInterval(iv, N.rightChild);
			}
			else if(N.splitValue > iv.rightEndPoint){
				mapRightInterval(iv, N.leftChild);
			}
		}
	}

	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		ArrayList<Interval> resultList;
		
		resultList = findIntersection(this.root, q);
		return resultList;
	}
	
	private ArrayList<Interval>findIntersection(IntervalTreeNode T, Interval q){
		ArrayList<Interval> resultList = new ArrayList<Interval>();
		IntervalTreeNode R;
		float splitVal;
		ArrayList<Interval>LList = new ArrayList<Interval>();
		ArrayList<Interval>RList = new ArrayList<Interval>();
		IntervalTreeNode LSub;
		IntervalTreeNode RSub;
		int i;
		
		R =T;
		splitVal = R.splitValue;
		LList = R.leftIntervals;
		RList = R.rightIntervals;
		LSub = R.leftChild;
		RSub = R.rightChild;
		//System.out.println( "0>" + R.toString());
		if(isLeafNode(R)){
			return resultList;
		}
		if(splitVal >= q.leftEndPoint && splitVal <= q.rightEndPoint){
			//System.out.println( "1> In splitVal >= q.leftEndPoint && splitVal <= q.rightEndPoint " );
			if(LList != null){ resultList.addAll(LList);}
			resultList.addAll(findIntersection(RSub, q));
			resultList.addAll(findIntersection(LSub, q));
			
		}
		else if(splitVal < q.leftEndPoint){//splitVal falls to the left of queried interval
			if(RList != null){
				//System.out.println( "2> In splitVal < q.leftEndPoint" );
				i = RList.size()-1;
				while(i >= 0 && RList.get(i).intersects(q)){
					resultList.add(RList.get(i));
					i--;
				}
			}
			resultList.addAll(findIntersection(RSub, q));
		}
		else if(splitVal > q.rightEndPoint){//splitVal falls to the right of queried interval
			if(LList != null){
				//System.out.println( "3> splitVal > q.rightEndPoint" );
				i = 0;
				while(i < LList.size() && LList.get(i).intersects(q)){
					resultList.add(LList.get(i));
					i++;
				}
			}
			resultList.addAll(findIntersection(LSub, q));
		}
		
		return resultList;
		
	}

}

