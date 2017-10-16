#include "C3D.h"
#include "IsoSurface3D.h"
Shape3D* newHyperbolicParaboloid3D(float p,float q,float x0,float z0,float x1,float z1);
Shape3D* newEquation3D(double value,double x0,double x1,double y0,double y1,double z0,double z1);
int main()
{
	loadOpenGLPlatform();
	BranchGroup* BranchGroup1=newBranchGroup();
	DirectionalLight* DirectionalLight1=newDirectionalLight(newColor3f(0.5,0.5,0.5),newVector3f(0,0,-1));
	addChild_BL(BranchGroup1,DirectionalLight1);
//	addChild_BS(BranchGroup1,newHyperbolicParaboloid3D(0.2f,0.1f,-0.5f,-0.5f,0.5f,0.5f));
	double r=2.2,v=1.0;
	addChild_BS(BranchGroup1,newEquation3D(v,-r,r,-r,r,-r,r));
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
double f1(double x,double y,double z)
{
	double a=0.2,b=0.5,c=0.4;
	return x*x/(a*a)-y*y/(b*b)-z*z/(c*c);
}
double f2(double x,double y,double z)
{
	double a=1.0,b=0.5,c=0.4;
	return x*x/(a*a)-y*y/(b*b)+z*z/(c*c);
}
double f3(double x,double y,double z)
{
	double a=0.3,b=0.5,c=0.4;
	return x*x/(a*a)-y*y/(b*b)-z*z/(c*c);
}
double f4(double x,double y,double z)
{
	double a=0.3,b=0.5;
	return x*x/(a*a)+y*y/(b*b)-z;
}
double f5(double x,double y,double z)
{
	double a=0.3,b=0.5;
	return x*x/(a*a)-y*y/(b*b)-z;
}
Shape3D* newEquation3D(double value,double x0,double x1,double y0,double y1,double z0,double z1)
{
	int i,j,k,l=100,m=l,n=l;
	double dx=(x1-x0)/(n-1);
	double dy=(y1-y0)/(l-1);
	double dz=(z1-z0)/(m-1);
	Value3D* value3D=newValue3D(x0,dx,y0,dy,z0,dz,l,m,n);
	for(k=0;k<l;k++)
	{
		double y=y0+k*dy;
		for(i=0;i<m;i++)
		{
			double z=z0+i*dz;
			for(j=0;j<n;j++)
			{
				double x=x0+j*dx;
				value3D->values[k*m*n+i*n+j]=f1(x,y,z);
			}
		}
	}
	Shape3D* isoSurface3D=newIsoSurface3D(value3D,value);
	Material* Material1=newMaterial();
	setDiffuseColor(Material1,newColor3f(0,1,0));
	Appearance* Appearance1=newAppearance();
	setMaterial(Appearance1,Material1);
	setAppearance(isoSurface3D,Appearance1);
	return isoSurface3D;
}
