public class JavaAndCombination
{
	public static void main(String[] args)
	{
		int n=7;
		(new Combination(n)).println();
	}
}
class Combination
{
	public int[][] C;
	private int n;
	public Combination(int n)
	{
		this.n=n;
		this.C=new int[n+1][n+1];
		for(int i=0;i<=n;i++)
		{
			this.C[i][0]=1;
			this.C[i][i]=1;
		}
		for(int i=2;i<=n;i++)
		{
			for(int j=1;j<i;j++)
			{
				this.C[i][j]=C[i-1][j-1]+C[i-1][j];
			}
		}
	}
	public void println()
	{
		for(int i=0;i<=n;i++)
		{
			for(int j=0;j<=i;j++)
			{
				System.out.print(C[i][j]+"\t");
			}
			System.out.println();
		}
	}
}