import java.awt.*;
import java.applet.*;
public class JavaAndLeast_SquaresCurve extends Applet
{
	public  void paint(Graphics g)
	{
		int n=3;
		int l=6;
		double[] x=new double[]{10,30,50,100,200,500};	
		double[] fx=new double[]{10,60,90,230,280,60};
		for(int i=0;i<l;i++)g.fillOval((int)x[i],(int)fx[i],5,5);
		Least_SquaresCurve Least_SquaresCurve1=new Least_SquaresCurve(n,x,fx);
		for(double X=0;X<600;X++)g.fillOval((int)X,(int)Least_SquaresCurve1.Pn(X),1,1);
	}
	public  static void main(String[] args)
	{
		int n=3;
		int l=6;
		double[] x=new double[]{10,30,50,100,200,500};	
		double[] fx=new double[]{10,60,90,230,280,60};
		Least_SquaresCurve Least_SquaresCurve1=new Least_SquaresCurve(n,x,fx);
		for(double X=0;X<300;X++)System.out.println(Least_SquaresCurve1.Pn(X));
	}
}
class Least_SquaresCurve
{
	private double[] x;
	private double[] fx;
	private int n;
	private int l;
	public Least_SquaresCurve(int n,double[] x,double[] fx)
	{
		this.l=x.length;
		this.x=new double[l];
		this.fx=new double[l];
		for(int i=0;i<l;i++)
		{
			this.x[i]=x[i];
			this.fx[i]=fx[i];
		}
		this.n=n;
	}
	private double a(int i)
	{
		double a=0;
		for(int j=0;j<l;j++)
		{
			double q=Q(i,x[j]);
			a+=x[j]*q*q;
		}		
		return a/d(i);
	}
	private double b(int i)
	{
		return d(i)/d(i-1);
	}
	private double c(int i)
	{
		double c=0;
		for(int j=0;j<l;j++)
		{
			double q=Q(i,x[j]);
			c+=fx[j]*q;
		}
		return c;
	}
	private double d(int i)
	{
		double d=0;
		for(int j=0;j<l;j++)
		{
			double q=Q(i,x[j]);
			d+=q*q;
		}
		return d;
	}
	private double Q(int i,double x)
	{
		if(i==0)return 1;
		if(i==1)return x-a(0);
		return (x-a(i-1))*Q(i-1,x)-b(i-1)*Q(i-2,x);
	}
	public double Pn(double x)
	{
		double Pn=0;
		for(int i=0;i<n;i++)Pn+=c(i)/d(i)*Q(i,x);
		return Pn;
	}
}