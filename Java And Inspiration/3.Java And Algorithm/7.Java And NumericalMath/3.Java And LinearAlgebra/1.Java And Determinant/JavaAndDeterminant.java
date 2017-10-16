public class JavaAndDeterminant
{
	public static void main(String[] args)
	{
		double[][] matrix=new double[][]
		{
			{3,0,4},
			{1,1,2},
			{2,1,0}
		};
		System.out.println("using recursive method result is :"+Determinant.det(matrix));
		matrix=new double[][]
		{
			{1,2,-3,4},
			{2,3,-4,7},
			{-1,-2,5,-8},
			{1,3,-5,10}
		};
		Determinant Determinant1=new Determinant(matrix);
		System.out.println("using iterative method result is :"+Determinant1.getResult());
	}
}
class Determinant
{
	private double[][] d;
	public Determinant(double[][] determinant)
	{
		int l=determinant.length;
		this.d=new double[l][l];
		for(int i=0;i<l;i++)for(int j=0;j<l;j++)this.d[i][j]=determinant[i][j];
	}
	public double getResult()
	{
		double D=0;
		int n=d.length;
		for(int k=0;k<n;k++)
		{
			for(int i=k+1;i<n;i++)
			{
				D=d[i][k]/d[k][k];
				for(int j=k;j<n;j++)this.d[i][j]-=D*d[k][j];
			}
			print(d);
		}
		double result=1.0;
		for(int k=0;k<n;k++)result*=d[k][k];
		return result;
	}
	public static double det(double[][] matrix)
	{
		int l=matrix.length;
		if(l==2)return matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0];
		double e=1.0;
		double result=0;
		double[][][] cofactors=new double[l][][];
		for(int j=0;j<l;j++)
		{
			cofactors[j]=getCofactor(matrix,0,j);
			result+=e*matrix[0][j]*det(cofactors[j]);
			e*=-1;
		}
		return result;
	}
	private static double[][] getCofactor(double[][] matrix,int row,int column)
	{
		int l=matrix.length-1;
		double[][] cofactor=new double[l][l];
		for(int i=0;i<l;i++)
		{
			for(int j=0;j<l;j++)
			{
				if(i<row&&j<column)cofactor[i][j]=matrix[i][j];
				else if(i>=row&&j<column)cofactor[i][j]=matrix[i+1][j];
				else if(i<row&&j>=column)cofactor[i][j]=matrix[i][j+1];
				else cofactor[i][j]=matrix[i+1][j+1];
			}
		}
		return cofactor;
	}
	private static void print(double[][] matrix)
	{
		int l=matrix.length;
		System.out.println("_________________________________________________________________________________\n");
		for(int i=0;i<l;i++)
		{
			for(int j=0;j<l;j++)
			{
				System.out.print("\t"+matrix[i][j]+"\t");
			}
			System.out.println();
		}
		System.out.println("_________________________________________________________________________________");
	}
}