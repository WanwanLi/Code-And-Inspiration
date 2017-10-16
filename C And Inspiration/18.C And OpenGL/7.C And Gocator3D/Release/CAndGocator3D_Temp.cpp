

//#include"GocatorDevice.h"
#include<glut.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <memory.h> 
#ifndef GOCATOR_DEVICE
int devicesAreRunning=0;
void initDevices(){}
void connectDevices(){devicesAreRunning=1;}
int c=0;
void receiveDataFromDevices(double* profile,int profilePointCount)
{
	double w=2,x0=-w/2;
	double dx=w/profilePointCount;
	for(int i=0;i<profilePointCount;i++)
	{
		double x=x0+i*dx;
		double k=0.1*sin(0.1+c);
		profile[i]=0.1*sin(10*x+k);
		c+=1;
	}
}
void stopDevices(){devicesAreRunning=0;}
#endif
float PI=3.1415926;
#define Vector3f struct Vector3f
Vector3f
{
	float x;
	float y;
	float z;
};
Vector3f* newVector3f(float x,float y,float z)
{
	Vector3f* vector=(Vector3f*)(malloc(sizeof(Vector3f)));
	vector->x=x;
	vector->y=y;
	vector->z=z;
	return vector;
}
Vector3f* cross(Vector3f* v0,Vector3f* v1)
{
	Vector3f* vector=(Vector3f*)(malloc(sizeof(Vector3f)));
	vector->x=v0->y*v1->z-v1->y*v0->z;
	vector->y=v0->z*v1->x-v1->z*v0->x;
	vector->z=v0->x*v1->y-v1->x*v0->y;
	return vector;
}
float angleToY(float x,float y)
{
	float r=sqrt(x*x+y*y);
	return r==0?0:y>=0?asin(x/r):PI-asin(x/r);
}
void rotX(Vector3f* v,float a)
{
	float y=v->y;
	float z=v->z;
	v->y=y*cos(a)-z*sin(a);
	v->z=y*sin(a)+z*cos(a);
}
void rotY(Vector3f* v,float a)
{
	float z=v->z;
	float x=v->x;
	v->z=z*cos(a)-x*sin(a);
	v->x=z*sin(a)+x*cos(a);
}
void rotZ(Vector3f* v,float a)
{
	float x=v->x;
	float y=v->y;
	v->x=x*cos(a)-y*sin(a);
	v->y=x*sin(a)+y*cos(a);
}
void rotate(Vector3f* vector,Vector3f* axis,float angle)
{
	Vector3f* v=newVector3f(vector->x,vector->y,vector->z);
	Vector3f* a=newVector3f(axis->x,axis->y,axis->z);
	float rotz=angleToY(a->x,a->y);
	rotZ(a,rotz);
	float rotx=-angleToY(a->z,a->y);
	rotZ(v,rotz);
	rotX(v,rotx);
	rotY(v,angle);
	rotX(v,-rotx);
	rotZ(v,-rotz);
	vector->x=v->x;
	vector->y=v->y;
	vector->z=v->z;
	free(v);
	free(a);
}
void scale(Vector3f* v,float a)
{
	v->x*=a;
	v->y*=a;
	v->z*=a;
}
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
float viewDistance=2.0;
Vector3f* viewDirection=newVector3f(0,0,1);
Vector3f* viewUpDirection=newVector3f(0,1,0);
Vector3f* viewRightDirection=newVector3f(1,0,0);
float eyeX=0.0,eyeY=0.0,eyeZ=viewDistance,centerX=0.0,centerY=0.0,centerZ=0.0,upX=0,upY=1,upZ=0;
float viewTranslateX=0,viewTranslateY=0,viewTranslateZ=0;
float minX=-5,maxX=-minX,minZ=-2,maxZ=-minZ,Y=-0.5;
int currentMouseX=0,currentMouseY=0,currentMouseZ=0,currentMouseButton=0;
int translateMouseX=0,translateMouseY=0,translateMouseZ=0;
void glTransformViewPoint()
{
	float k=0.01;
	centerX=viewDirection->x*k*translateMouseZ+viewTranslateX;
	centerY=viewDirection->y*k*translateMouseZ+viewTranslateY;
	centerZ=viewDirection->z*k*translateMouseZ+viewTranslateZ;
	eyeX=centerX+viewDirection->x*viewDistance;
	eyeY=centerY+viewDirection->y*viewDistance;
	eyeZ=centerZ+viewDirection->z*viewDistance;
}
int profileCount=500;
int profilePointCount=100;
int currentProfileIndex=0;
double startY=-2.0,endY=2.0,dy=(endY-startY)/(profilePointCount-1);
double startX=-4.0,endX=4.0,dx=(endX-startX)/(profileCount-1);
double** profileSurface;
double* newDouble(int length)
{
	double* d=(double*)malloc(length*sizeof(double));
	for(int i=0;i<length;i++)d[i]=0;
	return d;
}
void glProfile(double* profile)
{
	glBegin(GL_LINES);
	glColor3f(1,0,0);
	double w=2,x0=-w/2;
	double dx=w/profilePointCount;
	for(int i=0;i<profilePointCount-1;i++)
	{
		double x=x0+i*dx;
		glVertex3f(x,profile[i],0);
		glVertex3f(x+dx,profile[i+1],0);
	}
	glEnd();
}
void glProfileSurface()
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
			double y0=startY+i*dy;
			double z00=profileSurface[j0][i+0];
			double z10=profileSurface[j1][i+0];
			double z20=profileSurface[j2][i+0];
			double z01=profileSurface[j0][i+1];
			double z11=profileSurface[j1][i+1];
			double dzx00=(z10-z00)/dx;
			double dzy00=(z01-z00)/dy;
			double dzx10=(z20-z10)/dx;
			double dzy10=(z11-z10)/dy;
			glNormal3f(-dzx00,-dzy00,1);
			glVertex3f(x0,y0,z00);
			glNormal3f(-dzx10,-dzy10,1);
			glVertex3f(x1,y0,z10);
		}
		glEnd();
	}
	glDisable(GL_COLOR_MATERIAL);
}
void glProfileMesh()
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
		glBegin(GL_LINE_STRIP);	
		for(int i=0;i<profilePointCount-1;i++)
		{
			double y0=startY+i*dy;
			double z00=profileSurface[j0][i+0];
			double z10=profileSurface[j1][i+0];
			double z20=profileSurface[j2][i+0];
			double z01=profileSurface[j0][i+1];
			double z11=profileSurface[j1][i+1];
			double dzx00=(z10-z00)/dx;
			double dzy00=(z01-z00)/dy;
			double dzx10=(z20-z10)/dx;
			double dzy10=(z11-z10)/dy;
			glNormal3f(-dzx00,-dzy00,1);
			glVertex3f(x0,y0,z00);
			glNormal3f(-dzx10,-dzy10,1);
			glVertex3f(x1,y0,z10);
		}
		glEnd();
	}
	glDisable(GL_COLOR_MATERIAL);
}
void glProfilePoints()
{
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT,GL_AMBIENT_AND_DIFFUSE);
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	for(int j=0;j<profileCount-2;j++)
	{
		int j0=(currentProfileIndex+j+0)%profileCount;
		int j1=(currentProfileIndex+j+1)%profileCount;
		double x0=startX+j*dx;	
		double x1=x0+dx;
		glBegin(GL_POINTS);	
		for(int i=0;i<profilePointCount-1;i++)
		{
			double y0=startY+i*dy;
			double z00=profileSurface[j0][i+0];
			double z10=profileSurface[j1][i+0];
			double z01=profileSurface[j0][i+1];
			double dzx00=(z10-z00)/dx;
			double dzy00=(z01-z00)/dy;
			glNormal3f(-dzx00,-dzy00,1);
			glVertex3f(x0,y0,z00);
		}
		glEnd();
	}
	glDisable(GL_COLOR_MATERIAL);
}
void glutInitGocatorDeviceAndProfileSurface()
{
	initDevices();
	connectDevices();
	profileSurface=(double**)malloc(profileCount*sizeof(double*));
	for(int i=0;i<profileCount;i++)profileSurface[i]=newDouble(profilePointCount);
}
void glutReceiveProfileData()
{
	if(devicesAreRunning)receiveDataFromDevices(profileSurface[currentProfileIndex],profilePointCount);
	else memset(profileSurface[currentProfileIndex],0,profilePointCount*sizeof(double));
	currentProfileIndex=(currentProfileIndex+1)%profileCount;
}
#define PROFILE_SURFACE 0
#define PROFILE_MESH 1
#define PROFILE_POINTS 2
int profileType=PROFILE_SURFACE,profileKind=3;
void glProfileData()
{
	switch(profileType)
	{
		case PROFILE_SURFACE:glProfileSurface();break;
		case PROFILE_MESH:glProfileMesh();break;
		case PROFILE_POINTS:glProfilePoints();break;
	}
}
void displayFunc()
{
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTransformViewPoint();
	gluLookAt(eyeX,eyeY,eyeZ,centerX,centerY,centerZ,upX,upY,upZ);
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
	glProfileData();
	glutSwapBuffers();
}
void timerFunc(int value)
{
	glutReceiveProfileData();
	glutPostRedisplay();
	glutTimerFunc(33,timerFunc,1);
}
float fieldOfView=45.0,near=1.0,far=10.0;
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
void rotateViewDirection()
{
	float k=0.01;
	float rotUp=-k*translateMouseX;
	float rotRight=-k*translateMouseY;
	rotate(viewDirection,viewUpDirection,rotUp);
	rotate(viewRightDirection,viewUpDirection,rotUp);
	rotate(viewDirection,viewRightDirection,rotRight);
	rotate(viewUpDirection,viewRightDirection,rotRight);
	upX=viewUpDirection->x;
	upY=viewUpDirection->y;
	upZ=viewUpDirection->z;
}
void motionFunc(int x,int y)
{
	if(currentMouseButton==GLUT_RIGHT_BUTTON)
	{
		translateMouseX=x-currentMouseX;
		translateMouseY=y-currentMouseY;
		currentMouseX=x;
		currentMouseY=y;
		rotateViewDirection();
	}
	else
	{
		translateMouseZ+=y-currentMouseZ;
		currentMouseZ=y;
	}
	glutPostRedisplay();
}
void specialFunc(int k,int x,int y)
{
	float d=-0.1;
	switch(k)
	{
		case GLUT_KEY_UP:
		{
			viewTranslateX+=d*viewUpDirection->x;
			viewTranslateY+=d*viewUpDirection->y;
			viewTranslateZ+=d*viewUpDirection->z;
			return;
		}
		case GLUT_KEY_DOWN:
		{
			viewTranslateX-=d*viewUpDirection->x;
			viewTranslateY-=d*viewUpDirection->y;
			viewTranslateZ-=d*viewUpDirection->z;
			return;
		}
		case GLUT_KEY_LEFT:
		{
			viewTranslateX-=d*viewRightDirection->x;
			viewTranslateY-=d*viewRightDirection->y;
			viewTranslateZ-=d*viewRightDirection->z;
			return;
		}
		case GLUT_KEY_RIGHT:
		{
			viewTranslateX+=d*viewRightDirection->x;
			viewTranslateY+=d*viewRightDirection->y;
			viewTranslateZ+=d*viewRightDirection->z;
			return;
		}
		case GLUT_KEY_F1:
		{
			if(devicesAreRunning)
			{
				stopDevices();
				devicesAreRunning=0;
			}
			else
			{
				initDevices();
				connectDevices();
				devicesAreRunning=1;
			}
			return;
		}
		case GLUT_KEY_F2:
		{
			profileType=(profileType+1)%profileKind;
			return;
		}
	}
}
int main(int argc,char** argv)
{
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("C And GocatorDevice");
	glutInitGocatorDeviceAndProfileSurface();
	glutDisplayFunc(displayFunc);
	glutReshapeFunc(reshapeFunc);
	glutMouseFunc(mouseFunc);
	glutMotionFunc(motionFunc);
	glutTimerFunc(33,timerFunc,1);
	glutSpecialFunc(specialFunc);
	glutMainLoop();
}
