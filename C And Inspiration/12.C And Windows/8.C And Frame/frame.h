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
String formatString(const String format,...)
{
	char buffer[1000];
	va_list arglist;
	va_start(arglist,format);
	int n=vsprintf(buffer,format,arglist);
	va_end(arglist);
	String string=newChar(n);
	int i;
	for(i=0;i<n;i++)string[i]=buffer[i];
	return string;
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
boolean IsOnControl();
void GetActionSource(int ID);
void DeleteHPen();
void CreateControls();
void showMessageBox(String,const String,...);
int this_controlID;
HDC this_hdc;
LPARAM this_lParam;
COLORREF this_color;
HPEN this_hPen;
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
			paint();
			DeleteHPen();
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
void setColor(int red,int green,int blue)
{
	COLORREF colorRef=RGB(red,green,blue);
	DeleteHPen();
	this_hPen=CreatePen(PS_SOLID,1,colorRef);
	SelectObject(this_hdc,this_hPen);
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
