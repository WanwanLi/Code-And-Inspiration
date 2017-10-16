#include <windows.h>
#include "graphics3d.h"
#ifndef String
#define String char*
#endif
#ifndef boolean
#define boolean BOOL
#endif
#ifndef true
#define true TRUE
#endif
#ifndef false
#define false FALSE
#endif
#ifndef null
#define null NULL
#endif
LRESULT CALLBACK WndProc(HWND,UINT,WPARAM,LPARAM);
HINSTANCE this_hInstance;
HINSTANCE this_hPrevInstance;
PSTR this_szCmdLine;
int this_iCmdShow;
WNDCLASS this_window;
HWND this_hwnd;
static TCHAR this_szAppName[]=TEXT("windows.h");
void winMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,PSTR szCmdLine,int iCmdShow)
{
	this_hInstance=hInstance;
	this_hPrevInstance=hPrevInstance;
	this_szCmdLine=szCmdLine;
	this_iCmdShow=iCmdShow;
}
void newWindow(String name,int x0,int y0,int width,int height)
{
	this_window.style=CS_HREDRAW|CS_VREDRAW ;
	this_window.lpfnWndProc=WndProc ;
	this_window.cbClsExtra=0;
	this_window.cbWndExtra=0;
	this_window.hInstance=this_hInstance ;
	this_window.hIcon=LoadIcon(NULL,IDI_APPLICATION);
	this_window.hCursor=LoadCursor(NULL,IDC_ARROW);
	this_window.hbrBackground=(HBRUSH)GetStockObject(WHITE_BRUSH) ;
	this_window.lpszMenuName=NULL ;
	this_window.lpszClassName=this_szAppName ;
	RegisterClass(&this_window);
	this_hwnd=CreateWindow(this_szAppName,	// window class name
	TEXT(name), 				// window caption
	WS_OVERLAPPEDWINDOW,        		// window style
                x0,              				// initial x position
                y0,              				// initial y position
                width,              				// initial x size
                height,             				// initial y size
                NULL,                       			// parent window handle
	NULL,                       			// window menu handle
	this_hInstance,                  			// program instance handle
	NULL);                     				// creation parameters
}
void showWindow()
{
	ShowWindow(this_hwnd,this_iCmdShow);
	UpdateWindow(this_hwnd);
	MSG msg;
	while(GetMessage(&msg,NULL,0,0))
	{
		TranslateMessage(&msg) ;
		DispatchMessage(&msg) ;
	}
}

void onCreate();
void paint();
void mousePressed();
void mouseMoved();
void mouseReleased();
void onClose();
HDC this_hdc;
LPARAM this_lParam;
COLORREF this_color;
HPEN this_hPen;
LRESULT CALLBACK WndProc(HWND hwnd,UINT message,WPARAM wParam,LPARAM lParam)
{

	PAINTSTRUCT paintStruct;
	RECT rect ;
	switch(message)
	{
		case WM_CREATE:onCreate();return 0;
		case WM_PAINT:
			this_hdc=BeginPaint(hwnd,&paintStruct);
			this_hPen=null;
			paint(); 
			if(this_hPen!=null)DeleteObject(this_hPen);
          			EndPaint(hwnd,&paintStruct);
			return 0;
		case WM_LBUTTONDOWN:
		case WM_RBUTTONDOWN:
			this_lParam=lParam;
			mousePressed();
			return 0;
		case WM_MOUSEMOVE:
			this_lParam=lParam;
			mouseMoved();
			InvalidateRect(hwnd,NULL,TRUE);
			return 0;
		case WM_LBUTTONUP:
		case WM_RBUTTONUP:
			this_lParam=lParam;
			mouseReleased();
			return 0;
		case WM_DESTROY:onClose();PostQuitMessage(0);return 0;
     	}
	return DefWindowProc(hwnd,message,wParam,lParam);
}
void drawLine(int x0,int y0,int x1,int y1)
{
	MoveToEx(this_hdc,x0,y0,NULL);
	LineTo(this_hdc,x1,y1);
}
int getX()
{
	return LOWORD(this_lParam);
}
int getY()
{
	return HIWORD(this_lParam);
}
void setColor(Color* color)
{
	COLORREF colorRef=RGB(color->red,color->green,color->blue);
	this_hPen=CreatePen(PS_SOLID,1,colorRef);
	SelectObject(this_hdc,this_hPen);
}