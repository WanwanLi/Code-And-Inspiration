#include <windows.h>
#include <stdio.h>
int main()
{	
	HINSTANCE hinstance=LoadLibrary("CAndDLLExport.dll");
	typedef int (*ADD)(int a,int b);
	ADD add=(ADD)GetProcAddress(hinstance,"add");
	typedef int (*SUB)(int a,int b);
	SUB sub=(SUB)GetProcAddress(hinstance,"sub");
	printf("ADD:%d\n",add(5,10));
	printf("SUB:%d\n",sub(10,5));
	return 0;
}
