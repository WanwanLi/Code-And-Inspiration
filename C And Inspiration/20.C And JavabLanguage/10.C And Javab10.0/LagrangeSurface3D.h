double** getParameterMatrix_Lagrange(double* knots,int curvePointsLength,int ctrlPointsLength);
double* getUniformKnots(int ctrlPointsLength);
Shape3D* newLagrangeSurface3D(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,int uStep,int vStep)
{
	int r=uStep,c=vStep,v=0,m=ctrlPointsLength,n=ctrlPoints0Length,i,j;
	double *uKnots=getUniformKnots(m),*vKnots=getUniformKnots(n);
	double** U=getParameterMatrix_Lagrange(uKnots,r,m);
	double** V=getParameterMatrix_Lagrange(vKnots,c,n);
	int coordinatesLength=r*c,coordinateIndicesLength=(r-1)*(c-1)*4;
	Point3f** coordinates=new_Point3f(coordinatesLength);
	int* coordinateIndices=new_int(coordinateIndicesLength);
	for(i=0;i<r;i++)for(j=0;j<c;j++)coordinates[i*c+j]=getCoordinate(ctrlPoints,m,n,U,V,i,j);
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
double** getParameterMatrix_Lagrange(double* knots,int curvePointsLength,int ctrlPointsLength)
{
	double* u=knots;
	int l=curvePointsLength;
	int n=ctrlPointsLength,i,j,k;
	double d=1.0/(l-1);
	double** L=new_Double(l,n);
	for(k=0;k<l;k++)
	{
		double t=k*d;
		for(i=0;i<n;i++)
		{
			L[k][i]=1.0;
			for(j=0;j<n;j++)
			{
				L[k][i]*=i==j?1:(t-u[j])/(u[i]-u[j]);
			}
		}
	}
	return L;
}
double* getUniformKnots(int ctrlPointsLength)
{
	int l=ctrlPointsLength,i;
	double* knots=new_double(l);
	double du=1.0/(l-1);
	for(i=0;i<l;i++)knots[i]=i*du;
	return knots;
}
