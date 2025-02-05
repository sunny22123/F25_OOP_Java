//hsiangyl, hsiang-yu lee
package lab5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Anagrammar {
	String[] words;		//stores all words read from words.txt
	boolean isInDictionary; //true if the clue word exists in words.txt
	boolean hasAnagrams;	//true if the clue word has anagrams
	String[] anagramArray;	//stores all anagrams of clue-word, if found
	
	/**loadWords method reads the file and loads all words 
	 * into the words[] array */
	void loadWords(String filename) {
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			int wordCount = 0;
			while (reader.readLine() != null){
				wordCount++;
			}
			reader.close();
			words = new String[wordCount];
			// store in words array
			reader = new BufferedReader(new FileReader(filename));
			String line;
			int index=0;
			while((line = reader.readLine()) != null){
				words[index] = line.trim();
				index++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	/** findAnagrams method traverses the words array and looks 
	 * for anagrams of the clue. While doing so, if the clue-word itself is found in the 
	 * words array, it sets the isInDictionary to true.
	 * If it finds any anagram of the clue, it sets the hasAnagram to true. 
	 * It loads the anagram into the anagramArray. 
	 * If no anagrams found, then anagramArray remains an array with size 0. 
	 * */
	void findAnagrams(String clue) {
		isInDictionary = false;
		hasAnagrams = false;

		anagramArray = new String[words.length];
		int anagramCount = 0;

		// sort
		String sortedClue = sortLetters(clue);

		// match the word
		for (int i = 0; i < words.length; i ++){
			String word = words[i];

			if(word.equals(clue)){
				isInDictionary = true;
			}

			String sortedWord = sortLetters(word);

		// re-combination
			if (sortedWord.equals(sortedClue) && !word.equals(clue)){
				anagramArray[anagramCount] = word;
				anagramCount++;
				hasAnagrams = true;
			}
		}

		// resize if no match
		String[] result = new String[anagramCount];
		for (int i = 0; i < anagramCount; i++) {
			result[i] = anagramArray[i];
		}
		anagramArray = result;
}

	public String sortLetters(String word) {
		char[] chars = word.toCharArray();
		Arrays.sort(chars);
		return new String(chars);
}
}
