import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Compresie {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("compresie.in"));

		String line = reader.readLine();
		int n = Integer.parseInt(line); // Lungimea sirului A

		int[] A = new int[n];
		line = reader.readLine();
		String[] elements = line.split(" ");
		for (int i = 0; i < n; i++) {
			A[i] = Integer.parseInt(elements[i]);
		}

		line = reader.readLine();
		int m = Integer.parseInt(line); // Lungimea sirului B

		int[] B = new int[m];
		line = reader.readLine();
		elements = line.split(" ");
		for (int i = 0; i < m; i++) {
			B[i] = Integer.parseInt(elements[i]);
		}
		reader.close();

		// Indicii pentru parcurgerea sirurilor A si B
		int i = 0, j = 0;
		// Suma elementelor din sirurile A si B
		long sumaA = 0, sumaB = 0;
		// Lungimea secventei comune
		int lungime = 0;
		// boolean care are ca scop sa determine daca se mai pot realiza
		// compresii in cazul in care unul dintre siruri a fost epuizat
		boolean ultimaCompresie = false;

		while (i < n && j < m) {
			// Cazul in care am reusit sa gasim o secventa comuna
			// astfel ca putem "comprima" sirurile
			if (sumaA == sumaB && sumaA != 0) {
				sumaA = sumaB = 0;
				lungime++; // Incrementam lungimea secventei comune
			}
			// Verific relatia dintre sume pentru a determina cu care index
			// sa continui parcurgerea
			if (sumaA <= sumaB) {
				// Daca mai sunt elemente in sirul A
				if (i < n) {
					sumaA += A[i];
					i++;
				}
			} else {
				// Daca mai sunt elemente in sirul B
				if (j < m) {
					sumaB += B[j];
					j++;
				}
			}
		}

		// Daca mai sunt elemente in sirul A si sumele nu sunt egale
		while (i < n && sumaA < sumaB) {
			sumaA += A[i++];
			// Daca am ajuns aici, inseamna ca am facut o ultima compresie
			ultimaCompresie = true;
		}
		// Daca mai sunt elemente în sirul B
		while (j < m && sumaB < sumaA) {
			sumaB += B[j++];
			ultimaCompresie = true;
		}

		// Aici verific nu numai faptul ca sumele sunt egale, dar si daca nu
		// cumva unul dintre siruri nu are un rest care nu a fost "explorat"
		if (sumaA == sumaB && ultimaCompresie) {
			lungime++;
		} else {
			// Daca am ajuns aici inseamna ca ori nu am gasit nicio secventa
			// comuna, ori am gasit una, dar nu am putut sa compresam destul
			// sirurile astfel incat sa ajunga cu acelasi numar de elemente
			lungime = -1;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("compresie.out"))) {
			writer.write(String.valueOf(String.valueOf(lungime)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
