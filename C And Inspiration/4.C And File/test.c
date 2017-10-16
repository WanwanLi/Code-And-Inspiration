#include <stdio.h>
#define String char*
void readFile(String fileName)
{
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return;
	int d0,d1;
	char c0,c1,c2;
	fscanf(file,"%d",&d0);
	fscanf(file,"%c",&c0);
	fscanf(file,"%c",&c1);
	fscanf(file,"%c",&c2);
	fscanf(file,"%d",&d1);
	fclose(file);
	printf("%d\n",d0);
	printf("%c\n",c0);
	printf("%c\n",c1);
	printf("%c\n",c2);
	printf("%d\n",d1);
}
void writeFile(String fileName)
{
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return;
	fprintf(file,"%d",10);
	fprintf(file,"%s,\0","ok");
	fprintf(file,"%d",30);
	fclose(file);
}
int main()
{
	writeFile("test.txt");
	readFile("test.txt");
	return 0;
}
