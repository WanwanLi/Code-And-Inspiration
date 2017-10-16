#include "def.h"
#define Button struct Button
Button
{
	String name;
	int x,y,w,h;
	Button* next;
};
Button* newButton(String name,int x,int y,int w,int h)
{
	Button* button=(Button*)malloc(sizeof(Button));
	button->name=name;
	button->x=x;
	button->y=y;
	button->w=w;
	button->h=h;
	return button;
};
Button* firstButton=null;
Button* lastButton=null;
int buttonLength=0;
void addButton(String name,int x,int y,int w,int h)
{
	Button* button=newButton(name,x,y,w,h);
	if(firstButton==null)
	{
		firstButton=lastButton=button;
	}
	else
	{
		lastButton->next=button;
		lastButton=button;
	}
	buttonLength++;
}

