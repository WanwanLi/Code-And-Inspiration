#include "C3D.h"
Shape3D* newHyperbolicParaboloid3D(float p,float q,float x0,float z0,float x1,float z1);
int main()
{
	loadOpenGLPlatform();
	BranchGroup* BranchGroup1=newBranchGroup();
	addChild_BS(BranchGroup1,newHyperbolicParaboloid3D(0.2f,0.1f,-0.5f,-0.5f,0.5f,0.5f));
	SimpleUniverse* simpleUniverse=newSimpleUniverse();
	addBranchGraph(simpleUniverse,BranchGroup1);
//	setViewPlatformBehavior(OrbitBehavior);
	getViewingPlatform(simpleUniverse);
	return 0;
}
float f(float x,float z,float p,float q)
{
	return 0.5f*(x*x/p-z*z/q);
}
Vector3f* unitVector3f(float x,float y,float z)
{
	float l=(float)sqrt(x*x+y*y+z*z);
	return newVector3f(x/l,y/l,z/l);
}
Shape3D* newHyperbolicParaboloid3D(float p,float q,float x0,float z0,float x1,float z1)
{
	int n=100,m=100,i,j;
	Point3f** coordinates=new_Point3f(m*n);
	float dx=(x1-x0)/(n-1),dz=(z1-z0)/(m-1);
	for(i=0;i<m;i++)
	{
		float x=x0+i*dx;
		for(j=0;j<n;j++)
		{
			float z=z0+j*dz;
			coordinates[i*n+j]=newPoint3f(x,f(x,z,p,q),z);
		}
	}
	Color3f** colors=new_Color3f(m*n);
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			colors[i*n+j]=newColor3f(0,(0.0f+i)/(m-1),(n-1.0f-j)/(n-1));
		}
	}
	Vector3f** normals=new_Vector3f(m*n);
	for(i=0;i<m;i++)
	{
		float x=x0+i*dx;
		for(j=0;j<n;j++)
		{
			float z=z0+j*dz;
			float dfx=f(x+dx,z,p,q)-f(x,z,p,q);
			float dfz=f(x,z+dz,p,q)-f(x,z,p,q);
			normals[i*n+j]=unitVector3f(-dfx/dx,1,-dfz/dz);
		}
	}
	int coordinateIndicesLength=6*(m-1)*(n-1);
	int *coordinateIndices=new_int(coordinateIndicesLength);
	int v=0;
	for(i=1;i<m;i++)
	{
		for(j=0;j<n-1;j++)
		{
			coordinateIndices[v++]=i*n+j;
			coordinateIndices[v++]=(i-1)*n+j;
			coordinateIndices[v++]=i*n+j+1;

			coordinateIndices[v++]=(i-1)*n+j;
			coordinateIndices[v++]=(i-1)*n+j+1;
			coordinateIndices[v++]=i*n+j+1;
		}
	}
	GeometryInfo* GeometryInfo1=newGeometryInfo(TRIANGLE_ARRAY);
	setCoordinates(GeometryInfo1,coordinates,m*n);
	setCoordinateIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	setColors(GeometryInfo1,colors,m*n);
	setColorIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
//	setNormals(GeometryInfo1,normals,m*n);
//	setNormalIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	generateNormals(GeometryInfo1);
	Appearance* Appearance1=newAppearance();
	setMaterial(Appearance1,newMaterial());
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	setAppearance(shape3D,Appearance1);
	return shape3D;
}

