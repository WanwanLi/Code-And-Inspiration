#include "awt.h"
int main(int argc,char** argv)
{
	showWindow();
	return 0;
}
int x,y;
double a;
int r=255,g=0,b=0;
Font* font1,*font2;
void init()
{
	addButton("button1",200,100,100,50);
	Menu Menu1=newMenu();
	addMenuItem(Menu1,"MenuItem1","Menu1.MenuItem1");
	addMenuItem(Menu1,"MenuItem2","Menu1.MenuItem2");
	addImage("JavaAndOpenGL",237,291);
	font1=newFont("Helvetica",BOLD,20);
	font2=newFont("Times New Roman",PLAIN,20);
	addTextField("textField1",400,100,100,50);
}
void actionPerformed()
{
	String s=getSource();
	if(equals(s,"button1")){a=0;printf("button1 clicked...\n");setText("textField1","ok");}
	else if(equals(s,"MenuItem1"))printf("Menu1.MenuItem1 is clicked...\n");
	else if(equals(s,"MenuItem2"))printf("Menu1.MenuItem2 is clicked...\n");
}

void paint()
{
	setColor(r,g,b);
	fillRect(x,y+screenHeight/2,100,60);
	drawImage("JavaAndOpenGL",x+150,y+screenHeight/2);
	drawImage("JavaAndOpenGL",150,200);
	setColor(r,g,b);
	setFont(font1);
	drawString("C And OpenGL\nC And drawString",350,200);
	setColor(0,255,0);
	setFont(font2);
	drawString("C And OpenGL\nUsing GLUT & GL",550,200);
}
void timer()
{
	a+=0.01;
	y=(int)(100*a);
	x=(int)(100*a*a);
	repaint();
}
void keyPressed()
{
	char c=getKeyChar();
	switch(c)
	{
		case 's':a=0;return;
		case 'd':a+=0.1;return;
		case 'a':a-=0.1;return;
	}
	int k=getKeyCode();
	switch(k)
	{
		case F1:a=0;break;
		case UP:a-=0.1;break;
		case DOWN:a+=0.1;break;
	}
}
void mousePressed()
{
	printf("mousePressed: (%d,%d)\n",getX(),getY());
}
void mouseReleased()
{
	printf("mouseReleased: (%d,%d)\n",getX(),getY());
}
void mouseMoved()
{
	printf("mouseMoved: (%d,%d)\n",getX(),getY());
}
void mouseDragged()
{
	printf("mouseDragged: (%d,%d)\n",getX(),getY());
}
