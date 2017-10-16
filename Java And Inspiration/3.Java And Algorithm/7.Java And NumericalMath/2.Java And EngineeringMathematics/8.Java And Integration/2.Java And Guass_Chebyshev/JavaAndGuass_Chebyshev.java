public class JavaAndGuass_Chebyshev
{
	public static void main(String[] args)
	{
		Guass_Chebyshev Guass_Chebyshev1=new Guass_Chebyshev();
		double a=-1;
		double b=1;
		System.out.println("Guass_Chebyshev1.S(-1.0,1.0)="+Guass_Chebyshev1.S(10));
	}
}
class Guass_Chebyshev
{
	public double S(int n)
	{
		double G=0;
		double[] x=new double[n];
		double[] w=new double[n];
		double c=Math.PI/2/n;
		for(int i=0;i<n;i++)
		{
			w[i]=Math.PI/n;
			x[i]=Math.cos(c);
			c+=w[i];
		}
		for(int i=0;i<n;i++)G+=w[i]*f(x[i]);
		return G;
		
	}

	private double f(double x)
	{
		double fx=Math.pow(x,10)/Math.sqrt(1-x*x);
		return Math.pow(x,10);
	}
}