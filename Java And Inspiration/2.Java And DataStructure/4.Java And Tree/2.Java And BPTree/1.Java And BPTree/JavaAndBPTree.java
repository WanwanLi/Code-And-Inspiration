import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
public class JavaAndBPTree extends Applet
{
	public void paint(Graphics g)
	{
		int[] array=new int[]{1,9,7,4,2,6,8,4,11,15,18,13,39,28,66,30,47,65};
		BPTree BPTree1=new BPTree();
		BPTree1.insertArray(array);
		g.drawString(BPTree1.getInOrderBPTree(),50,30);
		g.drawString(BPTree1.getLevelOrderBPTree(),50,50);
		BPTree1.drawBPTree(g,500,150,20);
	}
	public static void main(String[] args)
	{
		int[] array=new int[]{1,9,7,4,2,6,8,4,11,15,18,13,39,28,66,30,47,65};
		final BPTree BPTree1=new BPTree();
		BPTree1.insertArray(array);
		System.out.println(BPTree1.getInOrderBPTree());
		System.out.println(BPTree1.getLevelOrderBPTree());
		System.out.println(BPTree1.getLinkedListBPTree());
		Frame Frame1=new Frame()
		{
			public void paint(Graphics g)
			{
				BPTree1.drawBPTree(g,600,200,20);								
			}
		};
		Frame1.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		Frame1.setVisible(true);
	}
}
class BPTree
{
	class BPNode
	{
		public static final int BPSize=3;
		public static final int BPHalfSize=BPSize%2!=0?(BPSize+1)/2:BPSize/2;
		public static final int EndKey=11235813;
		public int[] Key=new int[BPSize+1];
		public BPNode[] Child=new BPNode[BPSize+1];
		public BPNode Parent;
		public BPNode Next;
		public int Number;
		public boolean isLeaf;
		public BPNode(int key)
		{
			this.Number=1;
			this.Key[Number]=key;
			this.Key[Number+1]=this.EndKey;
			this.Child[0]=this.Child[1]=null;
			this.Parent=null;
			this.Next=null;
			this.isLeaf=false;
		}
	}
	BPNode Root;
	BPNode Head;
	String InOrderBPTree;
	public void insert(int key)
	{
		if(Root==null)
		{
			Root=new BPNode(key);
			Root.isLeaf=true;
			Head=Root;
			return;
		}
		BPNode c=Root;
		BPNode p=c;
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
			if(p.Number<BPNode.BPSize)
			{
				p.Key[p.Number+1]=BPNode.EndKey;
				return;
			}
			else
			{
				c=new BPNode(0);
				c.Number=BPNode.BPSize-BPNode.BPHalfSize;
				for(int j=0;j<=c.Number;j++)
				{
					c.Child[j]=p.Child[j+BPNode.BPHalfSize];
					if(c.Child[j]!=null)c.Child[j].Parent=c;
				}
				for(int j=1;j<=c.Number;j++)c.Key[j]=p.Key[j+BPNode.BPHalfSize];
				c.Key[c.Number+1]=BPNode.EndKey;
				c.Parent=p.Parent;
				key=p.Key[BPNode.BPHalfSize];
				p.Number=BPNode.BPHalfSize-1;
				if(p.isLeaf)
				{
					c.isLeaf=true;
					p.Number++;
					c.Next=p.Next;
					p.Next=c;
				}
				p.Key[p.Number+1]=BPNode.EndKey;
				if(p.Parent==null)
				{
					Root=new BPNode(key);
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
	public String getInOrderBPTree()
	{
		InOrderBPTree="InOrderBPTree Is:";
		this.displayInOrder(Root);
		return InOrderBPTree;
	}
	private void displayInOrder(BPNode n)
	{
		if(n!=null)
		{
			displayInOrder(n.Child[0]);
			for(int i=1;i<=n.Number;i++)
			{
				InOrderBPTree+=" "+n.Key[i];
				displayInOrder(n.Child[i]);
			}
		}
	}
	public String getLinkedListBPTree()
	{
		String LinkedListBPTree="LinkedListBPTree Is:";
		for(BPNode n=Head;n!=null;n=n.Next)
		{
			for(int i=1;i<=n.Number;i++)LinkedListBPTree+=" "+n.Key[i];
		}
		return LinkedListBPTree;
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
	public String getLevelOrderBPTree()
	{
		String LevelOrderBPTree="LevelOrderBPTree Is:";
		Queue Queue1=new Queue();
		Queue1.enQueue(Root);
		while(Queue1.isNotEmpty())
		{
			BPNode n=(BPNode)Queue1.deQueue();
			if(n.Child[0]!=null)Queue1.enQueue(n.Child[0]);
			for(int i=1;i<=n.Number;i++)
			{
				LevelOrderBPTree+=" "+n.Key[i];
				if(n.Child[i]!=null)Queue1.enQueue(n.Child[i]);
			}
		}
		return LevelOrderBPTree;
	}
	private int getLevel(BPNode n)
	{
		int level=1;
		for(BPNode p=n.Parent;p!=null;p=p.Parent)level++;
		return level;
	}
	private int getHeight(BPNode n)
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
	public void drawBPTree(Graphics g,int x,int y,int size)
	{
		Queue Queue1=new Queue();
		Queue QueueX=new Queue();
		Queue QueueY=new Queue();
		Queue QueueX0=new Queue();
		Queue1.enQueue(Root);
		QueueX.enQueue(x);
		QueueY.enQueue(y);
		int height=this.getHeight(Root);
		int x0=0,y0=0;
		while(Queue1.isNotEmpty())
		{
			BPNode n=(BPNode)Queue1.deQueue();
			int X0=QueueX.dequeue();
			int Y0=QueueY.dequeue();
			if(n.isLeaf)
			{
				QueueX0.enQueue(X0);
				QueueX0.enQueue(X0+n.Number*size);
				y0=Y0+size/2;
			}
			int level=this.getLevel(n);
			int width=BPNode.BPSize*pow(BPNode.BPSize,height-level)*size/2;
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
		boolean notBegin=true;
		while(QueueX0.isNotEmpty())
		{
			int X0=QueueX0.dequeue();
			int dx=size/3;
			int dy=size/5;
			if(notBegin)
			{
				notBegin=false;
				x0=QueueX0.dequeue();
				continue;
			}
			g.drawLine(x0,y0,X0,y0);
			g.drawLine(X0-dx,y0-dy,X0,y0);
			g.drawLine(X0-dx,y0+dy,X0,y0);
			x0=QueueX0.dequeue();
		}
	}
}