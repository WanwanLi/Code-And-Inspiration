#include <stdio.h>
#include <stdlib.h>
#define DLL struct DLL
DLL
{
	char* name;
	int id;
};
extern "C" _declspec(dllexport) DLL* newDLL(char* name,int id)
{
	DLL* dll=(DLL*)malloc(sizeof(DLL));
	dll->name=name;
	dll->id=id;
	return dll;
}
extern "C" _declspec(dllexport) int add(int a,int b)
{
	return a+b;
}
 extern "C" _declspec(dllexport) int sub(int a,int b)
{	
	return a-b;
}
extern "C" _declspec(dllexport) void run()
{
	printf("C AND DLL!\n");
}
extern "C" _declspec(dllexport) void printDLL(DLL* dll)
{
	printf("DLL.Name=%s\tDLL.ID=%d\n",dll->name,dll->id);
}
int main()
{	
	return 0;
}
