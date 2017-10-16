import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class JavaAndRangeTree
{
	public static void main(String[] args)
	{
		double[][] data=
		{
			{10, 15},{4,7},{3,10},{8,21},{6,3},{2,9},{5,14},
			{7,25},{14,13},{12,53},{1,72},{30,28},{100,81}
		};
		double[] min={5, 10}, max={30, 28};
		final RangeTree RangeTree1=new RangeTree(data);
		System.out.println(RangeTree1.getInOrderRangeTree());
		Integer[] result=RangeTree1.search(min, max);
		System.out.print("Range search result is:");
		for(Integer index : result)
		{
			int i=index.intValue();
			System.out.print(" "+data[i][0]+","+data[i][1]+"("+i+")");
		}
		System.out.println();
		Frame Frame1=new Frame()
		{
			public void paint(Graphics g)
			{
				RangeTree1.drawRangeTree(g, 650, 100, 20);							
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
class Node
{
	public Node leftChild;
	public Node rightChild;
	public int height,index;
	public RangeTree tree;
	public boolean isLeaf;
	public Node(int index)
	{
		this.height=0;
		this.index=index;
		this.isLeaf=false;
		this.leftChild=null;
		this.rightChild=null;
	}
	public boolean isUnbalanced()
	{
		return Math.abs(getHeight(leftChild)-getHeight(rightChild))>=2;
	}
	public void updateHeight()
	{
		this.height=Math.max(getHeight(leftChild), getHeight(rightChild))+1;
	}
	int getHeight(Node node)
	{
		return node==null?0:node.height;
	}
	public void drawNode(Graphics g, int x, int y, int size, double[][] data)
	{
		String string=""+data[index][0]+","+data[index][1];
		int length=string.length()*size/3;
		if(!isLeaf)g.drawRect(x, y, length, size);
		else this.drawCircle(g, x, y, length, size);
		g.drawString(string, x+size/4, y+size*3/4);
		int dx=getWidth(size, length, 0.8, 2);
		int dy=4*size, y1=y+dy; y+=size;		
		if(leftChild!=null)
		{
			int x1=x-dx;
			g.drawLine(x, y, x1+length/2, y1);
			leftChild.drawNode(g, x1, y1, size, data);
		}
		if(rightChild!=null)
		{
			int x1=x+dx;
			g.drawLine(x+length, y, x1+length/2, y1);
			rightChild.drawNode(g, x1, y1, size, data);
		}
	}
	public LinkedList<Integer> search(double[] min, double[] max)
	{
		LinkedList<Integer> list=new LinkedList<Integer>();
		Integer[] indices=tree.search(min[Y], max[Y]);
		for(Integer i : indices)
		{
			if(tree.isValidIndex(i, min[X], max[X]))list.add(i);
		}
		return list;
	}
	void drawCircle(Graphics g, int x, int y, int width, int height)
	{
		((Graphics2D)g).draw(new Ellipse2D.Double(x, y, width, height));
	}
	int getWidth(int size, int length, double scale, double spray)
	{
		return (int)(Math.max(length/2, scale*size*Math.pow(spray, height)));
	}
	public static final int X=0, Y=1;
}
class RangeTree
{
	int dim;
	Node root;
	double[][] data;
	boolean[] hashTable;
	String inOrderRangeTree;
	LinkedList<Integer> leavesList;
	LinkedList<Integer> rangeSearchList;
	public RangeTree(double[][] data)
	{
		this.dim=X;
		this.root=null;
		this.data=data;
		this.hashTable=new boolean[data.length];
		for(int i=0; i<data.length; i++)
		{
			this.root=insert(root, i);
		}
		this.createLeaves(root);
		this.createTrees();
	}
	public RangeTree(double[][] data, boolean[] hashTable, Integer[] indices)
	{
		this.dim=Y;
		this.root=null;
		this.data=data;
		for(Integer i : indices)
		{
			this.root=insert(root, i.intValue());
		}
		this.createLeaves(root);
		this.hashTable=hashTable;
	}
	public Integer[] search(double min, double max)
	{
		this.rangeSearchList=new LinkedList<Integer>();
		this.search(root, min, max);
		Integer[] result=new Integer[rangeSearchList.size()];
		return rangeSearchList.toArray(result);
	}
	public Integer[] search(double[] min, double[] max)
	{
		this.rangeSearchList=new LinkedList<Integer>();
		this.clearHashTable(); this.search(root, min, max);
		Integer[] result=new Integer[rangeSearchList.size()];
		return rangeSearchList.toArray(result);
	}
	void search(Node node, double[] min, double[] max)
	{
		if(node==null||node.isLeaf)return;
		else if(data(node)>=max[X])
		{
			this.search(node.leftChild, min, max);
		}
		else if(data(node)<min[X])
		{
			this.search(node.rightChild, min, max);
		}
		else 
		{
			this.rangeSearchList.addAll(node.search(min, max));
			this.search(node.leftChild, min, max);
			this.search(node.rightChild, min, max);
		}
	}
	void search(Node node, double min, double max)
	{
		if(node==null)return;
		else if(node.isLeaf&&data(node)<=max)
		{
			this.rangeSearchList.add(node.index);
		}
		else if(data(node)>=max)
		{
			this.search(node.leftChild, min, max);
		}
		else if(data(node)<min)
		{
			this.search(node.rightChild, min, max);
		}
		else 
		{
			this.search(node.leftChild, min, max);
			this.search(node.rightChild, min, max);
		}
	}
	Node insert(Node node, int index)
	{
		if(node==null)node=new Node(index);
		else if(data(index)<data(node))
		{
			node.leftChild=insert(node.leftChild, index);
			if(node.isUnbalanced())
			{
				if(data(index)<data(node.leftChild))node=rotateLeftChild(node);
				else node=rotateLeftChildWithRightChild(node);
			}
		}
		else if(data(index)>data(node))
		{
			node.rightChild=insert(node.rightChild, index);
			if(node.isUnbalanced())
			{
				if(data(index)>data(node.rightChild))node=rotateRightChild(node);
				else node=rotateRightChildWithLeftChild(node);
			}
		}
		node.updateHeight();
		return node;
	}
	void createLeaves(Node node)
	{
		if(node.isLeaf)return;
		int index=node.index;
		if(node.leftChild!=null)
		{
			Node maxLeftChild=getMaxChild(node.leftChild);
			if(maxLeftChild!=null&&!maxLeftChild.isLeaf)
			{
				maxLeftChild.rightChild=new Node(index);
				maxLeftChild.rightChild.isLeaf=true;
				maxLeftChild.leftChild=new Node(maxLeftChild.index);
				maxLeftChild.leftChild.isLeaf=true;
			}
			this.createLeaves(node.leftChild);
		}
		else if(node.rightChild!=null)
		{
			node.leftChild=new Node(node.index);
			node.leftChild.isLeaf=true;
		}
		else node.isLeaf=true;
		if(node.rightChild!=null)this.createLeaves(node.rightChild);
	}
	Integer[] getLeavesArray(Node node)
	{
		this.leavesList=new LinkedList<Integer>(); this.getLeaves(node);
		Integer[] leaves=new Integer[this.leavesList.size()];
		return this.leavesList.toArray(leaves);
	}
	void  getLeaves(Node node)
	{
		if(node==null)return;
		else if(node.isLeaf)
		{
			this.leavesList.add(node.index);	
		}
		else 
		{
			this.getLeaves(node.leftChild);
			this.getLeaves(node.rightChild);
		}
	}
	public void createTrees()
	{
		this.createTrees(root);
	}
	void createTrees(Node node)
	{
		if(node==null||node.isLeaf)return;
		Integer[] leaves=this.getLeavesArray(node);
		node.tree=new RangeTree(data, hashTable, leaves);
		this.createTrees(node.leftChild);
		this.createTrees(node.rightChild);
	}
	Node rotateLeftChild(Node node)
	{
		Node leftChild=node.leftChild;
		node.leftChild=leftChild.rightChild;
		leftChild.rightChild=node;
		leftChild.updateHeight();
		node.updateHeight();
		return leftChild;
	}
	Node rotateRightChild(Node node)
	{
		Node rightChild=node.rightChild;
		node.rightChild=rightChild.leftChild;
		rightChild.leftChild=node;
		rightChild.updateHeight();
		node.updateHeight();
		return rightChild;
	}
	Node rotateLeftChildWithRightChild(Node node)
	{
		node.leftChild=rotateRightChild(node.leftChild);
		return rotateLeftChild(node);
	}
	Node rotateRightChildWithLeftChild(Node node)
	{
		node.rightChild=rotateLeftChild(node.rightChild);
		return rotateRightChild(node);
	}
	Node getMaxChild(Node node)
	{
		if(node==null)return null;
		else if(node.rightChild==null)return node;
		else return getMaxChild(node.rightChild);
	}
	public String getInOrderRangeTree()
	{
		this.inOrderRangeTree="InOrderRangeTree Is:";
		this.getInOrderRangeTree(root);
		return inOrderRangeTree;
	}
	void getInOrderRangeTree(Node node)
	{
		if(node!=null)
		{
			this.getInOrderRangeTree(node.leftChild);
			if(node.isLeaf)
			{
				this.inOrderRangeTree+=" {"+data[node.index][X];
				this.inOrderRangeTree+=", "+data[node.index][Y];
				this.inOrderRangeTree+="}("+node.index+")";
			}
			this.getInOrderRangeTree(node.rightChild);
		}
	}
	double data(int index)
	{
		return this.data[index][dim];
	}
	double data(Node node)
	{
		return this.data[node.index][dim];
	}
	public void drawRangeTree(Graphics g, int x, int y, int size)
	{
		this.root.drawNode(g, x, y, size, data);
	}
	public void clearHashTable()
	{
		for(int i=0; i<hashTable.length; i++)
		{
			this.hashTable[i]=false;
		}
	}
	public boolean isValidIndex(Integer index, double min, double max)
	{
		int i=index.intValue();
		if(min<=data[i][X]&&data[i][X]<=max&&!hashTable[i])
		{
			this.hashTable[i]=true;
			return true;
		}
		return false;
	}
	public static final int X=0, Y=1;
}