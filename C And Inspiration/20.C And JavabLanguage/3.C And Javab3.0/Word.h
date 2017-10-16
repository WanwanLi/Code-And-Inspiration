#define Word_NULL 100
#define Word_ID 101
#define Word_KEY 102
#define Word_INT 103
#define Word_LOG 104
#define Word_ARI 105
#define Word_MRK 106
#define Word_CHR 107
#define Word_STR 108
#define Word_DOU 109
#define Word_EQ 110
#define Word_NE 111
#define Word_LE 112
#define Word_LT 113
#define Word_GE 114
#define Word_GT 115
#define Word_AND 116
#define Word_OR 117
#define Word_NOT 118
#define Word_MOV 119
#define Word_ADD 120
#define Word_SUB 121
#define Word_MUL 122
#define Word_DIV 123
#define Word_INC 124
#define Word_DEC 125
#define Word_ACC 126
#define Word_DEL 127
#define Word_MOD 128
#define Word_PRO 129
#define Word_QUO 130
#define Word_POW 131
#define Word_RAD 132
#define Word_SQRT 133
#define Word_L_PARENTHESE 134
#define Word_R_PARENTHESE 135
#define Word_L_SQRBRACKET 136
#define Word_R_SQRBRACKET 137
#define Word_L_BRACE 138
#define Word_R_BRACE 139
#define Word_DOT 140
#define Word_COMMA 141
#define Word_SEMICOLON 142
#define Word_QUESTION 143
#define Word_COLON 144
#define KeyWord_null 0
#define KeyWord_int 1
#define KeyWord_double 2
#define KeyWord_String 3
#define KeyWord_void 4
#define KeyWord_public 5
#define KeyWord_new 6
#define KeyWord_for 7
#define KeyWord_while 8
#define KeyWord_if 9
#define KeyWord_else 10
#define KeyWord_array 11
#define KeyWord_return 12
#define KeyWord_this 13
#define KeyWord_class 14
#define KeyWord_Object 15
#define KeyWord_Vector 16
#define KeyWord_break 17
#define KeyWord_do 18
#define KeyWord_switch 19
#define KeyWord_case 20
#define KeyWord_default 21
#define KeyWord_until 22
#define KeyWord_func 23
int KeyWordTableLength=24;
String KeyWordTable[]=
{
	"null",
	"int",
	"double",
	"String",
	"void",
	"public",
	"new",
	"for",
	"while",
	"if",
	"else",
	"array",
	"return",
	"this",
	"class",
	"Object",
	"Vector",
	"break",
	"do",
	"switch",
	"case",
	"default",
	"until",
	"func"
};
int getKeyWordIndex(String word)
{
	int l=KeyWordTableLength,i;
	for(i=0;i<l;i++)if(equals(word,KeyWordTable[i]))return i;
	if(equals(word,"Point")||equals(word,"Color"))return KeyWord_Vector;
	return -1;
}
#define Word struct Word
Word
{
	Word* next;
	int classification;
	int operationType;
};
Word* newWord(int classification,int operationType)
{
	Word* word=(Word*)malloc(sizeof(Word));
	word->classification=classification;
	word->operationType=operationType;
	return word;
}
#define WordList struct WordList
WordList
{
	Word* firstWord;
	Word* lastWord;
	int length;
};
WordList* newWordList()
{
	WordList* wordList=(WordList*)malloc(sizeof(WordList));
	wordList->firstWord=null;
	wordList->lastWord=null;
	wordList->length=0;
	return wordList;
}
void addWord(WordList* wordList,Word* word)
{
	wordList->length++;
	if(wordList->firstWord==null)
	{
		wordList->firstWord=word;
		wordList->lastWord=word;
	}
	else
	{
		wordList->lastWord->next=word;
		wordList->lastWord=word;
	}
}
void freeWordList(WordList* wordList)
{
	int length=wordList->length,i;
	Word* word=wordList->firstWord;
	for(i=0;i<length;i++)
	{
		Word* temp=word;
		word=word->next;
		free(temp);
	}
	free(wordList);
}
int* toWordTable(WordList* wordList)
{
	int length=wordList->length,i;
	int* table=newInt(2*length);
	Word* word=wordList->firstWord;
	for(i=0;i<length;i++,word=word->next)
	{
		table[i*2+0]=word->classification;
		table[i*2+1]=word->operationType;
	}
	freeWordList(wordList);
	return table;
}
#define Identifier struct Identifier
Identifier
{
	String name;
	int type;
	int pointer;
	int label;
	Identifier* next;
};
Identifier* newIdentifier(String name,int type)
{
	Identifier* identifier=(Identifier*)malloc(sizeof(Identifier));
	identifier->name=name;
	identifier->type=type;
	identifier->pointer=-1;
	identifier->label=-1;
	identifier->next=null;
	return identifier;
}
#define IdentifierList struct IdentifierList
IdentifierList
{
	Identifier* firstIdentifier;
	Identifier* lastIdentifier;
	int length;
};
IdentifierList* newIdentifierList()
{
	IdentifierList* identifierList=(IdentifierList*)malloc(sizeof(IdentifierList));
	identifierList->firstIdentifier=null;
	identifierList->lastIdentifier=null;
	identifierList->length=0;
	return identifierList;
}
void addIdentifier(IdentifierList* identifierList,String name)
{
	identifierList->length++;
	if(identifierList->firstIdentifier==null)
	{
		identifierList->firstIdentifier=newIdentifier(name,0);
		identifierList->lastIdentifier=identifierList->firstIdentifier;
	}
	else
	{
		if(notExist(identifierList,name))
		{
			Identifier* id=newIdentifier(name,0);
			identifierList->lastIdentifier->next=id;
			identifierList->lastIdentifier=id;
		}
	}
}
Identifier** toIdentifierTable(IdentifierList* identifierList)
{
	int length=identifierList->length,i;
	Identifier** table=(Identifier**)malloc(length*sizeof(IdentifierList*));
	Identifier* id=identifierList->firstIdentifier;
	for(i=0;i<length;i++,id=id->next)table[i]=id;
	free(identifierList);
	return table;
}
int getIdentifierIndex(IdentifierList* identifierList,String name)
{
	int index=0;
	Identifier* id;
	for(id=identifierList->firstIdentifier;id!=null;id=id->next,index++)
	{
		if(equals(id->name,name))return index;
	}
	addIdentifier(identifierList,name);
	return identifierList->length-1;
}
boolean notExist(IdentifierList* identifierList,String name)
{
	Identifier* id;
	for(id=identifierList->firstIdentifier;id!=null;id=id->next)
	{
		if(equals(id->name,name))return false;
	}
	return true;
}
