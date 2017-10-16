#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <glut.h>
#define Public extern "C" _declspec(dllexport)
#ifndef String
#define String char*
#endif
#ifndef null
#define null NULL
#endif
#define PI 3.1415926
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
#define COORDINATES 1
#define NORMALS 2
#define COLOR_3 4
#define TEXTURE_COORDINATE_2 8
#define PointArray struct PointArray
PointArray
{
	int vertexCount;
	int geometryMode;
	Point3f** coordinates;
	Vector3f** normals;
	Color3f** colors;
};
Public PointArray* newPointArray(int vertexCount,int geometryMode)
{
	PointArray* pointArray=(PointArray*)malloc(sizeof(PointArray));
	pointArray->vertexCount=vertexCount;
	pointArray->geometryMode=geometryMode;
	if(geometryMode&COORDINATES!=0)pointArray->coordinates=new_Point3f(vertexCount);
	else pointArray->coordinates=null;
	if(geometryMode&NORMALS!=0)pointArray->normals=new_Vector3f(vertexCount);
	else pointArray->normals=null;
	if(geometryMode&COLOR_3!=0)pointArray->colors=new_Color3f(vertexCount);
	else pointArray->colors=null;
	return pointArray;
}
Public void setCoordinate(PointArray* pointArray,int index,Point3f* coordinate)
{
	pointArray->coordinates[index]=coordinate;
}
Public void setCoordinates(PointArray* pointArray,int startIndex,Point3f** coordinates,int length)
{
	for(int i=0;i<length;i++)
	{
		pointArray->coordinates[i+startIndex]=coordinates[i];
	}
}
Public void setColor(PointArray* pointArray,int index,Color3f* color)
{
	pointArray->colors[index]=color;
}
Public void setColors(PointArray* pointArray,int startIndex,Color3f** colors,int length)
{
	for(int i=0;i<length;i++)
	{
		pointArray->colors[i+startIndex]=colors[i];
	}
}
#define LineArray struct LineArray
LineArray
{
};
#define TriangleArray struct TriangleArray
TriangleArray
{
};
#define QuadArray struct QuadArray
QuadArray
{
};
#define PointStripArray struct PointStripArray
PointStripArray
{
};
#define LineStripArray struct LineStripArray
LineStripArray
{
};
#define TriangleStripArray struct TriangleStripArray
TriangleStripArray
{
};
#define IndexedPointArray struct IndexedPointArray
IndexedPointArray
{
};
#define IndexedLineArray struct IndexedLineArray
IndexedLineArray
{
};
#define IndexedTriangleArray struct IndexedTriangleArray
IndexedTriangleArray
{
};
#define IndexedQuadArray struct IndexedQuadArray
IndexedQuadArray
{
};
#define IndexedLineStripArray struct IndexedLineStripArray
IndexedLineStripArray
{
};
#define IndexedTriangleStripArray struct IndexedTriangleStripArray
IndexedTriangleStripArray
{
};
#define IndexedTriangleFanArray struct IndexedTriangleFanArray
IndexedTriangleFanArray
{
};
#define IndexedGeometryStripArray struct IndexedGeometryStripArray
#define INDEXED_LINE_STRIP_ARRAY 1
#define INDEXED_TRIANGLE_STRIP_ARRAY 2
#define INDEXED_TRIANGLE_FAN_ARRAY 3
IndexedGeometryStripArray
{
	int indexedGeometryStripArrayType;
	IndexedLineStripArray* indexedLineStripArray;
	IndexedTriangleStripArray* indexedTriangleStripArray;
	IndexedTriangleFanArray* indexedTriangleFanArray;
};
#define IndexedGeometryArray struct IndexedGeometryArray
#define INDEXED_POINT_ARRAY 1
#define INDEXED_LINE_ARRAY 2
#define INDEXED_TRIANGLE_ARRAY 3
#define INDEXED_QUAD_ARRAY 4
#define INDEXED_GEOMETRY_STRIP_ARRAY 5
IndexedGeometryArray
{
	int indexedGeometryArrayType;
	IndexedPointArray* indexedPointArray;
	IndexedLineArray* indexedLineArray;
	IndexedTriangleArray* indexedTriangleArray;
	IndexedQuadArray* indexedQuadArray;
	IndexedGeometryStripArray* indexedGeometryStripArray;
};
#define GeometryStripArray struct GeometryStripArray
#define POINT_STRIP_ARRAY 1
#define LINE_STRIP_ARRAY 2
#define TRIANGLE_STRIP_ARRAY 3
GeometryStripArray
{
	int geometryStripArrayType;
	PointStripArray* pointStripArray;
	LineStripArray* lineStripArray;
	TriangleStripArray* triangleStripArray;
};
#define GeometryArray struct GeometryArray
#define POINT_ARRAY 1
#define LINE_ARRAY 2
#define TRIANGLE_ARRAY 3
#define QUAD_ARRAY 4
#define GEOMETRY_STRIP_ARRAY 5
#define INDEXED_GEOMETRY_ARRAY 6
GeometryArray
{
	int geometryArrayType;
	PointArray* pointArray;
	LineArray* lineArray;
	TriangleArray* triangleArray;
	QuadArray* quadArray;
	GeometryStripArray* geometryStripArray;
	IndexedGeometryArray* indexedGeometryArray;
};
Public GeometryArray* newGeometryArray(PointArray* pointArray)
{
	GeometryArray* geometryArray=(GeometryArray*)malloc(sizeof(GeometryArray));
	geometryArray->geometryArrayType=POINT_ARRAY;
	geometryArray->pointArray=pointArray;
	geometryArray->lineArray=null;
	geometryArray->triangleArray=null;
	geometryArray->quadArray=null;
	geometryArray->geometryStripArray=null;
	geometryArray->indexedGeometryArray=null;
	return geometryArray;
}
#define PolygonAttributes struct PolygonAttributes
#define POLYGON_POINT 1
#define POLYGON_LINE 2
#define POLYGON_FILL 3
#define CULL_BACK 4
#define CULL_NONE 5
PolygonAttributes
{
	int polygonMode;
	int cullFace;
};
PolygonAttributes* newPolygonAttributes()
{
	PolygonAttributes* polygonAttributes=(PolygonAttributes*)malloc(sizeof(PolygonAttributes));
	polygonAttributes->polygonMode=POLYGON_FILL;
	polygonAttributes->cullFace=CULL_BACK ;
	return polygonAttributes;
}
Public void setPolygonMode(PolygonAttributes* polygonAttributes,int polygonMode)
{
	polygonAttributes->polygonMode=polygonMode;
}
Public void setCullFace(PolygonAttributes* polygonAttributes,int cullFace)
{
	polygonAttributes->cullFace=cullFace;
}
#define PointAttributes struct PointAttributes
PointAttributes
{
	float pointSize;
	int pointAntialiasingEnable;
};
Public PointAttributes* newPointAttributes()
{
	PointAttributes* pointAttributes=(PointAttributes*)malloc(sizeof(PointAttributes));
	pointAttributes->pointSize=1.0f;
	pointAttributes->pointAntialiasingEnable=false;
	return pointAttributes;
}
Public void setPointSize(PointAttributes* pointAttributes,float pointSize)
{
	pointAttributes->pointSize=pointSize;
}
Public void setPointAntialiasingEnable(PointAttributes* pointAttributes,int enable)
{
	pointAttributes->pointAntialiasingEnable=enable;
}
#define LineAttributes struct LineAttributes
#define PATTERN_SOLID 6
#define PATTERN_DASH 7
#define PATTERN_DOT 8
#define PATTERN_DASH_DOT 9
LineAttributes
{
	float lineWidth;
	int linePattern;
	int lineAntialiasingEnable;
};
Public LineAttributes* newLineAttributes()
{
	LineAttributes* lineAttributes=(LineAttributes*)malloc(sizeof(LineAttributes));
	lineAttributes->lineWidth=1.0f;
	lineAttributes->linePattern=PATTERN_SOLID;
	lineAttributes->lineAntialiasingEnable=false;
	return lineAttributes;
}
Public void setLineWidth(LineAttributes* lineAttributes,float lineWidth)
{
	lineAttributes->lineWidth=lineWidth;
}
Public void setLinePattern(LineAttributes* lineAttributes,int linePattern)
{
	lineAttributes->linePattern=linePattern;
}
Public void setLineAntialiasingEnable(LineAttributes* lineAttributes,int enable)
{
	lineAttributes->lineAntialiasingEnable=enable;
}
#define Material struct Material
Material
{
	float ambientColor[3];
	float diffuseColor[3];
	float specularColor[3];
	float specularShininess;
};
Public Material* newMaterial()
{
	Material* material=(Material*)malloc(sizeof(Material));
	material->ambientColor[0]=0;
	material->ambientColor[1]=0;
	material->ambientColor[2]=0;
	material->diffuseColor[0]=0;
	material->diffuseColor[1]=0;
	material->diffuseColor[2]=0;
	material->specularColor[0]=0;
	material->specularColor[1]=0;
	material->specularColor[2]=0;
	material->specularShininess=50.0;
	return material;
}
Public void setDiffuseColor(Material* material,Color3f* color)
{
	material->diffuseColor[0]=color->red;
	material->diffuseColor[1]=color->green;
	material->diffuseColor[2]=color->blue;
}
#define Appearance struct Appearance
Appearance
{
	PolygonAttributes* polygonAttributes;
	PointAttributes* pointAttributes;
	LineAttributes* lineAttributes;
	Material* material;
};
Public Appearance* newAppearance()
{
	Appearance* appearance=(Appearance*)malloc(sizeof(Appearance));
	appearance->polygonAttributes=null;
	appearance->pointAttributes=null;
	appearance->lineAttributes=null;
	appearance->material=null;
	return appearance;
}
Public void setPolygonAttributes(Appearance* appearance,PolygonAttributes* polygonAttributes)
{
	appearance->polygonAttributes=polygonAttributes;
}
Public void setPointAttributes(Appearance* appearance,PointAttributes* pointAttributes)
{
	appearance->pointAttributes=pointAttributes;
}
Public void setLineAttributes(Appearance* appearance,LineAttributes* lineAttributes)
{
	appearance->lineAttributes=lineAttributes;
}
Public void setMaterial(Appearance* appearance,Material* material)
{
	appearance->material=material;
}
#define Shape3D struct Shape3D
Shape3D
{
	GeometryArray* geometryArray;
	Appearance* appearance;
	Shape3D* next;
};
Public Shape3D* newShape3D()
{
	Shape3D* shape3D=(Shape3D*)malloc(sizeof(Shape3D));
	shape3D->geometryArray=null;
	shape3D->appearance=null;
	shape3D->next=null;
	return shape3D;
}
Public void setGeometry(Shape3D* shape3D,PointArray* pointArray)
{
	shape3D->geometryArray=newGeometryArray(pointArray);
}
Public void setAppearance(Shape3D* shape3D,Appearance* appearance)
{
	shape3D->appearance=appearance;
}
#define Primitive struct Primitive
#define CONE 1
#define CYLINDER 2
#define SPHERE 3
#define BOX 4
#define COLOR_CUBE 5
Primitive
{
	int type;
	float radius;
	float length;
	float width;
	float height;
	Appearance* appearance;
	Primitive* next;
};
Public Primitive* newCone(float radius,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=CONE;
	primitive->radius=radius;
	primitive->height=height;
	primitive->appearance=appearance;
	primitive->next=null;
	return primitive;
}
Public Primitive* newCylinder(float radius,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=CYLINDER;
	primitive->radius=radius;
	primitive->height=height;
	primitive->appearance=appearance;
	return primitive;
}
Public Primitive* newSphere(float radius,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=SPHERE;
	primitive->radius=radius;
	primitive->appearance=appearance;
	return primitive;
}
Public Primitive* newBox(float length,float width,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=BOX;
	primitive->length=length;
	primitive->width=width;
	primitive->height=height;
	primitive->appearance=appearance;
	return primitive;
}
Public Primitive* newColorCube(float size,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=BOX;
	primitive->length=size;
	primitive->width=size;
	primitive->height=size;
	primitive->appearance=appearance;
	return primitive;
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
#define Transform3D struct Transform3D
Transform3D
{
	Vector3d* translation;
	Vector3d* scale;
	AxisAngle4d* rotation;
};
Public Transform3D* newTransform3D()
{
	Transform3D* transform3D=(Transform3D*)malloc(sizeof(Transform3D));
	transform3D->translation=null;
	transform3D->scale=null;
	transform3D->rotation=null;
	return transform3D;
}
Public void setTranslation(Transform3D* transform3D,Vector3d* translation)
{
	transform3D->translation=translation;
}
Public void setScale(Transform3D* transform3D,Vector3d* scale)
{
	transform3D->scale=scale;
}
Public void setRotation(Transform3D* transform3D,AxisAngle4d* rotation)
{
	transform3D->rotation=rotation;
}
Public void rotX(Transform3D* transform3D,double a)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(1,0,0,a);
}
Public void rotY(Transform3D* transform3D,double a)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(0,1,0,a);
}
Public void rotZ(Transform3D* transform3D,double a)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(0,0,1,a);
}
#define TransformGroup struct TransformGroup
TransformGroup
{
	TransformGroup* firstTransformGroup;
	TransformGroup* lastTransformGroup;
	int transformGroupLength;
	Primitive* firstPrimitive;
	Primitive* lastPrimitive;
	int primitiveLength;
	Shape3D* firstShape3D;
	Shape3D* lastShape3D;
	int shape3DLength;
	Transform3D* transform3D;
	TransformGroup* next;
};
Public TransformGroup* newTransformGroup(Transform3D* transform3D)
{
	TransformGroup* transformGroup=(TransformGroup*)malloc(sizeof(TransformGroup));
	transformGroup->firstTransformGroup=null;
	transformGroup->lastTransformGroup=null;
	transformGroup->transformGroupLength=0;
	transformGroup->firstPrimitive=null;
	transformGroup->lastPrimitive=null;
	transformGroup->primitiveLength=0;
	transformGroup->transform3D=transform3D;
	transformGroup->next=null;
	return transformGroup;
}
Public void addChild_TT(TransformGroup* transformGroup,TransformGroup* childTransformGroup)
{
	if(transformGroup->firstTransformGroup==null)
	{
		transformGroup->firstTransformGroup=childTransformGroup;
		transformGroup->lastTransformGroup=childTransformGroup;
	}
	else
	{
		transformGroup->lastTransformGroup->next=childTransformGroup;
		transformGroup->lastTransformGroup=childTransformGroup;
	}
	transformGroup->transformGroupLength++;
}
Public void addChild_TP(TransformGroup* transformGroup,Primitive* primitive)
{
	if(transformGroup->firstPrimitive==null)
	{
		transformGroup->firstPrimitive=primitive;
		transformGroup->lastPrimitive=primitive;
	}
	else
	{
		transformGroup->lastPrimitive->next=primitive;
		transformGroup->lastPrimitive=primitive;
	}
	transformGroup->primitiveLength++;
}
Public void addChild_TS(TransformGroup* transformGroup,Shape3D* shape3D)
{
	if(transformGroup->firstShape3D==null)
	{
		transformGroup->firstShape3D=shape3D;
		transformGroup->lastShape3D=shape3D;
	}
	else
	{
		transformGroup->lastShape3D->next=shape3D;
		transformGroup->lastShape3D=shape3D;
	}
	transformGroup->shape3DLength++;
}
#define BranchGroup struct BranchGroup
BranchGroup
{
	TransformGroup* firstTransformGroup;
	TransformGroup* lastTransformGroup;
	int transformGroupLength;
	Primitive* firstPrimitive;
	Primitive* lastPrimitive;
	int primitiveLength;
	Shape3D* firstShape3D;
	Shape3D* lastShape3D;
	int shape3DLength;
	BranchGroup* next;
};
Public BranchGroup* newBranchGroup()
{
	BranchGroup* branchGroup=(BranchGroup*)malloc(sizeof(BranchGroup));
	branchGroup->firstTransformGroup=null;
	branchGroup->lastTransformGroup=null;
	branchGroup->transformGroupLength=0;
	branchGroup->firstPrimitive=null;
	branchGroup->lastPrimitive=null;
	branchGroup->primitiveLength=0;
	branchGroup->firstShape3D=null;
	branchGroup->lastShape3D=null;
	branchGroup->shape3DLength=0;
	return branchGroup;
};
Public void addChild_BT(BranchGroup* branchGroup,TransformGroup* transformGroup)
{
	if(branchGroup->firstTransformGroup==null)
	{
		branchGroup->firstTransformGroup=transformGroup;
		branchGroup->lastTransformGroup=transformGroup;
	}
	else
	{
		branchGroup->lastTransformGroup->next=transformGroup;
		branchGroup->lastTransformGroup=transformGroup;
	}
	branchGroup->transformGroupLength++;
}
Public void addChild_BP(BranchGroup* branchGroup,Primitive* primitive)
{
	if(branchGroup->firstPrimitive==null)
	{
		branchGroup->firstPrimitive=primitive;
		branchGroup->lastPrimitive=primitive;
	}
	else
	{
		branchGroup->lastPrimitive->next=primitive;
		branchGroup->lastPrimitive=primitive;
	}
	branchGroup->primitiveLength++;
}
Public void addChild_BS(BranchGroup* branchGroup,Shape3D* shape3D)
{
	if(branchGroup->firstShape3D==null)
	{
		branchGroup->firstShape3D=shape3D;
		branchGroup->lastShape3D=shape3D;
	}
	else
	{
		branchGroup->lastShape3D->next=shape3D;
		branchGroup->lastShape3D=shape3D;
	}
	branchGroup->shape3DLength++;
}
#define SimpleUniverse struct SimpleUniverse
SimpleUniverse
{
	BranchGroup* firstBranchGroup;
	BranchGroup* lastBranchGroup;
	int branchGroupLength;
};
Public SimpleUniverse* newSimpleUniverse()
{
	SimpleUniverse* simpleUniverse=(SimpleUniverse*)malloc(sizeof(SimpleUniverse));
	simpleUniverse->firstBranchGroup=null;
	simpleUniverse->lastBranchGroup=null;
	simpleUniverse->branchGroupLength=0;
	return simpleUniverse;
}
Public void addBranchGraph(SimpleUniverse* simpleUniverse,BranchGroup* branchGroup)
{
	if(simpleUniverse->firstBranchGroup==null)
	{
		simpleUniverse->firstBranchGroup=branchGroup;
		simpleUniverse->lastBranchGroup=branchGroup;
	}
	else
	{
		simpleUniverse->lastBranchGroup->next=branchGroup;
		simpleUniverse->lastBranchGroup=branchGroup;
	}
	simpleUniverse->branchGroupLength++;
}
void glDisplayPrimitive(Primitive* primitive)
{
	GLUquadric* gluQuadric=gluNewQuadric();
	gluQuadricDrawStyle(gluQuadric,GLU_LINE);
	gluQuadricDrawStyle(gluQuadric,GLU_FILL);
	gluQuadricNormals(gluQuadric,GLU_FLAT);
	gluQuadricNormals(gluQuadric,GLU_SMOOTH);
	Appearance* appearance=primitive->appearance;
	if(appearance!=null)
	{
		Material* material=appearance->material;
		if(material!=null)
		{
			glMaterialfv(GL_FRONT,GL_AMBIENT,material->ambientColor);
			glMaterialfv(GL_FRONT,GL_DIFFUSE,material->diffuseColor);
			glMaterialfv(GL_FRONT,GL_SPECULAR,material->specularColor);
			glMaterialf(GL_FRONT,GL_SHININESS,material->specularShininess);
		}
	}
	switch(primitive->type)
	{
		case CONE:
		{
			double baseRadius=primitive->radius+0.0;
			double topRadius=0.0;
			double height=primitive->height+0.0;
			int slices=20,stacks=1;
			gluCylinder(gluQuadric,baseRadius,topRadius,height,slices,stacks);
			break;
		}
		case CYLINDER:
		{
			double baseRadius=primitive->radius+0.0;
			double topRadius=baseRadius;
			double height=primitive->height+0.0;
			int slices=20,stacks=1;
			gluCylinder(gluQuadric,baseRadius,topRadius,height,slices,stacks);
			break;
		}
		case SPHERE:
		{
			double radius=primitive->radius+0.0;
			int slices=20,stacks=10;
			gluSphere(gluQuadric,radius,slices,stacks);
			break;
		}
		case BOX:
		{
			break;
		}
	}
}
void glDisplay(PointArray* pointArray,Appearance* appearance)
{
	if(appearance!=null)
	{
		PointAttributes* pointAttributes=appearance->pointAttributes;
		if(pointAttributes!=null)
		{
			glPointSize(pointAttributes->pointSize);
			if(pointAttributes->pointAntialiasingEnable)
			{
				glEnable(GL_POINT_SMOOTH);
				glHint(GL_POINT_SMOOTH_HINT,GL_NICEST);
			}
		}
	}
	glBegin(GL_POINTS);
	int glVertex=pointArray->geometryMode&COORDINATES;
	int glColor=pointArray->geometryMode&COLOR_3;
	for(int i=0;i<pointArray->vertexCount;i++)
	{
		if(glVertex)
		{
			Point3f* coordinate=pointArray->coordinates[i];
			glVertex3f(coordinate->x,coordinate->y,coordinate->z);
		}
		if(glColor)
		{
			Color3f* color=pointArray->colors[i];
			glColor3f(color->red,color->green,color->blue);
		}
	}
	glEnd();
	
}
void glDisplayShape3D(Shape3D* shape3D)
{
	GeometryArray* geometryArray=shape3D->geometryArray;
	if(geometryArray==null)return;
	Appearance* appearance=shape3D->appearance;
	switch(geometryArray->geometryArrayType)
	{
		case POINT_ARRAY:
		{
			PointArray* pointArray=shape3D->geometryArray->pointArray;
			if(pointArray!=null)glDisplay(pointArray,appearance);
			break;
		}
	}
}
void glDisplayTransformGroup(TransformGroup* transformGroup)
{
	Transform3D* transform3D=transformGroup->transform3D;
	int glTranslationIsNull=1;
	glPushMatrix();
	if(transform3D!=null)
	{
		Vector3d* translation=transform3D->translation;
		Vector3d* scale=transform3D->scale;
		AxisAngle4d* rotation=transform3D->rotation;
		if(translation!=null)
		{
			glTranslatef(translation->x,translation->y,translation->z);
			glPushMatrix();
			glTranslationIsNull=0;
		}
		if(rotation!=null)glRotatef(rotation->a*180.0/PI,rotation->x,rotation->y,rotation->z);
		if(scale!=null)glScalef(scale->x,scale->y,scale->z);
	}
	TransformGroup* childTransformGroup=transformGroup->firstTransformGroup;
	int l=transformGroup->transformGroupLength;
	for(int i=0;i<l;i++,childTransformGroup=childTransformGroup->next)glDisplayTransformGroup(childTransformGroup);
	Primitive* primitive=transformGroup->firstPrimitive;
	l=transformGroup->primitiveLength;
	for(int i=0;i<l;i++,primitive=primitive->next)glDisplayPrimitive(primitive);
	Shape3D* shape3D=transformGroup->firstShape3D;
	l=transformGroup->shape3DLength;
	for(int i=0;i<l;i++,shape3D=shape3D->next)glDisplayShape3D(shape3D);
	if(!glTranslationIsNull)glPopMatrix();
	glPopMatrix();
}
void glDisplayBranchGroup(BranchGroup* branchGroup)
{
	TransformGroup* transformGroup=branchGroup->firstTransformGroup;
	int l=branchGroup->transformGroupLength;
	for(int i=0;i<l;i++,transformGroup=transformGroup->next)glDisplayTransformGroup(transformGroup);
	Primitive* primitive=branchGroup->firstPrimitive;
	l=branchGroup->primitiveLength;
	for(int i=0;i<l;i++,primitive=primitive->next)glDisplayPrimitive(primitive);
	Shape3D* shape3D=branchGroup->firstShape3D;
	l=branchGroup->shape3DLength;
	for(int i=0;i<l;i++,shape3D=shape3D->next)glDisplayShape3D(shape3D);
}
SimpleUniverse* viewingPlatform=null;
void glDisplayViewingPlatform()
{
	if(viewingPlatform==null)return;
	BranchGroup* branchGroup=viewingPlatform->firstBranchGroup;
	int l=viewingPlatform->branchGroupLength;
	for(int i=0;i<l;i++,branchGroup=branchGroup->next)glDisplayBranchGroup(branchGroup);
}

void glTest()
{
/*
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
	*/
}
float backgroundColor[]={0.0,0.0,0.0,0.0};
float lightPosition[]={2.0,2.0,2.0,1};
float lightDirection[]={0.0,0.0,2.0,0};
float lightColor[]={1.0,1.0,1.0,1.0};
float distanceR=2.0,angleU=PI/2,angleV=-PI/2,up=1;
float eyeX=0.0,eyeY=0.0,eyeZ=distanceR,centerX=0.0,centerY=0.0,centerZ=0.0;
int currentMouseX=0,currentMouseY=0,currentMouseZ=0,currentMouseButton=0;
int translateMouseX=0,translateMouseY=0,translateMouseZ=0;
int timerIsEnabled=0;
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

	glDisplayViewingPlatform();

	glutSwapBuffers();
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
Public void getViewingPlatform(SimpleUniverse* simpleUniverse)
{
	viewingPlatform=simpleUniverse;
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("");
	glutDisplayFunc(displayFunc);
	glutReshapeFunc(reshapeFunc);
	glutMouseFunc(mouseFunc);
	glutMotionFunc(motionFunc);
	glutMainLoop();
}
