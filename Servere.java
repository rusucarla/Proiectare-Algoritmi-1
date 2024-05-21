import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Servere {
	static class Server {
		double putereMaxima;
		double pragCurent;

		public Server(double putereMaxima, double pragCurent) {
			this.putereMaxima = putereMaxima;
			this.pragCurent = pragCurent;
		}
	}

	public static double calculeazaPutereaMinima(double curent, Server[] servere) {
		double putereMinima = Double.MAX_VALUE;
		for (Server server : servere) {
			// Pentru fiecare server calculez puterea rezultanta cu formula data
			// si pragul de curent pe care vreau sa il testez
			double putere = server.putereMaxima - Math.abs(curent - server.pragCurent);
			// Aflu puterea minima
			putereMinima = Math.min(putereMinima, putere);
		}
		return putereMinima;
	}

	public static Server cautaCurentOptim(Server[] servere, double minCurent, 
		double maxCurent, double precizie) {

		double curentOptim = minCurent; // Nu poate fi mai mic decat curentul minim
		double putereMinimaOptima = -Double.MAX_VALUE;

		// Cat timp diferenta dintre curentul minim si cel maxim este mai mare
		// ca 0.1, caut un curent optim
		while ((maxCurent - minCurent) > precizie) {
			double curentMediu = (minCurent + maxCurent) / 2;
			// Aflu puterea minima pentru curentul mediu
			double putereMedie = calculeazaPutereaMinima(curentMediu, servere);
			// Pentru a avea cu ce compara, aflu puterea minima pentru curentul
			// mediu + 0.1 si puterea minima pentru curentul mediu - 0.1
			// Adica un prag de stanga si unul de dreapta
			double putereMediePlusPrecizie = 
				calculeazaPutereaMinima(curentMediu + precizie, servere);
			double putereMedieMinusPrecizie = 
				calculeazaPutereaMinima(curentMediu - precizie, servere);
			// Incep sa fac comparatii pentru a alege
			// un curent optim si o putere minima optima
			// 1. Daca puterea minima pentru mijloc > puterea minima optima de pana acum
			if (putereMedie > putereMinimaOptima) {
				putereMinimaOptima = putereMedie;
				curentOptim = curentMediu;
			}
			// 2. Daca puterea minima pentru mijloc - 0.1 (stanga) > puterea minima optima
			if (putereMedieMinusPrecizie > putereMinimaOptima) {
				putereMinimaOptima = putereMedieMinusPrecizie;
				curentOptim = curentMediu - precizie;
				// Daca am gasit in stanga, ajustez limita superioara pentru a
				// cauta in continuare in intervalul (minCurent, curentMediu)
				maxCurent = curentMediu;
				continue;
			}
			// 3. Daca puterea minima pentru mijloc + 0.1 (dreapta) > puterea minima optima
			if (putereMediePlusPrecizie > putereMinimaOptima) {
				putereMinimaOptima = putereMediePlusPrecizie;
				curentOptim = curentMediu + precizie;
				// daca am gasit in dreapta, ajustez limita inferioara pentru a
				// cauta in continuare in intervalul (curentMediu, maxCurent)
				minCurent = curentMediu;
				continue;
			}
			// Dupa ce am stabilit curentul si puterea minima optima
			// vreau sa aflu cum sa continui cautarea
			// Ma voi folosi de marginea inferioara (puteMediuMinusPrecizie)
			// drept criteriu de comparatie
			if (putereMedie > putereMedieMinusPrecizie) {
				// Am gasit o putere mai mare in dreapta
				minCurent = curentMediu; // Mergem in partea dreapta
			} else {
				// Am gasit o putere mai mare in stanga
				maxCurent = curentMediu; // Mergem in partea stanga
			}
		}
		// Returnez serverul cu curentul optim
		Server rezultatServer = new Server(putereMinimaOptima, curentOptim);
		return rezultatServer;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("servere.in"));
		PrintWriter writer = new PrintWriter("servere.out");
		int n = Integer.parseInt(reader.readLine());
		Server[] servere = new Server[n];

		StringTokenizer serverePuteri = new StringTokenizer(reader.readLine());
		StringTokenizer serverePraguri = new StringTokenizer(reader.readLine());

		double minCurent = Double.MAX_VALUE;
		double maxCurent = Double.MIN_VALUE;

		for (int i = 0; i < n; i++) {
			double putereMaxima = Double.parseDouble(serverePuteri.nextToken());
			double pragCurent = Double.parseDouble(serverePraguri.nextToken());
			servere[i] = new Server(putereMaxima, pragCurent);

			if (pragCurent < minCurent) {
				minCurent = pragCurent;
			}
			if (pragCurent > maxCurent) {
				maxCurent = pragCurent;
			}
		}
		reader.close();
		// Precizia cu care vreau sa caut serverul optim este 0.1
		// pentru ca mi se cere sa o afisez cu o singura zecimala
		double precizie = 0.1;
		// Caut serverul optim -> serverul cu puterea minima maxima pentru curentul optim
		Server serverOptim = cautaCurentOptim(servere, minCurent, maxCurent, precizie);
		writer.println(String.format("%.1f", serverOptim.putereMaxima));
		writer.close();
	}
}
