public class JavaAndJordan
{
	public static void main(String[] args)
	{
		Jordan Jordan1=new Jordan();
		int n=3;
		double[][] a=new double[][]
		{
			{-0.002,2.0,2},
			{1,0.78125,0},
			{3.996,5.5625,4}
		};
		double[] b=new double[]
		{
			0.4,
			1.3816,
			7.4178
		};
		double[] x=Jordan1.x(n,a,b);
		for(int i=0;i<n;i++)System.out.println("Jordan.x["+i+"]="+x[i]);
		n=4;
		a=new double[][]
		{
			{2.1756,4.0231,-2.1732,5.1967},
			{-4.0231,6,0,1.1973},
			{-1,-5.2107,1.1111,0},
			{6.0235,7,0,-4.1561}
		};
		b=new double[]
		{
			17.101999,
			-6.1593,
			3.0004,
			0
		};
		double[] X=Jordan1.X(n,a,b);
		for(int i=0;i<n;i++)System.out.println("Jordan.X["+i+"]="+X[i]);
		double[] getX=Jordan1.getX(n,a,b);
		for(int i=0;i<n;i++)System.out.println("Jordan.getX["+i+"]="+getX[i]);
	}
}
class Jordan
{
	public double[] x(int n,double[][] a,double[] b)
	{
		double J=0;
		double[] x=new double[n];
		for(int k=0;k<n;k++)
		{
			for(int i=0;i<n;i++)
			{
				if(i!=k)
				{
					J=a[i][k]/a[k][k];
					for(int j=k;j<n;j++)a[i][j]-=J*a[k][j];
					b[i]-=J*b[k];
				}
			}
		}
		for(int i=0;i<n;i++)x[i]=b[i]/a[i][i];
		return x;
	}
	private double Abs(double d)
	{
		return (d>=0?d:-d);
	}
	public double[] X(int n,double[][] a,double[] b)
	{
		double t=0;
		double J=0;
		double[] x=new double[n];
		for(int k=0;k<n;k++)
		{
			int r=k;
			double abs=Abs(a[k][k]);
			for(int i=k+1;i<n;i++)
			{
				if(Abs(a[i][k])>abs)
				{
					abs=Abs(a[i][k]);
					r=i;
				}
			}
			if(r!=k)
			{
				for(int j=k;j<n;j++)
				{
					t=a[r][j];
					a[r][j]=a[k][j];
					a[k][j]=t;
				}
				t=b[r];
				b[r]=b[k];
				b[k]=t;
			}
			for(int i=0;i<n;i++)
			{
				if(i!=k)
				{
					J=a[i][k]/a[k][k];
					for(int j=k;j<n;j++)a[i][j]-=J*a[k][j];
					b[i]-=J*b[k];
				}
			}
		}
		for(int i=0;i<n;i++)x[i]=b[i]/a[i][i];
		return x;
	}
	public double[] getX(int n,double[][] a,double[] b)
	{
		double t=0;
		double J=0;
		double[] x=new double[n];
		for(int k=0;k<n;k++)
		{
			int r=k;
			int c=k;
			double abs=Abs(a[k][k]);
			for(int i=k+1;i<n;i++)
			{
				for(int j=0;j<n;j++)
				{
					if(Abs(a[i][j])>abs)
					{
						abs=Abs(a[i][j]);
						r=i;
						c=j;
					}
				}
			}
			if(k==n-1)for(int j=0;j<n;j++)if(a[r][j]!=0)c=j;
			if(r!=k)
			{
				for(int j=0;j<n;j++)
				{
					t=a[r][j];
					a[r][j]=a[k][j];
					a[k][j]=t;
				}
				t=b[r];
				b[r]=b[k];
				b[k]=t;
			}
			for(int i=0;i<n;i++)
			{
				if(i!=k)
				{
					J=a[i][c]/a[k][c];
					for(int j=0;j<n;j++)a[i][j]-=J*a[k][j];
					b[i]-=J*b[k];
				}
			}
		}
		for(int j=0;j<n;j++)for(int i=0;i<n;i++)if(a[i][j]!=0)x[j]=b[i]/a[i][j];
		return x;
	}
}