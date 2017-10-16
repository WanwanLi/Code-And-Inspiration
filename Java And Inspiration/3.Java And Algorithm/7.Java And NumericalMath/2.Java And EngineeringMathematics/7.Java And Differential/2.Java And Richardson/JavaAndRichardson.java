import java.io.*;
import java.util.*;
public class JavaAndRichardson
{
	public static void main(String[] args)
	{
		double x=0.5;
		Richardson Richardson1=new Richardson();
		System.out.println("Richardson1.df("+x+")="+Richardson1.df(x));
		System.out.println("Richardson1.d2f("+x+")="+Richardson1.d2f(x));
	}
}
class Richardson
{
	double h=0.1;
	private double f(double x)
	{
		return Math.exp(x);
	}
	public double df(double x)
	{
		double R0=(-f(x+2*h)+8*f(x+h)-8*f(x-h)+f(x-2*h))/(12*h);
		h*=0.5;
		double R1=(-f(x+2*h)+8*f(x+h)-8*f(x-h)+f(x-2*h))/(12*h);
		R1=R1+(R1-R0)/15;
		return R1;
	}
	public double d2f(double x)
	{
		double R0=(-f(x+2*h)+16*f(x+h)-30*f(x)+16*f(x-h)-f(x-2*h))/(12*h*h);
		h*=0.5;
		double R1=(-f(x+2*h)+16*f(x+h)-30*f(x)+16*f(x-h)-f(x-2*h))/(12*h*h);
		R1=R1+(R1-R0)/15;
		return R1;
	}
}