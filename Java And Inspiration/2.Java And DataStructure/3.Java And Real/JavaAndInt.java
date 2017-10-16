public class JavaAndInt
{
	public static void main(String[] args)
	{
		Int a=new Int("180");
		Int b=new Int("-180");
		Int c=a.add(b);
		System.out.println("a="+a.toString());
		System.out.println("b="+b.toString());
		System.out.println("c="+c.toString());
		Int d=new Int("180");
		Int e=new Int("180");
		Int f=d.sub(e);
		System.out.println("d="+d.toString());
		System.out.println("e="+e.toString());
		System.out.println("d-e="+f.toString());
		Int g=new Int("-180");
		Int h=new Int("-180");
		Int i=g.mul(h);
		System.out.println("g="+g.toString());
		System.out.println("h="+h.toString());
		System.out.println("g*h="+i.toString());
		System.out.println("180*180="+(180*180));
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
class Int
{
	Digit first, last;
	int value, length;
	boolean isNegative;
	public Int()
	{
		this.reset();
	}
	public Int(String value)
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
			this.append(toInt(value.charAt(i))); i++;
		}
	}
	public Int add(Int value)
	{
		if(this.isNegative())return value.sub(this.absoluteValue());
		if(value.isNegative())return this.sub(value.absoluteValue());
		int nextDigit=0; Int result=new Int(); Digit digit1, digit2;
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
		return result;
	}
	public Int sub(Int value)
	{
		if(value.isNegative())return this.add(value.absoluteValue());
		if(this.isNegative())return this.absoluteValue().add(value).negative();
		if(value.isGreaterThan(this))return value.sub(this).negative();
		int nextDigit=0; Int result=new Int(); Digit digit1, digit2;
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
		result.trim();
		return result;
	}
	public Int mul(Int value)
	{
		Int result=new Int("0");
		for(Digit digit=value.first; digit!=null; digit=digit.next)
		{
			result=result.add(this.mul(digit.value));
			if(digit!=value.last)result.append(0);
		}
		if(this.isNegative()^value.isNegative())result.negative();
		return result;
	}
	public Int add(String value)
	{
		return this.add(new Int(value));
	}
	public Int sub(String value)
	{
		return this.sub(new Int(value));
	}
	public Int mul(String value)
	{
		return this.mul(new Int(value));
	}
	public String toString()
	{
		String string=isNegative?"-":"";
		for(Digit digit=first; digit!=null; digit=digit.next)
		{
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
	public Int negative()
	{
		this.isNegative=!this.isNegative;
		return this;
	}
	public Int absoluteValue()
	{
		if(!isNegative)return this;
		String string=this.toString();
		return new Int(string.substring(1, length+1));
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
	boolean isGreaterThan(Int value)
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
		Digit digit=first;
		while(digit!=last&&digit.value==0)digit=digit.next;
		this.first=digit;
		if(this.first.value==0)this.isNegative=false;
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
	Int mul(int value)
	{
		int nextDigit=0; Int result=new Int();
		for(Digit digit=last; digit!=null; digit=digit.prev)
		{
			int currentDigit=mul(digit.value, value, nextDigit);
			result.push(currentDigit%10); nextDigit=currentDigit/10;
		}
		if(nextDigit!=0)result.push(nextDigit);
		return result;
	}
	void reset()
	{
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

