#define true 1
#define false 0
#define null 0
#define INF 2147483647.0
#define String char*
int* newInt(int n)
{
	return (int*)malloc(n*sizeof(int));
}
int** new_Int(int m,int n)
{
	int** a=(int**)malloc(m*sizeof(int*)),i;
	for(i=0;i<m;i++)a[i]=newInt(n);
	return a;
}
double* newDouble(int n)
{
	return (double*)malloc(n*sizeof(double));
}
double** new_Double(int m,int n)
{
	double** a=(double**)malloc(m*sizeof(double*));int i;
	for(i=0;i<m;i++)a[i]=newDouble(n);
	return a;
}
String newString(int n)
{
	char* chars=(char*)malloc((n+1)*sizeof(char));
	chars[n]='\0';
	return chars;
}
String* new_String(int n)
{
	String* strings=(String*)malloc(n*sizeof(String));
	return strings;
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
int equals(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	if(length0!=length1)return 0;
	for(i=0;i<length0;i++)if(string0[i]!=string1[i])return 0;
	return 1;
}
int getIndex(String* a,String e,int n)
{
	int i=0;
	for(;i<n;i++)if(equals(a[i],e))break;
	return i;
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
void addInt(List* list,int integer)
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
int nextInt(List* list)
{
	if(list->length==0)return 0;
	Node* node=list->first;
	list->first=node->next;
	list->length--;
	return node->integer;
}
void pushInt(List* list,int integer)
{
	Node* node=newNode(integer,0.0,"");
	node->next=list->first;
	list->first=node;
	list->length++;
}
int popInt(List* list)
{
	return nextInt(list);
}
String nextString(List* list)
{
	if(list->length==0)return 0;
	Node* node=list->first;
	list->first=node->next;
	list->length--;
	return node->string;
}
