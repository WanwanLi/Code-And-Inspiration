#include <stdio.h>
#include <thread>
#include <atomic>
using namespace std;
class Node
{
	public: int key;
	public: atomic<Node*> next;
	public: Node(int key)
	{
		this->key = key;
		this->next=NULL;
	}
};
class Stack
{
	private: atomic<Node*> head;
	public: Stack()
	{
		this->head=NULL;
	}
	public: void push(int key)
	{
		Node* head = this->head;
		Node* node = new Node(key);
		node->next=head;
		while (!this->head.compare_exchange_weak(head,node))/*
		which is : this->head = node, but in a thread-safe way. */
		{
			node->next=head;
		}
	}
	public: int pop()
	{
		Node* head = this->head;
		while (head && !this->head.compare_exchange_weak(head, head->next));/*
		which is : this->head = head->next, but in a thread-safe way. */
		return head!=NULL?head->key:0;
	}
	public: bool contains(int key)
	{
		for(Node* n=this->head;n!=NULL;n=n->next)
		{
			if(n->key==key)return true;
		}
		return false;
	}
	public: void println()
	{
		printf("Stack = { ");
		for(Node* n=this->head;n!=NULL;n=n->next)
		{
			printf("%d, ", n->key);
		}
		printf(" ... }. \n");
	}
};
void execute_push(int id, Stack* stack)
{
	stack->push(id);
}
void execute_pop(int id, Stack* stack)
{
	printf("%d, ", stack->pop());
}
void test(Stack* stack)
{
	stack->push(11);
	stack->push(12);
	stack->push(13);
	printf("Stack contains 12? %s \n", stack->contains(12)?"Yes":"No");
	stack->println();
	printf("Pop: %d\n", stack->pop());
	printf("Pop: %d\n", stack->pop());
	printf("Stack contains 12? %s \n", stack->contains(12)?"Yes":"No");
	stack->println();
}
int main (int argc, char *argv[])
{
	Stack* stack=new Stack();
	const int length=10;
	thread threads[length];
	for(int i=0; i<length; i++)threads[i]=thread(execute_push, i, stack);
	for(int i=0; i<length; i++)threads[i].join();
	stack->println();
	printf("Pop = {");
	for(int i=0; i<length; i++)threads[i]=thread(execute_pop, i, stack);
	for(int i=0; i<length; i++)threads[i].join();
	printf(" ... }.\n");
	return 0;
}
