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
	int vectorNumber;
	Vector** vectorTable;
	int declarationsLength;
	int* declarations;
	int beginInstructionPointer;
	int instructionsLength;
	int** instructions;
	int instructionPointer;
	List* queue;
	List* stack;
	FILE* file;
	int uStep,vStep,wStep;
	SimpleUniverse* simpleUniverse;
	BranchGroup* branchGroup;
	SharedGroup* currentSharedGroup;
	SharedGroupList* sharedGroupList;
	TransformGroupNode* topTransformGroupNode;
	Appearance* appearance;
	Light* light;
	Material* material;
	boolean materialIsChanged;
	Color3f* diffuseColor;
	Color3f* emissiveColor;
	Color3f* ambientColor;
	Color3f* specularColor;
	Vector3d* translation;
	Vector3d* scale;
	AxisAngle4d* rotation;
	float shininess;
	Point2f** textureCoordinates;
	Color3f** colors;
	Texture2D* texture;
	TransparencyAttributes* transparencyAttributes;
	int uOrder,vOrder,wOrder;
	double *uKnots,*vKnots,*wKnots;
	int uCtrlCount,vCtrlCount,wCtrlCount;
	double*** weights;
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
	fscanf(file,"%d,",&virtualMachine->vectorNumber);
	virtualMachine->vectorTable=newVectors(virtualMachine->vectorNumber);
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
	virtualMachine->uStep=DEFAULT_FUNC_STEP;
	virtualMachine->vStep=DEFAULT_FUNC_STEP;
	virtualMachine->wStep=DEFAULT_FUNC_STEP;
	virtualMachine->simpleUniverse=null;
	virtualMachine->branchGroup=newBranchGroup();
	virtualMachine->currentSharedGroup=null;
	virtualMachine->sharedGroupList=newSharedGroupList();
	virtualMachine->topTransformGroupNode=null;
	virtualMachine->light=null;
	virtualMachine->diffuseColor=newColor3f(1,1,1);
	virtualMachine->emissiveColor=newColor3f(0,0,0);
	virtualMachine->ambientColor=newColor3f(0,0,0);
	virtualMachine->specularColor=newColor3f(0,0,0);
	virtualMachine->shininess=0.5f;
	virtualMachine->material=newMaterial();
	virtualMachine->materialIsChanged=false;
	virtualMachine->translation=null;
	virtualMachine->scale=null;
	virtualMachine->rotation=null;
	virtualMachine->textureCoordinates=null;
	virtualMachine->colors=null;
	virtualMachine->texture=null;
	virtualMachine->transparencyAttributes=null;
	virtualMachine->uOrder=2;
	virtualMachine->vOrder=2;
	virtualMachine->wOrder=2;
	virtualMachine->uKnots=null;
	virtualMachine->vKnots=null;
	virtualMachine->wKnots=null;
	virtualMachine->uCtrlCount=0;
	virtualMachine->vCtrlCount=0;
	virtualMachine->wCtrlCount=0;
	virtualMachine->weights=new__Double(1,0,0);
	virtualMachine->weights[0]=null;
	return virtualMachine;
}
void setViewingPlatform(VirtualMachine* virtualMachine,SimpleUniverse* simpleUniverse)
{
	addBranchGraph(simpleUniverse,virtualMachine->branchGroup);int i;
	for(i=0;i<virtualMachine->classNumber;i++)
	{
		addBranchGraph(simpleUniverse,virtualMachine->virtualMachineTable[i]->branchGroup);
	}
	virtualMachine->simpleUniverse=simpleUniverse;
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
Vector* executeInstruction_newVector(VirtualMachine* virtualMachine,int id)
{
	double x=virtualMachine->doubleTable[id+0];
	double y=virtualMachine->doubleTable[id+1];
	double z=virtualMachine->doubleTable[id+2];
	return newVector(x,y,z);
}
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

		case '~':return (int)executeInstruction_newVector(virtualMachine,id)->x;
		case '\\':return (int)virtualMachine->vectorTable[id]->x;
		case '<':return (int)virtualMachine->vectorTable[virtualMachine->identifierTable[id]]->x;
		case '>':return (int)virtualMachine->vectorTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]->x;
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

		case '~':return executeInstruction_newVector(virtualMachine,id)->x;
		case '\\':return virtualMachine->vectorTable[id]->x;
		case '<':return virtualMachine->vectorTable[virtualMachine->identifierTable[id]]->x;
		case '>':return virtualMachine->vectorTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]->x;
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

		case '~':return vtoa(executeInstruction_newVector(virtualMachine,id));
		case '\\':return vtoa(virtualMachine->vectorTable[id]);
		case '<':return vtoa(virtualMachine->vectorTable[virtualMachine->identifierTable[id]]);
		case '>':return vtoa(virtualMachine->vectorTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]);

	}
}
Vector* executeInstruction_getVector(VirtualMachine* virtualMachine,char ic,int id)
{
	switch(ic)
	{
		case '~':return executeInstruction_newVector(virtualMachine,id);
		case '\\':return virtualMachine->vectorTable[id];
		case '<':return virtualMachine->vectorTable[virtualMachine->identifierTable[id]];
		case '>':return virtualMachine->vectorTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]];
		default:
		{
			double d=executeInstruction_getDouble(virtualMachine,ic,id);
			return newVector(d,d,d);
		}
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
void executeInstruction_setVector(VirtualMachine* virtualMachine,char ic,int id,Vector* v)
{
	switch(ic)
	{
		case '\\':virtualMachine->vectorTable[id]=v;break;
		case '<':virtualMachine->vectorTable[virtualMachine->identifierTable[id]]=v;break;
		case '>':virtualMachine->vectorTable[virtualMachine->integerTable[virtualMachine->integerTable[id]]]=v;break;
	}
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
	virtualMachine->instructionPointer++;
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
		case '\\':case '<':case '>':
		{
			Vector* v=executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]);
			executeInstruction_setVector(virtualMachine,instruction[1],instruction[2],v);
			virtualMachine->instructionPointer++;
			return;
		}
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
		case '\\':case '<':case '>':
		{
			Vector* v=executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]);
			mulDouble(v,-1);
			executeInstruction_setVector(virtualMachine,instruction[1],instruction[2],v);
			virtualMachine->instructionPointer++;
			return;
		}
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
		case '\\':case '<':case '>':
		{
			Vector* v=executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='+')addVector(v,executeInstruction_getVector(virtualMachine,instruction[i+1],instruction[i+2]));
				else subVector(v,executeInstruction_getVector(virtualMachine,instruction[i+1],instruction[i+2]));
			}
			executeInstruction_setVector(virtualMachine,instruction[1],instruction[2],v);
			virtualMachine->instructionPointer++;
			return;
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
	Vector* v=newVector(0,0,0);
	boolean isVector=false;
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
boolean IsVector(char ic)
{
	return ic=='~'||ic=='\\'||ic=='<'||ic=='>';
}
void executeInstruction_VMUL(VirtualMachine* virtualMachine,int* instruction)
{
	int i,sgn;
	int d=0;
	double f=0;
	String s=newChar(0);
	Vector* v=newVector(0,0,0);
	boolean isVector=true;
	if(IsVector(instruction[3]))v=executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]);
	else {f=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);isVector=false;}
	for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
	{
		if(IsVector(instruction[i+1]))
		{
			if(isVector)
			{
				if(sgn=='*')f=mulVector(v,executeInstruction_getVector(virtualMachine,instruction[i+1],instruction[i+2]));
				else f=angleToVector(v,executeInstruction_getVector(virtualMachine,instruction[i+1],instruction[i+2]));
				isVector=false;
			}
			else
			{
				v=executeInstruction_getVector(virtualMachine,instruction[i+1],instruction[i+2]);
				if(sgn=='*')mulDouble(v,f);
				else mulDouble(v,1.0/f);
				isVector=true;
			}
		}
		else 
		{
			if(isVector)
			{
				if(sgn=='*')mulDouble(v,executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]));
				else mulDouble(v,1.0/executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]));
			}
			else
			{
				if(sgn=='*')f*=executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]);
				else f/=executeInstruction_getDouble(virtualMachine,instruction[i+1],instruction[i+2]);
			}
		}
	}
	if(!isVector)v=newVector(f,f,f);
	executeInstruction_setVector(virtualMachine,instruction[1],instruction[2],v);
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
		case '~':case '\\':case '<':case '>':enQueueVector(virtualMachine->queue,executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]));break;
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
		case '\\':case '<':case '>':
		{
			Vector* v=deQueueVector(virtualMachine->queue);
			executeInstruction_setVector(virtualMachine,instruction[1],instruction[2],v);
			virtualMachine->instructionPointer++;return;
		}
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
		case '\\':case '<':case '>':if(equalVectors(executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]),executeInstruction_getVector(virtualMachine,instruction[5],instruction[6])))d=1;break;
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
		case '\\':case '<':case '>':if(!equalVectors(executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]),executeInstruction_getVector(virtualMachine,instruction[5],instruction[6])))d=1;break;
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
		case '\\':case '<':case '>':
		{
			Vector* v;
			if(c==0)v=executeInstruction_getVector(virtualMachine,instruction[7],instruction[8]);
			else v=executeInstruction_getVector(virtualMachine,instruction[5],instruction[6]);
			executeInstruction_setVector(virtualMachine,instruction[1],instruction[2],v);
			virtualMachine->instructionPointer++;return;
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
		case '\\':case '<':case '>':
		{
			int i=3;
			Vector* v=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
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
				if(equalVectors(v,executeInstruction_getVector(virtualMachine,instruction[i+2],instruction[i+3]))){instructionPointer=instruction[i+1];break;}
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
		case '\\':case '<':case '>':pushVector(virtualMachine->stack,executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]));break;
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
		case '\\':case '<':case '>':
		{
			Vector* v=popVector(virtualMachine->stack);
			executeInstruction_setVector(virtualMachine,instruction[1],instruction[2],v);
			virtualMachine->instructionPointer++;return;
		}
	}
	executeInstruction_setIntegerDoubleOrString(virtualMachine,instruction[1],instruction[2],d,f,s);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SCAN(VirtualMachine* virtualMachine,int* instruction)
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
void executeInstruction_PRINT(VirtualMachine* virtualMachine,int* instruction)
{
	switch(instruction[1])
	{
		case '@':case '&':case ':':printf("%d\n",executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]));break;
		case '#':case '^':case '!':printf("%f\n",executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]));break;
		case '$':case '*':case '?':printf("%s\n",executeInstruction_getString(virtualMachine,instruction[1],instruction[2]));break;
		case '\\':case '<':case '>':printVector(executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]));break;
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
	String s=newChar(100);
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
void executeInstruction_MOVPI(VirtualMachine* virtualMachine,int* instruction)
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
		case '#':case '^':case '!':
		{
			switch(instruction[3])
			{
				case '~':case '\\':case '<':case '>':f=lengthOfVector(executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]));break;
				default:f=abs(executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]));break;
			}
			break;
		}
		
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
		case '\\':case '<':case '>':
		{
			Vector* v=executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]);
			for(i=5,sgn=instruction[i];sgn!=END;i+=3,sgn=instruction[i])
			{
				if(sgn=='^')crossVector(v,executeInstruction_getVector(virtualMachine,instruction[i+1],instruction[i+2]));
				else projectVector(v,executeInstruction_getVector(virtualMachine,instruction[i+1],instruction[i+2]));
			}
			executeInstruction_setVector(virtualMachine,instruction[1],instruction[2],v);
			virtualMachine->instructionPointer++;
			return;
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
void executeInstruction_FUNC(VirtualMachine* virtualMachine,int* instruction)
{
	int paramNumber=instruction[1],i,p0=2,p1=p0+paramNumber;
	for(i=0;i<paramNumber;i++)
	{
		double f=executeInstruction_getDouble(virtualMachine,instruction[p1+i*2+0],instruction[p1+i*2+1]);
		virtualMachine->doubleTable[instruction[p0+i]]=f;
	}
	p1+=paramNumber*2;
	int ip0=instruction[p1+0],ip1=instruction[p1+1],ip=virtualMachine->instructionPointer;
	virtualMachine->instructionPointer=ip0;
	while(virtualMachine->instructionPointer<ip1)
	{
		int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
		executeInstruction[instruction1[0]](virtualMachine,instruction1);
	}
	virtualMachine->instructionPointer=ip+1;
}
void executeInstruction_PRINTF(VirtualMachine* virtualMachine,int* instruction)
{
	String funcName=executeInstruction_getString(virtualMachine,instruction[1],instruction[2]);
	int paramNumber=instruction[5],i,j,c,p=6,step=virtualMachine->uStep;
	int* counter=newInt(paramNumber),l=(int)pow(step,paramNumber);
	int* paramPointer=newInt(paramNumber);
	double *param0=newDouble(paramNumber),*dParam=newDouble(paramNumber);
	for(i=0;i<paramNumber;i++)
	{
		counter[i]=0;
		paramPointer[i]=instruction[p+i];
	}
	p+=paramNumber;
	for(i=0;i<paramNumber;i++)
	{
		param0[i]=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
		dParam[i]=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-param0[i])/(step-1);p+=2;
	}
	int ip0=instruction[p+0],ip1=instruction[p+1],ip=virtualMachine->instructionPointer;
	for(c=0;c<l;c++)
	{
		printf("%s(",funcName);
		for(i=0;i<paramNumber;i++)
		{
			double f=param0[i]+counter[i]*dParam[i];
			printf("%f",f);
			virtualMachine->doubleTable[paramPointer[i]]=f;
			if(i<paramNumber-1)printf(",");
		}
		printf(")=");
		virtualMachine->instructionPointer=ip0;
		while(virtualMachine->instructionPointer<ip1)
		{
			int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
			executeInstruction[instruction1[0]](virtualMachine,instruction1);
		}
		if(instruction[3]=='#')printf("%f\n",virtualMachine->doubleTable[instruction[4]]);
		else printVector(virtualMachine->vectorTable[instruction[4]]);
		if(++counter[0]>=step)
		{
			for(j=0;j<paramNumber-1&&counter[j]>=step;j++)
			{
				counter[j]=0;
				counter[j+1]++;
			}
		}
	}
	virtualMachine->instructionPointer=ip+1;
}
void executeInstruction_STEP(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->uStep=(int)v->x;
	virtualMachine->vStep=(int)v->y;
	virtualMachine->wStep=(int)v->z;
	virtualMachine->instructionPointer++;
}
void executeInstruction_LIGHT0(VirtualMachine* virtualMachine,int* instruction)
{
	Light* light=newDirectionalLight(newColor3f(1,1,1),newVector3f(-1,-1,-1));
	addChild_BL(virtualMachine->branchGroup,light);
	virtualMachine->instructionPointer++;
}
void executeInstruction_LIGHT(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v0=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	Vector* v1=executeInstruction_getVector(virtualMachine,instruction[3],instruction[4]);
	Light* light=newDirectionalLight(newColor3f(v0->x,v0->y,v0->z),newVector3f(v1->x,v1->y,v1->z));
	addChild_BL(virtualMachine->branchGroup,light);
	virtualMachine->instructionPointer++;
}
void executeInstruction_newMaterial(VirtualMachine* virtualMachine)
{
	if(virtualMachine->materialIsChanged)
	{
		Material* material=newMaterial();
		setDiffuseColor(material,virtualMachine->diffuseColor);
		setEmissiveColor(material,virtualMachine->emissiveColor);
		setAmbientColor(material,virtualMachine->ambientColor);
		setSpecularColor(material,virtualMachine->specularColor);
		setShininess(material,virtualMachine->shininess);
		virtualMachine->material=material;
		virtualMachine->materialIsChanged=false;
	}
}
Appearance* executeInstruction_getAppearance(VirtualMachine* virtualMachine)
{
	executeInstruction_newMaterial(virtualMachine);
	Appearance* Appearance1=newAppearance();
	setMaterial(Appearance1,virtualMachine->material);
	setTransparencyAttributes(Appearance1,virtualMachine->transparencyAttributes);
	return Appearance1;
}
void executeInstruction_COLOR(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v0=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->diffuseColor=newColor3f(v0->x,v0->y,v0->z);
	virtualMachine->materialIsChanged=true;
	virtualMachine->instructionPointer++;
}
void executeInstruction_COLOR0(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->diffuseColor=newColor3f(1,1,1);
	virtualMachine->emissiveColor=newColor3f(0,0,0);
	virtualMachine->ambientColor=newColor3f(0,0,0);
	virtualMachine->specularColor=newColor3f(0,0,0);
	virtualMachine->shininess=0.5f;
	virtualMachine->materialIsChanged=true;
	virtualMachine->instructionPointer++;
}
void executeInstruction_ACOLOR(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v0=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->ambientColor=newColor3f(v0->x,v0->y,v0->z);
	virtualMachine->materialIsChanged=true;
	virtualMachine->instructionPointer++;
}
void executeInstruction_ECOLOR(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v0=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->emissiveColor=newColor3f(v0->x,v0->y,v0->z);
	virtualMachine->materialIsChanged=true;
	virtualMachine->instructionPointer++;
}
void executeInstruction_SCOLOR(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v0=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->specularColor=newColor3f(v0->x,v0->y,v0->z);
	virtualMachine->materialIsChanged=true;
	virtualMachine->instructionPointer++;
}
void executeInstruction_SHINE(VirtualMachine* virtualMachine,int* instruction)
{
	double shininess=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->shininess=shininess;
	virtualMachine->materialIsChanged=true;
	virtualMachine->instructionPointer++;
}
void executeInstruction_addChild_P(VirtualMachine* virtualMachine,Primitive* primitive)
{
	if(virtualMachine->topTransformGroupNode==null)addChild_BP(virtualMachine->branchGroup,primitive);
	else addChild_TP(virtualMachine->topTransformGroupNode->transformGroup,primitive);
}
void executeInstruction_addChild_S(VirtualMachine* virtualMachine,Shape3D* shape3D)
{
	if(virtualMachine->topTransformGroupNode==null)addChild_BS(virtualMachine->branchGroup,shape3D);
	else addChild_TS(virtualMachine->topTransformGroupNode->transformGroup,shape3D);
}
void executeInstruction_addChild_T(VirtualMachine* virtualMachine,TransformGroup* transformGroup)
{
	if(virtualMachine->topTransformGroupNode==null)addChild_BT(virtualMachine->branchGroup,transformGroup);
	else addChild_TT(virtualMachine->topTransformGroupNode->transformGroup,transformGroup);
}
void executeInstruction_SPHERE(VirtualMachine* virtualMachine,int* instruction)
{
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	double r=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	executeInstruction_addChild_P(virtualMachine,newSphere(r,Appearance1));
	virtualMachine->instructionPointer++;
}
void executeInstruction_CONE(VirtualMachine* virtualMachine,int* instruction)
{
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	double r=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	double h=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
	executeInstruction_addChild_P(virtualMachine,newCone(r,h,Appearance1));
	virtualMachine->instructionPointer++;
}
void executeInstruction_CYLINDER(VirtualMachine* virtualMachine,int* instruction)
{
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	double r=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	double h=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
	executeInstruction_addChild_P(virtualMachine,newCylinder(r,h,Appearance1));
	virtualMachine->instructionPointer++;
}
void executeInstruction_BOX(VirtualMachine* virtualMachine,int* instruction)
{
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	Vector* size=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	executeInstruction_addChild_P(virtualMachine,newBox(size->x,size->y,size->z,Appearance1));
	virtualMachine->instructionPointer++;
}
void executeInstruction_CUBE(VirtualMachine* virtualMachine,int* instruction)
{
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	double size=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	executeInstruction_addChild_P(virtualMachine,newColorCube(size,Appearance1));
	virtualMachine->instructionPointer++;
}
void executeInstruction_PUSHTG(VirtualMachine* virtualMachine,int* instruction)
{
	Transform3D* transform3D=newTransform3D();
	setTranslation(transform3D,virtualMachine->translation);
	setRotation(transform3D,virtualMachine->rotation);
	setScale(transform3D,virtualMachine->scale);
	TransformGroup* transformGroup=newTransformGroup(transform3D);
	executeInstruction_addChild_T(virtualMachine,transformGroup);
	TransformGroupNode* transformGroupNode=newTransformGroupNode(transformGroup);
	transformGroupNode->next=virtualMachine->topTransformGroupNode;
	virtualMachine->topTransformGroupNode=transformGroupNode;
	virtualMachine->instructionPointer++;
}
void executeInstruction_POPTG(VirtualMachine* virtualMachine,int* instruction)
{
	if(virtualMachine->topTransformGroupNode==null)return;
	virtualMachine->topTransformGroupNode=virtualMachine->topTransformGroupNode->next;
	virtualMachine->instructionPointer++;
}
void executeInstruction_TRANSLATE(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->translation=newVector3d(v->x,v->y,v->z);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ROTATE(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	double f=executeInstruction_getDouble(virtualMachine,instruction[3],instruction[4]);
	virtualMachine->rotation=newAxisAngle4d(v->x,v->y,v->z,f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ROTX(VirtualMachine* virtualMachine,int* instruction)
{
	double f=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->rotation=newAxisAngle4d(1,0,0,f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ROTY(VirtualMachine* virtualMachine,int* instruction)
{
	double f=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->rotation=newAxisAngle4d(0,1,0,f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ROTZ(VirtualMachine* virtualMachine,int* instruction)
{
	double f=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->rotation=newAxisAngle4d(0,0,1,f);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SCALE(VirtualMachine* virtualMachine,int* instruction)
{
	Vector* v=executeInstruction_getVector(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->scale=newVector3d(v->x,v->y,v->z);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SURF(VirtualMachine* virtualMachine,int* instruction)
{
	int paramNumber=2,i,j,c,p=3,m=virtualMachine->uStep,n=virtualMachine->vStep;
	double u,u0,du,v,v0,dv;
	u=instruction[p+0];
	v=instruction[p+1];
	p+=paramNumber;
	u0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	du=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-u0)/(m-1);p+=2;
	v0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	dv=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-v0)/(n-1);p+=2;
	int ip0=instruction[p+0],ip1=instruction[p+1],ip=virtualMachine->instructionPointer;
	Point3f** coordinates=new_Point3f(m*n);
	for(i=0;i<m;i++)
	{
		u=u0+i*du;
		virtualMachine->doubleTable[instruction[3]]=u;
		for(j=0;j<n;j++)
		{
			v=v0+j*dv;
			virtualMachine->doubleTable[instruction[4]]=v;
			virtualMachine->instructionPointer=ip0;
			while(virtualMachine->instructionPointer<ip1)
			{
				int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
				executeInstruction[instruction1[0]](virtualMachine,instruction1);
			}
			if(instruction[1]=='#')coordinates[i*n+j]=newPoint3f(u,virtualMachine->doubleTable[instruction[2]],v);
			else {Vector* v=virtualMachine->vectorTable[instruction[2]];coordinates[i*n+j]=newPoint3f(v->x,v->y,v->z);}
		}
	}
	Shape3D* surface3D=newSurface3D(coordinates,virtualMachine->textureCoordinates,virtualMachine->colors,m,n);
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	setTexture(Appearance1,virtualMachine->texture);
	setAppearance(surface3D,Appearance1);
	executeInstruction_addChild_S(virtualMachine,surface3D);
	virtualMachine->instructionPointer=ip+1;
}
void executeInstruction_ISOSURF(VirtualMachine* virtualMachine,int* instruction)
{
	int paramNumber=3,k,i,j,c,p=5,n=virtualMachine->uStep,l=virtualMachine->vStep,m=virtualMachine->wStep;
	double x,x0,dx,y,y0,dy,z,z0,dz;
	p+=paramNumber;
	x0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	dx=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-x0)/(n-1);p+=2;
	y0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	dy=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-y0)/(l-1);p+=2;
	z0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	dz=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-z0)/(m-1);p+=2;
	int ip0=instruction[p+0],ip1=instruction[p+1],ip=virtualMachine->instructionPointer;
	double value=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	Value3D* value3D=newValue3D(x0,dx,y0,dy,z0,dz,l,m,n);
	for(k=0;k<l;k++)
	{
		double y=y0+k*dy;
		virtualMachine->doubleTable[instruction[6]]=y;
		for(i=0;i<m;i++)
		{
			double z=z0+i*dz;
			virtualMachine->doubleTable[instruction[7]]=z;
			for(j=0;j<n;j++)
			{
				double x=x0+j*dx;
				virtualMachine->doubleTable[instruction[5]]=x;
				virtualMachine->instructionPointer=ip0;
				while(virtualMachine->instructionPointer<ip1)
				{
					int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
					executeInstruction[instruction1[0]](virtualMachine,instruction1);
				}
				value3D->values[k*m*n+i*n+j]=virtualMachine->doubleTable[instruction[4]];
			}
		}
	}
	Shape3D* isoSurface3D=newIsoSurface3D(value3D,value);
	executeInstruction_newMaterial(virtualMachine);
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	setAppearance(isoSurface3D,Appearance1);
	executeInstruction_addChild_S(virtualMachine,isoSurface3D);
	virtualMachine->instructionPointer=ip+1;
}
void executeInstruction_COLORS(VirtualMachine* virtualMachine,int* instruction)
{
	int paramNumber=2,i,j,c,p=3,m=virtualMachine->uStep,n=virtualMachine->vStep;
	double u,u0,du,v,v0,dv;
	u=instruction[p+0];
	v=instruction[p+1];
	p+=paramNumber;
	u0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	du=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-u0)/(m-1);p+=2;
	v0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	dv=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-v0)/(n-1);p+=2;
	int ip0=instruction[p+0],ip1=instruction[p+1],ip=virtualMachine->instructionPointer;
	Color3f** colors=new_Color3f(m*n);
	for(i=0;i<m;i++)
	{
		u=u0+i*du;
		virtualMachine->doubleTable[instruction[3]]=u;
		for(j=0;j<n;j++)
		{
			v=v0+j*dv;
			virtualMachine->doubleTable[instruction[4]]=v;
			virtualMachine->instructionPointer=ip0;
			while(virtualMachine->instructionPointer<ip1)
			{
				int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
				executeInstruction[instruction1[0]](virtualMachine,instruction1);
			}
			if(instruction[1]=='#'){double f=virtualMachine->doubleTable[instruction[2]];colors[i*n+j]=newColor3f(f,f,f);}
			else {Vector* v=virtualMachine->vectorTable[instruction[2]];colors[i*n+j]=newColor3f(v->x,v->y,v->z);}
		}
	}
	virtualMachine->colors=colors;
	virtualMachine->instructionPointer=ip+1;
}
void executeInstruction_COLORS0(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->colors=null;
	virtualMachine->instructionPointer++;
}
void executeInstruction_TEXTURE(VirtualMachine* virtualMachine,int* instruction)
{
	String s=executeInstruction_getString(virtualMachine,instruction[1],instruction[2]);
	int w=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
	int h=executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]);
	virtualMachine->texture=newTexture2D(s,w,h);
	virtualMachine->instructionPointer++;
}
void executeInstruction_TEXTURE0(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->texture=null;
	virtualMachine->instructionPointer++;
}
void executeInstruction_TEXCOORD(VirtualMachine* virtualMachine,int* instruction)
{
	int i,j,m=virtualMachine->uStep,n=virtualMachine->vStep;
	Point2f** textureCoordinates=new_Point2f(m*n);
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			textureCoordinates[i*n+j]=newPoint2f((0.0f+i)/(m-1),(0.0f+j)/(n-1));
		}
	}
	virtualMachine->textureCoordinates=textureCoordinates;
	virtualMachine->instructionPointer++;
}
void executeInstruction_TEXCOORD0(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->textureCoordinates=null;
	virtualMachine->instructionPointer++;
}
void executeInstruction_TEXCOORDS(VirtualMachine* virtualMachine,int* instruction)
{
	int paramNumber=2,i,j,c,p=3,m=virtualMachine->uStep,n=virtualMachine->vStep;
	double u,u0,du,v,v0,dv;
	u=instruction[p+0];
	v=instruction[p+1];
	p+=paramNumber;
	u0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	du=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-u0)/(m-1);p+=2;
	v0=executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1]);p+=2;
	dv=(executeInstruction_getDouble(virtualMachine,instruction[p+0],instruction[p+1])-v0)/(n-1);p+=2;
	int ip0=instruction[p+0],ip1=instruction[p+1],ip=virtualMachine->instructionPointer;
	Point2f** textureCoordinates=new_Point2f(m*n);
	for(i=0;i<m;i++)
	{
		u=u0+i*du;
		virtualMachine->doubleTable[instruction[3]]=u;
		for(j=0;j<n;j++)
		{
			v=v0+j*dv;
			virtualMachine->doubleTable[instruction[4]]=v;
			virtualMachine->instructionPointer=ip0;
			while(virtualMachine->instructionPointer<ip1)
			{
				int* instruction1=virtualMachine->instructions[virtualMachine->instructionPointer];
				executeInstruction[instruction1[0]](virtualMachine,instruction1);
			}
			if(instruction[1]=='#'){double f=virtualMachine->doubleTable[instruction[2]];textureCoordinates[i*n+j]=newPoint2f(f,f);}
			else {Vector* v=virtualMachine->vectorTable[instruction[2]];textureCoordinates[i*n+j]=newPoint2f(v->x,v->y);}
		}
	}
	virtualMachine->textureCoordinates=textureCoordinates;
	virtualMachine->instructionPointer=ip+1;
}
void executeInstruction_SHARE(VirtualMachine* virtualMachine,int* instruction)
{
	String sharedGroupName=executeInstruction_getString(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->currentSharedGroup=addSharedGroup(virtualMachine->sharedGroupList,sharedGroupName);
	TransformGroup* transformGroup=virtualMachine->currentSharedGroup->transformGroup;
	TransformGroupNode* transformGroupNode=newTransformGroupNode(transformGroup);
	transformGroupNode->next=virtualMachine->topTransformGroupNode;
	virtualMachine->topTransformGroupNode=transformGroupNode;
	virtualMachine->instructionPointer++;
}
void executeInstruction_SHARE0(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->topTransformGroupNode=virtualMachine->topTransformGroupNode->next;
	virtualMachine->instructionPointer++;
}
void executeInstruction_GROUP(VirtualMachine* virtualMachine,int* instruction)
{
	String sharedGroupName=executeInstruction_getString(virtualMachine,instruction[1],instruction[2]);
	TransformGroup* transformGroup=getSharedGroup(virtualMachine->sharedGroupList,sharedGroupName)->transformGroup;
	executeInstruction_addChild_T(virtualMachine,transformGroup);
	virtualMachine->instructionPointer++;
}
void executeInstruction_TRANSPARENCY(VirtualMachine* virtualMachine,int* instruction)
{
	double transparency=executeInstruction_getDouble(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->transparencyAttributes=newTransparencyAttributes(BLENDED,transparency);
	virtualMachine->instructionPointer++;
}
void executeInstruction_POLYGONS(VirtualMachine* virtualMachine,int* instruction)
{
	int id0=virtualMachine->identifierTable[instruction[1]];
	int id1=virtualMachine->identifierTable[instruction[2]];
	int id2=virtualMachine->identifierTable[instruction[3]];
	int coordinatesLength=virtualMachine->integerTable[id0],i;
	Point3f** coordinates=new_Point3f(coordinatesLength);
	for(i=0;i<coordinatesLength;i++)
	{
		Vector* v=virtualMachine->vectorTable[virtualMachine->integerTable[id0+1+i]];
		coordinates[i]=newPoint3f(v->x,v->y,v->z);
	}
	int coordinateIndicesLength=virtualMachine->integerTable[id1];
	int* coordinateIndices=new_int(coordinateIndicesLength);
	for(i=0;i<coordinateIndicesLength;i++)
	{
		coordinateIndices[i]=virtualMachine->integerTable[virtualMachine->integerTable[id1+1+i]];
	}
	int stripCountsLength=virtualMachine->integerTable[id2];
	int* stripCounts=new_int(stripCountsLength);
	for(i=0;i<stripCountsLength;i++)
	{
		stripCounts[i]=virtualMachine->integerTable[virtualMachine->integerTable[id2+1+i]];
	}
	Shape3D* polygons3D=newPolygons3D(coordinates,coordinatesLength,coordinateIndices,coordinateIndicesLength,stripCounts,stripCountsLength);
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	setTexture(Appearance1,virtualMachine->texture);
	setAppearance(polygons3D,Appearance1);
	executeInstruction_addChild_S(virtualMachine,polygons3D);
	virtualMachine->instructionPointer++;
}
void executeInstruction_CTRLCOUNT(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->uCtrlCount=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->vCtrlCount=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
	virtualMachine->wCtrlCount=executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ORDER(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->uOrder=executeInstruction_getInteger(virtualMachine,instruction[1],instruction[2]);
	virtualMachine->vOrder=executeInstruction_getInteger(virtualMachine,instruction[3],instruction[4]);
	virtualMachine->wOrder=executeInstruction_getInteger(virtualMachine,instruction[5],instruction[6]);
	virtualMachine->instructionPointer++;
}
double* executeInstruction_getKnots(VirtualMachine* virtualMachine,int ic,int id,int uvw)
{
	int ctrlPointsLength=0,order=0;
	switch(uvw)
	{
		case 0:ctrlPointsLength=virtualMachine->uCtrlCount;order=virtualMachine->uOrder;break;
		case 1:ctrlPointsLength=virtualMachine->vCtrlCount;order=virtualMachine->vOrder;break;
		case 2:ctrlPointsLength=virtualMachine->wCtrlCount;order=virtualMachine->wOrder;break;
	}
	double* knots;
	switch(ic)
	{
		case KeyWord_null:knots=getStandardUniformNurbsKnots(ctrlPointsLength,order);break;
		case KeyWord_void:knots=getSemiUniformNurbsKnots(ctrlPointsLength,order);break;
		case KeyWord_default:knots=getBezierUniformNurbsKnots(ctrlPointsLength,order);break;
		default:
		{
			int knotsLength=virtualMachine->integerTable[id],i;
			knots=new_double(knotsLength);
			for(i=0;i<knotsLength;i++)
			{
				knots[i]=virtualMachine->doubleTable[virtualMachine->integerTable[id+1+i]];
			}
		}
	}
	return knots;
}
void executeInstruction_KNOTS(VirtualMachine* virtualMachine,int* instruction)
{
	virtualMachine->uKnots=executeInstruction_getKnots(virtualMachine,instruction[1],instruction[2],0);
	virtualMachine->vKnots=executeInstruction_getKnots(virtualMachine,instruction[3],instruction[4],1);
	virtualMachine->wKnots=executeInstruction_getKnots(virtualMachine,instruction[5],instruction[6],2);
	virtualMachine->instructionPointer++;
}
void executeInstruction_WEIGHTS(VirtualMachine* virtualMachine,int* instruction)
{
	int id=virtualMachine->identifierTable[instruction[1]],i,j,k;
	int m=virtualMachine->uCtrlCount,n=virtualMachine->vCtrlCount,l=virtualMachine->wCtrlCount;
	double*** weights=new__Double(l,m,n);
	for(k=0;k<l;k++)for(i=0;i<m;i++)for(j=0;j<n;j++)weights[k][i][j]=virtualMachine->doubleTable[virtualMachine->integerTable[id+1+k*m*n+i*n+j]];
	virtualMachine->weights=weights;
	virtualMachine->instructionPointer++;
}
void executeInstruction_NURBS(VirtualMachine* virtualMachine,int* instruction)
{
	int id=virtualMachine->identifierTable[instruction[1]],i,j;
	int m=virtualMachine->uCtrlCount,n=virtualMachine->vCtrlCount;
	Point3f*** ctrlPoints=new__Point3f(m,n);
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			Vector* v=virtualMachine->vectorTable[virtualMachine->integerTable[id+1+i*n+j]];
			ctrlPoints[i][j]=newPoint3f(v->x,v->y,v->z);
		}
	}
	double*** weights=virtualMachine->weights;
	double *uKnots=virtualMachine->uKnots,*vKnots=virtualMachine->vKnots;
	int uOrder=virtualMachine->uOrder,vOrder=virtualMachine->vOrder;
	int uStep=virtualMachine->uStep,vStep=virtualMachine->vStep;
	Shape3D* nurbsSurface3D=newNurbsSurface3D(ctrlPoints,m,n,weights[0],uKnots,vKnots,uOrder,vOrder,uStep,vStep);
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	setTexture(Appearance1,virtualMachine->texture);
	setAppearance(nurbsSurface3D,Appearance1);
	executeInstruction_addChild_S(virtualMachine,nurbsSurface3D);
	virtualMachine->instructionPointer++;
}
void executeInstruction_ISONURBS(VirtualMachine* virtualMachine,int* instruction)
{
	int id=virtualMachine->identifierTable[instruction[1]],i,j,k;
	int m=virtualMachine->uCtrlCount,n=virtualMachine->vCtrlCount,l=virtualMachine->wCtrlCount;
	double*** ctrlValues=new__Double(l,m,n);
	for(k=0;k<l;k++)for(i=0;i<m;i++)for(j=0;j<n;j++)ctrlValues[k][i][j]=virtualMachine->doubleTable[virtualMachine->integerTable[id+1+k*m*n+i*n+j]];
	double value=executeInstruction_getDouble(virtualMachine,instruction[2],instruction[3]);
	Vector* v0=executeInstruction_getVector(virtualMachine,instruction[4],instruction[5]);
	Vector* v1=executeInstruction_getVector(virtualMachine,instruction[6],instruction[7]);
	Point3f *p0=newPoint3f(v0->x,v0->y,v0->z),*p1=newPoint3f(v1->x,v1->y,v1->z);
	double*** weights=virtualMachine->weights;
	double *uKnots=virtualMachine->uKnots,*vKnots=virtualMachine->vKnots,*wKnots=virtualMachine->wKnots;
	int uOrder=virtualMachine->uOrder,vOrder=virtualMachine->vOrder,wOrder=virtualMachine->wOrder;
	int uStep=virtualMachine->uStep,vStep=virtualMachine->vStep,wStep=virtualMachine->wStep;
	Shape3D* nurbsIsoSurface3D=newNurbsIsoSurface3D(p0,p1,value,ctrlValues,l,m,n,weights,uKnots,vKnots,wKnots,uOrder,vOrder,wOrder,uStep,vStep,wStep);
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	setAppearance(nurbsIsoSurface3D,Appearance1);
	executeInstruction_addChild_S(virtualMachine,nurbsIsoSurface3D);
	virtualMachine->instructionPointer++;
}
void executeInstruction_TORNURBS(VirtualMachine* virtualMachine,int* instruction)
{
	int id=virtualMachine->identifierTable[instruction[1]],i,j;
	int m=virtualMachine->uCtrlCount,n=virtualMachine->vCtrlCount;
	Point3f*** ctrlPoints=new__Point3f(m,n);
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			Vector* v=virtualMachine->vectorTable[virtualMachine->integerTable[id+1+i*n+j]];
			ctrlPoints[i][j]=newPoint3f(v->x,v->y,v->z);
		}
	}
	double*** weights=virtualMachine->weights;
	int uOrder=virtualMachine->uOrder,vOrder=virtualMachine->vOrder;
	int uStep=virtualMachine->uStep,vStep=virtualMachine->vStep;
	Shape3D* nurbsTorus3D=newNurbsTorus3D(ctrlPoints,m,n,weights[0],uOrder,vOrder,uStep,vStep);
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	setTexture(Appearance1,virtualMachine->texture);
	setAppearance(nurbsTorus3D,Appearance1);
	executeInstruction_addChild_S(virtualMachine,nurbsTorus3D);
	virtualMachine->instructionPointer++;
}
void executeInstruction_SPHNURBS(VirtualMachine* virtualMachine,int* instruction)
{
	int id0=virtualMachine->identifierTable[instruction[1]];
	int id1=instruction[2]==-1?-1:virtualMachine->identifierTable[instruction[2]];
	int id2=virtualMachine->identifierTable[instruction[3]];
	int id3=virtualMachine->identifierTable[instruction[4]];
	int ctrlPointsLength=virtualMachine->integerTable[id0],i;
	Point3f** ctrlPoints=new_Point3f(ctrlPointsLength);
	for(i=0;i<ctrlPointsLength;i++)
	{
		Vector* v=virtualMachine->vectorTable[virtualMachine->integerTable[id0+1+i]];
		ctrlPoints[i]=newPoint3f(v->x,v->y,v->z);
	}
	int weightsLength=id1==-1?0:virtualMachine->integerTable[id1];
	double* weights=new_double(weightsLength);
	for(i=0;i<weightsLength;i++)
	{
		weights[i]=virtualMachine->doubleTable[virtualMachine->integerTable[id1+1+i]];
	}
	if(id1==-1)weights=null;
	int ordersLength=virtualMachine->integerTable[id2];
	int* orders=new_int(ordersLength);
	for(i=0;i<ordersLength;i++)
	{
		orders[i]=virtualMachine->integerTable[virtualMachine->integerTable[id2+1+i]];
	}
	int ctrlCountsLength=virtualMachine->integerTable[id3];
	int* ctrlCounts=new_int(ctrlCountsLength);
	for(i=0;i<ctrlCountsLength;i++)
	{
		ctrlCounts[i]=virtualMachine->integerTable[virtualMachine->integerTable[id3+1+i]];
	}
	int uStep=virtualMachine->uStep,vStep=virtualMachine->vStep;
	Shape3D* nurbsSphere3D=newNurbsSphere3D(ctrlPoints,weights,orders,ctrlCounts,ctrlCountsLength,uStep,vStep);
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	setAppearance(nurbsSphere3D,Appearance1);
	executeInstruction_addChild_S(virtualMachine,nurbsSphere3D);
	virtualMachine->instructionPointer++;
}
void executeInstruction_CTRLNURBS(VirtualMachine* virtualMachine,int* instruction)
{
	int id=virtualMachine->identifierTable[instruction[1]],i,j;
	int m=virtualMachine->uCtrlCount,n=virtualMachine->vCtrlCount;
	Point3f*** ctrlPoints=new__Point3f(m,n);
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			Vector* v=virtualMachine->vectorTable[virtualMachine->integerTable[id+1+i*n+j]];
			ctrlPoints[i][j]=newPoint3f(v->x,v->y,v->z);
		}
	}
	double*** weights=virtualMachine->weights;
	int uOrder=virtualMachine->uOrder,vOrder=virtualMachine->vOrder;
	int uStep=virtualMachine->uStep,vStep=virtualMachine->vStep;
	Shape3D* nurbsCtrlSurface3D=newNurbsCtrlSurface3D(ctrlPoints,m,n,weights[0],uOrder,vOrder,uStep,vStep);
	Appearance* Appearance1=executeInstruction_getAppearance(virtualMachine);
	setTexture(Appearance1,virtualMachine->texture);
	setAppearance(nurbsCtrlSurface3D,Appearance1);
	executeInstruction_addChild_S(virtualMachine,nurbsCtrlSurface3D);
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
	executeInstruction[SCAN]=executeInstruction_SCAN;
	executeInstruction[PRINT]=executeInstruction_PRINT;
	executeInstruction[OPEN]=executeInstruction_OPEN;
	executeInstruction[READ]=executeInstruction_READ;
	executeInstruction[WRITE]=executeInstruction_WRITE;
	executeInstruction[CLOSE]=executeInstruction_CLOSE;
	executeInstruction[RUN]=executeInstruction_RUN;
	executeInstruction[NEW]=executeInstruction_NEW;
	executeInstruction[INVOKE]=executeInstruction_INVOKE;
	executeInstruction[MOVPI]=executeInstruction_MOVPI;
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
	executeInstruction[VMUL]=executeInstruction_VMUL;
	executeInstruction[FUNC]=executeInstruction_FUNC;
	executeInstruction[PRINTF]=executeInstruction_PRINTF;
	executeInstruction[STEP]=executeInstruction_STEP;
	executeInstruction[LIGHT0]=executeInstruction_LIGHT0;
	executeInstruction[LIGHT]=executeInstruction_LIGHT;
	executeInstruction[COLOR]=executeInstruction_COLOR;
	executeInstruction[COLOR0]=executeInstruction_COLOR0;
	executeInstruction[SPHERE]=executeInstruction_SPHERE;
	executeInstruction[ACOLOR]=executeInstruction_ACOLOR;
	executeInstruction[ECOLOR]=executeInstruction_ECOLOR;
	executeInstruction[SCOLOR]=executeInstruction_SCOLOR;
	executeInstruction[SHINE]=executeInstruction_SHINE;
	executeInstruction[SPHERE]=executeInstruction_SPHERE;
	executeInstruction[CONE]=executeInstruction_CONE;
	executeInstruction[CYLINDER]=executeInstruction_CYLINDER;
	executeInstruction[BOX]=executeInstruction_BOX;
	executeInstruction[CUBE]=executeInstruction_CUBE;
	executeInstruction[PUSHTG]=executeInstruction_PUSHTG;
	executeInstruction[POPTG]=executeInstruction_POPTG;
	executeInstruction[TRANSLATE]=executeInstruction_TRANSLATE;
	executeInstruction[ROTATE]=executeInstruction_ROTATE;
	executeInstruction[ROTX]=executeInstruction_ROTX;
	executeInstruction[ROTY]=executeInstruction_ROTY;
	executeInstruction[ROTZ]=executeInstruction_ROTZ;
	executeInstruction[SCALE]=executeInstruction_SCALE;
	executeInstruction[SURF]=executeInstruction_SURF;
	executeInstruction[ISOSURF]=executeInstruction_ISOSURF;
	executeInstruction[COLORS]=executeInstruction_COLORS;
	executeInstruction[COLORS0]=executeInstruction_COLORS0;
	executeInstruction[TEXTURE]=executeInstruction_TEXTURE;
	executeInstruction[TEXTURE0]=executeInstruction_TEXTURE0;
	executeInstruction[TEXCOORD]=executeInstruction_TEXCOORD;
	executeInstruction[TEXCOORD0]=executeInstruction_TEXCOORD0;
	executeInstruction[TEXCOORDS]=executeInstruction_TEXCOORDS;
	executeInstruction[SHARE]=executeInstruction_SHARE;
	executeInstruction[SHARE0]=executeInstruction_SHARE0;
	executeInstruction[GROUP]=executeInstruction_GROUP;
	executeInstruction[TRANSPARENCY]=executeInstruction_TRANSPARENCY;
	executeInstruction[POLYGONS]=executeInstruction_POLYGONS;
	executeInstruction[CTRLCOUNT]=executeInstruction_CTRLCOUNT;
	executeInstruction[ORDER]=executeInstruction_ORDER;
	executeInstruction[KNOTS]=executeInstruction_KNOTS;
	executeInstruction[WEIGHTS]=executeInstruction_WEIGHTS;
	executeInstruction[NURBS]=executeInstruction_NURBS;
	executeInstruction[ISONURBS]=executeInstruction_ISONURBS;
	executeInstruction[TORNURBS]=executeInstruction_TORNURBS;
	executeInstruction[SPHNURBS]=executeInstruction_SPHNURBS;
	executeInstruction[CTRLNURBS]=executeInstruction_CTRLNURBS;
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
	virtualMachine->vectorNumber=virtualMachine_Class->vectorNumber;
	virtualMachine->vectorTable=newVectors(virtualMachine->vectorNumber);
	virtualMachine->beginInstructionPointer=virtualMachine_Class->beginInstructionPointer;
	virtualMachine->declarations=virtualMachine_Class->declarations;
	virtualMachine->declarationsLength=virtualMachine_Class->declarationsLength;
	virtualMachine->queue=virtualMachine_Class->queue;
	virtualMachine->stack=virtualMachine_Class->stack;
	virtualMachine->instructions=virtualMachine_Class->instructions;
	virtualMachine->uStep=DEFAULT_FUNC_STEP;
	virtualMachine->vStep=DEFAULT_FUNC_STEP;
	virtualMachine->wStep=DEFAULT_FUNC_STEP;
	virtualMachine->simpleUniverse=null;
	virtualMachine->branchGroup=virtualMachine_Class->branchGroup;
	virtualMachine->currentSharedGroup=null;
	virtualMachine->sharedGroupList=newSharedGroupList();
	virtualMachine->topTransformGroupNode=null;
	virtualMachine->light=null;
	virtualMachine->diffuseColor=newColor3f(1,1,1);
	virtualMachine->emissiveColor=newColor3f(0,0,0);
	virtualMachine->ambientColor=newColor3f(0,0,0);
	virtualMachine->specularColor=newColor3f(0,0,0);
	virtualMachine->shininess=0.5f;
	virtualMachine->material=newMaterial();
	virtualMachine->materialIsChanged=false;
	virtualMachine->translation=null;
	virtualMachine->scale=null;
	virtualMachine->rotation=null;
	virtualMachine->textureCoordinates=null;
	virtualMachine->colors=null;
	virtualMachine->texture=null;
	virtualMachine->transparencyAttributes=null;
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
