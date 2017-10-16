#include<OpenGL\glut.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
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
	angleU=PI/2+k*translateMouseY;
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
#define Color3f struct Color3f
Color3f
{
	float red;
	float green;
	float blue;
};
Color3f* newColor3f(float red,float green,float blue)
{
	Color3f* color=(Color3f*)(malloc(sizeof(Color3f)));
	color->red=red;
	color->green=green;
	color->blue=blue;
	return color;
}
Color3f** newColor3f(int length)
{
	Color3f** colors=(Color3f**)(malloc(length*sizeof(Color3f*)));
	return colors;
}
#define Point3f struct Point3f
Point3f
{
	float x;
	float y;
	float z;
};
Point3f* newPoint3f(float x,float y,float z)
{
	Point3f* point=(Point3f*)(malloc(sizeof(Point3f)));
	point->x=x;
	point->y=y;
	point->z=z;
	return point;
}
Point3f** newPoint3f(int length)
{
	Point3f** points=(Point3f**)(malloc(length*sizeof(Point3f*)));
	return points;
}
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
Vector3f** newVector3f(int length)
{
	Vector3f** vectors=(Vector3f**)(malloc(length*sizeof(Vector3f*)));
	return vectors;
}
float f(float x,float y)
{
	return 0.1*sin(5*x);
}
float g(float u,float v)
{
	return u+v;
}
Point3f** coordinates;
Vector3f** normals;
Color3f** colors;
int surfaceWidth=100;
int surfaceHeight=800;
float x0=-2.0,x1=2.0,z0=-1.0,z1=1.0;
float dx=(x1-x0)/(surfaceWidth-1),dz=(z1-z0)/(surfaceHeight-1);
void glutInitGeometryInfo()
{
	coordinates=newPoint3f(surfaceHeight*surfaceWidth);
	normals=newVector3f(surfaceHeight*surfaceWidth);
	colors=newColor3f(surfaceHeight*surfaceWidth);
	for(int i=0;i<surfaceHeight;i++)
	{
		float z=z0+i*dz;
		for(int j=0;j<surfaceWidth;j++)
		{
			float x=x0+j*dx;
			float dfx=f(x+dx,z)-f(x,z);
			float dfz=f(x,z+dz)-f(x,z);
			float u=i*1.0/(surfaceHeight-1);
			float v=j*1.0/(surfaceWidth-1);
			coordinates[i*surfaceWidth+j]=newPoint3f(x,f(x,z),z);
			normals[i*surfaceWidth+j]=newVector3f(-dfx/dx,1,-dfz/dz);
			colors[i*surfaceWidth+j]=newColor3f(u,g(u,v),v);
		}
	}
}
void glutSolidSurface()
{
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT,GL_AMBIENT_AND_DIFFUSE);
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	glMaterialfv(GL_FRONT,GL_SPECULAR,WHITE);
	for(int i=0;i<surfaceHeight-1;i++)
	{
		glBegin(GL_TRIANGLE_STRIP);	
		for(int j=0;j<surfaceWidth;j++)
		{
			Point3f* p0=coordinates[i*surfaceWidth+j];
			Point3f* p1=coordinates[(i+1)*surfaceWidth+j];
			Vector3f* n0=normals[i*surfaceWidth+j];
			Vector3f* n1=normals[(i+1)*surfaceWidth+j];
			Color3f* c0=colors[i*surfaceWidth+j];
			Color3f* c1=colors[(i+1)*surfaceWidth+j];
			glNormal3f(n0->x,n0->y,n0->z);
			glColor3f(c0->red,c0->green,c0->blue);
			glVertex3f(p0->x,p0->y,p0->z);
			glNormal3f(n1->x,n1->y,n1->z);
			glColor3f(c1->red,c1->green,c1->blue);
			glVertex3f(p1->x,p1->y,p1->z);
		}
		glEnd();
	}
	glDisable(GL_COLOR_MATERIAL);
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

	glutSolidSurface();


	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT,GL_AMBIENT_AND_DIFFUSE);
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	glMaterialfv(GL_FRONT,GL_SPECULAR,WHITE);
	glBegin(GL_QUADS);
	glNormal3f(0,1,0);
	glColor3f(0.60f,0.40f,0.10f);
	glVertex3f(minX,Y,minZ);
	glColor3f(0.60f,0.40f,0.50f);
	glVertex3f(minX,Y,maxZ);
	glColor3f(0.60f,0.80f,0.10f);
	glVertex3f(maxX,Y,maxZ);
	glColor3f(0.90f,0.40f,0.10f);
	glVertex3f(maxX,Y,minZ);
	glEnd();
	glDisable(GL_COLOR_MATERIAL);


	glMaterialfv(GL_FRONT,GL_AMBIENT,ambientColor);
	glMaterialfv(GL_FRONT,GL_DIFFUSE,diffuseColor);
	glMaterialfv(GL_FRONT,GL_SPECULAR,specularColor);
	glMaterialf(GL_FRONT,GL_SHININESS,specularShininess);

	glPushMatrix();
	glColor3f(1,0,0);
	glTranslatef(0,0.2,-2);
	GLUquadric* gluQuadric=gluNewQuadric();
	gluQuadricDrawStyle(gluQuadric,GLU_LINE);
	gluQuadricDrawStyle(gluQuadric,GLU_FILL);
	gluQuadricNormals(gluQuadric,GLU_FLAT);
	gluQuadricNormals(gluQuadric,GLU_SMOOTH);
	gluSphere(gluQuadric,radius,slices,stacks);
	glPopMatrix();

	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	glMaterialfv(GL_FRONT,GL_DIFFUSE,GREEN);
	glMaterialfv(GL_FRONT,GL_SPECULAR,BLACK);

	glPushMatrix();
	glTranslatef(0.0f,0.1f,-2.5f);
	glPushMatrix();
	glRotatef(-(rotY+=0.5)*2.0f,0.0f,1.0f,0.0f);
	glTranslatef(1.0f,0.0f,0.0f);
	glutSolidSphere(0.1f,17,9);
	glPopMatrix();     
	glPopMatrix();

	glutSwapBuffers();
}
void timerFunc(int value)
{
	glutPostRedisplay();
	glutTimerFunc(3,timerFunc,1);
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
	glutInitGeometryInfo();
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("");
	glutDisplayFunc(displayFunc);
	glutReshapeFunc(reshapeFunc);
	glutMouseFunc(mouseFunc);
	glutMotionFunc(motionFunc);
	glutTimerFunc(33,timerFunc,1);
	glutMainLoop();
}
