#include <stdio.h>
using namespace std;
class Node
{
	public:
	int key;
	Node* next;
	Node(int key);
};
Node::Node(int key)
{
	this->key = key;
	this->next = NULL;
}
class LinkedList
{
	public:
	int length;
	Node* first;
	Node* last;
	void insert(int key);
	Node* remove(int key);
	void println();
	LinkedList();
	~LinkedList();
};
LinkedList::LinkedList()
{
	this->first=NULL;
	this->last=NULL;
	this->length=0;
}
void LinkedList::insert(int key)
{
	Node* node=new Node(key);
	if(length==0)
	{
		this->first=node;
		this->last=node;
	}
	else if(key<this->first->key)
	{
		node->next=this->first;
		this->first=node;
	}
	else if(key>=this->last->key)
	{
		this->last->next=node;
		this->last=node;
	}
	else
	{
		Node* n=this->first;
		for(int i=0;i<length-1;i++,n=n->next)
		{
			if(n->next->key>key)break;
		}
		node->next=n->next;
		n->next=node;
	}
	this->length++;
}
Node* LinkedList::remove(int key)
{
	Node* node=NULL;
	if(length==1)
	{
		node=this->first;
		this->first=NULL;
		this->last=NULL;
	}
	else if(key==this->first->key)
	{
		node=this->first;
		this->first=this->first->next;
	}
	else
	{
		Node* n=this->first;
		for(int i=0;i<length-1;i++,n=n->next)
		{
			if(n->next->key==key)break;
		}
		if(n->next==NULL)return NULL;
		node=n->next;
		n->next=n->next->next;
		if(node==last)this->last=n;
	}
	this->length--;
	return node;
}
void LinkedList::println()
{
	Node* n=this->first;
	printf("LinkedList = { ");
	for(int i=0;i<length;i++,n=n->next)
	{
		printf("%d, ", n->key);
	}
	printf(" ... }. \n");
}
LinkedList::~LinkedList()
{
	Node* n=this->first;
	printf("Delete: { ");
	for(int i=0;i<length;i++)
	{
		printf("%d, ", n->key);
		Node* t=n;
		n=n->next;
		delete t;
	}
	printf(" ... }. \n");
}
int main (int argc, char* argv[])
{
	int length=10;
	LinkedList* LinkedList1=new LinkedList();
	printf("Insert [0:9] into LinkedList1:\n");
	for(int i=0;i<length;i++)LinkedList1->insert(i);
	LinkedList1->println();
	printf("Remove 0 from LinkedList1:\n");
	Node* Node1=LinkedList1->remove(0);
	LinkedList1->println();
	printf("Remove 5 from LinkedList1:\n");
	Node1=LinkedList1->remove(5);
	LinkedList1->println();
	printf("Remove 9 from LinkedList1:\n");
	Node1=LinkedList1->remove(9);
	LinkedList1->println();
	printf("Insert 10 into LinkedList1:\n");
	LinkedList1->insert(10);
	LinkedList1->println();
	printf("Remove 11 from LinkedList1:\n");
	Node1=LinkedList1->remove(11);
	LinkedList1->println();
	delete LinkedList1;
	return 0;
}
