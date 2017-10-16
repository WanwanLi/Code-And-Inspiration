#include"frame.h"
int Frame()
{
	newFrame();
	addLabel("label1",20,20,1300,80);
	addButton("button1",60,30,100,50);
	addButton("button2",250,30,100,50);
	addCheckbox("checkbox1",450,30,100,50);
	addTextArea("textArea1",650,30,100,50);
	addMenu("menu1");
	addMenuItem("menu1","menuItem1");
	addMenuItem("menu1","menuItem2");
	addMenuItem("menu1","menuItem3");
	addMenu("menu2");
	addMenuItem("menu2","menuItem4");
	addMenuItem("menu2","menuItem5");
	addMenuItem("menu2","menuItem6");
	addOscillogram("Oscillogram1",20,90,1300,600,10,20,SIGNAL_CURVE);//|SIGNAL_STEMS|SIGNAL_POINTS);
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
double f1(double x)
{
	return 2*sin(2*PI*2*x/32+PI/2);
}
double f2(double x)
{
	return 2*x*x*x;
}
double f3(double x)
{
	return x==0?1.0:exp(-2*x)*heaviside(x);
}
void testSignal()
{
	signal=f1;
	int length=32;
	double* x=X(0,50,length);
	double* y=Y(x,length);
/*
	y=sin(2*PI*2/32,PI/2,x,length);
	y=exp(0.1,x,length);
	y=pow(1.1,x,length);
	length=6;
	x=X(0,5,length);
	y=C(1,length);
	double* k=x;
	y=convolve(y,&length,k,length);
	x=X(0,5,length);
	x=X(0,length-1,length);
	double* a=doubles(2,1.0,-0.8);
	double* b=doubles(1,2.0);
	length=4;
	x=doubles(length,1.0,2.0,3.0,4.0);
	y=output(a,2,x,length,b,1);
	length=2000;
	x=X(0,200,length);
	b=sin(0.009*2*PI,0.01*2*PI,x,length);
	double* y1=cos(0.4*PI,0,x,length);
	double* y2=square(0.4*PI,0,x,length);
	double* y3=sawtooth(0.4*PI,0,0.5,x,length);
	y=modulate(b,y1,length);
	y=modulate(b,y2,length);
	y=modulate(b,y3,length);
*/
	setCoordinates("Oscillogram1",x,y,length);
	setBaseLine("Oscillogram1",0);
	setMinMaxLines("Oscillogram1",-5,5);
}

void testSystem()
{

	int length=1000;
	double* x=X(0,8,length);
	double* a=doubles(2,1.0,2.0);
	double* b=doubles(3,1.0,2.0,1.0);
	double* y=output(a,2,C(1.0,length),length,0,8,b,3);
	y=output(a,2,impulse(0,1,length),length,0,8,b,3);
	y=output(a,2,step(0,2,length),length,0,8,b,3);
	signal=f3;
	x=X(0,10,length);
	y=Y(x,length);
	y=output(a,2,y,length,0,8,b,3);
	setCoordinates("Oscillogram1",x,y,length);
	
}
void onCreate()
{
//	getFunction();
/*
	signal=f2;
	int length=500;
	double* x=X(0,10,length);
	double* y=d(1,x,length);
	setCoordinates("Oscillogram1",x,y,length);
*/
	testSystem();
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
//	repaint();
}
void mouseReleased()
{
	showMessageBox("curser position","x/left=%d  y/top=%d",getX(),getY());
	b=false;
}
void paint()
{
	//paintFunction(50,250,1200,400);
	setColor(255,255,255);
	drawString("X Value",1260,600);
	drawString("Y Value",90,135);
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
		showMessageBox("menu1","menuItem3 and execute Frame1");
		execute("Frame1");
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