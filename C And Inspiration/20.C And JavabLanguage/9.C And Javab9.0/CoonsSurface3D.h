Point3f* getCoordinate_Coons(Point3f** pU0,Point3f** pU1,Point3f** p0V,Point3f** p1V,int i,int j,int uStep,int vStep);
Shape3D* newCoonsSurface3D(Point3f** pointsU0,Point3f** pointsU1,Point3f** points0V,Point3f** points1V,int uStep,int vStep)
{
	int r=uStep,c=vStep,v=0,i,j;
	int coordinatesLength=r*c,coordinateIndicesLength=(r-1)*(c-1)*4;
	Point3f** coordinates=new_Point3f(coordinatesLength);
	for(i=0;i<r;i++)for(j=0;j<c;j++)coordinates[i*c+j]=getCoordinate_Coons(pointsU0,pointsU1,points0V,points1V,i,j,r,c);
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
Point3f* midPoint(Point3f* p0,Point3f* p1,double u)
{
	double x=(1-u)*p0->x+u*p1->x;
	double y=(1-u)*p0->y+u*p1->y;
	double z=(1-u)*p0->z+u*p1->z;
	return newPoint3f(x,y,z);
}
Point3f* getCoordinate_Coons(Point3f** pU0,Point3f** pU1,Point3f** p0V,Point3f** p1V,int i,int j,int uStep,int vStep)
{
	int m=uStep,n=vStep;
	double u=i*1.0/(m-1),v=j*1.0/(n-1);
	Point3f* q=midPoint(p0V[j],p1V[j],u);
	Point3f* r=midPoint(pU0[i],pU1[i],v);
	Point3f* p00=p0V[0],*p01=p0V[n-1];
	Point3f* p10=p1V[0],*p11=p1V[n-1];
	Point3f* sU0=midPoint(p00,p10,u);
	Point3f* sU1=midPoint(p01,p11,u);
	Point3f* s=midPoint(sU0,sU1,v);
	double x=q->x+r->x-s->x;
	double y=q->y+r->y-s->y;
	double z=q->z+r->z-s->z;
	return newPoint3f(x,y,z);
}
