#include "List.h"
#include "Word.h"
#include "Vecmath.h"
#include "Instructions.h"
#include "LexicalAnalyser.h"
#include "SyntaxTranslator.h"
#include "VirtualMachine.h"
int main(int argc,String* argv)
{
	String className=argv[1];
	String fileName=add(className,".javab");
	FILE* file=fopen(fileName,"r");
	if(file!=NULL)
	{
		fclose(file);
		LexicalAnalyser* LexicalAnalyser1=newLexicalAnalyser(className);
//		printWordTable(LexicalAnalyser1);
		SyntaxTranslator* SyntaxTranslator1=newSyntaxTranslator(LexicalAnalyser1);
		generateVirtualMachineInstructions(SyntaxTranslator1);
		printVirtualMachineInstructions(SyntaxTranslator1);
	}
//printf("VM started..\n");
	VirtualMachine* VirtualMachine1=newVirtualMachine(className);
//printf("VM loaded..\n");
//	printVirtualMachineInstructions(VirtualMachine1);
//printf("VM printed..\n");
	run(VirtualMachine1);
	return 0;
}

