//#include "GocatorDevice.h"
#include <glut.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#ifndef GocatorDevice_h
void initDevices(){}
void connectDevices(){}
int c=0;
void receiveDataFromDevices(double* profile,int profilePointCount)
{
	double w=2,x0=-w/2;
	double dx=w/profilePointCount;
	for(int i=0;i<profilePointCount;i++)
	{
		double x=x0+i*dx;
		profile[i]=0.1*sin(10*x+sin(0.0+0.01*c));
		c+=1;
	}
}
void stopDevices(){}
#endif
float backgroundColor[]={0.0,0.0,0.0,0.0};
float lightPosition[]={2.0,2.0,2.0,1};
float lightDirection[]={0.0,0.0,2.0,0};
float lightColor[]={1.0,1.0,1.0,1.0};
float ambientColor[]={0.3,0.2,0.05,1.0};
float diffuseColor[]={1.0,0.0,0.0,1.0};
float specularColor[]={1.0,1.0,1.0,1.0};
float specularShininess=50.0;
float BLACK[]={0.0,0.0,0.0,1.0};
float RED[]={1.0,0.0,0.0,1.0};
float GREEN[]={0.0,1.0,0.0,1.0};
float BLUE[]={0.0,0.0,1.0,1.0};
float WHITE[]={1.0,1.0,1.0,1.0};
double radius=0.2;
int slices=20,stacks=10;
float PI=3.1415926;
float distanceR=2.0,angleU=PI/2,angleV=-PI/2,up=1;
float eyeX=0.0,eyeY=0.0,eyeZ=distanceR,centerX=0.0,centerY=0.0,centerZ=0.0;
int currentMouseX=0,currentMouseY=0,currentMouseZ=0,currentMouseButton=0;
int translateMouseX=0,translateMouseY=0,translateMouseZ=0;
float rotY=0.0,minX=-5,maxX=-minX,minZ=-2,maxZ=-minZ,Y=-0.5;
void glTransformViewPoint()
{
	float k=0.01;up=1;
	angleV=PI/2+k*translateMouseX;
	angleU=PI/2-k*translateMouseY;
	if(angleU<0)up=((int)(angleU/-PI)%2==0)?-1:1;
	if(angleU>PI)up=((int)(angleU/PI)%2==1)?-1:1;
	float viewDirectionX=sin(angleU)*cos(angleV);
	float viewDirectionY=cos(angleU);
	float viewDirectionZ=sin(angleU)*sin(angleV);
	centerX=viewDirectionX*k*translateMouseZ;
	centerY=viewDirectionY*k*translateMouseZ;
	centerZ=viewDirectionZ*k*translateMouseZ;
	eyeX=centerX+viewDirectionX*distanceR;
	eyeY=centerY+viewDirectionY*distanceR;
	eyeZ=centerZ+viewDirectionZ*distanceR;
}
int profileCount=800;
int profilePointCount=500;
int currentProfileIndex=0;
double profileSurfaceWidth=8,startZ=-profileSurfaceWidth,endZ=profileSurfaceWidth,dy=(endZ-startZ)/(profilePointCount-1);
double profileSurfaceLength=12,startX=-profileSurfaceLength,endX=profileSurfaceLength,dx=(endX-startX)/(profileCount-1);
double** profileSurface;
double* newDouble(int length)
{
	double* d=(double*)malloc(length*sizeof(double));
	for(int i=0;i<length;i++)d[i]=0;
	return d;
}
void glutInitGocatorDeviceAndProfileSurfaceData()
{
	initDevices();
	connectDevices();
	profileSurface=(double**)malloc(profileCount*sizeof(double*));
	for(int i=0;i<profileCount;i++)
	{
		profileSurface[i]=newDouble(profilePointCount);
	}
}
void glutGetProfileData()
{
	receiveDataFromDevices(profileSurface[currentProfileIndex],profilePointCount);
//	receiveDataFromDevice(2,profileSurface[currentProfileIndex],profilePointCount);
	currentProfileIndex=(currentProfileIndex+1)%profileCount;
}
void glutRawProfileCurve()
{
	glBegin(GL_LINES);
	double w=4,x0=-w/2;
	double dx=w/dataLength;
	for(int i=0;i<dataLength-1;i++)
	{
		double x=x0+i*dx;
		glVertex3f(x,getZValue(i),0);
		glVertex3f(x+dx,getZValue(i+1),0);
	}
	glEnd();
}
void glutProfileCurve()
{
	int i=currentProfileIndex==0?0:(currentProfileIndex-1)%profileCount;
	double*profile=profileSurface[i];
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	glColor3f(1,0,0);
	glBegin(GL_LINES);
	double w=4,x0=-w/2;
	double dx=w/profilePointCount;
	for(int i=0;i<profilePointCount-1;i++)
	{
		double x=x0+i*dx;
		glVertex3f(x,profile[i],0);
		glVertex3f(x+dx,profile[i+1],0);
	}
	glEnd();
}
void glutProfileSurface()
{
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT,GL_AMBIENT_AND_DIFFUSE);
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	for(int j=0;j<profileCount-2;j++)
	{
		int j0=(currentProfileIndex+j+0)%profileCount;
		int j1=(currentProfileIndex+j+1)%profileCount;
		int j2=(currentProfileIndex+j+2)%profileCount;
		double x0=startX+j*dx;	
		double x1=x0+dx;
		glBegin(GL_TRIANGLE_STRIP);	
		for(int i=0;i<profilePointCount-1;i++)
		{
			double z0=startZ+i*dz;
			double y00=profileSurface[j0][i+0];
			double y10=profileSurface[j1][i+0];
			double y20=profileSurface[j2][i+0];
			double y01=profileSurface[j0][i+1];
			double y11=profileSurface[j1][i+1];
			double dyx00=(y10-y00)/dx;
			double dyz00=(y01-y00)/dz;
			double dyx10=(y20-y10)/dx;
			double dyz10=(y11-y10)/dz;
			glNormal3f(-dyx00,1,-dyz00);
			glVertex3f(x0,y00,z0);
			glNormal3f(-dyx10,1,-dyz10);
			glVertex3f(x1,y10,z0);
		}
		glEnd();
	}
	glDisable(GL_COLOR_MATERIAL);
}
void glutStopDeviceAndFreeProfileSurfaceData()
{
	stopDevices();
	for(int i=0;i<profileCount;i++)
	{
		free(profileSurface[i]);
	}
}
void displayFunc()
{
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTransformViewPoint();
	gluLookAt(eyeX,eyeY,eyeZ,centerX,centerY,centerZ,0,up,0);
	glClearColor(backgroundColor[0],backgroundColor[1],backgroundColor[2],backgroundColor[3]);
	glCullFace(GL_BACK);
	glFrontFace(GL_CCW);
	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glLightfv(GL_LIGHT0,GL_AMBIENT,lightColor);
	glLightfv(GL_LIGHT0,GL_SPECULAR,lightColor);
	glLightfv(GL_LIGHT0,GL_DIFFUSE,lightColor);
	glLightfv(GL_LIGHT0,GL_POSITION,lightPosition);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
//	glutRawProfileCurve();
//	glutProfileCurve();
	glutProfileSurface();
	glutSwapBuffers();
}
int timeDelay=1;
void timerFunc(int value)
{
	glutGetProfileData();
	glutPostRedisplay();
	glutTimerFunc(timeDelay,timerFunc,1);
}
float fieldOfView=45.0,near=1.0,far=40.0;
void reshapeFunc(int w,int h)
{
	glViewport(0,0,w,h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(fieldOfView,(w+0.0)/h,near,far);
	glutPostRedisplay();
}
void mouseFunc(int button,int state,int x,int y)
{
	if(state==GLUT_DOWN)
	{
		currentMouseX=x;
		currentMouseY=y;
		currentMouseZ=y;
		currentMouseButton=button;
	}
}

void motionFunc(int x,int y)
{
	if(currentMouseButton==GLUT_RIGHT_BUTTON)
	{
		translateMouseX+=x-currentMouseX;
		translateMouseY+=y-currentMouseY;
		currentMouseX=x;
		currentMouseY=y;
	}
	else
	{
		translateMouseZ+=y-currentMouseZ;
		currentMouseZ=y;
	}
	glutPostRedisplay();
}
int main(int argc,char** argv)
{
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
	glutInitGocatorDeviceAndProfileSurfaceData();
	glutStopDeviceAndFreeProfileSurfaceData();
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("C And GocatorDevice");
	glutDisplayFunc(displayFunc);
	glutReshapeFunc(reshapeFunc);
	glutMouseFunc(mouseFunc);
	glutMotionFunc(motionFunc);
	glutTimerFunc(timeDelay,timerFunc,1);
	glutMainLoop();
}
