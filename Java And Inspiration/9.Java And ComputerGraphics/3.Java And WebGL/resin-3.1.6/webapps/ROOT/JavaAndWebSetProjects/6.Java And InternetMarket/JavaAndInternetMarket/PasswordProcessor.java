package JavaAndInternetMarket;
public class PasswordProcessor
{
	public String getCodeByPassword(String password)
	{
		String code="";
		for(int i=0;i<password.length();i++)code+=this.getBinary(password.charAt(i));	
		return this.getReversedCode(code);
	}
	public String getPasswordByCode(String code)
	{
		String password="";
		String reversedCode=this.getReversedCode(code);
		int i=0;
		while(i<reversedCode.length())
		{
			String binary="";
			for(int j=0;j<4;j++)binary+=reversedCode.charAt(i++);
			password+=this.getDecimal(binary);
		}
		return password;
	}
	private String getBinary(char decimal)
	{
		String s="0000";
		switch(decimal)
		{
			case'0':s="0000";break;
			case'1':s="0001";break;
			case'2':s="0010";break;
			case'3':s="0011";break;
			case'4':s="0100";break;
			case'5':s="0101";break;
			case'6':s="0110";break;
			case'7':s="0111";break;
			case'8':s="1000";break;
			case'9':s="1001";break;
			default: s="0000";
		}
		return s;
	}
	private String getDecimal(String binary)
	{
		String s;
		if(binary.equals("0000"))s="0";
		else if(binary.equals("0000"))s="0";
		else if(binary.equals("0001"))s="1";
		else if(binary.equals("0010"))s="2";
		else if(binary.equals("0011"))s="3";
		else if(binary.equals("0100"))s="4";
		else if(binary.equals("0101"))s="5";
		else if(binary.equals("0110"))s="6";
		else if(binary.equals("0111"))s="7";
		else if(binary.equals("1000"))s="8";
		else if(binary.equals("1001"))s="9";
		else s="0";
		return s;
	}
	private String getReversedCode(String code)
	{
		String reversedCode="";
		for(int i=0;i<code.length();i++)reversedCode+=(code.charAt(i)=='0'?"1":"0");
		return reversedCode;
	}
}