import java.awt.*;
import java.awt.event.*;

public class JavaAndBSTree
{
	public static void main(String[] args)
	{
		double[] array=new double[]{10,4,3,8,6,2,5,7,14,12,1,30,16};
		final BSTree BSTree1=new BSTree();
		BSTree1.insertArray(array);
		System.out.println(BSTree1.getInOrderBSTree());
		BSTree1.delete(8);System.out.println("Delete 8");
		System.out.println(BSTree1.getInOrderBSTree());
		BSTree1.delete(7);System.out.println("Delete 7");
		System.out.println(BSTree1.getInOrderBSTree());
		Frame Frame1=new Frame()
		{
			public void paint(Graphics g)
			{
				BSTree1.drawBSTree(g, 600, 100, 20);								
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
	public double data;
	public Node leftChild;
	public Node rightChild;
	public Node(double data)
	{
		this.data=data;
		this.leftChild=null;
		this.rightChild=null;
	}
	public void drawNode(Graphics g, int x, int y, int size, int height)
	{
		String data=""+this.data;
		int length=data.length()*size/2;
		g.drawRect(x, y, length, size);
		g.drawString(data, x+size/4, y+size*3/4);
		int dx=(int)(Width*size*Math.pow(Size, height));
		int dy=4*size, y1=y+dy; y+=size;		
		if(leftChild!=null)
		{
			int x1=x-dx;
			g.drawLine(x, y, x1+length/2, y1);
			leftChild.drawNode(g, x1, y1, size, height-1);
		}
		if(rightChild!=null)
		{
			int x1=x+dx;
			g.drawLine(x+length, y, x1+length/2, y1);
			rightChild.drawNode(g, x1, y1, size, height-1);
		}
	}
	public double Size=2, Width=0.4;
}
class BSTree
{
	Node root;
	public BSTree()
	{
		this.root=null;
	}
	public void insert(double data)
	{
		this.root=insert(root, data);
	}
	public boolean search(double data)
	{
		return search(root, data)==null?false:true;
	}
	public boolean delete(double data)
	{
		if(!search(data))return false;
		this.root=delete(root, data);
		return true;
	}
	public void insertArray(double[] array)
	{
		for(int i=0; i<array.length; i++)this.insert(array[i]);
	}
	Node insert(Node node, double data)
	{
		if(node==null)node=new Node(data);
		else if(data<node.data)
		{
			node.leftChild=insert(node.leftChild, data);
		}
		else if(data>node.data)
		{
			node.rightChild=insert(node.rightChild, data);
		}
		return node;
	}
	Node search(Node node, double data)
	{
		if(node==null)return null;
		else if(data<node.data)
		{
			return search(node.leftChild, data);
		}
		else if(data>node.data)
		{
			return search(node.rightChild, data);
		}
		else return node;
	}
	Node delete(Node node, double data)
	{
		if(node==null)return null;
		else if(data<node.data)
		{
			node.leftChild=delete(node.leftChild, data);
		}
		else if(data>node.data)
		{
			node.rightChild=delete(node.rightChild, data);
		}
		else
		{
			if(node.leftChild!=null&&node.rightChild!=null)
			{
				node.data=getMaxChild(node.leftChild).data;
				node.leftChild=delete(node.leftChild, node.data);
			}
			else node=node.leftChild!=null?node.leftChild:node.rightChild;
		}
		return node;
	}
	Node getMaxChild(Node node)
	{
		if(node==null)return null;
		else if(node.rightChild==null)return node;
		else return getMaxChild(node.rightChild);
	}
	int getHeight(Node node)
	{
		return node==null?0:Math.max(getHeight(node.leftChild), getHeight(node.rightChild))+1;
	}
	String inOrderBSTree;
	public String getInOrderBSTree()
	{
		this.inOrderBSTree="InOrderBSTree Is:";
		this.getInOrderBSTree(root);
		return inOrderBSTree;
	}
	void getInOrderBSTree(Node node)
	{
		if(node!=null)
		{
			this.getInOrderBSTree(node.leftChild);
			this.inOrderBSTree+=" "+node.data;
			this.getInOrderBSTree(node.rightChild);
		}
	}
	public void drawBSTree(Graphics g, int x, int y, int size)
	{
		this.root.drawNode(g, x, y, size, getHeight(root));
	}
}
