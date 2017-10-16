#include "String.h"
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
	for(i=0;i<l;i++)
	{
		op=operator(expression[i]);
		if(op>0)
		{
			operand=toInteger(number);
			if(noResult)
			{
				result=operand;
				noResult=0;
			}
			else
			{
				switch(operation) 
				{
					case 1:result+=operand;break;
					case 2:result-=operand;break;
					case 3:result*=operand;break;
					case 4:result/=operand;break;
				}
			}
			number="";
			operation=op;
		}
		else number=append(number,expression[i]);

	}
	return result;
}
int main()
{
	String expression="3+7-15=";
	String result=add(expression,toString(calculate(expression)));
	printf("The result of calculator is : %s\n",result);
	return 0;
}

