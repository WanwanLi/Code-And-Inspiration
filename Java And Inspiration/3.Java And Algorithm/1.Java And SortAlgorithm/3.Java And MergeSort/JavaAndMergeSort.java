public class JavaAndMergeSort
{
	public static void main(String[] args)
	{
		int l=10;
		double[] array=new double[l];
		for(int i=0;i<l;i++)array[i]=(Math.random()*100);
		MergeSort MergeSort1=new MergeSort(array);
		MergeSort1.display();
		MergeSort1.mergeSort(0,array.length-1);
		array=MergeSort1.getArray();
		for(int i=1;i<l;i++)if(array[i]<array[i-1])System.out.println("false="+i);
		MergeSort1.display();
	}

}
class MergeSort
{
	private double[] array;
	private int[] index;
	public MergeSort(double[] array)
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
	private void merge(int low,int mid,int high)
	{
		int i=low,j=mid+1,k=0;
		double[] newArray=new double[high-low+1];
		int[] newIndex=new int[high-low+1];
		while(i<=mid&&j<=high)
		{
			if(array[i]<=array[j])
			{

				newArray[k]=array[i];
				newIndex[k++]=index[i++];
			}
			else
			{
				newArray[k]=array[j];
				newIndex[k++]=index[j++];
			}
		}
		while(i<=mid)
		{
			newArray[k]=array[i];
			newIndex[k++]=index[i++];
		}
		while(j<=high)
		{
			newArray[k]=array[j];
			newIndex[k++]=index[j++];
		}
		for(i=low,k=0;i<=high;i++,k++)
		{
			this.array[i]=newArray[k];
			this.index[i]=newIndex[k];
		}
	}
	public void mergeSort(int low,int high)
	{
		if(low<high)
		{
			int mid=(low+high)/2;
			this.mergeSort(low,mid);
			this.mergeSort(mid+1,high);
			this.merge(low,mid,high);
		}
	}
	public void display()
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+index[i]+"]="+array[i]);
	}
}
class NewMergeSort
{
	private double[] array;
	public NewMergeSort(double[] array)
	{
		this.array=array;
	}
	private void merge(int low,int mid,int high)
	{
		int i=low,j=mid+1,k=0;
		double[] a=new double[high-low+1];
		while(i<=mid&&j<=high)if(array[i]<=array[j])a[k++]=array[i++];else a[k++]=array[j++];
		while(i<=mid)a[k++]=array[i++];
		while(j<=high)a[k++]=array[j++];
		for(i=low,k=0;i<=high;i++,k++)array[i]=a[k];
	}
	private void mergeOnce(int length)
	{
		int i;for(i=0;i+2*length<array.length;i+=2*length)merge(i,i+length-1,i+2*length-1);
		if(i+length<array.length)merge(i,i+length-1,array.length-1);
	}
	public void mergeSort()
	{
		for(int l=1;l<array.length;l*=2)mergeOnce(l);
	}
	public void displayArray()
	{
		for(int i=0;i<array.length;i++)System.out.println(array[i]);
	}
}












