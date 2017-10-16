all: clean compile

go: all
	java MainWindow

clean:
	rm -f *.class *~

compile:
	javac *.java

tar:
	tar -cvf proj1.tar *.java Makefile Readme.txt

gzip:
	gzip proj1.tar

submit: tar gzip
