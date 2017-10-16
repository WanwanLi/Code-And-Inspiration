#include <windows.h>
#include <stdio.h>
int main()
{	
	HINSTANCE hinstance=LoadLibrary("DLL.dll");
	typedef struct DLL DLL;
	typedef DLL* (*NEWDLL)(char* name,int id);
	NEWDLL newDLL=(NEWDLL)GetProcAddress(hinstance,"newDLL");
	DLL* dll=newDLL("CAndDLL",1);
	typedef void (*PRINTDLL)(DLL* dll);
	PRINTDLL printDLL=(PRINTDLL)GetProcAddress(hinstance,"printDLL");
	printDLL(dll);
	typedef void (*RUN)();
	RUN run=(RUN)GetProcAddress(hinstance,"run");
	run();
	typedef int (*ADD)(int a,int b);
	ADD add=(ADD)GetProcAddress(hinstance,"add");
	typedef int (*SUB)(int a,int b);
	SUB sub=(SUB)GetProcAddress(hinstance,"sub");
	printf("ADD:%d\n",add(5,10));
	printf("SUB:%d\n",sub(10,5));
	FreeLibrary(hinstance);
	return 0;
}
