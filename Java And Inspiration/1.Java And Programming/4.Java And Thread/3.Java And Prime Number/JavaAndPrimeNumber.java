import java.io.*;
public class JavaAndPrimeNumber
{
	public static void main(String[] args)
	{
		int min=4,max=100000000;
		int treadNumber=8;
		Semaphore mutex=new Semaphore(1);
		PrimeNumberCounter counter=new PrimeNumberCounter(min,max,treadNumber);
		counter.printMinMax();
		PrimeNumberChecker[] checker=new PrimeNumberChecker[treadNumber];
		long start=System.currentTimeMillis();
		for(int i=0;i<treadNumber;i++)
		{
			checker[i]=new PrimeNumberChecker(i,mutex,counter);
			checker[i].start();
		}
		counter.println();
		long end=System.currentTimeMillis();
		System.out.println("It has taken : " + ((end - start) / 1000+" sec..."));
		counter.printRunTime(start);
		counter.printFile("primes.txt", start, end);
		System.out.println("Output file : primes.txt is created... ");
	}
}
class PrimeNumberCounter
{
	public long sum;
	public int count;
	private int length;
	public int[] min;
	public int[] max;
	private boolean[] lock;
	public long[] finishTime;
	public LinkedList linkedList;
	public PrimeNumberCounter(int min,int max,int length)
	{
		this.sum=5;
		this.count=2;
		this.length=length;
		this.min=new int[length];
		this.max=new int[length];
		this.lock=new boolean[length];
		this.finishTime=new long[length];
		this.initMinMaxAndLock(min,max);
		this.linkedList=new LinkedList();
	}
	private void initMinMaxAndLock(int xMin,int xMax)
	{
		double dx=(xMax-xMin)/length;
		for(int i=0;i<length;i++)
		{
			this.lock[i]=true;
			this.min[i]=xMin+(int)(i*dx);
			this.max[i]=xMin+(int)(i*dx+dx);
		}
		this.max[length-1]=xMax;
	}
	public void printMinMax()
	{
		for(int i=0;i<length;i++)
		{
			System.out.println(i+" : "+min[i]+","+max[i]);
		}
	}
	public synchronized void unlock(int i) 
	{
		this.lock[i]=false;
		this.notify();
	}
	private boolean isLocked()
	{
		for(int i=0;i<length;i++)
		{
			if(this.lock[i])return true;
		}
		return false;
	}
	public synchronized void println()
	{
		while(isLocked())try{this.wait();}
		catch (InterruptedException e){}
		System.out.println("Total Count is: "+this.count);
		System.out.println("Total Sum is: "+this.sum);
		Node p=linkedList.last;int i=0;
		for(;p!=null&&i<10;p=p.previous,i++)
		{
			System.out.println("Prime Number["+(count-i)+"] is: "+p.integer);
		}
	}
	public synchronized void printFile(String fileName, long startTime, long endTime)
	{
		while(isLocked())try{this.wait();}
		catch (InterruptedException e){}
		long executionTime= (endTime - startTime) / 1000;
		try
		{
			PrintWriter PrintWriter1=new PrintWriter(fileName);
			PrintWriter1.print("<Execution Time = "+executionTime+" sec> ");
			PrintWriter1.print("<total number of primes found = "+this.count+"> ");
			PrintWriter1.println("<sum of all primes found = "+this.sum+"> ");
			PrintWriter1.println("<top ten maximum primes, listed in order from lowest to highest>");
			PrintWriter1.println("<begin>"); 
			Node n=linkedList.first;int i=0;
			for(;n!=null&&i<10;n=n.next,i++)
			{
				PrintWriter1.println("\tPrime Number["+(count-10+i)+"] = "+n.integer);
			}
			PrintWriter1.println("<end>");
			PrintWriter1.close();
		}
		catch(Exception e){}
	}
	public void printRunTime(long startTime)
	{
		for(int i=0;i<length;i++)
		{
			long runTime=(this.finishTime[i]-startTime)/1000;
			System.out.println("Run time["+i+"] is "+runTime+" sec...");
		}
	}
}
class PrimeNumberChecker extends Thread
{
	private int id;
	private Semaphore mutex;
	private PrimeNumberCounter counter;
	public PrimeNumberChecker(int id,Semaphore mutex,PrimeNumberCounter counter)
	{
		this.id=id;
		this.mutex=mutex;
		this.counter=counter;
	}
	private boolean isPrimeNumber(int n)
	{
		if(n<=3)return false;
		if(n%2==0||n%3==0)return false;
		for(int i=5;i*i<=n;i+=6)
		{
			if(n%i==0||n%(i+2)==0)return false;
		}
		return true;
	}
	public void run()
	{
		int min=this.counter.min[id];
		int max=this.counter.max[id];
		for(int i=min;i<max;i++)
		{
			if(isPrimeNumber(i))
			{
				this.mutex.wait(0);
				this.counter.sum+=i;
				this.counter.count++;
				this.counter.linkedList.insert(i);
				this.mutex.signal();
			}
		}
		this.counter.finishTime[id]=System.currentTimeMillis();
		this.counter.unlock(id);
	}
}
class Semaphore
{
	private int counter;
	public Semaphore() 
	{
		this(0);
	}
	public Semaphore(int i) 
	{
		this.counter = i;
	}
	public synchronized void wait(int n)
	{
		while (counter == 0) try{wait();}
		catch (InterruptedException e){}
		this.counter--;
	}
	public synchronized void signal() 
	{
		if (counter == 0) this.notify();
		this.counter++;
	}
	public int get()
	{
		return this.counter;
	}
}
class Node
{
	public int integer;
	public Node previous;
	public Node next;
	public Node(int integer)
	{
		this.integer=integer;
		this.previous=null;
		this.next=null;
	}
}
class LinkedList 
{
	public Node first;
	public Node last;
	public int length;
	public LinkedList()
	{
		this.first=null;
		this.last=null;
		this.length=0;
	}
	public void insert(int integer)
	{
		if(length>=10)
		{
			this.length--;
			this.first=this.first.next;
			this.first.previous=null;
		}
		this.length++;
		Node node=new Node(integer);
		if(first==null)this.first=this.last=node;
		else
		{
			Node n,p=last;
			for(;p!=null&&p.integer>integer;p=p.previous);
			if(p==null)
			{
				node.next=first;
				first.previous=node;
				this.first=node;
				return;
			}
			n=p.next;
			if(n==null)
			{
				this.last.next=node;
				node.previous=last;
				this.last=node;
				return;
			}
			p.next=node;
			node.previous=p;
			node.next=n;
			n.previous=node;
		}
	}
}

