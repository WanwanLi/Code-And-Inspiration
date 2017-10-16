public class JavaAndDijkstraSolution
{
	public static void main(String[] args)
	{
		int length=Integer.parseInt(args[0]);
		Semaphore[] chopsticks=new Semaphore[length];
		Philosopher[] philosophers=new Philosopher[length];
		for(int i=0;i<length;i++)
		{
			chopsticks[i]=new Semaphore(1);
		}
		for(int i=0;i<length;i++)
		{
			philosophers[i]=new Philosopher(i, chopsticks);
		}
		for(int i=0;i<length;i++)
		{
			philosophers[i].start();
		}
	}
}
class Philosopher extends Thread
{
	private int id,length;
	private Semaphore[] chopsticks;
	public Philosopher(int id, Semaphore[] chopsticks)
	{
		this.id=id;
		this.chopsticks=chopsticks;
		this.length=chopsticks.length;
	}
	private int getID(int id)
	{
		return (id+length)%length;
	}
	private void sleep(int time)
	{
		try{Thread.sleep(time);}
		catch(Exception e){}
	}
	public void run()
	{
		while(true)
		{
			System.out.println("Philosopher #"+id+" is now thinking.");
			System.out.println("Philosopher #"+id+" is now hungry.");
			if(id==length-1)
			{
				chopsticks[0].wait(0); 
				chopsticks[id].wait(0); 
			}
			else
			{
				chopsticks[id].wait(0); 
				chopsticks[id+1].wait(0); 
			}
			System.out.println("Philosopher #"+id+" is now eating.");
			chopsticks[id].signal();
			chopsticks[getID(id+1)].signal();
			sleep(100);
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
