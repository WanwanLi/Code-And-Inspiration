public class JavaAndBitonicSort
{
	public static void main(String[] args)
	{
		int l=8;
		boolean isAscending=true;
		double[] array=new double[l];
		for(int i=0;i<l;i++)array[i]=(Math.random()*100);
		BitonicSort BitonicSort1=new BitonicSort(array);
		BitonicSort1.bitonicSort(0,array.length,isAscending);
		array=BitonicSort1.getArray();
		for(int i=1;i<l;i++)if((array[i-1]<=array[i])!=isAscending)System.out.println("false="+i);
		BitonicSort1.display();
	}
}
class BitonicSort
{
	private double[] array;
	public BitonicSort(double[] array)
	{
		int l=array.length;
		this.array=new double[l];
		for(int i=0;i<l;i++)this.array[i]=array[i];
	}
	public double[] getArray()
	{
		return this.array;
	}
	public void bitonicSort(int begin,int length,boolean isAscending)
	{
		if(length<=1)return;
		int half_length=length/2;
		this.bitonicSort(begin,half_length,true);
		this.bitonicSort(begin+half_length,half_length,false);
		this.bitonicMerge(begin,length,isAscending);		
	}
	private void bitonicMerge(int begin,int length,boolean isAscending)
	{
		if(length<=1)return;
		int half_length=length/2;
		for(int n=0;n<half_length;n++)
		{
			int i=begin+n;
			int j=i+half_length;
			this.compare(i,j,isAscending);
		}
		this.bitonicMerge(begin,half_length,isAscending);
		this.bitonicMerge(begin+half_length,half_length,isAscending);
	}
	private void compare(int i,int j,boolean isAscending)
	{
		if((this.array[i]<=this.array[j])!=isAscending)this.swap(i,j);
	}
	private void swap(int i,int j)
	{
		double array_i=this.array[i];
		this.array[i]=this.array[j];
		this.array[j]=array_i;
	}
	public void display()
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+i+"]="+array[i]);
	}
}