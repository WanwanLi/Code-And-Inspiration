import java.awt.*;
import java.applet.*;
public class JavaAndLeast_SquaresLine extends Applet
{
	public  void paint(Graphics g)
	{
		int l=6;
		double[] x=new double[]{10,30,50,100,200,250};	
		double[] fx=new double[]{20,60,90,230,380,450};
		for(int i=0;i<l;i++)g.fillOval((int)x[i],(int)fx[i],5,5);
		Least_SquaresLine Least_SquaresLine1=new Least_SquaresLine(x,fx);
		for(double X=0;X<600;X++)g.fillOval((int)X,(int)Least_SquaresLine1.L(X),1,1);
	}
	public  static void main(String[] args)
	{
		double[] x=new double[]{10,30,50,100,200,250};	
		double[] fx=new double[]{20,60,90,230,380,450};
		Least_SquaresLine Least_SquaresLine1=new Least_SquaresLine(x,fx);
		for(double X=0;X<300;X++)System.out.println(Least_SquaresLine1.L(X));
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
	}
	private double a()
	{
		return cov()/d();
	}
	private double b()
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
	public double L(double x)
	{
		return a()*x+b();
	}
}