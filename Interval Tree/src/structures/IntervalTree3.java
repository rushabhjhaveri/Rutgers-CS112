package structures;

import java.util.ArrayList;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree3 {
	
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
	public IntervalTree3(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		sortIntervals(intervalsLeft, 'l');
		sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = 
							getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
	}
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		if (lr == 'l'){
			for (int i=1; i<intervals.size(); i++){
				Interval left = intervals.get(i);
				int j = i-1;
				while (j>=0 && intervals.get(j).leftEndPoint > left.leftEndPoint){
					intervals.set(j+1, intervals.get(j));
					j = j-1;
				}
				intervals.set(j+1, left);
			}
			System.out.println();
			System.out.println("Left Intervals: " + intervals);

		}
		else{
			for (int i=1; i<intervals.size(); i++){
				Interval right = intervals.get(i);
				int j = i-1;
				while (j>=0 && intervals.get(j).rightEndPoint > right.rightEndPoint){
					intervals.set(j+1, intervals.get(j));
					j = j-1;
				}
				intervals.set(j+1, right);
			}
			System.out.println();
			System.out.println("Right Intervals: " + intervals);

		}
			ArrayList<Interval> temp = new ArrayList<Interval>();
			for (Interval y : intervals){
				temp.add(y);
			}
			intervals.clear();
			for (Interval x : temp){
				intervals.add(x);
			}
			//System.out.println("Intervals: " + intervals);
			return;
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
		ArrayList<Integer> sortedEndPoints = new ArrayList<Integer>();
		
		for(Interval left : leftSortedIntervals){
			if(!(sortedEndPoints.contains(left.leftEndPoint)))
			sortedEndPoints.add(left.leftEndPoint);
			//System.out.println("left endpoints added: " + sortedEndPoints);
		}
		
		int temp = -1;
		boolean absent = true;
		
		for(int i = 0; i < rightSortedIntervals.size(); i++){
			for(int j = 0; j < sortedEndPoints.size(); j++){
				if(sortedEndPoints.get(j) < rightSortedIntervals.get(i).rightEndPoint){
					temp = j;
					absent = true;
				}
				else if (sortedEndPoints.get(j) == rightSortedIntervals.get(i).rightEndPoint){
					absent = false;
					break;
				}
			}
			if(absent){
				sortedEndPoints.add(temp+1, rightSortedIntervals.get(i).rightEndPoint);
			}
			//System.out.println("right endpoints added: " + sortedEndPoints);
		}
		System.out.println();
		System.out.println("sorted endpoints: " + sortedEndPoints);
		
		return sortedEndPoints;
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points
	 * without duplicates.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		
		Queue<IntervalTreeNode> treeQueue = new Queue<IntervalTreeNode>();
		
		for(int x : endPoints){
			IntervalTreeNode temp = new IntervalTreeNode(x,x,x);
			temp.leftIntervals = new ArrayList<Interval>();
			temp.rightIntervals = new ArrayList<Interval>();
			treeQueue.enqueue(temp);
		}
		
		for(int i = 0; i<treeQueue.size;i++){
			IntervalTreeNode itn = treeQueue.dequeue();
			treeQueue.enqueue(itn);
			//System.out.println("endpoints enqueue: " + itn.splitValue);
		}
		
		int size = treeQueue.size;
		IntervalTreeNode treeRoot = null;
		
	while (size > 0){	
		if(size == 1){
			treeRoot = treeQueue.dequeue();
			return treeRoot;
		}
		else {
			int temps = size;
			while(temps>1){
				IntervalTreeNode T1 = treeQueue.dequeue();
				IntervalTreeNode T2 = treeQueue.dequeue();
				float v1 = T1.maxSplitValue;
				float v2 = T2.minSplitValue;
				float x = (v1+v2)/2;
				IntervalTreeNode N = new IntervalTreeNode(x, T1.minSplitValue, T2.maxSplitValue);
				N.leftIntervals = new ArrayList<Interval>();
				N.rightIntervals = new ArrayList<Interval>();
				N.leftChild = T1;
				N.rightChild = T2;
				treeQueue.enqueue(N);
				temps = temps-2;
		}
			if(temps == 1){
				IntervalTreeNode t = treeQueue.dequeue();
				treeQueue.enqueue(t);
				}
			size = treeQueue.size;
		}
	}
		treeRoot = treeQueue.dequeue();
		//System.out.println();
		//System.out.println("tree: " + treeRoot);
		return treeRoot;
}
	
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		for (Interval x : leftSortedIntervals){
			mapIntervals(x, root, true);
		}
		for (Interval x : rightSortedIntervals){
			mapIntervals(x, root, false);
		}
		return;
		}
	
	private void mapIntervals(Interval x, IntervalTreeNode Root, boolean isLeft){
		if (x.contains(Root.splitValue)){
			if (isLeft){
				Root.leftIntervals.add(x);
			} else {
				Root.rightIntervals.add(x);
			}
			return;
		}
		
		if (Root.splitValue < x.leftEndPoint){
			mapIntervals(x, Root.rightChild, isLeft);
		} else {
			mapIntervals(x, Root.leftChild, isLeft);
		}	
	}
	
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		return findIntervals(root, q);
	}
	
	private ArrayList<Interval> findIntervals(IntervalTreeNode R, Interval q) {
		
		ArrayList<Interval> ResultList = new ArrayList<Interval>();
		
		if(R == null){
			return ResultList;
		}
		
		float SplitVal = R.splitValue;
		ArrayList<Interval> Llist = R.leftIntervals;
		ArrayList<Interval> Rlist = R.rightIntervals;
		IntervalTreeNode Lsub = R.leftChild;
		IntervalTreeNode Rsub = R.rightChild;
		
		if(q.contains(SplitVal)){
			for(Interval x : Llist){
				ResultList.add(x);
			}
				ResultList.addAll(findIntervals(Rsub, q));
				ResultList.addAll(findIntervals(Lsub, q));
		}
		else if(SplitVal < q.leftEndPoint) {
			int i = Rlist.size()-1;
			while(i>=0 && Rlist.get(i).intersects(q)){
				ResultList.add(Rlist.get(i));
				i = i-1;
			}
			ResultList.addAll(findIntervals(Rsub, q));
		}
		else if(SplitVal > q.rightEndPoint){
			int i = 0;
			while(i<Llist.size() && Llist.get(i).intersects(q)){
				ResultList.add(Llist.get(i));
				i = i+1;
			}
			ResultList.addAll(findIntervals(Lsub, q));
		}
		//System.out.println();
		//System.out.println("resultlist: " + ResultList);
		return ResultList;
	}
	
}

