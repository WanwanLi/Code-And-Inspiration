#include "C3D.h"
Shape3D* newNurbsSurface3D(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double* uKnots,double* vKnots,int uOrder,int vOrder);
Point3f* getCoordinate_Nurbs(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double** U,double** V,int u,int v);
Point3f* getCoordinate_BSpline(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** U,double** V,int u,int v);
double** getParameterMatrix(double* knots,int curvePointsLength,int order,int ctrlPointsLength);
double* getStandardUniformNurbsKnots(int ctrlPointsLength,int order);
double* getBezierUniformNurbsKnots(int ctrlPointsLength,int order);
double* getSemiUniformNurbsKnots(int ctrlPointsLength,int order);
Point3f*** new__Point3f(int m,int n);
int main()
{
	loadOpenGLPlatform();
	BranchGroup* BranchGroup1=newBranchGroup();
	DirectionalLight* DirectionalLight1=newDirectionalLight(newColor3f(0.8,0.8,0.8),newVector3f(-2,-2,-2));
	addChild_BL(BranchGroup1,DirectionalLight1);
	Point3f* CtrlPoints[]=
	{
		newPoint3f(-0.6,-0.1,-0.2),
		newPoint3f(-0.4,-0.25,-0.3),
		newPoint3f(-0.1,-0.25,-0.3),
		newPoint3f(0.3,-0.55,-0.2),
		newPoint3f(0.5,-0.42,-0.3),
		newPoint3f(0.65,-0.3,-0.2),

		newPoint3f(-0.6,0.1,0.1),
		newPoint3f(-0.4,0.25,0.1),
		newPoint3f(-0.1,0.25,0.1),
		newPoint3f(0.3,-0.25,0.1),
		newPoint3f(0.5,-0.2,0.1),
		newPoint3f(0.65,0.3,0.1),

		newPoint3f(-0.6,-0.1,0.2),
		newPoint3f(-0.4,-0.25,0.3),
		newPoint3f(-0.1,-0.25,0.3),
		newPoint3f(0.3,-0.55,0.2),
		newPoint3f(0.5,-0.42,0.3),
		newPoint3f(0.65,-0.3,0.2)
	};
	double Weights[]=
	{
		1,1,1,1,1,1,
		1,1,10,1,1,1,
		1,1,1,1,1,1
	};
/*
	//	int order=1;double[] knots={0.0,0.0,0.2,0.4,0.6,0.8,1.0,1.0};
	//	int order=2;double[] knots={0.0,0.0,0.0,0.25,0.5,0.75,1.0,1.0,1.0};
	//	int order=3;double[] knots={0.0,0.0,0.0,0.0,0.33,0.66,1.0,1.0,1.0,1.0};
	//	int order=4;double[] knots={0.0,0.0,0.0,0.0,0.0,0.5,1.0,1.0,1.0,1.0,1.0};
	//	int order=5;double[] knots={0.0,0.0,0.0,0.0,0.0,0.0,1.0,1.0,1.0,1.0,1.0,1.0};
		int vOrder=2;double[] vKnots=NurbsSurface3D.getBezierUniformNurbsKnots(ctrlPoints[0].length,vOrder);
	//	int vOrder=3;double[] vKnots=NurbsSurface3D.getStandardUniformNurbsKnots(ctrlPoints[0].length,vOrder);
	//	int uOrder=1;double[] uKnots={0.0,0.0,0.5,1.0,1.0};
		int uOrder=2;double[] uKnots={0.0,0.0,0.0,1.0,1.0,1.0};
*/
	int m=3,n=6,uOrder=2,vOrder=2,i,j;
	double* uKnots=getBezierUniformNurbsKnots(m,uOrder);
	double* vKnots=getBezierUniformNurbsKnots(n,vOrder);
	Point3f*** ctrlPoints=new__Point3f(m,n);double** weights=new_Double(m,n);
	for(i=0;i<m;i++)for(j=0;j<n;j++){ctrlPoints[i][j]=CtrlPoints[i*n+j];weights[i][j]=Weights[i*n+j];}
	Shape3D* NurbsSurface3D=newNurbsSurface3D(ctrlPoints,m,n,weights,uKnots,vKnots,uOrder,vOrder);
	Appearance* Appearance1=newAppearance();
	Material* Material1=newMaterial();
	setDiffuseColor(Material1,newColor3f(0,1,0));
	setMaterial(Appearance1,Material1);
	setAppearance(NurbsSurface3D,Appearance1);
	addChild_BS(BranchGroup1,NurbsSurface3D);
	SimpleUniverse* simpleUniverse=newSimpleUniverse();
	addBranchGraph(simpleUniverse,BranchGroup1);
//	setViewPlatformBehavior(OrbitBehavior);
	getViewingPlatform(simpleUniverse);
	return 0;
}
Shape3D* newNurbsSurface3D(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double* uKnots,double* vKnots,int uOrder,int vOrder)
{
	int r=100,c=100,v=0,m=ctrlPointsLength,n=ctrlPoints0Length,i,j;
	double** U=getParameterMatrix(uKnots,r,uOrder,m);
	double** V=getParameterMatrix(vKnots,c,vOrder,n);
	int coordinatesLength=r*c,coordinateIndicesLength=(r-1)*(c-1)*4;
	Point3f** coordinates=new_Point3f(coordinatesLength);
	int* coordinateIndices=new_int(coordinateIndicesLength);
	if(weights==null)for(i=0;i<r;i++)for(j=0;j<c;j++)coordinates[i*c+j]=getCoordinate_BSpline(ctrlPoints,m,n,U,V,i,j);
	else for(i=0;i<r;i++)for(j=0;j<c;j++)coordinates[i*c+j]=getCoordinate_Nurbs(ctrlPoints,m,n,weights,U,V,i,j);
	for(i=0;i<r-1;i++)
	{
		for(j=0;j<c-1;j++)
		{
			coordinateIndices[v++]=(i+0)*c+(j+0);
			coordinateIndices[v++]=(i+1)*c+(j+0);
			coordinateIndices[v++]=(i+1)*c+(j+1);
			coordinateIndices[v++]=(i+0)*c+(j+1);
		}
	}
	GeometryInfo* GeometryInfo1=newGeometryInfo(QUAD_ARRAY);
	setCoordinates(GeometryInfo1,coordinates,coordinatesLength);
	setCoordinateIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	generateNormals(GeometryInfo1);
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	return shape3D;
}
Point3f* getCoordinate_BSpline(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** U,double** V,int u,int v)
{
	int m=ctrlPointsLength;
	int n=ctrlPoints0Length,i,j;
	double x=0,y=0,z=0;
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			x+=ctrlPoints[i][j]->x*U[u][i]*V[v][j];
			y+=ctrlPoints[i][j]->y*U[u][i]*V[v][j];
			z+=ctrlPoints[i][j]->z*U[u][i]*V[v][j];
		}
	}
	return newPoint3f(x,y,z);
}
Point3f* getCoordinate_Nurbs(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double** U,double** V,int u,int v)
{
	int m=ctrlPointsLength;
	int n=ctrlPoints0Length,i,j;
	double x=0,y=0,z=0,w=0;
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			x+=ctrlPoints[i][j]->x*U[u][i]*V[v][j]*weights[i][j];
			y+=ctrlPoints[i][j]->y*U[u][i]*V[v][j]*weights[i][j];
			z+=ctrlPoints[i][j]->z*U[u][i]*V[v][j]*weights[i][j];
			w+=U[u][i]*V[v][j]*weights[i][j];
		}
	}
	return newPoint3f(x/w,y/w,z/w);
}
double** getParameterMatrix(double* knots,int curvePointsLength,int order,int ctrlPointsLength)
{
	double* u=knots;
	int l=curvePointsLength,i,j,k;
	int m=order,n=ctrlPointsLength;
	double d=(u[n]-u[m])/(l-1);
	double** b=new_Double(l,n);
	double** B=new_Double(m+1,n+1);
	for(k=0;k<l;k++)
	{
		double t=u[m]+k*d;
		for(j=0;j<=n;j++)B[0][j]=isBetween(t,u[j],u[j+1])?1:0;
		for(i=1;i<=m;i++)
		{
			for(j=0;j<n;j++)
			{
				double du0=u[j+0+i]-u[j+0];
				double du1=u[j+1+i]-u[j+1];
				double U0=du0==0?0:(t-u[j])/du0;
				double U1=du1==0?0:(u[j+1+i]-t)/du1;
				B[i][j]=B[i-1][j]*U0+B[i-1][j+1]*U1;
			}
		}
		for(j=0;j<n;j++)b[k][j]=B[m][j];
	}
	return b;	
}
double* getStandardUniformNurbsKnots(int ctrlPointsLength,int order)
{
	int l=ctrlPointsLength+order+1,i;
	double* knots=new_double(l);
	double du=1.0/(l-1);
	for(i=0;i<l;i++)knots[i]=i*du;
	return knots;
}
double* getBezierUniformNurbsKnots(int ctrlPointsLength,int order)
{
	int l=ctrlPointsLength+order+1,i;
	double* knots=new_double(l);
	int c=0,k=l-2*order;
	double du=1.0/(k-1);
	for(i=0;i<order;i++)knots[c++]=0;
	for(i=0;i<k;i++)knots[c++]=i*du;
	for(i=0;i<order;i++)knots[c++]=1;
	return knots;
}
double* getSemiUniformNurbsKnots(int ctrlPointsLength,int order)
{
	int l=ctrlPointsLength+order+1,i;
	double* knots=new_double(l);
	int c=0,k=l-2*(order+1);
	for(i=0;i<=order;i++)knots[c++]=0;
	for(i=0;i<k;i++)knots[c++]=0.5;
	for(i=0;i<=order;i++)knots[c++]=1;
	return knots;
}
int isBetween(double x,double x0,double x1)
{
	return x==0?x0==0:x0<x&&x<=x1;
}
Point3f*** new__Point3f(int m,int n)
{
	Point3f*** p=(Point3f***)malloc(m*sizeof(Point3f**));int i;
	for(i=0;i<m;i++)p[i]=new_Point3f(n);
	return p;
}
