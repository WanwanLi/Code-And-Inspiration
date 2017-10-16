int index;
String code;
int codeLength;
List* integerList;
List* stringList;
List* doubleList;
IdentifierList* identifierList;
#define LexicalAnalyser struct LexicalAnalyser
LexicalAnalyser
{
	String className;
	int* wordTable;
	int* integerTable;
	String* stringTable;
	double* doubleTable;
	Identifier** identifierTable;
	int wordTableLength;
	int integerTableLength;
	int stringTableLength;
	int doubleTableLength;
	int identifierTableLength;
};
void getCode(String className);
void createWordTable(LexicalAnalyser* lexicalAnalyser);
void createIntegerTable(LexicalAnalyser* lexicalAnalyser);
void createStringTable(LexicalAnalyser* lexicalAnalyser);
void createDoubleTable(LexicalAnalyser* lexicalAnalyser);
void createIdentifierTable(LexicalAnalyser* lexicalAnalyser);
String getLogicString(int operationType);
String getArithmeticString(int operationType);
String getMarkString(int operationType);
Word* scanNextWord(LexicalAnalyser* lexicalAnalyser);
void printWord(LexicalAnalyser* lexicalAnalyser,int w0,int w1);
LexicalAnalyser* newLexicalAnalyser(String className)
{
	index=0;
	integerList=newList();
	stringList=newList();
	doubleList=newList();
	identifierList=newIdentifierList();
	getCode(className);
	LexicalAnalyser* lexicalAnalyser=(LexicalAnalyser*)malloc(sizeof(LexicalAnalyser));
	lexicalAnalyser->className=className;
	createWordTable(lexicalAnalyser);
	createIntegerTable(lexicalAnalyser);
	createStringTable(lexicalAnalyser);
	createDoubleTable(lexicalAnalyser);
	createIdentifierTable(lexicalAnalyser);
	return lexicalAnalyser;
}
void getCode(String className)
{
	String fileName=add(className,".javab");
	FILE* file=fopen(fileName,"r");
	if(file==null)
	{
		code="";
		codeLength=0;
		return;
	}
	List* charList=newList();
	char c='\0';
	while(!feof(file))
	{
		fscanf(file,"%c",&c);
		addInteger(charList,(int)c);
	}
	fclose(file);
	codeLength=charList->length;
	code=toString(charList);
}
void createWordTable(LexicalAnalyser* lexicalAnalyser)
{
	WordList* wordList=newWordList();
	index=0;
	while(index<codeLength)
	{
		Word* word=scanNextWord(lexicalAnalyser);
		addWord(wordList,word);
	}
	lexicalAnalyser->wordTableLength=wordList->length*2;
	lexicalAnalyser->wordTable=toWordTable(wordList);
}
void createIntegerTable(LexicalAnalyser* lexicalAnalyser)
{
	lexicalAnalyser->integerTableLength=integerList->length;
	lexicalAnalyser->integerTable=toIntegers(integerList);
}
void createStringTable(LexicalAnalyser* lexicalAnalyser)
{
	lexicalAnalyser->stringTableLength=stringList->length;
	lexicalAnalyser->stringTable=toStrings(stringList);
}
void createDoubleTable(LexicalAnalyser* lexicalAnalyser)
{
	lexicalAnalyser->doubleTableLength=doubleList->length;
	lexicalAnalyser->doubleTable=toDoubles(doubleList);
}
void createIdentifierTable(LexicalAnalyser* lexicalAnalyser)
{
	lexicalAnalyser->identifierTableLength=identifierList->length;
	lexicalAnalyser->identifierTable=toIdentifierTable(identifierList);
}
void printWord(LexicalAnalyser* lexicalAnalyser,int w0,int w1)
{
	switch(w0)
	{
		case Word_ID:printf("%s\n",lexicalAnalyser->identifierTable[w1]->name);break;
		case Word_KEY:printf("%s\n",KeyWordTable[w1]);break;
		case Word_INT:printf("%d\n",lexicalAnalyser->integerTable[w1]);break;
		case Word_STR:printf("\"%s\"\n",lexicalAnalyser->stringTable[w1]);break;
		case Word_DOU:printf("%f\n",lexicalAnalyser->doubleTable[w1]);break;
		case Word_LOG:printf("%s\n",getLogicString(w1));break;
		case Word_ARI:printf("%s\n",getArithmeticString(w1));break;
		case Word_MRK:printf("%s\n",getMarkString(w1));break;
	}
}
void printWordTable(LexicalAnalyser* lexicalAnalyser)
{
	int i,length=lexicalAnalyser->wordTableLength;
	int* wordTable=lexicalAnalyser->wordTable;
	for(i=0;i<length/2;i++)
	{
		int w0=wordTable[i*2+0];
		int w1=wordTable[i*2+1];
		printWord(lexicalAnalyser,w0,w1);
	}
}
String getLogicString(int operationType)
{
	switch(operationType)
	{
		case Word_EQ:return "==";
		case Word_NE:return "!=";
		case Word_LE:return "<=";
		case Word_LT:return "<";
		case Word_GE:return ">=";
		case Word_GT:return ">";
		case Word_AND:return "&&";
		case Word_OR:return "||";
		case Word_NOT:return "!";
	}
	return "null";
}
String getArithmeticString(int operationType)
{	
	switch(operationType)
	{
		case Word_MOV:return "=";
		case Word_INC:return "++";
		case Word_ADD:return "+";
		case Word_DEC:return "--";
		case Word_SUB:return "-";
		case Word_MUL:return "*";
		case Word_DIV:return "/";
		case Word_AND:return "&";
		case Word_OR:return "|";
		case Word_ACC:return "+=";
		case Word_DEL:return "-=";
		case Word_MOD:return "%";
		case Word_PRO:return "*=";
		case Word_QUO:return "/=";
	}
	return "null";
}
String getMarkString(int operationType)
{	
	switch(operationType)
	{	
		case Word_L_PARENTHESE:return "(";
		case Word_R_PARENTHESE:return ")";
		case Word_L_SQRBRACKET:return "[";
		case Word_R_SQRBRACKET:return "]";
		case Word_L_BRACE:return "{";
		case Word_R_BRACE:return "}";
		case Word_DOT:return ".";
		case Word_COMMA:return ",";
		case Word_SEMICOLON:return ";";
		case Word_QUESTION:return "?";
		case Word_COLON:return ":";
	}
	return "null";
}
Word* scanNextWord(LexicalAnalyser* lexicalAnalyser)
{
	String s="";
	int length=codeLength;
	char c=code[index++];
	while(c==' '||c=='\t'||c=='\n'||c=='\r')c=code[index++];
	if(isAlpha(c))
	{
		while(isAlpha(c)||isDigit(c)||c=='_')
		{
			s=append(s,c);
			c=code[index++];
			if(index>=length)break;
		}
		index--;
		int n=getKeyWordIndex(s);
		if(n==-1){n=getIdentifierIndex(identifierList,s);return newWord(Word_ID,n);}
		else return newWord(Word_KEY,n);
	}
	else if(isDigit(c))
	{
		boolean isDouble=false,isPercent=false,isPI=false;
		while(isDigit(c)||c=='.'||(isDouble&&(c=='%'||c=='P'||c=='E')))
		{
			if(c=='%'){isPercent=true;index++;break;}
			else if(c=='P'){c=code[index++];if(c=='I')isPI=true;index++;break;}
			else if(c=='E')
			{
				boolean isNegative=false;
				double b=parseDouble(s);
				c=code[index++];
				if(c=='-')isNegative=true;
				else index--;
				s="";
				c=code[index++];
				while(isDigit(c))
				{
					s=append(s,c);
					c=code[index++];
					if(index>=length)break;
				}
				index--;
				int e=isNegative?-parseInt(s):parseInt(s);
				addDouble(doubleList,b*pow(10,e));
				return newWord(Word_DOU,doubleList->length-1);
			}
			s=append(s,c);
			if(c=='.')isDouble=true;
			c=code[index++];
			if(index>=length)break;
		}
		index--;
		if(isDouble)
		{
			if(isPercent)addDouble(doubleList,parseDouble(s)/100.0);
			else if(isPI)addDouble(doubleList,parseDouble(s)*3.1415926);
			else addDouble(doubleList,parseDouble(s));
			return newWord(Word_DOU,doubleList->length-1);
		}
		else 
		{
			addInteger(integerList,parseInt(s));
			return newWord(Word_INT,integerList->length-1);
		}
	}
	else
	{
		switch(c)
		{
			case '=':
			c=code[index++];
			if(c=='=')return newWord(Word_LOG,Word_EQ);
			else{index--;return newWord(Word_ARI,Word_MOV);}
			case '!':
			c=code[index++];
			if(c=='=')return newWord(Word_LOG,Word_NE);
			else{index--;return newWord(Word_LOG,Word_NOT);}
			case '<':
			c=code[index++];
			if(c=='=')return newWord(Word_LOG,Word_LE);
			else{index--;return newWord(Word_LOG,Word_LT);}
			case '>':
			c=code[index++];
			if(c=='=')return newWord(Word_LOG,Word_GE);
			else{index--;return newWord(Word_LOG,Word_GT);}
			case '+':
			c=code[index++];
			if(c=='+')return newWord(Word_ARI,Word_INC);
			else if(c=='=')return newWord(Word_ARI,Word_ACC);
			else{index--;return newWord(Word_ARI,Word_ADD);}
			case '-':
			c=code[index++];
			if(c=='-')return newWord(Word_ARI,Word_DEC);
			else if(c=='=')return newWord(Word_ARI,Word_DEL);
			else{index--;return newWord(Word_ARI,Word_SUB);}
			case '*':
			c=code[index++];
			if(c=='=')return newWord(Word_ARI,Word_PRO);
			else{index--;return newWord(Word_ARI,Word_MUL);}
			case '/':
			if(index>=length)break;
			c=code[index++];
			while(c=='*')
			{
				if(index>=length)break;
				c=code[index++];
				if(c=='/')break;
				while(c!='*'&&index<length)c=code[index++];
			}
			if(c=='=')return newWord(Word_ARI,Word_QUO);
			else{index--;return newWord(Word_ARI,Word_DIV);}
			case '&':
			c=code[index++];
			if(c=='&')return newWord(Word_LOG,Word_AND);
			else{index--;return newWord(Word_ARI,Word_AND);}
			case '|':
			c=code[index++];
			if(c=='|')return newWord(Word_LOG,Word_OR);
			else{index--;return newWord(Word_ARI,Word_OR);}
			case '%':return newWord(Word_ARI,Word_MOD);
			case '^':return newWord(Word_ARI,Word_POW);
			case '\\':return newWord(Word_ARI,Word_RAD);
			case '(':return newWord(Word_MRK,Word_L_PARENTHESE);
			case ')':return newWord(Word_MRK,Word_R_PARENTHESE);
			case '[':return newWord(Word_MRK,Word_L_SQRBRACKET);
			case ']':return newWord(Word_MRK,Word_R_SQRBRACKET);
			case '{':return newWord(Word_MRK,Word_L_BRACE);
			case '}':return newWord(Word_MRK,Word_R_BRACE);
			case '\"':
			c=code[index++];
			while(c!='\"')
			{
				if(c==',')c='.';
				s=append(s,c);
				c=code[index++];
				if(index>=length)break;
			}
			addString(stringList,s);
			return newWord(Word_STR,stringList->length-1);
			case '.':
			c=code[index++];
			if(c=='/')return newWord(Word_ARI,Word_SQRT);
			else{index--;return newWord(Word_MRK,Word_DOT);}
			case ',':return newWord(Word_MRK,Word_COMMA);
			case ';':return newWord(Word_MRK,Word_SEMICOLON);
			case '?':return newWord(Word_MRK,Word_QUESTION);
			case ':':return newWord(Word_MRK,Word_COLON);
		}
		return newWord(-1,-1);
	}
}
