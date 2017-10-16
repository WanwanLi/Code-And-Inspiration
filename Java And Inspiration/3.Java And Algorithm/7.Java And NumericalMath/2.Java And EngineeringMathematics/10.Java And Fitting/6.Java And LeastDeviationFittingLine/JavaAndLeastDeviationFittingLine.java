import java.awt.*;
import java.applet.*;
public class JavaAndLeastDeviationFittingLine extends Applet
{
	public  void paint(Graphics g)
	{
		int n=5;
		int l=6;
		double[] x=new double[]{10,30,50,100,200,250};	
		double[] fx=new double[]{20,60,90,230,380,450};
		for(int i=0;i<l;i++)g.fillOval((int)x[i],(int)fx[i],5,5);
		LeastDeviationFittingLine LeastDeviationFittingLine1=new LeastDeviationFittingLine(x,fx);
		for(double X=0;X<600;X++)g.fillOval((int)X,(int)LeastDeviationFittingLine1.L(X),1,1);
	}
	public  static void main(String[] args)
	{
		int n=3;
		int l=5;
		double[] x=new double[l];
		double[] fx=new double[l];
		for(int i=0;i<l;i++){x[i]=(i+1)*0.1;fx[i]=Math.cos(x[i]);}
		LeastDeviationFittingLine LeastDeviationFittingLine1=new LeastDeviationFittingLine(x,fx);
		System.out.println(LeastDeviationFittingLine1.A);
		System.out.println(LeastDeviationFittingLine1.B);
	}
}
class LeastDeviationFittingLine
{
	private double[] x;
	private double[] fx;
	private double[] y;
	public double A=0;
	public double B=0;
	public double D=0;
	private int l;
	private double standardError;
	private double maxError;
	private int maxErrorPosition;
	public LeastDeviationFittingLine(double[] x,double[] fx)
	{
		this.l=x.length;
		this.x=new double[l];
		this.fx=new double[l];
		for(int i=0;i<l;i++)
		{
			this.x[i]=x[i];
			this.fx[i]=fx[i];
		}
		this.getLeastDeviationFittingLine_AB();
	}
	private double Abs(double x)
	{
		return (x>=0?x:-x);
	}
	private double Sgn(double x)
	{
		return (x>=0?1:-1);
	}
	private double F(double a,double b)
	{
		double F=0;
		for(int i=0;i<l;i++)F+=x[i]*Sgn(fx[i]-a*x[i]-b);
		return F;
	}
	private void getLeastDeviationFittingLine_B(double a)
	{
		double[] d=new double[l];
		for(int i=0;i<l;i++)d[i]=fx[i]-a*x[i];
		for(int i=0;i<l;i++)
		{
			int k=i;
			for(int j=i;j<l;j++)if(d[j]<d[k])k=j;
			if(k!=i)
			{
				double t=d[k];
				d[k]=d[i];
				d[i]=t;
			}
		}
		this.B=(d[l/2]+d[(l+1)/2])/2;
	}
	private void getLeastDeviationFittingLine_AB()
	{
		Least_SquaresLine Least_SquaresLine1=new Least_SquaresLine(x,fx);
		this.A=Least_SquaresLine1.A;
		this.getLeastDeviationFittingLine_B(A);
		double k1=F(A,B);
		if(k1==0)return;
		else
		{
			int h=1;
			if(k1<0)h=-h;
			double a1=A;
			double a2=a1+h;
			this.getLeastDeviationFittingLine_B(a2);
			double k2=F(a2,B);
			while(k1*k2>0)
			{
				a1=a2;
				a2=a1+h;
				k1=k2;
				this.getLeastDeviationFittingLine_B(a2);
				k2=F(a2,B);
			}
			while(Abs(a2-a1)>1E-7)
			{
				double a0=(a1+a2)/2;
				this.getLeastDeviationFittingLine_B(a0);
				double k0=F(a0,B);
				if(k0*k1>0){k1=k0;a1=a0;}
				else {k2=k0;a2=a0;}
			}
			A=(a1+a2)/2;
			this.getLeastDeviationFittingLine_B(A);
		}
	}
	public double L(double X)
	{
		return A*X+B;
	}
}

class Least_SquaresLine
{
	private double[] x;
	private double[] fx;
	private int n;
	private int l;
	private double Avg_x;
	private double Avg_fx;
	private double Avg_xx;
	private double Avg_xfx;
	public double A=0;
	public double B=0;
	public Least_SquaresLine(double[] x,double[] fx)
	{
		this.l=x.length;
		for(int i=0;i<l;i++)
		{
			Avg_x+=x[i];
			Avg_fx+=fx[i];
			Avg_xx+=x[i]*x[i];
			Avg_xfx+=x[i]*fx[i];
		}
		Avg_x/=l;
		Avg_fx/=l;
		Avg_xx/=l;
		Avg_xfx/=l;
		this.A=this.a();
		this.B=this.b();
	}
	public double a()
	{
		return cov()/d();
	}
	public double b()
	{
		return Avg_fx-a()*Avg_x;
	}
	private double cov()
	{
		return Avg_xfx-Avg_x*Avg_fx;
	}
	private double d()
	{
		return Avg_xx-Avg_x*Avg_x;
	}
	public double L(double X)
	{
		return A*X+B;
	}
}