public class JavaAndLinearProgramming
{
	public static void main(String[] args)
	{

/*
		double[] a=new double[]{3,2,2.9};
		double[][] s=new double[][]
		{
			{8,16,10},
			{10,5,8},
			{2,13,10}
		};
		double[] b=new double[]
		{
			304,
			400,
			420
		};

		double[] a=new double[]{3,2,2.9};
		double[][] s=new double[][]
		{
			{8,16,10},
			{10,5,8},
			{2,13,10}
		};
		double[] b=new double[]
		{
			364,
			400,
			420
		};

		double[] a=new double[]{3,2,2.9,2.1};
		double[][] s=new double[][]
		{
			{8,16,10,12},
			{10,5,8,5},
			{2,13,10,10}
		};
		double[] b=new double[]
		{
			304,
			400,
			420
		};
*/
		double[] a=new double[]{3,2,2.9,1.87};
		double[][] s=new double[][]
		{
			{8,16,10,4},
			{10,5,8,4},
			{2,13,10,12}
		};
		double[] b=new double[]
		{
			304,
			400,
			420
		};

		int n=a.length;
		int m=b.length;
		LinearProgramming LinearProgramming1=new LinearProgramming(m,n,a,s,b);
		double[] r=LinearProgramming1.getResult();
		for(int j=0;j<n;j++)System.out.println("result["+j+"]="+r[j]);
		double Max=0;
		for(int j=0;j<n;j++)Max+=r[j]*a[j];
		System.out.println("Max="+Max);
	}
}
class LinearProgramming
{
	double[] a;
	double[][] s;
	double[] b;
	int[] p;
	int m;
	int n;
	int I;
	int J;
	final double INF=11235813;
	public LinearProgramming(int m,int n,double[] a,double[][] s,double[] b)
	{
		this.a=new double[m+n];
		this.s=new double[m][m+n];
		this.b=new double[m];
		this.p=new int[m];
		this.m=m;
		this.n=n;
		this.I=0;
		this.J=0;
		for(int i=0;i<m;i++)this.b[i]=b[i];
		for(int i=0;i<m;i++)this.p[i]=i+n;
		for(int j=0;j<n;j++)this.a[j]=a[j];
		for(int j=n;j<m+n;j++)this.a[j]=0;
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)this.s[i][j]=s[i][j];
		for(int i=0;i<m;i++)this.s[i][i+n]=1;
	}
	private double getMaxA()
	{
		double maxA=-INF;
		J=0;
		for(int j=0;j<m+n;j++)
		{
			if(a[j]>maxA)
			{
				maxA=a[j];
				J=j;
			}
		}
		return maxA;
	}
	private void getMinB_S()
	{
		
		double minB_S=INF;
		I=0;
		for(int i=0;i<m;i++)
		{
			if(s[i][J]!=0)
			{
				double b_s=b[i]/s[i][J];
				if(b_s<minB_S)
				{
					minB_S=b_s;
					I=i;
				}
			}
		}
	}
	private void setSByJordan()
	{
		double sIJ=s[I][J];
		for(int i=0;i<m;i++)
		{
			if(i!=I)
			{
				double k=s[i][J]/s[I][J];
				for(int j=0;j<m+n;j++)s[i][j]-=k*s[I][j];
				b[i]-=k*b[I];
			}
		}
		double k=a[J]/s[I][J];
		for(int j=0;j<m+n;j++)a[j]-=k*s[I][j];
		for(int j=0;j<m+n;j++)s[I][j]/=sIJ;
		b[I]/=sIJ;
		p[I]=J;
	}
	private void printTable()
	{
		String s="b\t";
		for(int j=0;j<m+n;j++)s+="s"+j+"\t";
		s+="\n";
		for(int i=0;i<m;i++)
		{
			s+=b[i]+"\t";
			for(int j=0;j<m+n;j++)s+=this.s[i][j]+"\t";
			s+="\n";
		}
		s+="a\t";
		for(int j=0;j<m+n;j++)s+=a[j]+"\t";
		s+="\n";
		System.out.println(s);
	}
	public double[] getResult()
	{
		this.printTable();
		double[] result=new double[n];
		double maxA=this.getMaxA();
		int z=0;
		while(maxA>0)
		{
			this.getMinB_S();
			this.setSByJordan();
			this.printTable();
			maxA=this.getMaxA();
			if(z>100){System.out.println("No Best Solution!");break;}
			z++;
		}
		for(int j=0;j<n;j++)
		{
			int i=0;
			for(i=0;i<m;i++)if(p[i]==j)break;
			if(i!=m)result[j]=b[i];
			else System.out.println("error!");
		}
		return result;
	}
}