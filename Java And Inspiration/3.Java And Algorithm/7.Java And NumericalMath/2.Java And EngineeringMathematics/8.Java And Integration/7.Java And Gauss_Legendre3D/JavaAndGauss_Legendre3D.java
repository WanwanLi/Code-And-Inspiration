import java.io.*;
import java.util.*;
public class JavaAndGauss_Legendre3D
{
	public static void main(String[] args)
	{
		Gauss_Legendre3D Gauss_Legendre3D1=new Gauss_Legendre3D();
		int n=3-1;
		double[] X=new double[]{0,0,0};
		System.out.println("Gauss_Legendre3D1.S()="+Gauss_Legendre3D1.S(n,X));
	}
}
class Gauss_Legendre3D
{
	double[] x=new double[10];
	double[] w=new double[10];
	public Gauss_Legendre3D()
	{
		x=this.getXFromGauss_LegendreTable(10);
		w=this.getWFromGauss_LegendreTable(10);
	}
	public double S(int n,double[] X)
	{
		double G=0;
		double a=this.a(n,X);
		double b=this.b(n,X);
		if(n==0)
		{
			for(int i=0;i<10;i++)
			{
				X[0]=x[i]*(b-a)/2+(b+a)/2;
				G+=(b-a)/2*w[i]*F(X);
			}
		}
		else
		{
			for(int i=0;i<10;i++)
			{
				X[n]=x[i]*(b-a)/2+(b+a)/2; 
				G+=(b-a)/2*w[i]*S(n-1,X);
			}
		}
		return G;
	}
	private double F(double[] X)
	{
		return X[0]*X[0]+X[1]*X[1]+X[2]*X[2];
	}
	private double a(int n,double[] X)
	{
		switch(n)
		{
			case 0:return Math.sqrt(1-X[1]*X[1]-X[2]*X[2]);
			case 1:return 0;
			case 2:return 0;
			default:return 0;
		}
	}
	private double b(int n,double[] X)
	{
		switch(n)
		{
			case 0:return Math.sqrt(X[1]*X[1]+X[2]*X[2]);
			case 1:return Math.sqrt(1-X[2]*X[2]);
			case 2:return 1;
			default:return 0;
		}
	}
	private double[] getXFromGauss_LegendreTable(int n)
	{
		double[] x=new double[n];	
		try
		{
			Scanner Scanner1=new Scanner(new File("Gauss_LegendreTable\\x.txt"));
			double d=Scanner1.nextDouble();
			d=Scanner1.nextDouble();
			while(Scanner1.hasNext()&&d!=n)d=Scanner1.nextDouble();
			d=Scanner1.nextDouble();
			for(int i=0;i<n;i++)
			{
				x[i]=d;
				d=Scanner1.nextDouble();
			}
		}
		catch(Exception e){}
		return x;
	}
	private double[] getWFromGauss_LegendreTable(int n)
	{
		double[] w=new double[n];	
		try
		{
			Scanner Scanner1=new Scanner(new File("Gauss_LegendreTable\\w.txt"));
			double d=Scanner1.nextDouble();
			d=Scanner1.nextDouble();
			while(Scanner1.hasNext()&&d!=n)d=Scanner1.nextDouble();
			d=Scanner1.nextDouble();
			for(int i=0;i<n;i++)
			{
				w[i]=d;
				d=Scanner1.nextDouble();
			}
		}
		catch(Exception e){}
		return w;
	}
}