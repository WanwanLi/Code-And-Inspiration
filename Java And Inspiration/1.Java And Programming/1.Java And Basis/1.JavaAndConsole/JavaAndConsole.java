import java.lang.System;
import java.util.Scanner;

public class JavaAndConsole
{	
	public static void main(String[] args)
	{
		System.out.print("Please input a string:");
		Scanner scanner=new Scanner(System.in);
		String s=scanner.nextLine();
		System.out.println("scanner.nextLine()="+s);
		System.out.print("Please input an integer:");
		int t=scanner.nextInt();
		System.out.println("scanner.nextInt()="+t);
		System.out.print("Please input a double:");
		double d=scanner.nextDouble();
		System.out.println("scanner.nextDouble()="+d);
		System.out.print("Safe int input:");
		int k=nextInt(scanner);
		System.out.println("nextInt(scanner)="+k);
	}
	static int nextInt(Scanner scanner)
	{
		try
		{
			return scanner.nextInt();
		}
		catch(Exception e)
		{
			System.out.print("Please input again:");
			scanner.nextLine();
			return nextInt(scanner);
		}
	}
}