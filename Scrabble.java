/*
 * RUNI version of the Scrabble game.
 */
public class Scrabble {

	// Note 1: "Class variables", like the five class-level variables declared below,
	// are global variables that can be accessed by any function in the class. It is
	// customary to name class variables using capital letters and underline characters.
	// Note 2: If a variable is declared "final", it is treated as a constant value
	// which is initialized once and cannot be changed later.

	// Dictionary file for this Scrabble game
	static final String WORDS_FILE = "dictionary.txt";

	// The "Scrabble value" of each letter in the English alphabet.
	// 'a' is worth 1 point, 'b' is worth 3 points, ..., z is worth 10 points.
	static final int[] SCRABBLE_LETTER_VALUES = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3,
												  1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };

	// Number of random letters dealt at each round of this Scrabble game
	static int HAND_SIZE = 10;

	// Maximum number of possible words in this Scrabble game
	static int MAX_NUMBER_OF_WORDS = 100000;

    // The dictionary array (will contain the words from the dictionary file)
	static String[] DICTIONARY = new String[MAX_NUMBER_OF_WORDS];

	// Actual number of words in the dictionary (set by the init function, below)
	static int NUM_OF_WORDS;

	// Populates the DICTIONARY array with the lowercase version of all the words read
	// from the WORDS_FILE, and sets NUM_OF_WORDS to the number of words read from the file.
	public static void init() {
		// Declares the variable in to refer to an object of type In, and initializes it to represent
		// the stream of characters coming from the given file. Used for reading words from the file.  
		In in = new In(WORDS_FILE);
        System.out.println("Loading word list from file...");
        NUM_OF_WORDS = 0;
		while (!in.isEmpty()) {
			// Reads the next "token" from the file. A token is defined as a string of 
			// non-whitespace characters. Whitespace is either space characters, or  
			// end-of-line characters.
			DICTIONARY[NUM_OF_WORDS++] = in.readString().toLowerCase();
		}
        System.out.println(NUM_OF_WORDS + " words loaded.");
	}

	// Checks if the given word is in the dictionary.
	public static boolean isWordInDictionary(String word) {
		for (int i = 0; i < DICTIONARY.length; i++) {
			if (word.equals(DICTIONARY[i])) {
				return true;
			}
		}
		return false;
	}
	
	// Returns the Scrabble score of the given word.
	// If the length of the word equals the length of the hand, adds 50 points to the score.
	// If the word includes the sequence "runi", adds 1000 points to the game.
	public static int wordScore(String word) {
		int sum = 0;

		char c;
		for (int i = 0; i < word.length(); i++) {
			c = word.charAt(i);
			sum += SCRABBLE_LETTER_VALUES[ (int) word.charAt(i) - 97 ];
		}
		sum *= word.length();

		sum += (word.length() == HAND_SIZE ? 50 : 0);

		sum += (MyString.subsetOf("runi", word) ? 1000 : 0);
		
		return sum;
	}

	// creates a random string of chars based on the frequency of each char in our dictionary
	public static String frequencyOfCharBasedRandomStringOfLetters(int size) {
		int[] frequencyArr = new int[26];
		int currCharFrequencySum;
		for (int i = 97; i <= 122; i ++) {
			currCharFrequencySum = 0;
			for (int j = 0; j < DICTIONARY.length; j++) {
				currCharFrequencySum += ( MyString.countChar(DICTIONARY[j], ((char) i)) );
			}
			frequencyArr[i-97] = currCharFrequencySum;
		} // we now got an array with every char's frequency in dictionary. nice.

		int[] cumalativeWeightsOfChars = new int[26];
		cumalativeWeightsOfChars[0] = frequencyArr[0];
		
		for (int i = 1; i < 26; i++) {
			cumalativeWeightsOfChars[i] = cumalativeWeightsOfChars[i-1] + frequencyArr[i];
		} // we get every char to value of sum of itself and previous chars. why? of course.
		

		int totalSum = cumalativeWeightsOfChars[25]; // makes sense

		/* prints the percentages of frequency for each char
		double sum = 0;
		for (int i = 0; i < 26; i++) {
			System.out.println((char) (i + 97) + ": " + (double) Math.round((10000)*((double) frequencyArr[i]/totalSum))/100 + "%");
			sum += (100)*((double) frequencyArr[i]/totalSum);
		}
		System.out.println(sum+"%");
		*/

		String randomStringBasedOnFrequency = "";
		for (int i = 0; i < size; i++) {
			// get random num between 0 and our total sum
			int randNum = (int) (Math.random() * totalSum);
			for (int j = 0; j < 25; j++) {
				if (randNum <= cumalativeWeightsOfChars[j]) {
					randomStringBasedOnFrequency = randomStringBasedOnFrequency + ( (char) (j + 97) );
					break;
				}
			}
		} // no further comments, explanation is trivial (read it in a hebrew accent).
		
		return randomStringBasedOnFrequency;
	}


	// Creates a random hand of length (HAND_SIZE - 2) and then inserts
	// into it, at random indexes, the letters 'a' and 'e'
	// (these two vowels make it easier for the user to construct words)
	public static String createHand() {
		/*
		  * this is the old boring random string :
		String newHand = MyString.randomStringOfLetters(HAND_SIZE - 2);
		newHand = MyString.insertRandomly('a', newHand);
		newHand = MyString.insertRandomly('e', newHand);
		*/

		// this is the fun, new, cool random string based on frequency of chars
		String newHand = frequencyOfCharBasedRandomStringOfLetters(HAND_SIZE);

		return newHand;
	}
	
    // Runs a single hand in a Scrabble game. Each time the user enters a valid word:
    // 1. The letters in the word are removed from the hand, which becomes smaller.
    // 2. The user gets the Scrabble points of the entered word.
    // 3. The user is prompted to enter another word, or '.' to end the hand. 
	public static void playHand(String hand) {
		int n = hand.length();
		int score = 0;
		// Declares the variable in to refer to an object of type In, and initializes it to represent
		// the stream of characters coming from the keyboard. Used for reading the user's inputs.   
		In in = new In();
		while (hand.length() > 0) {
			System.out.println("Current Hand: " + MyString.spacedString(hand));
			System.out.println("Enter a word, or '.' to finish playing this hand:");
			// Reads the next "token" from the keyboard. A token is defined as a string of 
			// non-whitespace characters. Whitespace is either space characters, or  
			// end-of-line characters.
			String input = in.readString();

			if (input.length() == 1 && input.charAt(0) == '.') {
				break;
			}
			if (MyString.subsetOf(input, hand)) {
				if (isWordInDictionary(input)) { // valid word
					score += wordScore(input);
					hand = MyString.remove(hand, input);
					System.out.println(input + " earned " + wordScore(input) + " points. Score: " + score + " points\n");
				} else {
					System.out.println("No such word in the dictionary. Try again.\n");
				}
			} else {
				System.out.println("Invalid word. Try again.\n");
			}

		}
		if (hand.length() == 0) {
	        System.out.println("Ran out of letters. Total score: " + score + " points");
		} else {
			System.out.println("End of hand. Total score: " + score + " points");
		}
	}

	// Plays a Scrabble game. Prompts the user to enter 'n' for playing a new hand, or 'e'
	// to end the game. If the user enters any other input, writes an error message.
	public static void playGame() {
		// Initializes the dictionary
    	init();
		// The variable in is set to represent the stream of characters 
		// coming from the keyboard. Used for getting the user's inputs.  
		In in = new In();

		while(true) {
			System.out.println("Enter n to deal a new hand, or e to end the game:");
			// Gets the user's input, which is all the characters entered by 
			// the user until the user enter the ENTER character.
			String input = in.readString();
			
			if (input.length() == 1 && input.charAt(0) == 'n') {
				playHand(createHand());
				break;
			}
			else if (input.length() == 1 && input.charAt(0) == 'e') {
				break;
			}
			else {
				System.out.println("I have given you very simple instructions so follow them.\n");
			}
		}
	}

	public static void main(String[] args) {
		//// Uncomment the test you want to run
		////testBuildingTheDictionary();  
		////testScrabbleScore();    
		////testCreateHands();  
		////testPlayHands();
		playGame();
	}

	public static void testBuildingTheDictionary() {
		init();
		// Prints a few words
		for (int i = 0; i < 5; i++) {
			System.out.println(DICTIONARY[i]);
		}
		System.out.println(isWordInDictionary("mango"));
	}
	
	public static void testScrabbleScore() {
		System.out.println(wordScore("bee"));	
		System.out.println(wordScore("babe"));
		System.out.println(wordScore("friendship"));
		System.out.println(wordScore("running"));
	}
	
	public static void testCreateHands() {
		System.out.println(createHand());
		System.out.println(createHand());
		System.out.println(createHand());
	}
	public static void testPlayHands() {
		init();
		playHand("ocostrza");
		playHand("arbffip");
		playHand("aretiin");
	}
}
