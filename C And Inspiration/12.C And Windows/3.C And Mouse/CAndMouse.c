#include "windows.h"
int WINAPI WinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,PSTR szCmdLine,int iCmdShow)
{
	winMain(hInstance,hPrevInstance,szCmdLine,iCmdShow);
	newWindow("window1",10,30,500,400);
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