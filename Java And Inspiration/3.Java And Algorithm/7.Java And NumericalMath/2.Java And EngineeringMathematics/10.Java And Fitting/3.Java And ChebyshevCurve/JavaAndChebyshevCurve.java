import java.awt.*;
import java.applet.*;
public class JavaAndChebyshevCurve extends Applet
{
	public  void paint(Graphics g)
	{
		int n=5;
		int l=6;
		double[] x=new double[]{10,30,50,100,200,250};	
		double[] fx=new double[]{20,60,90,230,380,450};
		for(int i=0;i<l;i++)g.fillOval((int)x[i],(int)fx[i],5,5);
		ChebyshevCurve ChebyshevCurve1=new ChebyshevCurve(n,x,fx);
		for(double X=0;X<600;X++)g.fillOval((int)X,(int)ChebyshevCurve1.Pn(X),1,1);
	}
	public  static void main(String[] args)
	{
		int n=3;
		int l=6;
		double[] x=new double[]{10,30,50,100,200,500};	
		double[] fx=new double[]{10,60,90,230,280,60};
		ChebyshevCurve ChebyshevCurve1=new ChebyshevCurve(n,x,fx);
		for(double X=0;X<300;X++)System.out.println(ChebyshevCurve1.Pn(X));
	}
}
class ChebyshevCurve
{
	private double[] x;
	private double[] fx;
	private double[] y;
	private int n;
	private int l;
	private double standardError;
	private double maxError;
	private int maxErrorPosition;
	public ChebyshevCurve(int n,double[] x,double[] fx)
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
		this.selectMinErrorDataPoints();
	}
	private double Abs(double x)
	{
		return (x>=0?x:-x);
	}
	private double Nn(double[] fx,double X)
	{
		double[] D=new double[n];
		double[] d=new double[n];
		for(int i=0;i<n;i++)D[i]=fx[i];
		double Nn=D[0];
		double dx=1.0;
		for(int i=1;i<n;i++)
		{
			for(int j=i;j<n;j++)d[j]=(D[j]-D[j-1])/(x[j]-x[j-i]);
			for(int j=i;j<n;j++)D[j]=d[j];
			dx*=(X-x[i-1]);
			Nn+=D[i]*dx;
		}
		return Nn;
	}
	private void getStandardError()
	{
		double[] D=new double[n];
		double[] d=new double[n];
		double[] T=new double[n];
		double[] t=new double[n];
		for(int i=0;i<n;i++)D[i]=fx[i];
		for(int i=0;i<n;i++)if(i%2==0)T[i]=1;else T[i]=-1;
		for(int i=1;i<n;i++)
		{
			for(int j=i;j<n;j++)d[j]=(D[j]-D[j-1])/(x[j]-x[j-i]);
			for(int j=i;j<n;j++)D[j]=d[j];
			for(int j=i;j<n;j++)t[j]=(T[j]-T[j-1])/(x[j]-x[j-i]);
			for(int j=i;j<n;j++)T[j]=t[j];
		}
		this.standardError=-D[n-1]/T[n-1];
	}
	private void getMaxErrorAndMaxErrorPosition()
	{
		this.maxErrorPosition=0;
		this.getStandardError();
		double s=this.standardError;
		this.maxError=s;
		y=new double[n];
		for(int i=0;i<n;i++)
		{
			y[i]=fx[i]+s;
			s*=-1;
		}
		for(int i=n;i<l;i++)
		{
			double error=Nn(y,x[i])-fx[i];
			if(Abs(error)>Abs(this.maxError))
			{
				this.maxError=error;
				this.maxErrorPosition=i;
			}
		}
	}
	private int getReplacePosition()
	{
		int p=0;
		if(this.standardError*this.maxError>0)
		{
			p=0;
			for(int i=2;i<n;i+=2)if(Abs(x[this.maxErrorPosition]-x[i])<Abs(x[this.maxErrorPosition]-x[p]))p=i;
		}
		else
		{
			p=1;
			for(int i=3;i<n;i+=2)if(Abs(x[this.maxErrorPosition]-x[i])<Abs(x[this.maxErrorPosition]-x[p]))p=i;
		}
		return p;
	}
	private void selectMinErrorDataPoints()
	{
		this.getStandardError();
		this.getMaxErrorAndMaxErrorPosition();
		while(this.maxErrorPosition>=n)
		{
			int p=this.getReplacePosition();
			int q=this.maxErrorPosition;
			double t=x[q];
			x[q]=x[p];
			x[p]=t;
			t=fx[q];
			fx[q]=fx[p];
			fx[p]=t;
			this.getStandardError();
			this.getMaxErrorAndMaxErrorPosition();
		}
	}
	public double Pn(double X)
	{
		return this.Nn(y,X);
	}
}