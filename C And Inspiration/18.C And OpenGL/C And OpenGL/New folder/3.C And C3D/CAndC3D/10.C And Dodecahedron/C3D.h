#include <windows.h>
#ifndef String
#define String char*
#endif
#ifndef null
#define null NULL
#endif
#define PI 3.1415926
#define Color3f struct Color3f
#define Point2f struct Point2f
#define Point3f struct Point3f
#define Vector3f struct Vector3f
#define GeometryInfo struct GeometryInfo
#define POINT_ARRAY 1
#define LINE_ARRAY 2
#define TRIANGLE_ARRAY 3
#define QUAD_ARRAY 4
#define POLYGON_ARRAY 15
#define GEOMETRY_STRIP_ARRAY 10
#define LINE_STRIP_ARRAY 12
#define TRIANGLE_STRIP_ARRAY 13
#define QUAD_STRIP_ARRAY 14
#define POLYGON_STRIP_ARRAY 15
#define GeometryArray struct GeometryArray
#define BLENDED 1
#define TransparencyAttributes struct TransparencyAttributes
#define Material struct Material
#define Texture2D struct Texture2D
#define Appearance struct Appearance
#define Shape3D struct Shape3D
#define Primitive struct Primitive
#define Cone Primitive
#define Cylinder Primitive
#define Cone Primitive
#define Sphere Primitive
#define Box Primitive
#define ColorCube Primitive
#define LIGHT_SPOT 3
#define LIGHT_AMBIENT 2
#define LIGHT_POSITION 1
#define LIGHT_DIRECTION 0
#define Light struct Light
#define PositionalLight Light
#define DirectionalLight Light
#define PointLight Light
#define AmbientLight Light
#define Vector3d struct Vector3d
#define AxisAngle4d struct AxisAngle4d
#define Transform3D struct Transform3D
#define TransformGroup struct TransformGroup
#define BranchGroup struct BranchGroup
#define SimpleUniverse struct SimpleUniverse
#define OrbitBehavior 1
typedef int* (*New_int)(int n);
typedef double* (*New_double)(int n);
typedef float* (*New_float)(int n);
typedef int** (*New_Int)(int m,int n);
typedef double** (*New_Double)(int m,int n);
typedef float** (*New_Float)(int m,int n);
typedef Color3f* (*NewColor3f)(float red,float green,float blue);
typedef Color3f** (*New_Color3f)(int length);
typedef Point2f* (*NewPoint2f)(float x,float y);
typedef Point2f** (*New_Point2f)(int length);
typedef Point3f* (*NewPoint3f)(float x,float y,float z);
typedef Point3f** (*New_Point3f)(int length);
typedef Vector3f* (*NewVector3f)(float x,float y,float z);
typedef Vector3f* (*Cross)(Vector3f* v0,Vector3f* v1);
typedef Vector3f** (*New_Vector3f)(int length);
typedef GeometryInfo* (*NewGeometryInfo)(int geometryArrayType);
typedef void (*SetCoordinates)(GeometryInfo* geometryInfo,Point3f** coordinates,int length);
typedef void (*SetTextureCoordinates)(GeometryInfo* geometryInfo,Point2f** textureCoordinates,int length);
typedef void (*SetColors)(GeometryInfo* geometryInfo,Color3f** colors,int length);
typedef void (*SetNormals)(GeometryInfo* geometryInfo,Vector3f** normals,int length);
typedef void (*SetCoordinateIndices)(GeometryInfo* geometryInfo,int* coordinateIndices,int length);
typedef void (*SetTextureCoordinateIndices)(GeometryInfo* geometryInfo,int* textureCoordinateIndices,int length);
typedef void (*SetColorIndices)(GeometryInfo* geometryInfo,int* colorIndices,int length);
typedef void (*SetNormalIndices)(GeometryInfo* geometryInfo,int* normalIndices,int length);
typedef void (*SetStripCounts)(GeometryInfo* geometryInfo,int* stripCounts,int length);
typedef GeometryArray* (*GetGeometryArray)(GeometryInfo* geometryInfo);
typedef void (*AddGeometry)(GeometryArray* geometryArray,GeometryInfo* geometryInfo);
typedef void (*GenerateNormals)(GeometryInfo* geometryInfo);
typedef TransparencyAttributes* (*NewTransparencyAttributes)(int blendMode,float transparency);
typedef void (*SetTransparency)(TransparencyAttributes* transparencyAttributes,float transparency);
typedef Material* (*NewMaterial)();
typedef void (*SetDiffuseColor)(Material* material,Color3f* color);
typedef void (*SetAmbientColor)(Material* material,Color3f* color);
typedef void (*SetEmissiveColor)(Material* material,Color3f* color);
typedef void (*SetSpecularColor)(Material* material,Color3f* color);
typedef void (*SetShininess)(Material* material,float shininess);
typedef Texture2D* (*NewTexture2D)(String imageName,int imageWidth,int imageHeight);
typedef Appearance* (*NewAppearance)();
typedef void (*SetTransparencyAttributes)(Appearance* appearance,TransparencyAttributes* transparencyAttributes);
typedef void (*SetMaterial)(Appearance* appearance,Material* material);
typedef void (*SetTexture)(Appearance* appearance,Texture2D* texture);
typedef Shape3D* (*NewShape3D)();
typedef void (*SetGeometry)(Shape3D* shape3D,GeometryArray* geometryArray);
typedef void (*SetAppearance)(Shape3D* shape3D,Appearance* appearance);
typedef Primitive* (*NewCone)(float radius,float height,Appearance* appearance);
typedef Primitive* (*NewCylinder)(float radius,float height,Appearance* appearance);
typedef Primitive* (*NewSphere)(float radius,Appearance* appearance);
typedef Primitive* (*NewBox)(float length,float width,float height,Appearance* appearance);
typedef Primitive* (*NewColorCube)(float size,Appearance* appearance);
typedef Light* (*NewPositionalLight)(Color3f* color,Point3f* position);
typedef Light* (*NewDirectionalLight)(Color3f* color,Vector3f* direction);
typedef Light* (*NewPointLight)(Color3f* color,Point3f* startPoint,Point3f* endPoint);
typedef Light* (*NewAmbientLight)(Color3f* color);
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
typedef void (*AddChild_BL)(BranchGroup* branchGroup,Light* light);
typedef SimpleUniverse* (*NewSimpleUniverse)();
typedef void (*AddBranchGraph)(SimpleUniverse* simpleUniverse,BranchGroup* branchGroup);
typedef void (*SetViewPlatformBehavior)(int orbitBehavior);
typedef void (*GetViewingPlatform)(SimpleUniverse* simpleUniverse);
HINSTANCE hinstance;
New_int new_int;
New_double new_double;
New_float new_float;
New_Int new_Int;
New_Double new_Double;
New_Float new_Float;
NewColor3f newColor3f;
New_Color3f new_Color3f;
NewPoint2f newPoint2f;
New_Point2f new_Point2f;
NewPoint3f newPoint3f;
New_Point3f new_Point3f;
NewVector3f newVector3f;
New_Vector3f new_Vector3f;
Cross cross;
NewGeometryInfo newGeometryInfo;
SetCoordinates setCoordinates;
SetTextureCoordinates setTextureCoordinates;
SetColors setColors;
SetNormals setNormals;
SetCoordinateIndices setCoordinateIndices;
SetTextureCoordinateIndices setTextureCoordinateIndices;
SetColorIndices setColorIndices;
SetNormalIndices setNormalIndices;
SetStripCounts setStripCounts;
GetGeometryArray getGeometryArray;
AddGeometry addGeometry;
GenerateNormals generateNormals;
NewAppearance newAppearance;
NewTransparencyAttributes newTransparencyAttributes;
SetTransparency setTransparency;
SetTransparencyAttributes setTransparencyAttributes;
NewMaterial newMaterial;
SetDiffuseColor setDiffuseColor;
SetAmbientColor setAmbientColor;
SetEmissiveColor setEmissiveColor;
SetSpecularColor setSpecularColor;
SetShininess setShininess;
SetMaterial setMaterial;
NewTexture2D newTexture2D;
SetTexture setTexture;
NewShape3D newShape3D;
SetGeometry setGeometry;
SetAppearance setAppearance;
NewCone newCone;
NewCylinder newCylinder;
NewSphere newSphere;
NewBox newBox;
NewColorCube newColorCube;
NewPositionalLight newPositionalLight;
NewDirectionalLight newDirectionalLight;
NewPointLight newPointLight;
NewAmbientLight newAmbientLight;
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
AddChild_BL addChild_BL;
NewSimpleUniverse newSimpleUniverse;
AddBranchGraph addBranchGraph;
GetViewingPlatform getViewingPlatform;
SetViewPlatformBehavior setViewPlatformBehavior;
void loadOpenGLPlatform()
{
	hinstance=LoadLibrary("C3D.dll");	
	new_int=(New_int)GetProcAddress(hinstance,"new_int");
	new_double=(New_double)GetProcAddress(hinstance,"new_double");
	new_float=(New_float)GetProcAddress(hinstance,"new_float");
	new_Int=(New_Int)GetProcAddress(hinstance,"new_Int");
	new_Double=(New_Double)GetProcAddress(hinstance,"new_Double");
	new_Float=(New_Float)GetProcAddress(hinstance,"new_Float");
	newColor3f=(NewColor3f)GetProcAddress(hinstance,"newColor3f");
	new_Color3f=(New_Color3f)GetProcAddress(hinstance,"new_Color3f");
	newPoint2f=(NewPoint2f)GetProcAddress(hinstance,"newPoint2f");
	new_Point2f=(New_Point2f)GetProcAddress(hinstance,"new_Point2f");
	newPoint3f=(NewPoint3f)GetProcAddress(hinstance,"newPoint3f");
	new_Point3f=(New_Point3f)GetProcAddress(hinstance,"new_Point3f");
	newVector3f=(NewVector3f)GetProcAddress(hinstance,"newVector3f");
	new_Vector3f=(New_Vector3f)GetProcAddress(hinstance,"new_Vector3f");
	cross=(Cross)GetProcAddress(hinstance,"cross");
	newGeometryInfo=(NewGeometryInfo)GetProcAddress(hinstance,"newGeometryInfo");
	setCoordinates=(SetCoordinates)GetProcAddress(hinstance,"setCoordinates");
	setTextureCoordinates=(SetTextureCoordinates)GetProcAddress(hinstance,"setTextureCoordinates");
	setColors=(SetColors)GetProcAddress(hinstance,"setColors");
	setNormals=(SetNormals)GetProcAddress(hinstance,"setNormals");
	setCoordinateIndices=(SetCoordinateIndices)GetProcAddress(hinstance,"setCoordinateIndices");
	setTextureCoordinateIndices=(SetTextureCoordinateIndices)GetProcAddress(hinstance,"setTextureCoordinateIndices");
	setColorIndices=(SetColorIndices)GetProcAddress(hinstance,"setColorIndices");
	setNormalIndices=(SetNormalIndices)GetProcAddress(hinstance,"setNormalIndices");
	setStripCounts=(SetStripCounts)GetProcAddress(hinstance,"setStripCounts");
	getGeometryArray=(GetGeometryArray)GetProcAddress(hinstance,"getGeometryArray");
	addGeometry=(AddGeometry)GetProcAddress(hinstance,"addGeometry");
	generateNormals=(GenerateNormals)GetProcAddress(hinstance,"generateNormals");
	newTransparencyAttributes=(NewTransparencyAttributes)GetProcAddress(hinstance,"newTransparencyAttributes");
	setTransparency=(SetTransparency)GetProcAddress(hinstance,"setTransparency");
	newMaterial=(NewMaterial)GetProcAddress(hinstance,"newMaterial");
	setDiffuseColor=(SetDiffuseColor)GetProcAddress(hinstance,"setDiffuseColor");
	setAmbientColor=(SetAmbientColor)GetProcAddress(hinstance,"setAmbientColor");
	setEmissiveColor=(SetEmissiveColor)GetProcAddress(hinstance,"setEmissiveColor");
	setSpecularColor=(SetSpecularColor)GetProcAddress(hinstance,"setSpecularColor");
	setShininess=(SetShininess)GetProcAddress(hinstance,"setShininess");
	newTexture2D=(NewTexture2D)GetProcAddress(hinstance,"newTexture2D");
	newAppearance=(NewAppearance)GetProcAddress(hinstance,"newAppearance");
	setTransparencyAttributes=(SetTransparencyAttributes)GetProcAddress(hinstance,"setTransparencyAttributes");
	setMaterial=(SetMaterial)GetProcAddress(hinstance,"setMaterial");
	setTexture=(SetTexture)GetProcAddress(hinstance,"setTexture");
	newShape3D=(NewShape3D)GetProcAddress(hinstance,"newShape3D");
	setGeometry=(SetGeometry)GetProcAddress(hinstance,"setGeometry");
	setAppearance=(SetAppearance)GetProcAddress(hinstance,"setAppearance");
	newCone=(NewCone)GetProcAddress(hinstance,"newCone");
	newCylinder=(NewCylinder)GetProcAddress(hinstance,"newCylinder");
	newSphere=(NewSphere)GetProcAddress(hinstance,"newSphere");
	newBox=(NewBox)GetProcAddress(hinstance,"newBox");
	newColorCube=(NewColorCube)GetProcAddress(hinstance,"newColorCube");
	newPositionalLight=(NewPositionalLight)GetProcAddress(hinstance,"newPositionalLight");
	newDirectionalLight=(NewDirectionalLight)GetProcAddress(hinstance,"newDirectionalLight");
	newPointLight=(NewPointLight)GetProcAddress(hinstance,"newPointLight");
	newAmbientLight=(NewAmbientLight)GetProcAddress(hinstance,"newAmbientLight");
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
	addChild_BL=(AddChild_BL)GetProcAddress(hinstance,"addChild_BL");
	newSimpleUniverse=(NewSimpleUniverse)GetProcAddress(hinstance,"newSimpleUniverse");
	addBranchGraph=(AddBranchGraph)GetProcAddress(hinstance,"addBranchGraph");
	setViewPlatformBehavior=(SetViewPlatformBehavior)GetProcAddress(hinstance,"setViewPlatformBehavior");
	getViewingPlatform=(GetViewingPlatform)GetProcAddress(hinstance,"getViewingPlatform");
}
void freeC3dPlatform()
{
	FreeLibrary(hinstance);
}
