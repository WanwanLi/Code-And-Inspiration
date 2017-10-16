Point3f* getCoordinate_Nurbs(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double** U,double** V,int u,int v);
Point3f* getCoordinate_BSpline(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** U,double** V,int u,int v);
double** getParameterMatrix(double* knots,int curvePointsLength,int order,int ctrlPointsLength);
double* getStandardUniformNurbsKnots(int ctrlPointsLength,int order);
double* getBezierUniformNurbsKnots(int ctrlPointsLength,int order);
double* getSemiUniformNurbsKnots(int ctrlPointsLength,int order);
Shape3D* newNurbsSurface3D(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double* uKnots,double* vKnots,int uOrder,int vOrder,int uStep,int vStep)
{
	int r=uStep,c=vStep,v=0,m=ctrlPointsLength,n=ctrlPoints0Length,i,j;
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
/*
class Mesh3D  extends Shape3D
{
	Mesh3D(Point3f** meshPoints)
	{
		int m=meshPoints->length,n=meshPoints[0]->length,v=0;
		Point3f* coordinates=new Point3f[m*n];
		for(i=0;i<m;i++)for(j=0;j<n;j++)coordinates[i*n+j]=meshPoints[i][j];
		int* coordinateIndices=new int[(m-1)*(n-1)*4+(m-1)*2+(n-1)*2];
		for(i=0;i<m-1;i++)
		{
			for(j=0;j<n-1;j++)
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+1)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+1);
			}
		}
		for(i=0;i<m-1;i++)
		{
			int j=n-1;
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+1)*n+(j+0);
			}
		}
		{
			int i=m-1;
			for(j=0;j<n-1;j++)
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+1);
			}
		}
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(coordinates->length,IndexedLineArray->COORDINATES,coordinateIndices->length);
		IndexedLineArray1->setCoordinates(0,coordinates);
		IndexedLineArray1->setCoordinateIndices(0,coordinateIndices);
		setGeometry(IndexedLineArray1);
	}
}
*/
