#include<glut.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define PI 3.1415926
#define String char*
#define byte GLubyte
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
float GREEN[]={0.0,1.0,0.0,0.7};
float BLUE[]={0.0,0.0,1.0,1.0};
float WHITE[]={1.0,1.0,1.0,1.0};
double radius=0.2;
int slices=20,stacks=10;

float rotateY=0,minX=-5,maxX=-minX,minZ=-2,maxZ=-minZ,Y=-0.5;
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
GLuint planeList;
void glutInitPlaneList()
{
	planeList=glGenLists(1);
	glNewList(planeList,GL_COMPILE);
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
	glEndList();
}
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
        glDisable(GL_LIGHTING);
    glShadeModel(GL_FLAT);
/*
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT,GL_AMBIENT_AND_DIFFUSE);
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	glMaterialfv(GL_FRONT,GL_SPECULAR,WHITE);
*/
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	for(int i=0;i<surfaceHeight-1;i++)
	{
		glBegin(GL_TRIANGLE_STRIP);	
		for(int j=0;j<surfaceWidth;j++)
		{
			float transparency=0.7f;
			Point3f* p0=coordinates[i*surfaceWidth+j];
			Point3f* p1=coordinates[(i+1)*surfaceWidth+j];
			Vector3f* n0=normals[i*surfaceWidth+j];
			Vector3f* n1=normals[(i+1)*surfaceWidth+j];
			Color3f* c0=colors[i*surfaceWidth+j];
			Color3f* c1=colors[(i+1)*surfaceWidth+j];
			glNormal3f(n0->x,n0->y,n0->z);
			glColor4f(c0->red,c0->green,c0->blue,transparency);
			glVertex3f(p0->x,p0->y,p0->z);
			glNormal3f(n1->x,n1->y,n1->z);
			glColor4f(c1->red,c1->green,c1->blue,transparency);
			glVertex3f(p1->x,p1->y,p1->z);
		}
		glEnd();
	}
    glShadeModel(GL_SMOOTH);
	glDisable(GL_BLEND);
	glDisable(GL_COLOR_MATERIAL);
        glEnable(GL_LIGHTING);
}
void glutSolidNurbsSurface()
{
	glEnable(GL_COLOR_MATERIAL);
	glEnable(GL_AUTO_NORMAL);
	glColorMaterial(GL_FRONT,GL_AMBIENT_AND_DIFFUSE);
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	glMaterialfv(GL_FRONT,GL_SPECULAR,WHITE);
	const int m=4,n=4,uknotCount=8,vknotCount=8,uOrder=4,vOrder=4;
	float uknots[uknotCount]={0,0,0,0,1,1,1,1};
	float vknots[vknotCount]={0,0,0,0,1,1,1,1};
	float controlPoints[m*n*3];
	for(int i=0;i<m;i++)
	{
		for(int j=0;j<n;j++)
		{
			controlPoints[i*n*3+j*3+0]=0.8*(j-1.5);
			if((i==1||i==2)&&(j==1||j==2))controlPoints[i*n*3+j*3+1]=0.6;
			else controlPoints[i*n*3+j*3+1]=-0.2;
			controlPoints[i*n*3+j*3+2]=0.4*(i-1.5);
		}
	}
	GLUnurbsObj* nurbsSurface=gluNewNurbsRenderer();
	gluBeginSurface(nurbsSurface);
	gluNurbsSurface(nurbsSurface,uknotCount,uknots,vknotCount,vknots,n*3,3,controlPoints,uOrder,vOrder,GL_MAP2_VERTEX_3);
	gluEndSurface(nurbsSurface);
	glDisable(GL_COLOR_MATERIAL);
}
byte* readImage(String imageName,int width,int height)
{
	char fileName[100];
	sprintf(fileName,"%s(%d,%d).OPGL",imageName,width,height);
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return NULL;
	byte *pixels=(byte*)malloc(width*height*4*sizeof(byte));
	fread(pixels,sizeof(byte),width*height*4,file);
	fclose(file);
	return pixels;
}
static GLuint texture;
byte* imagePixels;
int imageWidth=237,imageHeight=291;
void glInitTexture()
{
	String imageName="JavaAndOpenGL";
	imagePixels= readImage(imageName,imageWidth,imageHeight);
	glEnable(GL_TEXTURE_2D);
	glPixelStorei(GL_UNPACK_ALIGNMENT,1);
	glGenTextures(1,&texture);
	glBindTexture(GL_TEXTURE_2D,texture);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
	glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,imageWidth,imageHeight,0,GL_RGBA,GL_UNSIGNED_BYTE,imagePixels);
	glDisable(GL_TEXTURE_2D);
}
void glutImageSurface()
{
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	glMaterialfv(GL_FRONT,GL_SPECULAR,WHITE);
	glEnable(GL_TEXTURE_2D);
	glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_MODULATE);
	glBindTexture(GL_TEXTURE_2D,texture);
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
			glTexCoord2f((0.0f+i)/(surfaceHeight-1),(0.0f+j)/(surfaceWidth-1));
			glNormal3f(n0->x,n0->y,n0->z);
			glColor3f(c0->red,c0->green,c0->blue);
			glVertex3f(p0->x,p0->y,p0->z);
			glTexCoord2f((0.0f+i+1)/(surfaceHeight-1),(0.0f+j)/(surfaceWidth-1));
			glNormal3f(n1->x,n1->y,n1->z);
			glColor3f(c1->red,c1->green,c1->blue);
			glVertex3f(p1->x,p1->y,p1->z);
		}
		glEnd();
	}
	glDisable(GL_TEXTURE_2D);
}
void glutImage()
{
	glMaterialfv(GL_FRONT,GL_AMBIENT,BLACK);
	glMaterialfv(GL_FRONT,GL_SPECULAR,WHITE);
	glMaterialfv(GL_FRONT,GL_DIFFUSE,BLACK);
	glEnable(GL_TEXTURE_2D);
//	glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_ADD);
//	glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_REPLACE);
//	glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_BLEND);
	glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_MODULATE);
//	glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_COMBINE);
	glBindTexture(GL_TEXTURE_2D,texture);
	glBegin(GL_QUADS);

	glTexCoord2f(0.0,0.0);
	glNormal3f(0,1,1);
	glVertex3f(-1,1,-1);

	glTexCoord2f(1.0,0.0);
	glNormal3f(0,1,1);
	glVertex3f(-1,1,0);

	glTexCoord2f(1.0,1.0);
	glNormal3f(0,1,1);
	glVertex3f(1,1,0);

	glTexCoord2f(0.0,1.0);
	glNormal3f(0,1,1);
	glVertex3f(1,1,-1);

	glEnd();
	glDisable(GL_TEXTURE_2D);
}
float viewDistance=2.0;
Vector3f* viewDirection=newVector3f(0,0,1);
Vector3f* viewUpDirection=newVector3f(0,1,0);
Vector3f* viewRightDirection=newVector3f(1,0,0);
int currentMouseX=0,currentMouseY=0,currentMouseZ=0,currentMouseButton=0;
int translateMouseX=0,translateMouseY=0,translateMouseZ=0;
float eyeX=0.0,eyeY=0.0,eyeZ=viewDistance,centerX=0.0,centerY=0.0,centerZ=0.0,upX=0,upY=1,upZ=0;
float viewTranslateX=0,viewTranslateY=0,viewTranslateZ=0;
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
void glRotateViewDirection()
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

	glutSolidSurface();

//	glutImageSurface();

	glCallList(planeList);

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
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA,GL_ONE);
	glDepthMask(GL_FALSE);
	glPushMatrix();
	glTranslatef(0.0f,0.1f,-2.5f);
	glPushMatrix();
	glRotatef(-(rotateY+=0.5)*2.0f,0.0f,1.0f,0.0f);
	glTranslatef(1.0f,0.0f,0.0f);
	glutSolidSphere(0.1f,17,9);
	glPopMatrix();
	glDepthMask(GL_TRUE);
	glDisable(GL_BLEND);

	glPushMatrix();
	glTranslatef(-1.0f,0.5f,0.0f);
	glColor3f(0.0f,0.0f,1.0f);
//	glutSolidNurbsSurface();
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
		translateMouseX=x-currentMouseX;
		translateMouseY=y-currentMouseY;
		currentMouseX=x;
		currentMouseY=y;
		glRotateViewDirection();
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
	}
}
int main(int argc,char** argv)
{
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("");
	glutInitGeometryInfo();
	glutInitPlaneList();
	glInitTexture();
	glutDisplayFunc(displayFunc);
	glutReshapeFunc(reshapeFunc);
	glutMouseFunc(mouseFunc);
	glutMotionFunc(motionFunc);
	glutSpecialFunc(specialFunc);
	glutTimerFunc(33,timerFunc,1);
	glutMainLoop();
}
