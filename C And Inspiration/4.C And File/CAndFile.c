#include "String.h"
#include "File.h"
int main()
{
	String fileName="CAndFile.txt";
	String string="C And File";
	int b=writeFile(fileName,string);
	if(b==0)printf("File: \"%s\" dose not exist\n",fileName);
	else printf("File: \"%s\" has been written\n",fileName);
	string=readFile(fileName);
	if(string==NULL)printf("File: \"%s\" dose not exist\n",fileName);
	else printf("The content of File: \"%s\" is: \n%s",fileName,string);
	int length=4,i;
	String* strings=(String*)malloc(length*sizeof(String));
	strings[0]="C";
	strings[1]="And";
	strings[2]="File";
	strings[3]=".txt";
	b=writeStringsIntoFile(fileName,strings,length);
	if(b==0)printf("File: \"%s\" dose not exist\n",fileName);
	else printf("File: \"%s\" has been written\n",fileName);
	strings=readStringsFromFile(fileName,length);
	if(strings==NULL)printf("File: \"%s\" is empty:\n",fileName);
	else for(i=0;i<length;i++)printf("strings[%d]=%s\n",i,strings[i]);
	int* integers=newInt(length);
	for(i=0;i<length;i++)integers[i]=i*5;
	b=writeIntegersIntoFile(fileName,integers,length);
	if(b==0)printf("File: \"%s\" dose not exist\n",fileName);
	else printf("File: \"%s\" has been written\n",fileName);
	integers=readIntegersFromFile(fileName,length);
	if(string==NULL)printf("File: \"%s\" dose not exist\n",fileName);
	else for(i=0;i<length;i++)printf("integers[%d]=%d\n",i,integers[i]);
	return 0;
}
