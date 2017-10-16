#include<math.h>
#include <glut.h>
#ifndef String
#define String char*
#endif
#ifndef null
#define null NULL
#endif
#define PI 3.1415926
#define Public extern "C" _declspec(dllexport)
#define BranchGroupList GLuint
#define Color3f struct Color3f
Color3f
{
	float red;
	float green;
	float blue;
};
Public Color3f* newColor3f(float red,float green,float blue)
{
	Color3f* color=(Color3f*)(malloc(sizeof(Color3f)));
	color->red=red;
	color->green=green;
	color->blue=blue;
	return color;
}
Public Color3f** new_Color3f(int length)
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
Public Point3f* newPoint3f(float x,float y,float z)
{
	Point3f* point=(Point3f*)(malloc(sizeof(Point3f)));
	point->x=x;
	point->y=y;
	point->z=z;
	return point;
}
Public Point3f** new_Point3f(int length)
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
Public Vector3f* newVector3f(float x,float y,float z)
{
	Vector3f* vector=(Vector3f*)(malloc(sizeof(Vector3f)));
	vector->x=x;
	vector->y=y;
	vector->z=z;
	return vector;
}
Public Vector3f* cross(Vector3f* v0,Vector3f* v1)
{
	Vector3f* vector=(Vector3f*)(malloc(sizeof(Vector3f)));
	vector->x=v0->y*v1->z-v1->y*v0->z;
	vector->y=v0->z*v1->x-v1->z*v0->x;
	vector->z=v0->x*v1->y-v1->x*v0->y;
	return vector;
}
Public Vector3f** new_Vector3f(int length)
{
	Vector3f** vectors=(Vector3f**)(malloc(length*sizeof(Vector3f*)));
	return vectors;
}
float angleToY(float x,float y)
{
	float r=sqrt(x*x+y*y);
	return r==0?0:y>=0?asin(x/r):PI-asin(x/r);
}
void rotateX(Vector3f* v,float a)
{
	float y=v->y;
	float z=v->z;
	v->y=y*cos(a)-z*sin(a);
	v->z=y*sin(a)+z*cos(a);
}
void rotateY(Vector3f* v,float a)
{
	float z=v->z;
	float x=v->x;
	v->z=z*cos(a)-x*sin(a);
	v->x=z*sin(a)+x*cos(a);
}
void rotateZ(Vector3f* v,float a)
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
	float rotatez=angleToY(a->x,a->y);
	rotateZ(a,rotatez);
	float rotatex=-angleToY(a->z,a->y);
	rotateZ(v,rotatez);
	rotateX(v,rotatex);
	rotateY(v,angle);
	rotateX(v,-rotatex);
	rotateZ(v,-rotatez);
	vector->x=v->x;
	vector->y=v->y;
	vector->z=v->z;
	free(v);
	free(a);
}
#define Vector3d struct Vector3d
Vector3d
{
	float x;
	double y;
	double z;
};
Public Vector3d* newVector3d(double x,double y,double z)
{
	Vector3d* vector3d=(Vector3d*)malloc(sizeof(Vector3d));
	vector3d->x=x;
	vector3d->y=y;
	vector3d->z=z;
	return vector3d;
}
#define AxisAngle4d struct AxisAngle4d
AxisAngle4d
{
	double x;
	double y;
	double z;
	double a;
};
Public AxisAngle4d* newAxisAngle4d(double x,double y,double z,double a)
{
	AxisAngle4d* axisAngle4d=(AxisAngle4d*)malloc(sizeof(AxisAngle4d));
	axisAngle4d->x=x;
	axisAngle4d->y=y;
	axisAngle4d->z=z;
	axisAngle4d->a=a;
	return axisAngle4d;
}
void addPlate(double r,double y,int slices,int up)
{
	double a=-2*PI/(slices-1)*up;
	glBegin(GL_TRIANGLE_FAN);
	glNormal3f(0,up,0);
	glVertex3f(0,y,0);
	for(int i=0;i<slices;i++)
	{
		double x=r*cos(a*i);
		double z=r*sin(a*i);
		glVertex3f(x,y,z);
	}
	glEnd();
}
void addCylinder(double baseRadius,double topRadius,double height,int slices)
{
	double a=2*PI/(slices-1);
	double x,y1=height/2,y0=-y1,z;
	double r1=topRadius,r0=baseRadius;
	glBegin(GL_QUAD_STRIP);
	for(int i=0;i<slices;i++)
	{
		x=r0*cos(a*i);
		z=r0*sin(a*i);
		glNormal3f(x/r0,0,z/r0);
		glVertex3f(x,y0,z);
		x=r1*cos(a*i);
		z=r1*sin(a*i);
		glVertex3f(x,y1,z);

	}
	glEnd();
	if(r0!=0)addPlate(r0,y0,slices,-1);
	if(r1!=0)addPlate(r1,y1,slices,1);
}
void addCone(double radius,double height,int slices)
{
	addCylinder(radius,0,height,slices);
}
void addBox(double length,double width,double height)
{
	double x0=-length/2,x1=-x0;
	double y0=-height/2,y1=-y0;
	double z0=-width/2,z1=-z0;
	glBegin(GL_QUADS);
	glNormal3f(1,0,0);
	glVertex3f(x1,y0,z0);
	glVertex3f(x1,y1,z0);
	glVertex3f(x1,y1,z1);
	glVertex3f(x1,y0,z1);
	glNormal3f(-1,0,0);
	glVertex3f(x0,y0,z0);
	glVertex3f(x0,y0,z1);
	glVertex3f(x0,y1,z1);
	glVertex3f(x0,y1,z0);
	glEnd();
}
float BLACK[]={0.0,0.0,0.0,1.0};
float RED[]={1.0,0.0,0.0,1.0};
float GREEN[]={0.0,1.0,0.0,1.0};
float BLUE[]={0.0,0.0,1.0,1.0};
float WHITE[]={1.0,1.0,1.0,1.0};

