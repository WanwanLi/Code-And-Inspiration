public class JavaAndSplayTree
{
	public static void main(String[] args)
	{
		int[] array=new int[]{10,4,3,8,6,2,5};
		SplayTree SplayTree1=new SplayTree();
		SplayTree1.insertArray(array);
		System.out.println(SplayTree1.getInOrderSplayTree());
	}
}
class SplayTree
{
	String InOrderSplayTree;
	class Node
	{
		public int Data;
		public Node LChild;
		public Node RChild;
		public Node(int data)
		{
			this.Data=data;
			this.LChild=null;
			this.RChild=null;
		}
	}
	Node None;
	Node Head;
	Node Root;
	Node NewRoot;
	public SplayTree()
	{
		None=new Node(0);
		None.LChild=None;
		None.RChild=None;
		Root=None;
		Head=new Node(0);
		NewRoot=null;
	}
	private Node rotateWithLChild(Node n)
	{
		Node nL=n.LChild;
		if(nL!=null)
		{
			n.LChild=nL.RChild;
			nL.RChild=n;
			n=nL;
		}
		return n;
	}
	private Node rotateWithRChild(Node n)
	{
		Node nR=n.RChild;
		if(nR!=null)
		{
			n.RChild=nR.LChild;
			nR.LChild=n;
			n=nR;
		}
		return n;
	}
	private Node splay(int data,Node n)
	{
		Head.LChild=Head.RChild=None;
		Node LTreeMax=Head;
		Node RTreeMin=Head;
		None.Data=data;
		for(;;)
		{
			if(data<n.Data)
			{
				if(data<n.LChild.Data)n=this.rotateWithLChild(n);
				if(n.LChild==None)break;
				RTreeMin.LChild=n;
				RTreeMin=n;
				n=n.LChild;
			}
			else if(data>n.Data)
			{
				if(data>n.RChild.Data)n=this.rotateWithRChild(n);
				if(n.RChild==None)break;
				LTreeMax.RChild=n;
				LTreeMax=n;
				n=n.RChild;
			}
			else break;
		}
		LTreeMax.RChild=n.LChild;
		RTreeMin.LChild=n.RChild;
		n.LChild=Head.RChild;
		n.RChild=Head.LChild;
		return n;
	}
	public void insert(int data)
	{
		NewRoot=new Node(data);
		if(Root==None)NewRoot.LChild=NewRoot.RChild=None;
		else
		{
			Root=this.splay(data,Root);
			if(data<Root.Data)
			{
				NewRoot.LChild=Root.LChild;
				Root.LChild=None;
				NewRoot.RChild=Root;
			}
			else if(data>Root.Data)
			{
				NewRoot.RChild=Root.RChild;
				Root.RChild=None;
				NewRoot.LChild=Root;
			}
			else {;}
		}
		Root=NewRoot;
	}
	public void insertArray(int[] array)
	{
		int len=array.length;
		for(int i=0;i<len;i++)this.insert(array[i]);
	}
	String getInOrderSplayTree()
	{
		InOrderSplayTree="InOrderSplayTree Is:";
		this.displayInOrder(Root);
		return InOrderSplayTree;
	}
	private void displayInOrder(Node n)
	{
		if(n!=null&&n!=None)
		{
			displayInOrder(n.LChild);
			InOrderSplayTree+=" "+n.Data;
			displayInOrder(n.RChild);
		}
	}
}