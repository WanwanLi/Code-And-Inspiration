CFLAGS=-Wall -g -O0

all:
	gcc -c image.c
	gcc -c Stego.c
	gcc -c StegoExtract.c
	gcc -o Stego Stego.o image.o
	gcc -o StegoExtract StegoExtract.o image.o


Stego:Stego.c image.c
	gcc -c image.c
	gcc -c Stego.c
	gcc -o Stego Stego.o image.o


StegoExtract:StegoExtract.c image.c
	gcc -c StegoExtract.c
	gcc -o StegoExtract StegoExtract.o image.o


clean:
	rm -f Stego
	rm -f StegoExtract
	rm -f *.o;
