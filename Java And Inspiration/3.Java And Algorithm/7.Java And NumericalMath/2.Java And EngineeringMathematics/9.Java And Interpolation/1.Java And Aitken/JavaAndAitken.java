public class JavaAndAitken
{
	public static void main(String[] args)
	{
		double[] x=new double[]{0.1,0.15,0.3,0.45,0.55,0.6,0.7,0.85,0.9,1};
		int n=x.length;
		double[] fx=new double[n];
		for(int i=0;i<n;i++)fx[i]=Math.sin(x[i]);
		Aitken Aitken1=new Aitken();
		double X=0.8924736910453573;
		System.out.println(Aitken1.An(n,x,fx,X));
	}
}
class Aitken
{
	public double An(int n,double[] x,double[] fx,double X)
	{
		double[] A=new double[n];
		A[0]=fx[0];
		int i=1;
		for(i=1;i<n;i++)
		{
			A[i]=fx[i];
			for(int j=0;j<i;j++)A[i]=(X-x[j])/(x[i]-x[j])*A[i]+(X-x[i])/(x[j]-x[i])*A[j];
			if(A[i]==A[i-1])break;
		}
		return A[i-1];
	}
}