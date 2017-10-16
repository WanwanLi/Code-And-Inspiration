#include "LexicalAnalyser.h"
#include "SyntaxTranslator.h"
#include "VirtualMachine.h"
int main(int argc,String* argv)
{
/*
FILE* file=fopen("b.txt","r");
fprintf(file,"%s\n","asdf");
fclose(file);
String s;
file=fopen("a.txt","w");
fscanf(file,"%s\n",s);
fclose(file);
printf("s=%s\n",s);
*/
	String className=argv[1];
	LexicalAnalyser* LexicalAnalyser1=newLexicalAnalyser(className);
//	printWordTable(LexicalAnalyser1);
	SyntaxTranslator* SyntaxTranslator1=newSyntaxTranslator(LexicalAnalyser1);
	generateVirtualMachineInstructions(SyntaxTranslator1);
//printf("VM started..\n");
	VirtualMachine* VirtualMachine1=newVirtualMachine(className);
//printf("VM loaded..\n");
	printVirtualMachineInstructions(VirtualMachine1);
//printf("VM printed..\n");
	run(VirtualMachine1);
	return 0;
}

