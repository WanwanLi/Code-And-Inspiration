#include"String.h"
int main()
{
	int integer=-371200;
	String string=toString(integer);
	printf("The %d toString is %s \n",integer,string);
	int stringLength=length(string);
	printf("The length of %s is %d \n",string,stringLength);
	String sum=add(string,string);
	printf("The sum of %s and %s is %s \n",string,string,sum);
	String Sum=add(sum,sum);
	printf("The Sum of %s and %s is %s \n",sum,sum,Sum);
	String string1="-371200";
	printf("The  %s equals %s is %s \n",string,string1,boolean(equals(string,string1)));
	String substring1=substring(string1,1,4);
	printf("The substring (1,4) of  %s is %s \n",string1,substring1);
	String int1="0";
	String int2="250";
	String int3=ADD(int1,int2);
	printf("%s+%s=%s\n",int1,int2,int3);
	string="-0";
	integer=parseInt(string);
	printf("integer=%d\n",integer);
	return 0;
}

