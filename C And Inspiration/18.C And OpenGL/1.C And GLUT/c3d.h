#include <stdlib.h>
#include <stdio.h>
#include <OpenGL\glut.h>
#ifndef String
#define String char*
#endif
#ifndef null
#define null NULL
#endif
#define Point3f struct Point3f
Point3f
{
	float x;
	float y;
	float z;
};
Point3f* newPoint3f(float x,float y,float z)
{
	Point3f* point3f=(Point3f*)malloc(sizeof(Point3f));
	point3f->x=x;
	point3f->y=y;
	point3f->z=z;
	return point3f;
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
	Vector3f* vector3f=(Vector3f*)malloc(sizeof(Vector3f));
	vector3f->x=x;
	vector3f->y=y;
	vector3f->z=z;
	return vector3f;
}
#define Color3f struct Color3f
Color3f
{
	float r;
	float g;
	float b;
};
Color3f* newColor3f(float r,float g,float b)
{
	Color3f* color3f=(Color3f*)malloc(sizeof(Color3f));
	color3f->r=r;
	color3f->g=g;
	color3f->b=b;
	return color3f;
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
PointArray* newPointArray(int vertexCount,int geometryMode)
{
	PointArray* pointArray=(PointArray*)malloc(sizeof(PointArray));
	pointArray->vertexCount=vertexCount;
	pointArray->geometryMode=geometryMode;
	if(geometryMode&COORDINATES!=0)pointArray->coordinates=(Point3f**)malloc(vertexCount*sizeof(Point3f*));
	else pointArray->coordinates=null;
	if(geometryMode&NORMALS!=0)pointArray->normals=(Vector3f**)malloc(vertexCount*sizeof(Vector3f*));
	else pointArray->normals=null;
	if(geometryMode&COLOR_3!=0)pointArray->colors=(Color3f**)malloc(vertexCount*sizeof(Color3f*));
	else pointArray->colors=null;
	return pointArray;
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
GeometryArray* newGeometryArray(PointArray* pointArray)
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
void setPolygonMode(PolygonAttributes* polygonAttributes,int polygonMode)
{
	polygonAttributes->polygonMode=polygonMode;
}
void setCullFace(PolygonAttributes* polygonAttributes,int cullFace)
{
	polygonAttributes->cullFace=cullFace;
}
#define PointAttributes struct PointAttributes
PointAttributes
{
	float pointSize;
	int pointAntialiasingEnable;
};
PointAttributes* newPointAttributes()
{
	PointAttributes* pointAttributes=(PointAttributes*)malloc(sizeof(PointAttributes));
	pointAttributes->pointSize=1.0f;
	pointAttributes->pointAntialiasingEnable=false;
	return pointAttributes;
}
void setPointSize(PointAttributes* pointAttributes,float pointSize)
{
	pointAttributes->pointSize=pointSize;
}
void setPointAntialiasingEnable(PointAttributes* pointAttributes,int enable)
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
LineAttributes* newLineAttributes()
{
	LineAttributes* lineAttributes=(LineAttributes*)malloc(sizeof(LineAttributes));
	lineAttributes->lineWidth=1.0f;
	lineAttributes->linePattern=PATTERN_SOLID;
	lineAttributes->lineAntialiasingEnable=false;
	return lineAttributes;
}
void setLineWidth(LineAttributes* lineAttributes,float lineWidth)
{
	lineAttributes->lineWidth=lineWidth;
}
void setLinePattern(LineAttributes* lineAttributes,int linePattern)
{
	lineAttributes->linePattern=linePattern;
}
void setLineAntialiasingEnable(LineAttributes* lineAttributes,int enable)
{
	lineAttributes->lineAntialiasingEnable=enable;
}
#define ColoringAttributes struct ColoringAttributes
#define SHADE_FLAT 10
#define SHADE_GOURAUD 11
ColoringAttributes
{
	float red;
	float green;
	float blue;
	int shadeModel;
};
ColoringAttributes* newColoringAttributes()
{
	ColoringAttributes* coloringAttributes=(ColoringAttributes*)malloc(sizeof(ColoringAttributes));
	coloringAttributes->red=1.0f;
	coloringAttributes->green=1.0f;
	coloringAttributes->blue=1.0f;
	coloringAttributes->shadeModel=SHADE_GOURAUD;
	return coloringAttributes;
}
void setColor(ColoringAttributes* coloringAttributes,float red,float green,float blue)
{
	coloringAttributes->red=red;
	coloringAttributes->green=green;
	coloringAttributes->blue=blue;
}
void setShadeModel(ColoringAttributes* coloringAttributes,int shadeModel)
{
	coloringAttributes->shadeModel=shadeModel;
}
#define RenderingAttributes struct RenderingAttributes
RenderingAttributes
{
	int ignoreVertexColors;
};
RenderingAttributes* newRenderingAttributes()
{
	RenderingAttributes* renderingAttributes=(RenderingAttributes*)malloc(sizeof(RenderingAttributes));
	renderingAttributes->ignoreVertexColors=0;
	return renderingAttributes;
}
void setIgnoreVertexColors(RenderingAttributes* renderingAttributes,int ignoreVertexColors)
{
	renderingAttributes->ignoreVertexColors=ignoreVertexColors;
}
#define Material struct Material
Material
{
};
#define Appearance struct Appearance
Appearance
{
	PolygonAttributes* polygonAttributes;
	PointAttributes* pointAttributes;
	LineAttributes* lineAttributes;
	ColoringAttributes* coloringAttributes;
	RenderingAttributes* renderingAttributes;
	Material* material;
};
Appearance* newAppearance()
{
	Appearance* appearance=(Appearance*)malloc(sizeof(Appearance));
	appearance->polygonAttributes=null;
	appearance->pointAttributes=null;
	appearance->lineAttributes=null;
	appearance->coloringAttributes=null;
	appearance->renderingAttributes=null;
	appearance->material=null;
	return appearance;
}
void setPolygonAttributes(Appearance* appearance,PolygonAttributes* polygonAttributes)
{
	appearance->polygonAttributes=polygonAttributes;
}
void setPointAttributes(Appearance* appearance,PointAttributes* pointAttributes)
{
	appearance->pointAttributes=pointAttributes;
}
void setLineAttributes(Appearance* appearance,LineAttributes* lineAttributes)
{
	appearance->lineAttributes=lineAttributes;
}
void setColoringAttributes(Appearance* appearance,ColoringAttributes* coloringAttributes)
{
	appearance->coloringAttributes=coloringAttributes;
}
void setRenderingAttributes(Appearance* appearance,RenderingAttributes* renderingAttributes)
{
	appearance->renderingAttributes=renderingAttributes;
}
void setMaterial(Appearance* appearance,Material* material)
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
Shape3D* newShape3D()
{
	Shape3D* shape3D=(Shape3D*)malloc(sizeof(Shape3D));
	shape3D->geometryArray=null;
	shape3D->appearance=null;
	shape3D->next=null;
	return shape3D;
}
void setGeometryArray(Shape3D* shape3D,PointArray* pointArray)
{
	shape3D->geometryArray=newGeometryArray(pointArray);
}
void setAppearance(Shape3D* shape3D,Appearance* appearance)
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
Primitive* newCone(float radius,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=CONE;
	primitive->radius=radius;
	primitive->height=height;
	primitive->appearance=appearance;
	primitive->next=null;
	return primitive;
}
Primitive* newCylinder(float radius,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=CYLINDER;
	primitive->radius=radius;
	primitive->height=height;
	primitive->appearance=appearance;
	return primitive;
}
Primitive* newSphere(float radius,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=SPHERE;
	primitive->radius=radius;
	primitive->appearance=appearance;
	return primitive;
}
Primitive* newBox(float length,float width,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=BOX;
	primitive->length=length;
	primitive->width=width;
	primitive->height=height;
	primitive->appearance=appearance;
	return primitive;
}
Primitive* newColorCube(float size,Appearance* appearance)
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
Vector3d* newVector3d(double x,double y,double z)
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
	double w;
};
AxisAngle4d* newAxisAngle4d(double x,double y,double z,double w)
{
	AxisAngle4d* axisAngle4d=(AxisAngle4d*)malloc(sizeof(AxisAngle4d));
	axisAngle4d->x=x;
	axisAngle4d->y=y;
	axisAngle4d->z=z;
	axisAngle4d->w=w;
	return axisAngle4d;
}
#define Transform3D struct Transform3D
Transform3D
{
	Vector3d* translation;
	Vector3d* scale;
	AxisAngle4d* rotation;
};
Transform3D* newTransform3D()
{
	Transform3D* transform3D=(Transform3D*)malloc(sizeof(Transform3D));
	transform3D->translation=null;
	transform3D->scale=null;
	transform3D->rotation=null;
	return transform3D;
}
void setTranslation(Transform3D* transform3D,Vector3d* translation)
{
	transform3D->translation=translation;
}
void setScale(Transform3D* transform3D,Vector3d* scale)
{
	transform3D->scale=scale;
}
void setRotation(Transform3D* transform3D,AxisAngle4d* rotation)
{
	transform3D->rotation=rotation;
}
void rotX(Transform3D* transform3D,double w)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(1,0,0,w);
}
void rotY(Transform3D* transform3D,double w)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(0,1,0,w);
}
void rotZ(Transform3D* transform3D,double w)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(0,0,1,w);
}
#define TransformGroup struct TransformGroup
TransformGroup
{
	TransformGroup* firstTransformGroup;
	TransformGroup* lastTransformGroup;
	int transformGroupLength;
	Transform3D* transform3D;
	TransformGroup* next;
};
TransformGroup* newTransformGroup()
{
	TransformGroup* transformGroup=(TransformGroup*)malloc(sizeof(TransformGroup));
	transformGroup->firstTransformGroup=null;
	transformGroup->lastTransformGroup=null;
	transformGroup->transformGroupLength=0;
	transformGroup->transform3D=null;
	transformGroup->next=null;
	return transformGroup;
}
void addChild(TransformGroup* transformGroup,TransformGroup* childTransformGroup)
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
#define BranchGroup struct BranchGroup
BranchGroup
{
	TransformGroup* firstTransformGroup;
	TransformGroup* lastTransformGroup;
	int transformGroupLength;
	Primitive* firstPrimitive;
	Primitive* lastPrimitive;
	int primitiveLength;
	BranchGroup* next;
};
BranchGroup* newBranchGroup()
{
	BranchGroup* branchGroup=(BranchGroup*)malloc(sizeof(BranchGroup));
	branchGroup->firstTransformGroup=null;
	branchGroup->lastTransformGroup=null;
	branchGroup->transformGroupLength=0;
	branchGroup->firstPrimitive=null;
	branchGroup->lastPrimitive=null;
	branchGroup->primitiveLength=0;
	return branchGroup;
};
void addChild(BranchGroup* branchGroup,TransformGroup* transformGroup)
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
void addChild(BranchGroup* branchGroup,Primitive* primitive)
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
#define SimpleUniverse struct SimpleUniverse
SimpleUniverse
{
	BranchGroup* firstBranchGroup;
	BranchGroup* lastBranchGroup;
	int branchGroupLength;
};
SimpleUniverse* newSimpleUniverse()
{
	SimpleUniverse* simpleUniverse=(SimpleUniverse*)malloc(sizeof(SimpleUniverse));
	simpleUniverse->firstBranchGroup=null;
	simpleUniverse->lastBranchGroup=null;
	simpleUniverse->branchGroupLength=0;
	return simpleUniverse;
}
void addBranchGraph(SimpleUniverse* simpleUniverse,BranchGroup* branchGroup)
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
void display(TransformGroup* primitive)
{
}
void display(Primitive* primitive)
{
	GLUquadric* gluQuadric=gluNewQuadric();
	gluQuadricDrawStyle(gluQuadric,GLU_LINE);
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
printf(" : sphere %0.3f\n",radius);
			gluSphere(gluQuadric,radius,slices,stacks);
			break;
		}
		case BOX:
		{
			break;
		}
	}
}
void display(BranchGroup* branchGroup)
{
	TransformGroup* transformGroup=branchGroup->firstTransformGroup;
	int l=branchGroup->transformGroupLength;
	for(int i=0;i<l;i++,transformGroup=transformGroup->next)display(transformGroup);
	Primitive* primitive=branchGroup->firstPrimitive;
	l=branchGroup->primitiveLength;
	for(int i=0;i<l;i++,primitive=primitive->next)display(primitive);
}
SimpleUniverse* viewingPlatform=null;
void displayViewingPlatform()
{
	if(viewingPlatform==null)return;
	BranchGroup* branchGroup=viewingPlatform->firstBranchGroup;
	int l=viewingPlatform->branchGroupLength;
	for(int i=0;i<l;i++,branchGroup=branchGroup->next){printf("%d",i);display(branchGroup);}
}
void displayFunc()
{
	glClear(GL_COLOR_BUFFER_BIT);
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);
	glColor3f(1.0,0,0);
	GLUquadric* gluQuadric=gluNewQuadric();
	gluQuadricDrawStyle(gluQuadric,GLU_LINE);
	double radius=2.;
	int slices=20,stacks=10;
	gluSphere(gluQuadric,radius,slices,stacks);
	glutSwapBuffers();
}
void idleFunc()
{
	glutPostRedisplay();
}
void getViewingPlatform(SimpleUniverse* simpleUniverse,int argc,char** argv)
{
	viewingPlatform=simpleUniverse;
	glutInit(&argc,argv);
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB);
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("");

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-2.0,2.0,-2.0,2.0,-2.0,2.0);
	glClearColor(0.0,0.0,0.0,0.0);
//	glClearColor(1.0,1.0,1.0,0.0);
	glColor3f(1.0,0.5,0.5);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(1.0,1.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0);
	glutDisplayFunc(displayFunc);
//	glutIdleFunc(idleFunc);
/*
	glutTimerFunc(timeDelay,timerFunc,1);
	glutKeyboardFunc(keyboardFunc);
	glutSpecialFunc(specialFunc);
	glutMouseFunc(mouseFunc);
	glutMotionFunc(motionFunc);
	glutPassiveMotionFunc(passiveMotionFunc);
*/
	glutMainLoop();
}
#define Canvas3D struct Canvas3D
Canvas3D
{
	String name;
	int x,y,w,h;
	SimpleUniverse* simpleUniverse;
	Canvas3D* next;
};
Canvas3D* newCanvas3D(String name,int x,int y,int w,int h,SimpleUniverse* simpleUniverse)
{
	Canvas3D* canvas3D=(Canvas3D*)malloc(sizeof(Canvas3D));
	canvas3D->name=name;
	canvas3D->x=x;
	canvas3D->y=y;
	canvas3D->w=w;
	canvas3D->h=h;
	canvas3D->simpleUniverse=simpleUniverse;
	canvas3D->next=null;
	return canvas3D;
};
Canvas3D* firstCanvas3D=null;
Canvas3D* lastCanvas3D=null;
int canvas3DLength=0;
void addCanvas3D(String name,int x,int y,int w,int h,SimpleUniverse* simpleUniverse)
{
	Canvas3D* canvas3D=newCanvas3D(name,x,y,w,h,simpleUniverse);
	if(firstCanvas3D==null)
	{
		firstCanvas3D=canvas3D;
		lastCanvas3D=canvas3D;
	}
	else
	{
		lastCanvas3D->next=canvas3D;
		lastCanvas3D=canvas3D;
	}
	canvas3DLength++;
}