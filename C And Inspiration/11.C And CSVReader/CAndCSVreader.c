#include <stdio.h>
#include <stdlib.h>
#include <string.h>
char* newString(int length)
{
	char* string=(char*)(malloc((length+1)*sizeof(char)));
	string[length]='\0';
	return string;
}
float* newFloat32Array(int length)
{
	return (float*)malloc(length*sizeof(float));
}
int main()
{
	long line=0L;
	int column=-1,i;
	char string[1024];
	char* fileName="CSVFile.csv";
	FILE* file=fopen(fileName, "r");
	if(file==NULL)
	{
		printf("File does not exists... \n");
		return 0;
	}
	fgets(string,sizeof(string),file);
	char* token = strtok(string, ",");
	while(token)
	{
		printf("%s, ", token);
		token=strtok(NULL, ",");
		column++;
	}
	printf("Column No.: %d\n",column);
	float* f=newFloat32Array(column);
	fgets(string,sizeof(string),file);
	while(!feof(file))
	{

		char* token = strtok(string, ",");
		printf("line %d: %s ", line, token);
		for(i=0;i<column;i++)f[i]=atof(strtok(NULL, ","));
		for(i=0;i<column;i++)printf("%f, ", f[i]);printf("\n");
		fgets(string,sizeof(string),file);line++;
	}
	fclose(file);
	return 1;
}
