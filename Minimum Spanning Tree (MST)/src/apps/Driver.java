package apps;
import structures.*;
import java.io.*;
import java.util.*;
public class Driver {
	public static void main(String[] args){
		try{
			Graph graph = new Graph("graph2.txt");
			PartialTreeList p = MST.initialize(graph);
			printTreeList(p);
			ArrayList<PartialTree.Arc> pta = MST.execute(p);
			System.out.println("Final output: " + pta);
		}
		catch(IOException e){

		}
		
	}
	
	public static void printTreeList(PartialTreeList p){
		PartialTree ptree;
		Iterator itr = p.iterator();
		while(itr.hasNext()){
			//ptree = (PartialTree)itr.next();
			ptree = (PartialTree)itr.next();
			System.out.println(ptree.toString());
		}
	}
}
