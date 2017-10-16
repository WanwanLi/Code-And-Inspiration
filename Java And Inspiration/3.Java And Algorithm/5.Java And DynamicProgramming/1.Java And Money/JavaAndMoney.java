import java.io.*;
public class JavaAndMoney
{
	
	public static void main(String[] args)
	{
		double[] a;
		double[] b;
		int[] denominations=Money.getDefaultDenominations();
		int totalMoney=567;
		Money Money1=new Money(denominations,totalMoney);
		Money1.showResult();
	}
}
class Money
{
	private int[] denominations;
	private int totalMoney;
	private int[][] preMoney;
	private int[] counts;
	public Money(int[]  denominations,int totalMoney)
	{
		this.denominations=denominations;
		this.totalMoney=totalMoney;
		if(denominations[0]!=1)System.out.println("Exception in  thread \"Money\": denominations[0] should be 1");
		else this.getCounts();
	}
	public static int[] getDefaultDenominations()
	{
		return new int[]{1,5,10,20,50,100};
	}
	private void getCounts()
	{
		int length=denominations.length;
		int[][] minTotalCount=new int[length][totalMoney+1];
		this.preMoney=new int[length][totalMoney+1];
		for(int j=0;j<=totalMoney;j++)minTotalCount[0][j]=j/denominations[0];
		for(int i=1;i<length;i++)
		{
			for(int j=0;j<=totalMoney;j++)
			{
				minTotalCount[i][j]=Integer.MAX_VALUE;
				for(int k=0;k<=j/denominations[i];k++)
				{
					if(minTotalCount[i-1][j-k*denominations[i]]+k<minTotalCount[i][j])
					{
						minTotalCount[i][j]=minTotalCount[i-1][j-k*denominations[i]]+k;
						this.preMoney[i][j]=j-k*denominations[i];
					}
				}
			}
		}
		this.counts=new int[length];
		int total=totalMoney;
		int pre=preMoney[length-1][totalMoney];
		for(int i=length-1;i>=0;i--)
		{
			this.counts[i]=(total-pre)/denominations[i];
			total-=(total-pre);
			if(i==0)break;
			pre=preMoney[i-1][total];
		}
	}
	public void showResult()
	{
		String s="Total money is : "+totalMoney+"(Yuan) \nPaper money is : ";
		for(int i=0;i<denominations.length;i++)
		{
			s+=counts[i]+" X "+denominations[i]+"(Yuan)\t";
		}
		System.out.println(s);
	}
}