public class JavaAndLinkedListSort
{
	public static void main(String[] args)
	{
		LinkedList LinkedList1=new LinkedList();
		LinkedList LinkedList2=new LinkedList();
		for(int i=0;i<10;i++)
		{
			int n=(int)(100*Math.random());
			LinkedList1.insert(n);
			LinkedList2.add(n);
		}
		System.out.println("Before sorting...");
		for(Node n=LinkedList2.first;n!=null;n=n.next)
		{
			System.out.println(n.data);
		}
		System.out.println("After sorting...");
		for(Node n=LinkedList1.first;n!=null;n=n.next)
		{
			System.out.println(n.integer);
		}
		System.out.println("Invert sorting...");
		for(Node n=LinkedList1.last;n!=null;n=n.previous)
		{
			System.out.println(n.integer);
		}
	}
}
class Node
{
	public int integer;
	public Object data;
	public Node previous;
	public Node next;
	public Node(Object object)
	{
		this.integer=0;
		this.data=object;
		this.previous=null;
		this.next=null;
	}
	public Node(int integer)
	{
		this.integer=integer;
		this.previous=null;
		this.next=null;
		this.data=null;
	}
}
class LinkedList 
{
	public Node first;
	public Node last;
	public int length;
	public LinkedList()
	{
		this.first=null;
		this.last=null;
		this.length=0;
	}
	public void add(Object object)
	{
		this.length++;
		Node node=new Node(object);
		if(first==null)this.first=this.last=node;
		else
		{
			this.last.next=node;
			node.previous=last;
			this.last=node;
		}
	}
	public void insert(int integer)
	{
		this.length++;
		Node node=new Node(integer);
		if(first==null)this.first=this.last=node;
		else
		{
			Node p,n=first;
			for(;n!=null&&n.integer<=integer;n=n.next);
			if(n==null)
			{
				this.last.next=node;
				node.previous=last;
				this.last=node;
				return;
			}
			p=n.previous;
			if(p==null)
			{
				node.next=first;
				first.previous=node;
				this.first=node;
				return;
			}
			p.next=node;
			node.previous=p;
			node.next=n;
			n.previous=node;
		}
	}
	public void append(int integer)
	{
		this.length++;
		Node node=new Node(integer);
		if(first==null)this.first=this.last=node;
		else
		{
			Node n,p=last;
			for(;p!=null&&p.integer>integer;p=p.previous);
			if(p==null)
			{
				node.next=first;
				first.previous=node;
				this.first=node;
				return;
			}
			n=p.next;
			if(n==null)
			{
				this.last.next=node;
				node.previous=last;
				this.last=node;
				return;
			}
			p.next=node;
			node.previous=p;
			node.next=n;
			n.previous=node;
		}
	}
}

