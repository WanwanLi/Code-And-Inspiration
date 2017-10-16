public class JavaAndRuntime
{
	public static void main(String[] args)
	{
		try
		{
			Runtime Runtime1=Runtime.getRuntime();
			long start=System.currentTimeMillis();
			Runtime1.exec("notepad.exe JavaAndRuntime");
			long end=System.currentTimeMillis();
			System.out.println("It has taken : " + ((end - start) / 1000+" sec to start!"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}