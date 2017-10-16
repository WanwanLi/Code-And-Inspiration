//#include "String.h"
#define String char*
String newChar(int length)
{
	String string=(String)(malloc((length+1)*sizeof(char)));
	string[length]='\0';
	return string;
}
int parseInteger(String s)
{
	return atoi(s);
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
String append(String s,char c)
{
	int i=0;
	int l=length(s);
	String a=newChar(l+1);
	for(i=0;i<l;i++)a[i]=s[i];
	a[l]=c;
	return a;
}
int ADD(int a,int b)
{
	return a+b;
}
int SUB(int a,int b)
{
	return a-b;
}
int MUL(int a,int b)
{
	return a*b;
}
int DIV(int a,int b)
{
	return a/b;
}
int (*OP[5])(int a,int b);
void initOperations()
{
	OP[0]=0;
	OP[1]=ADD;
	OP[2]=SUB;
	OP[3]=MUL;
	OP[4]=DIV;
}
int operator(char c)
{
	if(c=='+')return 1;
	if(c=='-')return 2;
	if(c=='*')return 3;
	if(c=='/')return 4;
	if(c=='=')return 5;
	return 0;
}
int calculate(String expression)
{
	int i,op;
	String number="";
	int l=length(expression);
	int result=0;
	int noResult=1;
	int operand=0;
	int operation=0;
	initOperations();
	for(i=0;i<l;i++)
	{
		op=operator(expression[i]);
		if(op>0)
		{
			operand=parseInteger(number);
			if(noResult)
			{
				result=operand;
				noResult=0;
			}
			else result=OP[operation](result,operand);
			number="";
			operation=op;
		}
		else number=append(number,expression[i]);
	}
	return result;
}
int main()
{
	String expression="3+7-15/5*2=";
	printf("The result of calculator is : %s%d\n",expression,calculate(expression));
	return 0;
}

