class JavaAndSimpson
{
	public static void main(String[] args)
	{
		Simpson Simpson1=new Simpson();
		double a=0;
		double b=Math.PI;
		int n=1000;
		System.out.println(Simpson1.S(a,b,n));
	
	}
}
class Simpson
{
	public double S(double a,double b,int n)
	{
		double h=(b-a)/n;
		double s=f(a)+f(b);
		for(int i=1;i<n;i++)
		{
			if(i%2==0)s+=2*f(a+i*h);
			else s+=4*f(a+i*h);
		}
		s*=h/3;
		return s;
	}
	private double f(double x)
	{
		return x*Math.sin(x)+1/(x*x*x+1);
	}
}