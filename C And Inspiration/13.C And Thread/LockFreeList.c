#include <stdio.h>
#include <thread>
#include <atomic>
#include <iostream>
#define INT_MAX 2147483647
#define INT_MIN (-2147483647-1)
using namespace std;
template<typename Reference> 
class AtomicMarkableReference
{
	private: atomic<Reference> ref;
	private: const int64_t Marked = 0x0000000000000001;
	private: const int64_t UnMraked = 0x0000000000000000;
	public: void setReference(Reference p)
	{
		ref.store((Reference)(int64_t)p);
	}
	public: AtomicMarkableReference operator = (const AtomicMarkableReference &a)
	{
		if(*this != a)ref.store(a.ref.load());
		return *this;
	}
	public: Reference getReference()
	{
		Reference p = ref.load();
		return (bool)((int64_t)p & Marked) ? (Reference)((int64_t)(p)& ~Marked) : p;
	}
	public: bool isMarked()
	{
		Reference p = ref.load();
		return (bool)((int64_t)p & Marked);
	}
	public: Reference get(bool &b)
	{
		Reference p = ref.load();
		b = (bool)((int64_t)p & Marked);
		return b ? (Reference)((int64_t)(p)& ~Marked) : p;
	}
	public: bool compareAndSet(Reference expectedReference, Reference newReference, bool expectedMark, bool newMark)
	{
		Reference p = ref.load();
		bool b = (bool)((int64_t)p & Marked);
		if (b == expectedMark)
		{
			expectedReference = (Reference)((int64_t)expectedReference | (int64_t)b);
			return ref.compare_exchange_strong(expectedReference, (Reference)((int64_t)newReference | (int64_t)newMark));
		}
		return false;
	}
	public: void set(Reference newReference, bool newMark)
	{
		newReference = (Reference)((int64_t)newReference | (int64_t)newMark);
		ref.exchange(newReference);
	}
	public: bool attemptMark(Reference expectedReference, bool newMark)
	{
		Reference newReference = (Reference)((int64_t)expectedReference | (int64_t)newMark);
		expectedReference = isMarked() ? (Reference)((int64_t)expectedReference | Marked) : expectedReference;
		return ref.compare_exchange_strong(expectedReference, newReference);
	}
	public: void println()const
	{
		printf("Ref [%p]. ", getReference());
		printf("mark = %s.\n", isMarked() ? "Marked" : "UnMarked");
	}
};
class Node
{
	public: int key;
	public: AtomicMarkableReference<Node*> next;
	public: Node(int key)
	{
		this->key = key;
		this->next.setReference(NULL);
	}
};
class Window
{
	public: Node* pred;
	public: Node* curr;
	public: Window(Node* pred, Node* curr) 
	{
		this->pred = pred;
		this->curr = curr;
	}
};
class List
{
	private: Node* head;
	public: List()
	{
		this->head=new Node(INT_MIN);
		this->head->next.setReference(new Node(INT_MAX));
	}
	private: Window* find(int key)
	{
		bool marked = false;
		while(true)
		{
			Node* pred = this->head;
			Node* curr = pred->next.getReference();
			while(true)
			{
				Node* succ = curr->next.get(marked);
				while(marked) 
				{
					bool snip = pred->next.compareAndSet(curr, succ,false,false);
					if(!snip)continue;
					curr = succ;
					succ = curr->next.get(marked);
				}
				if(curr->key >= key)return new Window(pred, curr);
				pred = curr;
				curr = succ;
			}
		}
		return NULL;
	}
	public: bool insert(int key) 
	{
		while(true)
		{
			Window* window = find(key);
			Node* pred = window->pred;
			Node* curr = window->curr;
			if(curr->key == key) return false;
			else
			{
				Node* node =new Node(key);
				node->next.setReference(curr);
				if(pred->next.compareAndSet(curr, node,false,false)) return true;
			}
		}
	}
	public: int remove(int key) 
	{
		while(true)
		{
			Window* window = find(key);
			Node* pred = window->pred;
			Node* curr = window->curr;
			if(curr->key != key) return false;
			else
			{
				Node* succ = curr->next.getReference();
				bool snip = curr->next.attemptMark(succ,true);
				if(!snip)continue;
				pred->next.compareAndSet(curr, succ,false,false);
				return curr->key;
			}
		}
		return 0;
	}
	public: bool contains(int key) 
	{
		bool marked =false;
		Node* curr = this->head;
		while(curr->key < key) 
		{
			curr = curr->next.getReference();
			Node* succ = curr->next.get(marked);
		}
		return curr->key == key && !marked;
	}
	public: void println()
	{
		printf("List = { ");
		for(Node* n=this->head;n!=NULL;n=n->next.getReference())
		{
			if(n->key==INT_MIN)continue;
			if(n->key==INT_MAX)continue;
			printf("%d, ", n->key);
		}
		printf(" ... }. \n");
	}
};
void execute_insert(int id, List* list)
{
	list->insert(id);
}
void execute_remove(int id, List* list)
{
	printf("%d, ", list->remove(id));
}
void execute_contains(int id, List* list)
{
	printf("%d:%s, ", id, list->contains(id)?"Yes":"No");
}
void test(List* list)
{
	list->insert(11);
	list->insert(5);
	list->insert(14);
	list->insert(8);
	list->println();
	printf("Remove : %d\n", list->remove(5));
	printf("Remove : %d\n", list->remove(11));
	list->println();
	printf("List contains 8? %s \n", list->contains(8)?"Yes":"No");
	printf("List contains 11? %s \n", list->contains(11)?"Yes":"No");
}
int main(int argc, char *argv[])
{
	List* list=new List();
	const int length=10;
	thread threads[length];
	for(int i=0; i<length; i++)threads[i]=thread(execute_insert, i, list);
	for(int i=0; i<length; i++)threads[i].join();
	list->println();
	printf("Contains = {");
	for(int i=0; i<length; i++)threads[i]=thread(execute_contains, i, list);
	for(int i=0; i<length; i++)threads[i].join();
	printf(" ... }.\n");
	printf("Remove = {");
	for(int i=0; i<length; i++)threads[i]=thread(execute_remove, i, list);
	for(int i=0; i<length; i++)threads[i].join();
	printf(" ... }.\n");
	return 0;
}
