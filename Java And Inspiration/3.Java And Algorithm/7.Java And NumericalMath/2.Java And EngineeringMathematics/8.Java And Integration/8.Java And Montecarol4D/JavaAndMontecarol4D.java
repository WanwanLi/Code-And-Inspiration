import java.io.*;
import java.util.*;
public class JavaAndMontecarol4D
{
	public static void main(String[] args)
	{
		Montecarol4D Montecarol4D1=new Montecarol4D();
		int m=100000;
		double[] X=new double[]{0,0,0,0};
		System.out.println("Montecarol4D1.S("+m+")="+Montecarol4D1.S(m,X));
		m=400000;
		X=new double[]{0,0,0,0};
		System.out.println("Montecarol4D1.S("+m+")="+Montecarol4D1.S(m,X));
		m=900000;
		X=new double[]{0,0,0,0};
		System.out.println("Montecarol4D1.S("+m+")="+Montecarol4D1.S(m,X));
	}
}
class Montecarol4D
{
	public double S(int m,double[] X)
	{
		int n=4;
		double M=0;
		double[] a=new double[]{0,0,0,0};
		double[] b=new double[]{1,1,1,1};
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)X[j]=a[j]+(b[j]-a[j])*Math.random();	
			M+=F(X);
		}
		for(int j=0;j<n;j++)M*=b[j]-a[j];
		M/=m;
		return M;
	}
	private double F(double[] X)
	{
		return X[0]*X[0]+X[1]*X[1]+X[2]*X[2]+X[3]*X[3];
	}
}