//hsiangyulee, hsiangyl
package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/** GrammyAnalyst takes Grammys.txt to provide two reports and one search functionality
 */
public class GrammyAnalyst {
	/**initialize these member variables with appropriate data structures **/
	List<Nomination> nominations;  
	Map<String, List<Nomination>> grammyMap;  
	List<Artist> artists;

	public GrammyAnalyst() {
		nominations = new ArrayList<>();
		grammyMap = new HashMap<>();
		artists = new ArrayList<>();
	}

	public static void main(String[] args) {
		GrammyAnalyst ga = new GrammyAnalyst();
		ga.loadNominations();
		ga.loadGrammyMap();
		System.out.println("*********** Grammy Report ****************");
		ga.printGrammyReport();
		System.out.println("*********** Search Artist ****************");
		System.out.println("Enter artist name");
		Scanner input = new Scanner(System.in);
		String artist = input.nextLine();
		ga.searchGrammys(artist);
		ga.loadArtists();
		System.out.println("*********** Artists Report ****************");
		ga.printArtistsReport();
		input.close();
	}
	
	/**loadNominations() reads data from Grammys.txt and 
	 * populates the nominations list, where each element is a Nomination object
	 */

	void loadNominations() {
		try (Scanner scanner = new Scanner(new File("Grammys.txt"))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(";");
				if (parts.length >= 3) {
					String category = parts[0].trim();
					String title = parts[1].trim();
					String artist = parts[2].trim();
					nominations.add(new Nomination(category, title, artist));
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error reading Grammys.txt: " + e.getMessage());
		}
	}
	
	/**loadGrammyMap uses artist name in lower case as the key, and a list of 
	 * all nominations for that artist as its value. Hint: use 'nominations' list 
	 * created in previous method to populate this map.
	 */

	void loadGrammyMap() {
		for (Nomination nom : nominations) {
			String artistKey = nom.getArtist().toLowerCase();
			if (!grammyMap.containsKey(artistKey)) {
				grammyMap.put(artistKey, new ArrayList<>());
			}
			grammyMap.get(artistKey).add(nom);
		}
	}
	
	/**loadArtists loads the artists array List. Each Artist object in it should have
	 * artist's name in proper case, i.e., as read from data file, and 
	 * a list of nominations for that artist. Hint: use 'grammyMap' created in 
	 * previous method to populate this list
	 */

	void loadArtists() {
		for (Map.Entry<String, List<Nomination>> entry : grammyMap.entrySet()) {
			String properName = entry.getValue().get(0).getArtist();
			Artist artist = new Artist(properName, entry.getValue());
			artists.add(artist);
		}
		Collections.sort(artists);
	}
	
	/**printGrammyReport prints report as shown in the handout */

	void printGrammyReport() {
		List<Nomination> sortedNoms = new ArrayList<>(nominations);
		Collections.sort(sortedNoms);
		for (Nomination nom : sortedNoms) {
			System.out.printf("%s: %s, %s%n",
					nom.getArtist(),
					nom.getTitle(),
					nom.getCategory());
		}
	}
	
	/**printArtistReport prints report as shown in the handout */

	void printArtistsReport() {
		for (Artist artist : artists) {
			System.out.printf("%s: %d%n",
					artist.getName(),
					artist.getNominationCount());
		}
	}
	
	/**searchGrammys takes a string as input and makes a case-insensitive
	 * search on grammyMap. If found, it prints data about all nominations
	 * as shown in the handout.
	 */

	void searchGrammys(String artist) {
		String searchKey = artist.toLowerCase();
		if (grammyMap.containsKey(searchKey)) {
			List<Nomination> artistNoms = grammyMap.get(searchKey);
			for (Nomination nom : artistNoms) {
				System.out.printf("%s: %s: %s%n",
						nom.getArtist(),
						nom.getCategory(),
						nom.getTitle());
			}
		} else {
			System.out.println(artist + " not found!");
		}
	}
}
