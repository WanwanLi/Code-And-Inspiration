#include "windows.h"
int WINAPI WinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,PSTR szCmdLine,int iCmdShow)
{
	winMain(hInstance,hPrevInstance,szCmdLine,iCmdShow);
	newWindow("window1",10,30,500,400);
	showWindow();
	return 0;
}
void onCreate(){};
void paint(){};
void onClose(){};