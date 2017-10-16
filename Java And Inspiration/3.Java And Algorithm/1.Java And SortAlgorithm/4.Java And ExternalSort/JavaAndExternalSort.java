import java.util.*;
import java.io.*;
public class JavaAndExternalSort
{
	public static void main(String[] args)throws Exception
	{
		ExternalSort ExternalSort1=new ExternalSort();
		ExternalSort1.mergeSort(new File("File0.txt"));
	}
}

class ExternalSort
{
	class OrderedTable
	{
		class Item
		{
			String Data;
			public Item Next;
			public Item(String data)
			{
				Data=data;
				Next=null;
			}
		}
		Item First;
		public OrderedTable(){First=null;}
		public void add(String data)
		{
			Item item=new Item(data);
			if(First==null)First=item;
			else
			{
				if(First.Data.compareTo(data)>=0){item.Next=First;First=item;return;}
				Item i=First;
				for(;i.Next!=null&&i.Next.Data.compareTo(data)<0;i=i.Next);
				item.Next=i.Next;
				i.Next=item;
			}
		}
		public void display()
		{
			for(Item i=First;i!=null;i=i.Next)System.out.println(i.Data);		
		}
		public void printWriteAllWith(PrintWriter printWriter)throws Exception
		{
			for(Item i=First;i!=null;i=i.Next)printWriter.println(i.Data);		
		}
	}
	class JScanner
	{
		Scanner Jscanner;
		int Count;
		String Next;
		public JScanner(Scanner scanner,int length)
		{
			Jscanner=scanner;
			Count=length;
			if(Jscanner.hasNext())Next=Jscanner.nextLine();
			else Count=0;
		}
		public boolean hasNext(){return (Count>0);}
		public String nextLine()
		{
			String s=Next;
			Count--;
			if(Count>0)
			{
				if(Jscanner.hasNext())Next=Jscanner.nextLine();
				else Count=0;
			}
			return s;
		}
		public String peek(){return Next;}
	}
	int Size=3;
	private void split(File File0,File[] File1)throws Exception
	{
		Scanner Scanner0=new Scanner(new FileInputStream(File0));
		PrintWriter[] PrintWriter1={new PrintWriter(File1[0]),new PrintWriter(File1[1])};
		int i=0;
		OrderedTable OrderedTable1;
		while(Scanner0.hasNext())
		{
			OrderedTable1=new OrderedTable();
			for(int j=0;Scanner0.hasNext()&&j<Size;j++)OrderedTable1.add(Scanner0.nextLine());
			OrderedTable1.printWriteAllWith(PrintWriter1[i]);
			i=1-i;
		}
		PrintWriter1[0].close();
		PrintWriter1[1].close();
	}
	private boolean merge(File[] File0,File[] File1,int length)throws Exception
	{
		Scanner[] Scanner0={new Scanner(new FileInputStream(File0[0])),new Scanner(new FileInputStream(File0[1]))};
		PrintWriter[] PrintWriter1={new PrintWriter(File1[0]),new PrintWriter(File1[1])};
		int i=0;
		boolean mergeOnce=false;
		while(Scanner0[0].hasNext()||Scanner0[1].hasNext())
		{
			if(i==1)mergeOnce=true;
			JScanner[] JScanner0={new JScanner(Scanner0[0],length),new JScanner(Scanner0[1],length)};
			while(JScanner0[0].hasNext()||JScanner0[1].hasNext())
			{
				if(!JScanner0[1].hasNext()||(JScanner0[0].hasNext()&&JScanner0[0].peek().compareTo(JScanner0[1].peek())<0))PrintWriter1[i].println(JScanner0[0].nextLine());
				else PrintWriter1[i].println(JScanner0[1].nextLine());
			}
			i=1-i;
		}
		PrintWriter1[0].close();
		PrintWriter1[1].close();
		return mergeOnce;
	}
	public void mergeSort(File File0)throws Exception
	{
                  
		File[][] Files={{new File("Files00.txt"),new File("Files01.txt")},{new File("Files10.txt"),new File("Files11.txt")}};
		this.split(File0,Files[0]);
		int i=0,length=Size;
		while(this.merge(Files[i],Files[1-i],length)){i=1-i;length*=2;}
		Files[0][1].delete();
		Files[1][0].delete();
		Files[1][1].delete();   
	}
}

