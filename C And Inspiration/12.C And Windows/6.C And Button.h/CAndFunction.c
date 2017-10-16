#include <stdio.h>
#include<time.h>
#include "windows.h"
int WINAPI WinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,PSTR szCmdLine,int iCmdShow)
{
	winMain(hInstance,hPrevInstance,szCmdLine,iCmdShow);
	newWindow("window1",10,30,500,400);
	addButton("button1",50,50,100,50);
	addButton("button2",250,50,100,50);
	showWindow();
	return 0;
}
void onClose(){}
int x=0,y=0;
int dx=0,dy=0;
boolean b=false;
int functionLength=1000;
double* function;
double* newDouble(int length)
{
	double* d=(double*)(malloc(length*sizeof(double)));
	return d;
}
double random()
{
	int r=rand();
	double d=(r+90.0)/(32767+90);
	return d;
}
double f(double x)
{
	return random()*x;
}
void getFunction()
{
	function=newDouble(functionLength);
	for(int i=0;i<functionLength;i++)
	{
		function[i]=f(400.0*i/functionLength);
	}
}
void onCreate()
{
	getFunction();	
}
void paintFunction(int x,int y,int w,int h)
{
	setColor(0,0,0);
	drawRect(x,y,w,h);
	setColor(255,0,0);
	double dx=(w+0.0)/functionLength;
	for(int i=0;i<functionLength-1;i++)
	{
		int x0=(int)(x+i*dx);
		int x1=(int)(x+(i+1)*dx);
		double y0=(int)(y+h-function[i]);
		double y1=(int)(y+h-function[i+1]);
		drawLine(x0,y0,x1,y1);
	}
}
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
	setColor(255,0,0);
	drawLine(50+dx,70+dy,300+dx,200+dy);
	setColor(0,0,255);
	drawLine(80+dx,100+dy,400+dx,300+dy);
	paintFunction(50,250,1200,400);
}
void actionPerformed()
{
	String s=getSource();
	char string[100];
	if(equals(s,"button1"))
	{
		sprintf(string,"Button1=%d", 100);
		MessageBox(null,string,"info",0);
		getFunction();
		repaint();
	}
	else if(equals(s,"button2"))
	{
		sprintf(string,"Button2=%d", 200);
		MessageBox(null,string,"info",0);
	}
}