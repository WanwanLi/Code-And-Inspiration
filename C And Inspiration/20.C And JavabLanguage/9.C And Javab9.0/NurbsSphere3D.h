Point3f* getCoordinate_Nurbs_Circle(Point3f** ctrlPoints,int ctrlPointsLength,double* weights,double** B,int k,int order);
Point3f* getCoordinate_BSpline_Circle(Point3f** ctrlPoints,int ctrlPointsLength,double** B,int k,int order);
Point3f* getCoordinate_BSpline_Curve(Point3f** ctrlPoints,int ctrlPointsLength,double** B,int k);
Point3f** getCoordinates_Bezier(int curveLength,Point3f** ctrlPoints,int ctrlPointsLength);
Point3f** getCoordinates_Sphere(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,double** U,double** V,int u,int v,int uOrder,int vOrder);
Shape3D* newNurbsSphere3D(Point3f** CtrlPoints,double* Weights,int* orders,int* ctrlCounts,int ctrlCountsLength,int uStep,int vStep)
{
	int r=uStep,c=vStep,v=0,i,j,k=ctrlCountsLength;
	int coordinatesLength=r*c,coordinateIndicesLength=(r-1)*(c-1)*4;
	Point3f** coordinates=new_Point3f(coordinatesLength);
	Point3f*** curveCoordinates=new__Point3f(c,k);
	Point3f*** newCoordinates=new__Point3f(c,r);
	Point3f*** ctrlPoints=new__Point3f(k,0);
	double** weights=new_Double(k,0);
	for(i=0;i<k;i++)
	{
		int n=ctrlCounts[i];
		ctrlPoints[i]=new_Point3f(n);
		if(Weights!=null)weights[i]=new_double(n);
		for(j=0;j<n;j++)
		{
			if(Weights!=null)weights[i][j]=Weights[v];
			ctrlPoints[i][j]=CtrlPoints[v++];
		}
	}
	for(i=0;i<k;i++)
	{
		int m=orders[i],n=ctrlCounts[i];
		if(n==1)for(j=0;j<c;j++)curveCoordinates[j][i]=ctrlPoints[i][0];
		else
		{
			double* knots=getStandardUniformNurbsKnots_Circle(n,m);
			double** B=getParameterMatrix_Circle(knots,c,m,n);
			if(Weights==null)for(j=0;j<c;j++)curveCoordinates[j][i]=getCoordinate_BSpline_Circle(ctrlPoints[i],n,B,j,m);
			else for(j=0;j<c;j++)curveCoordinates[j][i]=getCoordinate_Nurbs_Circle(ctrlPoints[i],n,weights[i],B,j,m);
		}
	}
	for(j=0;j<c;j++)newCoordinates[j]=getCoordinates_Bezier(r,curveCoordinates[j],k);
	for(i=0;i<r;i++)for(j=0;j<c;j++)coordinates[i*c+j]=newCoordinates[j][i];
	int* coordinateIndices=new_int(coordinateIndicesLength);v=0;
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
Point3f* getCoordinate_Nurbs_Circle(Point3f** ctrlPoints,int ctrlPointsLength,double* weights,double** B,int k,int order)
{
	int n=ctrlPointsLength,i;
	double x=0,y=0,z=0,w=0;
	for(i=0;i<n+order;i++)
	{
		x+=ctrlPoints[(i+n)%n]->x*B[k][i]*weights[(i+n)%n];
		y+=ctrlPoints[(i+n)%n]->y*B[k][i]*weights[(i+n)%n];
		z+=ctrlPoints[(i+n)%n]->z*B[k][i]*weights[(i+n)%n];
		w+=B[k][i]*weights[(i+n)%n];
	}
	return newPoint3f(x/w,y/w,z/w);
}
Point3f* getCoordinate_BSpline_Curve(Point3f** ctrlPoints,int ctrlPointsLength,double** B,int k)
{
	int n=ctrlPointsLength,i;
	double x=0,y=0,z=0;
	for(i=0;i<n;i++)
	{
		x+=ctrlPoints[i]->x*B[k][i];
		y+=ctrlPoints[i]->y*B[k][i];
		z+=ctrlPoints[i]->z*B[k][i];
	}
	return newPoint3f(x,y,z);
}
Point3f* getCoordinate_BSpline_Circle(Point3f** ctrlPoints,int ctrlPointsLength,double** B,int k,int order)
{
	int n=ctrlPointsLength,i;
	double x=0,y=0,z=0;
	for(i=0;i<n+order;i++)
	{
		x+=ctrlPoints[(i+n)%n]->x*B[k][i];
		y+=ctrlPoints[(i+n)%n]->y*B[k][i];
		z+=ctrlPoints[(i+n)%n]->z*B[k][i];
	}
	return newPoint3f(x,y,z);
}
Point3f** getCoordinates_Bezier(int curveLength,Point3f** ctrlPoints,int ctrlPointsLength)
{
	int l=curveLength,n=ctrlPointsLength,i,m=2;
	double* knots=getBezierUniformNurbsKnots(n,m);
	double** B=getParameterMatrix(knots,l,m,n);
	Point3f** coordinates=new_Point3f(l);
	for(i=0;i<l;i++)coordinates[i]=getCoordinate_BSpline_Curve(ctrlPoints,n,B,i);
	return coordinates;
}
