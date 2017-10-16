#include <windows.h>
#include<time.h>
#include <math.h>
#include<stdio.h>
#include<stdarg.h>
#define BS_TEXTAREA 11235813
#define BS_LIST 11235814
#define BS_LABEL 11235815
#define BS_TIMER 11235816
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
#define PI 3.1415926
String newChar(int length)
{
	String string=(String)(malloc((length+1)*sizeof(char)));
	string[length]='\0';
	return string;
}
String* newString(int length)
{
	String* strings=(String*)malloc(length*sizeof(String));
	return strings;
}
int length(String string)
{
	int l=0;
	char c=string[l];
	while(c!='\0')
	{
		l++;
		c=string[l];
	}
	return l;
}
String addString(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	String add=newChar(length0+length1);
	for(i=0;i<length0;i++)add[i]=string0[i];
	for(i=0;i<length1;i++)add[length0+i]=string1[i];
	return add;
}
String* addStringItem(String* stringList,int* listLength,String stringItem)
{
	int length=(*listLength)++;
	String* newStringList=newString(length+1);
	int i;for(i=0;i<length;i++)newStringList[i]=stringList[i];
	newStringList[length]=stringItem;
	free(stringList);
	return newStringList;
}
int equals(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	if(length0!=length1)return 0;
	for(i=0;i<length0;i++)if(string0[i]!=string1[i])return 0;
	return 1;
}
String substring(String string,int i0,int i1)
{
	String subString=newChar(i1-i0);
	int i=i0;for(;i<i1;i++)subString[i-i0]=string[i];
	return subString;
}
String append(String s,char c)
{
	int i=0;
	int l=length(s);
	String a=newChar(l+1);
	for(i=0;i<l;i++)a[i]=s[i];
	a[l]=c;
	return a;
}
int* newInt(int length)
{
	int* integers=(int*)(malloc(length*sizeof(int)));
	return integers;
}
int parseInt(String string)
{
	return atoi(string);
}
double parseDouble(String string)
{
	return atof(string);
}
float parseFloat(String string)
{
	return atof(string);
}
double* newDouble(int length)
{
	double* d=(double*)(malloc(length*sizeof(double)));
	return d;
}
int* ints(int length,int int0,...)
{
	int i,*integers=newInt(length);
	va_list argList;
	va_start(argList,int0);
	integers[0]=int0;
	for(i=1;i<length;i++)integers[i]=va_arg(argList,int);
	va_end(argList);
	return integers;
}
double* doubles(int length,double double0,...)
{
	int i;double *doubles=newDouble(length);
	va_list argList;
	va_start(argList,double0);
	doubles[0]=double0;
	for(i=1;i<length;i++)doubles[i]=va_arg(argList,double);
	va_end(argList);
	return doubles;
}
String toString(String format,...)
{
	String buffer=newChar(1000);
	va_list arglist;
	va_start(arglist,format);
	int n=vsprintf(buffer,format,arglist);
	va_end(arglist);
	String string=newChar(n);
	int i;
	for(i=0;i<n;i++)string[i]=buffer[i];
	free(buffer);
	return string;
}
float* newFloat(int length)
{
	float* f=(float*)(malloc(length*sizeof(float)));
	return f;
}
double random()
{
	int r=rand();
	double d=(r+90.0)/(32767+90);
	return d;
}
#define Color struct Color
Color
{
	int red;
	int green;
	int blue;
};
Color newColor(int red,int green,int blue)
{
	Color* color=(Color*)(malloc(sizeof(Color)));
	color->red=red;
	color->green=green;
	color->blue=blue;
	return *color;
}
void setColor(int red,int green,int blue);
void setColor(Color color)
{
	setColor(color.red,color.green,color.blue);
}
#define Color_black newColor(0,0,0)
#define Color_white newColor(255,255,255)
#define Color_green newColor(0,255,0)
double (*signal)(double x);
double* X(double x0,double x1,int length)
{
	double* x=newDouble(length);
	double dx=(x1-x0)/(length-1);
	for(int i=0;i<length;i++)x[i]=x0+dx*i;
	return x;
}
double* C(double c,int length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=c;
	return y;
}
double* Y(double* x,int length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=signal(x[i]);
	return y;
}
double d(int n,double x,double dx)
{
	double df=0;
	if(n==0)df=signal(x);
	else df=(d(n-1,x+dx,dx)-d(n-1,x,dx))/dx;
	return df;
}
double* d(int n,double* x,int length)
{
	double dx=(x[length-1]-x[0])/(length-1);
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=d(n,x[i],dx);
	return y;
}
double* sin(double a,double b,double* x,int length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=sin(a*x[i]+b);
	return y;
}
double* cos(double a,double b,double* x,int length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=cos(a*x[i]+b);
	return y;
}
double* exp(double a,double* x,int length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=exp(a*x[i]);
	return y;
}
double* pow(double a,double* x,int length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=pow(a,x[i]);
	return y;
}
double* convolve(double* y,int *y_length,double* k,int k_length)
{
	int l=k_length;
	int length=*y_length+k_length-1;
	double* y1=newDouble(length);
	for(int i=0;i<length;i++)
	{
		y1[i]=0;
		for(int j=0;j<l&&i-j>=0;j++)
		{
			if(i-j<*y_length)y1[i]+=y[i-j]*k[j];
		}
	}
	*y_length=length;
	return y1;
}
double* output(double* a,int a_length,double* input,int length,double* b,int b_length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)
	{
		y[i]=0;
		for(int j=0;j<a_length&&i-j>=0;j++)y[i]-=a[j]*y[i-j];
		for(int j=0;j<b_length&&i-j>=0;j++)y[i]+=b[j]*input[i-j];
		y[i]/=a[0];
	}
	return y;
}
double square(double x)
{
	double d=x/(PI*2);
	double f=d-(int)d;
	return f<=0.5?1:-1;
}
double* square(double a,double b,double* x,int length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=square(a*x[i]+b);
	return y;
}
double unitSawtooth(double x,double k)
{
	if(k==1)return x;
	if(x<=-1||x>=1)return 0.0;
	if(x>k)return 1-(x-k)/(1-k);
	if(x<-k)return (-x-k)/(1-k)-1;
	return x/k;
}
double sawtooth(double x,double k)
{
	double d=x/(PI*2);
	double f=d-(int)d;
	return -unitSawtooth(2*(f-0.5),k);
}
double* sawtooth(double a,double b,double k,double* x,int length)
{
	double* y=newDouble(length);
	for(int i=0;i<length;i++)y[i]=sawtooth(a*x[i]+b,k);
	return y;
}
double heaviside(double x)
{
	return x>0?1:x<0?0:0.5;
}
void amplify(double* y,double A,int length)
{
	for(int i=0;i<length;i++)y[i]*=A;
}
double* modulate(double* b,double* y,int length)
{
	double* s=newDouble(length);
	for(int i=0;i<length;i++)s[i]=(1+b[i])*y[i];
	return s;
}
double* impulse(int i,double y,int length)
{
	double* x=newDouble(length);
	x[i]=y;
	return x;
}
double* step(int i,double y,int length)
{
	double* x=newDouble(length);
	for(int j=i;j<length;j++)x[j]=y;
	return x;
}
double D(int n,double* y,int i)
{
	if(n==0)return y[i];
	else return D(n-1,y,i)-D(n-1,y,i-1);
}
double* output(double* a,int a_length,double* input,int length,double t0,double t1,double* b,int b_length)
{
	double* x=input,*y=newDouble(length);
	double dT=(t1-t0)/(length-1);
	int dt_length=a_length>b_length?a_length:b_length;
	double* dt=newDouble(dt_length);
	dt[0]=pow(dT,a_length-1);
	for(int i=1;i<dt_length;i++)dt[i]=dt[i-1]/dT;
	double a0=a[0];
	for(int i=1;i<a_length;i++){a0*=dT;a0+=a[i];}
	for(int i=0;i<length;i++)
	{
		double yi=0;y[i]=0;
		for(int j=0;j<a_length&&i-j>=0;j++)yi-=a[j]*D(j,y,i)*dt[j];
		for(int j=0;j<b_length&&i-j>=0;j++)yi+=b[j]*D(j,x,i)*dt[j];
		y[i]=yi/a0;
	}
	return y;
}
#define Oscillogram struct Oscillogram
#define SIGNAL_CURVE 1
#define SIGNAL_POINTS 2
#define SIGNAL_STEMS 4
Oscillogram
{
	String name;
	int left,top,width,height,row,column,signalMode;
	double* x;
	double* y;
	int length;
	double baseY;
	double minX,maxX,minY,maxY,scaleX,scaleY;
	int X,Y,edge,dX,dY,lineWidth,pointSize,mode,charWidth;
	Color backgroundColor,gridColor,signalColor;
	boolean formatPI;
	Oscillogram* next;
};
Oscillogram* newOscillogram(String name,int left,int top,int width,int height,int row,int column,int signalMode)
{
	Oscillogram* oscillogram=(Oscillogram*)malloc(sizeof(Oscillogram));
	oscillogram->name=name;
	oscillogram->left=left;
	oscillogram->top=top;
	oscillogram->width=width;
	oscillogram->height=height;
	oscillogram->row=row;
	oscillogram->column=column;
	oscillogram->signalMode=signalMode;
	oscillogram->X=1;
	oscillogram->Y=2;
	oscillogram->length=0;
	oscillogram->baseY=0;
	oscillogram->edge=70;
	oscillogram->dX=30;
	oscillogram->dY=20;
	oscillogram->lineWidth=2;
	oscillogram->pointSize=10;
	oscillogram->charWidth=7;
	oscillogram->backgroundColor=Color_black;
	oscillogram->gridColor=Color_white;
	oscillogram->signalColor=Color_green;
	oscillogram->formatPI=false;
	oscillogram->next=null;
	return oscillogram;
}
Oscillogram* firstOscillogram=null;
Oscillogram* lastOscillogram=null;
int oscillogramLength=0;
void addOscillogram(String name,int left,int top,int width,int height,int row,int column,int signalMode)
{
	Oscillogram* oscillogram=newOscillogram(name,left,top,width,height,row,column,signalMode);
	if(firstOscillogram==null)
	{
		firstOscillogram=lastOscillogram=oscillogram;
	}
	else
	{
		lastOscillogram->next=oscillogram;
		lastOscillogram=oscillogram;
	}
	oscillogramLength++;
}
void setCoordinates(Oscillogram* oscillogram,double* x,double* y,int length)
{
	oscillogram->x=x;
	oscillogram->y=y;
	oscillogram->length=length;
	double maxX=x[0];
	double minX=x[0];
	double maxY=y[0];
	double minY=y[0];
	for(int i=0;i<length;i++)
	{
		if(x[i]>maxX)maxX=x[i];
		if(x[i]<minX)minX=x[i];
		if(y[i]>maxY)maxY=y[i];
		if(y[i]<minY)minY=y[i];
	}
	oscillogram->maxX=maxX;
	oscillogram->minX=minX;
	oscillogram->maxY=maxY;
	oscillogram->minY=minY;
	oscillogram->baseY=minY;
	int width=oscillogram->width;
	int height=oscillogram->height;
	double edge=0.0+oscillogram->edge;
	oscillogram->scaleX=(0.0+width-2*edge)/(maxX-minX);
	oscillogram->scaleY=(0.0+height-2*edge)/(maxY-minY);
}
int transformToCoordinateX(Oscillogram* oscillogram,double xi)
{
	return (int)(oscillogram->left+oscillogram->edge+(xi-oscillogram->minX)*oscillogram->scaleX);
}
int transformToCoordinateY(Oscillogram* oscillogram,double yi)
{
	return (int)(oscillogram->top+oscillogram->height-oscillogram->edge-(yi-oscillogram->minY)*oscillogram->scaleY);
}
void setFormat(Oscillogram* oscillogram,double pi)
{
	if(pi==PI)oscillogram->formatPI=true;
}
String format(double x,boolean formatPI)
{
	if(formatPI)x/=PI;
	String string=toString("%f",x);
	String formatString=newChar(0);
	int i=0,j=0,len=length(string);
	while(i<len&&string[i]!='.')formatString=append(formatString,string[i++]);
	for(;j<3&&i<len;j++,i++)formatString=append(formatString,string[i]);
	boolean hasE=false;
	for(;i<len;i++)
	{
		if(string[i]=='E'){formatString=append(formatString,'E');hasE=true;continue;};
		if(hasE)formatString=append(formatString,string[i]);
	}
	if(formatPI)formatString=addString(formatString,"PI");
	return formatString;
}
void setColors(Oscillogram* oscillogram,Color backgroundColor,Color gridColor,Color signalColor)
{
	oscillogram->backgroundColor=backgroundColor;
	oscillogram->gridColor=gridColor;
	oscillogram->signalColor=signalColor;
}
void setMinMaxLines(Oscillogram* oscillogram,double minY,double maxY)
{
	oscillogram->minY=minY;
	oscillogram->maxY=maxY;
	int height=oscillogram->height;
	int edge=oscillogram->edge;
	oscillogram->scaleY=(0.0+height-2*edge)/(maxY-minY);
}
void setRange(Oscillogram* oscillogram,double minX,double maxX)
{
	oscillogram->minX=minX;
	oscillogram->maxX=maxX;
	int width=oscillogram->width;
	int edge=oscillogram->edge;
	oscillogram->scaleX=(0.0+width-2*edge)/(maxX-minX);
}
void setBaseLine(Oscillogram* oscillogram,double y)
{
	if(oscillogram->minY<y&&y<oscillogram->maxY)oscillogram->baseY=y;
}
void setLineWidth(Oscillogram* oscillogram,int lineWidth)
{
	oscillogram->lineWidth=lineWidth;
}
void setPointSize(Oscillogram* oscillogram,int pointSize)
{
	oscillogram->pointSize=pointSize;
}
#define MenuItem struct MenuItem
MenuItem
{
	String name;
	int ID;
	int type;
	MenuItem* next;
};
MenuItem* newMenuItem(String name,int type)
{
	MenuItem* menuItem=(MenuItem*)malloc(sizeof(MenuItem));
	menuItem->name=name;
	menuItem->ID=0;
	menuItem->type=type;
	menuItem->next=null;
	return menuItem;
}
#define Menu struct Menu
Menu
{
	String name;
	int menuItemLength;
	MenuItem* firstMenuItem;
	MenuItem* lastMenuItem;
	Menu* next;
};
Menu* newMenu(String name)
{
	Menu* menu=(Menu*)malloc(sizeof(Menu));
	menu->name=name;
	menu->menuItemLength=0;
	menu->firstMenuItem=null;
	menu->lastMenuItem=null;
	menu->next=null;
	return menu;
}
Menu* firstMenu=null;
Menu* lastMenu=null;
int menuLength=0;
void addMenu(String name)
{
	Menu* menu=newMenu(name);
	if(firstMenu==null)
	{
		firstMenu=lastMenu=menu;
	}
	else
	{
		lastMenu->next=menu;
		lastMenu=menu;
	}
	menuLength++;
}
void addMenuItem(Menu* menu,String itemName,int itemType)
{
	MenuItem* menuItem=newMenuItem(itemName,itemType);
	if(menu->firstMenuItem==null)
	{
		menu->firstMenuItem=menu->lastMenuItem=menuItem;
	}
	else
	{
		menu->lastMenuItem->next=menuItem;
		menu->lastMenuItem=menuItem;
	}
	menu->menuItemLength++;
}
void addMenuItem(String menuName,String itemName)
{
	Menu* menu=firstMenu;
	for(int i=0;i<menuLength;i++,menu=menu->next)
	{
		if(equals(menu->name,menuName))
		{
			addMenuItem(menu,itemName,MF_STRING);
			return;
		}
	}
}
void addSeparator(String menuName)
{
	Menu* menu=firstMenu;
	for(int i=0;i<menuLength;i++,menu=menu->next)
	{
		if(equals(menu->name,menuName))
		{
			addMenuItem(menu,"_",MF_SEPARATOR);
			return;
		}
	}
}
#define Control struct Control
Control
{
	String name;
	int type;
	int ID;
	HWND hwnd;
	String* itemList;
	int itemLength;
	int x,y,w,h;
	Control* next;
};
Control* newControl(String name,int type,int x,int y,int w,int h)
{
	Control* control=(Control*)malloc(sizeof(Control));
	control->name=name;
	control->type=type;
	control->ID=0;
	control->hwnd=null;
	control->itemList=null;
	control->itemLength=0;
	control->x=x;
	control->y=y;
	control->w=w;
	control->h=h;
	control->next=null;
	return control;
};
Control* firstControl=null;
Control* lastControl=null;
int controlLength=0;
void addControl(String name,int type,int x,int y,int w,int h)
{
	Control* control=newControl(name,type,x,y,w,h);
	if(firstControl==null)
	{
		firstControl=lastControl=control;
	}
	else
	{
		lastControl->next=control;
		lastControl=control;
	}
	controlLength++;
}
void addButton(String name,int x,int y,int w,int h)
{
	addControl(name,BS_PUSHBUTTON,x,y,w,h);
}
void addCheckbox(String name,int x,int y,int w,int h)
{
	addControl(name,BS_AUTOCHECKBOX,x,y,w,h);
}
void addTextArea(String name,int x,int y,int w,int h)
{
	addControl(name,BS_TEXTAREA,x,y,w,h);
}
void addList(String name,int x,int y,int w,int h)
{
	addControl(name,BS_LIST,x,y,w,h);
}
void addLabel(String name,int x,int y,int w,int h)
{
	addControl(name,BS_LABEL,x,y,w,h);
}
void addTimer(String name,int interval)
{
	addControl(name,BS_TIMER,interval,0,0,0);
}
LRESULT CALLBACK WndProc(HWND,UINT,WPARAM,LPARAM);
HMENU CreateMenus();
HINSTANCE this_hInstance;
HINSTANCE this_hPrevInstance;
PSTR this_szCmdLine;
int this_iCmdShow;
WNDCLASS this_window;
HWND this_hwnd;
static TCHAR this_windowClassName[]=TEXT("FRAME");
#define Frame() WINAPI WinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,PSTR szCmdLine,int iCmdShow)
#define newFrame() newWindow(hInstance,hPrevInstance,szCmdLine,iCmdShow)
void newWindow(HINSTANCE hInstance,HINSTANCE hPrevInstance,PSTR szCmdLine,int iCmdShow)
{
	this_hInstance=hInstance;
	this_hPrevInstance=hPrevInstance;
	this_szCmdLine=szCmdLine;
	this_iCmdShow=iCmdShow;
	this_window.style=CS_HREDRAW|CS_VREDRAW ;
	this_window.lpfnWndProc=WndProc ;
	this_window.cbClsExtra=0;
	this_window.cbWndExtra=0;
	this_window.hInstance=this_hInstance ;
	this_window.hIcon=LoadIcon(NULL,IDI_APPLICATION);
	this_window.hCursor=LoadCursor(NULL,IDC_ARROW);
	this_window.hbrBackground=(HBRUSH)GetStockObject(COLOR_BTNFACE);
	this_window.lpszMenuName=this_windowClassName;
	this_window.lpszClassName=this_windowClassName;
	RegisterClass(&this_window);
}
void showFrame(String name,int x0,int y0,int width,int height)
{
	this_hwnd=CreateWindow(this_windowClassName,	// window class name
	TEXT(name), 					// window caption
	WS_OVERLAPPEDWINDOW,        			// window style
                x0,              					// initial x position
                y0,              					// initial y position
                width,              					// initial x size
                height,             					// initial y size
                NULL,                       				// parent window handle
	CreateMenus(),                       				// window menu handle
	this_hInstance,                  				// program instance handle
	NULL);                     					// creation parameters
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
void actionPerformed();
void textValueChanged();
void itemStateChanged();
void paint();
void keyTyped();
void mousePressed();
void mouseMoved();
void mouseReleased();
void onClose();
void PaintControls();
void DrawOscillograms();
boolean IsOnControl();
void GetActionSource(int ID);
void DeleteHPen();
void DeleteHBrush();
void CreateControls();
void showMessageBox(String,const String,...);
int this_controlID;
HDC this_hdc;
LPARAM this_lParam;
COLORREF this_color;
HPEN this_hPen;
HBRUSH this_hBrush;
boolean haveNotCreatedControls=true;
char keyChar;
String actionSource;
LRESULT CALLBACK WndProc(HWND hwnd,UINT message,WPARAM wParam,LPARAM lParam)
{
	this_hwnd=hwnd;
	this_lParam=lParam;
	switch(message)
	{
		case WM_CREATE:
		{
			CreateControls();
			CreateMenus();
			onCreate();
			return 0;
		}
		case WM_TIMER:
		{
			int ID=LOWORD(wParam);
			GetActionSource(ID);
			actionPerformed();
			return 0;
		}
		case WM_COMMAND:
		{
			int EVENT=HIWORD(wParam);
			int ID=LOWORD(wParam);
			switch(EVENT)
			{
				case BN_CLICKED:
				{
					GetActionSource(ID);
					actionPerformed();
					return 0;
				}
				case EN_CHANGE:
				{
					GetActionSource(ID);
					textValueChanged();
					return 0;
				}
				case LBN_SELCHANGE:
				{
					GetActionSource(ID);			
					itemStateChanged();
					return 0;
				}
			}
			return 0;
		}
		case WM_PAINT:
		{
			PAINTSTRUCT paintStruct;
			this_hdc=BeginPaint(hwnd,&paintStruct);
			this_hPen=null;
			this_hBrush=null;
			setColor(0,0,0);
			DrawOscillograms();
			paint();
			DeleteHPen();
			DeleteHBrush();
          			EndPaint(hwnd,&paintStruct);
			return 0;
		}
		case WM_CHAR:
		{
			keyChar=(char)wParam;
			keyTyped();
			return 0;
		}
		case WM_LBUTTONDOWN:
		case WM_RBUTTONDOWN:
		{
			mousePressed();
			return 0;
		}
		case WM_MOUSEMOVE:
		{
			mouseMoved();
			return 0;
		}
		case WM_LBUTTONUP:
		case WM_RBUTTONUP:
		{
			mouseReleased();
			return 0;
		}
		case WM_DESTROY:
		{
			onClose();
			PostQuitMessage(0);
			return 0;
		}
     	}
	return DefWindowProc(hwnd,message,wParam,lParam);
}
HMENU CreateMenus()
{
	this_controlID=0;
	HMENU mainMenu=CreateMenu();
	Menu* menu=firstMenu;
	for(int i=0;i<menuLength;i++,menu=menu->next)
	{
		HMENU popupMenu=CreateMenu();
		MenuItem* menuItem=menu->firstMenuItem;
		for(int j=0;j<menu->menuItemLength;j++,menuItem=menuItem->next)
		{
			menuItem->ID=this_controlID++;
			AppendMenu(popupMenu,menuItem->type,menuItem->ID,menuItem->name);
		}
		AppendMenu(mainMenu,MF_POPUP,(UINT_PTR)popupMenu,menu->name) ;
	}
	return mainMenu;
}
void CreateControls()
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		String name=control->name;
		control->ID=this_controlID;
		int x=control->x,y=control->y;
		int w=control->w,h=control->h;
		switch(control->type)
		{
			case BS_PUSHBUTTON:
			case BS_AUTOCHECKBOX:
			{
				control->hwnd=CreateWindow(TEXT("BUTTON"),
				TEXT(name),WS_CHILD|WS_VISIBLE|control->type,
				x,y,w,h,this_hwnd,(HMENU)this_controlID,
				(HINSTANCE)GetWindowLong(this_hwnd,GWL_HINSTANCE),null);
				break;
			}
			case BS_TEXTAREA:
			{
				control->hwnd=CreateWindow(TEXT("EDIT"),null,
				WS_CHILD|WS_VISIBLE|ES_LEFT|ES_MULTILINE|WS_BORDER,
				/*WS_HSCROLL|WS_VSCROLL|ES_AUTOHSCROLL|ES_AUTOVSCROLL,*/
				x,y,w,h,this_hwnd,(HMENU)this_controlID,
				(HINSTANCE)GetWindowLong(this_hwnd,GWL_HINSTANCE),null);
				break;
			}
			case BS_LIST:
			{
				control->hwnd=CreateWindow(TEXT("LISTBOX"),null,
				WS_CHILD|WS_VISIBLE|LBS_STANDARD,
				x,y,w,h,this_hwnd,(HMENU)this_controlID,
				(HINSTANCE)GetWindowLong(this_hwnd,GWL_HINSTANCE),null);
				for(int i=0;i<control->itemLength;i++)
				{
					SendMessage(control->hwnd,LB_ADDSTRING,0,(LPARAM)TEXT(control->itemList[i]));
				}
				break;
			}
			case BS_LABEL:
			{
				control->hwnd=CreateWindow(TEXT("STATIC"),TEXT(name),
				WS_CHILD|WS_VISIBLE|SS_LEFT,
				x,y,w,h,this_hwnd,(HMENU)this_controlID,
				(HINSTANCE)GetWindowLong(this_hwnd,GWL_HINSTANCE),null);
				break;
			}
		}
		this_controlID++;
	}
}
void GetActionSource(int ID)
{
	Menu* menu=firstMenu;
	for(int i=0;i<menuLength;i++,menu=menu->next)
	{
		MenuItem* menuItem=menu->firstMenuItem;
		for(int j=0;j<menu->menuItemLength;j++,menuItem=menuItem->next)
		{
			if(menuItem->ID==ID){actionSource=menuItem->name;return;}
		}
	}
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(control->ID==ID){actionSource=control->name;return;}
	}
	actionSource="null";
}
void drawLine(int x0,int y0,int x1,int y1)
{
	MoveToEx(this_hdc,x0,y0,NULL);
	LineTo(this_hdc,x1,y1);
}
void drawRect(int x,int y,int w,int h)
{
	int left=x,right=x+w;
	int top=y,bottom=y+h;
	Rectangle(this_hdc,left,top,right,bottom);
}
void drawString(String string,int x,int y)
{
	int w=100,h=20;
	int left=x,right=x+w;
	int top=y,bottom=y+h;
	RECT rect;
	rect.left=left;
	rect.top=top;
	rect.right=right;
	rect.bottom=bottom;
	DrawText(this_hdc,string,-1,&rect,DT_TOP|DT_LEFT|DT_SINGLELINE);
}
void fillRect(int x,int y,int w,int h)
{
	int left=x,right=x+w;
	int top=y,bottom=y+h;
	RECT rect;
	rect.left=left;
	rect.top=top;
	rect.right=right;
	rect.bottom=bottom;
	FillRect(this_hdc,&rect,this_hBrush);
}
void fillOval(int x,int y,int w,int h)
{
	int left=x,right=x+w;
	int top=y,bottom=y+h;
	SelectObject(this_hdc,this_hBrush);
	Ellipse(this_hdc,left,top,right,bottom);
	SelectObject(this_hdc,this_hPen);
}
int getX()
{
	return LOWORD(this_lParam);
}
int getY()
{
	return HIWORD(this_lParam);
}
void DeleteHPen()
{
	if(this_hPen!=null)DeleteObject(this_hPen);
}
void DeleteHBrush()
{
	if(this_hBrush!=null)DeleteObject(this_hBrush);
}
void setColor(int red,int green,int blue)
{
	COLORREF colorRef=RGB(red,green,blue);
	DeleteHPen();
	DeleteHBrush();
	this_hPen=CreatePen(PS_SOLID,1,colorRef);
	this_hBrush=CreateSolidBrush(colorRef);
	SelectObject(this_hdc,this_hPen);
	SetTextColor(this_hdc,colorRef);
	SetBkMode(this_hdc,TRANSPARENT);
}
void PaintControl(String name,int x,int y,int w,int h)
{
	setColor(0,0,0);
	int left=x,right=x+w;
	int top=y,bottom=y+h;
	RECT rect;
	rect.left=left;
	rect.top=top;
	rect.right=right;
	rect.bottom=bottom;
	Rectangle(this_hdc,left,top,right,bottom);
	DrawText(this_hdc,name,length(name),&rect,DT_CENTER|DT_VCENTER|DT_SINGLELINE);
}
void PaintControls()
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		String name=control->name;
		int x=control->x,y=control->y;
		int w=control->w,h=control->h;
		PaintControl(name,x,y,w,h);
	}
}
void repaint()
{
	InvalidateRect(this_hwnd,NULL,TRUE);
}
boolean IsInRange(int x,int y,int w,int h)
{
	int eX=LOWORD(this_lParam);
	int eY=HIWORD(this_lParam);
	return (x<=eX&&eX<=x+w)&&(y<=eY&&eY<=y+h);
}
boolean IsOnControl()
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		int x=control->x,y=control->y;
		int w=control->w,h=control->h;
		if(IsInRange(x,y,w,y))
		{
			actionSource=control->name;
			return true;
		}
	}	
	return false;
}
void setText(String controlName,const String format,...)
{
	char text[1000];
	va_list arglist;
	va_start(arglist,format);
	int n=vsprintf(text,format,arglist);
	va_end(arglist);
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(equals(controlName,control->name))
		{
			 SetWindowText(control->hwnd,text);
		}
	}
}
String getText(String textAreaName)
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(equals(textAreaName,control->name)&&control->type==BS_TEXTAREA)
		{
			char string[1000];
			int lineCount=SendMessage(control->hwnd,EM_GETLINECOUNT,0,0);
			String text="";
			for(int l=0;l<lineCount;l++)
			{
				int length=SendMessage(control->hwnd,EM_GETLINE,l,(LPARAM)string);
				string[length]='\n';
				string[length+1]='\0';
				text=addString(text,string);
			}
			return text;
		}
	}
	return "";
}
void setState(String checkboxName,boolean state)
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(equals(checkboxName,control->name)&&control->type==BS_AUTOCHECKBOX)
		{
			SendMessage(control->hwnd,BM_SETCHECK,state,0);
			return;
		}
	}
}
boolean getState(String checkboxName)
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(equals(checkboxName,control->name)&&control->type==BS_AUTOCHECKBOX)
		{
			int state=(int)SendMessage(control->hwnd,BM_GETCHECK,0,0);
			return state==0?false:true;
		}
	}
	return false;
}
void addItem(String listName,String item)
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(equals(listName,control->name)&&control->type==BS_LIST)
		{
			control->itemList=addStringItem(control->itemList,&control->itemLength,item);
			return;
		}
	}
}
String getSelectedItem(String listName)
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(equals(listName,control->name)&&control->type==BS_LIST)
		{
			int itemIndex=SendMessage(control->hwnd,LB_GETCURSEL,0,0);
			int length=SendMessage(control->hwnd,LB_GETTEXTLEN,itemIndex,0)+1;
			String item=newChar(length);
			SendMessage(control->hwnd,LB_GETTEXT,itemIndex,(LPARAM)item);
			return item;
		}
	}
	return "";
}
void start(String timerName)
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(equals(timerName,control->name))
		{
			SetTimer(this_hwnd,control->ID,control->x,null);
			return;
		}
	}
}
void stop(String timerName)
{
	Control* control=firstControl;
	for(int i=0;i<controlLength;i++,control=control->next)
	{
		if(equals(timerName,control->name))
		{
			KillTimer(this_hwnd,control->ID);
			return;
		}
	}
}
String getSource()
{
	return actionSource;
}
char getKeyChar()
{
	return keyChar;
}
void showMessageBox(String title,const String format,...)
{
	char string[1000];
	va_list arglist;
	va_start(arglist,format);
	int n=vsprintf(string,format,arglist);
	va_end(arglist);
	MessageBox(null,string,title,0);
}
void execute(String name)
{
	ShellExecute(NULL,"open",name,NULL,NULL,SW_SHOWNORMAL);
}
void drawGrid(Oscillogram* oscillogram)
{
	int left=oscillogram->left;
	int top=oscillogram->top;
	int width=oscillogram->width;
	int height=oscillogram->height;
	int row=oscillogram->row;
	int column=oscillogram->column;
	int edge=oscillogram->edge;
	boolean formatPI=oscillogram->formatPI;
	int charWidth=oscillogram->charWidth;
	double intervalY=(0.0+height-2*edge)/(row-1);
	double maxX=oscillogram->maxX;
	double minX=oscillogram->minX;
	double maxY=oscillogram->maxY;
	double minY=oscillogram->minY;
	double coordinateIntervalY=(maxY-minY)/(row-1);
	for(int i=0;i<row;i++)
	{
		int x0=left+edge;
		int x1=left+width-edge;
		int y=(int)(top+edge+i*intervalY);
		drawLine(x0,y,x1,y);
		double coordinateY=maxY-i*coordinateIntervalY;
		String text=format(coordinateY,formatPI);
		drawString(text,x0-charWidth*(length(text)+1),y-charWidth);
	}
	double intervalX=(0.0+width-2*edge)/(column-1);
	double coordinateIntervalX=(maxX-minX)/(column-1);
	for(int j=0;j<column;j++)
	{
		int x=(int)(left+edge+j*intervalX);
		int y1=top+edge;
		int y2=top+height-edge;
		drawLine(x,y1,x,y2);
		double coordinateX=minX+j*coordinateIntervalX;
		String text=format(coordinateX,formatPI);
		drawString(text,x-charWidth*length(text)/2,y2+3*charWidth/2);
	}
}
void drawBackground(Oscillogram* oscillogram)
{
	int left=oscillogram->left;
	int top=oscillogram->top;
	int width=oscillogram->width;
	int height=oscillogram->height;
	setColor(oscillogram->backgroundColor);
	fillRect(left,top,width,height);
	setColor(oscillogram->gridColor);
	drawGrid(oscillogram);
}
void drawCurve(Oscillogram* oscillogram)
{
	setColor(oscillogram->signalColor);
	for(int i=0;i<oscillogram->length-1;i++)
	{
		int x0=transformToCoordinateX(oscillogram,oscillogram->x[i]);
		int y0=transformToCoordinateY(oscillogram,oscillogram->y[i]);
		int x1=transformToCoordinateX(oscillogram,oscillogram->x[i+1]);
		int y1=transformToCoordinateY(oscillogram,oscillogram->y[i+1]);
		for(int j=0;j<oscillogram->lineWidth;j++)
		{
			int dy=j-oscillogram->lineWidth/2;
			drawLine(x0,y0+dy,x1,y1+dy);
		}
	}
}
void drawPoints(Oscillogram* oscillogram)
{
	setColor(oscillogram->signalColor);
	for(int i=0;i<oscillogram->length;i++)
	{
		int x=transformToCoordinateX(oscillogram,oscillogram->x[i]);
		int y=transformToCoordinateY(oscillogram,oscillogram->y[i]);
		int pointSize=oscillogram->pointSize;
		fillOval(x-pointSize/2,y-pointSize/2,pointSize,pointSize);
	}
}
void drawStems(Oscillogram* oscillogram)
{
	setColor(oscillogram->signalColor);
	for(int i=0;i<oscillogram->length;i++)
	{
		int xi=transformToCoordinateX(oscillogram,oscillogram->x[i]);
		int y0=transformToCoordinateY(oscillogram,oscillogram->baseY);
		int y1=transformToCoordinateY(oscillogram,oscillogram->y[i]);
		for(int j=0;j<oscillogram->lineWidth;j++)
		{
			int x=xi+j-oscillogram->lineWidth/2;
			drawLine(x,y0,x,y1);
		}
	}
	int x0=transformToCoordinateX(oscillogram,oscillogram->minX);
	int x1=transformToCoordinateX(oscillogram,oscillogram->maxX);
	int yb=transformToCoordinateY(oscillogram,oscillogram->baseY);
	for(int j=0;j<oscillogram->lineWidth;j++)
	{
		int y=yb+j-oscillogram->lineWidth/2;
		drawLine(x0,y,x1,y);
	}
}
void DrawOscillograms()
{
	Oscillogram* oscillogram=firstOscillogram;
	for(int i=0;i<oscillogramLength;i++,oscillogram=oscillogram->next)
	{
		drawBackground(oscillogram);
		drawGrid(oscillogram);
		String name=oscillogram->name;
		int left=oscillogram->left;
		int top=oscillogram->top;
		setColor(Color_white);
		drawString(name,left,top+10);
		int signalMode=oscillogram->signalMode;
		if(signalMode&SIGNAL_CURVE)drawCurve(oscillogram);
		if(signalMode&SIGNAL_POINTS)drawPoints(oscillogram);
		if(signalMode&SIGNAL_STEMS)drawStems(oscillogram);
	}
}
void setCoordinates(String oscillogramName,double* x,double* y,int length)
{
	Oscillogram* oscillogram=firstOscillogram;
	for(int i=0;i<oscillogramLength;i++,oscillogram=oscillogram->next)
	{
		if(equals(oscillogramName,oscillogram->name))
		{
			setCoordinates(oscillogram,x,y,length);
			return;
		}
	}
}
void setColors(String oscillogramName,Color backgroundColor,Color gridColor,Color signalColor)
{
	Oscillogram* oscillogram=firstOscillogram;
	for(int i=0;i<oscillogramLength;i++,oscillogram=oscillogram->next)
	{
		if(equals(oscillogramName,oscillogram->name))
		{
			setColors(oscillogram,backgroundColor,gridColor,signalColor);
			return;
		}
	}
}
void setMinMaxLines(String oscillogramName,double minY,double maxY)
{
	Oscillogram* oscillogram=firstOscillogram;
	for(int i=0;i<oscillogramLength;i++,oscillogram=oscillogram->next)
	{
		if(equals(oscillogramName,oscillogram->name))
		{
			setMinMaxLines(oscillogram,minY,maxY);
			return;
		}
	}
}
void setBaseLine(String oscillogramName,double y)
{
	Oscillogram* oscillogram=firstOscillogram;
	for(int i=0;i<oscillogramLength;i++,oscillogram=oscillogram->next)
	{
		if(equals(oscillogramName,oscillogram->name))
		{
			setBaseLine(oscillogram,y);
			return;
		}
	}
}
void setRange(String oscillogramName,double minX,double maxX)
{
	Oscillogram* oscillogram=firstOscillogram;
	for(int i=0;i<oscillogramLength;i++,oscillogram=oscillogram->next)
	{
		if(equals(oscillogramName,oscillogram->name))
		{
			setRange(oscillogram,minX,maxX);
			return;
		}
	}
}
void setLineWidth(String oscillogramName,int lineWidth)
{
	Oscillogram* oscillogram=firstOscillogram;
	for(int i=0;i<oscillogramLength;i++,oscillogram=oscillogram->next)
	{
		if(equals(oscillogramName,oscillogram->name))
		{
			setLineWidth(oscillogram,lineWidth);
			return;
		}
	}
}
void setPointSize(String oscillogramName,int pointSize)
{
	Oscillogram* oscillogram=firstOscillogram;
	for(int i=0;i<oscillogramLength;i++,oscillogram=oscillogram->next)
	{
		if(equals(oscillogramName,oscillogram->name))
		{
			setPointSize(oscillogram,pointSize);
			return;
		}
	}
}
