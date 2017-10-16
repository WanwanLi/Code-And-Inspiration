public class JavaAndDLL
{
	static
	{
		System.loadLibrary("JavaAndDLL");
	}
	public native static int get();
	public native static void set(int i);
	public static void main(String[] args) 
	{
		JavaAndDLL JavaAndDLL1=new JavaAndDLL();
		JavaAndDLL1.set(10);
		System.out.println(JavaAndDLL1.get());
	}
}
