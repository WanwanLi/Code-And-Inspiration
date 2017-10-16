import java.io.*;
public class JavaAndDecisionTree
{
	public static void main(String[] args)
	{
		test0();

	}
	public static void test0()
	{
		DecisionTree DecisionTree1=new DecisionTree();
		DecisionTree1.add("A",new int[]{1,2,1,3});
		DecisionTree1.add("B",new int[]{1,2,1,1});
		DecisionTree1.add("C",new int[]{2,1,3,1});
		DecisionTree1.add("D",new int[]{4,1,2,1});
		System.out.println("The Decision Tree (DFS) is :");
		DecisionTree1.println();
		System.out.print("The Test Result {1,2,1,1} is :");
		System.out.print(DecisionTree1.get(new int[]{1,2,1,1}));
	}
	public static void test1()
	{
		DecisionTree DecisionTree1=new DecisionTree();
		//DecisionTree1.genTest("Test1.data");
		DecisionTree1.train("Train.data");
		System.out.println("The Decision Tree (DFS) is :");
		DecisionTree1.println();
		System.out.println("The Test Results Are :");
		double missRate=100.0-DecisionTree1.test("Test3.data");
		System.out.println("The Miss Rate is :"+missRate+"%");
	}
}
class Node
{
	private int Size=30;
	public int length;
	public String value;
	public Node[] child;
	public int[] attributes;
	public Node(String value)
	{
		this.length=0;
		this.value=value;
		this.child=new Node[Size];
		this.attributes=new int[Size];
	}
	public Node addChild(int attribute)
	{
		if(this.child[attribute]==null)
		{
			this.child[attribute]=new Node("null");
			this.attributes[this.length++]=attribute;
		}
		return this.child[attribute];
	}
	public int getAttribute()
	{
		int k=(int)(Math.random()*this.length-1);
		return this.attributes[k];
	}
	public void println()
	{
		if(this.length==0)
		{
			Node n=this.child[0];
			if(n!=null)System.out.println(n.value);
		}
		else
		{
			for(int i=0;i<this.length;i++)
			{
				int attribute=this.attributes[i];
				System.out.print(attribute+" ");
				this.child[attribute].println();
			}
		}
	}
}
class DecisionTree
{
	private Node root;
	public DecisionTree()
	{
		this.root=new Node("null");
	}
	public void add(String value, int[] attributes)
	{
		Node n=this.root;
		for(int i=0;i<attributes.length;i++)
		{
			n=n.addChild(attributes[i]);
		}
		n.child[0]=new Node(value);
	}
	public String get(int[] attributes)
	{
		Node n=this.root;
		for(int i=0;i<attributes.length;i++)
		{
			int attribute=attributes[i];
			if(n.child[attribute]==null)
			{
				attribute=n.getAttribute();
			}
			n=n.child[attribute];
		}
		if(n.child[0]!=null)n=n.child[0];
		return n.value;
	}
	public void println()
	{
		this.root.println();
	}
	public void add(String[] trainData)
	{
		int[] attributes=new int[trainData.length-1];
		for(int i=0;i<attributes.length;i++)attributes[i]=Integer.parseInt(trainData[i+1]);
		this.add(trainData[0], attributes);
	}
	public String get(String[] testData)
	{
		int[] attributes=new int[testData.length-1];
		for(int i=0;i<attributes.length;i++)attributes[i]=Integer.parseInt(testData[i+1]);
		return this.get(attributes);
	}
	public void train(String fileName)
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName)); 
			String line=BufferedReader1.readLine();
			while(line!=null)
			{
				this.add(line.split(","));
				line=BufferedReader1.readLine();
			}
			BufferedReader1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public double test(String fileName)
	{
		try
		{
			int n=0,c=0;
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName)); 
			String line=BufferedReader1.readLine();n++;
			while(line!=null)
			{
				String[] testData=line.split(",");
				String value=this.get(testData);
				System.out.println(value);
				if(value.equals(testData[0]))c++;
				else System.out.println(".... MISS");
				line=BufferedReader1.readLine();n++;
			}
			BufferedReader1.close();
			return 100.0*c/n;
		}
		catch(Exception e){e.printStackTrace();}
		return 100.0;
	}
	public double genTest(String fileName)
	{
		try
		{
			int n=0,c=0;
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName)); 
			String line=BufferedReader1.readLine();
			String[] lines=new String[5000];
			while(line!=null)
			{
				lines[n++]=line;
				line=BufferedReader1.readLine();

			}
			BufferedReader1.close();
			for(int i=0;i<n;i++)
			{
				String str=lines[(int)(Math.random()*(n-1))];
				System.out.println(str);
			}
		}
		catch(Exception e){e.printStackTrace();}
		return 100.0;
	}
}
