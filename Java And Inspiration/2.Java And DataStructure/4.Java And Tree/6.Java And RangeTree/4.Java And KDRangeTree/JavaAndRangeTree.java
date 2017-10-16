import java.util.*;

public class JavaAndRangeTree
{
	static Scanner scanner=new Scanner(System.in);
	public static void main(String[] args)
	{
		String input=scanner.nextLine();
		double[][] data=new double[Integer.parseInt(input)][3];
		for(int i=0; i<data.length; i++)
		{
			String[] values=scanner.nextLine().split("\t");
			for(int j=0; j<values.length; j++)
			{
				data[i][j]=Double.parseDouble(values[j]);
			}
		}
		System.out.printf("Read in %d points\n", data.length);
		RangeTree RangeTree1=new RangeTree(data);
		System.out.println("Sorted points along X axis:");
		System.out.println(RangeTree1.getInOrderRangeTree());
		for(input=scanner.nextLine(); !input.equals("exit"); input=scanner.nextLine())
		{
			RangeTree1.clearHashTable(); 
			double[] values=parseDoubles(input.split(" "));
			double[] min={values[0], values[2], values[4]};
			double[] max={values[1], values[3], values[5]};
			System.out.printf("min: [%f, %f, %f]", min[0], min[1], min[2]);
			System.out.printf("max: [%f, %f, %f]", max[0], max[1], max[2]);
			LinkedList<Integer> result=RangeTree1.search(min, max);
			System.out.printf("Found %d points:\n", result.size());
			for(Integer index : result)
			{
				int i=index.intValue();
				String value=" {"+data[i][RangeTree.X];
				value+=", "+data[i][RangeTree.Y];
				value+=", "+data[i][RangeTree.Z];
				System.out.print(value+"}("+i+")\n");
			}
		}
	}
	static double[] parseDoubles(String[] strings)
	{
		int length=strings.length;
		double[] doubles=new double[length];
		for(int i=0; i<length; i++)
		{
			doubles[i]=Double.parseDouble(strings[i]);
		}
		swap(doubles, 0, 1); 
		swap(doubles, 2, 3);
		swap(doubles, 4, 5);
		return doubles;
	}
	static void swap(double[] doubles, int i, int j)
	{
		double a=doubles[i], b=doubles[j], t;
		if(doubles[i]>doubles[j]){t=a; a=b; b=t;}
		doubles[i]=a; doubles[j]=b;
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
	public LinkedList<Integer> search(double[] min, double[] max)
	{
		LinkedList<Integer> indices=tree.search(min[Z], max[Z]);
		LinkedList<Integer> list=new LinkedList<Integer>();
		for(Integer i : indices)
		{
			if(tree.isValidIndex(i, min, max))list.add(i);
		}
		return list;
	}
	public static final int X=0, Y=1, Z=2;
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
		this.createTrees(Y);
	}
	public RangeTree(double[][] data, boolean[] hashTable, Integer[] indices, int dim)
	{
		this.dim=dim;
		this.root=null;
		this.data=data;
		this.hashTable=hashTable;
		for(Integer i : indices)
		{
			this.root=insert(root, i.intValue());
		}
		this.createLeaves(root);
		if(dim==Y)this.createTrees(Z);
	}
	public LinkedList<Integer> search(double min, double max)
	{
		this.rangeSearchList=new LinkedList<Integer>();
		this.search(root, min, max);
		return rangeSearchList;
	}
	public LinkedList<Integer> search(double[] min, double[] max)
	{
		this.rangeSearchList=new LinkedList<Integer>();
		this.search(root, min, max);
		return rangeSearchList;
	}
	void search(Node node, double[] min, double[] max)
	{
		if(node==null||node.isLeaf)return;
		else if(data(node)>=max[dim])
		{
			this.search(node.leftChild, min, max);
		}
		else if(data(node)<min[dim])
		{
			this.search(node.rightChild, min, max);
		}
		else 
		{
			if(dim==X)this.rangeSearchList.addAll(node.tree.search(min, max));
			else if(dim==Y)this.rangeSearchList.addAll(node.search(min, max));
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
	public void createTrees(int dim)
	{
		this.createTrees(root, dim);
	}
	void createTrees(Node node, int dim)
	{
		if(node==null||node.isLeaf)return;
		Integer[] leaves=this.getLeavesArray(node);
		node.tree=new RangeTree(data, hashTable, leaves, dim);
		this.createTrees(node.leftChild, dim);
		this.createTrees(node.rightChild, dim);
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
				this.inOrderRangeTree+=", "+data[node.index][Z];
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
	public void clearHashTable()
	{
		for(int i=0; i<hashTable.length; i++)
		{
			this.hashTable[i]=false;
		}
	}
	public boolean isValidIndex(Integer index, double[] min, double[] max)
	{
		int i=index.intValue();
		if(isInRange(i, Y, min, max)&&!hashTable[i])
		{
			this.hashTable[i]=true;
			return true;
		}
		return false;
	}
	boolean isInRange(int i, int dim, double[] min, double[] max)
	{
		for(int d=0; d<=dim; d++)
		{
			if(data[i][d]<min[d]||data[i][d]>max[d])return false;
		}
		return true;
	}
	public static final int X=0, Y=1, Z=2;
}