public class JavaAndAdams_Bashforth
{
	public static void main(String[] args)
	{
		Adams_Bashforth Adams_Bashforth1=new Adams_Bashforth();
		double x0=0.0;
		double y0=1.0;
		double x=1;
		System.out.println("Adams_Bashforth1.y("+x+")="+Adams_Bashforth1.y(x0,y0,x));
	
	}
}
class Adams_Bashforth
{
	public double y(double x0,double y0,double x)
	{
		int n=10000;
		double h=(x-x0)/n;
		double x1=0.0,y1=0.0;
		double K1=0.0,K2=0.0,K3=0.0,K4=0.0;
		double[] X=new double[4];
		double[] Y=new double[4];
		double[] A=new double[4];
		double Y4=0;
		for(int i=0;i<4;i++)
		{
			X[i]=x0;
			Y[i]=y0;
			x1=x0+h;
			K1=f(x0,y0);
			K2=f(x0+h/2,y0+K1*h/2);
			K3=f(x0+h/2,y0+K2*h/2);
			K4=f(x0+h,y0+K3*h);
			y1=y0+(K1+2*K2+2*K3+K4)*h/6;
			x0=x1;
			y0=y1;
		}
		for(int i=0;i<4;i++)A[i]=f(X[i],Y[i]);
		for(;x0<x;x0+=h)
		{
			Y4=Y[3]+(55*A[3]-59*A[2]+37*A[1]-9*A[0])*h/24;
			Y[3]=Y4;
			A[0]=A[1];
			A[1]=A[2];
			A[2]=A[3];
			A[3]=f(x0,Y[3]);
		}
		return Y4;
	}
	private double f(double x,double y)
	{
		return -3*y+x*x*x;
	}
}
