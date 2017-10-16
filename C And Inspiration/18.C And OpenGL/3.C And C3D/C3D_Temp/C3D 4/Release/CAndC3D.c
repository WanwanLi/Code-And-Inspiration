#include "C3D.h"
Shape3D* newHyperbolicParaboloid3D(float p,float q,float x0,float z0,float x1,float z1);
int main()
{
	loadOpenGLPlatform();
	BranchGroup* BranchGroup1=newBranchGroup();
	Transform3D* transform3D=newTransform3D();
	rotX(transform3D,5*PI/4);
	TransformGroup* TransformGroup1=newTransformGroup(transform3D);
	addChild_TS(TransformGroup1,newHyperbolicParaboloid3D(0.2f,0.1f,-0.5f,-0.5f,0.5f,0.5f));
	addChild_BT(BranchGroup1,TransformGroup1);
	SimpleUniverse* simpleUniverse=newSimpleUniverse();
	addBranchGraph(simpleUniverse,BranchGroup1);
//	setViewPlatformBehavior(OrbitBehavior);
	getViewingPlatform(simpleUniverse);
	return 0;
}
double* X(double x0,double x1,int length)
{
	double* x=new_double(length);
	double dx=(x1-x0)/(length-1);int i;
	for(i=0;i<length;i++)x[i]=x0+dx*i;
	return x;
}
double* Sin(double A,double a,double b,double* x,int length)
{
	double* y=new_double(length);int i;
	for(i=0;i<length;i++)y[i]=A*sin(a*x[i]+b);
	return y;
}
double** Ytx(double c,double* Yx0,int m,double* Yt0,int n)
{
	int i,j;
	double** Y=new_Double(m,n);
	for(j=0;j<n;j++)Y[0][j]=Yt0[j];
	for(j=0;j<n;j++)Y[1][j]=Yt0[j];
	for(i=0;i<m;i++)Y[i][0]=Yx0[i];
	for(i=1;i<m-1;i++)
	{
		for(j=1;j<n-1;j++)
		{
			Y[i+1][j]=2*Y[i][j]-Y[i-1][j]+c*(Y[i][j+1]-2*Y[i][j]+Y[i][j-1]);
		}
		Y[i+1][j]=Y[i][j-1];
	}
	return Y;
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
		double c=0.2,t0=z0,t1=z1;
		double* Yx0=Sin(0.01,10*PI,0,X(t0,t1,n+1),n+1);
		double* Yt0=Sin(0.00,5*PI,0,X(x0,x1,m+1),m+1);
		double** Y=Ytx(c,Yx0,m+1,Yt0,n+1);
		for(i=0;i<m;i++)
		{
			float z=z0+i*dz;
			for(j=0;j<n;j++)
			{
				float x=x0+j*dx;
				coordinates[i*n+j]=newPoint3f(x,Y[i][j],z);
			}
		}
		Color3f** colors=new_Color3f(m*n);
		for(i=0;i<m;i++)
		{
			for(j=0;j<n;j++)
			{
				//colors[i*n+j]=newColor3f(0,(0.0f+i)/(m-1),(n-1.0f-j)/(n-1));
				colors[i*n+j]=newColor3f(1,1,1);
			}
		}
		Vector3f** normals=new_Vector3f(m*n);
		for(i=0;i<m;i++)
		{
			float z=z0+i*dz;
			for(j=0;j<n;j++)
			{
				float x=x0+j*dx;
				float dYx=Y[i][j+1]-Y[i][j];
				float dYz=Y[i+1][j]-Y[i][j];
				normals[i*n+j]=unitVector3f(dYx/dx,-1,dYz/dz);
			}
		}
		int* coordinateIndices=new_int(2*(m-1)*n);
		int v=0;
		for(i=1;i<m;i++)
		{
			for(j=0;j<n;j++)
			{
				coordinateIndices[v++]=i*n+j;
				coordinateIndices[v++]=(i-1)*n+j;

			}
		}
		int* stripCounts=new_int(m-1);
		for(i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo* GeometryInfo1=newGeometryInfo(TRIANGLE_STRIP_ARRAY);
		setCoordinates(GeometryInfo1,coordinates,m*n);
		setCoordinateIndices(GeometryInfo1,coordinateIndices,2*(m-1)*n);
		setColors(GeometryInfo1,colors,m*n);
		setColorIndices(GeometryInfo1,coordinateIndices,2*(m-1)*n);
		setStripCounts(GeometryInfo1,stripCounts,m-1);
//		setNormals(GeometryInfo1,normals,m*n);
//		setNormalIndices(GeometryInfo1,coordinateIndices,2*(m-1)*n);
		generateNormals(GeometryInfo1);
		Appearance* Appearance1=newAppearance();
		setMaterial(Appearance1,newMaterial());
		Shape3D* shape3D=newShape3D();
		setGeometry(shape3D,getGeometryArray(GeometryInfo1));
		setAppearance(shape3D,Appearance1);
		return shape3D;
	}

