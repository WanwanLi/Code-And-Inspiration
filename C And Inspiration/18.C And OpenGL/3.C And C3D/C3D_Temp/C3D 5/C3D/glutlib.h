#include <glut.h>
#include<math.h>
#ifndef String
#define String char*
#endif
#ifndef null
#define null NULL
#endif
#define PI 3.1415926
#define Public extern "C" _declspec(dllexport)
#define BranchGroupList GLuint
#define byte GLubyte
#define Texture GLuint
Public int* new_int(int n)
{
	int* ints=(int*)malloc(n*sizeof(int));
	return ints;
}
Public double* new_double(int n)
{
	double* doubles=(double*)malloc(n*sizeof(double));
	return doubles;
}
Public float* new_float(int n)
{
	float* floats=(float*)malloc(n*sizeof(float));
	return floats;
}
Public int** new_Int(int m,int n)
{
	int** ints=(int**)malloc(m*sizeof(int*));int i;
	for(i=0;i<m;i++)ints[i]=new_int(n);
	return ints;
}
Public float** new_Float(int m,int n)
{
	float** f=(float**)malloc(m*sizeof(float*));int i;
	for(i=0;i<m;i++)f[i]=new_float(n);
	return f;
}
Public double** new_Double(int m,int n)
{
	double** d=(double**)malloc(m*sizeof(double*));int i;
	for(i=0;i<m;i++)d[i]=new_double(n);
	return d;
}
#define Point2f struct Point2f
Point2f
{
	float x;
	float y;
};
Public Point2f* newPoint2f(float x,float y)
{
	Point2f* point=(Point2f*)(malloc(sizeof(Point2f)));
	point->x=x;
	point->y=y;
	return point;
}
Public Point2f** new_Point2f(int length)
{
	Point2f** points=(Point2f**)(malloc(length*sizeof(Point2f*)));
	for(int i=0;i<length;i++)points[i]=null;
	return points;
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
	for(int i=0;i<length;i++)points[i]=null;
	return points;
}
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
	for(int i=0;i<length;i++)colors[i]=null;
	return colors;
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
	for(int i=0;i<length;i++)vectors[i]=null;
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
	float rot_Z=angleToY(a->x,a->y);
	rotateZ(a,rot_Z);
	float rot_X=-angleToY(a->z,a->y);
	rotateZ(v,rot_Z);
	rotateX(v,rot_X);
	rotateY(v,angle);
	rotateX(v,-rot_X);
	rotateZ(v,-rot_Z);
	vector->x=v->x;
	vector->y=v->y;
	vector->z=v->z;
	free(v);
	free(a);
}
double* toAngles(double x,double y,double z)
{
	double* angleXY=new_double(2);
	double r=sqrt(x*x+y*y+z*z);
	double angleX=asin(y/r);
	double angleY=z>=0?asin(x/r):PI-asin(x/r);
	angleXY[0]=angleX;
	angleXY[1]=angleY;
	return angleXY;
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
Public byte* readImage(String imageName,int width,int height)
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
#define POINT_ARRAY 1
#define LINE_ARRAY 2
#define TRIANGLE_ARRAY 3
#define QUAD_ARRAY 4
#define POLYGON_ARRAY 5
#define GEOMETRY_STRIP_ARRAY 10
#define LINE_STRIP_ARRAY 12
#define TRIANGLE_STRIP_ARRAY 13
#define QUAD_STRIP_ARRAY 14
#define POLYGON_STRIP_ARRAY 15
#define GeometryInfo struct GeometryInfo
GeometryInfo
{
	int geometryArrayType;
	Point3f** coordinates;
	Point2f** textureCoordinates;
	Color3f** colors;
	Vector3f** normals;
	int* coordinateIndices;
	int* textureCoordinateIndices;
	int* colorIndices;
	int* normalIndices;
	int* stripCounts;
	int coordinatesLength;
	int textureCoordinatesLength;
	int colorsLength;
	int normalsLength;
	int coordinateIndicesLength;
	int textureCoordinateIndicesLength;
	int colorIndicesLength;
	int normalIndicesLength;
	int stripCountsLength;
	GeometryInfo* next;
};
Public GeometryInfo* newGeometryInfo(int geometryArrayType)
{
	GeometryInfo* geometryInfo=(GeometryInfo*)malloc(sizeof(GeometryInfo));
	geometryInfo->geometryArrayType=geometryArrayType;
	geometryInfo->coordinates=null;
	geometryInfo->textureCoordinates=null;
	geometryInfo->colors=null;
	geometryInfo->normals=null;
	geometryInfo->coordinateIndices=null;
	geometryInfo->textureCoordinateIndices=null;
	geometryInfo->colorIndices=null;
	geometryInfo->normalIndices=null;
	geometryInfo->stripCounts=null;
	geometryInfo->coordinatesLength=0;
	geometryInfo->textureCoordinatesLength=0;
	geometryInfo->colorsLength=0;
	geometryInfo->normalsLength=0;
	geometryInfo->coordinateIndicesLength=0;
	geometryInfo->textureCoordinateIndicesLength=0;
	geometryInfo->colorIndicesLength=0;
	geometryInfo->normalIndicesLength=0;
	geometryInfo->stripCountsLength=0;
	geometryInfo->next=null;
	return geometryInfo;
}
Public void setCoordinates(GeometryInfo* geometryInfo,Point3f** coordinates,int length)
{
	geometryInfo->coordinates=coordinates;
	geometryInfo->coordinatesLength=length;
}
Public void setTextureCoordinates(GeometryInfo* geometryInfo,Point2f** textureCoordinates,int length)
{
	geometryInfo->textureCoordinates=textureCoordinates;
	geometryInfo->textureCoordinatesLength=length;
}
Public void setColors(GeometryInfo* geometryInfo,Color3f** colors,int length)
{
	geometryInfo->colors=colors;
	geometryInfo->colorsLength=length;
}
Public void setNormals(GeometryInfo* geometryInfo,Vector3f** normals,int length)
{
	geometryInfo->normals=normals;
	geometryInfo->normalsLength=length;
}
Public void setCoordinateIndices(GeometryInfo* geometryInfo,int* coordinateIndices,int length)
{
	geometryInfo->coordinateIndices=coordinateIndices;
	geometryInfo->coordinateIndicesLength=length;
}
Public void setTextureCoordinateIndices(GeometryInfo* geometryInfo,int* textureCoordinateIndices,int length)
{
	geometryInfo->textureCoordinateIndices=textureCoordinateIndices;
	geometryInfo->textureCoordinateIndicesLength=length;
}
Public void setColorIndices(GeometryInfo* geometryInfo,int* colorIndices,int length)
{
	geometryInfo->colorIndices=colorIndices;
	geometryInfo->colorIndicesLength=length;
}
Public void setNormalIndices(GeometryInfo* geometryInfo,int* normalIndices,int length)
{
	geometryInfo->normalIndices=normalIndices;
	geometryInfo->normalIndicesLength=length;
}
Public void setStripCounts(GeometryInfo* geometryInfo,int* stripCounts,int length)
{
	geometryInfo->stripCounts=stripCounts;
	geometryInfo->stripCountsLength=length;
}
Vector3f* getNormal(Point3f* p0,Point3f* p1,Point3f* p2)
{
	float x0=p1->x-p0->x;
	float y0=p1->y-p0->y;
	float z0=p1->z-p0->z;
	float x1=p2->x-p1->x;
	float y1=p2->y-p1->y;
	float z1=p2->z-p1->z;
	float x=y0*z1-y1*z0;
	float y=z0*x1-z1*x0;
	float z=x0*y1-x1*y0;
	float l=(float)sqrt(x*x+y*y+z*z);
	return newVector3f(x/l,y/l,z/l);
}
void generateTriangleArrayNormals(GeometryInfo* geometryInfo)
{
	Point3f** coordinates=geometryInfo->coordinates;
	int* coordinateIndices=geometryInfo->coordinateIndices;
	int* stripCounts=geometryInfo->stripCounts;
	int coordinatesLength=geometryInfo->coordinatesLength;
	int coordinateIndicesLength=geometryInfo->coordinateIndicesLength;
	int stripCountsLength=geometryInfo->stripCountsLength,c=0;
	int normalsLength=coordinatesLength;
	Vector3f** normals=new_Vector3f(normalsLength);
	int vertexCount=coordinateIndices==null?coordinatesLength:coordinateIndicesLength;
	for(int i=0;i<vertexCount;i+=3)
	{
		int coordinateIndex0=coordinateIndices==null?i+0:coordinateIndices[i+0];
		int coordinateIndex1=coordinateIndices==null?i+1:coordinateIndices[i+1];
		int coordinateIndex2=coordinateIndices==null?i+2:coordinateIndices[i+2];
		Point3f* coordinate0=coordinates[coordinateIndex0];
		Point3f* coordinate1=coordinates[coordinateIndex1];
		Point3f* coordinate2=coordinates[coordinateIndex2];
		Vector3f* normal=getNormal(coordinate0,coordinate1,coordinate2);
		if(normals[coordinateIndex0]==null)normals[coordinateIndex0]=normal;
		if(normals[coordinateIndex1]==null)normals[coordinateIndex1]=normal;
		if(normals[coordinateIndex2]==null)normals[coordinateIndex2]=normal;
	}
	geometryInfo->normals=normals;
	geometryInfo->normalsLength=normalsLength;
	geometryInfo->normalIndices=coordinateIndices;
	geometryInfo->normalIndicesLength=coordinateIndicesLength;
}
void generateQuadArrayNormals(GeometryInfo* geometryInfo)
{
	Point3f** coordinates=geometryInfo->coordinates;
	int* coordinateIndices=geometryInfo->coordinateIndices;
	int* stripCounts=geometryInfo->stripCounts;
	int coordinatesLength=geometryInfo->coordinatesLength;
	int coordinateIndicesLength=geometryInfo->coordinateIndicesLength;
	int stripCountsLength=geometryInfo->stripCountsLength,c=0;
	int normalsLength=coordinatesLength;
	Vector3f** normals=new_Vector3f(normalsLength);
	int vertexCount=coordinateIndices==null?coordinatesLength:coordinateIndicesLength;
	for(int i=0;i<vertexCount;i+=4)
	{
		int coordinateIndex0=coordinateIndices==null?i+0:coordinateIndices[i+0];
		int coordinateIndex1=coordinateIndices==null?i+1:coordinateIndices[i+1];
		int coordinateIndex2=coordinateIndices==null?i+2:coordinateIndices[i+2];
		int coordinateIndex3=coordinateIndices==null?i+3:coordinateIndices[i+3];
		Point3f* coordinate0=coordinates[coordinateIndex0];
		Point3f* coordinate1=coordinates[coordinateIndex1];
		Point3f* coordinate2=coordinates[coordinateIndex2];
		Point3f* coordinate3=coordinates[coordinateIndex3];
		Vector3f* normal=getNormal(coordinate0,coordinate1,coordinate2);
		if(normals[coordinateIndex0]==null)normals[coordinateIndex0]=normal;
		if(normals[coordinateIndex1]==null)normals[coordinateIndex1]=normal;
		normal=getNormal(coordinate1,coordinate2,coordinate3);
		if(normals[coordinateIndex2]==null)normals[coordinateIndex2]=normal;
		if(normals[coordinateIndex3]==null)normals[coordinateIndex3]=normal;
	}
	geometryInfo->normals=normals;
	geometryInfo->normalsLength=normalsLength;
	geometryInfo->normalIndices=coordinateIndices;
	geometryInfo->normalIndicesLength=coordinateIndicesLength;
}
void generateGeometryStripArrayNormals(GeometryInfo* geometryInfo)
{
	Point3f** coordinates=geometryInfo->coordinates;
	int* coordinateIndices=geometryInfo->coordinateIndices;
	int* stripCounts=geometryInfo->stripCounts;
	int coordinatesLength=geometryInfo->coordinatesLength;
	int coordinateIndicesLength=geometryInfo->coordinateIndicesLength;
	int stripCountsLength=geometryInfo->stripCountsLength;
	int normalsLength=coordinatesLength,c=0;
	Vector3f** normals=new_Vector3f(normalsLength);
	for(int i=0;i<stripCountsLength;i++)
	{
		for(int j=0;j+3<=stripCounts[i];j++)
		{
			int coordinateIndex0=coordinateIndices==null?c+0:coordinateIndices[c+0];
			int coordinateIndex1=coordinateIndices==null?c+1:coordinateIndices[c+1];
			int coordinateIndex2=coordinateIndices==null?c+2:coordinateIndices[c+2];
			Point3f* coordinate0=coordinates[coordinateIndex0];
			Point3f* coordinate1=coordinates[coordinateIndex1];
			Point3f* coordinate2=coordinates[coordinateIndex2];
			Vector3f* normal=getNormal(coordinate0,coordinate1,coordinate2);
			if(normals[coordinateIndex0]==null)normals[coordinateIndex0]=normal;
			if(j+3==stripCounts[i])
			{
				if(normals[coordinateIndex1]==null)normals[coordinateIndex1]=normal;
				if(normals[coordinateIndex2]==null)normals[coordinateIndex2]=normal;
				c+=3;
			}
			else c++;
		}
	}
	geometryInfo->normals=normals;
	geometryInfo->normalsLength=normalsLength;
	geometryInfo->normalIndices=coordinateIndices;
	geometryInfo->normalIndicesLength=coordinateIndicesLength;
}
void glutPlate(double r,double y,int slices,int up)
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
void glutCylinder(double baseRadius,double topRadius,double height,int slices)
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
	if(r0!=0)glutPlate(r0,y0,slices,-1);
	if(r1!=0)glutPlate(r1,y1,slices,1);
}
void glutCone(double radius,double height,int slices)
{
	glutCylinder(radius,0,height,slices);
}
void glutBox(double length,double width,double height)
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
	glNormal3f(0,1,0);
	glVertex3f(x0,y1,z0);
	glVertex3f(x0,y1,z1);
	glVertex3f(x1,y1,z1);
	glVertex3f(x1,y1,z0);
	glNormal3f(0,-1,0);
	glVertex3f(x0,y0,z0);
	glVertex3f(x1,y0,z0);
	glVertex3f(x1,y0,z1);
	glVertex3f(x0,y0,z1);
	glNormal3f(0,0,1);
	glVertex3f(x0,y0,z1);
	glVertex3f(x1,y0,z1);
	glVertex3f(x1,y1,z1);
	glVertex3f(x0,y1,z1);
	glNormal3f(0,0,-1);
	glVertex3f(x0,y0,z0);
	glVertex3f(x0,y1,z0);
	glVertex3f(x1,y1,z0);
	glVertex3f(x1,y0,z0);
	glEnd();
}
float BLACK[]={0.0,0.0,0.0,1.0};
float RED[]={1.0,0.0,0.0,1.0};
float GREEN[]={0.0,1.0,0.0,1.0};
float BLUE[]={0.0,0.0,1.0,1.0};
float WHITE[]={1.0,1.0,1.0,1.0};
void glutColorCube(double length,double width,double height)
{
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT,GL_DIFFUSE);
	double x0=-length/2,x1=-x0;
	double y0=-height/2,y1=-y0;
	double z0=-width/2,z1=-z0;
	glBegin(GL_QUADS);
	glNormal3f(1,0,0);
	glColor3f(1,0,0);
	glVertex3f(x1,y0,z0);
	glVertex3f(x1,y1,z0);
	glVertex3f(x1,y1,z1);
	glVertex3f(x1,y0,z1);
	glNormal3f(-1,0,0);
	glColor3f(1,1,0);
	glVertex3f(x0,y0,z0);
	glVertex3f(x0,y0,z1);
	glVertex3f(x0,y1,z1);
	glVertex3f(x0,y1,z0);
	glNormal3f(0,1,0);
	glColor3f(0,1,0);
	glVertex3f(x0,y1,z0);
	glVertex3f(x0,y1,z1);
	glVertex3f(x1,y1,z1);
	glVertex3f(x1,y1,z0);
	glNormal3f(0,-1,0);
	glColor3f(0,1,1);
	glVertex3f(x0,y0,z0);
	glVertex3f(x1,y0,z0);
	glVertex3f(x1,y0,z1);
	glVertex3f(x0,y0,z1);
	glNormal3f(0,0,1);
	glColor3f(0,0,1);
	glVertex3f(x0,y0,z1);
	glVertex3f(x1,y0,z1);
	glVertex3f(x1,y1,z1);
	glVertex3f(x0,y1,z1);
	glNormal3f(0,0,-1);
	glColor3f(1,0,1);
	glVertex3f(x0,y0,z0);
	glVertex3f(x0,y1,z0);
	glVertex3f(x1,y1,z0);
	glVertex3f(x1,y0,z0);
	glEnd();
	glDisable(GL_COLOR_MATERIAL);
}
/*
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
*/
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
void glutGeometryArray(GeometryInfo* geometryInfo)
{
	int geometryArrayType=geometryInfo->geometryArrayType;
	Point3f** coordinates=geometryInfo->coordinates;
	Point2f** textureCoordinates=geometryInfo->textureCoordinates;
	Color3f** colors=geometryInfo->colors;
	Vector3f** normals=geometryInfo->normals;
	int* coordinateIndices=geometryInfo->coordinateIndices;
	int* textureCoordinateIndices=geometryInfo->textureCoordinateIndices;
	int* colorIndices=geometryInfo->colorIndices;
	int* normalIndices=geometryInfo->normalIndices;
	int coordinatesLength=geometryInfo->coordinatesLength;
	int textureCoordinatesLength=geometryInfo->textureCoordinatesLength;
	int colorsLength=geometryInfo->colorsLength;
	int normalsLength=geometryInfo->normalsLength;
	int coordinateIndicesLength=geometryInfo->coordinateIndicesLength;
	int textureCoordinateIndicesLength=geometryInfo->textureCoordinateIndicesLength;
	int colorIndicesLength=geometryInfo->colorIndicesLength;
	int normalIndicesLength=geometryInfo->normalIndicesLength,c=0;
	if(colors!=null)
	{
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT,GL_DIFFUSE);
	}
	int vertexCount=coordinateIndices==null?coordinatesLength:coordinateIndicesLength;
	switch(geometryArrayType)
	{
		case POINT_ARRAY:glBegin(GL_POINTS);break;
		case LINE_ARRAY:glBegin(GL_LINES);break;
		case TRIANGLE_ARRAY:glBegin(GL_TRIANGLES);break;
		case QUAD_ARRAY:glBegin(GL_QUADS);break;
//		case POLYGON_ARRAY:glBegin(GL_POLYGONS);break;
	}
	for(int i=0;i<vertexCount;i++)
	{
		if(textureCoordinates!=null)
		{
			int textureCoordinateIndex=textureCoordinateIndices==null?c:textureCoordinateIndices[c];
			Point2f* textureCoordinate=textureCoordinates[textureCoordinateIndex];
			glTexCoord2f(textureCoordinate->x,textureCoordinate->y);
		}
		if(colors!=null)
		{
			int colorIndex=colorIndices==null?i:colorIndices[i];
			Color3f* color=colors[colorIndex];
			glColor3f(color->red,color->green,color->blue);
		}
		if(normals!=null)
		{
			int normalIndex=normalIndices==null?i:normalIndices[i];
			Vector3f* normal=normals[normalIndex];
			glNormal3f(normal->x,normal->y,normal->z);
		}
		if(coordinates!=null)
		{
			int coordinateIndex=coordinateIndices==null?i:coordinateIndices[i];
			Point3f* coordinate=coordinates[coordinateIndex];
			glVertex3f(coordinate->x,coordinate->y,coordinate->z);
		}
	}
	glEnd();
	glDisable(GL_COLOR_MATERIAL);
	glDisable(GL_TEXTURE_2D);
}
void glutGeometryStripArray(GeometryInfo* geometryInfo)
{
	int geometryArrayType=geometryInfo->geometryArrayType;
	Point3f** coordinates=geometryInfo->coordinates;
	Point2f** textureCoordinates=geometryInfo->textureCoordinates;
	Color3f** colors=geometryInfo->colors;
	Vector3f** normals=geometryInfo->normals;
	int* coordinateIndices=geometryInfo->coordinateIndices;
	int* textureCoordinateIndices=geometryInfo->textureCoordinateIndices;
	int* colorIndices=geometryInfo->colorIndices;
	int* normalIndices=geometryInfo->normalIndices;
	int* stripCounts=geometryInfo->stripCounts;
	int coordinatesLength=geometryInfo->coordinatesLength;
	int textureCoordinatesLength=geometryInfo->textureCoordinatesLength;
	int colorsLength=geometryInfo->colorsLength;
	int normalsLength=geometryInfo->normalsLength;
	int coordinateIndicesLength=geometryInfo->coordinateIndicesLength;
	int textureCoordinateIndicesLength=geometryInfo->textureCoordinateIndicesLength;
	int colorIndicesLength=geometryInfo->colorIndicesLength;
	int normalIndicesLength=geometryInfo->normalIndicesLength;
	int stripCountsLength=geometryInfo->stripCountsLength,c=0;
	if(colors!=null)
	{
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT,GL_DIFFUSE);
	}
	for(int i=0;i<stripCountsLength;i++)
	{
		switch(geometryArrayType)
		{
			case LINE_STRIP_ARRAY:glBegin(GL_LINE_STRIP);break;
			case TRIANGLE_STRIP_ARRAY:glBegin(GL_TRIANGLE_STRIP);break;
			case QUAD_STRIP_ARRAY:glBegin(GL_QUAD_STRIP);break;
//			case POLYGON_STRIP_ARRAY:glBegin(GL_POLY_STRIP);break;
		}
		for(int j=0;j<stripCounts[i];j++,c++)
		{
			if(textureCoordinates!=null)
			{
				int textureCoordinateIndex=textureCoordinateIndices==null?c:textureCoordinateIndices[c];
				Point2f* textureCoordinate=textureCoordinates[textureCoordinateIndex];
				glTexCoord2f(textureCoordinate->x,textureCoordinate->y);
			}
			if(colors!=null)
			{
				int colorIndex=colorIndices==null?c:colorIndices[c];
				Color3f* color=colors[colorIndex];
				glColor3f(color->red,color->green,color->blue);
			}
			if(normals!=null)
			{
				int normalIndex=normalIndices==null?c:normalIndices[c];
				Vector3f* normal=normals[normalIndex];
				glNormal3f(normal->x,normal->y,normal->z);
			}
			if(coordinates!=null)
			{
				int coordinateIndex=coordinateIndices==null?c:coordinateIndices[c];
				Point3f* coordinate=coordinates[coordinateIndex];
				glVertex3f(coordinate->x,coordinate->y,coordinate->z);
			}
		}
		glEnd();
	}
	glDisable(GL_COLOR_MATERIAL);
	glDisable(GL_TEXTURE_2D);
}
