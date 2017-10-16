import java.awt.*;
import java.awt.event.*;

public class JavaAndAVLTree
{
	public static void main(String[] args)
	{
		double[] array=new double[]{10,4,3,8,6,2,5,7,14,12,1,30,16,100};
		final AVLTree AVLTree1=new AVLTree();
		AVLTree1.insertArray(array);
		System.out.println(AVLTree1.getInOrderAVLTree());
		AVLTree1.delete(8); System.out.println("Delete 8");
		System.out.println(AVLTree1.getInOrderAVLTree());
		AVLTree1.delete(7); System.out.println("Delete 7");
		System.out.println(AVLTree1.getInOrderAVLTree());
		Frame Frame1=new Frame()
		{
			public void paint(Graphics g)
			{
				AVLTree1.drawAVLTree(g, 600, 100, 20);							
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
	public int height;
	public double data;
	public Node leftChild;
	public Node rightChild;
	public Node(double data)
	{
		this.height=0;
		this.data=data;
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
	public void drawNode(Graphics g, int x, int y, int size)
	{
		String data=""+this.data+"("+this.height+")";
		int length=data.length()*size/3;
		g.drawRect(x, y, length, size);
		g.drawString(data, x+size/4, y+size*3/4);
		int dx=(int)(Width*size*Math.pow(Size, height));
		int dy=4*size, y1=y+dy; y+=size;		
		if(leftChild!=null)
		{
			int x1=x-dx;
			g.drawLine(x, y, x1+length/2, y1);
			leftChild.drawNode(g, x1, y1, size);
		}
		if(rightChild!=null)
		{
			int x1=x+dx;
			g.drawLine(x+length, y, x1+length/2, y1);
			rightChild.drawNode(g, x1, y1, size);
		}
	}
	public double Size=2, Width=0.5;
}
class AVLTree
{
	Node root;
	public AVLTree()
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
			if(node.isUnbalanced())
			{
				if(data<node.leftChild.data)node=rotateLeftChild(node);
				else node=rotateLeftChildWithRightChild(node);
			}
		}
		else if(data>node.data)
		{
			node.rightChild=insert(node.rightChild, data);
			if(node.isUnbalanced())
			{
				if(data>node.rightChild.data)node=rotateRightChild(node);
				else node=rotateRightChildWithLeftChild(node);
			}
		}
		node.updateHeight();
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
		if(node!=null)node.updateHeight();
		return node;
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
	String inOrderAVLTree;
	public String getInOrderAVLTree()
	{
		this.inOrderAVLTree="InOrderAVLTree Is:";
		this.getInOrderAVLTree(root);
		return inOrderAVLTree;
	}
	void getInOrderAVLTree(Node node)
	{
		if(node!=null)
		{
			this.getInOrderAVLTree(node.leftChild);
			this.inOrderAVLTree+=" "+node.data;
			this.getInOrderAVLTree(node.rightChild);
		}
	}
	public void drawAVLTree(Graphics g, int x, int y, int size)
	{
		this.root.drawNode(g, x, y, size);
	}
}