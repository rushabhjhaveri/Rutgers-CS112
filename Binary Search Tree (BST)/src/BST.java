//package tryit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class BST {
	
	// implement a Binary Search Tree (BST) of integers
	// A binary search tree is a binary tree with the following properties: 
    //	 	 The data stored at each node has a distinguished key which is unique in the tree and belongs to a total order.
    //	 	 The key of any node is greater than all keys occurring in its left subtree 
    //	 	 and less than all keys occurring in its right subtree.
	//   
	// task add a node, search for a value in a tree, traverse, delete a node  and print the tree inOrder, preOrder and postOrder


		// instance variables
	    int value;
	    BST parent;
	    BST lchildNode;
	    BST rchildNode;
	    
	    // constructor
	    BST(int n) {
	    	this.value = n;
	    	this.lchildNode = null;
	    	this.rchildNode = null;
	    	this.parent = null;
	    }
	    
	    // methods
	    private static BST createNode(int n) {
	    	BST bt = new BST (n);
	    	return bt;
	    }
	    
	    // getters
	    private static int valueOf(BST bt) {
	    	return bt.value;
	    }
	    
	    private static BST lchildNodeOf(BST bt) {
	    	return bt.lchildNode;
	    }
	    private static BST rchildNodeOf(BST bt) {
	    	return bt.rchildNode;
	    }
	    private static BST parentOf(BST bt) {
	    	return bt.parent;
	    }
	    
	    private static Boolean isNullNode(BST bt) {
	    	return (bt == null);
	    }
	    
	    private static Boolean isRootNode(BST bt) {
	    	return ( parentOf(bt)== null);
	    }
	    
	    private static Boolean isLeafNode(BST bt) {
	    	return(  (isNullNode(lchildNodeOf(bt))) && (isNullNode(rchildNodeOf(bt))) && (! isNullNode(parentOf(bt))) );
	    }
	    
	    private static Boolean searchBSTFor(BST bt, int n) {
	       boolean retVal = false;
	       if (isNullNode(bt)) {
	    		return false;
	    	} else if (n == valueOf(bt) ){
	    	    return true;
	    	} else if (n < valueOf(bt) ){
	    		retVal = searchBSTFor(lchildNodeOf(bt),n);
	    	} else if (n > valueOf(bt) ){
	    		retVal = searchBSTFor(rchildNodeOf(bt),n);
	    	}
	       
	       return retVal;
	    	
	    }
	    
	    // get's the rightmost value in the tree leftC
	    private static BST getPredecessor(BST bt) {
	    	BST retVal = null;
	    	BST tmpbt = bt;
	    	
	    	while (! isNullNode(rchildNodeOf(tmpbt))) {
	    		retVal = rchildNodeOf(tmpbt);
	    		tmpbt = rchildNodeOf(tmpbt);
	    	}
	    	return retVal;
	    }
	    // setters
	    private static void setValue(BST bt, int n) {
	    	bt.value = n;
	    }
	    
	    private static void inOrderTraversal(BST bt) {
	    	if (isNullNode(bt)) {
	    		return ;
	    	} else {
	    		inOrderTraversal(lchildNodeOf(bt));
	    		System.out.print( valueOf(bt) + "{" 
                                               + (!isNullNode(parentOf(bt)) ? valueOf(parentOf(bt)) : "NULL" )      
                                               + "}" 
                                          + " "  );
	    		inOrderTraversal(rchildNodeOf(bt));
	    	}
	    }
	    
	    private static void preOrderTraversal(BST bt) {
	    	if (isNullNode(bt)) {
	    		return ;
	    	} else {
	    		System.out.print( valueOf(bt) + " ");
	    		preOrderTraversal(lchildNodeOf(bt));
	    		preOrderTraversal(rchildNodeOf(bt));
	    	}
	    }
	    
	    private static void postOrderTraversal(BST bt) {
	    	if (isNullNode(bt)) {
	    		return ;
	    	} else {
	    		postOrderTraversal(lchildNodeOf(bt));
	    		postOrderTraversal(rchildNodeOf(bt));
	    		System.out.print( valueOf(bt) + " ");
	    	}
	    }
	    
	    private static BST addNode(BST bt, int n) {
	    // private static void addNode(BST bt, int n) {
	    	BST newNode;
	    	if (isNullNode(bt)) {
	    		// System.out.println("In isNull Node logic...");
	    		newNode = createNode(n);
	    		return newNode;
	    		
	    	} else if ( (n < valueOf(bt)) || (n > valueOf(bt)) ) {
	    	// 	System.out.println("0. In n< valueOf || (n > valueOf(bt))  logic...");
	    		if (isLeafNode(bt)) {
	    	//		System.out.println("0L. (isLeafNode(bt))  logic...");
	    			newNode = createNode(n);
	    			newNode.parent = bt;
	    			if (n < valueOf(bt)) {
	    				bt.lchildNode = newNode;
	    			} else if ((n > valueOf(bt))) {
	    				bt.rchildNode = newNode;
	    			}
	    		} else {
	    		//	System.out.println("0. ELSE  logic...");
	    			if (n < valueOf(bt) && (isNullNode(lchildNodeOf(bt))) ) {
	    				// System.out.println("1. In n< valueOf and isNUllNode(left child))  logic...");
	    				newNode = createNode(n);
	    				bt.lchildNode = newNode;
	        			newNode.parent = bt;
	    			} else if (n > valueOf(bt) && (isNullNode(rchildNodeOf(bt))) ) {
	    		//		System.out.println("2. n > valueOf(bt) && (isNullNode(rchildNodeOf(bt)))  logic...");
	    				newNode = createNode(n);
	    				bt.rchildNode = newNode;
	        			newNode.parent = bt;
	    			} else if (n == valueOf(bt)) {
	    				System.out.println("Duplicate Value...Ignoring the value...");
	    				// not sure what do do here
	    			} else{
	    				
	    				// do recursrion
	    				if (n < valueOf(bt)) {
	    			// 		System.out.println("4. In n< valueOf   logic...");
	    				   newNode = addNode(lchildNodeOf(bt),n);
	    				} else if ((n > valueOf(bt))) {
	    			//		System.out.println("5. In n> valueOf   logic...");	
	    				   newNode = addNode(rchildNodeOf(bt),n);
	    				}
	    			}
	    		}
	    		
	    	}
	    	return bt;
	    }
	    
	    private static BST deleteNode(BST bt,int n) {
	    	BST newNode;
	    	BST parentOfbt ;
	    	BST onlyChild = null;                    // used in CASE 2
	    	BST leftC = null;			 // used in CASE 3
	    	BST lpred = null;			 // used in CASE 3
	    	BST parentoflpred = null;		// used in CASE 3

	    	if (isNullNode(bt)) {
	    		System.out.println("0 :  DeleteNode : In isNull Node logic...number " + n + "does not exists");
                        System.exit(-1);
	    	} else if ( (isRootNode(bt)) && (n == valueOf(bt)) &&
                            (isNullNode(lchildNodeOf(bt)))  && (isNullNode(rchildNodeOf(bt))) ) {
	    		//  root node and both the child are null  then set bt to  null as it is an empty tree now
	    		System.out.println("1(a) :  DeleteNode : In root Node logic...");
	    		bt = null;
	    	} else if (  (isLeafNode(bt))  && (n == valueOf(bt) )) {
	    		// CASE 1 : deletion of a leaf node
	    		System.out.println("2 :  DeleteNode : In leaf Node logic...");
	    		parentOfbt = parentOf(bt);
	    		if (n < valueOf(parentOfbt)) {
	    			System.out.println("2(a) :  DeleteNode : In leaf Node logic...setting lchildnode = null");
	    			parentOfbt.lchildNode = null;
	    		} else if (n > valueOf(parentOfbt)) {
	    			parentOfbt.rchildNode = null;
	    		}
	    	} else if (  (n == valueOf(bt)) && 
	    			     ( isNullNode(lchildNodeOf(bt))  ||  isNullNode(rchildNodeOf(bt)) ) 
	    			  ) {
	    		// CASE 2: Node to be deleted (bt) has only 1 child (left or right), then to delete that node :
	    		//      1.simply make the parent of bt  point to the left or right child of  bt
	    		//      2. Make the onlyChild's parent point to parent of bt


	    		// get the parent of bt 
	    		parentOfbt = parentOf(bt);
	    		
	    		// get the onlyChild of bt
	    		if ( isNullNode(lchildNodeOf(bt)) ) {
	    			onlyChild =  rchildNodeOf(bt);     // left child is null, so only child is the rchildNode
	    		}  else if ( isNullNode(rchildNodeOf(bt)) ) {
	    			onlyChild =  lchildNodeOf(bt);    //  right child is null, so only child is the lchildNode

	    		} 

	    		System.out.println("3 (a) :  DeleteNode : CASE 2 : valueOf(bt) = " + valueOf(bt) +
                        "  valueOf(parentOfbt) " + (isNullNode(parentOfbt) ? "NULL" : valueOf(parentOfbt))  + 
                        "  valueOf(onlyChild) " + valueOf(onlyChild) );
                        
                        if  (isNullNode(parentOfbt)) {
                               // implies that bt is a root node
                               onlyChild.parent =  null;           // make onlyChild the new root
                               bt = onlyChild;                     // make bt point to the new root as we return bt
                        } else {
				// 2. Make the onlyChild's parent point to parent of bt
				onlyChild.parent =  parentOfbt;      // only child's parent now points to parent of deleted node (bt)

				// 1. simply make the parent of bt  point to the left or right child of  bt 
				if ( valueOf(onlyChild) < valueOf(parentOfbt) ) {
					System.out.println("3 (a) :  DeleteNode : CASE 2 : valueOf(bt) < valueOf(parentOfbt) "); 
					parentOfbt.lchildNode = onlyChild;
				} else if ( valueOf(onlyChild) > valueOf(parentOfbt) ) {
					System.out.println("3 (b) :  DeleteNode : CASE 2 : valueOf(bt) > valueOf(parentOfbt) ");
					parentOfbt.rchildNode = onlyChild;
				}
                        }
	    	} else {
                         
	    		if (n < valueOf(bt)) {
	    			// recurse down the left child node
	    			System.out.println("4(a) RecurseLChild :  DeleteNode : n < valueOf(bt) ");
	    			deleteNode(lchildNodeOf(bt),n);
	    		} else if (n > valueOf(bt)) {
	    			// recurse down the right child node
	    			System.out.println("4(b) RecurseRChild :  DeleteNode : n > valueOf(bt) ");
	    			deleteNode(rchildNodeOf(bt),n);
	    		} else if (n == valueOf(bt)) {
			    System.out.println("4(c) :  DeleteNode : CASE 3 : n  == valueOf(bt) logic... "); 
	    			// CASE 3 : Node (called bt) to be deleted has 2 child, then to delete it we have :
		    		//    PREDECESSOR:(We will use predecessor for this program)
		    		//     1. Find EITHER   predecessor  of bt      
		    		//       Predecessor of bt is : get the left child of bt  (called leftC) and then 
	    			//            get it's rightmost leaf node(called lpred)
		    		//     2 .If the  predecessor (lpred) has a child (A), then A should become the left child of (leftC)
		    		//     3. Change (replace) the value node bt to be value of node lpred
	    			//     4. Make the parentof(lpred).rchildNode = null;
		    		//                
		    		//    SUCCESSOR      
		    		//     1.  OR  Find the successor 
		    		//         Successor of bt  is :  get the right child of bt (called rightC) and then
	    			//            get the leftmost leaf node (called lsucc)
		    		//     2. if successor (lsucc) has a child (B), then B should become the right child of (rightC)
		    		//     3. Change (replace) the value node bt to be value of node lsucc
	    			//     4. Make the parentof(lsucc).lchildNode = null;
		    		// 
		    		//    NOTE : successor(z) or predecessor(z) can have ATMOST 1 child node (A)  (otherwise it's not a leaf node)
		    		//
	    			
	    			// PREDECESSOR METHOD
	    			//1.Predecessor of bt is : get the left child of bt  (called leftC) and then
	    			//    get it's rightmost leaf node(called lpred)
	    			leftC = lchildNodeOf(bt);
	    			lpred = getPredecessor(leftC);
	    		
	    			if (isLeafNode(leftC)) {
	    				System.out.println("4(d) :  DeleteNode : CASE 3 : isLeafNode(leftC)... " +
	    			                            " bt.value = " + valueOf(bt) +
	    			                            " leftC.value = " + valueOf(leftC) + 
	    			                            " bt.parent.value = " + valueOf(parentOf(bt)) +
	                                                    " lpred.value = " + 
	                                                    (isNullNode(lpred) ? "NULL" :  + valueOf(lpred) ) 
	    			                      ); 
	    				if ( valueOf(leftC) < valueOf(parentOf(bt)) ) {
	    					leftC.rchildNode = bt.rchildNode;
	    					bt.rchildNode.parent = leftC;

	    					bt.parent.lchildNode = leftC;
	    					leftC.parent = bt.parent;

	    					// now clean up bt
	    					bt.parent = null;
	    					bt.lchildNode = null;
	    					bt.rchildNode = null;

	    				} else if ( valueOf(leftC) > valueOf(parentOf(bt)) ) {
	    					leftC.rchildNode = bt.rchildNode;
	    					bt.parent.rchildNode = leftC;
	    					leftC.parent = bt.parent;

	    					// now clean up bt
	    					bt.parent = null;
	    					bt.lchildNode = null;
	    					bt.rchildNode = null;
	    				}

	    			} else {	
	    				
	    				if ( ! isNullNode(lpred)) {     

	    					System.out.println("4(e) :  DeleteNode : CASE 3 : lpred ... " + 
	    							" bt.value = " + valueOf(bt) +
	    							" leftC.value = " + valueOf(leftC) + 
	    							" bt.parent.value = " + (isNullNode(bt.parent) ? "NULL" :  + valueOf(parentOf(bt)) ) +
	    							" lpred.value = " + 
	    							(isNullNode(lpred) ? "NULL" :  + valueOf(lpred) )
	    							);
	    					//2.If the  predecessor (lpred) has a left child (A), then A should become the right child
	    					//  of  the parent of lpred
	    					parentoflpred = parentOf(lpred);
	    					System.out.println("value of parentoflpred = " + valueOf(parentoflpred) );
	    					if (! isNullNode(lchildNodeOf(lpred))) {
	    						parentoflpred.rchildNode = lpred.lchildNode;
	    						lpred.lchildNode.parent = parentoflpred;
	    					} else { 
	    						parentoflpred.rchildNode = null;
	    					} 

	    					//3. Change (replace) the value node bt to be value of node lpred
	    					bt.value = lpred.value;

	    				} else {
	    					System.out.println("4(f) :  DeleteNode : CASE 3 : lpred ... " + 
	    							" bt.value = " + valueOf(bt) +
	    							" leftC.value = " + valueOf(leftC) + 
	    							" bt.parent.value = " + (isNullNode(bt.parent) ? "NULL" :  + valueOf(parentOf(bt)) ) +
	    							" lpred.value = " + 
	    							(isNullNode(lpred) ? "NULL" :  + valueOf(lpred) )
	    							);
	    					// lpred is null  or is same as leftC based on getPredecessor method implementation
	    					//  in my code it's null


	    					//3. Change (replace) the value node bt to be value of node lpred
	    					bt.value = lpred.value; 

	    					bt.lchildNode = leftC.lchildNode;
	    					leftC.lchildNode.parent = bt;

	    				}
				}

			} // n == valueOf(bt)
	    		
	    		
	    	}
	    		
	    		
	    	return bt;
	    }

 
	    private static void prntInOrderBST (BST bt) {
	    	// test out the inOrder, preOrder and postOrder traversal of a tree
	    	System.out.print("[");
	    	inOrderTraversal(bt);
	    	System.out.println("]");
	    }
		public static void main(String[] args) throws FileNotFoundException{
			// TODO Auto-generated method stub

			int rlevel = 0;               // recursion level  -- used to understand recursion 
			BST bt = null;
			Boolean retVal = false;
			
	        
	        /*
			// HOLD on to the original object (console by default)
			PrintStream stdout = System.out;

			// output file  -- writes  output of console to a file  called output.txt
			// set autoflushing to true, i.e.:
			// if output is needed on console then comment out the next 2 lines   and also
			//    comment out the stmt : out.close()  at the end of main function
			PrintStream out = new PrintStream(new FileOutputStream("qsortOutput.txt"),true);
			System.setOut(out);
			*/
			
			    // create  a BST 
	
	               bt = addNode(bt,10);
	               System.out.println("Value of rootNode = "+ valueOf(bt) );
			  
	            
	            // add left child node 
	            bt = addNode(bt,5);
	            System.out.println("Value of rootNode = "+ valueOf(bt) );
	            System.out.println("Value of leftChildNode = "+ valueOf(lchildNodeOf(bt)) + 
	            		           " PARENT value =" + valueOf(parentOf(lchildNodeOf(bt))) );
	            
	            // add a right child node 
	            bt = addNode(bt,75);
	            System.out.println("Value of rootNode = "+ valueOf(bt) );
	            System.out.println("Value of leftChildNode = "+ valueOf(lchildNodeOf(bt)) + " PARENT value =" + valueOf(parentOf(lchildNodeOf(bt))) );
	            System.out.println("Value of rightChildNode = "+ valueOf(rchildNodeOf(bt)) + " PARENT value =" + valueOf(parentOf(rchildNodeOf(bt))) );
	            
	            bt = addNode(bt,2);
	            System.out.println("Value of rootNode = "+ valueOf(bt) );
	            System.out.println("Value of leftChildNode = "+ valueOf(lchildNodeOf(bt)) );
	            System.out.println("Value of leftChildNode(leftChildNode) = "+ valueOf(lchildNodeOf(lchildNodeOf(bt))) + 
	            		           " PARENT value =" + valueOf(parentOf( lchildNodeOf(lchildNodeOf(bt)) )));
	            System.out.println("Value of rightChildNode = "+ valueOf(rchildNodeOf(bt)) );
	            
	            bt = addNode(bt,85);
	            System.out.println("Value of rootNode = "+ valueOf(bt) );
	            System.out.println("Value of leftChildNode = "+ valueOf(lchildNodeOf(bt)) );
	            System.out.println("Value of leftChildNode(leftChildNode) = "+ valueOf(lchildNodeOf(lchildNodeOf(bt))) );
	            System.out.println("Value of rightChildNode = "+ valueOf(rchildNodeOf(bt)) );
	            System.out.println("Value of rightChildNode(rightChildNode) = "+ valueOf(rchildNodeOf(rchildNodeOf(bt))) );
	            
	             bt = addNode(bt,88);
	             bt = addNode(bt,7);
	            bt = addNode(bt,65);
	            bt = addNode(bt,80);
	            
	            // test to check of duplicate node 
	            if (retVal = searchBSTFor(bt,7) ){
	               System.out.println("7 =  " + retVal  + (retVal==true ? " ALREADY EXISTS in the BST ... ignoring duplicate value" 
	            		                                                : " DOES NOT EXISTS  in BST"));
	            } else {
	            	bt = addNode(bt,7);
	            }
	            
	            System.out.println("===============================END ADDITION TESTS  ========================= ");
	            // test out the inOrder, preOrder and postOrder traversal of a tree
	            System.out.println("In-order Traversal ");
	            System.out.print("[");
	            inOrderTraversal(bt);
	            System.out.println("]");
	            
	            System.out.println("Pre-order Traversal ");
	            System.out.print("[");
	            preOrderTraversal(bt);
	            System.out.println("]");
	            
	            System.out.println("Post-order Traversal ");
	            System.out.print("[");
	            postOrderTraversal(bt);
	            System.out.println("]");
	            
	            
	            System.out.println("===============================END TRAVERSAL TESTS ========================= ");
	            // search tests
	            
	            // root test
	               // retVal = true
	            retVal = searchBSTFor(bt, 10);
	            System.out.println("10 =  " + retVal  + (retVal==true ? " EXISTS in the BST " : " DOES NOT EXISTS  in BST"));
	            
	            // left child test
	              // retVal = true
	            retVal = searchBSTFor(bt, 2);
	            System.out.println("2 =  " + retVal  + (retVal==true ? " EXISTS in the BST " : " DOES NOT EXISTS  in BST"));
	            
	               // reetVal = false
	            retVal = searchBSTFor(bt, 1);
	            System.out.println("1 =  " + retVal  + (retVal==true ? " EXISTS in the BST " : " DOES NOT EXISTS  in BST"));
	            
	            //  right child test
	              // retVal = true
	            retVal = searchBSTFor(bt, 88);
	            System.out.println("88 =  " + retVal  + (retVal==true ? " EXISTS in the BST " : " DOES NOT EXISTS  in BST"));
	            
	              // retVal = false
	            retVal = searchBSTFor(bt, 77);
	            System.out.println("77 =  " + retVal  + (retVal==true ? " EXISTS in the BST " : " DOES NOT EXISTS  in BST"));
	            
	            System.out.println("===============================END SEARCH  TESTS ========================= ");
	            
	            // deletion tests
	            // delete leaf

	            System.out.println("LEAF NODE TEST:  BEFORE Deleting 2 from  TREE bt : In-order Traversal " );
	            prntInOrderBST(bt);
	            bt = deleteNode(bt,2);
	            System.out.println("END LEAT NODE TEST: AFTER Deleting 2 from  TREE bt : In-order Traversal " );
	            prntInOrderBST(bt);
	            System.out.println("--------------------");

		            
		            
	            // delete root node when it is the only  node on the tree
	            BST bt1 = null;
	            bt1 = addNode(bt1,10);

	            System.out.println("ONLY ROOT NODE TEST : BEFORE Deleting 10 from  TREE bt1 : In-order Traversal " );
	            prntInOrderBST(bt1);
	            bt1 = deleteNode(bt1,10);
	            System.out.println("ONLY ROOT NODE TEST : AFTER Deleting 10 from  TREE bt : In-order Traversal " );
	            prntInOrderBST(bt1);
	            System.out.println("--------------------");

	              
	            // CASE 2 :  
	            // 2.1 delete node with only 1 child on the right of the left tree
	            BST bt2 = null;
	            bt2= addNode(bt2,10);
	            bt2= addNode(bt2,5);
	            bt2= addNode(bt2,7);

	            System.out.println("BEFORE Deleting 5 from  TREE bt2 : In-order Traversal " );
	            prntInOrderBST(bt2);
	            bt2 = deleteNode(bt2,5);
	            System.out.println("AFTER Deleting 5 from  TREE bt2 : In-order Traversal " );
	            prntInOrderBST(bt2);
	            System.out.println("--------------------");

	            // 2.2 delete node with only 1 child on the left of the left  tree
	            BST bt3 = null;
	            bt3= addNode(bt3,10);
	            bt3= addNode(bt3,5);
	            bt3= addNode(bt3,2);
	            System.out.println("BEFORE Deleting 5 from  TREE bt3 : In-order Traversal " );
	            prntInOrderBST(bt3);
	            bt3 = deleteNode(bt3,5);
	            System.out.println("AFTER Deleting 5 from  TREE bt3 : In-order Traversal " );
	            prntInOrderBST(bt3);
	            System.out.println("--------------------");

	            // 2.3 delete node with only 1 child on the right of the right  tree
	            BST bt4 = null;
	            bt4= addNode(bt4,10);
	            bt4= addNode(bt4,75);
	            bt4= addNode(bt4,85);
	            System.out.println("BEFORE Deleting 75 from  TREE bt4 : In-order Traversal " );
	            prntInOrderBST(bt4);
	            bt4 = deleteNode(bt4,75);
	            System.out.println("AFTER Deleting 75 from  TREE bt4 : In-order Traversal " );
	            prntInOrderBST(bt4);
	            System.out.println("--------------------");


	            // 2.4 delete node with only 1 child on the right of the right  tree
	            BST bt5 = null;
	            bt5= addNode(bt5,10);
	            bt5= addNode(bt5,75);
	            bt5= addNode(bt5,65);
	            System.out.println("BEFORE Deleting 75 from  TREE bt5 : In-order Traversal " );
	            prntInOrderBST(bt5);
	            bt5 = deleteNode(bt5,75);
	            System.out.println("AFTER Deleting 75 from  TREE bt5 : In-order Traversal " );
	            prntInOrderBST(bt5);
	            System.out.println("--------------------");


	            // delete node with 2 child on a  tree
	            BST bt6 = null;
	            bt6= addNode(bt6,10);
	            bt6= addNode(bt6,5);
	            bt6= addNode(bt6,2);
	            bt6= addNode(bt6,7);
	            bt6= addNode(bt6,75);
	            bt6= addNode(bt6,65);
	            bt6= addNode(bt6,85);
	            System.out.println("BEFORE Deleting 10 from  TREE bt6 : In-order Traversal " );
	            prntInOrderBST(bt6);
	            bt6 = deleteNode(bt6,10);
	            System.out.println("AFTER Deleting 10 from  TREE bt6 : In-order Traversal " );
	            prntInOrderBST(bt6);
	            System.out.println("--------------------");


	            BST bt7 = null;
	            bt7= addNode(bt7,10);
	            bt7= addNode(bt7,5);
	            bt7= addNode(bt7,2);
	            bt7= addNode(bt7,7);
	            bt7= addNode(bt7,75);
	            bt7= addNode(bt7,65);
	            bt7= addNode(bt7,85);
	            System.out.println("BEFORE Deleting 5 from  TREE bt7 : In-order Traversal " );
	            prntInOrderBST(bt7);
	            bt7 = deleteNode(bt7,5);
	            System.out.println("AFTER Deleting 5 from  TREE bt7 : In-order Traversal " );
	            prntInOrderBST(bt7);
	            bt7 = deleteNode(bt7,2);
	            System.out.println("AFTER Deleting 2 from  TREE bt7 : In-order Traversal " );
	            prntInOrderBST(bt7);
	            bt7 = deleteNode(bt7,75);
	            System.out.println("AFTER Deleting 75 from  TREE bt7 : In-order Traversal " );
	            prntInOrderBST(bt7);
	            System.out.println("--------------------");

	            BST bt8 = null;
	            bt8= addNode(bt8,10);
	            bt8= addNode(bt8,5);
	            bt8= addNode(bt8,2);
	            bt8= addNode(bt8,7);
	            bt8= addNode(bt8,75);
	            bt8= addNode(bt8,65);
	            bt8= addNode(bt8,85);
	            bt8= addNode(bt8,88);
	            bt8= addNode(bt8,60);
	            bt8= addNode(bt8,50);
	            bt8= addNode(bt8,62);
	            bt8= addNode(bt8,66);
	            bt8= addNode(bt8,69);
	            bt8= addNode(bt8,67);
	            System.out.println("BEFORE Deleting 75 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,75);
	            System.out.println("AFTER Deleting 75 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,65);
	            System.out.println("AFTER Deleting 65 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,60);
	            System.out.println("AFTER Deleting 60 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,62);
	            System.out.println("AFTER Deleting 62 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,5);
	            System.out.println("AFTER Deleting 5 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,10);
	            System.out.println("AFTER Deleting 10 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,2);
	            System.out.println("AFTER Deleting 2 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,7);
	            System.out.println("AFTER Deleting 7 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,50);
	            System.out.println("AFTER Deleting 50 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,69);
	            System.out.println("AFTER Deleting 69 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,85);
	            System.out.println("AFTER Deleting 85 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,66);
	            System.out.println("AFTER Deleting 66 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,67);
	            System.out.println("AFTER Deleting 67 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);
	            bt8 = deleteNode(bt8,88);
	            System.out.println("AFTER Deleting 88 from  TREE bt8 : In-order Traversal " );
	            prntInOrderBST(bt8);


	            System.out.println("===============================END DELETION TESTS ========================= ");
	            /*
			//close the output file 
					out.close();


					//change the output back to console
					System.setOut(stdout);                   // reset to standard output
	             */
	            System.out.println("End of program...Output file is BindaryTreeOutput.txt");
		}

	}

