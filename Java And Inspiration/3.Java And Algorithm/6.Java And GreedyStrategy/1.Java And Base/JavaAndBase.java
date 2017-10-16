public class JavaAndBase
{
	public static void main(String[] args)
	{
		Base Base1=new Base();
		System.out.println("Binary(116)="+Base1.getBinaryFromDecimal("116"));
		System.out.println("Decimal(1110100)="+Base1.getDecimalFromBinary("1110100"));
		System.out.println("Binary(0.8125)="+Base1.getBinaryFromDecimal("0.8125"));
		System.out.println("Decimal(0.1101)="+Base1.getDecimalFromBinary("0.1101"));
		System.out.println("Binary(116.8125)="+Base1.getBinaryFromDecimal("116.8125"));
		System.out.println("Binary(21.316)="+Base1.getBinaryFromDecimal("21.316"));
		System.out.println("Decimal(1110100.1101)="+Base1.getDecimalFromBinary("1110100.1101"));
		System.out.println("Decimal(10101.010100001110010101100000010000011000100100110111010011)="+Base1.getDecimalFromBinary("10101.010100001110010101100000010000011000100100110111010011"));
	}
}
class Base
{
	public String getBinaryFromDecimal(String decimal)
	{
		int p=0;
		String s="";
		String binary="";
		while(p<decimal.length()&&decimal.charAt(p)!='.'){s+=decimal.charAt(p);p++;}
		int int1=Integer.parseInt(s);
		if(int1==0)binary="0";
		while(int1!=0)
		{
			binary=int1%2+binary;
			int1/=2;
		}
		if(p==decimal.length()){return binary;}
		binary+=".";
		s="0.";
		while(p<decimal.length()-1){p++;s+=decimal.charAt(p);}
		double double1=Double.parseDouble(s);
		while(double1!=0)
		{
			double1*=2;
			if(double1>=1.0)
			{
				binary+="1";
				double1-=1.0;
			}
			else binary+="0";
		}
		return binary;
	}
	public String getDecimalFromBinary(String binary)
	{
		String decimal="";
		int p=0;
		int int1=0;
		while(p<binary.length()&&binary.charAt(p)!='.')
		{
			int1+=binary.charAt(p)-'0';
			int1*=2;
			p++;
		}
		int1/=2;
		if(p==binary.length()){return (decimal+int1);}
		p=binary.length()-1;
		double double1=0;
		while(p>=0&&binary.charAt(p)!='.')
		{
			double1+=binary.charAt(p)-'0';
			double1/=2;
			p--;
		}
		decimal+=double1;
		decimal=decimal.substring(1,decimal.length());
		decimal=int1+decimal;
		return decimal;
	}
}