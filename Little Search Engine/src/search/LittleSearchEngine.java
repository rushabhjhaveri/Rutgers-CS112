package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;

	/**
	 * The frequency (numdoc2 of times) the keyword occurs in the above document.
	 */
	int frequency;

	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
			throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}

	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
			throws FileNotFoundException {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		Scanner sc = new Scanner(new File (docFile));
		String keyword = "";
		String line = "";
		StringTokenizer tokenizer;
		String token;
		HashMap<String, Occurrence> resultant = new HashMap<String, Occurrence>();
		//if empty file
		if(docFile == null || docFile.length() == 0){
			sc.close();
			throw new FileNotFoundException();
		}

		//go through file line by line
		while(sc.hasNextLine()){
			line = sc.nextLine();
			tokenizer = new StringTokenizer(line);
			while(tokenizer.hasMoreTokens()){
				token = tokenizer.nextToken();
				keyword = getKeyWord(token);
				if(keyword != null){
					//if HashMap already has the key
					//update the frequency
					if(resultant.containsKey(keyword)){
						resultant.get(keyword).frequency++;
					}
					else{//HashMap does not contain the key - add it and set frequency to 1.
						resultant.put(keyword, new Occurrence(docFile,1));
					}
				}
			}
		}

		//close scanner
		sc.close();

		//return resultant
		//System.out.println("=============LOAD  KEYWORS===================");
		//printHashMap(resultant);
		//System.out.println(" Size =" + resultant.size());
		return resultant;
	}


	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		// COMPLETE THIS METHOD
		ArrayList<Occurrence> occurrences;

		//add each key into the master keywordsIndex hashMap
		for(String key: kws.keySet()){

			// DEBUG
			//System.out.println("In For Loop");
			// END DEBUG

			//case 1: the key is already in the master hashMap
			if(keywordsIndex.containsKey(key)){
				keywordsIndex.get(key).add(kws.get(key));
				insertLastOccurrence(keywordsIndex.get(key));
			}
			else{//case 2: the key is not in the master keywordsIndex hashMap 
				//create arraylist of occurrences 
				occurrences = new ArrayList<Occurrence>();
				Occurrence occVal = kws.get(key);

				//DEBUG
				//System.out.println("BEFORE : Key = " + key + " occVal = " + occVal);
				// END DEBUG

				occurrences.add(occVal);

				//DEBUG
				//printArrayList(occurrences);

				// END DEBUG

				//add the key into master hashMap
				keywordsIndex.put(key, occurrences);
			}
		}

	}



	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		char ch = 0;
		String tempWord;
		//debug statement
		//System.out.println("Word: [" + word + "]");
		tempWord = word;
		if(tempWord == null){
			//debug statement
			//System.out.println("Empty word");
			return null;
		}

		//convert to lowercase 
		//tempWord.trim().toLowerCase();

		tempWord = tempWord.toLowerCase();

		//debug statement
		//System.out.println("Word after converting to lower case: " + word + " tempWord = " + tempWord);

		//remove punctuation
		tempWord = tempWord.replaceAll("[.,?!:;]+$", "");
		//System.out.println("Word after removing punctuation: " + tempWord);

		//check if noise word
		if(noiseWords.containsKey(tempWord)){
			//debug statement
			//System.out.println(word + " = Noise word");
			return null;
		}

		//check if all letters
		for(int i = 0; i < tempWord.length(); i++){
			ch = tempWord.charAt(i);
			if(!(Character.isLetter(ch))){
				//debug statement
				//System.out.println("Unacceptable word");
				return null;
			}
		}
		//debug statement
		//System.out.println("Acceptable word:" + tempWord);
		return tempWord;
	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		int high = 0;
		int low = 0;
		int target = 0;
		int mid = 0;
		ArrayList<Integer> midIndex = new ArrayList<Integer>();

		//debug statement
		//System.out.println("In insertLastOccurrence: ");
		//printArrayList(occs);

		//if list is empty, return null
		if(occs.size()==0){
			return null;
		}

		//binary search to find where to insert occurrence
		low = 0;
		high = occs.size()-2;
		target = occs.get(occs.size()-1).frequency;

		//midIndex.add(mid);

		//binary search, add mid to arraylist
		while(low <= high){
			mid = (high + low)/2;
			if(occs.get(mid).frequency < target){
				high = mid-1;
			}
			else if(occs.get(mid).frequency > target){
				low = mid+1;
			}
			else{
				break;
			}

		}

		midIndex.add(mid);
		//find where target arr3ds to go
		if(occs.get(mid).frequency > target){
			occs.add(mid+1, occs.get(occs.size()-1));
		}
		else{
			occs.add(mid, occs.get(occs.size()-1));
		}
		occs.remove(occs.size()-1);
		return midIndex;
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */

	public ArrayList<String> top5search(String kw1, String kw2) { 
		ArrayList<String> retArrList = null;

		ArrayList<Occurrence> kw1OccList; 
		ArrayList<Occurrence> kw2OccList; 

		int maxDocuments = 5; 
		int i; 
		// String str; 
		String doc1,doc2; 

		// convert search strings to lowercase as the key values are in lowercase    (case insensitive) 
		kw1 = kw1.toLowerCase();               
		kw2 = kw2.toLowerCase(); 

		kw1OccList = this.keywordsIndex.get(kw1); 
		kw2OccList = this.keywordsIndex.get(kw2); 

		if ((kw1OccList == null) && (kw2OccList == null) ) {return null;} 


		retArrList = new ArrayList<String>(); 

		if (kw1OccList == null) {       
			for (i=0 ; i < (kw2OccList.size()); i++) { 
				doc2 = kw2OccList.get(i).document; 
				retArrList.add(doc2); 
				if (retArrList.size() >= (maxDocuments)) { 
					break; 
				}       
			}  // end for 
		} else if (kw2OccList == null) {       
			for (i=0 ; i < (kw1OccList.size()); i++) { 
				doc1 = kw1OccList.get(i).document; 
				retArrList.add(doc1); 
				if (retArrList.size() >= (maxDocuments)) { 
					break; 
				} 
			}  // end for 
		} else { 
			// so both the list have atleast one Occurrence   : will loop thru  list1 and manipulate list2 
			//  int startWithList = 1; 
			int secondCnt=0; 
			Boolean done = false; 

			for (i = 0; i < ( kw1OccList.size()) ; i++) { 
				if  (secondCnt < kw2OccList.size()) { 
					/* 
                    // Debug 
                    System.out.println("kw1OccList frequency = " + kw1OccList.get(i).frequency 
                            + " kw2OccList.get(secondCnt).frequency =" +  kw2OccList.get(secondCnt).frequency 
                            + " kw1OccList.document = " + kw1OccList.get(i).document 
                            + " kw2OccList.get(secondCnt).document = " + kw2OccList.get(secondCnt).document 
                            ); 
                    //End Debug 
					 */ 

					if  ((kw1OccList.get(i).frequency)  > (kw2OccList.get(secondCnt).frequency) )  { 
						// add the document of kw1OccList to the retArrList 

						// DEBUG System.out.println(" > than condition"); // END DEBUG 
						doc1 = kw1OccList.get(i).document; 
						if (! retArrList.contains(doc1)) { 
							retArrList.add(doc1); 
							// check if doc11 == doc2 then increment secondCnt 
							doc2=kw2OccList.get(secondCnt).document; 
							if (doc1.equals(doc2)) { 
								secondCnt++; 
							} 
							// check if retArrList.size > 5 
							if (retArrList.size() >= (maxDocuments)) { 
								done = true; 
								break; 
							}       
						}     
					} else  if ( (kw1OccList.get(i).frequency) == (kw2OccList.get(secondCnt).frequency) )  { 
						//add the document of kw1OccList to the retArrList 
						doc1 = kw1OccList.get(i).document; 
						if (! retArrList.contains(doc1)) { 
							retArrList.add(doc1); 
						} 

						// check if doc11 == doc2 then increment secondCnt 
								doc2=kw2OccList.get(secondCnt).document; 
								if (doc1.equals(doc2)) { 
									secondCnt++; 
								} 

								// check if retArrList.size > 5 
								if (retArrList.size() >= (maxDocuments)) { 
									done = true; 
									break; 
								}       

					} else if ( (kw1OccList.get(i).frequency) < (kw2OccList.get(secondCnt).frequency) )  { 

						// add the document of kw2OccList to the retArrList 
						// check if the document is already in retArrList, in which case ignore it 
						if (secondCnt < kw2OccList.size()) { 
							while (secondCnt < kw2OccList.size()) { 
								if ( (kw2OccList.get(secondCnt).frequency) > (kw1OccList.get(i).frequency) )  { 
									doc2 = kw2OccList.get(secondCnt).document; 
									if (! retArrList.contains(doc2)) { 
										retArrList.add(doc2); 

										secondCnt++; 
										// check if retArrList.size > 5 
										if (retArrList.size() >= (maxDocuments)) { 
											done = true; 
											break; 
										}       
									} else { 
										secondCnt++; 
									} 

								} else { 
									break; 
								} 

							} // end while secondCnt < kw2OccList.size 
						}  // end if secondCnt < kw2OccList.size 

						if (! done) { 
							// add the document of kw1OccList to the retArrList 
							// check if the document is already in retArrList, in which case ignore it 
							doc1 = kw1OccList.get(i).document; 
							if (! retArrList.contains(doc1)) { 
								retArrList.add(doc1); 
							} 

							// check if retArrList.size > 5 
							if (retArrList.size() >= (maxDocuments)) { 
								done = true; 
								break; 
							}     
						}  // end if (! done) 
					} 
				} else { 
					// at this point value of i > size of 2 list, so NO need to check the 2nd list 

					//add the document of kw1OccList to the retArrList 
					doc1 = kw1OccList.get(i).document; 
					if (! retArrList.contains(doc1)) { 
						retArrList.add(doc1); 
					} 

					// check if retArrList.size > 5 
					if (retArrList.size() >= (maxDocuments)) { 
						done = true; 
						break; 
					}       

				} 

			} // end for i <  kw1OccList.size() 

			// at this point the 2nd list could have been exhausted if it was <= 1st list 
			//      (in that case nothing to be done 
			// OR  the 2nd list being greater then 1st then the 2nd list  has to be traversed 
			// logic for the OR condition 

			for (int scnt = secondCnt ; scnt < ( kw2OccList.size()) ; scnt++) { 
				// add the document of kw2OccList to the retArrList 
				doc2 = kw2OccList.get(scnt).document; 
				if (! retArrList.contains(doc2)) { 
					retArrList.add(doc2); 
				} 


				// check if retArrList.size > 5 
				if (retArrList.size() >= (maxDocuments)) { 
					done = true; 
					break; 
				}       

			} //end for 

		}   // BIG IF CONDITION 


		// since only Max of 5 elements have to be returned...truncate any that are additional 
		if (retArrList.size() > maxDocuments) { 
			for (i = (retArrList.size() -1); i > maxDocuments ; i--) { 
				retArrList.remove(i); 
			} 
		} 

		// DEBUG 
		//printArrList(retArrList);     
		// END DEBUG 
		return retArrList; 
	} 



	private void printMasterHashMap() { 

		ArrayList<Occurrence> arrValList; 
		String key; 
		Iterator iterator = this.keywordsIndex.keySet().iterator(); 

		System.out.println("=================MASTER HASH TABLE=================="); 
		while (iterator.hasNext()) { 
			key = iterator.next().toString(); 
			arrValList = this.keywordsIndex.get(key); 

			System.out.print("Key=" + key + " "+ "Value=["); 
			for (Occurrence val:arrValList) { 
				System.out.print(val + " "); 
			} 
			System.out.print("]"); 
			System.out.println();         
		} // end while iterator 
		System.out.println("===========END MASTER HASH TABLE====================="); 
	}

	private void printHashMap(HashMap<String, Occurrence> hashMap){
		Iterator itr = hashMap.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry pair = (Map.Entry)itr.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());

		}
	}

	private void printArrayList(ArrayList<Occurrence> arrValList) { 

		System.out.print( "ArrayList=["); 
		for (Occurrence val:arrValList) { 
			System.out.print(val + " "); 
		} 
		System.out.print("]"); 
		System.out.println();         
		System.out.println("===========END ArrayList====================="); 
	}

	private void printArrList(ArrayList<String> arrValList) { 

		System.out.print( "ArrayList=["); 
		for (String val:arrValList) { 
			System.out.print(val + " "); 
		} 
		System.out.print("]"); 
		System.out.println();         
		System.out.println("===========END ArrayList====================="); 
	}
}
