void unitVector(Vector3f* vector)
{
	double length=vectorLength(vector);
	vector->x/=length;
	vector->y/=length;
	vector->z/=length;
}
Shape3D* newCrust3D(Point3f*** points,double** outerSizes,double** innerSizes,Point2f** textureCoordinates,Color3f** colors,int uStep,int vStep)
{
	int m=uStep-1,n=vStep-1,v=0,i,j;
	int coordinatesLength=m*n*2;
	int coordinateIndicesLength=(m-1)*n*2*2+n*2*2+m*2*2;
	int stripCountsLength=(m-1)*2+4;
	Point3f** coordinates=new_Point3f(coordinatesLength);
	int** indices=new_Int(m*n,2);
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			Vector3f* v10=pointsDistance(points[i][j],points[i+1][j+0]);
			Vector3f* v01=pointsDistance(points[i][j],points[i+0][j+1]);
			Vector3f* normal=vectorCross(v10,v01);unitVector(normal);
			double x0=points[i][j]->x+outerSizes[i][j]*normal->x;
			double y0=points[i][j]->y+outerSizes[i][j]*normal->y;
			double z0=points[i][j]->z+outerSizes[i][j]*normal->z;
			coordinates[v]=newPoint3f(x0,y0,z0);indices[i*n+j][0]=v++;
			double x1=points[i][j]->x-innerSizes[i][j]*normal->x;
			double y1=points[i][j]->y-innerSizes[i][j]*normal->y;
			double z1=points[i][j]->z-innerSizes[i][j]*normal->z;
			coordinates[v]=newPoint3f(x1,y1,z1);indices[i*n+j][1]=v++;
		}
	}
	int* coordinateIndices=new_int(coordinateIndicesLength);v=0;
	for(i=1;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			coordinateIndices[v++]=indices[i*n+j][1];
			coordinateIndices[v++]=indices[(i-1)*n+j][1];
		}
	}
	for(i=1;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			coordinateIndices[v++]=indices[(i-1)*n+j][0];
			coordinateIndices[v++]=indices[i*n+j][0];
		}
	}
	{
		int i=0;
		for(j=0;j<n;j++)
		{
			coordinateIndices[v++]=indices[i*n+j][1];
			coordinateIndices[v++]=indices[i*n+j][0];
		}
	}
	{
		int i=m-1;
		for(j=0;j<n;j++)
		{
			coordinateIndices[v++]=indices[i*n+j][0];
			coordinateIndices[v++]=indices[i*n+j][1];
		}
	}
	for(i=0;i<m;i++)
	{
		int j=0;
		{
			coordinateIndices[v++]=indices[i*n+j][0];
			coordinateIndices[v++]=indices[i*n+j][1];
		}
	}
	for(i=0;i<m;i++)
	{
		int j=n-1;
		{
			coordinateIndices[v++]=indices[i*n+j][1];
			coordinateIndices[v++]=indices[i*n+j][0];
		}
	}
	int* stripCounts=new_int(stripCountsLength);v=0;
	for(i=0;i<m-1;i++)stripCounts[v++]=2*n;
	for(i=0;i<m-1;i++)stripCounts[v++]=2*n;
	stripCounts[v++]=2*n;
	stripCounts[v++]=2*n;
	stripCounts[v++]=2*m;
	stripCounts[v++]=2*m;
	GeometryInfo* GeometryInfo1=newGeometryInfo(TRIANGLE_STRIP_ARRAY);
	setCoordinates(GeometryInfo1,coordinates,coordinatesLength);
	setCoordinateIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	setTextureCoordinates(GeometryInfo1,textureCoordinates,coordinatesLength);
	setTextureCoordinateIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	setColors(GeometryInfo1,colors,coordinatesLength);
	setColorIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	setStripCounts(GeometryInfo1,stripCounts,stripCountsLength);
	generateNormals(GeometryInfo1);
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	return shape3D;
}
