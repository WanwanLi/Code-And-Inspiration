public class JavaAndLagrange
{
	public static void main(String[] args)
	{
		int n=7;
		double[] x=new double[]{1,2,3,4,5,6,7};
		double[] fx=new double[]{1,1.414214,1.732051,2,2.236068,2.449490,2.645751};
		Lagrange Lagrange1=new Lagrange();
		double X=2.5;
		System.out.println("Lagrange1.Ln("+X+")="+Lagrange1.Ln(n,x,fx,X));
		System.out.println("Math.sqrt("+X+")="+Math.sqrt(X));
		System.out.println("Lagrange1.Ln("+X+")-Math.sqrt("+X+")="+(Lagrange1.Ln(n,x,fx,X)-Math.sqrt(X)));
	}
}
class Lagrange
{
	public double Ln(int n,double[] x,double[] fx,double X)
	{
		double Ln=0.0;
		double li=1.0;
		for(int i=0;i<n;i++)
		{
			li=1.0;
			for(int j=0;j<n;j++)if(j!=i)li*=(X-x[j])/(x[i]-x[j]);
			Ln+=li*fx[i];
		}
		return Ln;
	}
}