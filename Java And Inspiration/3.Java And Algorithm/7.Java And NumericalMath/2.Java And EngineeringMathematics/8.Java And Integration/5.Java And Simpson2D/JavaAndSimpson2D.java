public class JavaAndSimpson2D
{
	public static void main(String[] args)
	{
		Simpson2D simpson2D=new Simpson2D();
		double a=0;
		double b=1;
		int n=100;
		System.out.println("simpson2D.S("+a+","+b+")="+simpson2D.S(a,b,n));
	}
}
class Simpson2D
{
	private int n=0;
	public double S(double a,double b,int n)
	{
		this.n=n;
		double h=(b-a)/n;
		double s=g(a)+g(b);
		for(int i=1;i<n;i++)
		{
			if(i%2==0)s+=2*g(a+i*h);
			else s+=4*g(a+i*h);
		}
		s*=h/3;
		return s;
	}
	private double g(double x)
	{
		double y0=this.y0(x);
		double y1=this.y1(x);
		double h=(y1-y0)/n;
		double g=f(x,y0)+f(x,y1);
		for(int i=1;i<n;i++)
		{
			if(i%2==0)g+=2*f(x,y0+i*h);
			else g+=4*f(x,y0+i*h);
		}
		g*=h/3;
		return g;
	}
	private double y0(double x)
	{
		return x;
	}
	private double y1(double x)
	{
		return Math.sqrt(x);
	}
	private double f(double x,double y)
	{
		return Math.sin(x-y*y);
	}
}