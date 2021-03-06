#define String char*
String newString(int length)
{
	String string=(String)(malloc((length+1)*sizeof(char)));
	string[length]='\0';
	return string;
}
int length(String string)
{
	int l=0;
	char c=string[l];
	while(c!='\0')
	{
		l++;
		c=string[l];
	}
	return l;
}
String add(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	String add=newString(length0+length1);
	for(i=0;i<length0;i++)add[i]=string0[i];
	for(i=0;i<length1;i++)add[length0+i]=string1[i];
	return add;
}
String toString(int integer)
{
	String zero="0";
	if(integer==0)return zero;
	int isNegative=0;
	if(integer<0)isNegative=1;
	int number=integer;
	if(isNegative)number=-integer;
	int length=0;
	int i=0;
	while(number>0)
	{
		number/=10;
		length++;
	}
	number=integer;
	if(isNegative)number=-integer;
	String string=newString(length);
	for(i=0;i<length;i++)
	{
		string[length-1-i]=(char)(number%10+'0');
		number/=10;
	}
	if(isNegative)return add("-",string);
	return string;
}
String boolean(int integer)
{
	if(integer>0)return "true";
	else return "false";
}
int equals(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	if(length0!=length1)return 0;
	for(i=0;i<length0;i++)if(string0[i]!=string1[i])return 0;
	return 1;
}
String substring(String string,int i0,int i1)
{
	String subString=newString(i1-i0);
	int i=i0;for(;i<i1;i++)subString[i-i0]=string[i];
	return subString;
}
int max(a,b)
{
	return (a>=b)?a:b;
}
String ADD(String integer1,String integer2)
{
	int length1=length(integer1);
	int length2=length(integer2);
	int length3=max(length1,length2);
	String integer3=newString(length3);
	int c=0;
	int i=length1-1,j=length2-1,k=length3-1;
	for(;i>=0&&j>=0;i--,j--,k--)
	{
		int d1=integer1[i]-'0';
		int d2=integer2[j]-'0';
		int s=d1+d2+c;
		c=s/10;
		integer3[k]=s%10+'0';
	}
	for(;i>=0;i--,k--)
	{
		int d1=integer1[i]-'0';
		int s=d1+c;
		c=s/10;
		integer3[k]=s%10+'0';
	}
	for(;j>=0;j--,k--)
	{
		int d2=integer2[j]-'0';
		int s=d2+c;
		c=s/10;
		integer3[k]=s%10+'0';
	}
	if(c!=0)
	{
		String integer4=newString(length3+1);
		integer4[0]=c+'0';
		for(i=0;i<length3;i++)integer4[i+1]=integer3[i];
		return integer4;
	}
	return integer3;
}
int* newInt(int length)
{
	int* integers=(int*)(malloc(length*sizeof(int)));
	return integers;
}
int exponent(int base,int index)
{
	int i=0;
	int result=1;
	for(i=0;i<index;i++)result*=base;
	return result;
}
int toInteger(String string)
{
	int i,k;
	char c;
	int l=length(string);
	int integer=0;
	for(i=0;i<l;i++)
	{
		c=string[i];
		k=(int)(c-'0');
		integer+=k*exponent(10,l-1-i);
	}
	return integer;
}
String append(String s,char c)
{
	int i=0;
	int l=length(s);
	String a=newString(l+1);
	for(i=0;i<l;i++)a[i]=s[i];
	a[l]=c;
	return a;
}
