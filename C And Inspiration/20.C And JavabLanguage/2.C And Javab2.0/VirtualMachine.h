#define VirtualMachine struct VirtualMachine
VirtualMachine
{
	String className;
	String* methodTable;
	int* methodPointerTable;
	int* methodReturnTypeTable;
	int methodNumber;
	int objectNumber;
	int classNumber;
	String* classTable;
	VirtualMachine** virtualMachineTable;
	int identifierNumber;
	int* identifierTable;
	int integerNumber;
	int integerTableLength;
	int* integerTable;
	int doubleNumber;
	int doubleTableLength;
	double* doubleTable;
	int stringNumber;
	int stringTableLength;
	String* stringTable;
	int declarationsLength;
	int* declarations;
	int beginInstructionPointer;
	int instructionsLength;
	int** instructions;
	int instructionPointer;
	List* queue;
	List* stack;
	FILE* file;
};
void run(VirtualMachine* virtualMachine);
VirtualMachine* newVirtualMachine_Class(String className,List* queue,List* stack);
VirtualMachine* newVirtualMachine_Object(VirtualMachine* virtualMachine_Class);
//void printVirtualMachineInstructions(VirtualMachine* virtualMachine);
VirtualMachine* newVirtualMachine(String className)
{
	VirtualMachine* virtualMachine=(VirtualMachine*)malloc(sizeof(VirtualMachine));
	virtualMachine->className=className;
	String fileName=add(className,".classb");
	FILE* file=fopen(fileName,"r");
	if(file==NULL){printf("File:%s.classb dose not exist...",className);exit(0);}int i;
	fscanf(file,"%d,",&virtualMachine->methodNumber);
	virtualMachine->methodTable=newString(virtualMachine->methodNumber);
	for(i=0;i<virtualMachine->methodNumber;i++)virtualMachine->methodTable[i]=fscans(file);
	virtualMachine->methodPointerTable=newInt(virtualMachine->methodNumber);
	for(i=0;i<virtualMachine->methodNumber;i++)fscanf(file,"%d,",&virtualMachine->methodPointerTable[i]);
	virtualMachine->methodReturnTypeTable=newInt(virtualMachine->methodNumber);
	for(i=0;i<virtualMachine->methodNumber;i++)fscanf(file,"%d,",&virtualMachine->methodReturnTypeTable[i]);
	fscanf(file,"%d,",&virtualMachine->objectNumber);
	fscanf(file,"%d,",&virtualMachine->classNumber);
	virtualMachine->classTable=newString(virtualMachine->classNumber);
	for(i=0;i<virtualMachine->classNumber;i++)virtualMachine->classTable[i]=fscans(file);
	virtualMachine->virtualMachineTable=(VirtualMachine**)malloc((virtualMachine->classNumber+virtualMachine->objectNumber)*sizeof(VirtualMachine*));
	fscanf(file,"%d,",&virtualMachine->identifierNumber);
	virtualMachine->identifierTable=newInt(virtualMachine->identifierNumber);
	for(i=0;i<virtualMachine->identifierNumber;i++)virtualMachine->identifierTable[i]=0;
	fscanf(file,"%d,",&virtualMachine->integerNumber);
	virtualMachine->integerTable=newInt(virtualMachine->integerNumber);
	fscanf(file,"%d,",&virtualMachine->integerTableLength);
	for(i=0;i<virtualMachine->integerTableLength;i++)fscanf(file,"%d,",&virtualMachine->integerTable[i]);
	fscanf(file,"%d,",&virtualMachine->doubleNumber);
	virtualMachine->doubleTable=newDouble(virtualMachine->doubleNumber);
	fscanf(file,"%d,",&virtualMachine->doubleTableLength);
	for(i=0;i<virtualMachine->doubleTableLength;i++)virtualMachine->doubleTable[i]=atof(fscans(file));
	fscanf(file,"%d,",&virtualMachine->stringNumber);
	virtualMachine->stringTable=newString(virtualMachine->stringNumber);
	fscanf(file,"%d,",&virtualMachine->stringTableLength);
	for(i=0;i<virtualMachine->stringTableLength;i++)virtualMachine->stringTable[i]=fscans(file);
	fscanf(file,"%d,",&virtualMachine->declarationsLength);
	virtualMachine->declarations=newInt(virtualMachine->declarationsLength);
	for(i=0;i<virtualMachine->declarationsLength;i++)fscanf(file,"%d,",&virtualMachine->declarations[i]);
	fscanf(file,"%d,",&virtualMachine->beginInstructionPointer);
	fscanf(file,"%d,",&virtualMachine->instructionsLength);
	int instructionsListLength;
	fscanf(file,"%d,",&instructionsListLength);
	virtualMachine->instructions=toInstructions(readIntegers(file,instructionsListLength),virtualMachine->instructionsLength);
	fclose(file);
	virtualMachine->queue=newList();
	virtualMachine->stack=newList();
	for(i=0;i<virtualMachine->classNumber;i++)virtualMachine->virtualMachineTable[i]=newVirtualMachine_Class(virtualMachine->classTable[i],virtualMachine->queue,virtualMachine->stack);
	return virtualMachine;
}
/*
void printVirtualMachineInstructions(VirtualMachine* virtualMachine)
{
	String fileName=add(virtualMachine->className,".asmb");
	FILE* file=fopen(fileName,"w");
	if(file==NULL){printf("ERROR: Can not write File: %s ...\n",fileName);return;}int i;
//	String* methodStrings=virtualMachinemethodList.getStrings();
//	fprintf(file,"methodStringsLength=%d\n",methodStringsLength);
//	for(i=0;i<virtualMachine->methodStringsLength;i++)fprintf(file,"methodStrings[%d]=%s\n",i,virtualMachine->methodStrings[i]);

	fprintf(file,"objectNumber=%d\n",virtualMachine->objectNumber);
	fprintf(file,"classNumber=%d\n",virtualMachine->classNumber);
	for(i=0;i<virtualMachine->classNumber;i++)fprintf(file,"classTable[%d]=%s\n",i,virtualMachine->classTable[i]);
	fprintf(file,"identifierNumber=%d\n",virtualMachine->identifierNumber);
	fprintf(file,"integerNumber=%d\n",virtualMachine->integerNumber);
	fprintf(file,"integerTableLength=%d\n",virtualMachine->integerTableLength);
	for(i=0;i<virtualMachine->integerTableLength;i++)fprintf(file,"integerTable[%d]=%d\n",i,virtualMachine->integerTable[i]);
	fprintf(file,"stringNumber=%d\n",virtualMachine->stringNumber);
	fprintf(file,"stringTableLength=%d\n",virtualMachine->stringTableLength);
	for(i=0;i<virtualMachine->stringTableLength;i++)fprintf(file,"stringTable[%d]=%s\n",i,virtualMachine->stringTable[i]);
	fprintf(file,"doubleNumber=%d\n",virtualMachine->doubleNumber);
	fprintf(file,"doubleTableLength=%d\n",virtualMachine->doubleTableLength);
	for(i=0;i<virtualMachine->doubleTableLength;i++)fprintf(file,"doubleTable[%d]=%f\n",i,virtualMachine->doubleTable[i]);
	fprintf(file,"declarationsLength=%d\n",virtualMachine->declarationsLength);
	for(i=0;i<virtualMachine->declarationsLength;i++)fprintf(file,"declarations[%d]=%d\n",i,virtualMachine->declarations[i]);
	fprintf(file,"beginInstructionPointer=%d\n",virtualMachine->beginInstructionPointer);
	fprintf(file,"instructionsLength=%d\n",virtualMachine->instructionsLength);
	printInstructions(file,virtualMachine->instructions,virtualMachine->instructionsLength);
	fclose(file);
}
*/
int executeInstruction_getInteger(VirtualMachine* virtualMachine,char ic,int id)
{
	switch(ic)
	{
		case ' ':return id;
		case '@':return virtualMachine->integerTable[id];
		case '&':return virtualMachine->integerTable[virtualMachine->identifierTable[id]];
		case ':':return virtualMachine->integerTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]];

		case '#':return (int)virtualMachine->doubleTable[id];
		case '^':return (int)virtualMachine->doubleTable[virtualMachine->identifierTable[id]];
		case '!':return (int)virtualMachine->doubleTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]];

		case '$':return atoi(virtualMachine->stringTable[id]);
		case '*':return atoi(virtualMachine->stringTable[virtualMachine->identifierTable[id]]);
		case '?':return atoi(virtualMachine->stringTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]);
	}
}
double executeInstruction_getDouble(VirtualMachine* virtualMachine,char ic,int id)
{
	switch(ic)
	{
		case '#':return virtualMachine->doubleTable[id];
		case '^':return virtualMachine->doubleTable[virtualMachine->identifierTable[id]];
		case '!':return virtualMachine->doubleTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]];

		case '$':return atof(virtualMachine->stringTable[id]);
		case '*':return atof(virtualMachine->stringTable[virtualMachine->identifierTable[id]]);
		case '?':return atof(virtualMachine->stringTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]);

		case ' ':return 0.0+id;
		case '@':return 0.0+virtualMachine->integerTable[id];
		case '&':return 0.0+virtualMachine->integerTable[virtualMachine->identifierTable[id]];
		case ':':return 0.0+virtualMachine->integerTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]];
	}
}
String executeInstruction_getString(VirtualMachine* virtualMachine,char ic,int id)
{
	switch(ic)
	{
		case '$':return virtualMachine->stringTable[id];
		case '*':return virtualMachine->stringTable[virtualMachine->identifierTable[id]];
		case '?':return virtualMachine->stringTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]];

		case ' ':return dtoa(id);
		case '@':return dtoa(virtualMachine->integerTable[id]);
		case '&':return dtoa(virtualMachine->integerTable[virtualMachine->identifierTable[id]]);
		case ':':return dtoa(virtualMachine->integerTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]);

		case '#':return ftoa(virtualMachine->doubleTable[id]);
		case '^':return ftoa(virtualMachine->doubleTable[virtualMachine->identifierTable[id]]);
		case '!':return ftoa(virtualMachine->doubleTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]);

	}
}
double* executeInstruction_getDoubleArray(VirtualMachine* virtualMachine,char ic,int id,int* length)
{
	int n=virtualMachine->integerTable[virtualMachine->identifierTable[id]];
	double* doubleArray=newDouble(n);
	int i0=virtualMachine->identifierTable[id]+1,i;
	switch(ic)
	{
		case '!':for(i=0;i<n;i++)doubleArray[i]=virtualMachine->doubleTable[virtualMachine->integerTable[i0+i]];break;
		case ':':for(i=0;i<n;i++)doubleArray[i]=0.0+virtualMachine->integerTable[virtualMachine->integerTable[i0+i]];break;
		case '?':for(i=0;i<n;i++)doubleArray[i]=atof(virtualMachine->stringTable[virtualMachine->integerTable[i0+i]]);break;
	}
	*length=n;

	return doubleArray;
}
void executeInstruction_setInteger(VirtualMachine* virtualMachine,int id,int d)
{
	virtualMachine->integerTable[id]=d;
}
void executeInstruction_setDouble(VirtualMachine* virtualMachine,int id,double f)
{
	virtualMachine->doubleTable[id]=f;
}
void executeInstruction_setString(VirtualMachine* virtualMachine,int id,String s)
{
	virtualMachine->stringTable[id]=s;
}
void executeInstruction_setIntegerDoubleOrString(VirtualMachine* virtualMachine,char ic,int id,int d,double f,String s)
{
	switch(ic)
	{
		case '%': virtualMachine->identifierTable[id]=d;return;

		case '@': virtualMachine->integerTable[id]=d;return;
		case '&': virtualMachine->integerTable[virtualMachine->identifierTable[id]]=d;return;
		case ':': virtualMachine->integerTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]=d;return;

		case '#': virtualMachine->doubleTable[id]=f;return;
		case '^': virtualMachine->doubleTable[virtualMachine->identifierTable[id]]=f;return;
		case '!': virtualMachine->doubleTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]=f;return;

		case '$': virtualMachine->stringTable[id]=s;return;
		case '*': virtualMachine->stringTable[virtualMachine->identifierTable[id]]=s;return;
		case '?': virtualMachine->stringTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]=s;return;
	}
}
void (*executeInstruction[NUM])(VirtualMachine* virtualMachine,int* instruction);
void executeInstruction_NOP(VirtualMachine* virtualMachine,int* instruction)
{
}
void executeInstruction_EXIT(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->instructionPointer=END;
}
void executeInstruction_MOV(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '%':case '@':case '&':case ':':d=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);break;
		case '#':case '^':case '!':f=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);break;
		case '$':case '*':case '?':s=executeInstruction_getString(virtualMachine,instruction[3],instruction[4]);break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_NEG(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':d=-executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);break;
		case '#':case '^':case '!':f=-executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);break;
		case '$':case '*':case '?':s=reverse(executeInstruction_getString(virtualMachine,instruction[3],instruction[4]));break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ADD(VirtualMachine* virtualMachine,int* instruction)
{
	int i,sgn;
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':
		{
			d=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='+')d+=executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]);
				else d-=executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]);
			}
			break;
		}
		case '#':case '^':case '!':
		{
			f=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='+')f+=executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]);
				else f-=executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]);
			}
			break;
		}
		case '$':case '*':case '?':
		{
			s=executeInstruction_getString(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='+')s=add(s,executeInstruction_getString(virtualMachine,instruction[i+1],instruction[i+2]));
				else s=add(s,reverse(executeInstruction_getString(virtualMachine,instruction[i+1],instruction[i+2])));
			}
			break;
		}
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_MUL(VirtualMachine* virtualMachine,int* instruction)
{
	int i,sgn;
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':
		{
			d=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='*')d*=executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]);
				else if(sgn=='/')d/=executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]);
				else d%=executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]);
			}
			break;
		}
		case '#':case '^':case '!':
		{
			f=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='*')f*=executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]);
				else f/=executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]);
			}
			break;
		}
		case '$':case '*':case '?':
		{
			s=executeInstruction_getString(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='*')s=mul(s,executeInstruction_getString(virtualMachine,instruction[i+1],instruction[i+2]));
				else s=sub(s,reverse(executeInstruction_getString(virtualMachine,instruction[i+1],instruction[i+2])));
			}
			break;
		}
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_INC(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':d=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2])+1;break;
		case '#':case '^':case '!':f=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2])+1.0;break;
		case '$':case '*':case '?':s=inc(executeInstruction_getString(virtualMachine,instruction[1],instruction[2]));break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_DEC(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':d=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2])-1;break;
		case '#':case '^':case '!':f=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2])-1.0;break;
		case '$':case '*':case '?':s=dec(executeInstruction_getString(virtualMachine,instruction[1],instruction[2]));break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ACC(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	char sgn=instruction[3];
	switch(instruction[1])
	{
		case '@':case '&':case ':':
		{
			d=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]);
			if(sgn=='+')d+=executeInstruction_getInteger(virtualMachine,instruction[4],instruction[5]);
			else d-=executeInstruction_getInteger(virtualMachine,instruction[4],instruction[5]);break;
		}
		case '#':case '^':case '!':
		{
			f=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
			if(sgn=='+')f+=executeInstruction_getDouble(virtualMachine,instruction[4],instruction[5]);
			else f-=executeInstruction_getDouble(virtualMachine,instruction[4],instruction[5]);break;
		}
		case '$':case '*':case '?':
		{
			s=executeInstruction_getString(virtualMachine,instruction[1],instruction[2]);
			if(sgn=='+')s=add(s,executeInstruction_getString(virtualMachine,instruction[4],instruction[5]));
			else s=add(s,reverse(executeInstruction_getString(virtualMachine,instruction[4],instruction[5])));break;
		}
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_PRO(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	char sgn=instruction[3];
	switch(instruction[1])
	{
		case '@':case '&':case ':':
		{
			d=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]);
			if(sgn=='*')d*=executeInstruction_getInteger(virtualMachine,instruction[4],instruction[5]);
			else d/=executeInstruction_getInteger(virtualMachine,instruction[4],instruction[5]);break;
		}
		case '#':case '^':case '!':
		{
			f=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
			if(sgn=='*')f*=executeInstruction_getDouble(virtualMachine,instruction[4],instruction[5]);
			else f/=executeInstruction_getDouble(virtualMachine,instruction[4],instruction[5]);break;
		}
		case '$':case '*':case '?':
		{
			s=executeInstruction_getString(virtualMachine,instruction[1],instruction[2]);
			if(sgn=='*')s=mul(s,executeInstruction_getString(virtualMachine,instruction[4],instruction[5]));
			else s=sub(s,executeInstruction_getString(virtualMachine,instruction[4],instruction[5]));break;
		}
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ENQ(VirtualMachine* virtualMachine,int* instruction)
{
	switch(instruction[1])
	{
		case '@':case '&':case ':':enQueueInteger(virtualMachine->queue,executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]));break;
		case '#':case '^':case '!':enQueueDouble(virtualMachine->queue,executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]));break;
		case '$':case '*':case '?':enQueueString(virtualMachine->queue,executeInstruction_getString(virtualMachine,instruction[1],instruction[2]));break;
	}
	virtualMachine->instructionPointer++;
}
void executeInstruction_DEQ(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':d=deQueueInteger(virtualMachine->queue);break;
		case '#':case '^':case '!':f=deQueueDouble(virtualMachine->queue);break;
		case '$':case '*':case '?':s=deQueueString(virtualMachine->queue);break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_CALL(VirtualMachine* virtualMachine,int* instruction)
{
	push(virtualMachine->stack,virtualMachine->instructionPointer+1);
	virtualMachine->instructionPointer=instruction[1];
}
void executeInstruction_AND(VirtualMachine* virtualMachine,int* instruction)
{
	int sgn;
	double f=0;
	String s=newChar(0);
	int i,d=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
	for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
	{
		if(sgn=='+')d+=executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]);
		else d*=executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]);
		if(sgn=='+'&&d!=0){d=1;break;}
		if(sgn=='*'&&d==0){d=0;break;}
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_NOT(VirtualMachine* virtualMachine,int* instruction)
{
	double f=0;
	String s=newChar(0);
	int d=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4])==0?1:0;
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_EQ(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[3])
	{
		case '@':case '&':case ':':if(executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4])==executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '#':case '^':case '!':if(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4])==executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '$':case '*':case '?':if(equals(executeInstruction_getString(virtualMachine,instruction[3],instruction[4]),executeInstruction_getString(virtualMachine,instruction[5],instruction[6])))d=1;break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_NE(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[3])
	{
		case '@':case '&':case ':':if(executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4])!=executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '#':case '^':case '!':if(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4])!=executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '$':case '*':case '?':if(!equals(executeInstruction_getString(virtualMachine,instruction[3],instruction[4]),executeInstruction_getString(virtualMachine,instruction[5],instruction[6])))d=1;break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_LE(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[3])
	{
		case '@':case '&':case ':':if(executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4])<=executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '#':case '^':case '!':if(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4])<=executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '$':case '*':case '?':if(length(executeInstruction_getString(virtualMachine,instruction[3],instruction[4]))<=length(executeInstruction_getString(virtualMachine,instruction[5],instruction[6])))d=1;break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_LT(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[3])
	{
		case '@':case '&':case ':':if(executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4])<executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '#':case '^':case '!':if(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4])<executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '$':case '*':case '?':if(length(executeInstruction_getString(virtualMachine,instruction[3],instruction[4]))<length(executeInstruction_getString(virtualMachine,instruction[5],instruction[6])))d=1;break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_GE(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[3])
	{
		case '@':case '&':case ':':if(executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4])>=executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '#':case '^':case '!':if(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4])>=executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '$':case '*':case '?':if(length(executeInstruction_getString(virtualMachine,instruction[3],instruction[4]))>=length(executeInstruction_getString(virtualMachine,instruction[5],instruction[6])))d=1;break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_GT(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[3])
	{
		case '@':case '&':case ':':if(executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4])>executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '#':case '^':case '!':if(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4])>executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]))d=1;break;
		case '$':case '*':case '?':if(length(executeInstruction_getString(virtualMachine,instruction[3],instruction[4]))>length(executeInstruction_getString(virtualMachine,instruction[5],instruction[6])))d=1;break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_JZ(VirtualMachine* virtualMachine,int* instruction)
{
	int d=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->instructionPointer=d==0?instruction[3]:virtualMachine->instructionPointer+1;
}
void executeInstruction_JNZ(VirtualMachine* virtualMachine,int* instruction)
{
	int d=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->instructionPointer=d!=0?instruction[3]:virtualMachine->instructionPointer+1;
}
void executeInstruction_JMP(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->instructionPointer=instruction[1];
}
void executeInstruction_RET(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->instructionPointer=pop(virtualMachine->stack);
}
void executeInstruction_CMOV(VirtualMachine* virtualMachine,int* instruction)
{
	int c=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':
		{
			if(c==0)d=executeInstruction_getInteger(virtualMachine,instruction[7],instruction[8]);
			else d=executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]);break;
		}
		case '#':case '^':case '!':
		{
			if(c==0)f=executeInstruction_getDouble(virtualMachine,instruction[7],instruction[8]);
			else f=executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]);break;
		}
		case '$':case '*':case '?':
		{
			if(c==0)s=executeInstruction_getString(virtualMachine,instruction[7],instruction[8]);
			else s=executeInstruction_getString(virtualMachine,instruction[5],instruction[6]);break;
		}
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SWITCH(VirtualMachine* virtualMachine,int* instruction)
{
	int instructionPointer=virtualMachine->instructionPointer;
	switch(instruction[1])
	{
		case '@':case '&':case ':':
		{
			int d=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]),i=3;
			while(1)
			{
				if(instruction[i]==END-1){instructionPointer=instruction[i+1];break;}
				if(instruction[i]==END-2){instructionPointer=instruction[i+1];break;}
				virtualMachine->instructionPointer=instruction[i];
				while(virtualMachine->instructionPointer<instruction[i+1])
				{
					int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
					executeInstruction[instruction1[0]](virtualMachine,instruction1);
				}
				if(d==executeInstruction_getInteger(virtualMachine,instruction[i+2],instruction[i+3])){instructionPointer=instruction[i+1];break;}
				i+=4;
			}
			break;
		}
		case '#':case '^':case '!':
		{
			int i=3;
			double f=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
			while(1)
			{
				if(instruction[i]==END-1){instructionPointer=instruction[i+1];break;}
				if(instruction[i]==END-2){instructionPointer=instruction[i+1];break;}
				virtualMachine->instructionPointer=instruction[i];
				while(virtualMachine->instructionPointer<instruction[i+1])
				{
					int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
					executeInstruction[instruction1[0]](virtualMachine,instruction1);
				}
				if(f==executeInstruction_getDouble(virtualMachine,instruction[i+2],instruction[i+3])){instructionPointer=instruction[i+1];break;}
				i+=4;
			}
			break;
		}
		case '$':case '*':case '?':
		{
			int i=3;
			String s=executeInstruction_getString(virtualMachine,instruction[1],instruction[2]);
			while(1)
			{
				if(instruction[i]==END-1){instructionPointer=instruction[i+1];break;}
				if(instruction[i]==END-2){instructionPointer=instruction[i+1];break;}
				virtualMachine->instructionPointer=instruction[i];
				while(virtualMachine->instructionPointer<instruction[i+1])
				{
					int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
					executeInstruction[instruction1[0]](virtualMachine,instruction1);
				}
				if(equals(s,executeInstruction_getString(virtualMachine,instruction[i+2],instruction[i+3]))){instructionPointer=instruction[i+1];break;}
				i+=4;
			}
			break;
		}
	}
	virtualMachine->instructionPointer=instructionPointer;
}
void executeInstruction_PUSH(VirtualMachine* virtualMachine,int* instruction)
{
	switch(instruction[1])
	{
		case '@':case '&':case ':':pushInteger(virtualMachine->stack,executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]));break;
		case '#':case '^':case '!':pushDouble(virtualMachine->stack,executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]));break;
		case '$':case '*':case '?':pushString(virtualMachine->stack,executeInstruction_getString(virtualMachine,instruction[1],instruction[2]));break;
	}
	virtualMachine->instructionPointer++;
}
void executeInstruction_POP(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0,i;
	float f=0.0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':d=popInteger(virtualMachine->stack);break;
		case '#':case '^':case '!':f=popDouble(virtualMachine->stack);break;
		case '$':case '*':case '?':s=popString(virtualMachine->stack);break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_IN(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	float f=0.0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':scanf("%d",&d);break;
		case '#':case '^':case '!':scanf("%f",&f);break;
		case '$':case '*':case '?':scanf("%s",s);break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_OUT(VirtualMachine* virtualMachine,int* instruction)
{
	switch(instruction[1])
	{
		case '@':case '&':case ':':printf("%d\n",executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]));break;
		case '#':case '^':case '!':printf("%f\n",executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]));break;
		case '$':case '*':case '?':printf("%s\n",executeInstruction_getString(virtualMachine,instruction[1],instruction[2]));break;
	}
	virtualMachine->instructionPointer++;
}
void executeInstruction_OPEN(VirtualMachine* virtualMachine,int* instruction)
{
	String s=executeInstruction_getString(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->file=fopen(s,"r+");
	if(virtualMachine->file==NULL)virtualMachine->file=fopen(s,"w+");
	virtualMachine->instructionPointer++;
}
void executeInstruction_READ(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	float f=0.0;
	String s=newChar(0);
	FILE* file=virtualMachine->file;
	switch(instruction[1])
	{
		case '@':case '&':case ':':fscanf(file,"%d\n",&d);break;
		case '#':case '^':case '!':fscanf(file,"%f\n",&f);break;
		case '$':case '*':case '?':fscanf(file,"%s\n",s);break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_WRITE(VirtualMachine* virtualMachine,int* instruction)
{
	FILE* file=virtualMachine->file;
	switch(instruction[1])
	{
		case '@':case '&':case ':':fprintf(file,"%d\n",executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]));break;
		case '#':case '^':case '!':fprintf(file,"%f\n",executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]));break;
		case '$':case '*':case '?':fprintf(file,"%s\n",executeInstruction_getString(virtualMachine,instruction[1],instruction[2]));break;
	}
	virtualMachine->instructionPointer++;
}
void executeInstruction_CLOSE(VirtualMachine* virtualMachine,int* instruction)
{
	fclose(virtualMachine->file);
	virtualMachine->instructionPointer++;
}
void executeInstruction_RUN(VirtualMachine* virtualMachine,int* instruction)
{
	run(virtualMachine->virtualMachineTable[instruction[1]]);
	virtualMachine->instructionPointer++;
}
void executeInstruction_NEW(VirtualMachine* virtualMachine,int* instruction)
{
	VirtualMachine* virtualMachine_Class=virtualMachine->virtualMachineTable[instruction[2]];
	virtualMachine->virtualMachineTable[instruction[1]]=newVirtualMachine_Object(virtualMachine_Class);
	virtualMachine->instructionPointer++;
}
void executeInstruction_INVOKE(VirtualMachine* virtualMachine,int* instruction)
{
	VirtualMachine* virtualMachine1=virtualMachine->virtualMachineTable[instruction[1]];
	virtualMachine1->instructionPointer=instruction[2];
	push(virtualMachine1->stack,END);
	while(virtualMachine1->instructionPointer!=END)
	{
		int* instruction1=virtualMachine1->instructions[virtualMachine1->instructionPointer];
		executeInstruction[instruction1[0]](virtualMachine1,instruction1);
	}
	virtualMachine->instructionPointer++;
}
void executeInstruction_PI(VirtualMachine* virtualMachine,int* instruction)
{
	double f=Math_PI;
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SIN(VirtualMachine* virtualMachine,int* instruction)
{
	double f=sin(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_COS(VirtualMachine* virtualMachine,int* instruction)
{
	double f=cos(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_TAN(VirtualMachine* virtualMachine,int* instruction)
{
	double f=tan(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_COT(VirtualMachine* virtualMachine,int* instruction)
{
	double f=cot(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SEC(VirtualMachine* virtualMachine,int* instruction)
{
	double f=sec(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_CSC(VirtualMachine* virtualMachine,int* instruction)
{
	double f=csc(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ASIN(VirtualMachine* virtualMachine,int* instruction)
{
	double f=asin(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ACOS(VirtualMachine* virtualMachine,int* instruction)
{
	double f=acos(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ATAN(VirtualMachine* virtualMachine,int* instruction)
{
	double f=atan(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ACOT(VirtualMachine* virtualMachine,int* instruction)
{
	double f=acot(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ASEC(VirtualMachine* virtualMachine,int* instruction)
{
	double f=asec(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ACSC(VirtualMachine* virtualMachine,int* instruction)
{
	double f=acsc(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ABS(VirtualMachine* virtualMachine,int* instruction)
{
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':
		{
			switch(instruction[3])
			{
				case '$':case '*':case '?':d=length(executeInstruction_getString(virtualMachine,instruction[3],instruction[4]));break;
				default:d=(int)abs(executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]));break;
			}
			break;
		}
		case '#':case '^':case '!':f=abs(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));break;
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_POW(VirtualMachine* virtualMachine,int* instruction)
{
	int i,sgn;
	int d=0;
	double f=0;
	String s=newChar(0);
	switch(instruction[1])
	{
		case '@':case '&':case ':':
		{
			d=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='^')d=pow(d,executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]));
				else d=(int)(pow(d,1.0/executeInstruction_getInteger(virtualMachine,instruction[i+1],instruction[i+2]))+0.5);
			}
			break;
		}
		case '#':case '^':case '!':
		{
			f=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='^')f=pow(f,executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]));
				else f=pow(f,1.0/executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]));
			}
			break;
		}
		case '$':case '*':case '?':
		{
			s=executeInstruction_getString(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='^')s=add(s,executeInstruction_getString(virtualMachine,instruction[i+1],instruction[i+2]));
				else s=add(s,reverse(executeInstruction_getString(virtualMachine,instruction[i+1],instruction[i+2])));
			}
			break;
		}
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_EXP(VirtualMachine* virtualMachine,int* instruction)
{
	double f=exp(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_LOG(VirtualMachine* virtualMachine,int* instruction)
{
	double f=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
	f=Log(f,executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_LG(VirtualMachine* virtualMachine,int* instruction)
{
	double f=log10(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_LN(VirtualMachine* virtualMachine,int* instruction)
{
	double f=log(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SQRT(VirtualMachine* virtualMachine,int* instruction)
{
	double f=sqrt(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_RAND(VirtualMachine* virtualMachine,int* instruction)
{
	double f=random();
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_FACT(VirtualMachine* virtualMachine,int* instruction)
{
	int d=factorial(executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setInteger(virtualMachine,instruction[2],d);
	virtualMachine->instructionPointer++;
}
void executeInstruction_PERM(VirtualMachine* virtualMachine,int* instruction)
{
	int d=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
	d=permutation(d,executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]));
	executeInstruction_setInteger(virtualMachine,instruction[2],d);
	virtualMachine->instructionPointer++;
}
void executeInstruction_COMB(VirtualMachine* virtualMachine,int* instruction)
{
	int d=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
	d=combination(d,executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]));
	executeInstruction_setInteger(virtualMachine,instruction[2],d);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SQU(VirtualMachine* virtualMachine,int* instruction)
{
	double f=square(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SAW(VirtualMachine* virtualMachine,int* instruction)
{
	double f=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
	f=sawtooth(f,executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SH(VirtualMachine* virtualMachine,int* instruction)
{
	double f=sh(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_CH(VirtualMachine* virtualMachine,int* instruction)
{
	double f=ch(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_TH(VirtualMachine* virtualMachine,int* instruction)
{
	double f=th(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ARSH(VirtualMachine* virtualMachine,int* instruction)
{
	double f=arsh(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ARCH(VirtualMachine* virtualMachine,int* instruction)
{
	double f=arch(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ARTH(VirtualMachine* virtualMachine,int* instruction)
{
	double f=arth(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_GAU(VirtualMachine* virtualMachine,int* instruction)
{
	double x=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
	double x0=executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]);
	double f=gauss(x,x0,executeInstruction_getDouble(virtualMachine,instruction[7],instruction[8]));
	executeInstruction_setDouble(virtualMachine,instruction[2],f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_TAY(VirtualMachine* virtualMachine,int* instruction)
{
	double x=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
	double x0=executeInstruction_getDouble(virtualMachine,instruction[5],instruction[6]);
	int n;double* df=executeInstruction_getDoubleArray(virtualMachine,instruction[7],instruction[8],&n);
	executeInstruction_setDouble(virtualMachine,instruction[2],taylor(x,x0,df,n));
	virtualMachine->instructionPointer++;
}
void initExecuteInstructionTable()
{
	executeInstruction[NOP]=executeInstruction_NOP;
	executeInstruction[EXIT]=executeInstruction_EXIT;
	executeInstruction[MOV]=executeInstruction_MOV;
	executeInstruction[ADD]=executeInstruction_ADD;
	executeInstruction[MUL]=executeInstruction_MUL;
	executeInstruction[NEG]=executeInstruction_NEG;
	executeInstruction[INC]=executeInstruction_INC;
	executeInstruction[DEC]=executeInstruction_DEC;
	executeInstruction[ACC]=executeInstruction_ACC;
	executeInstruction[PRO]=executeInstruction_PRO;
	executeInstruction[ENQ]=executeInstruction_ENQ;
	executeInstruction[DEQ]=executeInstruction_DEQ;
	executeInstruction[CALL]=executeInstruction_CALL;
	executeInstruction[AND]=executeInstruction_AND;
	executeInstruction[NOT]=executeInstruction_NOT;
	executeInstruction[EQ]=executeInstruction_EQ;
	executeInstruction[NE]=executeInstruction_NE;
	executeInstruction[LE]=executeInstruction_LE;
	executeInstruction[LT]=executeInstruction_LT;
	executeInstruction[GE]=executeInstruction_GE;
	executeInstruction[GT]=executeInstruction_GT;
	executeInstruction[JZ]=executeInstruction_JZ;
	executeInstruction[JNZ]=executeInstruction_JNZ;
	executeInstruction[JMP]=executeInstruction_JMP;
	executeInstruction[RET]=executeInstruction_RET;
	executeInstruction[CMOV]=executeInstruction_CMOV;
	executeInstruction[SWITCH]=executeInstruction_SWITCH;
	executeInstruction[PUSH]=executeInstruction_PUSH;
	executeInstruction[POP]=executeInstruction_POP;
	executeInstruction[IN]=executeInstruction_IN;
	executeInstruction[OUT]=executeInstruction_OUT;
	executeInstruction[OPEN]=executeInstruction_OPEN;
	executeInstruction[READ]=executeInstruction_READ;
	executeInstruction[WRITE]=executeInstruction_WRITE;
	executeInstruction[CLOSE]=executeInstruction_CLOSE;
	executeInstruction[RUN]=executeInstruction_RUN;
	executeInstruction[NEW]=executeInstruction_NEW;
	executeInstruction[INVOKE]=executeInstruction_INVOKE;
	executeInstruction[PI]=executeInstruction_PI;
	executeInstruction[SIN]=executeInstruction_SIN;
	executeInstruction[COS]=executeInstruction_COS;
	executeInstruction[TAN]=executeInstruction_TAN;
	executeInstruction[COT]=executeInstruction_COT;
	executeInstruction[SEC]=executeInstruction_SEC;
	executeInstruction[CSC]=executeInstruction_CSC;
	executeInstruction[ASIN]=executeInstruction_ASIN;
	executeInstruction[ACOS]=executeInstruction_ACOS;
	executeInstruction[ATAN]=executeInstruction_ATAN;
	executeInstruction[ACOT]=executeInstruction_ACOT;
	executeInstruction[ASEC]=executeInstruction_ASEC;
	executeInstruction[ACSC]=executeInstruction_ACSC;
	executeInstruction[ABS]=executeInstruction_ABS;
	executeInstruction[POW]=executeInstruction_POW;
	executeInstruction[EXP]=executeInstruction_EXP;
	executeInstruction[LOG]=executeInstruction_LOG;
	executeInstruction[LG]=executeInstruction_LG;
	executeInstruction[LN]=executeInstruction_LN;
	executeInstruction[SQRT]=executeInstruction_SQRT;
	executeInstruction[RAND]=executeInstruction_RAND;
	executeInstruction[FACT]=executeInstruction_FACT;
	executeInstruction[PERM]=executeInstruction_PERM;
	executeInstruction[COMB]=executeInstruction_COMB;
	executeInstruction[SQU]=executeInstruction_SQU;
	executeInstruction[SAW]=executeInstruction_SAW;
	executeInstruction[SH]=executeInstruction_SH;
	executeInstruction[CH]=executeInstruction_CH;
	executeInstruction[TH]=executeInstruction_TH;
	executeInstruction[ARSH]=executeInstruction_ARSH;
	executeInstruction[ARCH]=executeInstruction_ARCH;
	executeInstruction[ARTH]=executeInstruction_ARTH;
	executeInstruction[GAU]=executeInstruction_GAU;
	executeInstruction[TAY]=executeInstruction_TAY;
}
void executeDeclarations(VirtualMachine* virtualMachine)
{
	int i,l=virtualMachine->declarationsLength/2;
	for(i=0;i<l;i++)
	{
		int ip0=virtualMachine->declarations[i*2+0];
		int ip1=virtualMachine->declarations[i*2+1];
		virtualMachine->instructionPointer=ip0;
		while(virtualMachine->instructionPointer!=ip1)
		{
			int* instruction=virtualMachine->instructions[virtualMachine->instructionPointer];
			executeInstruction[instruction[0]](virtualMachine,instruction);
		}
	}
}
VirtualMachine* newVirtualMachine_Class(String className,List* queue,List* stack)
{
	VirtualMachine* virtualMachine=newVirtualMachine(className);
	virtualMachine->queue=queue;
	virtualMachine->stack=stack;
	return virtualMachine;
}
VirtualMachine* newVirtualMachine_Object(VirtualMachine* virtualMachine_Class)
{
	VirtualMachine* virtualMachine=(VirtualMachine*)malloc(sizeof(VirtualMachine));
	virtualMachine->methodNumber=virtualMachine_Class->methodNumber;
	virtualMachine->methodTable=virtualMachine_Class->methodTable;
	virtualMachine->methodPointerTable=virtualMachine_Class->methodPointerTable;
	virtualMachine->methodPointerTable=virtualMachine_Class->methodReturnTypeTable;
	virtualMachine->objectNumber=virtualMachine_Class->objectNumber;
	virtualMachine->classNumber=virtualMachine_Class->classNumber;
	virtualMachine->virtualMachineTable=virtualMachine_Class->virtualMachineTable;
	virtualMachine->identifierNumber=virtualMachine_Class->identifierNumber;
	virtualMachine->identifierTable=newInt(virtualMachine->identifierNumber);
	memset(virtualMachine->identifierTable,0,virtualMachine->identifierNumber);int i;
	for(i=0;i<virtualMachine->identifierNumber;i++)virtualMachine->identifierTable[i]=0;
	virtualMachine->integerNumber=virtualMachine_Class->integerNumber;
	virtualMachine->integerTableLength=virtualMachine_Class->integerTableLength;
	virtualMachine->integerTable=newInt(virtualMachine->integerNumber);
	for(i=0;i<virtualMachine->integerTableLength;i++)virtualMachine->integerTable[i]=virtualMachine_Class->integerTable[i];
	virtualMachine->doubleNumber=virtualMachine_Class->doubleNumber;
	virtualMachine->doubleTableLength=virtualMachine_Class->doubleTableLength;
	virtualMachine->doubleTable=newDouble(virtualMachine->doubleNumber);
	for(i=0;i<virtualMachine->doubleTableLength;i++)virtualMachine->doubleTable[i]=virtualMachine_Class->doubleTable[i];
	virtualMachine->stringNumber=virtualMachine_Class->stringNumber;
	virtualMachine->stringTableLength=virtualMachine_Class->stringTableLength;
	virtualMachine->stringTable=newString(virtualMachine->stringNumber);
	for(i=0;i<virtualMachine->stringTableLength;i++)virtualMachine->stringTable[i]=virtualMachine_Class->stringTable[i];
	virtualMachine->beginInstructionPointer=virtualMachine_Class->beginInstructionPointer;
	virtualMachine->declarations=virtualMachine_Class->declarations;
	virtualMachine->declarationsLength=virtualMachine_Class->declarationsLength;
	virtualMachine->queue=virtualMachine_Class->queue;
	virtualMachine->stack=virtualMachine_Class->stack;
	virtualMachine->instructions=virtualMachine_Class->instructions;
	return virtualMachine;
}
void run(VirtualMachine* virtualMachine)
{
	initExecuteInstructionTable();
	executeDeclarations(virtualMachine);
	virtualMachine->instructionPointer=virtualMachine->beginInstructionPointer;
	while(virtualMachine->instructionPointer!=END)
	{
//printf("\nExecute: %d\t ",virtualMachine->instructionPointer);
		int* instruction=virtualMachine->instructions[virtualMachine->instructionPointer];
		executeInstruction[instruction[0]](virtualMachine,instruction);
	}
//this.showIdentifierTable();
//this.showDoubleTable();
}
