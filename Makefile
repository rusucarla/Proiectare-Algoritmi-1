.PHONY: build clean

build: Servere.class Colorare.class Compresie.class Criptat.class Oferta.class

run-p1:
	java Servere
run-p2:
	java Colorare
run-p3:
	java Compresie
run-p4:
	java Criptat
run-p5:
	java Oferta

Servere.class: Servere.java
	javac $^
Colorare.class: Colorare.java
	javac $^
Compresie.class: Compresie.java
	javac $^
Criptat.class: Criptat.java
	javac $^
Oferta.class: Oferta.java
	javac $^

clean:
	rm -f *.class
