#include <windows.h>
#ifndef String
#define String char*
#endif
#ifndef null
#define null NULL
#endif
#define PI 3.1415926
#define Color3f struct Color3f
#define Point3f struct Point3f
#define Vector3f struct Vector3f
#define PointArray struct PointArray
#define GeometryArray struct GeometryArray
#define Material struct Material
#define Appearance struct Appearance
#define Shape3D struct Shape3D
#define Primitive struct Primitive
#define Vector3d struct Vector3d
#define AxisAngle4d struct AxisAngle4d
#define Transform3D struct Transform3D
#define TransformGroup struct TransformGroup
#define BranchGroup struct BranchGroup
#define SimpleUniverse struct SimpleUniverse
#define OrbitBehavior 1
typedef Color3f* (*NewColor3f)(float red,float green,float blue);
typedef Color3f** (*New_Color3f)(int length);
typedef Point3f* (*NewPoint3f)(float x,float y,float z);
typedef Point3f** (*New_Point3f)(int length);
typedef Vector3f* (*NewVector3f)(float x,float y,float z);
typedef Vector3f* (*Cross)(Vector3f* v0,Vector3f* v1);
typedef Vector3f** (*New_Vector3f)(int length);
typedef PointArray* (*NewPointArray)(int vertexCount,int geometryMode);
typedef void (*SetCoordinate)(PointArray* pointArray,int index,Point3f* coordinate);
typedef void (*SetCoordinates)(PointArray* pointArray,int startIndex,Point3f** coordinates,int length);
typedef void (*SetColor)(PointArray* pointArray,int index,Color3f* color);
typedef void (*SetColors)(PointArray* pointArray,int startIndex,Color3f** colors,int length);
typedef GeometryArray* (*NewGeometryArray)(PointArray* pointArray);
typedef Material* (*NewMaterial)();
typedef void (*SetDiffuseColor)(Material* material,Color3f* color);
typedef Appearance* (*NewAppearance)();
typedef void (*SetMaterial)(Appearance* appearance,Material* material);
typedef Shape3D* (*NewShape3D)();
typedef void (*SetGeometry)(Shape3D* shape3D,PointArray* pointArray);
typedef void (*SetAppearance)(Shape3D* shape3D,Appearance* appearance);
typedef Primitive* (*NewCone)(float radius,float height,Appearance* appearance);
typedef Primitive* (*NewCylinder)(float radius,float height,Appearance* appearance);
typedef Primitive* (*NewSphere)(float radius,Appearance* appearance);
typedef Primitive* (*NewBox)(float length,float width,float height,Appearance* appearance);
typedef Primitive* (*NewColorCube)(float size,Appearance* appearance);
typedef Vector3d* (*NewVector3d)(double x,double y,double z);
typedef AxisAngle4d* (*NewAxisAngle4d)(double x,double y,double z,double a);
typedef Transform3D* (*NewTransform3D)();
typedef void (*SetTranslation)(Transform3D* transform3D,Vector3d* translation);
typedef void (*SetScale)(Transform3D* transform3D,Vector3d* scale);
typedef void (*SetRotation)(Transform3D* transform3D,AxisAngle4d* rotation);
typedef void (*RotX)(Transform3D* transform3D,double a);
typedef void (*RotY)(Transform3D* transform3D,double a);
typedef void (*RotZ)(Transform3D* transform3D,double a);
typedef TransformGroup* (*NewTransformGroup)(Transform3D* transform3D);
typedef void (*AddChild_TT)(TransformGroup* transformGroup,TransformGroup* childTransformGroup);
typedef void (*AddChild_TP)(TransformGroup* transformGroup,Primitive* primitive);
typedef void (*AddChild_TS)(TransformGroup* transformGroup,Shape3D* shape3D);
typedef BranchGroup* (*NewBranchGroup)();
typedef void (*AddChild_BT)(BranchGroup* branchGroup,TransformGroup* transformGroup);
typedef void (*AddChild_BP)(BranchGroup* branchGroup,Primitive* primitive);
typedef void (*AddChild_BS)(BranchGroup* branchGroup,Shape3D* shape3D);
typedef SimpleUniverse* (*NewSimpleUniverse)();
typedef void (*AddBranchGraph)(SimpleUniverse* simpleUniverse,BranchGroup* branchGroup);
typedef void (*SetViewPlatformBehavior)(int orbitBehavior);
typedef void (*GetViewingPlatform)(SimpleUniverse* simpleUniverse);
HINSTANCE hinstance;
NewColor3f newColor3f;
New_Color3f new_Color3f;
NewPoint3f newPoint3f;
New_Point3f new_Point3f;
NewVector3f newVector3f;
New_Vector3f new_Vector3f;
Cross cross;
NewPointArray newPointArray;
SetCoordinate setCoordinate;
SetColor setColor;
SetColors setColors;
NewGeometryArray newGeometryArray;
SetDiffuseColor setDiffuseColor;
NewAppearance newAppearance;
NewMaterial newMaterial;
SetMaterial setMaterial;
NewShape3D newShape3D;
SetGeometry setGeometry;
SetAppearance setAppearance;
NewCone newCone;
NewCylinder newCylinder;
NewSphere newSphere;
NewBox newBox;
NewColorCube newColorCube;
NewVector3d newVector3d;
NewAxisAngle4d newAxisAngle4d;
NewTransform3D newTransform3D;
SetTranslation setTranslation;
SetScale setScale;
SetRotation setRotation;
RotX rotX;
RotY rotY;
RotZ rotZ;
NewTransformGroup newTransformGroup;
AddChild_TT addChild_TT;
AddChild_TP addChild_TP;
AddChild_TS addChild_TS;
NewBranchGroup newBranchGroup;
AddChild_BT addChild_BT;
AddChild_BP addChild_BP;
AddChild_BS addChild_BS;
NewSimpleUniverse newSimpleUniverse;
AddBranchGraph addBranchGraph;
GetViewingPlatform getViewingPlatform;
SetViewPlatformBehavior setViewPlatformBehavior;
void loadOpenGLPlatform()
{
	hinstance=LoadLibrary("C3D.dll");	
	newColor3f=(NewColor3f)GetProcAddress(hinstance,"newColor3f");
	new_Color3f=(New_Color3f)GetProcAddress(hinstance,"new_Color3f");
	newPoint3f=(NewPoint3f)GetProcAddress(hinstance,"newPoint3f");
	new_Point3f=(New_Point3f)GetProcAddress(hinstance,"new_Point3f");
	newVector3f=(NewVector3f)GetProcAddress(hinstance,"newVector3f");
	new_Vector3f=(New_Vector3f)GetProcAddress(hinstance,"new_Vector3f");
	cross=(Cross)GetProcAddress(hinstance,"cross");
	newPointArray=(NewPointArray)GetProcAddress(hinstance,"newPointArray");
	setCoordinate=(SetCoordinate)GetProcAddress(hinstance,"setCoordinate");
	setColor=(SetColor)GetProcAddress(hinstance,"setColor");
	setColors=(SetColors)GetProcAddress(hinstance,"setColors");
	newGeometryArray=(NewGeometryArray)GetProcAddress(hinstance,"newGeometryArray");
	newMaterial=(NewMaterial)GetProcAddress(hinstance,"newMaterial");
	setDiffuseColor=(SetDiffuseColor)GetProcAddress(hinstance,"setDiffuseColor");
	newAppearance=(NewAppearance)GetProcAddress(hinstance,"newAppearance");
	setMaterial=(SetMaterial)GetProcAddress(hinstance,"setMaterial");
	newShape3D=(NewShape3D)GetProcAddress(hinstance,"newShape3D");
	setGeometry=(SetGeometry)GetProcAddress(hinstance,"setGeometry");
	setAppearance=(SetAppearance)GetProcAddress(hinstance,"setAppearance");
	newCone=(NewCone)GetProcAddress(hinstance,"newCone");
	newCylinder=(NewCylinder)GetProcAddress(hinstance,"newCylinder");
	newSphere=(NewSphere)GetProcAddress(hinstance,"newSphere");
	newBox=(NewBox)GetProcAddress(hinstance,"newBox");
	newColorCube=(NewColorCube)GetProcAddress(hinstance,"newColorCube");
	newVector3d=(NewVector3d)GetProcAddress(hinstance,"newVector3d");
	newAxisAngle4d=(NewAxisAngle4d)GetProcAddress(hinstance,"newAxisAngle4d");
	newTransform3D=(NewTransform3D)GetProcAddress(hinstance,"newTransform3D");
	setTranslation=(SetTranslation)GetProcAddress(hinstance,"setTranslation");
	setScale=(SetScale)GetProcAddress(hinstance,"setScale");
	setRotation=(SetRotation)GetProcAddress(hinstance,"setRotation");
	rotX=(RotX)GetProcAddress(hinstance,"rotX");
	rotY=(RotY)GetProcAddress(hinstance,"rotY");
	rotZ=(RotZ)GetProcAddress(hinstance,"rotZ");
	newTransformGroup=(NewTransformGroup)GetProcAddress(hinstance,"newTransformGroup");
	addChild_TT=(AddChild_TT)GetProcAddress(hinstance,"addChild_TT");
	addChild_TP=(AddChild_TP)GetProcAddress(hinstance,"addChild_TP");
	addChild_TS=(AddChild_TS)GetProcAddress(hinstance,"addChild_TS");
	newBranchGroup=(NewBranchGroup)GetProcAddress(hinstance,"newBranchGroup");
	addChild_BT=(AddChild_BT)GetProcAddress(hinstance,"addChild_BT");
	addChild_BP=(AddChild_BP)GetProcAddress(hinstance,"addChild_BP");
	addChild_BS=(AddChild_BS)GetProcAddress(hinstance,"addChild_BS");
	newSimpleUniverse=(NewSimpleUniverse)GetProcAddress(hinstance,"newSimpleUniverse");
	addBranchGraph=(AddBranchGraph)GetProcAddress(hinstance,"addBranchGraph");
	setViewPlatformBehavior=(SetViewPlatformBehavior)GetProcAddress(hinstance,"setViewPlatformBehavior");
	getViewingPlatform=(GetViewingPlatform)GetProcAddress(hinstance,"getViewingPlatform");
}
void freeC3dPlatform()
{
	FreeLibrary(hinstance);
}
