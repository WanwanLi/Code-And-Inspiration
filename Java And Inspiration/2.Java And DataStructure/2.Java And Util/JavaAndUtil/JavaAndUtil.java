public class JavaAndUtil
{
	public static void main(String[] args)
	{
		LinkedList LinkedList1=new LinkedList();
		LinkedList1.add("String0");
		LinkedList1.add("String1");
		LinkedList1.add("String2");
		LinkedList1.add("String3");
		LinkedList1.add("String4");
		LinkedList1.add("String5");
		System.out.println((String)LinkedList1.get(4));
		LinkedList1.remove(3);
		Iterator Iterator1=LinkedList1.iterator();
		while(Iterator1.hasNext())System.out.println((String)Iterator1.next());	
		Iterator1.remove();
		LinkedList1.remove();
		Iterator1=LinkedList1.iterator();
		while(Iterator1.hasNext())System.out.println((String)Iterator1.next());	
		LinkedList1.add(0,"String0");
		System.out.println((String)LinkedList1.get(3));
		System.out.println((String)LinkedList1.remove(3));
		Iterator1=LinkedList1.iterator();
		while(Iterator1.hasNext())System.out.println((String)Iterator1.next());	
		Iterator Iterator2=LinkedList1.lastIterator();		
		while(Iterator2.hasPrevious())System.out.println((String)Iterator2.previous());			
		LinkedList1.add(3,"String3");		
		System.out.println(LinkedList1.size());
		while(LinkedList1.size()>0)System.out.println((String)LinkedList1.pop());
		HashTable HashTable1=new HashTable(11);
		HashTable1.put("Key1","Object1");
		System.out.println("Key1:"+(String)HashTable1.get("Key1"));	
		System.out.println("Remove Key1:"+(String)HashTable1.remove("Key1"));
		System.out.println("Key1:"+(String)HashTable1.get("Key1"));
		TreeSet TreeSet1=new TreeSet();
		TreeSet1.add("Object5");	
		TreeSet1.add("Object3");
		TreeSet1.add("Object1");
		TreeSet1.add("Object4");
		TreeSet1.add("Object2");
		TreeSet1.add("Object0");
		Iterator1=TreeSet1.iterator();
		while(Iterator1.hasNext())System.out.println((String)Iterator1.next());	
		TreeSet1.remove("Object3");
		Iterator1=TreeSet1.iterator();
		while(Iterator1.hasNext())System.out.println((String)Iterator1.next());	
		
	}
}
class Iterator
{
	public Object Data;
	public Iterator Previous;
	public Iterator Next;

	public Iterator(Object object)
	{
		Data=object;
		Previous=null;
		Next=null;
	}
	public boolean hasNext()
	{
		if(this==null)return false;
		else return (Next!=null);
	}
	public boolean hasPrevious()
	{
		if(this==null)return false;
		else return (Previous!=null);
	}	
	public Object next()
	{
		if(this==null||this.Next==null)return null;
		Object object=this.Next.Data;
		this.Next=this.Next.Next;
		return object;
	}
	public Object previous()
	{
		if(this==null||this.Previous==null)return null;
		Object object=this.Previous.Data;
		this.Previous=this.Previous.Previous;
		return object;
	}
	public void remove()
	{
		this.Next=null;
	}
} 
class LinkedList 
{
	Iterator Iterator1;
	Iterator Iterator2;
	int Size;
	public LinkedList()
	{
		Iterator1=Iterator2=null;
		Size=0;
	}
	public void add(Object object)
	{
		Size++;
		Iterator i=new Iterator(object);
		if(Iterator1==null)Iterator1=Iterator2=i;
		else
		{
			Iterator2.Next=i;
			i.Previous=Iterator2;
			Iterator2=i;
		}
	}
	public void add(int index ,Object object)
	{
		Size++;
		Iterator I=new Iterator(object);
		if(index==0)
		{
			I.Next=Iterator1;
			Iterator1.Previous=I;
			Iterator1=I;
		}
		else
		{
			Iterator i=Iterator1;		
			for(int j=0;j<Size&&j<index-1;j++)i=i.Next;
			I.Next=i.Next;
			i.Next=I;			
			I.Previous=i;
			if(I.Next!=null)I.Next.Previous=I;
		}
	}

	public Object remove()
	{		
		if(Iterator1!=null)
		{
			Size--;
			Iterator i=Iterator1;	
			Iterator1=Iterator1.Next;
			if(Iterator1!=null)Iterator1.Previous=null;
			return i.Data;
		}
		else return null;
	}
	public Object remove(int index)
	{
		if(index==0&&Iterator1!=null)
		{
			Size--;
			Iterator i=Iterator1;	
			Iterator1=Iterator1.Next;
			if(Iterator1!=null)Iterator1.Previous=null;
			return i.Data;
		}
		Iterator i=Iterator1;		
		for(int j=0;j<Size&&j<index-1;j++)i=i.Next;
		Iterator I=i.Next;
		if(I!=null)
		{
			i.Next=I.Next;
			if(i.Next!=null)i.Next.Previous=i;
			Size--;
			return I.Data;
		}
		else return null;
	}

	public void push(Object object)
	{
		Size++;
		Iterator i=new Iterator(object);
		i.Next=Iterator1;
		Iterator1.Previous=i;
		Iterator1=i;
	}
	public Object pop()
	{
		if(Iterator1!=null)
		{
			Size--;
			Iterator i=Iterator1;
			Iterator1=Iterator1.Next;
			if(Iterator1!=null)Iterator1.Previous=null;
			return i.Data;
		}
		else return null;
	}
	public Object peek()
	{
		if(Iterator1!=null)return Iterator1.Data;
		else return null;
	}
	
	public Object get(int index)
	{
		Iterator i=Iterator1;
		for(int j=0;j<Size&&j<index;j++)i=i.Next;
		if(i!=null)return i.Data;
		else return null;
	}

	public Iterator iterator()
	{
		 Iterator  Iterator0=new  Iterator(0);
		 Iterator0.Next= Iterator1;
		 return Iterator0;
	}
	public Iterator lastIterator()
	{
		 Iterator  Iterator0=new  Iterator(0);
		 Iterator0.Previous= Iterator2;
		 return Iterator0;
	}
	public int size()
	{
		return Size;
	}
}
class HashTable
{
	LinkedList[] LinkedLists1;
	LinkedList[] LinkedLists2;
	int Size;
	private int getHashCode(String s)
	{
		int HashCode=0;
		for(int i=0;i<s.length();i++)HashCode=HashCode*37+s.charAt(i);
		HashCode%=Size;
		return HashCode;
	}
	public HashTable(int init)
	{	
		Size=init;
		LinkedLists1=new LinkedList[Size];
		LinkedLists2=new LinkedList[Size];
		for(int i=0;i<Size;i++)
		{
			LinkedLists1[i]=new LinkedList();	
			LinkedLists2[i]=new LinkedList();
		}
	}
	public void put(Object key,Object object)
	{
		int i=this.getHashCode((String)key);
		LinkedLists1[i].add(key);
		LinkedLists2[i].add(object);
	}
	public Object get(Object key)
	{
		int i=this.getHashCode((String)key);
		Iterator Iterator1=LinkedLists1[i].iterator();
		Iterator Iterator2=LinkedLists2[i].iterator();
		while(Iterator1.hasNext())
		{
			if(((String)Iterator1.next()).equals((String)key))return Iterator2.next();
			Iterator2.next();
		}
		return null;
	}
	public Object remove(Object key)
	{
		int i=this.getHashCode((String)key);
		Iterator Iterator1=LinkedLists1[i].iterator();
		Iterator Iterator2=LinkedLists2[i].iterator();
		Object o=null;
		int c=0;
		while(Iterator1.hasNext())
		{
			if(((String)Iterator1.next()).equals((String)key)){o= Iterator2.next();break;}
			Iterator2.next();
			c++;
		}
		LinkedLists1[i].remove(c);
		LinkedLists2[i].remove(c);
		return o;
	}
}

class TreeSet
{
	Iterator Root;
	LinkedList LinkedList1;
	public TreeSet()
	{
		Root=null;
	}
	private Iterator insert(Iterator iterator,Object object)
	{
		if(iterator==null)iterator=new Iterator(object);		
		else if(((String)object).compareTo((String)iterator.Data)<0)
		{
			iterator.Previous=insert(iterator.Previous,object);
			
		}
		else if(((String)object).compareTo((String)iterator.Data)>0)
		{
			iterator.Next=insert(iterator.Next,object);
		}
		else{;}
		return iterator;
	}
	private void fillLinkedList(Iterator i)
	{
		if(i!=null)
		{
			fillLinkedList(i.Previous);
			LinkedList1.add(i.Data);
			fillLinkedList(i.Next);
		}
	}
	public void add(Object object)
	{
		Root=insert(Root,object);
	}
	private Iterator getMostPreviousIteratorOf(Iterator iterator)
	{
		if(iterator.hasPrevious())return getMostPreviousIteratorOf(iterator.Previous);
		else return iterator;
	}
	private Iterator delete(Iterator iterator,Object object)
	{
		if(iterator==null)return null;
		else if(((String)object).compareTo((String)iterator.Data)<0)
		{
			iterator.Previous=delete(iterator.Previous,object);
			
		}
		else if(((String)object).compareTo((String)iterator.Data)>0)
		{
			iterator.Next=delete(iterator.Next,object);
		}
		else if(iterator.Previous!=null&&iterator.Next!=null)
		{
			iterator.Data=getMostPreviousIteratorOf(iterator.Next).Data;
			iterator.Next=delete(iterator.Next,iterator.Data);
		}
		else iterator=(iterator.Previous!=null?iterator.Previous:iterator.Next);
		return iterator;
	}
	public void remove(Object object)
	{
		Root=delete(Root,object);
	}
	public Iterator iterator()
	{
		LinkedList1=new LinkedList();
		fillLinkedList(Root);
		return LinkedList1.iterator();
	}
}















