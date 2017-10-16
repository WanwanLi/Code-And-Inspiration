public class JavaAndGeneralList
{
	public static void main(String[] args)
	{
		new GeneralList("(a,(b,(c,(d,e))))");
	}
}
class GeneralList
{
	class GNode
	{
		public boolean DataIsGNode;
		public GNode Data;
		public char data;
		public GNode Next;
		public GNode()
		{
			this.DataIsGNode=false;
			this.Data=null;
			this.data='.';
			this.Next=null;
		}
	}
	int number;
	GNode head;
	String string;
	String generalList;
	public GeneralList(String expression)
	{
		this.string=expression;
		System.out.println(string);
		int i=0;
		head=this.createGeneralList(i);
		this.generalList="General List:";
		this.displayGeneralList(head);
		System.out.println(generalList);
	}	
	private GNode createGeneralList(int i)
	{
		GNode g;
		char c=string.charAt(i++);
		if(c!='\0')
		{
			g=new GNode();
			if(c=='(')
			{
				g.DataIsGNode=true;
				g.Data=this.createGeneralList(i);
			}
			else if(c==')')g=null;
			else
			{
				g.DataIsGNode=false;
				g.data=c;
			}
		}
		else g=null;
		c=string.charAt(i++);
		if(g!=null)
		{	
			if(c==',')g.Next=this.createGeneralList(i);
			else g.Next=null;
		}
		return g;
	}
	private void displayGeneralList(GNode g)
	{
		if(g!=null)
		{
			if(g.DataIsGNode)
			{
				generalList+="(";
				this.displayGeneralList(g.Data);
			}
			else generalList+=g.data;
			if(g.Next!=null)
			{
				generalList+=",";
				this.displayGeneralList(g.Next);
			}
			if(g.DataIsGNode)generalList+=")";
		}
		else generalList+="";
	}
}






