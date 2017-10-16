Point3f* getCoordinate_Nurbs_Torus(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double** U,double** V,int u,int v,int uOrder,int vOrder);
Point3f* getCoordinate_BSpline_Torus(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** U,double** V,int u,int v,int uOrder,int vOrder);
double** getParameterMatrix_Circle(double* knots,int curvePointsLength,int order,int ctrlPointsLength);
double* getStandardUniformNurbsKnots_Circle(int ctrlPointsLength,int order);
Shape3D* newNurbsTorus3D(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,int uOrder,int vOrder,int uStep,int vStep)
{
	double* uKnots=getStandardUniformNurbsKnots_Circle(ctrlPointsLength,uOrder);
	double* vKnots=getStandardUniformNurbsKnots_Circle(ctrlPoints0Length,vOrder);
	int r=uStep,c=vStep,v=0,m=ctrlPointsLength,n=ctrlPoints0Length,i,j;
	double** U=getParameterMatrix_Circle(uKnots,r,uOrder,m);
	double** V=getParameterMatrix_Circle(vKnots,c,vOrder,n);
	int coordinatesLength=r*c,coordinateIndicesLength=(r-1)*(c-1)*4;
	Point3f** coordinates=new_Point3f(coordinatesLength);
	int* coordinateIndices=new_int(coordinateIndicesLength);
	if(weights==null)for(i=0;i<r;i++)for(j=0;j<c;j++)coordinates[i*c+j]=getCoordinate_BSpline_Torus(ctrlPoints,m,n,U,V,i,j,uOrder,vOrder);
	else for(i=0;i<r;i++)for(j=0;j<c;j++)coordinates[i*c+j]=getCoordinate_Nurbs_Torus(ctrlPoints,m,n,weights,U,V,i,j,uOrder,vOrder);
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
Point3f* getCoordinate_BSpline_Torus(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** U,double** V,int u,int v,int uOrder,int vOrder)
{
	int m=ctrlPointsLength;
	int n=ctrlPoints0Length,i,j;
	double x=0,y=0,z=0;
	for(i=0;i<m+uOrder;i++)
	{
		for(j=0;j<n+vOrder;j++)
		{
			x+=ctrlPoints[(i+m)%m][(j+n)%n]->x*U[u][i]*V[v][j];
			y+=ctrlPoints[(i+m)%m][(j+n)%n]->y*U[u][i]*V[v][j];
			z+=ctrlPoints[(i+m)%m][(j+n)%n]->z*U[u][i]*V[v][j];
		}
	}
	return newPoint3f(x,y,z);
}
Point3f* getCoordinate_Nurbs_Torus(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double** U,double** V,int u,int v,int uOrder,int vOrder)
{
	int m=ctrlPointsLength;
	int n=ctrlPoints0Length,i,j;
	double x=0,y=0,z=0,w=0;
	for(i=0;i<m+uOrder;i++)
	{
		for(j=0;j<n+vOrder;j++)
		{
			x+=ctrlPoints[(i+m)%m][(j+n)%n]->x*U[u][i]*V[v][j]*weights[(i+m)%m][(j+n)%n];
			y+=ctrlPoints[(i+m)%m][(j+n)%n]->y*U[u][i]*V[v][j]*weights[(i+m)%m][(j+n)%n];
			z+=ctrlPoints[(i+m)%m][(j+n)%n]->z*U[u][i]*V[v][j]*weights[(i+m)%m][(j+n)%n];
			w+=U[u][i]*V[v][j]*weights[(i+m)%m][(j+n)%n];
		}
	}
	return newPoint3f(x/w,y/w,z/w);
}
double** getParameterMatrix_Circle(double* knots,int curvePointsLength,int order,int ctrlPointsLength)
{
	double* u=knots;
	int l=curvePointsLength,i,j,k;
	int m=order,n=ctrlPointsLength+order;
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
double* getStandardUniformNurbsKnots_Circle(int ctrlPointsLength,int order)
{
	int l=ctrlPointsLength+2*order+1,i;
	double* knots=new_double(l);
	double du=1.0/(l-1);
	for(i=0;i<l;i++)knots[i]=i*du;
	return knots;
}
