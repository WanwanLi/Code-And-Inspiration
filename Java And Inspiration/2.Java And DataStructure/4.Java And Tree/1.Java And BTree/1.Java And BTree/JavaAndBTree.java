import java.io.*;
import java.awt.*;
import java.applet.*;
import javax.swing.*;
public class JavaAndBTree extends Applet
{
	public void paint(Graphics g)
	{
		int[] array=new int[]{1,9,7,4,2,6,8,4,11,15,18,13,39,28,66,30,47,65};
		BTree BTree1=new BTree();
		BTree1.insertArray(array);
		g.drawString(BTree1.getInOrderBTree(),50,30);
		g.drawString(BTree1.getLevelOrderBTree(),50,50);
		BTree1.drawBTree(g,500,150,20);
	}
}
class BTree
{
	class BNode
	{
		public static final int BSize=3;
		public static final int BHalfSize=BSize%2!=0?(BSize+1)/2:BSize/2;
		public static final int EndKey=11235813;
		public int[] Key=new int[BSize+1];
		public BNode[] Child=new BNode[BSize+1];
		public BNode Parent;
		public int Number;
		public BNode(int key)
		{
			this.Number=1;
			this.Key[Number]=key;
			this.Key[Number+1]=this.EndKey;
			this.Child[0]=this.Child[1]=null;
			this.Parent=null;
		}
	}
	BNode Root;
	String InOrderBTree;
	public void insert(int key)
	{
		if(Root==null)
		{
			Root=new BNode(key);
			return;
		}
		BNode c=Root;
		BNode p=c;
		int i=1;
		while(c!=null)
		{
			i=1;
			while(key>c.Key[i])i++;
			p=c;
			c=p.Child[i-1];
		}
		for(;;)
		{
			for(int j=p.Number;j>=i;j--)
			{
				p.Key[j+1]=p.Key[j];
				p.Child[j+1]=p.Child[j];
			}
			p.Key[i]=key;
			p.Child[i]=c;
			p.Number++;
			if(p.Number<BNode.BSize)
			{
				p.Key[p.Number+1]=BNode.EndKey;
				return;
			}
			else
			{
				c=new BNode(0);
				c.Number=BNode.BSize-BNode.BHalfSize;
				for(int j=0;j<=c.Number;j++)
				{
					c.Child[j]=p.Child[j+BNode.BHalfSize];
					if(c.Child[j]!=null)c.Child[j].Parent=c;
				}
				for(int j=1;j<=c.Number;j++)c.Key[j]=p.Key[j+BNode.BHalfSize];
				c.Key[c.Number+1]=BNode.EndKey;
				c.Parent=p.Parent;
				key=p.Key[BNode.BHalfSize];
				p.Number=BNode.BHalfSize-1;
				p.Key[p.Number+1]=BNode.EndKey;
				if(p.Parent==null)
				{
					Root=new BNode(key);
					Root.Child[0]=p;
					p.Parent=Root;
					Root.Child[1]=c;
					c.Parent=Root;
					return;
				}
				p=p.Parent;
				i=1;
				while(key>p.Key[i])i++;
			}
		}
	}
	public void insertArray(int[] array)
	{
		for(int i=0;i<array.length;i++)this.insert(array[i]);
	}
	public String getInOrderBTree()
	{
		InOrderBTree="InOrderBTree Is:";
		this.displayInOrder(Root);
		return InOrderBTree;
	}
	private void displayInOrder(BNode n)
	{
		if(n!=null)
		{
			displayInOrder(n.Child[0]);
			for(int i=1;i<=n.Number;i++)
			{
				InOrderBTree+=" "+n.Key[i];
				displayInOrder(n.Child[i]);
			}
		}
	}
	class Node
	{
		public Object Data;
		public int data;
		public Node Next;
		public Node(Object data)
		{
			this.Data=data;
			this.Next=null;
		}
		public Node(int data)
		{
			this.data=data;
			this.Next=null;
		}
	}
	class Queue
	{
		Node Front;
		Node Rear;
		public boolean isNotEmpty()
		{
			return (Front!=null);
		}
		public void enQueue(Object data)
		{
			Node n=new Node(data);
			if(Front==null)
			{
				Front=n;
				Rear=n;
				return;
			}
			Rear.Next=n;
			Rear=Rear.Next;
		}
		public void enQueue(int data)
		{
			Node n=new Node(data);
			if(Front==null)
			{
				Front=n;
				Rear=n;
				return;
			}
			Rear.Next=n;
			Rear=Rear.Next;
		}
		public Object deQueue()
		{
			Node n=Front;
			if(Front==null)return 0;
			Front=Front.Next;
			return n.Data;
		}
		public int dequeue()
		{
			Node n=Front;
			if(Front==null)return 0;
			Front=Front.Next;
			return n.data;
		}
	}
	public String getLevelOrderBTree()
	{
		String LevelOrderBTree="LevelOrderBTree Is:";
		Queue Queue1=new Queue();
		Queue1.enQueue(Root);
		while(Queue1.isNotEmpty())
		{
			BNode n=(BNode)Queue1.deQueue();
			if(n.Child[0]!=null)Queue1.enQueue(n.Child[0]);
			for(int i=1;i<=n.Number;i++)
			{
				LevelOrderBTree+=" "+n.Key[i];
				if(n.Child[i]!=null)Queue1.enQueue(n.Child[i]);
			}
		}
		return LevelOrderBTree;
	}
	private int getLevel(BNode n)
	{
		int level=1;
		for(BNode p=n.Parent;p!=null;p=p.Parent)level++;
		return level;
	}
	private int getHeight(BNode n)
	{
		if(n==null)return 0;
		int maxHeightOfChild=0;
		for(int i=0;i<=n.Number;i++)
		{
			int height=this.getHeight(n.Child[i]);
			if(height>maxHeightOfChild)maxHeightOfChild=height;
		}
		return maxHeightOfChild+1;
	}
	private int pow(int a,int x)
	{
		int p=1;
		for(int i=0;i<x;i++)p*=a;
		return p;
	}
	public void drawBTree(Graphics g,int x,int y,int size)
	{
		Queue Queue1=new Queue();
		Queue QueueX=new Queue();
		Queue QueueY=new Queue();
		Queue1.enQueue(Root);
		QueueX.enQueue(x);
		QueueY.enQueue(y);
		int height=this.getHeight(Root);
		while(Queue1.isNotEmpty())
		{
			BNode n=(BNode)Queue1.deQueue();
			int X0=QueueX.dequeue();
			int Y0=QueueY.dequeue();
			int level=this.getLevel(n);
			int width=pow(BNode.BSize,height-level)*size;
			int dx=width/n.Number;
			int dy=4*size;
			int X1=X0-(width-n.Number*size)/2;
			int Y1=Y0+dy;
			if(n.Child[0]!=null)
			{
				Queue1.enQueue(n.Child[0]);
				QueueX.enQueue(X1);
				QueueY.enQueue(Y1);
				g.drawLine(X0,Y0+size,X1,Y1);
			}
			for(int i=1;i<=n.Number;i++)
			{
				g.drawRect(X0,Y0,size,size);
				g.drawString(""+n.Key[i],X0+size/4,Y0+size*3/4);
				X0+=size;
				if(n.Child[i]!=null)
				{
					Queue1.enQueue(n.Child[i]);
					QueueX.enQueue(X1+i*dx);
					QueueY.enQueue(Y1);
					g.drawLine(X0,Y0+size,X1+i*dx,Y1);

				}
			}
		}
	}
}