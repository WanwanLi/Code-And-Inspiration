#include <math.h>
#define Vector struct Vector
Vector
{
	double x;
	double y;
	double z;
};
Vector* newVector(double x,double y,double z)
{
	Vector* vector=(Vector*)malloc(sizeof(Vector));
	vector->x=x;
	vector->y=y;
	vector->z=z;
	return vector;
}
double Abs(double x)
{
	return (x<0?-x:x);
}
Vector* cross(Vector* v0,Vector* v1)
{
	double x=v0->y*v1->z-v1->y*v0->z;
	double y=v0->z*v1->x-v1->z*v0->x;
	double z=v0->x*v1->y-v1->x*v0->y;
	return newVector(x,y,z);
}
Vector* oppositeVector(Vector* v)
{
	return newVector(-v->x,-v->y,-v->z);
}
void transform(Vector* vector,double rotX,double rotY,double rotZ)
{
	double X,Y,Z;
	double x=vector->x;
	double y=vector->y;
	double z=vector->z;
	Y=y*cos(rotX)-z*sin(rotX);
	Z=y*sin(rotX)+z*cos(rotX);
	y=Y;z=Z;
	Z=z*cos(rotY)-x*sin(rotY);
	X=z*sin(rotY)+x*cos(rotY);
	z=Z;x=X;
	X=x*cos(rotZ)-y*sin(rotZ);
	Y=x*sin(rotZ)+y*cos(rotZ);
	vector->x=X;
	vector->y=Y;
	vector->z=Z;
}
Vector* reflectedVector(Vector* incidenceVector,Vector* normalVector)
{
	Vector* incidence=newVector(incidenceVector->x,incidenceVector->y,incidenceVector->z);
	Vector* normal=newVector(normalVector->x,normalVector->y,normalVector->z);
	double rotY=atan2(normal->z,normal->x);
	transform(incidence,0,rotY,0);
	transform(normal,0,rotY,0);
	double rotZ=atan2(normal->x,normal->y);
	transform(incidence,0,0,rotZ);
	transform(normal,0,0,rotZ);
	incidence->x=-incidence->x;
	incidence->z=-incidence->z;
	transform(incidence,0,0,-rotZ);
	transform(incidence,0,-rotY,0);
	incidence=oppositeVector(incidence);
	return incidence;
}
double Vector_cos(Vector* v0,Vector* v1)
{
	return (v0->x*v1->x+v0->y*v1->y+v0->z*v1->z)/(sqrt(v0->x*v0->x+v0->y*v0->y+v0->z*v0->z)*sqrt(v1->x*v1->x+v1->y*v1->y+v1->z*v1->z));
}


#define Point3D struct Point3D

Point3D
{
	double x;
	double y;
	double z;
};
Point3D* newPoint3D(double x,double y,double z)
{
	Point3D* point3D=(Point3D*)malloc(sizeof(Point3D));
	point3D->x=x;
	point3D->y=y;
	point3D->z=z;
	return point3D;
}
void translate(Point3D* point,double dX,double dY,double dZ)
{
	point->x+=dX;
	point->y+=dY;
	point->z+=dZ;
}
void rotate(Point3D* point,double rotX,double rotY,double rotZ)
{
	double X,Y,Z;
	double x=point->x;
	double y=point->y;
	double z=point->z;	
	Y=y*cos(rotX)-z*sin(rotX);
	Z=y*sin(rotX)+z*cos(rotX);
	y=Y;z=Z;
	Z=z*cos(rotY)-x*sin(rotY);
	X=z*sin(rotY)+x*cos(rotY);
	z=Z;x=X;
	X=x*cos(rotZ)-y*sin(rotZ);
	Y=x*sin(rotZ)+y*cos(rotZ);
	point->x=X;
	point->y=Y;
	point->z=Z;
}
#define Color struct Color
Color
{
	int red;
	int green;
	int blue;
};
Color* newColor(int red,int green,int blue)
{
	Color* color=(Color*)malloc(sizeof(Color));
	color->red=red;
	color->green=green;
	color->blue=blue;
	return color;
}

#define DirectionalLight struct DirectionalLight
DirectionalLight
{
	Vector* direction;
	Color* color;
};
DirectionalLight* newDirectionalLight(Color* color,Vector* direction)
{
	DirectionalLight* directionalLight=(DirectionalLight*)malloc(sizeof(DirectionalLight));
	directionalLight->direction=direction;
	directionalLight->color=color;
	return directionalLight;
}


