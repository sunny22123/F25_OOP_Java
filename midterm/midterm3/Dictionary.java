//hsiangyl, leehsiangyu
package exam3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.util.*;

public class Dictionary {
	public final static String DICTIONARY = "/Users/leesunny/Desktop/F24_java/midterm/src/exam3/SampleDictionary.txt";
	List<Word> wordList = new ArrayList<>();
	Map<String, Word> singleMap = new HashMap<>();
	Map<String, List<Word>> multiMap = new HashMap<>();
	
	public static void main(String[] args) {
		Dictionary dictionary = new Dictionary();
		dictionary.loadWordList();
		dictionary.loadSingleMap();
		dictionary.loadMultiMap();

		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter search word: ");
		String searchWord = scanner.nextLine().toLowerCase();

		System.out.println("------------WordList Search------------");
		List<String> wordListResult = dictionary.searchWordList(searchWord);
		if (wordListResult == null) System.out.println("Sorry! " + searchWord + " not found!");

		System.out.println("------------SingleMap Search------------");
		String singleMapResult = dictionary.searchSingleMap(searchWord);
		if (singleMapResult == null) System.out.println("Sorry! " + searchWord + " not found!");

		System.out.println("------------MultiMap Search------------");
		List<String> multiMapResult = dictionary.searchMultiMap(searchWord);
		if (multiMapResult == null) System.out.println("Sorry! " + searchWord + " not found!");
	}
	
	/**loadWordList() reads the txt file. For each line, it invokes 
	 * getWord() method that returns a Word object. This object is then
	 * added to the arrayList wordList
	 */
	void loadWordList() {
		try (BufferedReader reader = new BufferedReader(new FileReader(DICTIONARY))) {
			String line;
			while ((line = reader.readLine()) != null) {
				Word word = getWord(line);
				if (word != null) {
					wordList.add(word);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading dictionary file: " + e.getMessage());
		}
	}

	/** getWord() is a helper method to extract a word and its meaning from 
	 * a line of text. 
	 * It takes a wordString and splits it on "(". The first
	 * element after split is the word, and rest are elements of its meaning. 
	 * So it uses first element to initialize 'word' of Word, and rest to 
	 * initialize 'meaning' of Word. As '(' may occur anywhere in the 
	 * 'meaning', the split string is put back together by putting
	 * '(' in front of each piece.  
	 * @param wordString
	 * @return
	 */
	Word getWord(String wordString) {
		if (wordString == null || !wordString.contains("(")) return null;
		String[] parts = wordString.split("\\(", 2);
		String word = parts[0].trim();
		String meaning = "(" + parts[1].trim();
		return new Word(word, meaning);

	}
	

	/** loadSingleMap() takes each word from
	 * wordList and loads it into singleMap with key being
	 * the Word's word in lowercase, and its value being the whole 
	 * Word object.
	 */
	void loadSingleMap() {
		for (Word word : wordList) {
			singleMap.put(word.word.toLowerCase(), word);
		}
	}

	/**loadMultiMap() takes each word from wordList and loads it 
	 * into multiMap with key being the Word's word in lowercase, and 
	 * its value being a list of all Word objects for that word. 
	 */
	void loadMultiMap() {
		for (Word word : wordList) {
			String key = word.word.toLowerCase();
			multiMap.putIfAbsent(key, new ArrayList<>());
			multiMap.get(key).add(word);
		}
	}

	/** searchWordList() takes a searchWord String and and searches for it in wordList.
	 * If found, it prints all its meanings. Else it prints 'Sorry! word not found!'
	 * It also returns a list of meanings, if found. Else it returns null.
	 * @param searchWord
	 */
	List<String> searchWordList(String searchWord) {
		List<String> meanings = new ArrayList<>();
		for (Word word : wordList) {
			if (word.word.equalsIgnoreCase(searchWord)) {
				meanings.add(word.meaning);
			}
		}
		if (meanings.isEmpty()) return null;
		meanings.forEach(System.out::println);
		return meanings;
	}
	
	/** searchSingleMap() takes a searchWord String and searches for it in singleMap.
	 * If found, it prints its meaning. Else it prints 'Sorry! word not found!'
	 * It also returns the meaning string, if found, or else it returns null. 
	 * @param searchWord
	 */
	String searchSingleMap(String searchWord) {
		Word word = singleMap.get(searchWord.toLowerCase());
		if (word != null) {
			System.out.println(word.meaning);
			return word.meaning;
		}
		return null;
	}
	
	/** searchMultiMap() takes a searchWord String and searches for it in multiMap. 
	 * If found, it prints all its meanings. Else it prints 'Sorry! word not found!'
	 * It also returns a list of meanings, if found. Else it returns null.
	 * @param searchWord
	 */
	List<String> searchMultiMap(String searchWord) {
		List<Word> words = multiMap.get(searchWord.toLowerCase());
		if (words != null && !words.isEmpty()) {
			List<String> meanings = new ArrayList<>();
			for (Word word : words) {
				meanings.add(word.meaning);
				System.out.println(word.meaning);
			}
			return meanings;
		}
		return null;
	}
}
