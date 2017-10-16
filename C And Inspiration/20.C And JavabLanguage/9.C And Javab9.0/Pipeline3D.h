Vector3f* pointsDistance(Point3f* p1,Point3f* p2)
{
	return newVector3f(p2->x-p1->x,p2->y-p1->y,p2->z-p1->z);
}
double angleToY(double x,double y)
{
	double r=sqrt(x*x+y*y);
	return r==0?0:y>=0?asin(x/r):PI-asin(x/r);
}
double vectorLength(Vector3f* v)
{
	return sqrt(v->x*v->x+v->y*v->y+v->z*v->z);
}
Vector3f* vectorCross(Vector3f* v0,Vector3f* v1)
{
	double x=v0->y*v1->z-v1->y*v0->z;
	double y=v0->z*v1->x-v1->z*v0->x;
	double z=v0->x*v1->y-v1->x*v0->y;
	return newVector3f(x,y,z);
}
double vectorAngle(Vector3f* v0,Vector3f* v1)
{
	double l0=vectorLength(v0);
	double l1=vectorLength(v1);
	return acos((v0->x*v1->x+v0->y*v1->y+v0->z*v1->z)/(l0*l1));
}
void rotateX(Vector3f* v,double a)
{
	double y=v->y;
	double z=v->z;
	v->y=y*cos(a)-z*sin(a);
	v->z=y*sin(a)+z*cos(a);
}
void rotateY(Vector3f* v,double a)
{
	double z=v->z;
	double x=v->x;
	v->z=z*cos(a)-x*sin(a);
	v->x=z*sin(a)+x*cos(a);
}
void rotateZ(Vector3f* v,double a)
{
	double x=v->x;
	double y=v->y;
	v->x=x*cos(a)-y*sin(a);
	v->y=x*sin(a)+y*cos(a);
}
void rotate(Vector3f* vector,Vector3f* axis,double angle)
{
	if(angle==0)return;
	Vector3f* v=newVector3f(vector->x,vector->y,vector->z);
	Vector3f* a=newVector3f(axis->x,axis->y,axis->z);
	double rot_Z=angleToY(a->x,a->y);
	rotateZ(a,rot_Z);
	double rot_X=-angleToY(a->z,a->y);
	rotateZ(v,rot_Z);
	rotateX(v,rot_X);
	rotateY(v,angle);
	rotateX(v,-rot_X);
	rotateZ(v,-rot_Z);
	vector->x=v->x;
	vector->y=v->y;
	vector->z=v->z;
}
Shape3D* newPipeline3D(Point3f** points,int pointsLength,double* sizes,Point2f** textureCoordinates,Color3f** colors,int n)
{
	int m=pointsLength,v=0,i,j;
	int coordinatesLength=(m+2)*n;
	int coordinateIndicesLength=(m+1)*n*2;
	Point3f** coordinates=new_Point3f(coordinatesLength);
	int* coordinateIndices=new_int(coordinateIndicesLength);
	double angle=2*PI/(n-1);
	Vector3f* dF0=newVector3f(0,1,0),*axis=dF0;
	Vector3f** d=new_Vector3f(n);
	for(j=0;j<n;j++)
	{
		double x=cos(angle*j);
		double y=0;
		double z=sin(angle*j);
		d[j]=newVector3f(x,y,z);
	}
	for(j=0;j<n;j++)coordinates[v++]=points[0];
	for(i=0;i<m;i++)
	{
		Point3f* P=points[i];
		double R=sizes[i];
		if(i==m-1)angle=0;
		else
		{
			Vector3f* dF=pointsDistance(points[i],points[i+1]);
			angle=vectorAngle(dF0,dF);
			axis=vectorCross(dF0,dF);
			dF0=dF;
		}
		for(j=0;j<n;j++)
		{
			rotate(d[j],axis,angle);
			coordinates[v++]=newPoint3f(P->x+R*d[j]->x,P->y+R*d[j]->y,P->z+R*d[j]->z);
		}
	}
	for(j=0;j<n;j++)coordinates[v++]=points[m-1];
	v=0;
	for(i=1;i<m+2;i++)
	{
		for(j=0;j<n;j++)
		{
			coordinateIndices[v++]=(i-1)*n+j;
			coordinateIndices[v++]=i*n+j;
		}
	}
	int* stripCounts=new_int(m+1);
	for(i=0;i<m+1;i++)stripCounts[i]=2*n;
	GeometryInfo* GeometryInfo1=newGeometryInfo(TRIANGLE_STRIP_ARRAY);
	setCoordinates(GeometryInfo1,coordinates,coordinatesLength);
	setCoordinateIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	setTextureCoordinates(GeometryInfo1,textureCoordinates,coordinatesLength);
	setTextureCoordinateIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	setColors(GeometryInfo1,colors,coordinatesLength);
	setColorIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	setStripCounts(GeometryInfo1,stripCounts,m+1);
	generateNormals(GeometryInfo1);
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	return shape3D;
}
/*
Point3f*** transpose(Point3f*** pointsMatrix,int pointsMatrixLength,pointsMatrix0Length)
{
	int m=pointsMatrixLength,n=pointsMatrix0Length,i,j;
	Point3f*** newMatrix=new__Point3f(n,m);
	for(i=0;i<n;i++)
	{
		for(j=0;j<m;j++)newMatrix[i][j]=pointsMatrix[j][i];
	}
	return newMatrix;
}
*/
TransformGroup* newMeshline3D(Point3f*** points,int pointsLength,int points0Length,double size,int step,Appearance* appearance)
{
	int m=pointsLength,n=points0Length,i,j;
	Point3f*** newPoints=transpose(points,m,n);
	double* sizes=new_double(n),*newSizes=new_double(m);
	for(j=0;j<n;j++)sizes[j]=size;
	for(i=0;i<m;i++)newSizes[i]=size;
	TransformGroup* meshline3D=newTransformGroup(null);
	for(i=0;i<m;i++)
	{
		Shape3D* pipeline3D=newPipeline3D(points[i],n,sizes,null,null,step);
		setAppearance(pipeline3D,appearance);
		addChild_TS(meshline3D,pipeline3D);
	}
	for(j=0;j<n;j++)
	{
		Shape3D* pipeline3D=newPipeline3D(newPoints[j],m,newSizes,null,null,step);
		setAppearance(pipeline3D,appearance);
		addChild_TS(meshline3D,pipeline3D);
	}
	return meshline3D;
}
