public class JavaAndHashSort
{
	public static void main(String[] args)
	{
		int l=10;
		int[] array=new int[l];
		array=new int[]{3,3,5,70050,3,10000,9,2,3};
		HashSort HashSort1=new HashSort(array);
		HashSort1.show(array);
		System.out.println("______________________________________");
		HashSort1.display();
	}
}
class HashSort
{
	private int[] array;
	private int[] index;
	public HashSort(int[] array)
	{
		int min=Integer.MAX_VALUE;
		int max=-Integer.MAX_VALUE;
		int l=array.length;
		this.array=new int[l];
		for(int i=0;i<l;i++)
		{
			if(array[i]>max)max=array[i];
			if(array[i]<min)min=array[i];
		}
		int length=max-min+1;
		int[] HashTable=new int[length];
		StringQueue[] IndexTable=new StringQueue[length];
		for(int i=0;i<l;i++)
		{
			HashTable[array[i]-min]=array[i];
			if(IndexTable[array[i]-min]==null)IndexTable[array[i]-min]=new StringQueue();
			IndexTable[array[i]-min].enQueue(i);
		}
		int n=0;
		this.array=new int[l];
		this.index=new int[l];
		for(int i=0;i<length;i++)
		{
			if(IndexTable[i]!=null)
			{
				int[] indices=IndexTable[i].toIntArray();
				for(int j=0;j<indices.length;j++)
				{
					this.array[n]=HashTable[i];
					this.index[n++]=indices[j];
				}
			}
		}
	}
	public int[] getArray()
	{
		return this.array;
	}
	public int[] getIndex()
	{
		return this.index;
	}
	public void show(int[] array)
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+i+"]="+array[i]);
	}
	public void display()
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+index[i]+"]="+array[i]);
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
	public void enQueue(int a)
	{
		this.stringQueue+=a+";";
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
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println(stringQueue);
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