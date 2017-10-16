public class JavaAndThread
{
	public static void main(String[] args)
	{
		Thread1 thread1=new Thread1();
		thread1.start();
		Thread2 thread2=new Thread2();
		thread2.start();
		Thread Thread1=new Thread(new Runnable1());
		Thread1.start();
		Thread Thread2=new Thread(new Runnable2());
		Thread2.start();

	}
}
class Thread1 extends Thread
{
	public void run()
	{
		for(int i=0;i<100;i++)System.out.println("Thread1");
	}
}
class Runnable1 implements Runnable
{
	public void run()
	{
		for(int i=0;i<100;i++)System.out.println("Runnable1");
	}
}
class Thread2 extends Thread
{
	public void run()
	{
		for(int i=0;i<100;i++)
		{
			System.out.println("Thread2");
			try
			{
				Thread.sleep(50);
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
}
class Runnable2 implements Runnable
{
	public void run()
	{
		for(int i=0;i<100;i++)
		{
			System.out.println("Runnable2");
			try
			{
				Thread.sleep(50);
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
}