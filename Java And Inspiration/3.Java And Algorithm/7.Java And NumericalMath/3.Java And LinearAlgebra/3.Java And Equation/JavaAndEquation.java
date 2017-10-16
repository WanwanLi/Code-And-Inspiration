public class JavaAndEquation
{
	public static void main(String[] args)
	{
		test_Equation_Basis();
//		test_Equation_Particular();
	}
	private static void test_Equation_Basis()
	{
		double[][] a=new double[][]
		{
			{1,1,1,-1},
			{1,-1,1,-3},
			{1,3,1,1}
		};
		Equation Equation1=new Equation(a);
		Equation1.getSolutions();
		Equation1.print();
	}
	private static void test_Equation_Particular()
	{
		double[][] a=new double[][]
		{
			{1,1,-2,-1},
			{3,-2,-1,2},
			{0,5,7,3},
			{2,-3,-5,-1}
		};
		double[] b=new double[]
		{
			4,
			2,
			-2,
			4
		};
		Equation Equation1=new Equation(a);
		Equation1.getSolutions(b);
		Equation1.print();
	}
}
class Equation
{
	private double[][] a;
	private double[][] b;
	private double[][] bs;
	private double[] ps;
	private int m;
	private int n;
	public Equation(double[][] a)
	{
		this.m=a.length;
		this.n=a[0].length;
		this.init(a);
	}
	private void init(double[][] a)
	{
		Matrix Matrix1=new Matrix(a);
		this.m=Matrix1.getRank();
		this.a=new double[m][m];
		this.b=new double[n-m][m];
		for(int i=0;i<m;i++)for(int j=0;j<m;j++)this.a[i][j]=a[i][j];
		for(int j=0;j<m;j++)for(int i=0;i<n-m;i++)this.b[i][j]=-a[j][i+m];
	}
	public void getSolutions()
	{
Matrix.print(a);
Matrix.print(b);
		double[][] X=new double[n-m][];
		for(int i=0;i<n-m;i++)X[i]=(new Gauss()).getX(a,b[i]);
Matrix.print(X);
		double[][] basisSolutions=new double[n-m][n];
		for(int i=0;i<n-m;i++)
		{
			for(int j=0;j<m;j++)basisSolutions[i][j]=X[i][j];
			basisSolutions[i][m+i]=1;
		}
		this.bs=basisSolutions;
	}
	public void getSolutions(double[] b)
	{
		if(m==n){this.ps=(new Gauss()).getX(a,b);return;}
		this.getSolutions();
		double[] B=new double[m];
		for(int i=0;i<m;i++)B[i]=b[i];
		double[] x=(new Gauss()).getX(a,B);
		double[] particularSolution=new double[n];
		for(int i=0;i<m;i++)particularSolution[i]=x[i];
		this.ps=particularSolution;
	}
	public void print()
	{
		if(bs!=null)
		{
			System.out.println("The Basis Solutions are:");
			for(int i=0;i<n-m;i++)
			{
				System.out.print("(");
				for(int j=0;j<n-1;j++)System.out.print(bs[i][j]+",");
				System.out.println(bs[i][n-1]+")");
			}
		}
		if(ps!=null)
		{
			System.out.println("The particular Solution is:");
			System.out.print("(");
			for(int j=0;j<n-1;j++)System.out.print(ps[j]+",");
			System.out.println(ps[n-1]+")");
		}
	}
}
class Matrix
{
	private double[][] a;
	private int m;
	private int n;
	public Matrix(double[][] matrix)
	{
		this.m=matrix.length;
		this.n=matrix[0].length;
		this.a=new double[m][n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)this.a[i][j]=matrix[i][j];
	}
	public void add(double[][] matrix)
	{
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)this.a[i][j]+=matrix[i][j];
	}
	public void sub(double[][] matrix)
	{
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)this.a[i][j]-=matrix[i][j];
	}
	public void mul(double scalar)
	{
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)this.a[i][j]*=scalar;
	}
	public void mul(double[][] matrix)
	{
		int t=this.n;
		this.n=matrix[0].length;
		double[][] b=new double[m][n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)for(int k=0;k<t;k++)b[i][j]+=a[i][k]*matrix[k][j];
		this.a=b;
	}
	public void transpose()
	{
		double[][] b=new double[n][m];
		for(int i=0;i<n;i++)for(int j=0;j<m;j++)b[i][j]=a[j][i];
		this.a=b;
		this.m=a.length;
		this.n=a[0].length;
	}
	public void inverse()
	{
		if(m!=n)return;
		double[][] AE=new double[n][n+n];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)AE[i][j]=a[i][j];
			AE[i][n+i]=1;
		}
		for(int k=0;k<n;k++)
		{
			for(int i=0;i<n;i++)
			{
				if(i==k)continue;
				double M=AE[i][k]/AE[k][k];
				for(int j=k;j<n+n;j++)AE[i][j]-=M*AE[k][j];
			}
		}
		for(int i=0;i<n;i++)
		{
			double M=AE[i][i];
			for(int j=0;j<n+n;j++)AE[i][j]/=M;
		}
		for(int i=0;i<n;i++)for(int j=0;j<n;j++)this.a[i][j]=AE[i][n+j];
	}
	private double abs(double x)
	{
		return (x<0?-x:x);
	}
	public double[][] getEchelonMatrix()
	{
		double[][] a=new double[m][n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)a[i][j]=this.a[i][j];
		int r=0,c=0;
		while(r<m&&c<n)
		{
			int maxR=r;
			double max=0.0;
			for(int i=r;i<m;i++)
			{
				if(abs(a[i][c])>0.0)
				{
					maxR=i;
					max=abs(a[i][c]);
					break;
				}
			}
			if(max==0.0)
			{
				c++;
				continue;
			}
			if(maxR!=r)
			{
				for(int j=c;j<n;j++)
				{
					double t=a[maxR][j];
					a[maxR][j]=a[r][j];
					a[r][j]=t;
				}
			}
			for(int i=r+1;i<m;i++)
			{
				double R=a[i][c]/a[r][c];
				for(int j=c;j<n;j++)a[i][j]-=R*a[r][j];
			}
			r++;
			c++;
		}
		return a;
	}
	public int getRank()
	{
		double[][] echelonMatrix=this.getEchelonMatrix();
		for(int i=m-1;i>=0;i--)
		{
			for(int j=n-1;j>=0;j--)if(echelonMatrix[i][j]!=0.0)return (i+1);
		}
		return 0;
	}
	public double[][] getMatrix()
	{
		double[][] matrix=new double[m][n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)matrix[i][j]=a[i][j];
		return matrix;
	}
	private static String format(double d)
	{
		String s=d+"";
		if(s.length()>=6&&!s.contains("E"))return s.substring(0,6);
		return s;
	}
	public static void print(double[][] matrix)
	{
		int m=matrix.length;
		int n=matrix[0].length;
		for(int i=0;i<n*18;i++)System.out.print("_");System.out.println("\n");
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				System.out.print("\t"+format(matrix[i][j])+"\t");
			}
			System.out.println();
		}
		for(int i=0;i<n*18;i++)System.out.print("_");System.out.println("\n");
	}
	public void print()
	{
		for(int i=0;i<n*18;i++)System.out.print("_");System.out.println("\n");
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				System.out.print("\t"+format(a[i][j])+"\t");
			}
			System.out.println();
		}
		for(int i=0;i<n*18;i++)System.out.print("_");System.out.println("\n");
	}
}
class Gauss
{
	private double Abs(double d)
	{
		return (d>=0?d:-d);
	}
	public double[] getX(double[][] A,double[] B)
	{
		int n=A.length;
		double[][] a=new double[n][n];
		for(int i=0;i<n;i++)for(int j=0;j<n;j++)a[i][j]=A[i][j];
		double[] b=new double[n];
		for(int i=0;i<n;i++)b[i]=B[i];
		int[] R=new int[n];
		double G=0;
		double[] x=new double[n];
		for(int i=0;i<n;i++)R[i]=i;
		for(int k=0;k<n;k++)
		{
			int r=k;
			int c=k;
			double abs=Abs(a[k][k]);
			for(int i=k;i<n;i++)
			{
				for(int j=k;j<n;j++)
				{
					if(Abs(a[i][j])>abs)
					{
						abs=Abs(a[i][j]);
						r=i;
						c=j;
					}
				}
			}
			if(r!=k)
			{
				double t=0;
				for(int j=0;j<n;j++)
				{
					t=a[r][j];
					a[r][j]=a[k][j];
					a[k][j]=t;
				}
				t=b[r];
				b[r]=b[k];
				b[k]=t;
			}
			if(c!=k)
			{
				for(int i=0;i<n;i++)
				{
					double t=a[i][c];
					a[i][c]=a[i][k];
					a[i][k]=t;
				}
				int Rc=R[c];
				R[c]=R[k];
				R[k]=Rc;
			}
			for(int i=k+1;i<n;i++)
			{
				
				G=a[i][k]/a[k][k];
				for(int j=k;j<n;j++)a[i][j]-=G*a[k][j];
				b[i]-=G*b[k];		
			}
		}
		for(int i=n-1;i>=0;i--)
		{
			G=0;
			for(int j=i+1;j<n;j++)G+=a[i][j]*x[j];
			x[i]=(b[i]-G)/a[i][i];
		}
		double[] X=new double[n];
		for(int i=0;i<n;i++)X[R[i]]=x[i];
		return X;
	}
}