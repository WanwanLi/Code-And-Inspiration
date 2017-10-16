public class JavaAndNewton
{
	public static void main(String[] args)
	{
		int n=6;
		double[] x=new double[]{100,121,144,169,196,225};
		double[] fx=new double[]{10,11,12,13,14,15};
		Newton Newton1=new Newton();
		double X=115;
		System.out.println("Newton.Nn("+X+")="+Newton1.Nn(n,x,fx,X));
		System.out.println("Math.sqrt("+X+")="+Math.sqrt(X));
		System.out.println("Newton.Nn("+X+")-Math.sqrt("+X+")="+(Newton1.Nn(n,x,fx,X)-Math.sqrt(X)));
	}
}
class Newton
{
	public double Nn(int n,double[] x,double[] fx,double X)
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
}
