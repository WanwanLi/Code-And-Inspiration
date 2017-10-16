#include <stdio.h>
#include "windows.h"
#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#define MAX_VALUE 99999

double* newDouble(int length)
{
	double* array=(double*)malloc(length*sizeof(double));
	return array;
}
void initScreenZBuffer(double* screenZBuffer,int screenWidth,int screenHeight)
{
	for(int i=0;i<screenHeight;i++)
	{
		for(int j=0;j<screenWidth;j++)
		{
			screenZBuffer[i*screenWidth+j]=-MAX_VALUE;
		}
	}
}
int Julia(double x0,double y0,double z0,double t0)
{
	double x;
	double y;
	double z;
	double t;
	double p=-1;
	double q=0.2;
	double s=0;
	double v=0;
	int i;
	for(i=0;i<100;i++)
	{
		x=x0*x0-y0*y0-z0*z0-t0*t0+p;
		y=2*x0*y0+q;
		z=2*x0*z0+s;
		t=2*x0*t0+v;
		if (x*x+y*y+z*z+t*t>4)return i;		         
		x0=x;
		y0=y;
		z0=z;
		t0=t;
	}
	return i;	
}
boolean isRoot(double x,double y,double z,double t,double rotY)
{
	double Z=z*cos(rotY)-x*sin(rotY);
	double X=z*sin(rotY)+x*cos(rotY);
	return (Julia(X,y,Z,t)==100);
}

double getRoot(double x,double y,double rotY,double dZ)
{
	double r=1.5;
	double maxZ=r;
	double minZ=-r;
	double z=maxZ;
	double dz=z/100;
	double t=0;
	while(dz>dZ)
	{
		while(!isRoot(x,y,z,t,rotY))
		{
			z-=dz;
			if(z<minZ)break;
		}
		if(z<minZ)break;
		while(isRoot(x,y,z,t,rotY))
		{
			dz/=2;
			z+=dz;
		}
	}
	if(dz<=dZ)return z;
	else return -MAX_VALUE;
}
void getScreenZBuffer(double* screenZBuffer,int screenWidth,int screenHeight,double rotY)
{
	double k=200,dZ=0.001;
	for(int i=0;i<screenHeight;i++)
	{
		for(int j=0;j<screenWidth;j++)
		{
			double x=(j-screenWidth*0.5)/k;
			double y=(i-screenHeight*0.5)/k;
			screenZBuffer[i*screenWidth+j]=getRoot(x,y,rotY,dZ)*k;
		}
	}
}
double* screenZBuffer;
int screenWidth;
int screenHeight;
double rotY;
DirectionalLight* directionalLight;
Color* diffuseColor;
Color* specularColor;
Vector* getPixelNormal(int i,int j)
{
	if(i>=screenHeight-1||j>=screenWidth-1)return null;
	double z00=screenZBuffer[(i+0)*screenWidth+(j+0)];
	double z01=screenZBuffer[(i+0)*screenWidth+(j+1)];
	double z10=screenZBuffer[(i+1)*screenWidth+(j+0)];
	Vector* v0=newVector(1,0,z01-z00);
	Vector* v1=newVector(0,1,z10-z00);
	Vector* PixelNormal=cross(v0,v1);
	free(v0);
	free(v1);
	return PixelNormal;
}
Color* getColor(Vector* PixelNormal)
{
	if(PixelNormal==null)return newColor(0,0,0);
	double redDiffusionRatio=diffuseColor->red/255.0;
	double greenDiffusionRatio=diffuseColor->green/255.0;
	double blueDiffusionRatio=diffuseColor->blue/255.0;
	double cosLightDirection_PixelNormal=Vector_cos(oppositeVector(directionalLight->direction),PixelNormal);
	double red=redDiffusionRatio*directionalLight->color->red*cosLightDirection_PixelNormal;
	double green=greenDiffusionRatio*directionalLight->color->green*cosLightDirection_PixelNormal;
	double blue=blueDiffusionRatio*directionalLight->color->blue*cosLightDirection_PixelNormal;
	if(red<0)red=-red;
	if(green<0)green=-green;
	if(blue<0)blue=-blue;
	return newColor((int)red,(int)green,(int)blue);
}
void drawPixel(int i,int j)
{
/*	Vector* normal=getPixelNormal(i,j);
	if(normal==null)return;
	Color* color=getColor(normal);
*/
	Color* color=newColor(i%255,j%255,(i+j)%255);
	setColor(color);
	drawLine(j,i,j,i);
//	free(normal);
	free(color);
}
boolean isCloseToZero(double d)
{
	return (d>0&&d-0.0<1E-8)||(0>d&&0.0-d<1E-8);
}
void initWinMain()
{
	directionalLight=newDirectionalLight(newColor(255,255,255),newVector(0,0,-1));
	diffuseColor=newColor(255,0,0);
	specularColor=newColor(255,0,0);
	screenWidth=800;
	screenHeight=500;
	screenZBuffer=newDouble(screenHeight*screenWidth);
	initScreenZBuffer(screenZBuffer,screenWidth,screenHeight);
	rotY=0;
	getScreenZBuffer(screenZBuffer,screenWidth,screenHeight,rotY);
}
int WINAPI WinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,PSTR szCmdLine,int iCmdShow)
{
//	initWinMain();
	winMain(hInstance,hPrevInstance,szCmdLine,iCmdShow);
	newWindow("Cuda And Julia3D",10,30,screenWidth,screenHeight);
	showWindow();
	return 0;
}
void onCreate()
{
	
}
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
	/*
	if(!b)return;
	dx+=getX()-x;
	dy+=getY()-y;
	x=getX();
	y=getY();
	rotY=dx*0.01;
	getScreenZBuffer(screenZBuffer,screenWidth,screenHeight,rotY);
	*/
}
void mouseReleased()
{
	b=false;
}
void paint()
{
	for(int i=0;i<screenHeight-1;i++)
	{
		for(int j=0;j<screenWidth-1;j++)
		{
			drawPixel(i,j);
		}
	}
}
