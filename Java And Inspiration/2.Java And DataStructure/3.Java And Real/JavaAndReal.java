import java.util.Scanner;

public class JavaAndReal
{
	static Scanner scanner=new Scanner(System.in);
	public static void main(String[] args)
	{
		Real value=new Real("0");
		printMenu();
		for(String input=scanner.nextLine(); !input.equals("q"); input=nextLine("Make a chice:"))
		{
			if(input.equals("e"))value.set(nextLine("Enter a value:"));
			else if(input.equals("a"))value=value.add(nextLine("Add a value:"));
			else if(input.equals("s"))value=value.sub(nextLine("Sub a value:"));
			else if(input.equals("m"))value=value.mul(nextLine("Mul a value:"));
			else if(input.equals("r"))value=value.negative();
			else if(input.equals("c"))value.set("0");
			System.out.println("current value is:"+value.toString());
		}
	}
	static void printMenu()
	{
		System.out.println("(E)nter: e	|	(A)dd: a");
		System.out.println("(S)ubtract: s	|	(M)ultiply: m");
		System.out.println("(R)everse: r	|	(C)lear: c");
		System.out.print("(Q)uit: q     	|	Please make a chice:"); 
	}
	static String nextLine(String message)
	{
		System.out.print(message);
		return scanner.nextLine();
	}
}

class Digit
{
	public int value;
	public Digit next;
	public Digit prev;
	public Digit(int value)
	{
		this.value=value;
		this.next=null;
		this.prev=null;
	}
}
class Real
{
	Digit first, last;
	boolean isNegative;
	int value, length, point;
	public Real()
	{
		this.reset();
	}
	public Real(String value)
	{
		this.set(value);
	}
	public void set(String value)
	{
		int i=0;
		this.reset();
		if(isNegative(value))
		{
			this.isNegative=true; i++;
		}
		while(i<value.length())
		{
			if(value.charAt(i)=='.')this.point=i;
			else this.append(toInt(value.charAt(i))); i++;
		}
		if(isNegative)this.point--;
		if(point<0)this.point=length;
		this.trim();
	}
	public Real add(Real value)
	{
		if(this.isNegative())return value.sub(this.absoluteValue());
		if(value.isNegative())return this.sub(value.absoluteValue());
		int point1=this.getPoint(), point2=value.getPoint();
		if(point1>point2)return this.add(value.appendPoint(point1-point2));
		if(point2>point1)return this.appendPoint(point2-point1).add(value);
		int nextDigit=0; Real result=new Real(); Digit digit1, digit2;
		for(digit1=last, digit2=value.last; digit1!=null&&digit2!=null; digit1=digit1.prev, digit2=digit2.prev)
		{
			int currentDigit=add(digit1.value, digit2.value, nextDigit);
			result.push(currentDigit%10); nextDigit=currentDigit/10;
		}
		for(; digit1!=null; digit1=digit1.prev)
		{
			int currentDigit=add(digit1.value, 0, nextDigit);
			result.push(currentDigit); nextDigit=0;
		}
		for(; digit2!=null; digit2=digit2.prev)
		{
			int currentDigit=add(0, digit2.value, nextDigit);
			result.push(currentDigit); nextDigit=0;
		}
		if(nextDigit!=0)result.push(nextDigit);
		result.setPoint(value.getPoint());
		result.trim();
		return result;
	}
	public Real sub(Real value)
	{
		if(value.isNegative())return this.add(value.absoluteValue());
		if(this.isNegative())return this.absoluteValue().add(value).negative();
		int point1=this.getPoint(), point2=value.getPoint();
		if(point1>point2)return this.sub(value.appendPoint(point1-point2));
		if(point2>point1)return this.appendPoint(point2-point1).sub(value);
		if(value.isGreaterThan(this))return value.sub(this).negative();
		int nextDigit=0; Real result=new Real(); Digit digit1, digit2;
		for(digit1=last, digit2=value.last; digit1!=null&&digit2!=null; digit1=digit1.prev, digit2=digit2.prev)
		{
			int currentDigit=sub(digit1.value, digit2.value, nextDigit);
			result.push((currentDigit+10)%10); nextDigit=(currentDigit+10)/10-1;
		}
		for(; digit1!=null; digit1=digit1.prev)
		{
			int currentDigit=sub(digit1.value, 0, nextDigit);
			result.push((currentDigit+10)%10); nextDigit=(currentDigit+10)/10-1;
		}
		if(nextDigit<0)result.negative();
		result.setPoint(value.getPoint());
		result.trim();
		return result;
	}
	public Real mul(Real value)
	{
		Real result=new Real("0");
		for(Digit digit=value.first; digit!=null; digit=digit.next)
		{
			result=result.add(this.mul(digit.value));
			if(digit!=value.last)result.append(0);
			result.setPoint(0);
		}
		if(this.isNegative()^value.isNegative())result.negative();
		result.setPoint(this.getPoint()+value.getPoint());
		result.pushPoint();
		return result;
	}
	public Real add(String value)
	{
		return this.add(new Real(value));
	}
	public Real sub(String value)
	{
		return this.sub(new Real(value));
	}
	public Real mul(String value)
	{
		return this.mul(new Real(value));
	}
	public String toString()
	{
		String string=isNegative?"-":""; int i=0;
		for(Digit digit=first; digit!=null; digit=digit.next, i++)
		{
			if(i==point)string+=".";
			string+=digit.value;
		}
		return string;
	}
	public int length()
	{
		return this.length;
	}
	public boolean isNegative()
	{
		return isNegative;
	}
	boolean isNegative(String string)
	{
		return string.charAt(0)=='-';
	}
	public Real negative()
	{
		this.isNegative=!this.isNegative;
		return this;
	}
	public Real absoluteValue()
	{
		if(!isNegative)return this;
		String string=this.toString();
		return new Real(string.substring(1, string.length()));
	}
	public void pushPoint()
	{
		if(point>0)return;
		for(int i=0; i>=point; i--)this.push(0);
		this.point=1;
	}
	public Real appendPoint(int point)
	{
		Real result=new Real(this.toString());
		for(int i=0; i<point; i++)result.append(0);
		return result;
	}
	void push(int value)
	{
		this.length++;
		Digit digit=new Digit(value);
		if(first==null)this.first=this.last=digit;
		else
		{
			this.first.prev=digit;
			digit.next=first;
			this.first=digit;
		}
	}
	void append(int value)
	{
		this.length++;
		Digit digit=new Digit(value);
		if(first==null)this.first=this.last=digit;
		else
		{
			this.last.next=digit;
			digit.prev=last;
			this.last=digit;
		}
	}
	boolean isGreaterThan(Real value)
	{
		if(value.length()>this.length())return false;
		if(this.length()>value.length())return true;
		Digit digit1, digit2;
		for
		(
			digit1=first, digit2=value.first; 
			digit1!=null&&digit2!=null&&
			digit1.value==digit2.value; 
			digit1=digit1.next, 
			digit2=digit2.next
		);
		if(digit1==null||digit2==null)return false;
		return digit1.value>digit2.value;
	}
	void trim()
	{
		Digit digit; int i, left=0, mid=point, right=length;
		if(point>0)
		{
			for
			(
				digit=first, i=1; 
				i!=point&&digit.value==0;
				digit=digit.next, i++
			);
			this.first=digit;
			this.first.prev=null;
			left=i-1; mid=point-left;
		}
		if(point<length-1)
		{
			for
			(
				digit=last, i=length-1; 
				i!=point&&digit.value==0;
				digit=digit.prev, i--
			);
			this.last=digit;
			this.last.next=null;
			right=i+1;
		}
		this.point=mid;
		this.length=right-left;
		if(this.first.value+this.last.value==0)this.isNegative=false;
	}
	public int getPoint()
	{
		return this.length-this.point;
	}
	public void setPoint(int point)
	{
		this.point=this.length-point;
	}
	int add(int digit1, int digit2, int digit0)
	{
		return digit1+digit2+digit0;
	}
	int sub(int digit1, int digit2, int digit0)
	{
		return digit1-digit2+digit0;
	}
	int mul(int digit1, int digit2, int digit0)
	{
		return digit1*digit2+digit0;
	}
	Real mul(int value)
	{
		int nextDigit=0; Real result=new Real();
		for(Digit digit=last; digit!=null; digit=digit.prev)
		{
			int currentDigit=mul(digit.value, value, nextDigit);
			result.push(currentDigit%10); nextDigit=currentDigit/10;
		}
		if(nextDigit!=0)result.push(nextDigit);
		result.setPoint(0);
		return result;
	}
	void reset()
	{
		this.point=-1;
		this.first=null;
		this.last=null;
		this.length=0;
		this.isNegative=false;
	}
	int toInt(char c)
	{
		return (int)c-(int)'0';
	}
}

