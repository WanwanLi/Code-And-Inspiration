public class JavaAndGauss
{
	public static void main(String[] args)
	{
		Gauss Gauss1=new Gauss();
		int n=3;
		double[][] a=new double[][]
		{
			{0.1,0.3,0.5},
			{3,1,-1},
			{3,5,4}
		};
		double[] b=new double[]
		{
			1,
			5,
			3
		};
		double[] x=Gauss1.x(n,a,b);
		for(int i=0;i<n;i++)System.out.println("Gauss.x["+i+"]="+x[i]);
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
		double[] X=Gauss1.X(n,a,b);
		for(int i=0;i<n;i++)System.out.println("Gauss.X["+i+"]="+X[i]);
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
		double[] getX=Gauss1.getX(n,a,b);
		for(int i=0;i<n;i++)System.out.println("Gauss.getX["+i+"]="+getX[i]);
	}
}
class Gauss
{
	private double Abs(double d)
	{
		return (d>=0?d:-d);
	}
	public double[] x(int n,double[][] a,double[] b)
	{
		double G=0;
		double[] x=new double[n];
		for(int k=0;k<n;k++)
		{
			for(int i=k+1;i<n;i++)
			{
				
				G=a[i][k]/a[k][k];
				for(int j=k;j<n;j++)a[i][j]-=G*a[k][j];
				b[i]-=G*b[k];		
			}
		}
		for(int i=n-1;i>=0;i--)
		{
			G=0;
			for(int j=i+1;j<n;j++)G+=a[i][j]*x[j];
			x[i]=(b[i]-G)/a[i][i];
		}
		return x;
	}
	public double[] X(int n,double[][] a,double[] b)
	{
		double G=0;
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
				double t=0;
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
			for(int i=k+1;i<n;i++)
			{
				
				G=a[i][k]/a[k][k];
				for(int j=k;j<n;j++)a[i][j]-=G*a[k][j];
				b[i]-=G*b[k];		
			}
		}
		for(int i=n-1;i>=0;i--)
		{
			G=0;
			for(int j=i+1;j<n;j++)G+=a[i][j]*x[j];
			x[i]=(b[i]-G)/a[i][i];
		}
		return x;
	}
	public double[] getX(int n,double[][] a,double[] b)
	{
		int[] R=new int[n];
		double G=0;
		double[] x=new double[n];
		for(int i=0;i<n;i++)R[i]=i;
		for(int k=0;k<n;k++)
		{
			int r=k;
			int c=k;
			double abs=Abs(a[k][k]);
			for(int i=k;i<n;i++)
			{
				for(int j=k;j<n;j++)
				{
					if(Abs(a[i][j])>abs)
					{
						abs=Abs(a[i][j]);
						r=i;
						c=j;
					}
				}
			}
			if(r!=k)
			{
				double t=0;
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
			if(c!=k)
			{
				for(int i=0;i<n;i++)
				{
					double t=a[i][c];
					a[i][c]=a[i][k];
					a[i][k]=t;
				}
				int Rc=R[c];
				R[c]=R[k];
				R[k]=Rc;
			}
			for(int i=k+1;i<n;i++)
			{
				
				G=a[i][k]/a[k][k];
				for(int j=k;j<n;j++)a[i][j]-=G*a[k][j];
				b[i]-=G*b[k];		
			}
		}
		for(int i=n-1;i>=0;i--)
		{
			G=0;
			for(int j=i+1;j<n;j++)G+=a[i][j]*x[j];
			x[i]=(b[i]-G)/a[i][i];
		}
		double[] X=new double[n];
		for(int i=0;i<n;i++)X[R[i]]=x[i];
		return X;
	}
}