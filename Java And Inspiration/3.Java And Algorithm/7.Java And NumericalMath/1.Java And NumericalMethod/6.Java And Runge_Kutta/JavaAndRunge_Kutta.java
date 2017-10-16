public class JavaAndRunge_Kutta
{
	public static void main(String[] args)
	{
		Runge_Kutta Runge_Kutta1=new Runge_Kutta();
		double x0=0.0;
		double y0=1.0;
		for(double x=0.0;x<=1;x+=0.1)System.out.println("Runge_Kutta1.y("+x+")="+Runge_Kutta1.y(x0,y0,x));
		Runge_Kutta4 Runge_Kutta4_1=new Runge_Kutta4();
		x0=0.0;
		y0=-0.4;
		double dy0=-0.6;
		for(double x=0.0;x<=1;x+=0.1)System.out.println("Runge_Kutta4_1.y("+x+")="+Runge_Kutta4_1.y(x0,y0,dy0,x));
	
	}
}
class Runge_Kutta
{
	public double y(double x0,double y0,double x)
	{
		int n=10;
		double h=(x-x0)/n;
		double x1=0.0,y1=0.0;
		double K1=0.0,K2=0.0,K3=0.0,K4=0.0;
		for(int i=0;i<n;i++)
		{
			x1=x0+h;
			K1=f(x0,y0);
			K2=f(x0+h/2,y0+K1*h/2);
			K3=f(x0+h/2,y0+K2*h/2);
			K4=f(x0+h,y0+K3*h);
			y1=y0+(K1+2*K2+2*K3+K4)*h/6;
			x0=x1;
			y0=y1;
		}
		return y0;
	}
	private double f(double x,double y)
	{
		return y-2*x/y;
	}
}
class Runge_Kutta4
{
	public double y(double x0,double y0,double dy0,double x)
	{
		int n=10;
		double h=(x-x0)/n;
		double x1=0.0,y1=0.0,dy1=0.0;
		double R1=0.0,R2=0.0,R3=0.0,R4=0.0;
		for(int i=0;i<n;i++)
		{
			x1=x0+h;
			R1=f(x0,y0,dy0);
			R2=f(x0+h/2,y0+dy0*h/2,dy0+R1*h/2);
			R3=f(x0+h/2,y0+dy0*h/2+R1*h*h/4,dy0+R2*h/2);
			R4=f(x0+h,y0+dy0*h+R2*h*h/2,dy0+R3*h);
			y1=y0+(R1+R2+R3)*h*h/6+dy0*h;
			dy1=dy0+(R1+2*R2+2*R3+R4)*h/6;
			x0=x1;
			y0=y1;
			dy0=dy1;
		}
		return y0;
	}
	private double f(double x,double y,double dy)
	{
		return 2*dy-2*y+Math.exp(2*x)*Math.sin(x);
	}
}
