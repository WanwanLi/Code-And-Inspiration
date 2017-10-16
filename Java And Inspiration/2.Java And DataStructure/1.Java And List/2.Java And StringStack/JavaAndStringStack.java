public class JavaAndStringStack
{
	public static void main(String[] args)
	{
		StringStack StringStack1=new StringStack();
		for(int i=0;i<100;i++)StringStack1.push(i);
		StringStack1.show();
		while(StringStack1.isNotEmpty())System.out.println(StringStack1.pop());
	}
}
class StringStack
{
	private String stringStack;
	private int length;
	public StringStack()
	{
		this.stringStack="";
	}
	public void push(String string)
	{
		this.stringStack=string+";"+stringStack;
		this.length++;
	}
	public void push(int integer)
	{
		this.stringStack=integer+";"+stringStack;
		this.length++;
	}
	public String pop()
	{
		int i=0,l=0,Length=stringStack.length();
		for(i=0;i<Length;i++)
		{
			char c=stringStack.charAt(i);
			if(c==';')break;
		}
		String s0=stringStack.substring(0,i);
		String s1=stringStack.substring(i+1,Length);
		this.stringStack=s1;
		this.length--;
		return s0;
	}
	public void show()
	{
		System.out.println(stringStack);
	}
	public boolean isNotEmpty()
	{
		return (length>0);
	}
}