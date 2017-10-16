public class JavaAndNewton_Raphson
{
	public static void main(String[] args)
	{
		double x0=0.5;
		double x1=1.0;
		System.out.println("cos(x)-sin(x)=0  x=PI/4="+Math.PI/4);
		Newton_Raphson Newton_Raphson1=new Newton_Raphson();
		System.out.println("Newton_Raphson1.getRootByTangent("+x0+")="+Newton_Raphson1.getRootByTangent(x0));
		System.out.println("Newton_Raphson1.getRootBySecant("+x0+","+x1+")="+Newton_Raphson1.getRootBySecant(x0,x1));
	}
}
class Newton_Raphson
{
	public double getRootByTangent(double x0)
	{
		double x1=0;
		int n=40;
		for(int i=0;i<n;i++)
		{
			x1=x0-f(x0)/df(x0);
			if(x0==x1)return x1;
			x0=x1;
		}
		return x1;
	}
	public double getRootBySecant(double x0,double x1)
	{
		double x2=0;
		int n=10;
		for(int i=0;i<n;i++)
		{
			x2=x0-f(x0)/((f(x1)-f(x0))/(x1-x0));
			if(x0==x2)return x2;
			x1=x0;
			x0=x2;
		}
		return x2;
	}
	private double f(double x)
	{
		return Math.cos(x)-Math.sin(x);
	}
	private double df(double x)
	{
		double dx=0.000000001;
		return (f(x+dx)-f(x-dx))/(2*dx);
	}
}