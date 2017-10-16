public class JavaAndStringList
{
	public static void main(String[] args)
	{
		StringList StringList1=new StringList();
		int length=100;
		for(int i=0;i<length;i++)
		{
			double r=i;//+100*Math.random();
			StringList1.insert(r);
		}
		System.out.println("Length="+StringList1.length());
		for(int i=0;i<StringList1.length();i++)System.out.println(i+" : "+StringList1.doubleAt(i));
		StringList1.delete(50);
		for(int i=0;i<StringList1.length();i++)System.out.println(i+" : "+StringList1.doubleAt(i));
	}
}
class StringList
{
	private int length;
	private String indexList;
	private String stringList;
	public StringList()
	{
		this.indexList="";
		this.stringList="";
		this.length=0;
	}
	public void add(String string)
	{
		this.indexList+=this.stringList.length()+";";
		this.stringList+=string+";";
		this.length++;
	}
	public void add(int integer)
	{
		this.stringList+=integer+";";
		this.length++;
	}
	public void insert(int integer)
	{
		if(length==0)
		{
			this.stringList+=integer+";";
			this.length++;
			return;
		}
		int i=0,j,m;
		this.length++;
		int Length=stringList.length();
		do
		{
			j=i;
			String s="";
			char c=stringList.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringList.charAt(i++);
			}
			m=Integer.parseInt(s);
		}
		while(integer>=m&&i<Length);
		if(integer>=m){this.stringList+=integer+";";return;}
		String s0=stringList.substring(0,j);
		String s1=stringList.substring(j,Length);
		this.stringList=s0+integer+";"+s1;
	}
	public void insert(int integer,int integer1)
	{
		if(length==0)
		{
			this.stringList+=integer+";"+integer1+";";
			this.length+=2;
			return;
		}
		int i=0,j,m;
		this.length+=2;
		int Length=stringList.length();
		do
		{
			j=i;
			String s="";
			char c=stringList.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringList.charAt(i++);
			}
			m=Integer.parseInt(s);
			c=stringList.charAt(i++);
			while(c!=';')c=stringList.charAt(i++);
		}
		while(integer>=m&&i<Length);
		if(integer>=m){this.stringList+=integer+";"+integer1+";";return;}
		String s0=stringList.substring(0,j);
		String s1=stringList.substring(j,Length);
		this.stringList=s0+integer+";"+integer1+";"+s1;
	}
	public String getString(int index)
	{
		if(index>=length)return "null";
		String s="";
		int Length=stringList.length();
		for(int i=this.getStartIndex(index);i<Length;i++)
		{
			char c=stringList.charAt(i);
			if(c==';')break;
			s+=c;
		}
		return s;
	}
	public int intAt(int index)
	{
		if(index>=length)return 0;
		int Length=stringList.length();
		int i=0,l=0;
		for(;i<Length&&l<index;i++)
		{
			char c=stringList.charAt(i);
			if(c==';')l++;
		}
		char c;
		String s="";
		for(;i<Length;i++,s+=c)
		{
			c=stringList.charAt(i);
			if(c==';')break;
		}
		return Integer.parseInt(s);
	}
	public double doubleAt(int index)
	{
		if(index>=length)return 0;
		int Length=stringList.length();
		int i=0,l=0;
		for(;i<Length&&l<index;i++)
		{
			char c=stringList.charAt(i);
			if(c==';')l++;
		}
		char c;
		String s="";
		for(;i<Length;i++,s+=c)
		{
			c=stringList.charAt(i);
			if(c==';')break;
		}
		return Double.parseDouble(s);
	}
	public double[] toDoubleArray()
	{
		String s="";
		int n=0;
		double[] array=new double[length];
		for(int i=0;i<length;i++)
		{
			char c=stringList.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringList.charAt(n++);
			}
			array[i]=Double.parseDouble(s);
			s="";
		}
		return array;
	}
	private int getStartIndex(int index)
	{
		int i=0,j=0,l=0,Length=indexList.length();
		if(Length<stringList.length())
		{
			for(i=0;i<Length&&l<index;i++)
			{
				char c=indexList.charAt(i);
				if(c==';')l++;
			}
			String s="";
			for(j=i;j<Length;j++)
			{
				char c=indexList.charAt(j);
				if(c==';')break;
				s+=c;
			}
			Length=stringList.length();
			return Integer.parseInt(s);
		}
		Length=stringList.length();
		for(i=0;i<Length&&l<index;i++)
		{
			char c=stringList.charAt(i);
			if(c==';')l++;
		}
		return i;
	}
	public void insert(double real)
	{
		if(length==0)
		{
			this.stringList+=real+";";
			this.length++;
			return;
		}
		int i=0,j;
		double m;
		this.length++;
		int Length=stringList.length();
		do
		{
			j=i;
			String s="";
			char c=stringList.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringList.charAt(i++);
			}
			m=Double.parseDouble(s);
		}
		while(real>=m&&i<Length);
		if(real>=m){this.stringList+=real+";";return;}
		String s0=stringList.substring(0,j);
		String s1=stringList.substring(j,Length);
		this.stringList=s0+real+";"+s1;
	}
	public void insert(double real,double real1)
	{
		if(length==0)
		{
			this.stringList+=real+";"+real1+";";
			this.length+=2;
			return;
		}
		int i=0,j;
		double m;
		this.length+=2;
		int Length=stringList.length();
		do
		{
			j=i;
			String s="";
			char c=stringList.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringList.charAt(i++);
			}
			m=Double.parseDouble(s);
			c=stringList.charAt(i++);
			while(c!=';')c=stringList.charAt(i++);
		}
		while(real>=m&&i<Length);
		if(real>=m){this.stringList+=real+";"+real1+";";return;}
		String s0=stringList.substring(0,j);
		String s1=stringList.substring(j,Length);
		this.stringList=s0+real+";"+real1+";"+s1;
	}
	public void set(int index,String string)
	{
		if(index>=length)return;
		int i=0,j=0,l=0,Length=stringList.length();
		for(i=0;i<Length;i++)
		{
			char c=stringList.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringList.charAt(j);
			if(c==';')break;
		}
		String s0=stringList.substring(0,i);
		String s1=stringList.substring(j,Length);
		this.stringList=s0+";"+string+s1;
	}
	public void delete(int index)
	{
		if(index>=length)return;
		this.length--;
		int i=0,j=0,l=0,Length=stringList.length();
		for(i=0;i<Length;i++)
		{
			char c=stringList.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringList.charAt(j);
			if(c==';')break;
		}
		String s0=stringList.substring(0,i);
		String s1=stringList.substring(j,Length);
		this.stringList=s0+s1;
	}
	public boolean isNotEmpty()
	{
		return this.length>0;
	}
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println("Length="+indexList.length()+":"+indexList);
		System.out.println("Length="+stringList.length()+":"+stringList);
	}
}

class StringQueue
{
	private String stringQueue;
	private int length;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public StringQueue(String queue)
	{
		this.stringQueue=queue;
		this.length=this.getLength();
		if(length==0)
		{
			this.stringQueue+=";";
			this.length=1;
		}
	}
	public void enQueue(char character)
	{
		this.stringQueue+=character;
		this.length++;
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
		this.length++;
	}
	public void enQueue(int[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)this.stringQueue+=array[i]+";";
		this.length+=l;
	}
	public void insert(String string,int index)
	{
		this.length++;
		if(index>=length)
		{
			this.stringQueue+=string+";";
			return;
		}
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(i,Length);
		this.stringQueue=s0+";"+string+s1;
	}
	public void set(int index,String string)
	{
		if(index>=length)return;
		int i=0,j=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			if(c==';')break;
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+";"+string+s1;
	}
	public String deQueue()
	{
		String string="";
		if(stringQueue.length()==0)return string;
		int n=0;
		char c=stringQueue.charAt(n++);
		while(c!=';')
		{
			string+=c;
			c=stringQueue.charAt(n++);
		}
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		this.length--;
		return string;
	}
	private int getLength()
	{
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public String getStringQueue()
	{
		String queue="";
		queue+=stringQueue;
		return queue;
	}
	public void enStringQueue(StringQueue queue)
	{
		this.stringQueue+=queue.getStringQueue();
		this.length+=queue.length();
	}
	public StringQueue deStringQueue(int begin,int end)
	{
		int i=0,j=0,l=0,Length=stringQueue.length();
		String queue="";
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==begin)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			queue+=c;
			if(c==';')
			{
				l++;
				if(l>end)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+s1;
		this.length-=(end-begin+1);
		return new StringQueue(queue);
	}
	public String[] getStrings()
	{
		int l=this.length();
		String[] strings=new String[l];
		int n=0,i=0;
		String s="";
		char c;
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			strings[n++]=s;
			s="";
		}
		return strings;
	}
	public char[] toCharArray()
	{
		char[] array=new char[length];
		for(int i=0;i<length;i++)array[i]=stringQueue.charAt(i);
		return array;
	}
	public int[] toIntArray()
	{
		String s="";
		int n=0;
		int[] array=new int[length];
		for(int i=0;i<length;i++)
		{
			char c=stringQueue.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(n++);
			}
			array[i]=Integer.parseInt(s);
			s="";
		}
		return array;
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}
