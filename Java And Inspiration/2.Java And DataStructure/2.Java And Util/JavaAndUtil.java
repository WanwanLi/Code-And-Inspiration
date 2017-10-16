import java.util.*;

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
		for(Object object : LinkedList1)System.out.println((String)object);
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
		LinkedList1.add(3,"String3");		
		System.out.println(LinkedList1.size());
		while(LinkedList1.size()>0)System.out.println((String)LinkedList1.pop());
		Hashtable<String,String> Hashtable1=new Hashtable<String,String>(11);
		Hashtable1.put("Key1","Object1");
		System.out.println("Key1:"+(String)Hashtable1.get("Key1"));	
		System.out.println("Remove Key1:"+(String)Hashtable1.remove("Key1"));
		System.out.println("Key1:"+(String)Hashtable1.get("Key1"));
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
class Queue<T>
{
	LinkedList<T> queue;
	public Queue()
	{
		this.queue=new LinkedList<T>();
	}
	public void enQueue(T data)
	{
		this.queue.add(data);
	}
	public T deQueue()
	{
		return this.queue.remove();
	}
	public boolean isNotEmpty()
	{
		return this.queue.size()>0;
	}
}

