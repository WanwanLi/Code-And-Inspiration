gcc -c image.c
gcc -c Stego.c
gcc -c StegoExtract.c
gcc -o Stego Stego.o image.o
gcc -o StegoExtract StegoExtract.o image.o
Stego.exe cs262.pgm dest.pgm file.txt
StegoExtract.exe dest.pgm result.txt
notepad result.txt
