#include<stdio.h>
#include<stdlib.h>
#define Node struct Node
Node
{
	int integer;
	Node* next;
};
Node* first;
Node* last;
int listLength=0;
Node* newNode(int integer)
{
	Node* node=(Node*)malloc(sizeof(Node));
	node->integer=integer;
	node->next=NULL;
	return node;
}
void addIntegerIntoList(int integer)
{
	Node* node=newNode(integer);
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
int deleteIntegerFromList(int integer)
{
	int i;
	Node* n=first;
	Node* t=n;
	if(first->integer==integer)
	{
		first=first->next;
		free(n);
		listLength--;
		return 0;
	}
	for(i=0;i<listLength;i++,t=n,n=n->next)
	{
		if(n->integer==integer)
		{
			t->next=n->next;
			if(n==last)last=t;
			free(n);
			listLength--;
			return i;
		}
		
	}
	return -1;
}
int removeIntegerFromList(int index)
{
	if(index<0||index>=listLength)return 0;
	int i;
	Node* n=first;
	Node* t=n;
	if(index==0)first=first->next;
	else
	{
		for(i=0;i<index;i++,t=n,n=n->next);
		t->next=n->next;
		if(n==last)last=t;
	}
	int integer=n->integer;
	free(n);
	listLength--;
	return integer;
}
int existInList(int integer)
{
	int i; 
	Node* n;
	for(i=0,n=first;i<listLength;i++,n=n->next)
	{
		if(n->integer==integer)return 1;
	}
	return 0;
}
void printList()
{
	int i;
	Node* n=first;
	for(i=0;i<listLength;i++,n=n->next)
	{
		printf("integer[%d]=%d\n",i,n->integer);
	}
}
int main()
{
	int i,l=10;
	for(i=0;i<l;i++)addIntegerIntoList(i*2);
	printList();
	int integer=18,index=8;
	printf("delete integer[%d]=%d\n",deleteIntegerFromList(integer),integer);
	printList();
	printf("remove integer[%d]=%d\n",index,removeIntegerFromList(index));
	printList();
	integer=30;
	printf("add integer(%d)\n",integer);
	addIntegerIntoList(integer);
	printList();
	integer=-1;
	printf("existInList(%d)=%d\n",integer,existInList(integer));
	return 0;
}
