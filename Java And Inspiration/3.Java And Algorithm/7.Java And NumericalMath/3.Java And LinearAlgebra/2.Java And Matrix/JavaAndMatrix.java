public class JavaAndMatrix
{
	public static void main(String[] args)
	{
		test_MatricesMultiplication();
	}
	private static void test_MatrixOperations()
	{
		double[][] A=new double[][]
		{
			{1,6},
			{2,7},
			{3,8}
		};
		double[][] B=new double[][]
		{
			{4,3},
			{1,4}
		};
		A=new double[][]
		{
			{1},
			{2},
			{-1}
		};
		B=new double[][]
		{
			{3,5,7}
		};
		Matrix Matrix1=new Matrix(A);
		Matrix1.transpose();
		System.out.println("The Transposition of A is:");
		Matrix1.print();
		Matrix1.transpose();
		Matrix1.mul(B);
		System.out.println("The Multiplication of A and B is:");
		Matrix1.print();
	}
	private static void test_MatrixInverse()
	{
		double[][] A=new double[][]
		{
			{-2,1,0},
			{1,-2,1},
			{0,1,-2}
		};
		Matrix Matrix1=new Matrix(A);
		Matrix1.inverse();
		System.out.println("The Inversion of A is:");
		Matrix1.print();
	}
	private static void test_MatrixRank()
	{
		double[][] A=new double[][]
		{
			{3,2,3,4,5,9},
			{3,1,0,2,1,5},
			{0,1,3,2,6,10},
			{6,4,6,8,12,24}
		};
		Matrix Matrix1=new Matrix(A);
		System.out.println("The Rank of A is:"+Matrix1.getRank());
	}
	private static void test_MatricesMultiplication()
	{
		double[][][] matrices=new double[][][]
		{
			new double[200][2000],
			new double[2000][100],
			new double[100][20],
			new double[20][300],
			new double[300][50]
		};
		for(int k=0;k<matrices.length;k++)
		{
			for(int i=0;i<matrices[k].length;i++)
			{
				for(int j=0;j<matrices[k][0].length;j++)matrices[k][i][j]=Math.random();
			}
		}
		double[][] A=Matrix.MUL(matrices);
		System.out.println("MUL finished");
		Matrix Matrix1=new Matrix(matrices[0]);
		for(int k=1;k<matrices.length;k++)Matrix1.mul(matrices[k]);
		System.out.println(Matrix1.equals(A));
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
	public static double[][] MUL(double[][] a,double[][] b)
	{
		int m=a.length;
		int t=b.length;
		int n=b[0].length;
		double[][] c=new double[m][n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)for(int k=0;k<t;k++)c[i][j]+=a[i][k]*b[k][j];
		return c;
	}
	public static double[][] MUL(double[][][] matrices)
	{
		int l=matrices.length;
		int[] row=new int[l];
		int[] column=new int[l];
		for(int i=0;i<l;i++)
		{
			double[][] matrix=matrices[i];
			row[i]=matrix.length;
			column[i]=matrix[0].length;
		}
		int[][] minCost=new int[l][l];
		int[][] midFlag=new int[l][l];
		for(int di=1;di<l;di++)
		{
			for(int i=0;i<l-di;i++)
			{
				int j=i+di;
				minCost[i][j]=Integer.MAX_VALUE;
				for(int k=i;k<j;k++)
				{
					int minCost_ij=minCost[i][k]+minCost[k+1][j]+row[i]*column[k]*column[j];
					if(minCost_ij<minCost[i][j])
					{
						minCost[i][j]=minCost_ij;
						midFlag[i][j]=k;
					}
				}
			}
		}
		return getMultiplicationOfMatrices(matrices,midFlag,0,l-1);
	}
	private static double[][] getMultiplicationOfMatrices(double[][][] matrices,int[][] midFlag,int i,int j)
	{
		if(i==j)return matrices[i];
		int k=midFlag[i][j];
		double[][] a=getMultiplicationOfMatrices(matrices,midFlag,i,k);
		double[][] b=getMultiplicationOfMatrices(matrices,midFlag,k+1,j);
		return MUL(a,b);
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
this.print(AE);
		for(int k=0;k<n;k++)
		{
			for(int i=0;i<n;i++)
			{
				if(i==k)continue;
				double M=AE[i][k]/AE[k][k];
				for(int j=k;j<n+n;j++)AE[i][j]-=M*AE[k][j];
			}
this.print(AE);
		}
		for(int i=0;i<n;i++)
		{
			double M=AE[i][i];
			for(int j=0;j<n+n;j++)AE[i][j]/=M;
		}
this.print(AE);
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
System.out.println("exchanged:"+maxR+","+r);
this.print(a);
			}
			for(int i=r+1;i<m;i++)
			{
				double R=a[i][c]/a[r][c];
System.out.print("\tR["+i+"]="+R);
				for(int j=c;j<n;j++)a[i][j]-=R*a[r][j];
			}
System.out.println();
			r++;
			c++;
this.print(a);
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
	public boolean equals(double[][] matrix)
	{
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)if(!isClose(matrix[i][j],a[i][j]))
{
System.out.println(matrix[i][j]+","+a[i][j]);
return false;
}
		return true;
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
	private boolean isClose(double d0,double d1)
	{
		double d=Math.abs(d0-d1);
		double e=1E-4;
		return (d<e);
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