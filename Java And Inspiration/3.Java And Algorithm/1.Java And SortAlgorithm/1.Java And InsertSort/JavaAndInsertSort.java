public class JavaAndInsertSort
{
	public static void main(String[] args)
	{
		int l=100;
		double[] array=new double[l];
		for(int i=0;i<l;i++)array[i]=(Math.random()*100);
		InsertSort InsertSort1=new InsertSort(array);
		InsertSort1.display();
		InsertSort1.insertSort(0,array.length-1);
		array=InsertSort1.getArray();
		for(int i=1;i<l;i++)if(array[i]<array[i-1])System.out.println("false="+i);
		InsertSort1.display();
	}

}
class InsertSort
{
	private double[] array;
	private int[] index;
	public InsertSort(double[] array)
	{
		int l=array.length;
		this.array=new double[l];
		this.index=new int[l];
		for(int i=0;i<l;i++)
		{
			this.array[i]=array[i];
			this.index[i]=i;
		}
	}
	public double[] getArray()
	{
		return this.array;
	}
	public void insertSort(int low,int high)
	{
		int l=array.length;
		for(int i=low+1;i<=high;i++)
		{
			int j=i-1;
			double array_i=array[i];
			for(;j>=low&&array_i<array[j];j--)
			{
				this.array[j+1]=array[j];
				this.index[j+1]=index[j];
			}
			this.array[j+1]=array_i;
			this.index[j+1]=i;
		}
	}
	public void display()
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+index[i]+"]="+array[i]);
	}
}