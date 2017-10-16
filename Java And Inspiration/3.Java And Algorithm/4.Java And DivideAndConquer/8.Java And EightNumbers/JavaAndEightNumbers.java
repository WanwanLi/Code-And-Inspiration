public class JavaAndEightNumbers
{
	public static void main(String[] args)
	{
		EightNumbers EightNumbers1=new EightNumbers();
		int[] initialArray=new int[]{2,6,4,1,3,7,0,5,8};
		int[] ultimateArray=new int[]{2,6,4,1,0,7,5,3,8};
		EightNumbers1.showAllPosibleMovesFromNumbers(initialArray,ultimateArray);
	}
}
class EightNumbers
{
	class ArrayNode
	{
		public int[] Array=new int[9];
		public ArrayNode Next;
		public ArrayNode(int[] array)
		{
			for(int i=0;i<=8;i++)Array[i]=array[i];
			Next=null;
		}
	}
	class ArrayQueue
	{
		ArrayNode Front;
		ArrayNode Rear;
		public ArrayQueue()
		{
			Front=Rear=null;
		}
		public void addArray(int[] array)
		{
			ArrayNode node=new ArrayNode(array);
			if(Front==null)Front=Rear=node;
			else
			{
				Rear.Next=node;
				Rear=Rear.Next;
			}
		}
		public int[] getArray()
		{
			int[] array=new int[9];
			if(Front!=null)
			{
				for(int i=0;i<=8;i++)array[i]=Front.Array[i];
				Front=Front.Next;
			}
			return array;
		}
		public boolean isNotEmpty()
		{
			return (Front!=null);
		}
	}
	int[] Numbers=new int[9];
	int[] di={-1,1,0,0};
	int[] dj={0,0,-1,1};
	ArrayQueue Q;
	public EightNumbers()
	{
		Q=new ArrayQueue();
	}
	private boolean isSameArray(int[] array1,int[] array2)
	{
		for(int i=0;i<=8;i++)
		{
			if(array1[i]!=array2[i])return false;
		}
		return true;
	}
	private void showNumbers()
	{
		System.out.print("\n________");
		for(int i=0;i<=8;i++)
		{
			if(i%3==0)System.out.println();
			if(Numbers[i]==0)System.out.print("  ");
			else System.out.print(Numbers[i]+" ");
		}
	}
	private int[] copyArray(int[] array)
	{
		int[] t=new int[9];
		for(int i=0;i<=8;i++)t[i]=array[i];
		return t;
	}
	public void showAllPosibleMovesFromNumbers(int[] initialArray,int[] ultimateArray)
	{
		Q.addArray(initialArray);
		while(Q.isNotEmpty())
		{
			Numbers=Q.getArray();
			this.showNumbers();
			if(isSameArray(Numbers,ultimateArray))System.exit(0);
			int k0=0;
			for(;k0<=8;k0++)if(Numbers[k0]==0)break;
			int i0=k0/3,j0=k0%3;
			for(int n=0;n<4;n++)
			{
				int i1=i0+di[n];
				int j1=j0+dj[n];
				int k1=i1*3+j1;
				if(i1>=0&&i1<3&&j1>=0&&j1<3)
				{
					int[] t=this.copyArray(Numbers);
					t[k1]=Numbers[k0];	
					t[k0]=Numbers[k1];	
					Q.addArray(t);
				}
			}
		}
	}
}