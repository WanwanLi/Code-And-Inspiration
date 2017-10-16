#define SyntaxTranslator struct SyntaxTranslator
SyntaxTranslator
{
	String className;
	String* methodTable;
	int* methodPointerTable;
	int* methodReturnTypeTable;
	int methodNumber;
	String* classTable;
	int classNumber;
	int objectNumber;
	int identifierNumber;
	int* integerTable;
	int integerTableLength;
	int integerNumber;
	double* doubleTable;
	int doubleTableLength;
	int doubleNumber;
	String* stringTable;
	int stringTableLength;
	int stringNumber;
	int vectorNumber;
	int* declarations;
	int declarationsLength;
	int beginInstructionPointer;
	List* instructionsList;
	int instructionsLength;
};
List* classList;
List* methodList;
List* methodPointerList;
List* methodReturnTypeList;
List* invokeList;
List* declarationsList;
List* instructionsList;
List* switchList;
List* breakStack;
List* codeblockStack;
MethodList** methodListTable;
FunctionList* functionList;
Identifier** identifiers;
int instructionPointer;
int beginInstructionPointer;
String* methodTable;
int* methodPointerTable;
int* methodReturnTypeTable;
String* classTable;
int* wordTable;
int* integerTable;
double* doubleTable;
String* stringTable;
Identifier** identifierTable;
int classNumber;
int methodNumber;
int objectNumber;
int identifierNumber;
int integerNumber;
int doubleNumber;
int stringNumber;
int vectorNumber;
List* argumentsStack0;
List* argumentsStack1;
int w0,w1;
int index;
int wordLength;
int instructionsLength;
LexicalAnalyser* this_lexicalAnalyser;
void initSyntaxTranslator();
void getVirtualMachineInstructions();
void getClassDeclarationInstructions();
void getMethodsInstructions();
void getArgumentsInstructions();
void getCodeBlockInstructions();
void getCodeLineInstructions();
void getDeclarationInstructions(int identifierType);
void getArrayDeclarationInstructions(int identifierType);
void getArrayAssignmentInstructions(int id);
void getArrayInitInstructions(int id);
void getFunctionDeclarationInstructions();
void getFunctionAssignmentInstructions(int id);
void getCallMethodInstructions();
void getNewObjectInstructions(int id);
void getNewClassInstructions(int classIndex);
void getInvokeMethodInstructions(int classIndex,int objectIndex);
void getAssignmentInstructions();
int* getConditionalAssignmentInstructions(int* id0);
void getIfElseInstructions();
void getWhileInstructions();
void getUntilInstructions();
void getDoWhileInstructions();
void getForInstructions();
void getSwitchInstructions();
void getCaseInstructions();
void getDefaultInstructions();
void getBreakInstructions();
void getCodeblockBreakInstructions(int ip_end);
void getPopInstructions();
void getReturnInstructions();
void getThisInstructions();
int* getThisIdentifierInstructions();
int* getAndInstructions();
int* getLogicInstructions();
int* getCmpInstructions();
int* getAddInstructions(int* type);
int* getMulInstructions(int* type);
int* getPowInstructions(int* type);
int* getIdentifierInstructions(int* type);
int* getNewIdentifier(int type);
int* newID(char c,int id);
int getAddValue();
int getMulValue();
int getIntValue();
void generateVirtualMachineInstructions(SyntaxTranslator* syntaxTranslator);
void printVirtualMachineInstructions(SyntaxTranslator* syntaxTranslator);
SyntaxTranslator* newSyntaxTranslator(LexicalAnalyser* lexicalAnalyser)
{
	this_lexicalAnalyser=lexicalAnalyser;
	initSyntaxTranslator();
	getVirtualMachineInstructions();
	SyntaxTranslator* syntaxTranslator=(SyntaxTranslator*)malloc(sizeof(SyntaxTranslator));
	syntaxTranslator->className=lexicalAnalyser->className;
	syntaxTranslator->classTable=classTable;
	syntaxTranslator->classNumber=classNumber;
	syntaxTranslator->objectNumber=objectNumber;
	syntaxTranslator->methodTable=methodTable;
	syntaxTranslator->methodPointerTable=methodPointerTable;
	syntaxTranslator->methodReturnTypeTable=methodReturnTypeTable;
	syntaxTranslator->methodNumber=methodNumber;
	syntaxTranslator->identifierNumber=identifierNumber;
	syntaxTranslator->integerTable=integerTable;
	syntaxTranslator->integerTableLength=lexicalAnalyser->integerTableLength;
	syntaxTranslator->integerNumber=integerNumber;
	syntaxTranslator->doubleTable=doubleTable;
	syntaxTranslator->doubleTableLength=lexicalAnalyser->doubleTableLength;
	syntaxTranslator->doubleNumber=doubleNumber;
	syntaxTranslator->stringTable=stringTable;
	syntaxTranslator->stringTableLength=lexicalAnalyser->stringTableLength;
	syntaxTranslator->stringNumber=stringNumber;
	syntaxTranslator->vectorNumber=vectorNumber;
	syntaxTranslator->beginInstructionPointer=beginInstructionPointer;
	syntaxTranslator->declarationsLength=declarationsList->length;
	syntaxTranslator->declarations=toIntegers(declarationsList);
	syntaxTranslator->instructionsLength=instructionsLength;
	syntaxTranslator->instructionsList=instructionsList;
	return syntaxTranslator;
}
void initSyntaxTranslator()
{
	index=0;
	instructionsLength=0;
	classNumber=0;
	methodNumber=0;
	objectNumber=0;
	wordTable=this_lexicalAnalyser->wordTable;
	identifierTable=this_lexicalAnalyser->identifierTable;
	integerTable=this_lexicalAnalyser->integerTable;
	stringTable=this_lexicalAnalyser->stringTable;
	doubleTable=this_lexicalAnalyser->doubleTable;
	wordLength=this_lexicalAnalyser->wordTableLength/2;
	identifierNumber=this_lexicalAnalyser->identifierTableLength;
	integerNumber=this_lexicalAnalyser->integerTableLength;
	doubleNumber=this_lexicalAnalyser->doubleTableLength;
	stringNumber=this_lexicalAnalyser->stringTableLength;
	vectorNumber=0;
	instructionsList=newList();
	methodList=newList();
	methodPointerList=newList();
	methodReturnTypeList=newList();
	argumentsStack0=newList();
	argumentsStack1=newList();
	declarationsList=newList();
	classList=newList();
	breakStack=newList();
	codeblockStack=newList();
	functionList=newFunctionList();
}
void nextWord()
{
	if(index>=wordLength){w0=w1=-1;return;}
	w0=wordTable[index*2+0];
	w1=wordTable[index*2+1];
	index++;
}
void seekNextOneWord()
{
	index++;
	if(index>=wordLength){w0=w1=-1;index--;return;}
	w0=wordTable[index*2+0];
	w1=wordTable[index*2+1];
	index--;
}
void seekNextTwoWord()
{
	index+=2;
	if(index>=wordLength){w0=w1=-1;index-=2;return;}
	w0=wordTable[index*2+0];
	w1=wordTable[index*2+1];
	index-=2;
}
void getMethodListTable()
{
	methodListTable=(MethodList**)malloc(classNumber*sizeof(MethodList*));int k;
	for(k=0;k<classNumber;k++)
	{
		String className=classTable[k];
		String fileName=add(className,".javab");
		FILE* file=fopen(fileName,"r");
		if(file!=NULL)
		{
			fclose(file);
			LexicalAnalyser* LexicalAnalyser1=newLexicalAnalyser(className);
			SyntaxTranslator* SyntaxTranslator1=newSyntaxTranslator(LexicalAnalyser1);
			generateVirtualMachineInstructions(SyntaxTranslator1);
			printVirtualMachineInstructions(SyntaxTranslator1);
		}
		fileName=add(className,".classb");
		file=fopen(fileName,"r");
		if(file==NULL){printf("File:%s.classb dose not exist...",className);exit(0);}
		methodListTable[k]=newMethodList();
		fscanf(file,"%d,",&methodListTable[k]->methodNumber);
		methodListTable[k]->methodTable=newString(methodListTable[k]->methodNumber);int i;
		for(i=0;i<methodListTable[k]->methodNumber;i++)methodListTable[k]->methodTable[i]=fscans(file);
		methodListTable[k]->methodPointerTable=newInt(methodListTable[k]->methodNumber);
		for(i=0;i<methodListTable[k]->methodNumber;i++)fscanf(file,"%d,",&methodListTable[k]->methodPointerTable[i]);
		methodListTable[k]->methodReturnTypeTable=newInt(methodListTable[k]->methodNumber);
		for(i=0;i<methodListTable[k]->methodNumber;i++)fscanf(file,"%d,",&methodListTable[k]->methodReturnTypeTable[i]);
		fclose(file);
	}
}
void getVirtualMachineInstructions()
{
	getClassDeclarationInstructions();
	classNumber=classList->length;
	classTable=toStrings(classList);
	if(classNumber>0)
	{
		LexicalAnalyser* lexicalAnalyser=this_lexicalAnalyser;
		getMethodListTable();
		this_lexicalAnalyser=lexicalAnalyser;
		initSyntaxTranslator();
		getClassDeclarationInstructions();
		classNumber=classList->length;
		classTable=toStrings(classList);
	}
	while(index+2<wordLength)
	{
		seekNextTwoWord();
		if(!(w0==Word_MRK&&w1==Word_L_PARENTHESE))
		{
			addInteger(declarationsList,instructionsLength);
			while(!(w0==Word_MRK&&w1==Word_L_PARENTHESE))
			{
				nextWord();
				getDeclarationInstructions(w1);
				if(index+2>=wordLength)break;
				seekNextTwoWord();
			}
			addInteger(declarationsList,instructionsLength);			
		}
		else getMethodsInstructions();
	}
	methodNumber=methodList->length;
	methodTable=toStrings(methodList);
	methodPointerTable=toIntegers(methodPointerList);
	methodReturnTypeTable=toIntegers(methodReturnTypeList);
}
void getClassDeclarationInstructions()
{
	nextWord();
	if(w0==Word_KEY&&w1==KeyWord_class)
	{
		nextWord();
		while(w0==Word_ID)
		{
			identifierTable[w1]->type=KeyWord_class;
			identifierTable[w1]->pointer=classList->length;
			addString(classList,identifierTable[w1]->name);
			nextWord();
			if(w0==Word_MRK&&w1==Word_SEMICOLON)return;
			else if(w0==Word_MRK&&w1==Word_COMMA)nextWord();
			else return;
		}
	}
	else index--;
}
void getMethodsInstructions()
{
	nextWord();
	if(w0==Word_KEY&&(w1==KeyWord_public||w1==KeyWord_int||w1==KeyWord_double||w1==KeyWord_String||w1==KeyWord_Vector||w1==KeyWord_void))
	{
		int returnType=w1;
		nextWord();
		if(w0==Word_ID)
		{
			identifierTable[w1]->type=KeyWord_public;
			identifierTable[w1]->pointer=instructionsLength;
			identifierTable[w1]->label=returnType;
			if(returnType==KeyWord_public)beginInstructionPointer=identifierTable[w1]->pointer;
			addString(methodList,identifierTable[w1]->name);
			addInteger(methodPointerList,identifierTable[w1]->pointer);
			addInteger(methodReturnTypeList,identifierTable[w1]->label);
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
			{
				argumentsStack0=newList();
				argumentsStack1=newList();
				getArgumentsInstructions();
				nextWord();
				if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
				{
					getCodeBlockInstructions();
					if(returnType==KeyWord_public)
					{
						getPopInstructions();
						addIntegers(instructionsList,EXIT,END,LastInteger);
						instructionsLength++;
					}
					else if(returnType==KeyWord_void)
					{
						getPopInstructions();
						addIntegers(instructionsList,RET,END,LastInteger);
						instructionsLength++;
					}
				}
			}
		}
	}
}
void getArgumentsInstructions()
{	
	nextWord();
	if(w0==Word_KEY)
	{
		int identifierType=w1;
		nextWord();
		if(w0==Word_ID)
		{
			identifierTable[w1]->type=identifierType;
			index--;
			int* id0=getIdentifierInstructions(&identifierType);
			addIntegers(instructionsList,PUSH,id0[0],id0[1],END,LastInteger);
			instructionsLength++;
			push(argumentsStack0,id0[0]);
			push(argumentsStack1,id0[1]);
			int* id1=getNewIdentifier(identifierType);
			addIntegers(instructionsList,MOV,'%',w1,' ',id1[1],END,LastInteger);
			instructionsLength++;
			addIntegers(instructionsList,DEQ,id1[0],id1[1],END,LastInteger);
			instructionsLength++;
		}
		nextWord();
		if(w0==Word_MRK&&w1==Word_COMMA)getArgumentsInstructions();
		else index--;
	}
	else index--;
}
void getCodeBlockInstructions()
{
	nextWord();
	if(w0==Word_MRK&&w1==Word_R_BRACE)return;
	else if(w0==Word_MRK&&w1==Word_L_BRACE)getCodeBlockInstructions();
	else
	{
		index--;
		getCodeLineInstructions();
		getCodeBlockInstructions();
	}
}
void getCodeLineInstructions()
{
	nextWord();
	int type=w0==Word_ID?identifierTable[w1]->type:0;
	if(w0==Word_KEY&&(w1==KeyWord_int||w1==KeyWord_double||w1==KeyWord_String||w1==KeyWord_Vector||w1==KeyWord_func))getDeclarationInstructions(w1);
	else if(w0==Word_ID&&type==KeyWord_public)getCallMethodInstructions();
	else if(w0==Word_ID&&type==KeyWord_class)getNewObjectInstructions(w1);
	else if(w0==Word_ID&&(type==KeyWord_array||type==KeyWord_int||type==KeyWord_double||type==KeyWord_String||type==KeyWord_Vector||type==KeyWord_func||type==KeyWord_Object))getAssignmentInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_if)getIfElseInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_while)getWhileInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_until)getUntilInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_do)getDoWhileInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_break)getBreakInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_return)getReturnInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_for)getForInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_switch)getSwitchInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_case)getCaseInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_default)getDefaultInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_this)getThisInstructions();
	else if(w0==Word_KEY&&w1==KeyWord_new)getNewClassInstructions(-1);
	else {index--;getThisInstructions();}
}
void getDeclarationInstructions(int identifierType)
{
	if(identifierType==KeyWord_func){getFunctionDeclarationInstructions();return;}				
	nextWord();
	if(w0==Word_ID)
	{
		int id=w1;
		identifierTable[id]->type=identifierType;
		nextWord();
		int* id0=getNewIdentifier(identifierType);
		addIntegers(instructionsList,MOV,'%',id,' ',id0[1],END,LastInteger);
		instructionsLength++;
		if(w0==Word_ARI&&w1==Word_MOV)
		{
			int type;
			int* id1=getAddInstructions(&type);
			nextWord();
			if(w0==Word_LOG)id1=getConditionalAssignmentInstructions(id1);
			else index--;
			addIntegers(instructionsList,MOV,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			if(w0==Word_MRK&&w1==Word_COMMA)getDeclarationInstructions(identifierType);
			else if(w0==Word_MRK&&w1==Word_SEMICOLON)return;
			else return;
		}
		else if(w0==Word_MRK&&w1==Word_COMMA)getDeclarationInstructions(identifierType);
		else if(w0==Word_MRK&&w1==Word_SEMICOLON)return;
		else return;
	}
	else if(w0==Word_MRK&&w1==Word_L_SQRBRACKET)
	{
		nextWord();	
		if(w0==Word_MRK&&w1==Word_R_SQRBRACKET)
		{
			getArrayDeclarationInstructions(identifierType);
		}
	}
}
void getArrayDeclarationInstructions(int identifierType)
{	
	nextWord();
	if(w0==Word_ID)
	{
		int id=w1;
		identifierTable[id]->type=KeyWord_array;
		identifierTable[id]->label=identifierType;
		nextWord();
		if(w0==Word_ARI&&w1==Word_MOV)
		{
			getArrayAssignmentInstructions(id);
			nextWord();
			if(w0==Word_MRK&&w1==Word_COMMA)getArrayDeclarationInstructions(identifierType);
		}
		else if(w0==Word_MRK&&w1==Word_COMMA)getArrayDeclarationInstructions(identifierType);
	}
}
void getArrayAssignmentInstructions(int id)
{
	int i,startPointer=identifierTable[id]->pointer;
	int identifierType=identifierTable[id]->label;
	identifierTable[id]->pointer=integerNumber;
	nextWord();
	if(w0==Word_KEY&&w1==KeyWord_new)
	{
		nextWord();
		if(w0==Word_KEY)
		{
			identifierType=w1;
			identifierTable[id]->label=identifierType;
		}
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_SQRBRACKET)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_R_SQRBRACKET)
			{
				nextWord();
				if(w0==Word_MRK&&w1==Word_L_BRACE)getArrayInitInstructions(id);
			}
			else
			{
				index--;
				int arrayLength=getAddValue();
				addIntegers(instructionsList,MOV,'%',id,' ',integerNumber,END,LastInteger);
				instructionsLength++;
				addIntegers(instructionsList,MOV,'@',integerNumber++,' ',arrayLength,END,LastInteger);
				instructionsLength++;
				int integerNumber0=integerNumber;
				integerNumber+=arrayLength;
				for(i=0;i<arrayLength;i++)
				{
					int* id0=getNewIdentifier(identifierType);
					addIntegers(instructionsList,MOV,'@',integerNumber0+i,' ',id0[1],END,LastInteger);
					instructionsLength++;
				}
				nextWord();
			}
		}
	}
	else if(w0==Word_MRK&&w1==Word_L_BRACE)getArrayInitInstructions(id);
}
void getArrayInitInstructions(int id)
{
	int currentIndex=index;
	int arrayLength=0,i;
	nextWord();
	while(!(w0==Word_MRK&&w1==Word_R_BRACE))
	{
		if(w0==Word_LOG&&w1==Word_LT)
		{
			int counter=1;
			while(counter!=0)
			{
				nextWord();
				if(w0==Word_LOG&&w1==Word_LT)counter++;
				else if(w0==Word_LOG&&w1==Word_GT)counter--;
			}
		}
		nextWord();
		if(w0==Word_MRK&&w1==Word_COMMA)arrayLength++;
	}
	arrayLength++;
	addIntegers(instructionsList,MOV,'%',id,' ',integerNumber,END,LastInteger);
	instructionsLength++;
	addIntegers(instructionsList,MOV,'@',integerNumber++,' ',arrayLength,END,LastInteger);
	instructionsLength++;
	int integerNumber0=integerNumber;
	integerNumber+=arrayLength;
	index=currentIndex;
	for(i=0;i<arrayLength;i++)
	{
		int type,*id0=getAddInstructions(&type);
		if(id0[0]=='@'||id0[0]=='#'||id0[0]=='$'||id0[0]=='\\');
		else
		{
			int* id1=getNewIdentifier(identifierTable[id]->label);
			addIntegers(instructionsList,MOV,id1[0],id1[1],id0[0],id0[1],END,LastInteger);
			instructionsLength++;
			id0[1]=id1[1];
		}
		addIntegers(instructionsList,MOV,'@',integerNumber0+i,' ',id0[1],END,LastInteger);
		instructionsLength++;
		nextWord();
	}
}
void getFunctionDeclarationInstructions()
{
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		int i,*paramPointers=newInt(FUNC_MAX_PARAM_NUM);
		for(i=1;!(w0==Word_MRK&&w1==Word_R_PARENTHESE);i++)
		{	
			nextWord();
			if(w0==Word_ID)
			{	
				int id=w1;
				identifierTable[id]->type=KeyWord_double;
				int* id0=getNewIdentifier(KeyWord_double);
				addIntegers(instructionsList,MOV,'%',id,' ',id0[1],END,LastInteger);
				instructionsLength++;
				paramPointers[i]=id0[1];
			}
			nextWord();
		}
		paramPointers[0]=i-1;
		while(true)
		{
			nextWord();
			if(w0==Word_ID)
			{
				int id=w1;
				identifierTable[id]->type=KeyWord_func;
				identifierTable[id]->pointer=functionList->length;
				Function* function=newFunction(paramPointers);
				addFunction(functionList,function);
				nextWord();
				if(w0==Word_ARI&&w1==Word_MOV)getFunctionAssignmentInstructions(id);
				else index--;
				nextWord();
				if(w0==Word_MRK&&w1==Word_COMMA);
				else break;
			}
		}
	}
}
void getFunctionAssignmentInstructions(int id)
{
	Function* function=getFunction(functionList,identifierTable[id]->pointer);
	function->beginPointer=instructionsLength;
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_SQRBRACKET)
	{
		while(!(w0==Word_MRK&&w1==Word_R_SQRBRACKET))
		{
			nextWord();
			if(w0==Word_ID)
			{	
				int id=w1;
				identifierTable[id]->type=KeyWord_double;
				int* id0=getNewIdentifier(KeyWord_double);
				addIntegers(instructionsList,MOV,'%',id,' ',id0[1],END,LastInteger);
				instructionsLength++;
				nextWord();
				if(w0==Word_ARI&&w1==Word_MOV)
				{
					int type,*id1=getAddInstructions(&type);
					addIntegers(instructionsList,MOV,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
					instructionsLength++;
					nextWord();
				}
			}
		}
	}
	else index--;
	int type,*id1=getAddInstructions(&type);
	if(type==KeyWord_Vector)
	{
		int* id0=getNewIdentifier(KeyWord_Vector);
		addIntegers(instructionsList,MOV,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
		instructionsLength++;
		function->vectorPointer=id0[1];
	}
	else function->doublePointer=id1[1];
	function->endPointer=instructionsLength;
}
void getAssignmentInstructions()
{
	char ic=' ';
	int id=w1;
	int type=identifierTable[id]->type;
	if(type==KeyWord_array)
	{
		type=identifierTable[id]->label;
		ic=type==KeyWord_int?':':type==KeyWord_double?'!':type==KeyWord_String?'?':type==KeyWord_Vector?'<':' ';
		int* id0=getNewIdentifier(KeyWord_int);
		int* id1=newID(' ',identifierTable[id]->pointer+1);
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_SQRBRACKET)
		{
			int* id2=getAddInstructions(&type);
			nextWord();
			if(w0==Word_MRK&&w1==Word_R_SQRBRACKET)
			{
				addIntegers(instructionsList,ADD,id0[0],id0[1],id1[0],id1[1],'+',id2[0],id2[1],END,LastInteger);
				instructionsLength++;
				id=id0[1];
			}
		}
	}
	else if(type==KeyWord_Object)
	{
		int objectIndex=identifierTable[w1]->pointer;
		int classIndex=identifierTable[w1]->label;
		nextWord();
		if(w0==Word_ARI&&w1==Word_MOV)
		{
			nextWord();
			if(w0==Word_KEY&&w1==KeyWord_new)
			{
				getNewClassInstructions(classNumber+objectIndex);
			}
		}
		else if(w0==Word_MRK&&w1==Word_DOT)getInvokeMethodInstructions(classIndex,objectIndex);
		nextWord();
		return;
	}
	else if(type==KeyWord_func)
	{
		nextWord();
		if(w0==Word_ARI&&w1==Word_MOV)getFunctionAssignmentInstructions(id);
		nextWord();
		return;
	}
	else ic=type==KeyWord_int?'&':type==KeyWord_double?'^':type==KeyWord_String?'*':type==KeyWord_Vector?'<':' ';
	nextWord();
	if(w0==Word_ARI&&(w1==Word_INC||w1==Word_DEC))
	{
		if(w1==Word_INC)addIntegers(instructionsList,INC,ic,id,END,LastInteger);
		else addIntegers(instructionsList,DEC,ic,id,END,LastInteger);
		instructionsLength++;
	}
	else if(w0==Word_ARI&&(w1==Word_ACC||w1==Word_DEL))
	{
		int sgn=w1==Word_ACC?'+':'-';
		int* id1=getAddInstructions(&type);
		addIntegers(instructionsList,ACC,ic,id,sgn,id1[0],id1[1],END,LastInteger);
		instructionsLength++;
	}
	else if(w0==Word_ARI&&(w1==Word_PRO||w1==Word_QUO))
	{
		int sgn=w1==Word_PRO?'*':'/';
		int* id1=getAddInstructions(&type);
		addIntegers(instructionsList,PRO,ic,id,sgn,id1[0],id1[1],END,LastInteger);
		instructionsLength++;
	}
	else if(w0==Word_ARI&&w1==Word_MOV)
	{
		int* id1=getAddInstructions(&type);
		nextWord();
		if(w0==Word_LOG)id1=getConditionalAssignmentInstructions(id1);
		else index--;
		addIntegers(instructionsList,MOV,ic,id,id1[0],id1[1],END,LastInteger);
		instructionsLength++;
	}
	nextWord();
}
int* getConditionalAssignmentInstructions(int* id0)
{
	int CMP=EQ;
	switch(w1)
	{
		case Word_EQ:CMP=EQ;break;
		case Word_NE:CMP=NE;break;
		case Word_LE:CMP=LE;break;
		case Word_LT:CMP=LT;break;
		case Word_GE:CMP=GE;break;
		case Word_GT:CMP=GT;break;
	}
	int type;
	int* id1=getAddInstructions(&type);
	int* id2=getNewIdentifier(KeyWord_int);
	addIntegers(instructionsList,CMP,id2[0],id2[1],id0[0],id0[1],id1[0],id1[1],END,LastInteger);
	instructionsLength++;
	nextWord();
	if(w0==Word_LOG&&(w1==Word_AND||w1==Word_OR))
	{
		List* instructionList=newList();
		int* id3=getNewIdentifier(KeyWord_int);
		addIntegers(instructionList,AND,id3[0],id3[1],id2[0],id2[1],LastInteger);
		while(w0==Word_LOG&&(w1==Word_AND||w1==Word_OR))
		{
			char sgn=w1==Word_AND?'*':'+';
			int* id=getLogicInstructions();
			addIntegers(instructionList,sgn,id[0],id[1],LastInteger);
			nextWord();
		}
		index--;
		addInteger(instructionList,END);
		mergeIntegersFromTwoLists(instructionsList,instructionList);
		instructionsLength++;
		id2=id3;
	}
	else index--;
	nextWord();
	if(w0==Word_MRK&&w1==Word_QUESTION)
	{
		int identifierType;
		int* id3=getAddInstructions(&identifierType);
		nextWord();
		if(w0==Word_MRK&&w1==Word_COLON)
		{	
			int* id5=getNewIdentifier(identifierType);
			int* id4=getAddInstructions(&identifierType);
			addIntegers(instructionsList,CMOV,id5[0],id5[1],id2[0],id2[1],id3[0],id3[1],id4[0],id4[1],END,LastInteger);
			instructionsLength++;
			return id5;
		}
	}
	else index--;
	return id2;
}
void getCallMethodInstructions()
{
	int ip=identifierTable[w1]->pointer,i;
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_R_PARENTHESE);
		else
		{
			index--;
			do
			{
				int type,*id=getAddInstructions(&type);
				addIntegers(instructionsList,ENQ,id[0],id[1],END,LastInteger);
				instructionsLength++;
				nextWord();
			}
			while(w0==Word_MRK&&w1==Word_COMMA);
		}
		addIntegers(instructionsList,CALL,ip,END,LastInteger);
		instructionsLength++;
	}
	nextWord();
}
void getNewObjectInstructions(int id)
{
	nextWord();
	if(w0==Word_ID)
	{
		identifierTable[w1]->type=KeyWord_Object;
		int objectIndex=objectNumber++;
		identifierTable[w1]->pointer=objectIndex;
		int classIndex=identifierTable[id]->pointer;
		identifierTable[w1]->label=classIndex;
		addIntegers(instructionsList,NEW,classNumber+objectIndex,classIndex,END,LastInteger);
		instructionsLength++;
		nextWord();
		if(w0==Word_ARI&&w1==Word_MOV)
		{
			nextWord();
			if(w0==Word_KEY&&w1==KeyWord_new)
			{
				getNewClassInstructions(classNumber+objectIndex);
			}
		}
		else index--;
		nextWord();
		if(w0==Word_MRK&&w1==Word_COMMA)getNewObjectInstructions(id);
	}
	else if(w0==Word_MRK&&w1==Word_DOT)
	{
		getInvokeMethodInstructions(id,-1);
		nextWord();
		if(w0==Word_MRK&&w1==Word_COMMA)getNewObjectInstructions(id);
	}
}
void getNewClassInstructions(int classIndex)
{
	nextWord();
	if(w0==Word_ID&&identifierTable[w1]->type==KeyWord_class)
	{
		if(classIndex==-1)classIndex=identifierTable[w1]->pointer;
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_R_PARENTHESE);
			else
			{
				index--;
				do
				{
					int type,*id=getAddInstructions(&type);
					addIntegers(instructionsList,ENQ,id[0],id[1],END,LastInteger);
					instructionsLength++;
					nextWord();
				}
				while(w0==Word_MRK&&w1==Word_COMMA);
			}
			addIntegers(instructionsList,RUN,classIndex,END,LastInteger);
			instructionsLength++;
			if(classIndex==-1)nextWord();
		}
	}
}
int getMethodPointer(int classIndex,String methodName)
{
	MethodList* methodList=methodListTable[classIndex];int i;
	for(i=0;i<methodList->methodNumber;i++)if(equals(methodList->methodTable[i],methodName))break;
	return methodList->methodPointerTable[i];
}
int getMethodReturnType(int classIndex,String methodName)
{
	MethodList* methodList=methodListTable[classIndex];int i;
	for(i=0;i<methodList->methodNumber;i++)if(equals(methodList->methodTable[i],methodName))break;
	return methodList->methodReturnTypeTable[i];
}
void getInvokeMethodInstructions(int classIndex,int objectIndex)
{
	nextWord();
	String methodName=identifierTable[w1]->name;
	int methodPointer=getMethodPointer(classIndex,methodName);
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_R_PARENTHESE);
		else
		{
			index--;
			do
			{
				int type,*id=getAddInstructions(&type);
				addIntegers(instructionsList,ENQ,id[0],id[1],END,LastInteger);
				instructionsLength++;
				nextWord();
			}
			while(w0==Word_MRK&&w1==Word_COMMA);
		}
		if(objectIndex==-1)addIntegers(instructionsList,INVOKE,classIndex,methodPointer,END,LastInteger);
		else addIntegers(instructionsList,INVOKE,classNumber+objectIndex,methodPointer,END,LastInteger);
		instructionsLength++;
	}
}
void getIfElseInstructions()
{
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		int* id=getAndInstructions();
		addIntegers(instructionsList,NOP,END,LastInteger);
		int ip_if=instructionsLength++;
		nextWord();
		if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_BRACE)getCodeBlockInstructions();
			else {index--;getCodeLineInstructions();}	
			nextWord();
			if(w0==Word_KEY&&w1==KeyWord_else)
			{
				addIntegers(instructionsList,NOP,END,LastInteger);
				int ip_else=instructionsLength++;
				nextWord();
				if(w0==Word_KEY&&w1==KeyWord_if)
				{
					int ip_begin=instructionsLength;
					replaceInstruction(instructionsList,ip_if,JZ,id[0],id[1],ip_begin,LastInteger);
					getIfElseInstructions();
					int ip_end=instructionsLength;
					replaceInstruction(instructionsList,ip_else,JMP,ip_end,LastInteger);
				}
				else
				{
					if(w0==Word_MRK&&w1==Word_L_BRACE)getCodeBlockInstructions();
					else {index--;getCodeLineInstructions();}
					int ip_end=instructionsLength;
					replaceInstruction(instructionsList,ip_if,JZ,id[0],id[1],ip_else+1,LastInteger);
					replaceInstruction(instructionsList,ip_else,JMP,ip_end,LastInteger);						
				}
			}
			else
			{
				int ip_end=instructionsLength;
				replaceInstruction(instructionsList,ip_if,JZ,id[0],id[1],ip_end,LastInteger);
				index--;
			}
				
		}
	}
}
void getWhileInstructions()
{
	int ip_while=instructionsLength;
	push(codeblockStack,ip_while);
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		int type;
		int* id=getAndInstructions(&type);
		int ip_begin=instructionsLength;
		addIntegers(instructionsList,NOP,END,LastInteger);
		instructionsLength++;
		nextWord();
		if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_BRACE)getCodeBlockInstructions();
			else {index--;getCodeLineInstructions();}
			addIntegers(instructionsList,JMP,ip_while,END,LastInteger);
			instructionsLength++;
			int ip_end=instructionsLength;
			replaceInstruction(instructionsList,ip_begin,JZ,id[0],id[1],ip_end,LastInteger);
			getCodeblockBreakInstructions(ip_end);
		}
	}
}
void getUntilInstructions()
{
	int ip_until=instructionsLength;
	push(codeblockStack,ip_until);
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		int type;
		int* id=getAndInstructions(&type);
		int ip_begin=instructionsLength;
		addIntegers(instructionsList,NOP,END,LastInteger);
		instructionsLength++;
		nextWord();
		if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_BRACE)getCodeBlockInstructions();
			else {index--;getCodeLineInstructions();}
			addIntegers(instructionsList,JMP,ip_until,END,LastInteger);
			instructionsLength++;
			int ip_end=instructionsLength;
			replaceInstruction(instructionsList,ip_begin,JNZ,id[0],id[1],ip_end,LastInteger);
			getCodeblockBreakInstructions(ip_end);
		}
	}
}
void getDoWhileInstructions()
{
	int ip_while=instructionsLength;
	push(codeblockStack,ip_while);
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_BRACE)getCodeBlockInstructions();
	else {index--;getCodeLineInstructions();}
	nextWord();
	if(w0==Word_KEY&&w1==KeyWord_while)
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int type;
			int* id=getAndInstructions(&type);
			nextWord();
			if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
			{
				addIntegers(instructionsList,JNZ,id[0],id[1],ip_while,END,LastInteger);
				instructionsLength++;
				int ip_end=instructionsLength;
				getCodeblockBreakInstructions(ip_end);
				nextWord();
			}
		}
	}
}
void getForInstructions()
{
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		nextWord();
		if(w0==Word_KEY&&(w1==KeyWord_int||w1==KeyWord_double||w1==KeyWord_String))getDeclarationInstructions(w1);
		else do getAssignmentInstructions();while(w0==Word_MRK&&w1==Word_COMMA);
		int ip_for=instructionsLength;
		push(codeblockStack,ip_for);
		int* id=getAndInstructions();
		int ip_begin=instructionsLength;
		addIntegers(instructionsList,NOP,END,LastInteger);
		instructionsLength++;
		List* oldInstructionsList=instructionsList;
		int oldInstructionsLength=instructionsLength;
		List* newInstructionsList=newList();
		instructionsList=newInstructionsList;
		instructionsLength=0;
		nextWord();
		if(w0==Word_MRK&&w1==Word_SEMICOLON)
		{
			do
			{
				nextWord();
				getAssignmentInstructions();
			}
			while(w0==Word_MRK&&w1==Word_COMMA);
			int newInstructionsLength=instructionsLength;
			instructionsList=oldInstructionsList;
			instructionsLength=oldInstructionsLength;
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_BRACE)getCodeBlockInstructions();
			else {index--;getCodeLineInstructions();}
			mergeIntegersFromTwoLists(instructionsList,newInstructionsList);
			instructionsLength+=newInstructionsLength;
			addIntegers(instructionsList,JMP,ip_for,END,LastInteger);
			instructionsLength++;
			int ip_end=instructionsLength;
			replaceInstruction(instructionsList,ip_begin,JZ,id[0],id[1],ip_end,LastInteger);
			getCodeblockBreakInstructions(ip_end);
		}
		else {instructionsList=oldInstructionsList;instructionsLength=oldInstructionsLength;}
	}
}
void getCaseInstructions()
{
	int type;
	int ip_begin=instructionsLength;
	int* id=getAddInstructions(&type);
	int ip_end=instructionsLength;
	nextWord();
	if(w0==Word_MRK&&w1==Word_COLON)
	{
		addIntegers(switchList,ip_begin,ip_end,id[0],id[1],LastInteger);
	}
}
void getDefaultInstructions()
{
	int ip=instructionsLength;
	nextWord();
	if(w0==Word_MRK&&w1==Word_COLON)
	{
		addIntegers(switchList,END-1,ip,LastInteger);
	}
}
void getSwitchInstructions()
{
	int ip_begin=instructionsLength;
	push(codeblockStack,ip_begin);
	switchList=newList();
	nextWord();
	if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		int type;
		int* id=getAddInstructions(&type);
		addIntegers(instructionsList,NOP,END,LastInteger);
		int ip_switch=instructionsLength++;
		addIntegers(switchList,SWITCH,id[0],id[1],LastInteger);
		nextWord();
		if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_BRACE)
			{

				getCodeBlockInstructions();
				addIntegers(switchList,END-2,instructionsLength,LastInteger);
				replaceInstructionList(instructionsList,ip_switch,switchList);
				int ip_end=instructionsLength;
				getCodeblockBreakInstructions(ip_end);
			}
		}
	}
}
void getBreakInstructions()
{
	int ip_break=instructionsLength;
	push(breakStack,ip_break);
	addIntegers(instructionsList,NOP,END,LastInteger);
	instructionsLength++;
	int ip_begin=top(codeblockStack);
	push(breakStack,ip_begin);
	nextWord();
}
void getCodeblockBreakInstructions(int ip_end)
{
	int ip_begin=pop(codeblockStack);
	if(breakStack->length==0)return;
	int top_break=top(breakStack);
	while(top_break==ip_begin)
	{
		pop(breakStack);
		int ip_break=pop(breakStack);
		replaceInstruction(instructionsList,ip_break,JMP,ip_end,LastInteger);
		if(breakStack->length==0)return;
		top_break=top(breakStack);
	}
}
void getPopInstructions()
{
	if(argumentsStack0->length==0)return;
	Node* n0=argumentsStack0->first,*n1=argumentsStack1->first;int i;
	for(i=0;i<argumentsStack0->length;i++,n0=n0->next,n1=n1->next)
	{
		addIntegers(instructionsList,POP,n0->integer,n1->integer,END,LastInteger);
		instructionsLength++;
	}
}
void getReturnInstructions()
{
	int type;
	nextWord();
	if(w0==Word_MRK&&w1==Word_SEMICOLON);
	else
	{
		index--;
		int* id=getAddInstructions(&type);
		addIntegers(instructionsList,ENQ,id[0],id[1],END,LastInteger);
		instructionsLength++;
		nextWord();
	}
	getPopInstructions();
	addIntegers(instructionsList,RET,END,LastInteger);
	instructionsLength++;
}
void getThisInstructions()
{
	nextWord();
	if(w0==Word_MRK&&w1==Word_DOT);
	else index--;
	nextWord();
	if(w0==Word_ID)
	{
		String thisMethod=identifierTable[w1]->name;
		if(equals(thisMethod,"print"))
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
			{
				int type,*id=getAddInstructions(&type);
				addIntegers(instructionsList,OUT,id[0],id[1],END,LastInteger);
				instructionsLength++;
				nextWord();
				if(w0==Word_MRK&&w1==Word_R_PARENTHESE)nextWord();
			}
		}
		else if(equals(thisMethod,"open"))
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
			{
				int type,*id=getAddInstructions(&type);
				addIntegers(instructionsList,OPEN,id[0],id[1],END,LastInteger);
				instructionsLength++;
				nextWord();
				if(w0==Word_MRK&&w1==Word_R_PARENTHESE)nextWord();
			}
		}
		else if(equals(thisMethod,"write"))
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
			{
				int type,*id=getAddInstructions(&type);
				addIntegers(instructionsList,WRITE,id[0],id[1],END,LastInteger);
				instructionsLength++;
				nextWord();
				if(w0==Word_MRK&&w1==Word_R_PARENTHESE)nextWord();
			}
		}
		else if(equals(thisMethod,"close"))
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
			{
				addIntegers(instructionsList,CLOSE,END,LastInteger);
				instructionsLength++;
				nextWord();
				if(w0==Word_MRK&&w1==Word_R_PARENTHESE)nextWord();
			}
		}
		else if(identifierTable[w1]->type==KeyWord_public)getCallMethodInstructions();
		else if(equals(thisMethod,"setStep"))
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
			{
				int type,*id=getAddInstructions(&type);
				addIntegers(instructionsList,STEP,id[0],id[1],END,LastInteger);
				instructionsLength++;
				nextWord();
				if(w0==Word_MRK&&w1==Word_R_PARENTHESE)nextWord();
			}
		}
		else if(equals(thisMethod,"printf"))
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
			{
				int identifierType,*id0=getAddInstructions(&identifierType);
				nextWord();
				if(w0==Word_MRK&&w1==Word_COMMA)nextWord();
				if(w0==Word_ID&&identifierTable[w1]->type==KeyWord_func)
				{
					int index=identifierTable[w1]->pointer;
					Function* function=getFunction(functionList,index);
					int* paramPointers=function->paramPointers;
					int paramNumber=paramPointers[0],i,*id;
					int beginPointer=function->beginPointer;
					int endPointer=function->endPointer;
					int doublePointer=function->doublePointer;
					int vectorPointer=function->vectorPointer;
					List* instructionList=newList();
					if(function->doublePointer!=-1)id=newID('#',doublePointer);
					else id=newID('\\',vectorPointer);
					addIntegers(instructionList,OUTF,id0[0],id0[1],id[0],id[1],paramNumber,LastInteger);
					for(i=1;i<=paramNumber;i++)addInteger(instructionList,paramPointers[i]);
					nextWord();
					for(i=0;i<paramNumber;i++)
					{
						int *idi=getAddInstructions(&identifierType);
						addIntegers(instructionList,idi[0],idi[1],LastInteger);
						nextWord();
						idi=getAddInstructions(&identifierType);
						addIntegers(instructionList,idi[0],idi[1],LastInteger);
						nextWord();
					}
					addIntegers(instructionList,beginPointer,endPointer,END,LastInteger);
					mergeIntegersFromTwoLists(instructionsList,instructionList);
					instructionsLength++;
				}
				else index--;
				nextWord();
				if(w0==Word_MRK&&w1==Word_R_PARENTHESE)nextWord();
			}
		}
	}
}
int* getAndInstructions()
{
	int* id=getLogicInstructions();
	nextWord();
	if(w0==Word_LOG&&(w1==Word_AND||w1==Word_OR))
	{
		List* instructionList=newList();
		int* id1=getNewIdentifier(KeyWord_int);
		addIntegers(instructionList,AND,id1[0],id1[1],id[0],id[1],LastInteger);
		while(w0==Word_LOG&&(w1==Word_AND||w1==Word_OR))
		{
			char sgn=w1==Word_AND?'*':'+';
			id=getLogicInstructions();
			addIntegers(instructionList,sgn,id[0],id[1],LastInteger);
			nextWord();
		}
		index--;
		addInteger(instructionList,END);
		mergeIntegersFromTwoLists(instructionsList,instructionList);
		instructionsLength++;
		return id1;
	}
	else index--;
	return id;
}
int* getLogicInstructions()
{
	nextWord();
	if(w0==Word_LOG&&w1==Word_NOT)
	{
		int* id=getLogicInstructions();
		int* id1=getNewIdentifier(KeyWord_int);
		addIntegers(instructionsList,NOT,id1[0],id1[1],id[0],id[1],END,LastInteger);
		instructionsLength++;
		return id1;
	}
	else if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		int* id=getAndInstructions();
		nextWord();
		if(w0==Word_MRK&&w1==Word_R_PARENTHESE)return id;
		else {index--;return id;}
	}
	else {index--; return getCmpInstructions();}
}
int* getCmpInstructions()
{
	int type;
	int* id0=getAddInstructions(&type);
	nextWord();
	if(w0==Word_LOG&&(w1==Word_EQ||w1==Word_NE||w1==Word_LE||w1==Word_LT||w1==Word_GE||w1==Word_GT))
	{
		int CMP=EQ;
		switch(w1)
		{
			case Word_EQ:CMP=EQ;break;
			case Word_NE:CMP=NE;break;
			case Word_LE:CMP=LE;break;
			case Word_LT:CMP=LT;break;
			case Word_GE:CMP=GE;break;
			case Word_GT:CMP=GT;break;
		}
		int* id1=getAddInstructions(&type);
		int* id2=getNewIdentifier(KeyWord_int);
		addIntegers(instructionsList,CMP,id2[0],id2[1],id0[0],id0[1],id1[0],id1[1],END,LastInteger);
		instructionsLength++;
		return id2;
	}
	else index--;
	return id0;
}
int* getAddInstructions(int* type)
{
	int identifierType;
	int* id=getMulInstructions(&identifierType);
	*type=identifierType;
	nextWord();
	if(w0==Word_ARI&&(w1==Word_ADD||w1==Word_SUB))
	{
		List* instructionList=newList();
		int* id1=getNewIdentifier(identifierType);
		addIntegers(instructionList,ADD,id1[0],id1[1],id[0],id[1],LastInteger);
		while(w0==Word_ARI&&(w1==Word_ADD||w1==Word_SUB))
		{
			char sgn=w1==Word_ADD?'+':'-';
			id=getMulInstructions(&identifierType);
			addIntegers(instructionList,sgn,id[0],id[1],LastInteger);
			nextWord();
		}
		index--;
		addInteger(instructionList,END);
		mergeIntegersFromTwoLists(instructionsList,instructionList);
		instructionsLength++;
		return id1;
	}
	else index--;
	return id;
}
int* getMulInstructions(int* type)
{
	int identifierType,mul=MUL;
	int* id=getPowInstructions(&identifierType);
	if(identifierType==KeyWord_Vector)mul=VMUL;
	*type=identifierType;
	nextWord();
	if(w0==Word_ARI&&(w1==Word_MUL||w1==Word_DIV||w1==Word_MOD))
	{
		List* instructionList=newList();
		int* id1=getNewIdentifier(identifierType);
		addIntegers(instructionList,id1[0],id1[1],id[0],id[1],LastInteger);
		while(w0==Word_ARI&&(w1==Word_MUL||w1==Word_DIV||w1==Word_MOD))
		{
			char sgn;
			if(w1==Word_MUL)sgn='*';
			else if(w1==Word_DIV)sgn='/';
			else sgn='%';
			id=getPowInstructions(&identifierType);
			if(identifierType==KeyWord_Vector)mul=VMUL;
			addIntegers(instructionList,sgn,id[0],id[1],LastInteger);
			nextWord();
		}
		index--;
		addInteger(instructionList,END);
		addInteger(instructionsList,mul);
		mergeIntegersFromTwoLists(instructionsList,instructionList);
		instructionsLength++;
		return id1;
	}
	else index--;
	return id;
}
int* getPowInstructions(int* type)
{
	int identifierType;
	int* id=getIdentifierInstructions(&identifierType);
	*type=identifierType;
	nextWord();
	if(w0==Word_LOG&&w1==Word_NOT)
	{
		identifierType=KeyWord_int;
		int* id1=getNewIdentifier(identifierType);
		*type=identifierType;
		addIntegers(instructionsList,FACT,id1[0],id1[1],id[0],id[1],END,LastInteger);
		instructionsLength++;
		nextWord();
		id=id1;
	}
	if(w0==Word_ARI&&(w1==Word_POW||w1==Word_RAD))
	{
		List* instructionList=newList();
		int* id1=getNewIdentifier(identifierType);
		addIntegers(instructionList,POW,id1[0],id1[1],id[0],id[1],LastInteger);
		while(w0==Word_ARI&&(w1==Word_POW||w1==Word_RAD))
		{
			char sgn=w1==Word_POW?'^':'\\';
			id=getIdentifierInstructions(&identifierType);
			addIntegers(instructionList,sgn,id[0],id[1],LastInteger);
			nextWord();
		}
		index--;
		addInteger(instructionList,END);
		mergeIntegersFromTwoLists(instructionsList,instructionList);
		instructionsLength++;
		return id1;
	}
	else index--;
	return id;
}
int* getIdentifierInstructions(int* type)
{
	nextWord();
	*type=w0;
	if(w0==Word_ID)
	{
		int identifierType=identifierTable[w1]->type;
		*type=identifierType;
		if(identifierType==KeyWord_public)
		{
			identifierType=identifierTable[w1]->label;
			*type=identifierType;
			int* id=getNewIdentifier(identifierType);
			getCallMethodInstructions();
			index--;
			addIntegers(instructionsList,DEQ,id[0],id[1],END,LastInteger);
			instructionsLength++;
			return id;
		}
		else if(identifierType==KeyWord_array)
		{
			int id=w1;
			identifierType=identifierTable[w1]->label;
			*type=identifierType;
			char ic=*type==KeyWord_int?':':*type==KeyWord_double?'!':*type==KeyWord_String?'?':*type==KeyWord_Vector?'>':' ';
			int* id0=getNewIdentifier(KeyWord_int);
			int* id1=newID(' ',identifierTable[w1]->pointer+1);
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_SQRBRACKET)
			{
				int* id2=getAddInstructions(&identifierType);
				nextWord();
				if(w0==Word_MRK&&w1==Word_R_SQRBRACKET)
				{
					addIntegers(instructionsList,ADD,id0[0],id0[1],id1[0],id1[1],'+',id2[0],id2[1],END,LastInteger);
					instructionsLength++;
					return newID(ic,id0[1]);
				}
			}
			else if(w0==Word_MRK&&w1==Word_DOT)
			{
				nextWord();
				if(w0==Word_ID)
				{
					*type=KeyWord_int;
					String name=identifierTable[w1]->name;
					if(equals(name,"length"))return newID('&',id);
				}
			}
			else
			{
				index--;
				return newID(ic,id);
			}
		}
		else if(identifierType==KeyWord_class||identifierType==KeyWord_Object)
		{
			int classIndex,objectIndex;
			if(identifierType==KeyWord_class)
			{
				classIndex=identifierTable[w1]->pointer;
				objectIndex=-1;
			}
			else
			{
				classIndex=identifierTable[w1]->label;
				objectIndex=identifierTable[w1]->pointer;
			}
			nextWord();
			if(w0==Word_MRK&&w1==Word_DOT)
			{
				nextWord();index--;
				String methodName=identifierTable[w1]->name;
				int methodReturnType=getMethodReturnType(classIndex,methodName);
				*type=methodReturnType;
				int* id=getNewIdentifier(methodReturnType);
				getInvokeMethodInstructions(classIndex,objectIndex);
				addIntegers(instructionsList,DEQ,id[0],id[1],END,LastInteger);
				instructionsLength++;
				return id;
			}
		}
		else if(identifierType==KeyWord_func)
		{
			int index=identifierTable[w1]->pointer;
			Function* function=getFunction(functionList,index);
			int* paramPointers=function->paramPointers;
			int paramNumber=paramPointers[0],i,*id;
			int beginPointer=function->beginPointer;
			int endPointer=function->endPointer;
			int doublePointer=function->doublePointer;
			int vectorPointer=function->vectorPointer;
			List* instructionList=newList();
			addIntegers(instructionList,FUNC,paramNumber,LastInteger);
			for(i=1;i<=paramNumber;i++)addInteger(instructionList,paramPointers[i]);
			nextWord();
			if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
			{
				for(i=0;i<paramNumber;i++)
				{
					int identifierType,*idi=getAddInstructions(&identifierType);
					nextWord();
					addIntegers(instructionList,idi[0],idi[1],LastInteger);
				}
			}
			addIntegers(instructionList,beginPointer,endPointer,END,LastInteger);
			mergeIntegersFromTwoLists(instructionsList,instructionList);
			instructionsLength++;
			if(function->doublePointer!=-1)
			{
				id=getNewIdentifier(KeyWord_double);
				addIntegers(instructionsList,MOV,id[0],id[1],'#',doublePointer,END,LastInteger);
				instructionsLength++;
				*type=KeyWord_double;
			}
			else
			{
				id=getNewIdentifier(KeyWord_Vector);
				addIntegers(instructionsList,MOV,id[0],id[1],'\\',vectorPointer,END,LastInteger);
				instructionsLength++;
				*type=KeyWord_Vector;
			}
			return id;
		}
		else if(identifierType==KeyWord_int)return newID('&',w1);
		else if(identifierType==KeyWord_double)return newID('^',w1);
		else if(identifierType==KeyWord_String)return newID('*',w1);
		else if(identifierType==KeyWord_Vector)return newID('<',w1);
		else return getThisIdentifierInstructions(type);
	}
	else if(w0==Word_INT)return newID('@',w1);
	else if(w0==Word_DOU)return newID('#',w1);
	else if(w0==Word_STR)return newID('$',w1);
	else if(w0==Word_ARI&&w1==Word_SUB)
	{
		int identifierType;
		int* id=getIdentifierInstructions(&identifierType);
		*type=identifierType;
		int* id1=getNewIdentifier(identifierType);
		addIntegers(instructionsList,NEG,id1[0],id1[1],id[0],id[1],END,LastInteger);
		instructionsLength++;
		return id1;
	}
	else if(w0==Word_ARI&&w1==Word_SQRT)
	{
		int identifierType;
		int* id=getIdentifierInstructions(&identifierType);
		identifierType=KeyWord_double;
		*type=identifierType;
		int* id1=getNewIdentifier(identifierType);
		addIntegers(instructionsList,SQRT,id1[0],id1[1],id[0],id[1],END,LastInteger);
		instructionsLength++;
		return id1;
	}
	else if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		int identifierType;
		int* id=getAddInstructions(&identifierType);
		*type=identifierType;
		nextWord();
		if(w0==Word_MRK&&w1==Word_R_PARENTHESE)return id;
		else {index--;return id;}
	}
	else if(w0==Word_ARI&&w1==Word_OR)
	{
		int identifierType;
		int* id=getAddInstructions(&identifierType);
		if(identifierType==Word_STR||identifierType==KeyWord_String)identifierType=KeyWord_int;
		else if(identifierType==KeyWord_Vector)identifierType=KeyWord_double;
		*type=identifierType;
		int* id1=getNewIdentifier(identifierType);
		addIntegers(instructionsList,ABS,id1[0],id1[1],id[0],id[1],END,LastInteger);
		instructionsLength++;
		nextWord();
		if(w0==Word_ARI&&w1==Word_OR)return id1;
		else {index--;return id1;}
	}
	else if(w0==Word_LOG&&w1==Word_LT)
	{
		int identifierType=KeyWord_double,i;
		int* id=getNewIdentifier(identifierType);
		doubleNumber+=2;
		for(i=0;i<3;i++)
		{
			int* idi=getAddInstructions(&identifierType);
			addIntegers(instructionsList,MOV,id[0],id[1]+i,idi[0],idi[1],END,LastInteger);
			instructionsLength++;
			nextWord();
		}
		*type=KeyWord_Vector;
		id[0]='~';
		return id;
	}
	else if(w0==Word_KEY&&w1==KeyWord_this)
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_DOT)
		{
			nextWord();
			return getThisIdentifierInstructions();
		}
	}
	else return newID('@',-1);
}
int* getThisIdentifierInstructions(int* type)
{
	String thisMethod=identifierTable[w1]->name;
	if(equals(thisMethod,"nextInt")||equals(thisMethod,"nextDouble")||equals(thisMethod,"next")||equals(thisMethod,"nextLine")||equals(thisMethod,"nextVector"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
			{
				int* id;
				if(equals(thisMethod,"nextInt"))
				{
					*type=KeyWord_int;
					id=getNewIdentifier(KeyWord_int);
				}
				else if(equals(thisMethod,"nextDouble"))
				{
					*type=KeyWord_double;
					id=getNewIdentifier(KeyWord_double);
				}
				else if(equals(thisMethod,"nextVector"))
				{
					*type=KeyWord_Vector;
					id=getNewIdentifier(KeyWord_Vector);
				}
				else
				{
					*type=KeyWord_String;
					id=getNewIdentifier(KeyWord_String);
				}
				addIntegers(instructionsList,IN,id[0],id[1],END,LastInteger);
				instructionsLength++;
				return id;
			}
		}
	}
	else if(equals(thisMethod,"readInt")||equals(thisMethod,"readDouble")||equals(thisMethod,"read")||equals(thisMethod,"readLine"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
			{
				int* id;
				if(equals(thisMethod,"readInt"))
				{
					*type=KeyWord_int;
					id=getNewIdentifier(KeyWord_int);
				}
				else if(equals(thisMethod,"readDouble"))
				{
					*type=KeyWord_double;
					id=getNewIdentifier(KeyWord_double);
				}
				else
				{
					*type=KeyWord_String;
					id=getNewIdentifier(KeyWord_String);
				}
				addIntegers(instructionsList,READ,id[0],id[1],END,LastInteger);
				instructionsLength++;
				return id;
			}
		}
	}
	else if(equals(thisMethod,"PI"))
	{
		*type=KeyWord_double;
		int* id=getNewIdentifier(KeyWord_double);
		addIntegers(instructionsList,PI,id[0],id[1],END,LastInteger);
		instructionsLength++;
		return id;
	}
	else if(equals(thisMethod,"sin"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,SIN,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"cos"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,COS,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"tan"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,TAN,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"cot"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,COT,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"sec"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,SEC,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"csc"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,CSC,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"asin"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ASIN,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"acos"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ACOS,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"atan"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ATAN,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"acot"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ACOT,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"asec"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ASEC,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"acsc"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ACSC,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"exp"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,EXP,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"log"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			nextWord();
			if(w0==Word_MRK&&w1==Word_COMMA)
			{
				int *id2=getAddInstructions(type);
				addIntegers(instructionsList,LOG,id0[0],id0[1],id1[0],id1[1],id2[0],id2[1],END,LastInteger);
			}
			else
			{
				index--;
				addIntegers(instructionsList,LN,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			}
			instructionsLength++;
			*type=KeyWord_double;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"lg"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,LG,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"ln"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,LN,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"random"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			nextWord();
			if(w0==Word_MRK&&w1==Word_R_PARENTHESE)
			{
				int* id=getNewIdentifier(KeyWord_double);
				*type=KeyWord_double;
				addIntegers(instructionsList,RAND,id[0],id[1],END,LastInteger);
				instructionsLength++;
				return id;
			}
		}
	}
	else if(equals(thisMethod,"P"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_int);
			int *id1=getAddInstructions(type);
			nextWord();
			if(w0==Word_MRK&&w1==Word_COMMA)
			{
				int *id2=getAddInstructions(type);
				addIntegers(instructionsList,PERM,id0[0],id0[1],id1[0],id1[1],id2[0],id2[1],END,LastInteger);
				instructionsLength++;
			}
			*type=KeyWord_int;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"C"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_int);
			int *id1=getAddInstructions(type);
			nextWord();
			if(w0==Word_MRK&&w1==Word_COMMA)
			{
				int *id2=getAddInstructions(type);
				addIntegers(instructionsList,COMB,id0[0],id0[1],id1[0],id1[1],id2[0],id2[1],END,LastInteger);
				instructionsLength++;
			}
			*type=KeyWord_int;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"square"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			addIntegers(instructionsList,SQU,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			*type=KeyWord_double;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"sawtooth"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int* id1=getAddInstructions(type);
			nextWord();
			if(w0==Word_MRK&&w1==Word_COMMA)
			{
				int* id2=getAddInstructions(type);
				addIntegers(instructionsList,SAW,id0[0],id0[1],id1[0],id1[1],id2[0],id2[1],END,LastInteger);
				instructionsLength++;
			}
			*type=KeyWord_double;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"sh"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,SH,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"ch"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,CH,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"th"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,TH,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"arsh"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ARSH,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"arch"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ARCH,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"arth"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			*type=KeyWord_double;
			addIntegers(instructionsList,ARTH,id0[0],id0[1],id1[0],id1[1],END,LastInteger);
			instructionsLength++;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"gauss"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			nextWord();
			if(w0==Word_MRK&&w1==Word_COMMA)
			{
				int *id2=getAddInstructions(type);
				nextWord();
				if(w0==Word_MRK&&w1==Word_COMMA)
				{
					int *id3=getAddInstructions(type);
					addIntegers(instructionsList,GAU,id0[0],id0[1],id1[0],id1[1],id2[0],id2[1],id3[0],id3[1],END,LastInteger);
					instructionsLength++;
				}
			}
			*type=KeyWord_double;
			nextWord();
			return id0;
		}
	}
	else if(equals(thisMethod,"taylor"))
	{
		nextWord();
		if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
		{
			int* id0=getNewIdentifier(KeyWord_double);
			int *id1=getAddInstructions(type);
			nextWord();
			if(w0==Word_MRK&&w1==Word_COMMA)
			{
				int *id2=getAddInstructions(type);
				nextWord();
				if(w0==Word_MRK&&w1==Word_COMMA)
				{
					int *id3=getIdentifierInstructions(type);
					addIntegers(instructionsList,TAY,id0[0],id0[1],id1[0],id1[1],id2[0],id2[1],id3[0],id3[1],END,LastInteger);
					instructionsLength++;
				}
			}
			*type=KeyWord_double;
			nextWord();
			return id0;
		}
	}
}
int* getNewIdentifier(int type)
{
	if(type==Word_INT||type==KeyWord_int)return newID('@',integerNumber++);
	else if(type==Word_DOU||type==KeyWord_double)return newID('#',doubleNumber++);
	else if(type==Word_STR||type==KeyWord_String)return newID('$',stringNumber++);
	else if(type==KeyWord_Vector)return newID('\\',vectorNumber++);
	else return newID('@',-1);
}
int* newID(char c,int id)
{
	int* ID=newInt(2);
	ID[0]=c;
	ID[1]=id;
	return ID;
}
int getAddValue()
{
	int value=getMulValue();
	nextWord();
	if(w0==Word_ARI&&(w1==Word_ADD||w1==Word_SUB))
	{
		while(w0==Word_ARI&&(w1==Word_ADD||w1==Word_SUB))
		{
			int b=w1==Word_ADD;
			int value1=getMulValue();
			if(b)value+=value1;
			else value-=value1;
			nextWord();
		}
		index--;
		return value;
	}
	else index--;
	return value;
}
int getMulValue()
{
	int value=getIntValue();
	nextWord();
	if(w0==Word_ARI&&(w1==Word_MUL||w1==Word_DIV))
	{
		while(w0==Word_ARI&&(w1==Word_MUL||w1==Word_DIV))
		{
			int b=w1==Word_MUL;
			int value1=getIntValue();
			if(b)value*=value1;
			else value/=value1;
			nextWord();
		}
		index--;
		return value;
	}
	else index--;
	return value;
}
int getIntValue()
{
	nextWord();
	if(w0==Word_INT)return integerTable[w1];
	else if(w0==Word_ARI&&w1==Word_SUB)return -getIntValue();
	else if(w0==Word_MRK&&w1==Word_L_PARENTHESE)
	{
		int value=getAddValue();
		nextWord();
		return value;
	}
	else return 0;
}
void printVirtualMachineInstructions(SyntaxTranslator* syntaxTranslator)
{
	String fileName=add(syntaxTranslator->className,".asmb");
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return;int i;
	fprintf(file,"methodNumber=%d\n",syntaxTranslator->methodNumber);
	for(i=0;i<syntaxTranslator->methodNumber;i++)fprintf(file,"methodTable[%d]=%s\n",i,syntaxTranslator->methodTable[i]);
	for(i=0;i<syntaxTranslator->methodNumber;i++)fprintf(file,"methodPointerTable[%d]=%d\n",i,syntaxTranslator->methodPointerTable[i]);
	for(i=0;i<syntaxTranslator->methodNumber;i++)fprintf(file,"methodReturnTypeTable[%d]=%d\n",i,syntaxTranslator->methodReturnTypeTable[i]);
	fprintf(file,"objectNumber=%d\n",syntaxTranslator->objectNumber);
	fprintf(file,"classNumber=%d\n",syntaxTranslator->classNumber);
	for(i=0;i<syntaxTranslator->classNumber;i++)fprintf(file,"classTable[%d]=%s\n",i,syntaxTranslator->classTable[i]);
	fprintf(file,"identifierNumber=%d\n",syntaxTranslator->identifierNumber);
	fprintf(file,"integerNumber=%d\n",syntaxTranslator->integerNumber);
	fprintf(file,"integerTableLength=%d\n",syntaxTranslator->integerTableLength);
	for(i=0;i<syntaxTranslator->integerTableLength;i++)fprintf(file,"integerTable[%d]=%d\n",i,syntaxTranslator->integerTable[i]);
	fprintf(file,"doubleNumber=%d\n",syntaxTranslator->doubleNumber);
	fprintf(file,"doubleTableLength=%d\n",syntaxTranslator->doubleTableLength);
	for(i=0;i<syntaxTranslator->doubleTableLength;i++)fprintf(file,"doubleTable[%d]=%f\n",i,syntaxTranslator->doubleTable[i]);
	fprintf(file,"stringNumber=%d\n",syntaxTranslator->stringNumber);
	fprintf(file,"stringTableLength=%d\n",syntaxTranslator->stringTableLength);
	for(i=0;i<syntaxTranslator->stringTableLength;i++)fprintf(file,"stringTable[%d]=%s\n",i,syntaxTranslator->stringTable[i]);
	fprintf(file,"vectorNumber=%d\n",syntaxTranslator->vectorNumber);
	fprintf(file,"declarationsLength=%d\n",syntaxTranslator->declarationsLength);
	for(i=0;i<syntaxTranslator->declarationsLength;i++)fprintf(file,"declarations[%d]=%d\n",i,syntaxTranslator->declarations[i]);
	fprintf(file,"beginInstructionPointer=%d\n",syntaxTranslator->beginInstructionPointer);
	fprintf(file,"instructionsLength=%d\n",syntaxTranslator->instructionsLength);
	int** instructions=toInstructions(syntaxTranslator->instructionsList,syntaxTranslator->instructionsLength);
	printInstructions(file,instructions,syntaxTranslator->instructionsLength);
	fclose(file);
}
void generateVirtualMachineInstructions(SyntaxTranslator* syntaxTranslator)
{
	String fileName=add(syntaxTranslator->className,".classb");
	FILE* file=fopen(fileName,"w");
	if(file==NULL){printf("ERROR: Can not write File: %s ...\n",fileName);return;}int i;
	fprintf(file,"%d,",syntaxTranslator->methodNumber);
	for(i=0;i<syntaxTranslator->methodNumber;i++)fprintf(file,"%s,",syntaxTranslator->methodTable[i]);
	for(i=0;i<syntaxTranslator->methodNumber;i++)fprintf(file,"%d,",syntaxTranslator->methodPointerTable[i]);
	for(i=0;i<syntaxTranslator->methodNumber;i++)fprintf(file,"%d,",syntaxTranslator->methodReturnTypeTable[i]);
	fprintf(file,"%d,",syntaxTranslator->objectNumber);
	fprintf(file,"%d,",syntaxTranslator->classNumber);
	for(i=0;i<syntaxTranslator->classNumber;i++)fprintf(file,"%s,",syntaxTranslator->classTable[i]);
	fprintf(file,"%d,",syntaxTranslator->identifierNumber);
	fprintf(file,"%d,",syntaxTranslator->integerNumber);
	fprintf(file,"%d,",syntaxTranslator->integerTableLength);
	for(i=0;i<syntaxTranslator->integerTableLength;i++)fprintf(file,"%d,",syntaxTranslator->integerTable[i]);
	fprintf(file,"%d,",syntaxTranslator->doubleNumber);
	fprintf(file,"%d,",syntaxTranslator->doubleTableLength);
	for(i=0;i<syntaxTranslator->doubleTableLength;i++)fprintf(file,"%f,",syntaxTranslator->doubleTable[i]);
	fprintf(file,"%d,",syntaxTranslator->stringNumber);
	fprintf(file,"%d,",syntaxTranslator->stringTableLength);
	for(i=0;i<syntaxTranslator->stringTableLength;i++)fprintf(file,"%s,",syntaxTranslator->stringTable[i]);
	fprintf(file,"%d,",syntaxTranslator->vectorNumber);
	fprintf(file,"%d,",syntaxTranslator->declarationsLength);
	for(i=0;i<syntaxTranslator->declarationsLength;i++)fprintf(file,"%d,",syntaxTranslator->declarations[i]);
	fprintf(file,"%d,",syntaxTranslator->beginInstructionPointer);
	fprintf(file,"%d,",syntaxTranslator->instructionsLength);
	fprintf(file,"%d,",syntaxTranslator->instructionsList->length);
	writeIntegers(file,instructionsList);
	fclose(file);
}
