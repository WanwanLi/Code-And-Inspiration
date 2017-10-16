import java.io.*;
import java.util.*;
public class JavaAndCenterDifference
{
	public static void main(String[] args)
	{
		double x=0.5;
		CenterDifference_Oh2 centerDifference_Oh2=new CenterDifference_Oh2();
		System.out.println("centerDifference_Oh2.df("+x+")="+centerDifference_Oh2.df(x));
		System.out.println("centerDifference_Oh2.d2f("+x+")="+centerDifference_Oh2.d2f(x));
		CenterDifference_Oh4 centerDifference_Oh4=new CenterDifference_Oh4();
		System.out.println("centerDifference_Oh4.df("+x+")="+centerDifference_Oh4.df(x));
		System.out.println("centerDifference_Oh4.d2f("+x+")="+centerDifference_Oh4.d2f(x));
	}
}
class CenterDifference_Oh2
{
	double h=0.1;
	private double f(double x)
	{
		return Math.exp(x);
	}
	public double df(double x)
	{
		return (f(x+h)-f(x-h))/(2*h);
	}
	public double d2f(double x)
	{
		return (f(x+h)-2*f(x)+f(x-h))/(h*h);
	}
}
class CenterDifference_Oh4
{
	double h=0.1;
	private double f(double x)
	{
		return Math.exp(x);
	}
	public double df(double x)
	{
		return (-f(x+2*h)+8*f(x+h)-8*f(x-h)+f(x-2*h))/(12*h);
	}
	public double d2f(double x)
	{
		return (-f(x+2*h)+16*f(x+h)-30*f(x)+16*f(x-h)-f(x-2*h))/(12*h*h);
	}
}