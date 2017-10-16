#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#define String char*
String newString(int length)
{
	String string=(String)(malloc((length+1)*sizeof(char)));
	string[length]='\0';
	return string;
}
int length(String string)
{
	int l=0;
	char c=string[l];
	while(c!='\0')
	{
		l++;
		c=string[l];
	}
	return l;
}
String add(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	String add=newString(length0+length1);
	for(i=0;i<length0;i++)add[i]=string0[i];
	for(i=0;i<length1;i++)add[length0+i]=string1[i];
	return add;
}
String toString(int integer)
{
	String zero="0";
	if(integer==0)return zero;
	int isNegative=0;
	if(integer<0)isNegative=1;
	int number=integer;
	if(isNegative)number=-integer;
	int length=0;
	int i=0;
	while(number>0)
	{
		number/=10;
		length++;
	}
	number=integer;
	if(isNegative)number=-integer;
	String string=newString(length);
	for(i=0;i<length;i++)
	{
		string[length-1-i]=(char)(number%10+'0');
		number/=10;
	}
	if(isNegative)return add("-",string);
	return string;
}
int equals(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	if(length0!=length1)return 0;
	for(i=0;i<length0;i++)if(string0[i]!=string1[i])return 0;
	return 1;
}
int writeFile(String fileName,String string)
{
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return 0;
	fprintf(file,"%s",string);
	fclose(file);
	return 1;
}
String readFile(String fileName)
{
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return NULL;
	String result=newString(0);
	String string=newString(0);
	while(!feof(file))
	{
		fscanf(file,"%s",string);
		result=add(result,add(string,"\n"));
	}
	fclose(file);
	return result;
}
int* newInt(int length)
{
	int* integers=(int*)(malloc(length*sizeof(int)));
	return integers;
}
int exponent(int base,int index)
{
	int i=0;
	int result=1;
	for(i=0;i<index;i++)result*=base;
	return result;
}
int toInteger(String string)
{
	int i,k;
	char c;
	int l=length(string);
	int integer=0;
	for(i=0;i<l;i++)
	{
		c=string[i];
		k=(int)(c-'0');
		integer+=k*exponent(10,l-1-i);
	}
	return integer;
}
int writeIntegersIntoFile(String fileName,int* integers,int length)
{
	int i;
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return 0;
	for(i=0;i<length;i++)fprintf(file,"%d ",integers[i]);
	fclose(file);
	return 1;
}
int* readIntegersFromFile(String fileName,int length)
{
	int i;
	int* integers=newInt(length);
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return NULL;
	String string=newString(0);
	for(i=0;i<length;i++)
	{
		fscanf(file,"%s",string);
		integers[i]=toInteger(string);
	}
	fclose(file);
	return integers;
}


#define Node struct Node
Node
{
	String string;
	Node* next;
};
Node* first;
Node* last;
int listLength=0;
Node* newNode(String string)
{
	Node* node=(Node*)malloc(sizeof(Node));
	node->string=string;
	node->next=NULL;
	return node;
}
void addStringIntoList(String string)
{
	Node* node=newNode(string);
	if(first==NULL)
	{
		first=node;
		last=node;
	}
	else
	{
		last->next=node;
		last=node;
	}
	listLength++;
}

Node* readStringsFromFile(String fileName,int length)
{
	int i;
	first=NULL;
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return NULL;
	for(i=0;i<length;i++)
	{
		String string=newString(0);
		fscanf(file,"%s",string);
		addStringIntoList(string);
	}
	fclose(file);
	return first;
}

int randomCounter=0;
int getRandomInt(int m)
{
	randomCounter++;
	int random=rand();
	if(random<0)random*=-1;
	random%=m;
	return random;
}
String* parametersList;
int parametersLength;
String getArithmeticInstruction()
{
	int random=getRandomInt(9);
	if(random<4)
	{
		return parametersList[getRandomInt(parametersLength)];
	}
	else if(random<6)
	{
		return toString(getRandomInt(1000));
	}
	else if(random<7)
	{
		return add("(",add(getArithmeticInstruction(),")"));
	}
	else
	{
		random=getRandomInt(3);
		String op;
		if(random==0)op="+";
		else if(random==1)op="-";
		else op="*";
		return add(getArithmeticInstruction(),add(op,getArithmeticInstruction()));
	}
}
String getCompareInstruction()
{
	int random=getRandomInt(5);
	String op;
	if(random<=1)op="<=";
	else if(random==2)op=">=";
	else if(random==3)op="<";
	else op=">";
	return add(getArithmeticInstruction(),add(op,getArithmeticInstruction()));
}
String getCodeBlock(int level);
String getIfElseInstruction(int level)
{
	int random=getRandomInt(6),i;
	String tab=newString(level);
	for(i=0;i<level;i++)tab[i]='\t';
	String code=add("if(",add(getCompareInstruction(),")\n"));
	code=add(code,add(tab,"{\n"));
	code=add(code,getCodeBlock(level+1));
	code=add(code,add(tab,"}\n"));
	if(random<3)return code;
	else if(random<4)code=add(code,add(add(tab,"else if("),add(getCompareInstruction(),")\n")));
	else code=add(code,add(tab,"else\n"));
	code=add(code,add(tab,"{\n"));
	code=add(code,getCodeBlock(level+1));
	code=add(code,add(tab,"}\n"));
	return code;
}
String getForInstruction(int level)
{
	int random=getRandomInt(6),i;
	String tab=newString(level);
	for(i=0;i<level;i++)tab[i]='\t';
	String s=add(parametersList[getRandomInt(parametersLength)],"=");
	s=add(s,add(getArithmeticInstruction(),";"));
	String code=add(add("for(",s),add(getCompareInstruction(),";"));
	code=add(code,add(parametersList[getRandomInt(parametersLength)],"++)\n"));
	code=add(code,add(tab,"{\n"));
	code=add(code,getCodeBlock(level+1));
	code=add(code,add(tab,"}\n"));
	return code;
}
String getWhileInstruction(int level)
{
	int random=getRandomInt(6),i;
	String tab=newString(level);
	for(i=0;i<level;i++)tab[i]='\t';
	String code=add("while(",add(getCompareInstruction(),")\n"));
	code=add(code,add(tab,"{\n"));
	code=add(code,getCodeBlock(level+1));
	code=add(code,add(tab,"}\n"));
	return code;
}
String getInstruction(int level)
{
	int i;
	String code=newString(level);
	for(i=0;i<level;i++)code[i]='\t';
	int random=getRandomInt(10);
	if(random<4)
	{
		code=add(code,add(parametersList[getRandomInt(parametersLength)],"="));
		code=add(code,getArithmeticInstruction());
		code=add(code,";\n");
	}
	else if(random<6)code=add(code,getIfElseInstruction(level));
	else if(random<8)code=add(code,getForInstruction(level));
	else code=add(code,getWhileInstruction(level));
	return code;
}

String getCodeBlock(int level)
{
	int random=getRandomInt(12),i;
	String tab=newString(level);
	for(i=0;i<level;i++)tab[i]='\t';
	String code;
	if(level>0&&random<5)code=getInstruction(level);
	else
	{
		code=add(tab,"{\n");
		if(random<8)code=add(code,getInstruction(level+1));
		else code=add(code,getCodeBlock(level+1));
		if(level==0)code=add(code,add("\treturn ",add(getArithmeticInstruction(),";\n")));
		code=add(code,add(tab,"}\n"));
	}
	random=getRandomInt(4);
	if(level>0&&random<1)code=add(code,getCodeBlock(level));
	return code;
}
String getMethod(String method,String* parameters,int length)
{
	int i;
	String fileName="code.c";
	String code=add("int ",method);
	code=add(code,"(");
	parametersList=parameters;
	parametersLength=length;
	for(i=0;i<length;i++)
	{
		code=add(code,add("int ",parameters[i]));
		if(i<length-1)code=add(code,",");
	}
	code=add(code,")\n");
	code=add(code,getCodeBlock(0));
	return code;
}

void runConsole()
{
	String cmd=newString(0);
	scanf("%s",cmd);
	int i,j,k,m;
	for(i=0;cmd[i]!='\0';i++)if(cmd[i]==':')break;
	String fileName=newString(i);
	for(j=0;j<i;j++)fileName[j]=cmd[j];
	fileName=add(fileName,".g");
	if(equals(fileName,"exit"))exit(0);
	else
	{
		for(i++;cmd[i]!='\0';i++)if(cmd[i]=='>')break;
		String number=newString(i-j-1);
		for(j++,k=j;j<i;j++)number[j-k]=cmd[j];
		int length=toInteger(number);
		String parameters[length];
		for(i++;cmd[i]!='\0';i++)if(cmd[i]=='(')break;
		String method=newString(i-j-1);
		for(j++,k=j;j<i;j++)method[j-k]=cmd[j];
		for(m=0;m<length-1;m++)
		{
			for(i++;cmd[i]!='\0';i++)if(cmd[i]==',')break;
			parameters[m]=newString(i-j-1);
			for(j++,k=j;j<i;j++)parameters[m][j-k]=cmd[j];
		}
		for(i++;cmd[i]!='\0';i++)if(cmd[i]==')')break;
		parameters[m]=newString(i-j-1);
		for(j++,k=j;j<i;j++)parameters[m][j-k]=cmd[j];
		String code;
		srand((unsigned)time(NULL));
		while(randomCounter<40)
		{
			randomCounter=0;
			code=getMethod(method,parameters,length);
		}
		code=add(code,"int main(){return 0;}\n");
		writeFile(fileName,code);
		printf("%s",code);
	}
}
int main()
{
	runConsole();
	return 0;
}
