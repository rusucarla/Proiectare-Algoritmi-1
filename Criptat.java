import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Criptat {

	static final String INPUT_FILE = "criptat.in";
	static final String OUTPUT_FILE = "criptat.out";

	static class Word {
		String value;
		Map<Character, Integer> letterCount;
		double impactPercent = 0.0;

		Word(String value) {
			this.value = value;
			this.letterCount = new HashMap<>();
			for (char c : value.toCharArray()) {
				this.letterCount.put(c, this.letterCount.getOrDefault(c, 0) + 1);
			}
		}

		void calculateImpact(char mostFrequentLetter) {
			int letterFrequency = 
				this.letterCount.getOrDefault(mostFrequentLetter, 0);
			this.impactPercent = ((double) letterFrequency) / this.value.length();
		}

		double getImpactPercent() {
			return this.impactPercent;
		}
	}

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(INPUT_FILE)));
		int N = Integer.parseInt(br.readLine());
		List<Word> words = new ArrayList<>();

		for (int i = 0; i < N; i++) {
			words.add(new Word(br.readLine()));
		}
		// Inchid fisierul de intrare
		br.close();

		// Calculez frecventa literelor unice
		Map<Character, Integer> letterFrequency = calculateLetterFrequency(words);

		// vreau sa extrag intr-o lista toate literele unice
		Set<Character> uniqueLetters = letterFrequency.keySet();

		// Vreau pentru fiecare litera sa calculez parola si dupa sa aflu cea
		// mai buna parola dintre toate

		String bestPassword = "";

		for (char letter : uniqueLetters) {
			String passwordForLetter = getPassword(words, letter);
			if (passwordForLetter.length() > bestPassword.length()) {
				bestPassword = passwordForLetter;
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
			writer.write(String.valueOf(bestPassword.length()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String getPassword(List<Word> originalWords, char letter) {
		List<Word> words = new ArrayList<>(originalWords.size());
		for (Word word : originalWords) {
			Word newWord = new Word(word.value);
			newWord.calculateImpact(letter);
			words.add(newWord);
		}

		words.sort((w1, w2) -> Double.compare(w2.getImpactPercent(), w1.getImpactPercent()));
	
		// Daca cuvantul cu cel mai mare impact are un impact mai mic sau egal
		// cu 0.5, nu am nicio sansa sa creez o parola valida
		if (words.get(0).getImpactPercent() <= 0.5) {
			return "";
		}

		StringBuilder password = new StringBuilder();
		int totalLetterCount = 0, totalLetters = 0;

		for (int i = 0; i < words.size(); i++) {
			Word currentWord = words.get(i);
			// Calculez lungimea si impactul cuvantului curent
			int currentAddedLetters = totalLetterCount
				+ currentWord.letterCount.getOrDefault(letter, 0);
			int currentAddedLength = totalLetters + currentWord.value.length();
			double currentImpact = (double) currentAddedLetters / currentAddedLength;

			// Vreau sa aflu cea mai buna optiune dintre cuvantul curent si
			// cuvantul urmator si combinatia lor
			double bestImpact = currentImpact;
			int bestLength = currentAddedLength;
			// Cuvantul curent este intial cea mai buna optiune
			String bestOption = currentWord.value;

			// Verific daca pot adauga si cuvantul urmator
			if (i + 1 < words.size()) {
				Word nextWord = words.get(i + 1);
				// Calculez lungimea si impactul cuvantului urmator
				int nextAddedLetters = totalLetterCount
					+ nextWord.letterCount.getOrDefault(letter, 0);
				int nextAddedLength = totalLetters + nextWord.value.length();
				double nextImpact = (double) nextAddedLetters / nextAddedLength;

				// Calculez lungimea si impactul combinatiei celor doua cuvinte
				int bothAddedLetters = currentAddedLetters 
					+ nextWord.letterCount.getOrDefault(letter, 0);
				int bothAddedLength = currentAddedLength + nextWord.value.length();
				double bothImpact = (double) bothAddedLetters / bothAddedLength;

				// Voi presupune ca 2 cuvinte adaugate la parola sunt mai bune 
				// dpdv al lungimii atat timp cat respecta criteriul de impact
				if (bothImpact > 0.5 && bothAddedLength > bestLength) {
					bestImpact = bothImpact;
					bestLength = bothAddedLength;
					bestOption = currentWord.value + nextWord.value;
					// Sar peste cuvantul urmator
					i++;
				} else if (nextImpact > 0.5 && nextAddedLength > bestLength) {
					// Daca cuvantul urmator este mai potrivit decat best-ul
					// curent (asta poate fi si combinatia celor doua cuvinte)
					bestImpact = nextImpact;
					bestLength = nextAddedLength;
					bestOption = nextWord.value;
					i++;
				}
			}

			// Verific daca optiunea cea mai buna respecta criteriul de impact
			// (asta mai mult atunci cand sunt la ultimul cuvant petnru ca nu am
			// verificat impactul cuvantului curent)

			if (bestImpact > 0.5) {
				password.append(bestOption);
				Word bestWord = new Word(bestOption);
				totalLetterCount += bestWord.letterCount.getOrDefault(letter, 0);
				totalLetters += bestOption.length();
			}
		}

		return password.toString();
	}

	static Map<Character, Integer> calculateLetterFrequency(List<Word> words) {
		Map<Character, Integer> frequencyMap = new HashMap<>();
		// Calculeaz frecventa literelor din fiecare cuvant
		for (Word word : words) {
			for (Map.Entry<Character, Integer> entry : word.letterCount.entrySet()) {
				frequencyMap.put(entry.getKey(), frequencyMap.getOrDefault(entry.getKey(), 0)
						+ entry.getValue());
			}
		}
		return frequencyMap;
	}

}
