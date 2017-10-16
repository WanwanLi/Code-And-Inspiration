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
class Queue
{
	private: atomic<Node*> head;
	private: atomic<Node*> tail;
	public: Queue()
	{
		Node* node = new Node(0);
		node->next = node;
		this->head = node;
		this->tail = node;
	}
	public: void enqueue(int key)
	{
		bool succ=false;
		Node* null=NULL;
		Node* p = this->tail;
		atomic<Node*> q;
		q = new Node(key);
		do
		{
			p = this->tail;
			succ=p->next.compare_exchange_weak(null,q);
			if(!succ)this->tail.compare_exchange_weak(p,p->next);
		}
		while(!succ);
		this->tail.compare_exchange_weak(p,q);
	}
	public: int dequeue()
	{
		Node* head = this->head;
		while (head && !this->head.compare_exchange_weak(head, head->next));/*
		which is : this->head = head->next, but in a thread-safe way. */
		return head!=NULL?head->next.load()->key:0;
	}
	public: bool contains(int key)
	{
		for(Node* n=this->head.load()->next;n!=NULL;n=n->next)
		{
			if(n->key==key)return true;
		}
		return false;
	}
	public: void println()
	{
		printf("Queue = { ");
		for(Node* n=this->head.load()->next;n!=NULL;n=n->next)
		{
			printf("%d, ", n->key);
		}
		printf(" ... }. \n");
	}
};
void execute_enqueue(int id, Queue* queue)
{
	queue->enqueue(id);
}
void execute_dequeue(int id, Queue* queue)
{
	printf("%d, ", queue->dequeue());
}
void test(Queue* queue)
{
	queue->enqueue(11);
	queue->enqueue(12);
	queue->enqueue(13);
	printf("Queue contains 12? %s \n", queue->contains(12)?"Yes":"No");
	queue->println();
	printf("Dequeue: %d\n", queue->dequeue());
	printf("Dequeue: %d\n", queue->dequeue());
	printf("Queue contains 12? %s \n", queue->contains(12)?"Yes":"No");
	queue->println();
}
void test1(Queue* queue)
{
	const int length=10;
	thread threads[length];
	for(int i=0; i<length; i++)threads[i]=thread(execute_enqueue, i, queue);
	for(int i=0; i<length; i++)threads[i].join();
	queue->println();
	printf("Dequeue = {");
	for(int i=0; i<length; i++)threads[i]=thread(execute_dequeue, i, queue);
	for(int i=0; i<length; i++)threads[i].join();
	printf(" ... }.\n");
}
int main (int argc, char *argv[])
{
	Queue* queue=new Queue();
	test1(queue);
	return 0;
}
