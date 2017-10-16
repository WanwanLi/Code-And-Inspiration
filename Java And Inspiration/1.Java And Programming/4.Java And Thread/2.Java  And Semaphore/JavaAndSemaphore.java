public class JavaAndSemaphore
{
	public static void main(String[] args)
	{
		Semaphore count=new Semaphore(0);
		Semaphore output=new Semaphore(1);
		Increment increment=new Increment(count, output);
		Decrement decrement=new Decrement(count, output);
		decrement.start();
		increment.start();

	}
}
class Increment extends Thread
{
	private Semaphore count;
	private Semaphore output;
	public Increment(Semaphore count, Semaphore output)
	{
		this.count=count;
		this.output=output;
	}
	public void run()
	{
		while(true)
		{
			output.wait(0); // obtain exclusive access to standard output
			System.out.println("INC: before v("+count.get()+") value of count is "+count.get());
			output.signal(); // release exclusive access to standard output
			count.signal(); // increment the semaphore
		}
	}
}
class Decrement extends Thread
{
	private Semaphore count;
	private Semaphore output;
	public Decrement(Semaphore count, Semaphore output)
	{
		this.count=count;
		this.output=output;
	}
	public void run()
	{
		while(true)
		{
			output.wait(0); // obtain exclusive access to standard output
			System.out.println("DEC: before p("+count.get()+") value of count is "+count.get());
			output.signal(); // release exclusive access to standard output
			count.wait(0); // decrement the semaphore (or stop )
		}
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
