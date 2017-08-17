package search;
import java.util.*;
import java.io.*;
public class LSEDriver {
	Scanner sc = new Scanner(System.in);
	static LittleSearchEngine testEngine = new LittleSearchEngine();

	public static void main(String[] args) throws IOException{
		testEngine.makeIndex("docs.txt", "noisewords.txt");
		//System.out.println("=============AFTER MERGED  KEYWORS===================");
		//testEngine.printMasterHashMap();
		//System.out.println(testEngine.top5search("round", "streamer"));
		//both are noise words
		//testEngine.top5search("as", "at");
		// word1 is in the master hash map table  and word2 is NOT in the master hash map table 
		testEngine.top5search("round", "tt"); 

		// word1 is NOT in the master hash map table  and word2 is in the master hash map table 
		 //testEngine.top5search("t2", "round"); 

		// both words only in one document  - frequency does not  matter  (OUTPUT:WowCh1.txt) 
		//testEngine.top5search("slightest", "simply"); 

		// each word  in one document  - same frequency     (OUTPUT :WowCh1.txt AliceCh1.txt ) 
		//testEngine.top5search("slightest", "wild"); 

		// each word  in one document  - different frequency     (OUTPUT :AliceCh1.txt WowCh1.txt  ) 
		//testEngine.top5search("slightest", "ears"); 

		// both words only in both documents  - SAME frequency   (OUTPUT:WowCh1.txt AliceCh1.txt ) 
		//testEngine.top5search("really", "sat"); 


		// both words only in both documents  - DIFFERENT frequency   (OUTPUT:WowCh1.txt AliceCh1.txt ) 
		//testEngine.top5search("deep", "world"); 

		// 1st word  only in both documents  and 2 word in 1 document - DIFFERENT frequencies   (OUTPUT:AliceCh1.txt WowCh1.txt  ) 
		//testEngine.top5search("round", "swarm"); 

		// both words only in both documents  - frequencies of 1st word > frequences of 2nd word  (OUTPUT:AliceCh1.txt WowCh1.txt ) 
		//testEngine.top5search("round", "really");
	}
}
