package packageA.packageB.packageC;
import packageA.packageB.ClassB;

public class ClassC extends ClassB
{
	protected String string;
	public ClassC(String string)
	{
		super(string);
		this.string+=super.string;
	}
	public void println()
	{
		System.out.println("ClassC: "+string);
	}
}