package apps;

import structures.*;
import java.util.ArrayList;

public class MST {

	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {

		/* COMPLETE THIS METHOD */
		//a graph has an array of vertices
		Vertex[] vertices = graph.vertices;
		boolean[] visitedVertices = new boolean[vertices.length];
		int ctr = 0;
		Vertex.Neighbor neighbors;
		PartialTree.Arc a;
		//step 1: create an empty list L of partial trees
		PartialTreeList L = new PartialTreeList();

		//step 2
		for(Vertex v: vertices){//for every vertex v in the graph (that is, the set of vertices)
			PartialTree T = new PartialTree(v); //create a new PartialTree T, containing ONLY v

			//mark v as belonging to T
			visitedVertices[ctr] = true;
			//Note: do not update the counter immediately after the above statement
			//will require the counter for the siftDown level
			//update it later

			//create a priority queue (heap) and associate it with T
			//insert all of the arcs (edges) connected to v into P 
			//the lower the weight of an arc, the higher is its priority
			//since we are inserting arcs into the heap, the heap must be of type PartialTree.arc

			MinHeap<PartialTree.Arc> P = T.getArcs(); //this step associates the minHeap with T

			//the arcs(edges) connected to v will be v's neighbors
			neighbors = v.neighbors;
			while(neighbors != null){
				a = new PartialTree.Arc(v, neighbors.vertex, neighbors.weight); 
				//after the above statement, we now have one arc associated with the vertex v
				//insert it into P
				P.insert(a);
				//now we must perform a sift down process to make sure the inserted arc
				//is in the correct position in the minHeap
				//based on the law that the lower weighted arcs get higher priority
				//update neighbors to go to the next one
				neighbors = neighbors.next;
			}
			//add partial tree T to the list L 
			if(visitedVertices[ctr] == true){
				L.append(T);
			}
			//update counter++
			ctr++;
		} 

		return L;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {

		/* COMPLETE THIS METHOD */
		ArrayList<PartialTree.Arc> listArcs = new ArrayList<PartialTree.Arc>();
		PartialTree ptx;
		PartialTree.Arc a;
		MinHeap<PartialTree.Arc> pqx;
		MinHeap<PartialTree.Arc>pqy;
		Vertex v1;
		Vertex v2;
		PartialTree pty;
		int PTListSize = ptlist.size();
		while(PTListSize > 1){ 

			//remove partial tree PTX from list L
			ptx = ptlist.remove();
			System.out.println("PTX: " + ptx.toString());
			//let PQX be PTX's priority queue
			pqx = ptx.getArcs();

			System.out.println("Before Deletion - PQX: " + pqx.toString());
			//let a be the highest priority arc in PQX
			//remove the highest priority arc in PQX
			//question - why does this work
			do{
				a = pqx.deleteMin();

				//debug
				System.out.println("a: " + a.toString());
				System.out.println("After Deletion - PQX: " + pqx.toString());

				//but this doesn't?
				//a = pqx.getMin();
				//let v1 and v2 be the two vertices connected by a, where v1 belongs to PTX
				v1 = a.v1;
				System.out.println("v1: " + v1.toString());
				v2 = a.v2;
				System.out.println("v2: " + v2.toString());
				//if v2 also belongs to PTX (that is, they are in the same partial tree), 
				//go back to the previous step and pick the next highest priority arc
				//otherwise, continue to the next step
				if(v2.getRoot().equals(v1.getRoot())){
					continue;
				}
				else{
					break;
				}
			}while(true);
			System.out.println("Exited do-while loop");
			//report a- this is a component of the minimum spanning tree
			listArcs.add(a);
			//System.out.println("List Arcs: " + listArcs);
			//find the partial tree PTY to which v2 belongs
			//we have v2
			//we can get to know which partial tree it belongs to since every partial tree is described by
			//its root vertex
			//Vertex ptyRoot = v2.getRoot();
			//String name = ptyRoot.name;
			pty = ptlist.removeTreeContaining(v2);
			System.out.println("PTY is now stored in PTY");
			System.out.println("PTY: " + pty.toString());

			//remove PTY from the partial tree list L
			//let PQY be PTY's priority queue
			pqy = pty.getArcs();
			System.out.println("PQY: " + pqy.toString());
			//Combine PTX and PTY. 
			ptx.merge(pty);
			System.out.println("After merging: " + ptx.toString());
			//This includes merging the priority queues PQX and PQY into a single priority queue. 
			//this is already done as the merge method in partial tree method calls on the merge method of minHeap
			//pqx.merge(pqy);
			//Append the resulting tree to the end of L.
			ptlist.append(ptx);
			PTListSize--;
		}
		return listArcs;
	}
}
