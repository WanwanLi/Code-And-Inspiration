public class JavaAndFourierTransform
{
	public static void main(String[] args)
	{
		Complex c=new Complex("-10-5i");
		Complex d=new Complex("2+10i");
		System.out.println("c="+c.toString());
		System.out.println("d="+d.toString());
		Complex e0=Complex.e2PI(5,8);
		Complex e1=Complex.e2PI(5,3);
		System.out.println("e58="+e0.toString());
		System.out.println("e53="+e1.toString());
		double[] a=new double[]{0,1,1,0};
		FourierTransform FourierTransform1=new FourierTransform(a);
		Complex[] F=FourierTransform1.getFastFourierTransformedArray();
		for(int i=0;i<a.length;i++)System.out.println("F["+i+"]="+F[i].toString()+",[F]="+F[i].length());
		int l=2<<10;
		a=new double[l];
		for(int i=0;i<l;i++)a[i]=100*Math.random();
		FourierTransform1=new FourierTransform(a);
		Complex[] A=FourierTransform1.getFastFourierTransformedArray();
		Complex[] B=FourierTransform1.getFourierTransformedArray();
		for(int i=0;i<l;i++)if(!A[i].equals(B[i]))
		{
			System.out.println("A["+i+"]="+A[i].toString()+",[A]="+A[i].length());
			System.out.println("B["+i+"]="+B[i].toString()+",[B]="+B[i].length());
		}
	}
}

class FourierTransform
{
	private Complex[] A;
	private double[] a;
	public FourierTransform(double[] a)
	{
		this.a=a;
	}
	public Complex[] getFourierTransformedArray()
	{
		int n=a.length;
		this.A=new Complex[n];
		for(int i=0;i<n;i++)
		{
			Complex c=new Complex(a[0],0);
			for(int j=1;j<n;j++)
			{
				Complex e=Complex.e2PI(n,i*j);
				Complex t=Complex.MUL(e,a[j]);
				c.add(t);
			}
			this.A[i]=c;
		}
		return A;
	}
	private Complex[] getFastFourierTransformedArray(double[] a,int n)
	{
		if(n==2)
		{
			Complex[] A=new Complex[2];
			A[0]=new Complex(a[0]+a[1],0);
			A[1]=new Complex(a[0]-a[1],0);
			return A;
		}
		double[] b=new double[n/2];
		double[] c=new double[n/2];
		for(int i=0;i<n/2;i++)
		{
			b[i]=a[i*2];
			c[i]=a[i*2+1];
		}
		Complex[] A=new Complex[n];
		Complex[] B=new Complex[n/2];
		Complex[] C=new Complex[n/2];
		B=this.getFastFourierTransformedArray(b,n/2);
		C=this.getFastFourierTransformedArray(c,n/2);
		for(int i=0;i<n/2;i++)
		{
			A[i]=Complex.ADD(B[i],Complex.MUL(C[i],Complex.e2PI(n,i)));
			A[i+n/2]=Complex.ADD(B[i],Complex.MUL(C[i],Complex.e2PI(n,i+n/2)));
		}
		return A;
	}
	public Complex[] getFastFourierTransformedArray()
	{
		this.A=this.getFastFourierTransformedArray(a,a.length);
		return A;
	}
}
class Complex
{
	private double real;
	private double imaginary;
	public Complex(String string)
	{
		int l=string.length();
		int n=0;
		String r="",i="";
		while(n<l)
		{
			char c=string.charAt(n++);
			if(c=='+')break;
			if(c=='-'&&n!=1){n--;break;}
			else r+=c;
		}
		if(n==l&&string.charAt(l-1)=='i'){this.imaginary=Double.parseDouble(r.substring(0,l-1));return;}
		this.real=Double.parseDouble(r);
		while(n<l)
		{
			char c=string.charAt(n++);
			if(c=='i')break;
			else i+=c;
		}
		if(!i.equals(""))this.imaginary=Double.parseDouble(i);
	}
	public Complex(double real,double imaginary)
	{
		this.real=real;
		this.imaginary=imaginary;
	}
	public void add(Complex c)
	{
		this.real+=c.real;
		this.imaginary+=c.imaginary;
	}
	public void sub(Complex c)
	{
		this.real-=c.real;
		this.imaginary-=c.imaginary;
	}
	public void mul(double d)
	{
		this.real*=d;
		this.imaginary*=d;
	}
	public static Complex ADD(Complex c0,Complex c1)
	{
		return new Complex(c0.real+c1.real,c0.imaginary+c1.imaginary);
	}
	public static Complex SUB(Complex c0,Complex c1)
	{
		return new Complex(c0.real-c1.real,c0.imaginary-c1.imaginary);
	}
	public static Complex MUL(Complex c0,Complex c1)
	{
		/*
			(a+bi)*(c+di)
			=ac+bci+adi-bd
			=ac-bd + (ad+bc)i
		*/
		double real=c0.real*c1.real-c0.imaginary*c1.imaginary;
		double imaginary=c0.real*c1.imaginary+c0.imaginary*c1.real;
		return new Complex(real,imaginary);
	}
	public static Complex MUL(Complex c,double d)
	{
		return new Complex(c.real*d,c.imaginary*d);
	}
	public static Complex e(double w)
	{
		return new Complex(Math.cos(w),Math.sin(w));
	}
	public static Complex e2PI(int n,int exp)
	{
		double w=2.0*Math.PI/n*exp;
		return new Complex(Math.cos(w),Math.sin(w));
	}
	private boolean isClose(double d0,double d1)
	{
		double d=Math.abs(d0-d1);
		double e=1E-8;
		return (d<e);
	}
	public boolean equals(Complex c)
	{
		return (isClose(this.real,c.real)&&isClose(this.imaginary,c.imaginary));
	}
	public double length()
	{
		return Math.sqrt(real*real+imaginary*imaginary);
	}
	public String toString()
	{
		String s="";
		String real=this.real+"";
		String imaginary=this.imaginary+"";
		if(isClose(this.real,0.0))real="0.0";
		if(isClose(this.imaginary,0.0))imaginary="0.0";
		if(this.imaginary>=0)s=real+"+"+imaginary+"i";
		else s=real+""+imaginary+"i";
		return s;
	}
}