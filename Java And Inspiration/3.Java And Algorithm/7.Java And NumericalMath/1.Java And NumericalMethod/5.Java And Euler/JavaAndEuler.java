public class JavaAndEuler
{
	public static void main(String[] args)
	{
		Euler Euler1=new Euler();
		double x0=0.0;
		double y0=1.0;
		for(double x=0.0;x<=1;x+=0.1)System.out.println("Euler1.y("+x+")="+Euler1.y(x0,y0,x));
		Euler2 Euler2_1=new Euler2();
		for(double x=0.0;x<=1;x+=0.1)System.out.println("Euler2_1.y("+x+")="+Euler2_1.y(x0,y0,x));
		
	}
}
class Euler
{
	public double y(double x0,double y0,double x)
	{
		int n=100;
		double h=(x-x0)/n;
		double x1=0.0,y1=0.0;
		for(int i=0;i<n;i++)
		{
			x1=x0+h;
			y1=y0+h*f(x0,y0);
			x0=x1;
			if(y0==y1)break;
			y0=y1;
		}
		return y0;
	}
	private double f(double x,double y)
	{
		return y-2*x/y;
	}
}
class Euler2
{
	public double y(double x0,double y0,double x)
	{
		int n=100;
		double h=(x-x0)/n;
		double x1=0.0,y1=0.0;
		double E1,E2;
		for(int i=0;i<n;i++)
		{
			x1=x0+h;
			E1=f(x0,y0);
			E2=f(x1,y0+E1*h);
			y1=y0+(E1+E2)*h/2;
			x0=x1;
			if(y0==y1)break;
			y0=y1;
		}
		return y0;
	}
	private double f(double x,double y)
	{
		return y-2*x/y;
	}
}	