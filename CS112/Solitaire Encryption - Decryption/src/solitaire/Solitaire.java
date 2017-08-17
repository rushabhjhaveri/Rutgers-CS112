package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {

	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;

	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}

		// shuffle the cards
		Random randgen = new Random();
		for (int i = 0; i < cardValues.length; i++) {
			int other = randgen.nextInt(28);
			int temp = cardValues[i];
			cardValues[i] = cardValues[other];
			cardValues[other] = temp;
		}

		// create a circular linked list from this deck and make deckRear point to its last node
		CardNode cn = new CardNode();
		cn.cardValue = cardValues[0];
		cn.next = cn;
		deckRear = cn;
		for (int i=1; i < cardValues.length; i++) {
			cn = new CardNode();
			cn.cardValue = cardValues[i];
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}

	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
			throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = cn;
			deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		if(deckRear == null || deckRear.next == null){ //CLL is empty
			//throw new NoSuchElementException("List is empty");
			//throw exception here 
			return;
		}


		//   logic for joker A is the first card 
		//   also took it out of the while loop
		if(deckRear.next.cardValue == 27){ //Joker A is the first card in the list
			CardNode temp = deckRear.next;
			CardNode newFront = temp.next;

			deckRear.next = newFront; //last node now points to second node, which is the new first node
			temp.next = newFront.next;
			newFront.next = temp;
			return;
		}

		//Upon reaching here, the existence of Joker A in the list has been ensured
		CardNode prev = deckRear;
		CardNode front = deckRear.next;

		while(front != deckRear){ //iterate through the deck

			if(front.cardValue == 27 && front.next == deckRear){ //Joker A is the second-last card in the deck
				CardNode temp = front;
				CardNode frnt = deckRear.next; //points to front of the deck
				CardNode back = deckRear; //points to last card of deck
				prev.next = front.next; //the last node now becomes the second last node, hence its next should 
				//point to the next of front
				back.next = temp; //since the last node is now the second-last node, its next should now point to 
				//the new last node, which is Joker A
				deckRear = temp;//the node containing Joker A is now the last card in the deck, hence deckRear should
				//now point to it
				temp.next = frnt; //since the deck is a CLL, the next of the last node (Joker A) should 
				//point back to the front of the deck
				return;
			}
			else if(front.next.cardValue == 27 && front.next == deckRear){ //Joker A is the last card in the deck

				//UPDATED -- new logic -- errors corrected
				// last check cannot be out of the while loop as the last-1.next needs re-pointing
				CardNode temp = deckRear; //Store the node containing the Joker A in a temp node
				front.next = deckRear.next; //The node that front points to is the second-last node, which now becomes 
				//the last node, hence its next should point to the front of the deck,
				//since it's a CLL
				deckRear = front.next; //deckRear, the pointer to the last node of the deck, should now point to front's
				//next 
				temp.next = deckRear.next; //temp is now the first node and temp.next points 2nd node

				deckRear.next = temp; //CLL; last node's next should always point back to the front of the list
				return;	
			}
			else if(front.cardValue == 27){ //Joker A is somewhere in the middle of the deck
				// CardNode temp = front; //store Joker A's node in a temp node
				CardNode temp1 = front.next; //store node to be swapped with in another temp node
				prev.next = front.next; //previous node's next now must point to front.next
				front.next = temp1.next; 
				temp1.next = front;

				//swap complete
				return;
			}
			prev = front;
			front = front.next;
		}
	}

	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		// COMPLETE THIS METHOD
		CardNode lookAhead2 = null;   // points to 2 node from the front


		if(deckRear == null){ //list is empty
			//throw exception
			return;
		}

		if(deckRear.next.cardValue == 28){ //Joker B is the first item in the deck

			CardNode front = deckRear.next;
			CardNode newFront = front.next;
			lookAhead2 = newFront.next;

			deckRear.next = newFront;
			front.next = lookAhead2.next;
			lookAhead2.next = front;
			return;
		}

		CardNode rear = deckRear;
		CardNode front = deckRear.next;
		while(front != deckRear){ //iterating through deck

			if(front.next.cardValue == 28 && front.next == deckRear){ //Joker B is the last card in the deck
				CardNode temp = deckRear; //store last node in a temp node
				front.next = deckRear.next; //front's next now points to first node
				deckRear = front.next; //deckRear now points to front.next
				CardNode temp1 = deckRear.next; //store first node in another temp node
				deckRear.next = temp; //first node now points to temp
				temp.next = temp1; //temp's next now points to temp1
				CardNode temp2 = temp; //store temp in another temp node
				deckRear.next = temp1; //list's first node is now temp1
				CardNode temp3 = temp1.next; //store temp1's next in another temp node
				temp1.next = temp2; //temp1's next becomes temp2 
				temp2.next = temp3; //temp2's next becomes temp3
				return;
			}
			else if(front.cardValue == 28 && front.next.next == deckRear){ //Joker B is third last in the list
				CardNode back = deckRear; //store last node in a temp node
				CardNode temp = front; //store Joker B in a temp node
				CardNode frnt = deckRear.next; //store first node in a temp node
				rear.next = front.next; //rear's next now points to front.next
				back.next = temp; //back's next now points to temp
				deckRear = temp; //deckRear now points to temp, which is the last node
				deckRear.next = frnt; //temp.next points to front, which is now the first node
				return;
			}
			else if(front.cardValue == 28 && front.next == deckRear){ //Joker B is second-last in the list 

				CardNode origFront = deckRear.next;

				rear.next = front.next;
				front.next = origFront;
				deckRear.next = front;
				return;
			}
			else if(front.cardValue == 28){ //Joker B is somewhere in the middle of the deck

				CardNode frontNextNode = front.next; //store next node to front node 
				lookAhead2 = frontNextNode.next;

				rear.next = front.next; //previous node's next now must point to front.next
				front.next = lookAhead2.next; 
				lookAhead2.next = front;

				return;
			}
			else{
				rear = front;
				front = front.next;
			}
		}
	}


	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		if(deckRear == null){ //list empty
			//throw new NoSuchElementException();
			//throw exception
			return;
		}

		CardNode prev = deckRear.next;
		CardNode origFront = deckRear.next;     // this is required as  it stores the original first location
		CardNode curr = origFront.next;         // points to 2nd item in the list at the start
		
		// NEW CONDITION - scenario 2 - first card is a joker AND last card is a joker
		// DO NOTHING 
		if (    (origFront.cardValue == 27 || origFront.cardValue == 28) && 
				( deckRear.cardValue == 27 || deckRear.cardValue == 28 )
			){
			return;
		}
		
		
		// covers scenario 1 and 7
		//if there are no cards before the first joker, then the second joker will become the last card in the modified deck
		if(origFront.cardValue == 27 || origFront.cardValue == 28){
			while(curr != deckRear){
				
				
				if (curr.cardValue == 27 || curr.cardValue == 28){
					
					
					/*  OLD CODE   -- wrong interpretation of making the 2nd joker the last card
					// change previous next to point to the next value of the 2nd joker (curr.next)
					prev.next = curr.next;
					
					// 1. curr.next point to the originalFront
					// 2. deckRear.next points to curr 
					// 3. Now change deckRear to curr
					// 4. return
					
					 // curr.next = origFront;
					 // deckRear.next = curr;
					 // deckRear=curr;
					 // return;
					 */
					
					 // Since curr.vardValue is 2nd joker,  it becomes the new last card
					 deckRear=curr;
					 return;
					 
				}
				else {
					prev = curr;
					curr = curr.next;
				}
			}
		}
		else if(deckRear.cardValue == 27 || deckRear.cardValue == 28){ 
			//if there are no cards after the second joker,
			//then the first joker will become the first card of the modified deck
			// covers scenario 3  of testing
			
			prev = deckRear;
			curr = deckRear.next;
			
			// DEBUG
       	   // System.out.println("In triple cut: Scenario 3 :  before while:");
       	   // printList(deckRear);
          	//============END DEBUG ========================
       	    
			while(curr != deckRear){
				
				if(curr.cardValue == 27 || curr.cardValue == 28){
					
					
					/*   OLD CODE -- wrong interpretation
					// change previous next to point to the next value of the 2nd joker (curr.next)
					prev.next = curr.next;
					
					// 1. curr.next point to the originalFront
					// 2. deckRear.next points to curr 
					// curr.next = origFront;
					// deckRear.next = curr;
					*/
					
					// Since curr.vardValue is 1st joker,curr becomes the first card & prev becomes the new last  card
					deckRear = prev;
					
					// DEBUG
		       	   // System.out.println("In triple cut: In the condition of making current joker the front:");
		       	  //  printList(deckRear);
		       	//============END DEBUG ========================
		       	    
					return;
					
				} else {
					// ADDED THIS CONDITION AS IT WAS MISSED OUT
					prev = curr;
					curr = curr.next;
				}
				
			}  // end while
		}
		else{ //perform the triple cut
			// covers scenario 4,5,6 and 8   of testing
			prev = deckRear;
			curr = deckRear.next;
			while(curr != deckRear){
				if(curr.cardValue == 27 || curr.cardValue == 28){ //first joker found 
					CardNode firstJoker = curr;
					CardNode curr2 = curr.next;
					while(curr2 != deckRear.next){
						if(curr2.cardValue == 27 || curr2.cardValue == 28){ //second joker found 
							CardNode secondJoker = curr2;
							CardNode afterSecond = curr2.next;
							CardNode head = deckRear.next;
							deckRear.next = firstJoker;
							secondJoker.next = head;
							deckRear = prev;
							deckRear.next = afterSecond;
							
							// Added - missing from the code
							return;
						}
						else{
							curr2 = curr2.next;
						}
					}

				}
				else{
					prev = curr;
					curr = curr.next;
				}
			}
		}
	}


	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		// COMPLETE THIS METHOD
		if(deckRear == null || deckRear.next == null){ //list is empty
			//throw exception
			return;
		}
		int rearVal;
		if(deckRear.cardValue == 28){
			rearVal = 27;
		}
		else{
			rearVal = deckRear.cardValue;
		}

		if(rearVal == 27){
			return;
		}
		CardNode origFront = deckRear.next;
		CardNode secondLastCard = null;
		CardNode newFront;
		CardNode endCount = origFront;
		for(int i = 1; i < rearVal; i++){
			endCount = endCount.next;
		}
		newFront = endCount.next;
		CardNode ptr = deckRear.next;
		while(ptr != deckRear){
			if(ptr.next == deckRear){
				secondLastCard = ptr;
				break;
			}
			else{
				ptr = ptr.next;
			}
		}
		deckRear.next = newFront;
		endCount.next = deckRear;
		secondLastCard.next = origFront;
	}

	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */

	int getKey() {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		//int key = -1;
		int frontValue = 0;
		CardNode curr = null;
		int getNextVal = 0;
		int modifiedLastVal = 0;
		//int i = 0;
		do{
			// DEBUG
			//System.out.println("In getKey: Before Joker A");
			//printList(deckRear);
			//============END DEBUG ========================
			jokerA();

			// DEBUG
			//System.out.println("In getKey: After Joker A");
			//printList(deckRear);
			//============END DEBUG ========================

			// DEBUG
			//System.out.println("In getKey: Before Joker B");
			//printList(deckRear);
			//============END DEBUG ========================
			jokerB();

			// DEBUG
			//System.out.println("In getKey: After Joker B");
			//printList(deckRear);
			//============END DEBUG ========================

			// DEBUG
			//System.out.println("In getKey: Before Triple Cut");
			//printList(deckRear);
			//============END DEBUG ========================
			tripleCut();

			// DEBUG
			//System.out.println("In getKey: After Triple Cut");
			//printList(deckRear);
			//============END DEBUG ========================

			// DEBUG
			//System.out.println("In getKey: Before Count Cut");
			//printList(deckRear);
			//============END DEBUG ========================			
			countCut();

			// DEBUG
			//System.out.println("In getKey: After Count Cut");
			//printList(deckRear);
			//============END DEBUG ========================	


			frontValue = deckRear.next.cardValue;
			if(frontValue == 28){
				frontValue = 27;
			}
			curr = deckRear.next;
			//loop to count up to frontValue 
			for(int i = 1; i < frontValue; i++){
				curr = curr.next;
			}
			getNextVal = curr.next.cardValue;
		}while(getNextVal == 27 || getNextVal == 28);
		return getNextVal;
	}
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		String output = "";
		String trimmedMessage = "";
		int key = 0;
		int sum = 0;
		int alphaInt = 0;
		char ch = 0;
		//String trimmedMessage = message.replaceAll("^[a-zA-Z]", "");
		int len = message.length();
		for(int i = 0; i < len; i++){
			ch = message.charAt(i);
			//DEBUG STATEMENT
			//System.out.println("Character is: " + ch);
			//END DEBUG
			if(Character.isLetter(ch)){
				ch = Character.toUpperCase(ch);
				alphaInt = ch - 'A' + 1;
				key = getKey();
				sum = alphaInt + key;
				if(sum > 26){
					sum -= 26;
				}
				ch = (char)(sum -1 + 'A');
				//System.out.println("Corrosponding Encryption Character: " + ch);
				output += ch;
			}
		}
		return output;
	}

	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		String output = "";
		int len = 0;
		char ch = 0;
		int alphaInt = 0;
		len = message.length();
		//String trimMSG = message.replaceAll("[^a-zA-Z]","");
		for(int i = 0;i < len;i++){
			ch = message.charAt(i);
			alphaInt = ch-'A'+1;
			int key = getKey();
			int sum = alphaInt - key;
			if(sum <= 0){
				sum = sum + 26;
			}
			ch = (char)(sum-1+'A');
			output = output + ch;
		}
		return output;
	}
}
