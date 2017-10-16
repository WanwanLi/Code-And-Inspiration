import java.awt.*;
import java.applet.*;
public class JavaAndLeast_SquaresPolynomial extends Applet
{
	public  void paint(Graphics g)
	{
		int n=3;
		int l=6;
		double[] x=new double[]{10,30,50,100,200,250};	
		double[] fx=new double[]{20,60,90,230,380,450};
		for(int i=0;i<l;i++)g.fillOval((int)x[i],(int)fx[i],5,5);
		Least_SquaresPolynomial Least_SquaresPolynomial1=new Least_SquaresPolynomial(n,x,fx);
		for(double X=0;X<600;X++)g.fillOval((int)X,(int)Least_SquaresPolynomial1.Pn(X),1,1);
	}
	public  static void main(String[] args)
	{
		int n=3;
		double[] x=new double[]{-3,-2,-1,0,1,2,3};	
		double[] fx=new double[]{4,2,3,0,-1,-2,-5};
		Least_SquaresPolynomial Least_SquaresPolynomial1=new Least_SquaresPolynomial(n,x,fx);
		for(int i=0;i<n;i++)System.out.println(Least_SquaresPolynomial1.A[i]);
		double a=56.0/84.0;
		double b=-117.0/84.0;
		double c=-11.0/84.0;
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
	}
}
class Least_SquaresPolynomial
{
	public double[] A;
	private int n;
	public Least_SquaresPolynomial(int n,double[] x,double[] fx)
	{
		int l=x.length;
		double[] b=new double[n];
		double[][] a=new double[n][n];
		for(int i=0;i<n;i++)
		{
			b[i]=0;
			for(int j=0;j<l;j++)b[i]+=fx[j]*Pow(x[j],i);
		}
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				a[i][j]=0;
				for(int k=0;k<l;k++)a[i][j]+=Pow(x[k],i+j);
			}
		}
		Gauss Gauss1=new Gauss();
		this.A=Gauss1.X(n,a,b);
		this.n=n;
	}
	public double Pow(double x,int n)
	{
		double Pow=1;
		for(int i=0;i<n;i++)Pow*=x;
		return Pow;
	}
	public double Pn(double X)
	{
		double Pn=0;
		for(int i=0;i<n;i++)Pn+=A[i]*Pow(X,i);
		return Pn;
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