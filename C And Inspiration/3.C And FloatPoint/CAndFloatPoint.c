#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define String char*

String newString(int length)
{
	String string=(String)(malloc((length+1)*sizeof(char)));
	string[length]='\0';
	return string;
}
String createString(int length, char value)
{
	String string=newString(length);
	for(int i=0; i<length; i++)string[i]=value;
	return string;
}
String initString(String string)
{
	String str=newString(1000);
	strcpy(str, string);
	return str;
}
String addString(String a, String b)
{
	String str=initString(a);
	strcat(str, b);
	return str;
}
String subString(String string, int start, int end)
{
	int length=end-start;
	if(length<=0)return initString("");
	String substr=newString(length);
	strncpy(substr, string+start, length);
	return substr;
}
String integerToBinary(int value, int bits)
{
	String binary=initString("");
	for(int integer=value; integer>0; integer/=2)
	{
		strcat(binary, integer%2?"1":"0");
	}
	for(int i=strlen(binary); i<bits; i++)strcat(binary, "0");
	strrev(binary);
	return binary;
}
int binaryToInteger(String value)
{
	int integer=0x00, bit=0x01;
	for(int i=strlen(value)-1; i>=0; i--)
	{
		if(value[i]=='1')integer|=bit;
		bit<<=1;
	}
	return integer;
}
float binaryToFraction(String value)
{
	float bit=0.5, fraction=0.0;
	for(int i=0; i<strlen(value); i++)
	{
		if(value[i]=='1')fraction+=bit;
		bit/=2;
	}
	return fraction;
}
String fractionToBinary(float value)
{
	String binary=initString("");
	for(float fraction=(value-(int)value)*2; fraction>0; fraction*=2)
	{
		strcat(binary, fraction>=1?"1":"0");
		if(fraction>=1)fraction-=1;
	}
	return binary;
}
void printb(float value)
{
	String binary=integerToBinary((int)value, 0);
	if(strlen(binary)==0)strcat(binary, "0"); 	
	strcat(binary, "."); 	
	strcat(binary, fractionToBinary(value));
	if(value==(int)value)strcat(binary, "0");
	int length=strlen(binary);
	for(int i=0; i<length; i++)printf("%c", binary[i]);
}
void writeString(String *dest, String src, int start, int end, char value)
{
	int srclen=strlen(src);
	int destlen=strlen(*dest);
	for(int i=0; i<srclen; i++)
	{
		int j=start+i;
		if(j<destlen&&j<end)(*dest)[j]=src[i];
	}
	for(int i=start+srclen; i<end; (*dest)[i++]=value);
}
int toInt(float value)
{
	String integer=integerToBinary((int)value, -1);
	int intBits=strlen(integer);
	String fraction=fractionToBinary(value);
	int fractionBits=strlen(fraction);
	String result=createString(16, '0');
	String binary=addString(integer, fraction);
	String exponent=integerToBinary(intBits, 6);
	writeString(&result, binary, 15-8, 16, '0');
	writeString(&result, exponent, 1, 15-8, '0');
	return binaryToInteger(result);
}
float toFloat(int value)
{
	String integer=integerToBinary(value, 16);
	String binary=subString(integer, 15-8, 16);
	String exponent=subString(integer, 1, 15-8);
	int floatPoint=binaryToInteger(exponent);
	integer=subString(binary, 0, floatPoint);
	String fraction=subString(binary, floatPoint, strlen(binary));
	if(strlen(fraction)==0)
	{
		int length=floatPoint-strlen(integer);
		for(int i=0; i<length; i++)strcat(integer, "0");
	}
	return binaryToInteger(integer)+binaryToFraction(fraction);
}
int addFloats(int x, int y)
{
	float a=toFloat(x);
	float b=toFloat(y);
	return toInt(a+b);
}
int mulFloats(int x, int y)
{
	float a=toFloat(x);
	float b=toFloat(y);
	return toInt(a*b);
}
void test(float value)
{
	printf("\n______test: %f ________\n", value);
	printf("\nbinary of %f is: \n", value); printb(value);
	printf("\ntoFloat=%f\n", toFloat(toInt(value))); printf("\n");
}
void testAddFloats(float x, float y)
{
	printf("\n______test: %.3f + %.3f=%.3f ________\n", x, y, x+y);
	printf("\nbinary of %f is: \n", x); printb(x);
	printf("\nbinary of %f is: \n", y); printb(y);
	printf("\ntoFloat=%f\n", toFloat(addFloats(toInt(x), toInt(y))));  printf("\n");
}
void testMulFloats(float x, float y)
{
	printf("\n______test: %.3f x %.3f=%.3f ________\n", x, y, x*y);
	printf("\nbinary of %f is: \n", x); printb(x);
	printf("\nbinary of %f is: \n", y); printb(y);
	printf("\ntoFloat=%f\n", toFloat(mulFloats(toInt(x), toInt(y))));  printf("\n");
}
void main()
{
	test(3.703125);
	test(15213);
	test(157);
	test(0.8125);
	testAddFloats(3.14, 2.8);
	testMulFloats(3.14, 2.8);
}
