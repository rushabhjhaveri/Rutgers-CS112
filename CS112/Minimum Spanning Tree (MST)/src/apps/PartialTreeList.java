package apps;

import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Vertex;


public class PartialTreeList implements Iterable<PartialTree> {

	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;

		/**
		 * Next node in linked list
		 */
		public Node next;

		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;

	/**
	 * Number of nodes in the CLL
	 */
	private int size;

	/**
	 * Initializes this list to empty
	 */
	public PartialTreeList() {
		rear = null;
		size = 0;
	}

	/**
	 * Adds a new tree to the end of the list
	 * 
	 * @param tree Tree to be added to the end of the list
	 */
	public void append(PartialTree tree) {
		Node ptr = new Node(tree);
		if (rear == null) {
			ptr.next = ptr;
		} else {
			ptr.next = rear.next;
			rear.next = ptr;
		}
		rear = ptr;
		size++;
	}

	/**
	 * Removes the tree that is at the front of the list.
	 * 
	 * @return The tree that is removed from the front
	 * @throws NoSuchElementException If the list is empty
	 */
	public PartialTree remove() 
			throws NoSuchElementException {
		System.out.println("             ========   In remove size = "  + this.size  + "=============");
		Node hold = null;

		/* COMPLETE THIS METHOD */
		//case 1: check if tree is empty
		if(this.rear == null){
			throw new NoSuchElementException();
		}

		else if(rear.next == rear){//single node tree
			hold = rear;
			rear = null;
			size--;
		}
		else{
			hold = rear.next;
			rear.next = rear.next.next;
			size--;
		}

		return hold.tree;
	}
	
	private PartialTree old_removeTreeContaining(Vertex vertex) 
			throws NoSuchElementException {
		/* COMPLETE THIS METHOD */
		System.out.println("             ========   In removeTreeContaining size = "  + this.size  + "=============");
		Node ptr= null;
		Node prev = null;
		//boolean found = false; 
		PartialTree retTree = null;
		boolean done = false;
		//if the vertex passed is itself null
		if(vertex == null){
			throw new NoSuchElementException();
		}

		if(rear == null){
			throw new NoSuchElementException();
		}

		ptr = rear.next;
		prev = rear;

		//traverse through the partial tree list
		System.out.println("Before for loop: " + this.size);
		for(int i = 0; i < this.size; i++){
			System.out.println("ptr.tree.getRoot().name: " + ptr.tree.getRoot().name + "vertex.getRoot().name: " 
					+ vertex.getRoot().name);
			if(ptr.tree.getRoot().name.equals(vertex.getRoot().name)){
				done = true;
				retTree = ptr.tree;
				//now do the pointer re-adjustments
				//case 1: if there is only one node in the CLL
				if(prev == ptr){
					size--;
					if(i == this.size-1){
						this.rear = null;
					}
					else{
						rear.next = ptr.next;
					}
					break;
				}

				//case 2: the node is the last node in the CLL
				else if(ptr == rear){
					prev.next = rear.next;
					size--;
					break;
				}

				//case 3: the node is somewhere in the middle of the CLL
				else{
					prev.next = ptr.next;
					size--;
					break;
				}
			}

			prev = ptr;
			ptr  = ptr.next;
		}
		System.out.println("Exited for loop");

		return retTree;
	}

	/**
	 * Removes the tree in this list that contains a given vertex.
	 * 
	 * @param vertex Vertex whose tree is to be removed
	 * @return The tree that is removed
	 * @throws NoSuchElementException If there is no matching tree
	 */

	public PartialTree removeTreeContaining(Vertex vertex) 
			throws NoSuchElementException { 
		/* COMPLETE THIS METHOD */  // TO COMPLETE YET 
		PartialTree retPartialTree = null; 
		boolean done = false; 
		// DEBUG 
		System.out.println("           =========START:In PartialTreeeList.removeTreeContaining======="); 
		System.out.println("           this.size = " + this.size() + "======="); 
		// END DEBUG 
		if ((this.size() < 1) ) { 
			throw new NoSuchElementException(); 
		} else { 
			Node prev = this.rear.next; 
			// loop through the Partial Tree List to find the tree to be removed 
			Node curr = this.rear.next;        // start of the list 
			int thisSize = this.size(); 
			for (int i = 0; i < thisSize;i++){ 
				// DEBUG 
				System.out.println("           curr.tree.getRoot().name = " + curr.tree.getRoot().name 
						+ " vertex.name = " + vertex.name 
						+ " vertex.getRoot().name= " + vertex.getRoot().name); 
				// END DEBUG 
				// check if the curr.tree.root(vertex).name  equals vertex.name 
				if (curr.tree.getRoot().name.equals(vertex.getRoot().name)) { 
					// store the curr.tree into retPartialTree 
					retPartialTree = curr.tree; 
					done=true; 
					//bypass curr in the list 
					if (curr == prev) { 
						// this means it's the first Node element of the PartialTreeList  that is to be remoevd 
						// OR it's the ONLY element in the PartialTreeList 
						if (i == (thisSize-1)) { 
							// DEBUG   
							// System.out.println(" ===============curr == prev================="); 
							// END DEBUG 

							this.rear = null; 
						} else { 
							rear.next = curr.next; 
						} 
						this.size--; 
						break; 
					} else if (curr == rear) { 
						// this means it's the last Node element of the PartialTreeList that is to be remoevd. 
						prev.next = rear.next; 
						rear = prev; 
						this.size--; 
						break; 
					} else { 
						// this means it's some Node element that is not first or last that is to be removed 
						prev.next = curr.next; 
						this.size--; 
						break; 
					} 
				} else { 
					System.out.println("           =========ELSE:======="); 
					prev = curr; 
					curr = curr.next; 
				} 
			}  // end for 
			if (!done) { 
				System.out.println("           =========AFTER FOR : done = " + done + "======="); 
				throw new NoSuchElementException(); 
			} 
		} 

		// DEBUG 
		System.out.println("         =========END : PartialTreeeList.removeTreeContaining ============"); 
		// END DEBUG 
		return retPartialTree; 
	} 


	/**
	 * Gives the number of trees in this list
	 * 
	 * @return Number of trees
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns an Iterator that can be used to step through the trees in this list.
	 * The iterator does NOT support remove.
	 * 
	 * @return Iterator for this list
	 */
	public Iterator<PartialTree> iterator() {
		return new PartialTreeListIterator(this);
	}

	private class PartialTreeListIterator implements Iterator<PartialTree> {

		private PartialTreeList.Node ptr;
		private int rest;

		public PartialTreeListIterator(PartialTreeList target) {
			rest = target.size;
			ptr = rest > 0 ? target.rear.next : null;
		}

		public PartialTree next() 
				throws NoSuchElementException {
			if (rest <= 0) {
				throw new NoSuchElementException();
			}
			PartialTree ret = ptr.tree;
			ptr = ptr.next;
			rest--;
			return ret;
		}

		public boolean hasNext() {
			return rest != 0;
		}

		public void remove() 
				throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

	}
}


