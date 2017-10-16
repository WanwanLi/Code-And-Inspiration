public class JavaAndRomberg
{
	public static void main(String[] args)
	{
		Romberg Romberg1=new Romberg();
		double a=0;
		double b=1;
		System.out.println(Romberg1.S(a,b));
	}
}
class Romberg
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
		double s=(f(a)+f(b))/2;
		double T1=s*h;
		double S1=T1,C1=T1,R1=T1,T0,S0,C0;
		for(int c=0;c<10;c++)
		{
			T0=T1;S0=S1;C0=C1;
			s=(f(a)+f(b))/2;
			for(int i=1;i<n;i++)s+=f(a+i*h);
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
	public double f(double x)
	{
		return 4/(1+x*x);
	}
}