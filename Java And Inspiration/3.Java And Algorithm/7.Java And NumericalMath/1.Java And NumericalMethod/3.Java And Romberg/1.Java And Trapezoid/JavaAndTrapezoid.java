public class JavaAndTrapezoid
{
	public static void main(String[] args)
	{
		Trapezoid Trapezoid1=new Trapezoid();
		double a=0;
		double b=1;
		int n=1000;
		System.out.println(Trapezoid1.S(a,b,n));
	}
}
class Trapezoid
{
	public double S(double a,double b,int n)
	{
		double h=(a+b)/n;
		double s=(f(a)+f(b))/2;
		for(int i=1;i<n;i++)s+=f(a+i*h);
		return s*=h;
	}
	public double f(double x)
	{
		return 4/(1+x*x);
	}
}