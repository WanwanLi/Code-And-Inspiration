public class JavaAndRunge_Kutta3D
{
	public static void main(String[] args)
	{
		Runge_Kutta3D Runge_Kutta3D1=new Runge_Kutta3D();
		double x0=0.0;
		double[] y0={-0.4,-0.6};
		double x=1.0;
		double[] y=Runge_Kutta3D1.y(x0,y0,x);
		System.out.println("Runge_Kutta3D1.y[0]("+x+")="+y[0]);
		System.out.println("Runge_Kutta3D1.y[1]("+x+")="+y[1]);
	}
}
class Runge_Kutta3D
{
	int m=2;
	public double[] y(double x0,double[] y0,double x)
	{
		int n=1000;
		double h=(x-x0)/n;
		double x1=0.0;
		double[] y=new double[m];
		double[] y1=new double[n];
		double[] K1=new double[n];
		double[] K2=new double[n];
		double[] K3=new double[n];
		double[] K4=new double[n];
		for(int i=0;i<n;i++)
		{
			x1=x0+h;
			K1=f(x0,y0);
			for(int j=0;j<m;j++)y[j]=y0[j]+K1[j]*h/2;
			K2=f(x0+h/2,y);
			for(int j=0;j<m;j++)y[j]=y0[j]+K2[j]*h/2;
			K3=f(x0+h/2,y);
			for(int j=0;j<m;j++)y[j]=y0[j]+K3[j]*h;
			K4=f(x0+h,y);
			for(int j=0;j<m;j++)y1[j]=y0[j]+(K1[j]+2*K2[j]+2*K3[j]+K4[j])*h/6;
			x0=x1;
			for(int j=0;j<m;j++)y0[j]=y1[j];
		}
		return y0;
	}
	private double[] f(double x,double[] y)
	{
		double[] f=new double[m];
		f[0]=y[1];
		f[1]=2*(y[1]-y[0])+Math.exp(2*x)*Math.sin(x);
		return f;
	}
	private double[] f1(double x,double[] y)
	{
		double[] f=new double[m];
		f[0]=y[1];
		f[1]=2*(y[1]-y[0])+Math.exp(2*x)*Math.sin(x);
		return f;
	}
}