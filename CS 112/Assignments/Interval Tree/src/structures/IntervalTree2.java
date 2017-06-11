package structures;

import java.util.ArrayList;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree2 {
	
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
	public IntervalTree2(ArrayList<Interval> intervals) {
		
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
		// Selection Sort Algorithm
		for(int i = 0; i < intervals.size(); i++)
		{
			Interval intTarget = intervals.get(i);
			int target = (lr == 'l') ? intTarget.leftEndPoint : intTarget.rightEndPoint;
			int min = i, minValue = target;
			for(int j = i + 1; j < intervals.size(); j++)
			{
				Interval selIntTarget = intervals.get(j);
				int selTarget = (lr == 'l') ? selIntTarget.leftEndPoint : selIntTarget.rightEndPoint;
				if(selTarget < minValue)
				{
					minValue = selTarget;
					min = j;
				}
			}
			
			// once we found our min switch
			if(min != i)
			{
				intervals.set(i, intervals.get(min));
				intervals.set(min, intTarget);
			}
		}
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
		ArrayList<Integer> sortedEndpoints = new ArrayList<Integer>();
		int indexL = 0, indexR = 0;
		while(indexL < leftSortedIntervals.size() || indexR < rightSortedIntervals.size())
		{
			if(indexL >= leftSortedIntervals.size())
			{
				int endpoint = rightSortedIntervals.get(indexR++).rightEndPoint;
				if(!exist(sortedEndpoints, endpoint))
					sortedEndpoints.add(endpoint);
				continue;
			}
			
			if(indexR >= rightSortedIntervals.size())
			{
				int endpoint = leftSortedIntervals.get(indexL++).leftEndPoint;
				if(!exist(sortedEndpoints, endpoint))
					sortedEndpoints.add(endpoint);
				continue;
			}
			
			int left = leftSortedIntervals.get(indexL).leftEndPoint;
			int right = rightSortedIntervals.get(indexR).rightEndPoint;
			
			if(left < right)
			{
				if(!exist(sortedEndpoints, left))
					sortedEndpoints.add(left);
				indexL++;
			}else if(left == right)
			{
				if(!exist(sortedEndpoints, left))
					sortedEndpoints.add(left);
				indexL++;
				indexR++;
			}else
			{
				if(!exist(sortedEndpoints, right))
					sortedEndpoints.add(right);
				indexR++;
			}
		}
		
		return sortedEndpoints;
	}
	
	private static boolean exist(ArrayList<Integer> list, int a)
	{
		for(int b : list)
			if(a == b)
				return true;
		
		return false;
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points
	 * without duplicates.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		Queue<IntervalTreeNode> queue = new Queue<IntervalTreeNode>();
		
		for(int a : endPoints)
		{
			IntervalTreeNode node = new IntervalTreeNode(a, a, a);
			queue.enqueue(node);
		}
		
		return buildTreeNodesRecursively(queue);
	}
	
	private static IntervalTreeNode buildTreeNodesRecursively(Queue<IntervalTreeNode> queue)
	{
		int size = queue.size();
		if(size == 1)
		{
			return queue.dequeue();
		}
		
		int tempSize = size;
		while(tempSize > 1)
		{
			IntervalTreeNode t1 = queue.dequeue();
			IntervalTreeNode t2 = queue.dequeue();
			float v1 = t1.maxSplitValue;
			float v2 = t2.minSplitValue;
			IntervalTreeNode node = new IntervalTreeNode((v1 + v2)/2, v1, v2);
			node.leftChild = t1;
			node.rightChild = t2;
			queue.enqueue(node);
			tempSize -= 2;
		}
		
		if(tempSize == 1)
		{
			IntervalTreeNode node = queue.dequeue();
			queue.enqueue(node);
		}
		
		return buildTreeNodesRecursively(queue);
	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		IntervalTreeNode tree = root;
		for(Interval i : leftSortedIntervals)
		{
			tree = root;
			while(true)
			{
				float node = tree.splitValue;
				if(i.contains(node))
				{
					if(tree.leftIntervals == null)
						tree.leftIntervals = new ArrayList<Interval>();
					
					tree.leftIntervals.add(i);
					break;
				}
				
				if(i.rightEndPoint < tree.splitValue)
				{
					tree = tree.leftChild;
				}else if(i.leftEndPoint > tree.splitValue)
				{
					tree = tree.rightChild;
				}
			} // end while
		}
		
		for(Interval i : rightSortedIntervals)
		{
			tree = root;
			while(true)
			{
				float node = tree.splitValue;
				if(i.contains(node))
				{
					if(tree.rightIntervals == null)
						tree.rightIntervals = new ArrayList<Interval>();
					
					tree.rightIntervals.add(i);
					break;
				}
				
				if(i.rightEndPoint < tree.splitValue)
				{
					tree = tree.leftChild;
				}else if(i.leftEndPoint > tree.splitValue)
				{
					tree = tree.rightChild;
				}
			} // end while
		}
	}
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q)
	{
		ArrayList<Interval> result = new ArrayList<Interval>();
		intersectIntervals(result, q, root);
		return result;
	}
	
	public void intersectIntervals(ArrayList<Interval> result, Interval q, IntervalTreeNode root)
	{
		float splitVal = root.splitValue;
		ArrayList<Interval> lIntervals = root.leftIntervals;
		ArrayList<Interval> rIntervals = root.rightIntervals;
		IntervalTreeNode rL = root.leftChild, rR = root.rightChild;
		
		if(rL == null && rR == null)
		{
			return;
		}
		
		if(q.contains(splitVal))
		{
			if(lIntervals != null)
				result.addAll(lIntervals);
			
			intersectIntervals(result, q, rR);
			intersectIntervals(result, q, rL);
		}else if(splitVal < q.leftEndPoint)
		{
			if(rIntervals != null)
			{
				int i = rIntervals.size() - 1;
				while(i >= 0)
				{
					if(q.intersects(rIntervals.get(i)))
						result.add(rIntervals.get(i));
					i -= 1;
				}
			}
			
			intersectIntervals(result, q, rR);
		}else if(splitVal > q.rightEndPoint)
		{
			if(lIntervals != null)
			{
				int i = 0;
				while(i < lIntervals.size())
				{
					if(q.intersects(lIntervals.get(i)))
						result.add(lIntervals.get(i));
					i += 1;
				}
			}
			
			intersectIntervals(result, q, rL);
		}
	}

}

