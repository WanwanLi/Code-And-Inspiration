public class JavaAndAitken
{
	public static void main(String[] args)
	{
		(new Aitken()).x(1.5);
	}
}
class Aitken
{
	private double Abs(double d)
	{
		return (d>=0?d:-d);
	}
	private double f(double x)
	{
		double y=x*x*x+4*x*x-10;
		double x_x=10/(x+4);
		return Math.sqrt(10/(x+4));
	}
	public void x(double x0)
	{
		for(int i=0;i<100;i++)
		{
			System.out.println("Aitken.x["+i+"]="+x0);
			double a0=f(x0);
			double a1=f(a0);
			double x1=x0-(a0-x0)*(a0-x0)/(a1-2*a0+x0);
			if((a1-2*a0+x0)==0||x1==x0)break;
			x0=x1;
		}
	}
}