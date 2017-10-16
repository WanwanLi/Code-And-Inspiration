Point3f* F0(Point3f* ctrlPoint0,double t);
Point3f* F1(Point3f* ctrlPoint1,double t);
Point3f* G0(Point3f* ctrlVector0,double t);
Point3f* G1(Point3f* ctrlVector1,double t);
Point3f* D(Point3f** ctrlPoints,int i);
double d(double* knots,int i);
Point3f* getHermiteCoordinate(Point3f* ctrlPoint0,Point3f* ctrlPoint1,Point3f* ctrlVector0,Point3f* ctrlVector1,double d,double t);
Point3f* getBessellCtrlVector(Point3f** ctrlPoints,int ctrlPointsLength,double* knots,int i);
Point3f* getBessellCtrlVector(Point3f** ctrlPoints,int ctrlPointsLength,double* knots,int i);
Point3f*** getCtrlVectors(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double* knots);
Point3f* getCoordinate_Hermite(Point3f*** ctrlPoints,Point3f*** uCtrlVectors,Point3f*** vCtrlVectors,int i,int j,double dU,double dV,double u,double v);
Point3f** getCoordinates_Hermite(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,Point3f*** uCtrlVectors,Point3f*** vCtrlVectors,double* uKnots,double* vKnots,int row,int column);
Point3f*** transpose(Point3f*** pointsMatrix,int pointsMatrixLength,int pointsMatrix0Length);
Shape3D* newHermiteSurface3D(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,int uStep,int vStep)
{
	int m=ctrlPointsLength,n=ctrlPoints0Length,r=uStep,c=vStep,v=0,i,j;
	double *uKnots=getUniformKnots(m),*vKnots=getUniformKnots(n);
	Point3f*** vCtrlVectors=getCtrlVectors(ctrlPoints,m,n,vKnots);
	Point3f*** newCtrlPoints=transpose(ctrlPoints,m,n);
	Point3f*** newCtrlVectors=getCtrlVectors(newCtrlPoints,n,m,uKnots);
	Point3f*** uCtrlVectors=transpose(newCtrlVectors,n,m);
	Point3f** coordinates=getCoordinates_Hermite(ctrlPoints,m,n,uCtrlVectors,vCtrlVectors,uKnots,vKnots,r,c);
	int coordinatesLength=r*c,coordinateIndicesLength=(r-1)*(c-1)*4;
	int* coordinateIndices=new_int(coordinateIndicesLength);
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
Point3f* F0(Point3f* ctrlPoint0,double t)
{
	double k=1-3*t*t+2*t*t*t;
	double x=ctrlPoint0->x*k;
	double y=ctrlPoint0->y*k;
	double z=ctrlPoint0->z*k;
	return newPoint3f(x,y,z);
}
Point3f* F1(Point3f* ctrlPoint1,double t)
{
	double k=3*t*t-2*t*t*t;
	double x=ctrlPoint1->x*k;
	double y=ctrlPoint1->y*k;
	double z=ctrlPoint1->z*k;
	return newPoint3f(x,y,z);
}
Point3f* G0(Point3f* ctrlVector0,double t)
{
	double k=t-2*t*t+t*t*t;
	double x=ctrlVector0->x*k;
	double y=ctrlVector0->y*k;
	double z=ctrlVector0->z*k;
	return newPoint3f(x,y,z);
}
Point3f* G1(Point3f* ctrlVector1,double t)
{
	double k=-t*t+t*t*t;
	double x=ctrlVector1->x*k;
	double y=ctrlVector1->y*k;
	double z=ctrlVector1->z*k;
	return newPoint3f(x,y,z);
}
Point3f* getHermiteCoordinate(Point3f* ctrlPoint0,Point3f* ctrlPoint1,Point3f* ctrlVector0,Point3f* ctrlVector1,double d,double t)
{
	double x=0,y=0,z=0;
	x+=F0(ctrlPoint0,t)->x;
	y+=F0(ctrlPoint0,t)->y;
	z+=F0(ctrlPoint0,t)->z;

	x+=F1(ctrlPoint1,t)->x;
	y+=F1(ctrlPoint1,t)->y;
	z+=F1(ctrlPoint1,t)->z;

	x+=d*G0(ctrlVector0,t)->x;
	y+=d*G0(ctrlVector0,t)->y;
	z+=d*G0(ctrlVector0,t)->z;

	x+=d*G1(ctrlVector1,t)->x;
	y+=d*G1(ctrlVector1,t)->y;
	z+=d*G1(ctrlVector1,t)->z;
	return newPoint3f(x,y,z);
}
Point3f* D(Point3f** ctrlPoints,int i)
{
	double dx=ctrlPoints[i+1]->x-ctrlPoints[i]->x;
	double dy=ctrlPoints[i+1]->y-ctrlPoints[i]->y;
	double dz=ctrlPoints[i+1]->z-ctrlPoints[i]->z;
	return newPoint3f(dx,dy,dz);
}
double d(double* knots,int i)
{
	return knots[i+1]-knots[i];
}
Point3f* getBessellCtrlVector(Point3f** ctrlPoints,int ctrlPointsLength,double* knots,int i)
{
	int n=ctrlPointsLength;
	if(i==0)
	{
		Point3f* v=getBessellCtrlVector(ctrlPoints,n,knots,1);
		double d0=d(knots,0);
		Point3f* p=D(ctrlPoints,0);
		double x=2.0*p->x/d0-v->x;
		double y=2.0*p->y/d0-v->y;
		double z=2.0*p->z/d0-v->z;
		double r=sqrt(x*x+y*y+z*z);
		return newPoint3f(x/r,y/r,z/r);
	}
	else if(i==n-1)
	{
		Point3f* v=getBessellCtrlVector(ctrlPoints,n,knots,n-2);
		double d0=d(knots,n-2);
		Point3f* p=D(ctrlPoints,n-2);
		double x=2.0*p->x/d0-v->x;
		double y=2.0*p->y/d0-v->y;
		double z=2.0*p->z/d0-v->z;
		double r=sqrt(x*x+y*y+z*z);
		return newPoint3f(x/r,y/r,z/r);
	}
	else
	{
		double d0=d(knots,i-1);
		double d1=d(knots,i);
		Point3f* p0=D(ctrlPoints,i-1);
		Point3f* p1=D(ctrlPoints,i);
		double k0=d1/(d0+d1)/d0;
		double k1=d0/(d0+d1)/d1;
		double x=k0*p0->x+k1*p1->x;
		double y=k0*p0->y+k1*p1->y;
		double z=k0*p0->z+k1*p1->z;
		double r=sqrt(x*x+y*y+z*z);
		return newPoint3f(x/r,y/r,z/r);
	}
}
Point3f*** getCtrlVectors(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double* knots)
{
	int m=ctrlPointsLength,n=ctrlPoints0Length,i,j;
	Point3f*** ctrlVectors=new__Point3f(m,n);
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)ctrlVectors[i][j]=getBessellCtrlVector(ctrlPoints[i],n,knots,j);
	}
	return ctrlVectors;
}
Point3f* getCoordinate_Hermite(Point3f*** ctrlPoints,Point3f*** uCtrlVectors,Point3f*** vCtrlVectors,int i,int j,double dU,double dV,double u,double v)
{
	Point3f* zeroVector=newPoint3f(0,0,0);
	Point3f* ctrlPoint0=getHermiteCoordinate(ctrlPoints[i+0][j+0],ctrlPoints[i+1][j+0],uCtrlVectors[i+0][j+0],uCtrlVectors[i+1][j+0],dU,u);
	Point3f* ctrlPoint1=getHermiteCoordinate(ctrlPoints[i+0][j+1],ctrlPoints[i+1][j+1],uCtrlVectors[i+0][j+1],uCtrlVectors[i+1][j+1],dU,u);
	Point3f* ctrlVector0=getHermiteCoordinate(vCtrlVectors[i+0][j+0],vCtrlVectors[i+1][j+0],zeroVector,zeroVector,dU,u);
	Point3f* ctrlVector1=getHermiteCoordinate(vCtrlVectors[i+0][j+1],vCtrlVectors[i+1][j+1],zeroVector,zeroVector,dU,u);
	return getHermiteCoordinate(ctrlPoint0,ctrlPoint1,ctrlVector0,ctrlVector1,dV,v);
}
Point3f** getCoordinates_Hermite(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,Point3f*** uCtrlVectors,Point3f*** vCtrlVectors,double* uKnots,double* vKnots,int row,int column)
{
	int m=ctrlPointsLength,n=ctrlPoints0Length,r=0,c=0,i,j;
	Point3f** coordinates=new_Point3f(row*column);
	double dU,u=0.0,du=1.0/(row-1),dV,v=0.0,dv=1.0/(column-1);
	for(i=0;i<m-1;i++)
	{
		for(;u<uKnots[i+1]&&r<row;u+=du,r++)
		{
			dU=uKnots[i+1]-uKnots[i];c=0;v=0;
			for(j=0;j<n-1;j++)
			{
				for(;v<vKnots[j+1]&&c<column;v+=dv,c++)
				{
					dV=vKnots[j+1]-vKnots[j];
					coordinates[r*column+c]=getCoordinate_Hermite(ctrlPoints,uCtrlVectors,vCtrlVectors,i,j,dU,dV,(u-uKnots[i])/dU,(v-vKnots[j])/dV);
				}
			}
		}
	}
	return coordinates;
}
double distance(Point3f* point0,Point3f* point1)
{
	double dx=point1->x-point0->x;
	double dy=point1->y-point0->y;
	double dz=point1->z-point0->z;
	return sqrt(dx*dx+dy*dy+dz*dz);
}
double* getRiesenfeldKnots(Point3f** ctrlPoints,int ctrlPointsLength)
{
	int l=ctrlPointsLength,i;
	double* knots=new_double(l);
	double* u=new_double(l);u[0]=0;
	for(i=1;i<l;i++)u[i]=u[i-1]+distance(ctrlPoints[i-1],ctrlPoints[i]);
	for(i=0;i<l;i++)knots[i]=u[i]/u[l-1];
	return knots;
}
Point3f*** transpose(Point3f*** pointsMatrix,int pointsMatrixLength,int pointsMatrix0Length)
{
	int m=pointsMatrixLength,n=pointsMatrix0Length,i,j;
	Point3f*** newMatrix=new__Point3f(n,m);
	for(i=0;i<n;i++)
	{
		for(j=0;j<m;j++)newMatrix[i][j]=pointsMatrix[j][i];
	}
	return newMatrix;
}
