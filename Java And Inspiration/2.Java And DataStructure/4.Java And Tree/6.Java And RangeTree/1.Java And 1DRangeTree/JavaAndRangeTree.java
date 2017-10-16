import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class JavaAndRangeTree
{
	public static void main(String[] args)
	{
		double[] data=new double[]{10,4,3,8,6,2,5,7,14,12,1,30,16,100};
		final RangeTree RangeTree1=new RangeTree(data);
		System.out.println(RangeTree1.getInOrderRangeTree());
		Integer[] result=RangeTree1.search(5, 15);
		System.out.print("Range search result is:");
		for(Integer index : result)
		{
			int i=index.intValue();
			System.out.print(" "+data[i]+"("+i+")");
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
	public void drawNode(Graphics g, int x, int y, int size, double[] data)
	{
		String string=""+data[index]+"("+height+")";
		if(isLeaf)string=""+data[index]+"["+index+"]";
		int length=string.length()*size/3;
		if(!isLeaf)g.drawRect(x, y, length, size);
		else this.drawCircle(g, x, y, length, size);
		g.drawString(string, x+size/4, y+size*3/4);
		int dx=getWidth(size, 0.8, 2), dy=4*size;
		int y1=y+dy; y+=size;		
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
	void drawCircle(Graphics g, int x, int y, int width, int height)
	{
		((Graphics2D)g).draw(new Ellipse2D.Double(x, y, width, height));
	}
	int getWidth(int size, double scale, double spray)
	{
		return (int)(scale*size*Math.pow(spray, height));
	}
}
class RangeTree
{
	Node root;
	double[] data;
	String inOrderRangeTree;
	LinkedList<Integer> rangeSearchList;
	public RangeTree(double[] data)
	{
		this.root=null;
		this.data=data;
		for(int i=0; i<data.length; i++)
		{
			this.root=insert(root, i);
		}
		this.generateLeaves(root);
	}
	public Integer[] search(double min, double max)
	{
		this.rangeSearchList=new LinkedList<Integer>();
		this.search(root, min, max);
		Integer[] result=new Integer[rangeSearchList.size()];
		return rangeSearchList.toArray(result);
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
		else if(data[index]<data(node))
		{
			node.leftChild=insert(node.leftChild, index);
			if(node.isUnbalanced())
			{
				if(data[index]<data(node.leftChild))node=rotateLeftChild(node);
				else node=rotateLeftChildWithRightChild(node);
			}
		}
		else if(data[index]>data(node))
		{
			node.rightChild=insert(node.rightChild, index);
			if(node.isUnbalanced())
			{
				if(data[index]>data(node.rightChild))node=rotateRightChild(node);
				else node=rotateRightChildWithLeftChild(node);
			}
		}
		node.updateHeight();
		return node;
	}
	void generateLeaves(Node node)
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
			this.generateLeaves(node.leftChild);
		}
		else if(node.rightChild!=null)
		{
			node.leftChild=new Node(node.index);
			node.leftChild.isLeaf=true;
		}
		else node.isLeaf=true;
		if(node.rightChild!=null)this.generateLeaves(node.rightChild);
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
				this.inOrderRangeTree+=" "+data(node);
				this.inOrderRangeTree+="("+node.index+")";
			}
			this.getInOrderRangeTree(node.rightChild);
		}
	}
	double data(Node node)
	{
		return this.data[node.index];
	}
	public void drawRangeTree(Graphics g, int x, int y, int size)
	{
		this.root.drawNode(g, x, y, size, data);
	}
}