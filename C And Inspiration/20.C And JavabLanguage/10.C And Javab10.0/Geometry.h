#include "IsoSurface3D.h"
#include "NurbsSurface3D.h"
#include "NurbsIsoSurface3D.h"
#include "NurbsTorus3D.h"
#include "NurbsSphere3D.h"
#include "NurbsCtrlSurface3D.h"
#include "LagrangeSurface3D.h"
#include "HermiteSurface3D.h"
#include "CoonsSurface3D.h"
#include "Pipeline3D.h"
#include "Crust3D.h"
Shape3D* newSurface3D(Point3f** coordinates,Point2f** textureCoordinates,Color3f** colors,int m,int n)
{
	int i,j,v=0,*coordinateIndices=new_int(2*(m-1)*n);
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
	setTextureCoordinates(GeometryInfo1,textureCoordinates,m*n);
	setTextureCoordinateIndices(GeometryInfo1,coordinateIndices,2*(m-1)*n);
	setColors(GeometryInfo1,colors,m*n);
	setColorIndices(GeometryInfo1,coordinateIndices,2*(m-1)*n);
	setStripCounts(GeometryInfo1,stripCounts,m-1);
	generateNormals(GeometryInfo1);
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	return shape3D;
}
Shape3D* newPolygons3D(Point3f** coordinates,int coordinatesLength,int* coordinateIndices,int coordinateIndicesLength,int* stripCounts,int stripCountsLength)
{
	GeometryInfo* GeometryInfo1=newGeometryInfo(POLYGON_ARRAY);
	setCoordinates(GeometryInfo1,coordinates,coordinatesLength);
	setCoordinateIndices(GeometryInfo1,coordinateIndices,coordinateIndicesLength);
	setStripCounts(GeometryInfo1,stripCounts,stripCountsLength);
	generateNormals(GeometryInfo1);
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	return shape3D;
}
