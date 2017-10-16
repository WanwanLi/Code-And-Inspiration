#include "C3D.h"
Shape3D* newDodecahedron3D(float r);
Shape3D* newPolygon3D();
int main()
{
	loadOpenGLPlatform();
	BranchGroup* BranchGroup1=newBranchGroup();
	DirectionalLight* DirectionalLight1=newDirectionalLight(newColor3f(1,1,1),newVector3f(-2,-2,-2));
	addChild_BL(BranchGroup1,DirectionalLight1);
	PositionalLight* PositionalLight1=newPositionalLight(newColor3f(1,1,1),newPoint3f(100,100,100));
	addChild_BL(BranchGroup1,PositionalLight1);
	AmbientLight* AmbientLight1=newAmbientLight(newColor3f(1,1,1));
	addChild_BL(BranchGroup1,AmbientLight1);
	PointLight* PointLight1=newPointLight(newColor3f(1,1,0),newPoint3f(10,10,10),newPoint3f(-10,-10,-10));
	addChild_BL(BranchGroup1,PointLight1);
	addChild_BS(BranchGroup1,newDodecahedron3D(0.2f));
//	addChild_BS(BranchGroup1,newPolygon3D());
	SimpleUniverse* simpleUniverse=newSimpleUniverse();
	addBranchGraph(simpleUniverse,BranchGroup1);
//	setViewPlatformBehavior(OrbitBehavior);
	getViewingPlatform(simpleUniverse);
	return 0;
}
Shape3D* newPolygon3D()
{
	float r=0.3;
	int coordinatesLength=4;
	Point3f* Coordinates[]=
	{

		newPoint3f(-r,r,r),
		newPoint3f(-r,-r,r),
		newPoint3f(r,-r,r),
//		newPoint3f(r,r,r),
		newPoint3f(0,-r/2,r)
	};
	Point3f** coordinates=new_Point3f(coordinatesLength);int i;
	for(i=0;i<coordinatesLength;i++)coordinates[i]=Coordinates[i];
	int* stripCounts=new_int(1);stripCounts[0]=coordinatesLength;
	GeometryInfo* GeometryInfo1=newGeometryInfo(POLYGON_ARRAY);
	setCoordinates(GeometryInfo1,coordinates,coordinatesLength);
	setStripCounts(GeometryInfo1,stripCounts,1);
//	generateNormals(GeometryInfo1);
	TransparencyAttributes* TransparencyAttributes1=newTransparencyAttributes(BLENDED,0.5f);
	setTransparency(TransparencyAttributes1,0.5f);
	Appearance* Appearance1=newAppearance();
	Material* Material1=newMaterial();
	setDiffuseColor(Material1,newColor3f(0,1,0));
	setMaterial(Appearance1,Material1);
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	setAppearance(shape3D,Appearance1);
	return shape3D;
}


Shape3D* newDodecahedron3D(float r)
{
	float f=0.5f*(sqrt(5)+1);
	Point3f* Coordinates[]=
	{
		newPoint3f(r,r,r),
		newPoint3f(0.f,r/f,r*f),
		newPoint3f(r*f,0.f,r/f),
		newPoint3f(r/f,r*f,0.f),

		newPoint3f(-r,r,r),
		newPoint3f(0.f,-r/f,r*f),
		newPoint3f(r,-r,r),
		newPoint3f(r*f,0.f,-r/f),

		newPoint3f(r,r,-r),
		newPoint3f(-r/f,r*f,0.f),
		newPoint3f(-r*f,0.f,r/f),
		newPoint3f(-r,-r,r),

		newPoint3f(r/f,-r*f,0.f),
		newPoint3f(r,-r,-r),
		newPoint3f(0.f,r/f,-r*f),
		newPoint3f(-r,r,-r),

		newPoint3f(-r/f,-r*f,0.f),
		newPoint3f(-r*f,0.f,-r/f),
		newPoint3f(0.f,-r/f,-r*f),
		newPoint3f(-r,-r,-r)
	};
	int CoordinateIndices[]=
	{
		0,1,5,6,2,
		0,2,7,8,3,
		0,3,9,4,1,
		1,4,10,11,5,

		2,6,12,13,7,
		3,8,14,15,9,
		5,11,16,12,6,
		7,13,18,14,8,

		9,15,17,10,4,
		19,16,11,10,17,
		19,17,15,14,18,
		19,18,13,12,16
	};
	Point3f** coordinates=new_Point3f(4*5);int i;
	for(i=0;i<4*5;i++)coordinates[i]=Coordinates[i];
	int* coordinateIndices=new_int(12*5);
	for(i=0;i<12*5;i++)coordinateIndices[i]=CoordinateIndices[i];
	int* stripCounts=new_int(12);
	for(i=0;i<12;i++)stripCounts[i]=5;
	GeometryInfo* GeometryInfo1=newGeometryInfo(POLYGON_ARRAY);
	setCoordinates(GeometryInfo1,coordinates,4*5);
	setCoordinateIndices(GeometryInfo1,coordinateIndices,12*5);
	setStripCounts(GeometryInfo1,stripCounts,12);
	generateNormals(GeometryInfo1);
	TransparencyAttributes* TransparencyAttributes1=newTransparencyAttributes(BLENDED,0.5f);
	setTransparency(TransparencyAttributes1,0.5f);
	Appearance* Appearance1=newAppearance();
	Material* Material1=newMaterial();
	setDiffuseColor(Material1,newColor3f(0,1,0));
	setMaterial(Appearance1,Material1);
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	setAppearance(shape3D,Appearance1);
	return shape3D;
}

