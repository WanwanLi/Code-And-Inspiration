public class JavaAndJacobi
{
	public static void main(String[] args)
	{
		Jacobi Jacobi1=new Jacobi();
		double[][] a=new double[][]
		{
			{-10.01,9.05,0.12},
			{1.22,-4.33,2.67},
			{1.25,-3.69,-12.37}
		};
		double[] b=new double[]
		{
			1.43,
			3.22,
			0.58
		};
		Jacobi1.x(3,a,b);
		(new Gauss_Seidel()).x(3,a,b);
	}
}
class Jacobi
{
	public void x(int n,double[][] a,double[] b)
	{
		double[] x=new double[n];
		double[] t=new double[n];
		int m=40;
		double J=0;
		boolean bool=true;
		for(int k=0;k<m;k++)
		{
			for(int i=0;i<n;i++)t[i]=x[i];
			for(int i=0;i<n;i++)
			{
				J=0;
				for(int j=0;j<n;j++)if(i!=j)J+=a[i][j]*t[j];
				x[i]=(b[i]-J)/a[i][i];
			}
			System.out.println("Jacobi["+k+"].x[]=");
			for(int i=0;i<n;i++)System.out.println("x["+i+"]="+x[i]);
			bool=true;
			for(int i=0;i<n;i++)if(x[i]!=t[i]){bool=false;break;}
			if(bool)break;
		}
	}
}
class Gauss_Seidel
{
	public void x(int n,double[][] a,double[] b)
	{
		double[] x=new double[n];
		double[] t=new double[n];
		int m=40;
		double J=0;
		boolean bool=true;
		for(int k=0;k<m;k++)
		{
			for(int i=0;i<n;i++)t[i]=x[i];
			for(int i=0;i<n;i++)
			{
				J=0;
				for(int j=0;j<n;j++)if(i!=j)J+=a[i][j]*x[j];
				x[i]=(b[i]-J)/a[i][i];
			}
			System.out.println("Gauss_Seidel["+k+"].x[]=");
			for(int i=0;i<n;i++)System.out.println("x["+i+"]="+x[i]);
			bool=true;
			for(int i=0;i<n;i++)if(x[i]!=t[i]){bool=false;break;}
			if(bool)break;
		}
	}
}