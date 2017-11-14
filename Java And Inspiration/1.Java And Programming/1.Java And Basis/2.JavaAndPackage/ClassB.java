package packageA.packageB;
import packageA.ClassA;

public class ClassB extends ClassA
{
	protected String string;
	public ClassB(String string)
	{
		super(string);
		this.string="ClassB:";
		this.string+=super.string;
	}
	public void println()
	{
		System.out.println("ClassB: "+string);
	}
}