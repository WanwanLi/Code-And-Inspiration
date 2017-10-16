public class JavaAndEquation
{
	public static void main(String[] args)
	{
		JEquation JEquation1=new JEquation();
		JEquation1.getRoot();
	}
}
class Equation
{
	class Item
	{
		public Double Data;
		public Item Next;
		public Item(Double data)
		{
			Data=data;
			Next=null;	
		}
	}
	class Stack
	{
		Item Top;
		public void push(Double d)
		{
			Item i=new Item(d);
			if(Top==null)Top=i;
			else
			{
				i.Next=Top;
				Top=i;
			}
		}
		public double pop()
		{
			double d=Top.Data;
			Top=Top.Next;
			return d;
		}
		public boolean isNotEmpty()
		{
			return (Top!=null);
		}
	}
	String fx="";
	Stack S0;
	Stack S1;
	public Equation(String f)
	{
		fx=f;
		S0=new Stack();
		S1=new Stack();
		char c;
		String n="";
		double d=0.0;
		double b=1.0;

		for(int i=0;i<f.length();i++)
		{
			c=f.charAt(i);
			n="";
			while(c>='0'&&c<='9'&&i<f.length())
			{
				n+=c;
				c=f.charAt(++i);
			}
			switch(c)
			{
				case 'x':case 'X':d=Double.parseDouble(n);d*=b;S0.push(d);break;
				case '+':d=Double.parseDouble(n);S1.push(d);b=1.0;break;
				case '-':d=Double.parseDouble(n);S1.push(d);b=-1.0;break;
				case '=':d=Double.parseDouble(n);S1.push(d);break;
				default:break;
			}
		}
	}
	String F;
	public double f(double x)
	{
		double d=0.0;
		double d0=0.0;
		double d1=0.0;
		Stack s0=S0;
		Stack s1=S1;
		F="f("+x+")=";
		while(s0.isNotEmpty()&&s1.isNotEmpty())
		{
			d0=s0.pop();
			d1=s1.pop();
			d+=d0*Math.pow(x,d1);
			F+=d0+"*"+x+"^"+d1+"+";
		}
		F+="=";
		return d;
	}
	public String F()
	{
		return F;
	}
}

class JEquation
{	
	private double f(double x)
	{
		return x*x*x-5.0*x*x+16.0*x-80.0;
	}
	private double getx0From(double x1,double x2)
	{
		return (x1*f(x2)-x2*f(x1))/(f(x2)-f(x1));
	}
	private double getFinalx0(double x1,double x2)
	{
		double x0,fx0,fx1=f(x1);
		do
		{
			x0=this.getx0From(x1,x2);
			fx0=f(x0);
			if(fx0*fx1>0){x1=x0;fx1=fx0;}
			else x2=x0;
		}while(fx0>0.0001||fx0<-0.0001);
		System.out.println(x0);
		return x0;
	}
	public void getRoot()
	{
		double x1,x2;
		x1=x2=0;
		while(f(x1)>=0){x1--;if(x1<-112358)break;}
		if(x1<-112358)while(f(x1)>=0){x1++;if(x1>112358)break;}
		while(f(x2)<=0){x2++;if(x2>112358)break;}
		if(x2>112358)while(f(x2)<=0){x2--;if(x2<-112358)break;}
		System.out.println("x*x*x-5.0*x*x+16.0-80.0  x1:"+x1+"  x2:"+x2);
		System.out.println("The Final Root is:"+this.getFinalx0(x1,x2));
	}

}
