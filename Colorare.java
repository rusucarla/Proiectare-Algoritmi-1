import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Colorare {
	static final int MOD = 1000000007;
	static final String INPUT_FILE = "colorare.in";
	static final String OUTPUT_FILE = "colorare.out";

	public static void main(String[] args) {
		try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE));
				BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
			int K = Integer.parseInt(reader.readLine().trim()); // Numarul total de perechi
			// Citim perechile de intrare
			String[] inputPairs = reader.readLine().trim().split("\\s+");

			char lastType = ' '; // Initializam cu un spatiu (niciun tip anterior)
			long rezultat = 1;

			for (int i = 0; i < 2 * K; i += 2) {
				int X = Integer.parseInt(inputPairs[i]); // Numarul de zone consecutive
				char T = inputPairs[i + 1].charAt(0); // Tipul zonei (H sau V)

				// Prima zona din fisier
				if (lastType == ' ') {
					if (T == 'H') {
						// Prima zona H are 3 moduri de colorare.
						rezultat *= 6;
					} else { // T == 'V'
						// Prima zona V are 2 moduri de colorare.
						rezultat *= 3;
					}
				}
				// Prima zona din urmatoarele perechi de zone consecutive
				if (lastType != ' ') {
					// In functie de tipul zonei si de tipul zonei precedente
					if (T == 'H' && lastType == 'H') {
						// Daca ambele zone sunt de tip H, atunci avem 3 moduri
						// de colorare:
						// 1. Facem flip la culori
						// 2 si 3. Folosim cate o culoare care nu a fost
						// folosita in zona precedenta sau nu este prezent adiacenta
						rezultat *= 3;
					} else if ((T == 'H' && lastType == 'V') || (T == 'V' && lastType == 'V')) {
						// Daca zona precedenta este de tip V și actuala este de
						// tip H, atunci avem 2 moduri de colorare:
						// Folosesc cele 2 culori care nu sunt folosite in zona
						// precedenta
						// SAU
						// Daca ambele zone sunt de tip V, atunci avem 2 moduri
						// de colorare:
						// 1 si 2. Folosim cate o culoare care nu a fost folosita in zona precedenta
						rezultat *= 2;
					}
				}

				// Daca zona precedenta este de tip H și actuala este de
				// tip V, atunci avem 1 mod de colorare:
				// Folosesc culoarea care nu a fost folosita in zona precedenta
				// Am decis sa nu mai verific si aceasta posibilitate deoarece
				// oricum nu influenteaza rezultatul final

				// Acum ca am verificat primul element din fiecare tip de zona
				// (prima din fisier si prima din urmatoarele perechi), putem sa
				// calculam rezultatul pentru restul zonelor consecutive

				// Vom scadea cu 1 numarul de zone consecutive pentru a evita
				// dublarea rezultatului
				X--;

				// Si in cazul in care nu e prima zona din fisier, vom modifica
				// lastType pentru a deveni tipul primei zone pe care am
				// verificat-o mai sus

				lastType = T;

				// Fac aceleasi verificari de perechi ca mai sus, dar de data
				// asta folosesc si pow pentru a calcula rapid puterile necesare

				if (T == 'H' && lastType == 'H') {
					// Daca ambele zone sunt de tip H, atunci avem 3 moduri
					// de colorare:
					// 1. Facem flip la culori
					// 2 si 3. Folosim cate o culoare care nu a fost
					// folosita in zona precedenta sau nu este prezent adiacenta
					rezultat *= pow(3, X, MOD);
				} else if ((T == 'H' && lastType == 'V') || (T == 'V' && lastType == 'V')) {
					// Daca zona precedenta este de tip V și actuala este de
					// tip H, atunci avem 2 moduri de colorare:
					// Folosesc cele 2 culori care nu sunt folosite in zona
					// precedenta
					// SAU
					// Daca ambele zone sunt de tip V, atunci avem 2 moduri
					// de colorare:
					// 1 si 2. Folosim cate o culoare care nu a fost folosita in zona precedenta
					rezultat *= pow(2, X, MOD);
				}

				rezultat %= MOD;
			}
			writer.write(String.valueOf(rezultat));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Functie pentru calculul puterii a^b % m in timp logaritmic
	static long pow(long a, long b, long m) {
		long result = 1;
		a %= m;
		while (b > 0) {
			if (b % 2 == 1) {
				result = (result * a) % m;
			}
			a = (a * a) % m;
			b /= 2;
		}
		return result;
	}
}
