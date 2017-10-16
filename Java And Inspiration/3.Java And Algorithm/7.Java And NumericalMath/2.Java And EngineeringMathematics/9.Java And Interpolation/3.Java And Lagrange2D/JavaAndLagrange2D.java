public class JavaAndLagrange2D
{
	public static void main(String[] args)
	{
		int m=11;
		int n=11;
		double[] x=new double[m];
		double[] y=new double[n];
		double[][] fxy=new double[m][n];
		for(int i=0;i<m;i++)x[i]=0.1*i;
		for(int j=0;j<n;j++)y[j]=0.1*j;
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)fxy[i][j]=Math.exp(-x[i]-y[j]);
		Lagrange2D lagrange2D=new Lagrange2D();
		double X=0.35;
		double Y=0.55;
		System.out.println(lagrange2D.Ln(m,n,x,y,fxy,X,Y));
	}
}
class Lagrange2D
{
	public double Ln(int m,int n,double[] x,double[] y,double[][] fxy,double X,double Y)
	{
		double Ln=0;
		double li=1;
		double lj=1;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				li=lj=1;
				for(int k=0;k<m;k++)if(k!=i)li*=(X-x[k])/(x[i]-x[k]);
				for(int k=0;k<n;k++)if(k!=j)lj*=(Y-y[k])/(y[j]-y[k]);
				Ln+=li*lj*fxy[i][j];
			}
		}
		return Ln;
	}
}