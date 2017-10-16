public class JavaAndHermite
{
	public static void main(String[] args)
	{
		double[] x=new double[]{0.1,0.15,0.3,0.45,0.55,0.6,0.7,0.85,0.9,1};
		int n=x.length;
		double[] fx=new double[n];
		double[] dfx=new double[n];
		for(int i=0;i<n;i++)
		{
			fx[i]=Math.sin(x[i]);
			dfx[i]=Math.cos(x[i]);
		}
		Hermite Hermite1=new Hermite();
		double X=0.3542990177756594;
		System.out.println(Hermite1.Hn(n,x,fx,dfx,X));
	}
}
class Hermite
{
	public double Hn(int n,double[] x,double[] fx,double[] dfx,double X)
	{
		double Hn=0;
		double li=1;
		double dli=0;
		for(int i=0;i<n;i++)
		{
			li=1;
			dli=0;
			for(int j=0;j<n;j++)
			{
				if(j!=i)
				{
					li*=(X-x[j])/(x[i]-x[j]);
					dli+=1/(x[i]-x[j]);
				}
			}
			Hn+=li*li*(fx[i]+(X-x[i])*(dfx[i]-2*fx[i]*dli));
		}
		return Hn;
	}
}