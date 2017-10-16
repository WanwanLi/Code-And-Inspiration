public class JavaAndLinkedList
{
	public static void main(String[] args)
	{
		LinkedList LinkedList1=new LinkedList();
		LinkedList1.add("String0");
		LinkedList1.add("String1");
		LinkedList1.add("String2");
		LinkedList1.add(10);
		LinkedList1.add(100);
		LinkedList1.add(1000);
		for(Node n=LinkedList1.first;n!=null;n=n.next)
		{
			System.out.println(n.data);
		}
		for(Node n=LinkedList1.last;n!=null;n=n.previous)
		{
			System.out.println(n.data);
		}
	}
}
class Node
{
	public Object data;
	public Node previous;
	public Node next;
	public Node(Object object)
	{
		this.data=object;
		this.previous=null;
		this.next=null;
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
}

