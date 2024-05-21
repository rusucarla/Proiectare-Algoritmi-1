# README

## 1. Servere

Aici am definit o clasa `Server` care are ca atribute o `putereMaxima` si un
`pragCurent`.

Cum precizeaza si comentariile din cod, `precizie` este 0.1 influentat de
precizia ceruta pentru a afisa rezultatul final.

Functia `cautaCurentOptim` face exact ce spune, cauta pragul optim pentru a
maximiza puterea minima a serverelor.

Am folosit o cautare binara pentru a gasi pragul optim. Pentru fiecare valoare
de mijloc am calculat 3 puteri minime:

1. puterea minima pentru valoarea de mijloc
2. puterea minima pentru valoarea de mijloc - 0.1
3. puterea minima pentru valoarea de mijloc + 0.1

Cu aceste valori determin `curentOptim` si `putereMinimaOptima` si ajustez
limitele intervalului de cautare.

Pe langa aceste comparatii de mai jos, un criteriu de comparatie pentru a determina marginile intervalului de cautare
este `putereMedieMinusPrecizie` care poate fi interpretata ca o margine
inferioara.

Un alt lucru important de mentionat este ca am schimbat tipul de date pentru
putere si curent de la `int` la `Double` pentru a imi fi mai usor sa lucrez cu
valorile lor si faptul ca rezultatul final este un numar real.

In plus, functia de ridicare la putere in timp logaritmic a fost luata de pe
acest [site](https://www.geeksforgeeks.org/write-a-c-program-to-calculate-powxn/).

### Complexitate Servere

Citirea datelor are complexitatea $O(n)$, unde n este numarul de servere.

Folosind o cautare binara, complexitatea este $O(\log n)$ in mod normal, aici
mai explicit ar fi $O(\log{\frac{(\text{maxCurent - minCurent})}{\text{precizie
(0.1)}}})$

Pentru fiecare pas al cautarii binare, apelez functia `calculeazaPutereaMinima`
(de 3 ori) care face o parcurgere liniara a vectorului de servere, deci complexitatea este
de $O(n)$.

In concluzie, complexitatea totala este $O(n \cdot \log{\frac{(\text{maxCurent - minCurent})}{\text{precizie
(0.1)}}})$.

Complexitatea spatiala este $O(n)$, deoarece tinem in memorie vectorul de servere.

## 2. Colorare

Pentru prima zona, avem 3 culori disponibile si in functie de tipul zonei putem
avea 6 (H) sau 3(V) combinatii de culori.

Am decis sa fac distinctia intre prima zona a fisierului de intrare si restul de
prime zona care urmeaza in grupurile consecutive.

Astfel incat pentru "prima" zona, am 2 cazuri:

1. este prima din fisierul de intrare (am detaliat mai sus despre numarul de
   combinatii de culori rezultante)
2. este prima din grupul dintr-un grup zone consecutive

Pentru restul de zone, luam in calcul faptul ca zona precedenta este constanta
si este definita de tipul primei zone din grup si faptul ca numarul de zone s-a micsoarat cu 1.

Exemplu pentru cum ar arata cazurile pentru combinatii de zone precum:

### H H

1. Facem flip la culori

    | Tip zona | H | H |
    |----------|---|---|
    | Culori   | 1 | 2 |
    | Culori   | 2 | 1 |

2. Folosim una dintre culorile nefolosite

    | Tip zona | H | H |
    |----------|---|---|
    | Culori   | 1 | 3 |
    | Culori   | 2 | 1 |

3. Tot ca 2 dar in alt dreptunghi

    | Tip zona | H | H |
    |----------|---|---|
    | Culori   | 1 | 2 |
    | Culori   | 2 | 3 |

### V H

1. Folosesc culorile nefolosite de zona V

    | Tip zona | V | H |
    |----------|---|---|
    | Culori   | 1 | 2 |
    | Culori   | 1 | 3 |

2. Sau

    | Tip zona | H | H |
    |----------|---|---|
    | Culori   | 1 | 3 |
    | Culori   | 1 | 2 |

### V V

1. Folosesc culorile nefolosite de zona V

    | Tip zona | V | V |
    |----------|---|---|
    | Culori   | 1 | 2 |
    | Culori   | 1 | 2 |

2. Sau

    | Tip zona | V | V |
    |----------|---|---|
    | Culori   | 1 | 3 |
    | Culori   | 1 | 3 |

### H V

1. Folosesc culoarea nefolosita de zona H

    | Tip zona | H | V |
    |----------|---|---|
    | Culori   | 1 | 2 |
    | Culori   | 3 | 2 |

Aici am decis in program sa nu mai inmultesc deoarece $\cdot 1$ este redundant.

Astfel ca folosesc aceste cazuri pentru a determina numarul de combinatii de
culori atat pentru prima zona din grup cat si pentru cele care urmeaza,
diferenta fiind ca la restul din grup foloesc o fucntie de ridicare la putere cu
complexitate $O(\log n)$.

### Complexitate Colorare

Pentru citirea datelor, complexitatea este $O(K)$, unde K este numarul de
perechi.

In bucla principala, iterez de la 1 la K, astfel ca reiese o complexitate de
$O(K)$.

Inauntrul buclei, majoritatea operatiilor sunt doar inmultiri si operatii de
gradul 1, astfel ca complexitatea lor este $O(1)$. Pentru cele care folosesc
pow-ul (varianta mai eficienta) complexitatea acesteia este $O(\log b)$, unde b
este exponentul (in cazul asta numarul de zone ramase in grup).

Astfel, per total din bucla ar reiesi o complexitate de $O(K \cdot \log X)$,
unde X este numarul de zone consecutive din grup (fara prima, cum este detaliat
si mai sus, sau in comentariile din cod).

Complexitatea spatiala este $O(K)$, deoarece tinem in memorie vectorul de zone.

## 3. Compresie

La aceasta problema am folosit o parcurgere asemenatoare cu cea de la o interclasare a doi vectori.

Pentru a determina elementele care ar fi putut fi comprimate am construit sume
pentru fiecare element din vectorii A si B (`sumaA` si `sumaB`).

Daca ajungeau la o egalitate, asta inseamna ca se pot comprima elemente pana la
acea pozitie pentru a avea aceeasi suma si numar de elemente. Nu am modificat
vectorii pentru ca puteam sa ii modific simbolic si doar sa adun la contorul
`lungime` care tine minte numarul de elemente care ar fi in vectorul final
(rezultat din comprimarea celor doi vectori).

Tot in spiritul interclasarii, daca epuizam unul dintre vectori, cautam si in
restul celuilalt vector elementele care ar fi putut fi comprimate.

Astfel ca am folosit variabila booleana `ultimaCompresie` pentru a verifica
aceasta posibilitate.

### Complexitate Compresie

Citirea datelor are complexitatea $O(n + m)$, unde n si m sunt numarul de
elemente din vectorii A si B.

Complexitatea temporala a buclei este de asemenea $O(n + m)$ pentru ca fiecare
element al vectorilor este vizualizat cel putin o data (restul operatiilor sunt
de complexitate $O(1)$).

In plus, consider bucla mare sa incorporeze si cele 2 bucle mici pentru rest pentru ca se completeaza numarul de elemente astfel.

Per total, timpul de executie creste liniar cu numarul de
elemente din vectorii A si B.

Complexitate spatiala are aceeasi complexitate ca si cea temporala, $O(n + m)$,
deoarece tinem minte doar elementele care sunt in vectorii A si B.

## 4. Criptat

Pentru aceasta problema am folosit o abordare Greedy cu cateva adaosuri.

Prima idee a fost sa gasesc un criteriu pentru a putea maximiza lungimea si de a
nu incalca pragul de $> 50\%$ cand vine vorba de litera dominanta.

Acest criteriu a fost denumit `impact` si este calculat:
$\frac{\text{numar aparitii litera in cuvant}}
{\text{numar litere cuvant}}$

Mi-am dat seama intr-un timp destul de scurt ca litera cea mai des intalnita
per total in lista nu este si cea care poate fi folosita pentru a maximiza
lungimea parolei si a nu incalca pragul.

Astfel am parcurs toate literele unice din lista (in cazul cel mai nefavorabil 8
la numar dupa cum indica cerinta) si am calculat cea mai buna parola posibila
pentru acea litera.

Pentru fiecare litera a fost calculat impactul acelei litere in fiecare cuvant
si sortata lista de cuvinte in ordine descrescatoare.

Pentru a tine cont de niste cazuri speciale si pentru a grabi in unele cazuri
executia programului am folosit 2 verificari :

1. Daca litera nu apare in niciun cuvant de mai mult de 50% din lungimea
   cuvantului atunci nu o folosesc, pentru ca este clar ca dupa sortarea
   cuvintelor in favorea literei respective, nu are destule aparitii per total
   cat sa treaca de pragul de 50%.

2. Pentru a ma asigura ca aleg cea mai buna combinatie posibila, am decis sa am
   o variabila `Best` unde aleg cea mai buna optiune dintre cele 3 posibilitati :
   1. E cel mai bine sa punem doar cuvantul curent din lista
   2. E cel mai bine sa punem atat cuvantul curent din lista cat si cuvantul urmator
   3. E cel mai bine sa punem doar cuvantul urmator din lista

Aceste verificari sunt facute pentru a maximiza lungimea parolei si a minimiza
numarul de verificari inutile.

Cel mai bine este ilustrata gandirea in parcurgerea unui exemplu:

Il vom alege pe cel din cerinta cu lista de cuvinte `too, otter, tote, oo`:

``` c
Frecventa literelor: {r=1, t=5, e=2, o=6}
Literele unice sunt: [r, t, e, o]

Procesez litera: r

Cel mai mare impact este: 0.2 Nu am nicio sansa sa creez o parola valida

Procesez litera: t

Cel mai mare impact este: 0.5 Nu am nicio sansa sa creez o parola valida

Procesez litera: e

Cel mai mare impact este: 0.25 Nu am nicio sansa sa creez o parola valida

Procesez litera: o

i =  0 litera  = o
Cuvantul curent: oo are impactul: 1.0 si lungimea potentiala: 2
Cuvantul urmator: too are impactul: 0.6666666666666666 si lungimea potentiala: 3
Impactul lor impreuna: 0.8 si lungimea potentiala: 5
Cuvantul BEST: ootoo cu impactul: 0.8
i =  2 litera  = o
Cuvantul curent: tote are impactul: 0.5555555555555556 si lungimea potentiala: 9
Cuvantul urmator: otter are impactul: 0.5 si lungimea potentiala: 10
Impactul lor impreuna: 0.42857142857142855 si lungimea potentiala: 14
Cuvantul BEST: tote cu impactul: 0.5555555555555556
i =  3 litera  = o
Cuvantul curent: otter are impactul: 0.42857142857142855 si lungimea potentiala: 14
Cuvantul BEST: otter cu impactul: 0.42857142857142855
Parola finală pentru litera 'o' este: ootootote cu lungimea: 9

Parola finala este: ootootote cu un numar de litere de: 9
```

### Complexitate Criptat

Citirea datelor are complexitatea $O(n)$, unde n este numarul de cuvinte.

Pentru fiecare cuvant citit se calculeaza frecventa literelor, deci
complexitatea este de $O(N \cdot M)$, unde N este numarul de cuvinte si M este
numarul de litere din fiecare cuvant (In cerinta spune ca M este maxim 8).

Pentru a calcula impactul fiecarei litere, complexitatea este de $O(M)$.

Sortarea cuvintelor in functie de impact are complexitatea $O(N \cdot \log N)$.
Pentru toate sortarile in functie de literele unice, ar deveni $O(M \cdot N \cdot \log N)$.

Bucla pentru a construi parola pentru fiecare litera, care implica calculul
impactului, sortarea cuvintelor in functie de impact si parcurgerea listeri are
complexitatea aproximativ egala cu sortarea, deoarece extragerile si
comparatiile sunt $O(1)$.

Complexitatea spatiala este proportionala cu numarul de cuvinte si numarul de
litere unice din lista, deci $O(N \cdot M)$.

## 5. Oferta

Aceasta problema are 2 cerinte:

### K = 1

Prima cerinta (K=1) vrea sa gaseasca costul minim pentru a cumpara `N` produse.

Am folosit cum era si vagamente sugerat de cerinta, o abordare de programare
dinamica.

Am folosit un vector `costMinim` pentru a tine minte costul minim de a cumpara
`i` produse.

Pentru fiecare produs, am vazut ce oferte sunt disponibile si am calculat costul
minim pentru a cumpara toate produsele pana la `i`.

### K > 1

A doua cerinta (K>1) vrea sa gaseasca al `K`-lea cel mai mic cost pentru a
cumpara `N` produse.

Am folosit o matrice `costPosibil` care are N linii si K coloane pentr a tine
minte pentru fiecare linie cele mai mici K costuri necesare pentru a cumpara a
cumpara `numarul liniei` produse.

Primele 2 linii sunt cazuri de baza pentru a putea calcula restul matricei.
Pentru a cumpara un produs, se va introduce doar costul produsului respectiv.

Pentru a cumpara 2 produse si pentru a pastra ordinea crescatoare a costurilor,
se va introduce costul ofertei pentru 2 produse si costul pentru a cumpara
produsele separat.

In continuare, va fi calculat costul minim pentru fiecare coloana din linia
respectiva cu ajutorul catorva variabile auxiliare.

Pentru fiecare coloana se vor calcula 3 costuri posibile :

- costul de a cumpara produsul separat adunat cu costurile minime din linia
  precedenta (pentru a reusi sa cumpar cele `i` produse)
- costul ofertei pentru 2 produse (`i` si `i-1`) adunat cu costurile minime din
  linia care reprezinta `i-2` produse
- costul ofertei pentru 3 produse (`i`, `i-1` si `i-2`) adunat cu costurile minime
  din linia care reprezinta `i-3` produse (in cazul in care `i == 3` atunci se
  va lua doar costul ofertei pentru 3 produse)

Pentru aceste 3 costuri se va alege minimul si se va introduce in matrice.
Totusi, se mai face o verificare pentru a nu introduce pe cat se poate costuri
duplicate in matrice: `progresCost` si `costPrecedent`.

`progresCost` este o variabila pentru a verifica daca:

- index-ul liniei din care vine costul minim are inca o valoare valida
  (mai mica decat K)
- parcurg corect linia din care vine costul minim (daca am reusit sa obtin un
  cost minim cu acea coloana, vreau sa trec la urmatoarea coloana)

`costPrecedent` este o variabila care pe parcursul calculului costurilor minime
dintr-o linie va tine costul calculat in parcurgrea anterioara a buclei de
coloane.

Exista cazuri in care chiar daca costul minim este duplicat, sa se fi obtinut un
progres al index-ilor si atunci la o reluare a buclei, sa se gaseasca alt cost
minim. Totusi, daca nu exista progres, atunci se va iesi din bucla si se va
trece la urmatoarea linie.

### Complexitate Oferta

Citirea datelor are complexitatea $O(n)$, unde n este numarul de produse.

Pentru prima cerinta, complexitatea este $O(n)$, deoarece parcurg toate
produsele o singura data.

Pentru a doua cerinta, complexitatea este $O(n \cdot K)$, deoarece pentru
fiecare produs se vor parcurge cele K coloane ale matricei.

Complexitatea spatiala pentru a doua cerinta este $O(n \cdot K)$, deoarece tinem in memorie matricea
de costuri minime, iar pentru prima este doar $O(n)$ pentru vectorul in care
tinem costurile minime.
