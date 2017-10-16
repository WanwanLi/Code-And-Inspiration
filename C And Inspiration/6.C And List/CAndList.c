#include<stdio.h>
#include<stdlib.h>
#include<stdarg.h>
#define String char*
#define null NULL
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
String formatString(const String format,...)
{
	char buffer[1000];
	va_list arglist;
	va_start(arglist,format);
	int n=vsprintf(buffer,format,arglist);
	va_end(arglist);
	String string=newChar(n);
	int i;
	for(i=0;i<n;i++)string[i]=buffer[i];
	return string;
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
int* toIntegers(List* list)
{
	int l=list->length,i;
	int* ints=newInt(l);
	Node* n=list->first;
	for(i=0;i<l;i++,n=n->next)ints[i]=n->integer;
	return ints;
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
void freeList(List* list)
{
	int i;
	Node* n=list->first;
	for(i=0;i<list->length;i++,n=n->next)free(n);
	free(list);
}
int main()
{
	int i;
	List* integerList=newList();
	for(i=0;i<10;i++)addInteger(integerList,10*i);
	int* integers=toIntegers(integerList);
	for(i=0;i<integerList->length;i++)printf("integers[%d]=%d\n",i,integers[i]);
	freeList(integerList);
	free(integers);
	List* doubleList=newList();
	for(i=0;i<10;i++)addDouble(doubleList,1.1*i);
	double* doubles=toDoubles(doubleList);
	for(i=0;i<doubleList->length;i++)printf("doubles[%d]=%f\n",i,doubles[i]);
	freeList(doubleList);
	free(doubles);
	List* stringList=newList();
	for(i=0;i<10;i++)addString(stringList,formatString("%d*%.3f=%.3f",i,1.111,i*1.111));
	String* strings=toStrings(stringList);
	for(i=0;i<stringList->length;i++)printf("strings[%d]=%s\n",i,strings[i]);
	freeList(stringList);
	free(strings);
	return 0;
}
