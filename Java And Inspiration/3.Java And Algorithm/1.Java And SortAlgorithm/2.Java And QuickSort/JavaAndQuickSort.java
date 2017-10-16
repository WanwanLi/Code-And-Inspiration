public class JavaAndQuickSort
{
	public static void main(String[] args)
	{
		int l=111;
		double[] array=new double[l];

//double[] array={ 5.7,25.2,41,7.2,63.2,34.7,25,84,30.3,74.8,18.1};

		for(int i=0;i<l;i++)array[i]=(Math.random()*100);
		QuickSort QuickSort1=new QuickSort(array);
	//	QuickSort1.println();
		QuickSort QuickSort2=new QuickSort(array);
		QuickSort1.quickSort(0,array.length-1);
		array=QuickSort1.getArray();
		for(int i=1;i<l;i++)if(array[i]<array[i-1])System.out.println("false="+i);
		System.out.println("QuickSort1.median="+array[(l-1)/2]);
		System.out.println("QuickSort2.median="+QuickSort2.getMedian(0,l-1));
	//	QuickSort1.display();
	}
}
class QuickSort
{
	private double[] array;
	public QuickSort(double[] array)
	{
		int l=array.length;
		this.array=new double[l];
		for(int i=0;i<l;i++)this.array[i]=array[i];
	}
	public double[] getArray()
	{
		return this.array;
	}
	public void quickSort(int begin,int end)
	{
		if(begin>=end)return;
		double pivot=array[begin];
		int low=begin+1;
		int high=end;
		while(low<high)
		{
			while(low<high&&array[high]>=pivot)high--;
			while(low<high&&array[low]<=pivot)low++;
			if(low<high)this.swap(low,high);
		}
		if(array[begin]>array[low])this.swap(begin,low);
		if(low==begin+1){this.quickSort(low,end);return;}
		if(begin<low-1)this.quickSort(begin,low-1);
		if(high+1<end)this.quickSort(high+1,end);
	}
	private int getMidIndex(int begin,int end)
	{
		if(begin>=end)return end;
		double pivot=array[begin];
		int low=begin+1;
		int high=end;
		while(low<high)
		{
			while(low<high&&array[high]>=pivot)high--;
			while(low<high&&array[low]<=pivot)low++;
			if(low<high)this.swap(low,high);
		}
		if(array[begin]>array[low])this.swap(begin,low);
		return low;
	}
	public double getMedian(int begin,int end)
	{
		if(begin>=end)return array[end];
		int low=begin;
		int high=end;
		int mid=high/2;
		int index=-1;
		while(index!=mid)
		{
			index=getMidIndex(low,high);
			if(index<mid)low=index+1==mid?mid:index;
			else if(index>mid)high=index-1==mid?mid:index;
			else break;
		}
		return array[mid];
	}
	private void swap(int i,int j)
	{
		double arrayi=this.array[i];
		this.array[i]=this.array[j];
		this.array[j]=arrayi;
	}
	public void display()
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+i+"]="+array[i]);
	}
	public void println()
	{
		System.out.print("array= ");
		for(int i=0;i<array.length;i++)System.out.print(" "+array[i]);
		System.out.println();
	}
}