public class JavaAndRomberg2D
{
	public static void main(String[] args)
	{
		Romberg2D romberg2D=new Romberg2D();
		double a=0;
		double b=1;
		System.out.println("romberg2D.S("+a+","+b+")="+romberg2D.S(a,b));
	}
}
class Romberg2D
{
	double e=1E-18;
	private double Abs(double x)
	{
		return (x>=0?x:-x);
	}
	public double S(double a,double b)
	{
		double n=1;
		double h=(b-a)/n;
		double s=(g(a)+g(b))/2;
		double T1=s*h;
		double S1=T1,C1=T1,R1=T1,T0,S0,C0;
		for(int c=0;c<10;c++)
		{
			T0=T1;S0=S1;C0=C1;
			s=(g(a)+g(b))/2;
			for(int i=1;i<n;i++)s+=g(a+i*h);
			T1=s*h;
			S1=T1+(T1-T0)/3;
			C1=S1+(S1-S0)/15;
			R1=C1+(C1-C0)/63;
			h*=0.5;
			n*=2;
			if(c>1&&Abs(R1-C1)<e)break;
		}
		return R1;
	}
	private double g(double x)
	{
		double y0=this.y0(x);
		double y1=this.y1(x);
		double n=1;
		double h=y1-y0;
		double g=(f(x,y0)+f(x,y1))/2;
		double T1=g*h;
		double S1=T1,C1=T1,R1=T1,T0,S0,C0;
		for(int c=0;c<10;c++)
		{
			T0=T1;S0=S1;C0=C1;
			g=(f(x,y0)+f(x,y1))/2;
			for(int i=1;i<n;i++)g+=f(x,y0+i*h);
			T1=g*h;
			S1=T1+(T1-T0)/3;
			C1=S1+(S1-S0)/15;
			R1=C1+(C1-C0)/63;
			h*=0.5;
			n*=2;
			if(c>1&&Abs(R1-C1)<e)break;
		}
		return R1;
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