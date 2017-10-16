import java.util.*;
public class JavaAndDirectInsertSort
{
	public static void main(String[] args)
	{
		int[] array=new int[5];
		Scanner scanner=new Scanner(System.in);
		for(int i=0;i<array.length;i++)array[i]=scanner.nextInt();
		DirectInsertSort(array);
	}
	public static void DirectInsertSort(int[] array)
	{
		for(int i=1;i<array.length;i++)
		{
			int t=array[i];int j;
			for(j=i-1;j>=0&&t<array[j];j--)array[j+1]=array[j];
			array[j+1]=t;
		}
		for(int i=0;i<array.length;i++)System.out.println(array[i]);
	}
}
class DirectInsertSort
{
	public static int[] getInsertSortArray(int[] array)
	{
		int l=array.length;
		int[] newArray=new int[l];
		for(int i=0;i<l;i++)newArray[i]=Integer.MIN_VALUE;
		for(int i=0;i<l;i++)newArray=insertArray(newArray,array[i]);
		return newArray;
	}
	private static int[] insertArray(int[] array,int integer)
	{
		int length=array.length;
		int[] newArray=new int[length];
		int index=0;
		while(index<length&&integer<=array[index])index++;
		if(index==length)return array;
		for(int i=0;i<index;i++)newArray[i]=array[i];
		newArray[index]=integer;
		for(int i=index+1;i<length;i++)newArray[i]=array[i-1];
		return newArray;
	}
}