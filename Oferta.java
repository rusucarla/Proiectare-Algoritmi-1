import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Oferta {

	// fisiere de in si out
	private static final String INPUT_FILE = "oferta.in";
	private static final String OUTPUT_FILE = "oferta.out";

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE));
		String[] primaLinie = reader.readLine().split(" ");
		int N = Integer.parseInt(primaLinie[0]); // Numarul de produse
		int K = Integer.parseInt(primaLinie[1]); // Tipul de cerinta

		String[] liniaProduselor = reader.readLine().split(" ");
		int[] preturi = new int[N + 1];
		for (int i = 1; i <= N; i++) {
			preturi[i] = Integer.parseInt(liniaProduselor[i - 1]);
		}
		reader.close();

		// cazul K = 1 -> cel mai mic pret posibil
		// cazul K > 1 -> al k-lea cel mai mic pret posibil
		Oferta of = new Oferta();
		if (K == 1) {
			double costMinim = of.k1(preturi);
			of.scrieRezultat(costMinim);
		} else {
			if (N == 1 && K > 1) {
				of.scrieRezultat(-1);
			} else {
				double costK = of.k1_extended(preturi, N, K);
				if (costK == 0) {
					of.scrieRezultat(-1);
				} else {
					of.scrieRezultat(costK);
				}
			}
		}
	}

	private boolean amCost(double cost) {
		return cost != 0;
	}

	private boolean indexValid(int index, int K) {
		return index <= K;
	}

	private boolean updateProgres(double[] costuriPosibile,
		double costMinim, int[] indexii, int K) {
		boolean progres = false;
		for (int i = 1; i < costuriPosibile.length; i++) {
			if (costMinim == costuriPosibile[i] && indexValid(indexii[i], K)) {
				indexii[i]++;
				progres = true;
				break;
			}
		}
		return progres;
	}

	private void updateIndex(int[] indexii, int K, double[] costuriPosibile) {
		for (int i = 1; i < indexii.length; i++) {
			if (indexii[i] > K) {
				costuriPosibile[i] = Double.MAX_VALUE;
			}
		}
	}

	private double k1_extended(int[] preturi, int N, int K) {
		double[][] costPosibil = new double[N + 1][K + 1];
		int indexCost;
		// Cazul de baza : un singur produs
		costPosibil[1][1] = preturi[1];
		// Cazul de baza : doua produse
		// Pentru ca vreau sa fie sortate crescator, prima suma este cea cu
		// oferta pentru 2 produse
		costPosibil[2][1] = preturi[1] + preturi[2] - Math.min(preturi[1], preturi[2]) * 0.5;
		costPosibil[2][2] = preturi[1] + preturi[2];

		for (int i = 3; i <= N; i++) {
			// indexii[1] este indexul pentru costurile calculate pentru
			// produsul precedent (i-1)
			// indexii[2] este indexul pentru costurile calculate pentru
			// produsul preprecedent (i-2)
			// indexii[3] este indexul pentru costurile calculate pentru
			// produsul prepreprecedent (i-3)
			int[] indexii = { 0, 1, 1, 1 };
			// Pentru fiecare index, este un cost asociat
			double[] costuriPosibile = { 0, 0, 0, 0 };
			// Pentru a incerca sa nu prea repet costurile, folosesc un boolean care verifica
			// daca am repetat costul precedent si daca nu am facut niciun
			// progres, caz in care, ies din bucla
			boolean progresCost = false;
			double costPrecedent = -1;
			// indexCost e doar coloana cu care ne miscam prin linia i
			indexCost = 1;
			while (indexValid(indexCost, K)) {
				// Fiecare iteratie va porni cu premiza ca nu s-a facut niciun progres
				progresCost = false;
				// In cazul in care am terminat de parcurs vreuna dintre linii,
				// vrem sa o "scoatem din joc" si-atunci ii dam valoarea infinit
				// costului posibil pentru ca nu vrem sa fie luat in considerare
				updateIndex(indexii, K, costuriPosibile);
				// Verificam daca putem sa adunam costul produsului i la cea mai
				// buna oferta pentru i-1
				if (indexValid(indexii[1], K) && amCost(costPosibil[i - 1][indexii[1]])) {
					costuriPosibile[1] = costPosibil[i - 1][indexii[1]] + preturi[i];
				}
				// Verificam daca putem sa adunam costul ofertei de 2 produse
				// dintre i si i-1 la cea mai buna oferta pentru i-2
				if (indexValid(indexii[2], K) && amCost(costPosibil[i - 2][indexii[2]])) {
					double costOferta = preturi[i] + preturi[i - 1]
						- Math.min(preturi[i], preturi[i - 1]) * 0.5;
					costuriPosibile[2] = costPosibil[i - 2][indexii[2]] + costOferta;
				}
				// Verificam daca putem sa adunam costul ofertei de 3 produse
				// dintre i, i-1 si i-2 la cea mai buna oferta pentru i-3
				// In cazul in care i == 3, nu exista un cost pentru i-3, asa ca
				// sarim peste
				double pretMinim = Math.min(Math.min(preturi[i], preturi[i - 1]), preturi[i - 2]);
				double costOferta = preturi[i] + preturi[i - 1] + preturi[i - 2] - pretMinim;
				if (i == 3) {
					costuriPosibile[3] = costOferta;
				} else if (indexValid(indexii[3], K) && amCost(costPosibil[i - 3][indexii[3]])) {
					costuriPosibile[3] = costPosibil[i - 3][indexii[3]] + costOferta;
				}
				// Pentru ca vreau sa pastrez K cele mai mici costuri, aleg
				// minimul dintre cele 3 costuri posibile
				costPosibil[i][indexCost] =
					Math.min(Math.min(costuriPosibile[1], costuriPosibile[2]), costuriPosibile[3]);
				// Folosind un fel de interclasare, verificam care dintre
				// produse ne-a adus cel mai mic cost si incrementam indexul
				// pentru acel produs, pentru a putea calcula un nou cost care
				// sa fie ulterior comparat cu celelalte
				progresCost = updateProgres(costuriPosibile, costPosibil[i][indexCost], indexii, K);
				// Daca am gasit un cost nou, incrementez indexCost pentru a
				// trece la urmatoarea coloana
				if (costPosibil[i][indexCost] != costPrecedent) {
					costPrecedent = costPosibil[i][indexCost++];
				} else {
					// Daca am gasit un cost duplicat si care nu a starnit vreun
					// progres : din cauza faptului ca indexul costului minim > K
					if (!progresCost) {
						break;
					}
					// Altfel, trec inca o data prin bucla pentru a verifica
					// daca nu gasesc alt cost minim
				}
			}
		}
		// returnez cel mai mare dintre cele K cele mai mici costuri
		return costPosibil[N][K];
	}

	private void scrieRezultat(double rezultat) throws IOException {
		PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE));
		writer.printf("%.1f\n", rezultat);
		writer.close();
	}

	private double k1(int[] preturi) {
		// +1 pentru indexCost1 putea accesa direct cu indexul N
		double[] costMinim = new double[preturi.length + 1];
		// Costul de a cumpara 0 produse este 0
		costMinim[0] = 0;

		for (int i = 1; i < preturi.length; i++) {
			costMinim[i] = costMinim[i - 1] + preturi[i];

			// Verific daca putem aplica oferta pentru 2 produse
			if (i > 1) {
				double costOferta2 = preturi[i] + preturi[i - 1]
					- Math.min(preturi[i], preturi[i - 1]) * 0.5;
				double nouCost = costMinim[i - 2] + costOferta2;
				costMinim[i] = Math.min(costMinim[i], nouCost);
			}

			// Verific daca putem aplica oferta pentru 3 produse
			if (i > 2) {
				int pretMinim = Math.min(preturi[i], Math.min(preturi[i - 1], preturi[i - 2]));
				double costOferta3 = preturi[i] + preturi[i - 1] + preturi[i - 2] - pretMinim;
				double nouCost = costMinim[i - 3] + costOferta3;
				costMinim[i] = Math.min(costMinim[i], nouCost);
			}
		}
		return costMinim[preturi.length - 1];
	}

}
