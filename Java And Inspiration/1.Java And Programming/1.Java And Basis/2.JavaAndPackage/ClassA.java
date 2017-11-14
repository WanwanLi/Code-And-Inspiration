package packageA;

public class ClassA
{
	protected String string;
	public ClassA(String string)
	{
		this.string=string;
	}
	public void println()
	{
		System.out.println("ClassA: "+string);
	}
}