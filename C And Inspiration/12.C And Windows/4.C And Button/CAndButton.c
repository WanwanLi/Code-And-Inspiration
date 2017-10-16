#include <stdio.h>
#include "windows.h"
int WINAPI WinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,PSTR szCmdLine,int iCmdShow)
{
	winMain(hInstance,hPrevInstance,szCmdLine,iCmdShow);
	newWindow("window1",10,30,500,400);
	addButton("button1",50,50,100,50);
	addButton("button2",150,50,100,50);
	showWindow();
	return 0;
}
void onCreate(){}
void onClose(){}
int x=0,y=0;
int dx=0,dy=0;
boolean b=false;
void mousePressed()
{
	b=true;
	x=getX();
	y=getY();
}
void mouseMoved()
{
	if(!b)return;
	dx+=getX()-x;
	dy+=getY()-y;
	x=getX();
	y=getY();
}
void mouseReleased()
{
	b=false;
}
void paint()
{
	setColor(newColor(255,0,0));
	drawLine(50+dx,70+dy,300+dx,200+dy);
	setColor(newColor(0,0,255));
	drawLine(80+dx,100+dy,400+dx,300+dy);
}
void actionPerformed()
{
	String s=getSource();
	char string[100];
	if(equals(s,"button1"))
	{
		sprintf(string,"Button1=%d", 100);
		MessageBox(null,string,"info",0);
	}
	else if(equals(s,"button2"))
	{
		sprintf(string,"Button2=%d", 200);
		MessageBox(null,string,"info",0);
	}
}