#include <stdio.h>
int writeFile(String fileName,String string)
{
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return 0;
	fprintf(file,"%s",string);
	fclose(file);
	return 1;
}
String readFile(String fileName)
{
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return NULL;
	String result=newString(0);
	String string=newString(0);
	while(!feof(file))
	{
		fscanf(file,"%s",string);
		result=add(result,add(string,"\n"));
	}
	fclose(file);
	return result;
}

int writeStringsIntoFile(String fileName,String* strings,int length)
{
	int i;
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return 0;
	for(i=0;i<length;i++)fprintf(file,"%s ",strings[i]);
	fclose(file);
	return 1;
}
String* readStringsFromFile(String fileName,int length)
{
	int i;
	String* strings=(String*)malloc(length*sizeof(String));
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return NULL;
	for(i=0;i<length;i++)
	{
		strings[i]=newString(0);
		fscanf(file,"%s",strings[i]);
	}
	fclose(file);
	return strings;
}


int writeIntegersIntoFile(String fileName,int* integers,int length)
{
	int i;
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return 0;
	for(i=0;i<length;i++)fprintf(file,"%d ",integers[i]);
	fclose(file);
	return 1;
}
int* readIntegersFromFile(String fileName,int length)
{
	int i;
	int* integers=newInt(length);
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return NULL;
	String string=newString(0);
	for(i=0;i<length;i++)
	{
		fscanf(file,"%s",string);
		integers[i]=toInteger(string);
	}
	fclose(file);
	return integers;
}
