import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
public class JavaAndBTree extends Applet
{
	public static void main(String[] args)
	{
		int[] array=new int[]{1,9,7,4,2,6,8,4,11,15,18,13,39,28,66,30,47,65};
		final BTree BTree1=new BTree();
		BTree1.insertArray(array);
		System.out.println(BTree1.getInOrderBTree());
		System.out.println(BTree1.getLevelOrderBTree());
		Frame Frame1=new Frame()
		{
			public void paint(Graphics g)
			{
				BTree1.drawBTree(g,500,150,20);								
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
class IDGenerator
{
	public static final File FILE=new File(BTree.DIR+"BTree.ID");
	public static int next()
	{
		int id=1;
		try
		{
			String ID;
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(FILE));
			ID=BufferedReader1.readLine();
			if(ID==null||ID.equals(""))ID="1";
			id=Integer.parseInt(ID);
			BufferedReader1.close();
			PrintWriter PrintWriter1=new PrintWriter(FILE);
			PrintWriter1.println(id+1);
			PrintWriter1.close();
		}
		catch(Exception e){e.printStackTrace();}
		return id;
	}
}
class BNode implements Serializable
{
	public static final int BSize=3;
	public static final int BHalfSize=BSize%2!=0?(BSize+1)/2:BSize/2;
	public static final int EndKey=11235813;
	public int ID;
	public int Number;
	public int[] Key=new int[BSize+1];
	public int[] ChildID=new int[BSize+1];
	public int ParentID;
	public BNode(int key)
	{
		this.ID=IDGenerator.next();
		this.Number=1;
		this.Key[Number]=key;
		this.Key[Number+1]=this.EndKey;
		this.ChildID[0]=this.ChildID[1]=0;
		this.ParentID=0;
		this.writeToDisk();
	}
	public static BNode readFromDisk(int ID)
	{
		if(ID==0)return null;
		BNode n=null;
		try
		{
			File File1=new File(BTree.DIR+"ID"+ID+".BNode");
			if(File1.exists())
			{
				ObjectInputStream ObjectInputStream1=new ObjectInputStream(new FileInputStream(File1));
				n=(BNode)ObjectInputStream1.readObject();
				ObjectInputStream1.close();
			}
		}
		catch(Exception e){e.printStackTrace();}
		return n;
	}
	public void writeToDisk()
	{
		try
		{
			File File1=new File(BTree.DIR+"ID"+this.ID+".BNode");
			ObjectOutputStream ObjectOutputStream1=new ObjectOutputStream(new FileOutputStream(File1));
			ObjectOutputStream1.writeObject(this);
			ObjectOutputStream1.close();
			
		}
		catch(Exception e){e.printStackTrace();}
	}
	public BNode getChild(int i)
	{
		return this.readFromDisk(this.ChildID[i]);
	}
	public BNode getParent()
	{
		return this.readFromDisk(this.ParentID);
	}
}
class BTree
{
	public static final String DIR="BTree"+File.separator;
	int RootID;
	String InOrderBTree;
	public void insert(int key)
	{
		BNode Root=BNode.readFromDisk(RootID);
		if(Root==null)
		{
			Root=new BNode(key);
			RootID=Root.ID;
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
			c=p.getChild(i-1);
		}
		for(;;)
		{
			for(int j=p.Number;j>=i;j--)
			{
				p.Key[j+1]=p.Key[j];
				p.ChildID[j+1]=p.ChildID[j];
			}
			p.Key[i]=key;
			if(c!=null)p.ChildID[i]=c.ID;
			else p.ChildID[i]=0;
			p.Number++;
			if(p.Number<BNode.BSize)
			{
				p.Key[p.Number+1]=BNode.EndKey;
				p.writeToDisk();
				return;
			}
			else
			{
				c=new BNode(0);
				c.Number=BNode.BSize-BNode.BHalfSize;
				for(int j=0;j<=c.Number;j++)
				{
					c.ChildID[j]=p.ChildID[j+BNode.BHalfSize];
					if(c.ChildID[j]!=0)
					{
						BNode n=c.getChild(j);
						n.ParentID=c.ID;
						n.writeToDisk();
					}
				}
				for(int j=1;j<=c.Number;j++)c.Key[j]=p.Key[j+BNode.BHalfSize];
				c.Key[c.Number+1]=BNode.EndKey;
				c.ParentID=p.ParentID;
				key=p.Key[BNode.BHalfSize];
				p.Number=BNode.BHalfSize-1;
				p.Key[p.Number+1]=BNode.EndKey;
				if(p.getParent()==null)
				{
					Root=new BNode(key);
					RootID=Root.ID;
					Root.ChildID[0]=p.ID;
					p.ParentID=RootID;
					Root.ChildID[1]=c.ID;
					c.ParentID=RootID;
					Root.writeToDisk();
					p.writeToDisk();
					c.writeToDisk();
					return;
				}
				c.writeToDisk();
				p.writeToDisk();
				p=p.getParent();
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
		BNode Root=BNode.readFromDisk(RootID);
		this.displayInOrder(Root);
		return InOrderBTree;
	}
	private void displayInOrder(BNode n)
	{
		if(n!=null)
		{
			displayInOrder(n.getChild(0));
			for(int i=1;i<=n.Number;i++)
			{
				InOrderBTree+=" "+n.Key[i];
				displayInOrder(n.getChild(i));
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
		BNode Root=BNode.readFromDisk(RootID);
		Queue1.enQueue(Root);
		while(Queue1.isNotEmpty())
		{
			BNode n=(BNode)Queue1.deQueue();
			if(n.getChild(0)!=null)Queue1.enQueue(n.getChild(0));
			for(int i=1;i<=n.Number;i++)
			{
				LevelOrderBTree+=" "+n.Key[i];
				if(n.getChild(i)!=null)Queue1.enQueue(n.getChild(i));
			}
		}
		return LevelOrderBTree;
	}
	private int getLevel(BNode n)
	{
		int level=1;
		for(BNode p=n.getParent();p!=null;p=p.getParent())level++;
		return level;
	}
	private int getHeight(BNode n)
	{
		if(n==null)return 0;
		int maxHeightOfChild=0;
		for(int i=0;i<=n.Number;i++)
		{
			int height=this.getHeight(n.getChild(i));
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
		BNode Root=BNode.readFromDisk(RootID);
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
			if(n.getChild(0)!=null)
			{
				Queue1.enQueue(n.getChild(0));
				QueueX.enQueue(X1);
				QueueY.enQueue(Y1);
				g.drawLine(X0,Y0+size,X1,Y1);
			}
			for(int i=1;i<=n.Number;i++)
			{
				g.drawRect(X0,Y0,size,size);
				g.drawString(""+n.Key[i],X0+size/4,Y0+size*3/4);
				X0+=size;
				if(n.getChild(i)!=null)
				{
					Queue1.enQueue(n.getChild(i));
					QueueX.enQueue(X1+i*dx);
					QueueY.enQueue(Y1);
					g.drawLine(X0,Y0+size,X1+i*dx,Y1);

				}
			}
		}
	}
}