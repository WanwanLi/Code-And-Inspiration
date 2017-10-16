public class JavaAndKnapsack
{
	public static void main(String[] args)
	{
		int[] weight=new int[]{5,3,2,1};
		int[] value=new int[]{4,4,3,1};
		Knapsack Knapsack0=new Knapsack(4,7,weight,value);
		Knapsack0.showOption();
	}
}
class Knapsack
{
	class Goods
	{
		public int Weight;
		public int Value;
		public boolean isSelected;
		public Goods(int weight,int value)
		{
			this.Weight=weight;
			this.Value=value;
			this.isSelected=false;
		}
	}
	int number;
	int limitWeight;
	int maxWeight;
	int maxValue;
	Goods[] goods;
	boolean[] finalSelection;
	public Knapsack(int amount,int carryingCapacity,int[] weight,int[] value)
	{
		number=amount;
		limitWeight=carryingCapacity;
		goods=new Goods[number];
		finalSelection=new boolean[number];
		for(int i=0;i<number;i++)goods[i]=new Goods(weight[i],value[i]);
	}	
	private void tryGoods(int i,int weight,int value)
	{
		if(i>=number)
		{
			if(value>maxValue)
			{
				maxValue=value;
				maxWeight=weight;
				for(int k=0;k<number;k++)finalSelection[k]=goods[k].isSelected;
			}
		}
		else
		{
			if(weight+goods[i].Weight<limitWeight)
			{
				goods[i].isSelected=true;
				tryGoods(i+1,weight+goods[i].Weight,value+goods[i].Value);
			}
			goods[i].isSelected=false;
			tryGoods(i+1,weight,value);
		}
	}
	public void showOption()
	{
		this.tryGoods(0,0,0);
		String s="Solve Knapsack Problem:\n\n";
		for(int i=0;i<number;i++)goods[i].isSelected=finalSelection[i];
		for(int i=0;i<number;i++)if(goods[i].isSelected)s+="Goods["+i+"]:Weight="+goods[i].Weight+"\tValue="+goods[i].Value+"\n";
		s+="Total Weight="+maxWeight+"\nTotal Value="+maxValue;
		System.out.println(s);
		System.exit(0);		
	}

}






