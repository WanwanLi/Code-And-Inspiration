#include <OpenGL/glut.h>
#include "awt.h"
#include "c3d.h"
#define F1 GLUT_KEY_F1
#define F2 GLUT_KEY_F2
#define F3 GLUT_KEY_F3
#define F4 GLUT_KEY_F4
#define F5 GLUT_KEY_F5
#define F6 GLUT_KEY_F6
#define F7 GLUT_KEY_F7
#define F8 GLUT_KEY_F8
#define F9 GLUT_KEY_F9
#define F10 GLUT_KEY_F10
#define F11 GLUT_KEY_F11
#define F12 GLUT_KEY_F12
#define UP GLUT_KEY_UP
#define DOWN GLUT_KEY_DOWN
#define LEFT GLUT_KEY_LEFT
#define RIGHT GLUT_KEY_RIGHT
#define PAGE_UP GLUT_KEY_PAGE_UP
#define PAGE_DOWN GLUT_KEY_PAGE_DOWN
#define HOME GLUT_KEY_HOME
#define END GLUT_KEY_END
#define INSERT GLUT_KEY_INSERT
int timeDelay=1;
int mouseX,mouseY;
int screenWidth,screenHeight;
float glRed,glGreen,glBlue;
char keyChar;
int keyCode;
String actionSource;
void init();
void actionPerformed();
void paint();
void timer();
void keyPressed();
void mousePressed();
void mouseReleased();
void mouseMoved();
void mouseDragged();
void init();
void displayFunc();
void timerFunc(int i);
void keyboardFunc(unsigned char c,int x,int y);
void specialFunc(int k,int x,int y);
void mouseFunc(int button,int state,int x,int y);
void motionFunc(int x,int y);
void passiveMotionFunc(int x,int y);
void glInitTexture2D();

void runOpenGL(char* name,int argc,char** argv)
{
	glutInit(&argc,argv);
	glutInitDisplayMode(GLUT_SINGLE|GLUT_RGB);
	glutInitWindowSize(screenWidth,screenHeight);
	glutInitWindowPosition(0,0);
	glutCreateWindow(name);
	glClearColor(0.0,0.0,0.0,0.0);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
	glutDisplayFunc(displayFunc);
	glutTimerFunc(timeDelay,timerFunc,1);
	glutKeyboardFunc(keyboardFunc);
	glutSpecialFunc(specialFunc);
	glutMouseFunc(mouseFunc);
	glutMotionFunc(motionFunc);
	glutPassiveMotionFunc(passiveMotionFunc);
	init();
	glutMainLoop();
}

void ProcessMenu(int value)
{
	printf("%d\n",value);
}

void setWindowSize(int w,int h)
{
	screenWidth=w;
	screenHeight=h;
}
void setColor(int r,int g,int b)
{
	glRed=(r+0.0f)/255;
	glGreen=(g+0.0f)/255;
	glBlue=(b+0.0f)/255;
	glColor3f(glRed,glGreen,glBlue);
}
static GLuint glTextures[1];
void glInitTextures()
{
	glEnable(GL_TEXTURE_2D);
	glGenTextures(1,glTextures);
	glBindTexture(GL_TEXTURE_2D,glTextures[0]);
	glTexEnvi(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_DECAL);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
	glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
	glEnable(GL_BLEND);
}
void drawImage1(String name,int x,int y)
{
	Image* image=getImage(name);
	if(image==null)return;
	int w=image->width,h=image->height;
	glInitTextures();
	glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA8,w,h,1,GL_RGBA,GL_UNSIGNED_BYTE,image->pixels);
	glBegin(GL_QUADS);
	glVertex3f((x+0.0)/screenWidth,(screenHeight-y+0.0)/screenHeight,0);
	glVertex3f((x+0.0)/screenWidth,(screenHeight-(y+h)+0.0)/screenHeight,0);
	glVertex3f((x+w+0.0)/screenWidth,(screenHeight-(y+h)+0.0)/screenHeight,0);
	glVertex3f((x+w+0.0)/screenWidth,(screenHeight-y+0.0)/screenHeight,0);
	glTexCoord2f(0.0,0.0);
	glTexCoord2f(0.0,1.0);
	glTexCoord2f(1.0,1.0);
	glTexCoord2f(1.0,0.0);
	glEnd();
	glDeleteTextures(1,glTextures);
}
void drawImage(String name,int x,int y)
{
	Image* image=getImage(name);
	if(image==null)return;
	glRasterPos2f((x+0.0)/screenWidth,(screenHeight-(y+image->height)+0.0)/screenHeight);
	glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
	glEnable(GL_BLEND);
	glDrawPixels(image->width,image->height-2,GL_RGBA,GL_UNSIGNED_BYTE,image->pixels);
}
void drawString(String string,int x,int y)
{
	int y0=y;
	for(int i=0;i<currentFont->type;i++)
	{
		for(int j=0;j<currentFont->type;j++)
		{
			y=y0;
			int l=length(string);
			for(int k=0;k<l;k++)
			{
				glRasterPos2f((x+j+0.0)/screenWidth,(screenHeight-(y+i)+0.0)/screenHeight);
				for(;k<l;k++)
				{
					glutBitmapCharacter(currentFont->glutFont,*(string+k));
					if(*(string+k)=='\n'){y+=currentFont->size*4/3;break;}
				}
			}
		}
	}
}
void drawLine(int x0,int y0,int x1,int y1)
{
	glBegin(GL_LINES);
	glVertex3f((x0+0.0)/screenWidth,(screenHeight-y0+0.0)/screenHeight,0);
	glVertex3f((x1+0.0)/screenWidth,(screenHeight-y1+0.0)/screenHeight,0);
	glEnd();
}
void fillRect(int x,int y,int w,int h)
{
	glBegin(GL_QUADS);
	glVertex3f((x+0.0)/screenWidth,(screenHeight-y+0.0)/screenHeight,0);
	glVertex3f((x+0.0)/screenWidth,(screenHeight-(y+h)+0.0)/screenHeight,0);
	glVertex3f((x+w+0.0)/screenWidth,(screenHeight-(y+h)+0.0)/screenHeight,0);
	glVertex3f((x+w+0.0)/screenWidth,(screenHeight-y+0.0)/screenHeight,0);
	glEnd();
}
void drawRect(int x,int y,int w,int h)
{
	glBegin(GL_LINES);
	glVertex3f((x+0.0)/screenWidth,(screenHeight-y+0.0)/screenHeight,0);
	glVertex3f((x+w+0.0)/screenWidth,(screenHeight-y+0.0)/screenHeight,0);
	glVertex3f((x+0.0)/screenWidth,(screenHeight-(y+h)+0.0)/screenHeight,0);
	glVertex3f((x+w+0.0)/screenWidth,(screenHeight-(y+h)+0.0)/screenHeight,0);
	glVertex3f((x+0.0)/screenWidth,(screenHeight-y+0.0)/screenHeight,0);
	glVertex3f((x+0.0)/screenWidth,(screenHeight-(y+h)+0.0)/screenHeight,0);
	glVertex3f((x+w+0.0)/screenWidth,(screenHeight-y+0.0)/screenHeight,0);
	glVertex3f((x+w+0.0)/screenWidth,(screenHeight-(y+h)+0.0)/screenHeight,0);
	glEnd();
}

void drawButton(String text,int x,int y,int w,int h)
{
	setColor(240,240,240);
	fillRect(x,y,w,h/2);
	setColor(210,210,210);
	fillRect(x,y+h/2,w,h/2);
	setColor(0,0,0);
	drawRect(x,y,w,h);
	int l=length(text);
	glRasterPos2f((x+w/2.0-l*4.2)/screenWidth,(screenHeight-(y+h/2+4.2))/screenHeight);
	for(int k=0;k<l;k++)glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18,*(text+k));
	setColor(255,255,255);
	drawRect(x+1,y+1,w-2,h-2);
	
}
void drawButtons()
{
	Button* button=firstButton;
	for(int i=0;i<buttonLength;i++,button=button->next)
	{
		String text=button->text;
		int x=button->x,y=button->y;
		int w=button->w,h=button->h;
		drawButton(text,x,y,w,h);
	}
}
void drawTextField(String text,int x,int y,int w,int h,int focus)
{
	setColor(255,255,255);
	fillRect(x,y,w,h);
	setColor(60,120,170);
	drawLine(x,y,x+w,y);
	setColor(180,220,240);
	drawLine(x,y+h,x+w,y+h);
	setColor(180,210,230);
	drawLine(x,y,x,y+h);
	setColor(165,200,230);
	drawLine(x+w,y,x+w,y+h);
	setColor(0,0,0);
	int l=length(text);
	glRasterPos2f((x+5.0)/screenWidth,(screenHeight-(y+h/2+4.2))/screenHeight);
	for(int k=0;k<l;k++)glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18,*(text+k));
	if(focus)drawLine(x+3,y+3,x+3,y+h-3);
}
void drawTextFields()
{
	TextField* textField=firstTextField;
	for(int i=0;i<textFieldLength;i++,textField=textField->next)
	{
		String text=textField->text;
		int x=textField->x,y=textField->y;
		int w=textField->w,h=textField->h;
		int focus=textField->focus;
		drawTextField(text,x,y,w,h,focus);
	}
}
int isInRang(int x,int y,int w,int h)
{
	return (x<=mouseX&&mouseX<=x+w)&&(y<=mouseY&&mouseY<=y+h);
}
int isOnButton()
{
	Button* button=firstButton;
	for(int i=0;i<buttonLength;i++,button=button->next)
	{
		int x=button->x,y=button->y;
		int w=button->w,h=button->h;
		if(isInRang(x,y,w,y))
		{
			actionSource=button->text;
			return 1;
		}
	}	
	return 0;
}
int isOnTextField()
{
	int onTextField=0;
	TextField* textField=firstTextField;
	for(int i=0;i<textFieldLength;i++,textField=textField->next)
	{
		int x=textField->x,y=textField->y;
		int w=textField->w,h=textField->h;
		if(onTextField==0&&isInRang(x,y,w,y))
		{
			textField->focus=1;
			focusedTextField=textField;
			onTextField=1;
		}
		else textField->focus=0;
	}
	if(onTextField==0)focusedTextField=null;
	return onTextField;
}
void  createMenu(int id)
{
	MenuItem* menuItem=firstMenuItem;
	for(int i=0;i<menuItemLength;i++,menuItem=menuItem->next)
	{
		int itemID=menuItem->itemID;
		if(itemID==id)
		{
			actionSource=menuItem->name;
			actionPerformed();
			return;
		}
	}
}
int glutMenuItemID=1;
Menu newMenu()
{
	Menu menu=glutCreateMenu(createMenu);
	glutAttachMenu(GLUT_RIGHT_BUTTON);
	return menu;
}
void addMenuItem(Menu menu,String name,String text)
{
	glutSetMenu(menu);
	glutAddMenuEntry(text,glutMenuItemID);
	addNewMenuItem(name,glutMenuItemID++);
}
void repaint()
{
	glutPostRedisplay();
}
void displayFunc()
{
	glClear(GL_COLOR_BUFFER_BIT);
	paint();
	drawButtons();
	drawTextFields();
	glFlush();
}
void timerFunc(int i)
{
	timer();
	glutTimerFunc(timeDelay,timerFunc,1);
}
void keyboardFunc(unsigned char c,int x,int y)
{
	if(focusedTextField!=null)
	{
		focusedTextField->text=append(focusedTextField->text,c);
		repaint();
		return;
	}
	keyChar=c;
	keyPressed();
}
void specialFunc(int k,int x,int y)
{
	if(focusedTextField!=null)
	{
		if(k==LEFT)
		{
			focusedTextField->text=backspace(focusedTextField->text);
			repaint();
		}
		return;
	}
	keyCode=k;
	keyPressed();
}
void mouseFunc(int button,int state,int x,int y)
{
	mouseX=x;
	mouseY=y;
	if(state==GLUT_DOWN)mousePressed();
	if(state==GLUT_UP)
	{
		if(isOnButton())actionPerformed();
		else if(isOnTextField());
		else mouseReleased();
	}
}
void motionFunc(int x,int y)
{
	mouseX=x;
	mouseY=y;
	mouseDragged();
}
void passiveMotionFunc(int x,int y)
{
	mouseX=x;
	mouseY=y;
	mouseMoved();
}
int getKeyChar()
{
	char c=keyChar;
	keyChar=0;
	return c;
}
int getKeyCode()
{
	int k=keyCode;
	keyCode=-1;
	return k;
}
int getX()
{
	return mouseX;
}
int getY()
{
	return mouseY;
}
String getSource()
{
	return actionSource;
}

