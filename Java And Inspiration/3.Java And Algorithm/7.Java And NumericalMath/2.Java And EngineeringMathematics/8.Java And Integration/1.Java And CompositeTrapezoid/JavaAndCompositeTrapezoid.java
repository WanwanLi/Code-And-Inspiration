public class JavaAndCompositeTrapezoid
{
	public static void main(String[] args)
	{
		CompositeTrapezoid CompositeTrapezoid1=new CompositeTrapezoid();
		double a=0;
		double b=10;
		double e=1E-7;
		System.out.println("CompositeTrapezoid1.S("+a+","+b+","+e+")="+CompositeTrapezoid1.S(a,b,e));
	}
}
class CompositeTrapezoid
{
	private double Abs(double x)
	{
		return (x>=0?x:-x);
	}
	public double S(double a,double b,double e)
	{
		double h=b-a;
		double T0=(f(a)+f(b))*h/2;
		double T1=(f(a)+2*f(a+h/2)+f(b))*h/4;
		if(this.Abs(T1-T0)<e)return T1;
		else
		{
			double s1=this.S(a,a+h/2,e/2);
			double s2=this.S(a+h/2,b,e/2);
			return s1+s2;
		}
	}
	private double f(double x)
	{
		return Math.exp(x);
	}
}