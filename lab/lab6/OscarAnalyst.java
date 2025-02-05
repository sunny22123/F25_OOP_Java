//hsiangyl, leehsiangyu
package lab6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class OscarAnalyst {
	public List<Nomination> nominations;
	public List<Actor> actors;
	public Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		OscarAnalyst oscarAnalyst = new OscarAnalyst();
		oscarAnalyst.loadNominations("Oscar.txt");
		oscarAnalyst.loadActors();
		oscarAnalyst.analyze();
	}

	void analyze() {
		// Unchanged
	}

	void loadNominations(String filename) {
		this.nominations = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				int year = Integer.parseInt(parts[0]);
				String type = parts[1];
				String actorName = parts[2];
				String movieName = parts[3];
				String roleName = parts[4];

				Nomination nomination = new Nomination(year, type, movieName, roleName);
				this.nominations.add(nomination);
			}
		} catch (IOException e) {
			System.err.println("Error loading nominations: " + e.getMessage());
		}
	}

	void loadActors() {
		this.actors = new ArrayList<>();

		for (Nomination nomination : this.nominations) {
			String actorName = nomination.getMovieName();
			Actor actor = this.actors.stream()
					.filter(a -> a.getName().equals(actorName))
					.findFirst()
					.orElseGet(() -> {
						Actor newActor = new Actor(actorName);
						this.actors.add(newActor);
						return newActor;
					});
			actor.addAward(nomination);
		}
	}

	void printActorsReport() {
		// Unchanged
	}

	void printSearchResults(String searchString) {
		// Unchanged
	}

	List<Nomination> searchMovies(String searchString) {
		List<Nomination> matchingNominations = new ArrayList<>();

		for (Nomination nomination : this.nominations) {
			if (nomination.getMovieName().toLowerCase().contains(searchString.toLowerCase())) {
				matchingNominations.add(nomination);
			}
		}

		matchingNominations.sort((n1, n2) -> n1.getYear() - n2.getYear());
		return matchingNominations;
	}

	public class ActorComparator implements Comparator<Actor> {
		@Override
		public int compare(Actor a1, Actor a2) {
			int a1Awards = a1.getTotalAwards();
			int a2Awards = a2.getTotalAwards();

			if (a1Awards != a2Awards) {
				return a2Awards - a1Awards;
			} else {
				return a1.getName().compareTo(a2.getName());
			}
		}
	}
}

class Actor implements Comparable<Actor> {
	public String name;
	public List<Nomination> awards;

	public Actor(String name) {
		this.name = name;
		this.awards = new ArrayList<>();
	}

	public void addAward(Nomination award) {
		this.awards.add(award);
	}

	public List<Nomination> getAwards() {
		return this.awards;
	}

	public int getTotalAwards() {
		return this.awards.size();
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int compareTo(Actor other) {
		return this.name.compareTo(other.name);
	}
}

class Nomination {
	public int year;
	public String type;
	public String movie;
	public String role;

	public Nomination(int year, String type, String movie, String role) {
		this.year = year;
		this.type = type;
		this.movie = movie;
		this.role = role;
	}

	public int getYear() {
		return this.year;
	}

	public String getType() {
		return this.type;
	}

	public String getMovieName() {
		return this.movie;
	}

	public String getRole() {
		return this.role;
	}
}