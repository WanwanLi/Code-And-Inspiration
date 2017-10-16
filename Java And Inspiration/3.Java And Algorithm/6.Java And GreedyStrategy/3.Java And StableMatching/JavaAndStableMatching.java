class JavaAndStableMatching
{
	public static void main(String[] args)
	{
		String[][] preferList_XYZ=
		{
			{"X", "A","B","C"},
			{"Y", "B","A","C"},
			{"Z", "A","B","C"}
		};
		String[][] preferList_ABC=
		{
			{"A", "Y","X","Z"},
			{"B", "X","Y","Z"},
			{"C", "X","Y","Z"}
		};
		StableMatching stableMatching=new StableMatching();
		String[][] stableMatchingTable=stableMatching.getResult(preferList_XYZ,preferList_ABC);
		for(int i=0;i<stableMatchingTable.length;i++)
		{
			System.out.print(stableMatchingTable[i][0]+" --- ");
			System.out.println(stableMatchingTable[i][1]);
		}
	}
}
class StableMatching
{
	int[] stableMatchingList;
	boolean[] isMatched;
	String[] identityList;
	int[][] preferTable0;
	int[][] preferTable1;
	int[][] priorityTable;
	int length=0;
	private void createIdentityList(String[][] preferList0,String[][] preferList1)
	{
		this.length=preferList0.length;int i=0;
		this.identityList=new String[2*length];
		for(;i<length;i++)this.identityList[i]=preferList0[i][0];
		for(;i<2*length;i++)this.identityList[i]=preferList1[i-length][0];
	}
	private int[][] getPreferTable(String[][] preferList)
	{
		int[][] preferTable=new int[length][length];
		for(int i=0;i<length;i++)
		{
			for(int j=0;j<length;j++)
			{
				String ID=preferList[i][j+1];
				int k=getIdentityIndex(ID);
				preferTable[i][j]=k;
			}
		}
		return preferTable;
	}
	private void createPriorityTable()
	{
		this.priorityTable=new int[length][length];
		for(int i=0;i<length;i++)
		{
			for(int j=0;j<length;j++)
			{
				int k=this.preferTable1[i][j];
				this.priorityTable[i][k]=length-1-j;
			}
		}
	}
	public void createStableMatchingList()
	{
		int matchedNumber=0;
		this.stableMatchingList=new int[2*length];
		this.isMatched=new boolean[2*length];
		while(matchedNumber<length)
		{
			for(int i=0;i<length;i++)
			{
				if(isMatched[i])continue;
				for(int j=0;j<length;j++)
				{
					int k=preferTable0[i][j],w=k-length;
					int m=stableMatchingList[k];
					if(!isMatched[k])
					{
						matchedNumber++;
						this.match(i,k);
						break;
					}
					else if(priorityTable[w][i]>priorityTable[w][m])
					{
						this.seperate(m,k);
						this.match(i,k);
						break;
					}
				}
			}
		}
	}
	public String[][] getResult(String[][] preferList0,String[][] preferList1)
	{
		this.createIdentityList(preferList0,preferList1);
		this.preferTable0=getPreferTable(preferList0);
		this.preferTable1=getPreferTable(preferList1);
		this.createPriorityTable();
		this.createStableMatchingList();
		String[][] stableMatchingTable=new String[length][2];
		for(int i=0;i<length;i++)
		{
			int j=stableMatchingList[i];
			String m=this.identityList[i];
			String w=this.identityList[j];
			stableMatchingTable[i][0]=m;
			stableMatchingTable[i][1]=w;
		}
		return stableMatchingTable;
	}
	private void match(int i,int k)
	{
		this.stableMatchingList[i]=k;
		this.stableMatchingList[k]=i;
		this.isMatched[i]=true;
		this.isMatched[k]=true;
	}
	private void seperate(int i,int k)
	{
		this.isMatched[i]=false;
		this.isMatched[k]=false;
	}
	private int getIdentityIndex(String ID)
	{
		for(int i=0;i<this.identityList.length;i++)
		{
			if(this.identityList[i].equals(ID))return i;
		}
		return -1;
	}
}
