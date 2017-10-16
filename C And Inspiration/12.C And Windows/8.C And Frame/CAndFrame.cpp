#include"frame.h"
int Frame()
{
	newFrame();
	addLabel("label1",20,20,1100,160);
	addButton("button1",50,50,100,50);
	addButton("button2",250,50,100,50);
	addCheckbox("checkbox1",450,50,100,50);
	addTextArea("textArea1",650,50,100,100);
	addList("list1",850,50,100,100);
	addItem("list1","item1");
	addItem("list1","item2");
	addItem("list1","item3");
	addLabel("label2",1120,20,160,160);
	addTimer("timer1",1000);
	addTimer("timer2",100);
	addMenu("menu1");
	addMenuItem("menu1","menuItem1");
	addMenuItem("menu1","menuItem2");
	addMenuItem("menu1","menuItem3");
	addMenu("menu2");
	addMenuItem("menu2","menuItem4");
	addMenuItem("menu2","menuItem5");
	addMenuItem("menu2","menuItem6");
	showFrame("frame1",10,30,500,400);
	return 0;
}
void onClose(){}
int x=0,y=0;
int dx=0,dy=0;
boolean b=false;
int functionLength=1000;
double* function;
int centerX=0;
double zoomX=1.0;
double f(double x)
{
	return abs(x*sin(x/10));
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
		int x0=x+centerX+(int)((i*dx-centerX)*zoomX);
		int x1=x+centerX+(int)(((i+1)*dx-centerX)*zoomX);
		double y0=(int)(y+h-function[i]);
		double y1=(int)(y+h-function[i+1]);
		if(x0>x&&x1<x+w)drawLine(x0,y0,x1,y1);
	}
}
String expression="";
void keyTyped()
{
	char c=getKeyChar();
	expression=append(expression,c);
	if(c=='\n')
	{
		setText("button2",expression);
		expression="";
	}
	setText("label2",expression);
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
	zoomX=1-0.005*dy;
	centerX=-dx;
	repaint();
}
void mouseReleased()
{
	b=false;
}
void paint()
{
	paintFunction(50,250,1200,400);
}
int counter1=0;
int counter2=0;
void actionPerformed()
{
	String s=getSource();
	if(equals(s,"button1"))
	{
		showMessageBox("info","Button1=%d", 100);
		getFunction();
		repaint();
	}
	else if(equals(s,"button2"))
	{
		boolean state=getState("checkbox1");
		showMessageBox("info","Button2=%d and \n State of checkbox1 is %s", 200,state==true?"true":"false");
		setState("checkbox1",state==true?false:true);
	}
	else if(equals(s,"menuItem1"))
	{
		showMessageBox("menu1","menuItem1 and start timer1");
		start("timer1");
	}
	else if(equals(s,"menuItem2"))
	{
		showMessageBox("menu1","menuItem2 and stop timer1");
		stop("timer1");
	}
	else if(equals(s,"menuItem3"))
	{
		showMessageBox("menu1","menuItem3");
	}
	else if(equals(s,"menuItem4"))
	{
		showMessageBox("menu2","menuItem4 and start timer2");
		start("timer2");
	}
	else if(equals(s,"menuItem5"))
	{
		showMessageBox("menu2","menuItem5 and stop timer2");
		stop("timer2");
	}
	else if(equals(s,"menuItem6"))
	{
		showMessageBox("menu2","menuItem6");
	}
	else if(equals(s,"timer1"))
	{
		setText("label2","%d : %d",counter1++,counter2);
		if(counter1>=10)counter1=0;
	}
	else if(equals(s,"timer2"))
	{
		setText("label2","%d : %d",counter1,counter2++);
		if(counter2>=10)counter2=0;
	}
}
void textValueChanged()
{
	String s=getSource();
	if(equals(s,"textArea1"))
	{
		setText("button1","%d",parseInt(getText("textArea1")));
	}
}
void itemStateChanged()
{
	String s=getSource();
	if(equals(s,"list1"))
	{
		setText("textArea1",getSelectedItem("list1"));
	}
}