#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<stdarg.h>
#define String char*
#define null NULL
#define boolean int
#define true 1
#define false 0
int* newInt(int n)
{
	int* ints=(int*)malloc(n*sizeof(int));
	return ints;
}
double* newDouble(int n)
{
	double* Doubles=(double*)malloc(n*sizeof(double));
	return Doubles;
}
char* newChar(int n)
{
	char* chars=(char*)malloc((n+1)*sizeof(char));
	chars[n]='\0';
	return chars;
}
String* newString(int n)
{
	String* strings=(String*)malloc(n*sizeof(String));
	return strings;
}
String formatString(String format,...)
{
	String buffer=newChar(1000);
	va_list arglist;
	va_start(arglist,format);
	int n=vsprintf(buffer,format,arglist);
	va_end(arglist);
	String string=newChar(n);
	int i;
	for(i=0;i<n;i++)string[i]=buffer[i];
	free(buffer);
	return string;
}
String append(String s,char c)
{
	int i=0;
	int l=length(s);
	String a=newChar(l+1);
	for(i=0;i<l;i++)a[i]=s[i];
	a[l]=c;
	free(s);
	return a;
}
int length(String string)
{
	return strlen(string);
}
String add(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	String s=newChar(length0+length1);
	for(i=0;i<length0;i++)s[i]=string0[i];
	for(i=0;i<length1;i++)s[length0+i]=string1[i];
	return s;
}
String mul(String string0,String string1)
{
	int i=0,j=0,c=0;
	int length0=length(string0);
	int length1=length(string1);
	String s=newChar(2*length0*length1);
	for(i=0;i<length0;i++)
	{
		for(j=0;j<length1;j++)
		{
			s[c++]=string0[i];
			s[c++]=string1[j];
		}
	}
	return s;
}
String sub(String string0,String string1)
{
	int i=0,j=0,c=0,b=0;
	int l0=length(string0);
	int l1=length(string1);
	for(i=0;i<l0;i++)
	{
		b=0;
		for(j=0;i+j<l0&&j<l1;j++)if(string0[i+j]!=string1[j]){b=1;break;}
		if(b)c++;
		else i+=l1-1;
	}
	String s=newChar(c);
	c=0;
	for(i=0;i<l0;i++)
	{
		b=0;
		for(j=0;i+j<l0&&j<l1;j++)if(string0[i+j]!=string1[j]){b=1;break;}
		if(b)s[c++]=string0[i];
		else i+=l1-1;
	}
	return s;
}
String inc(String string)
{
	int i,l=length(string);
	String s=newChar(l+1);
	for(i=0;i<l;i++)s[i]=string[i];
	s[l]=string[l-1];
	return s;
}
String dec(String string)
{
	int i,l=length(string);
	if(l<=1)return newChar(0);
	String s=newChar(l-1);
	for(i=0;i<l-1;i++)s[i]=string[i];
	return s;
}
String reverse(String string)
{
	int i=0;
	int l=length(string);
	String s=newChar(l);
	for(i=0;i<l;i++)s[i]=string[l-1-i];
	return s;
}
boolean equals(String s1,String s2)
{
	return strcmp(s1,s2)==0?true:false;
}
int parseInt(String string)
{
	return atoi(string);
}
double parseDouble(String string)
{
	return atof(string);
}

boolean isAlpha(char c)
{
	return ('A'<=c&&c<='Z')||('a'<=c&&c<='z');
}
boolean isDigit(char c)
{
	return ('0'<=c&&c<='9');
}
#define Node struct Node
Node
{
	int integer;
	double Double;
	String string;
	Node* next;
};
Node* newNode(int integer,double Double,String string)
{
	Node* node=(Node*)malloc(sizeof(Node));
	node->integer=integer;
	node->Double=Double;
	node->string=string;
	node->next=null;
	return node;
}
#define List struct List
List
{
	Node* first;
	Node* last;
	int length;
};
List* newList()
{
	List* list=(List*)malloc(sizeof(List));
	list->first=null;
	list->last=null;
	list->length=0;
	return list;
}
void freeList(List* list)
{
	int i;
	Node* n=list->first;
	for(i=0;i<list->length;i++,n=n->next)free(n);
	free(list);
	list=null;
}
void addInteger(List* list,int integer)
{
	Node* node=newNode(integer,0.0,"");
	if(list->first==null)
	{
		list->first=node;
		list->last=node;
	}
	else
	{
		list->last->next=node;
		list->last=node;
	}
	list->length++;
}
#define LastInteger 2147483647
void addIntegers(List* list,int int0,...)
{
	int i,int_i;
	va_list argList;
	va_start(argList,int0);
	for(i=0,int_i=int0;int_i!=LastInteger;int_i=va_arg(argList,int))addInteger(list,int_i);
	va_end(argList);
}
void mergeIntegersFromTwoLists(List* list1,List* list2)
{
	int i;
	Node* n=list2->first;
	for(i=0;i<list2->length;i++,n=n->next)addInteger(list1,n->integer);
	freeList(list2);
}
void addDouble(List* list,double Double)
{
	Node* node=newNode(0,Double,"");
	if(list->first==null)
	{
		list->first=node;
		list->last=node;
	}
	else
	{
		list->last->next=node;
		list->last=node;
	}
	list->length++;
}
void addString(List* list,String string)
{
	Node* node=newNode(0,0.0,string);
	if(list->first==null)
	{
		list->first=node;
		list->last=node;
	}
	else
	{
		list->last->next=node;
		list->last=node;
	}
	list->length++;
}
int insertString(List* list,String string)
{
	Node* node=newNode(0,0.0,string);
	if(list->first==null)
	{
		list->first=node;
		list->last=node;
	}
	else
	{
		int i;
		Node* n;
		for(i=0,n=list->first;i<list->length;i++,n=n->next)
		{
			if(equals(string,n->string))return i;
		}
		list->last->next=node;
		list->last=node;
	}
	list->length++;
	return list->length-1;
}
int* toIntegers(List* list)
{
	int l=list->length,i;
	int* ints=newInt(l);
	Node* n=list->first;
	for(i=0;i<l;i++,n=n->next)ints[i]=n->integer;
	return ints;
}
void writeIntegers(FILE* file,List* list)
{
	int l=list->length,i;
	Node* n=list->first;
	for(i=0;i<l;i++,n=n->next)fprintf(file,"%d,",n->integer);
}
List* readIntegers(FILE* file,int listLength)
{
	List* list=newList();
	int l=listLength,i;
	for(i=0;i<l;i++)
	{
		int integer=0;
		fscanf(file,"%d,",&integer);
		addInteger(list,integer);
	}
	return list;
}
double* toDoubles(List* list)
{
	int l=list->length,i;
	double* Doubles=newDouble(l);
	Node* n=list->first;
	for(i=0;i<l;i++,n=n->next)Doubles[i]=n->Double;
	return Doubles;
}
String* toStrings(List* list)
{
	int l=list->length,i;
	String* strings=newString(l);
	Node* n=list->first;
	for(i=0;i<l;i++,n=n->next)strings[i]=n->string;
	return strings;
}
String toString(List* list)
{
	int l=list->length,i;
	String string=newChar(l);
	Node* n=list->first;
	for(i=0;i<l;i++,n=n->next)string[i]=(char)n->integer;
	return string;
}
void push(List* list,int integer)
{
	Node* n=newNode(integer,0,null);
	n->next=list->first;
	list->first=n;
	list->length++;
}
int pop(List* list)
{
	if(list->length==0)return 0;
	int integer=list->first->integer;
	list->first=list->first->next;
	list->length--;
	return integer;
}
int top(List* list)
{
	return list->first->integer;
}
void pushInteger(List* list,int integer)
{
	Node* n=newNode(integer,0,null);
	n->next=list->first;
	list->first=n;
	list->length++;
}
void pushDouble(List* list,double Double)
{
	Node* n=newNode(0,Double,null);
	n->next=list->first;
	list->first=n;
	list->length++;
}
void pushString(List* list,String string)
{
	Node* n=newNode(0,0,string);
	n->next=list->first;
	list->first=n;
	list->length++;
}
int popInteger(List* list)
{
	if(list->length==0)return 0;
	int integer=list->first->integer;
	list->first=list->first->next;
	list->length--;
	return integer;
}
double popDouble(List* list)
{
	if(list->length==0)return 0;
	double Double=list->first->Double;
	list->first=list->first->next;
	list->length--;
	return Double;
}
String popString(List* list)
{
	if(list->length==0)return null;
	String string=list->first->string;
	list->first=list->first->next;
	list->length--;
	return string;
}
void enQueueInteger(List* list,int integer)
{
	addInteger(list,integer);
}
void enQueueDouble(List* list,double Double)
{
	addDouble(list,Double);
}
void enQueueString(List* list,String string)
{
	addString(list,string);
}
int deQueueInteger(List* list)
{
	if(list->length==0)return 0;
	int integer=list->first->integer;
	list->first=list->first->next;
	list->length--;
	return integer;
}
double deQueueDouble(List* list)
{
	if(list->length==0)return 0;
	double Double=list->first->Double;
	list->first=list->first->next;
	list->length--;
	return Double;
}
String deQueueString(List* list)
{
	if(list->length==0)return newChar(0);
	String string=list->first->string;
	list->first=list->first->next;
	list->length--;
	return string;
}
void pushNode(List* list,Node* n)
{
	n->next=list->first;
	list->first=n;
	list->length++;
}
Node* popNode(List* list)
{
	Node* n=list->first;
	list->first=list->first->next;
	list->length--;
	return n;
}
List* reverseList(List* list)
{
	List* list1=newList();
	int i,length=list->length;
	for(i=0;i<length;i++)pushNode(list1,popNode(list));
	return list1;
}
void printList(List* list)
{
	if(list==null){printf("null\n");return;}
	int l=list->length,i;
	Node* n=list->first;
	for(i=0;i<l;i++,n=n->next)
	{
		printf("integer=%d\t",n->integer);
		printf("Double=%.3f\t",n->Double);
		printf("string=%s\n",n->string);
	}
}
String dtoa(int d)
{
	String a=newChar(256);
	sprintf(a,"%d",d);
	return a;
}
String ftoa(float f)
{
	String a=newChar(256);
	sprintf(a,"%f",f);
	return a;
}
String fscans(FILE* file)
{
	List* charList=newList();
	char c;
	fscanf(file,"%c",&c);
	while(c!=',')
	{
		addInteger(charList,c);
		fscanf(file,"%c",&c);
	}
	addInteger(charList,'\0');
	String s=toString(charList);
	return s;
}
#define MethodList struct MethodList
MethodList
{
	String* methodTable;
	int* methodPointerTable;
	int* methodReturnTypeTable;
	int methodNumber;
};
MethodList* newMethodList()
{
	MethodList* methodList=(MethodList*)malloc(sizeof(MethodList));
	methodList->methodTable=null;
	methodList->methodPointerTable=null;
	methodList->methodReturnTypeTable=null;
	methodList->methodNumber=0;
	return methodList;
}
