#include "QMesh.h"
#define INF 10000000
#define dot QVector3D::dotProduct
QVector3D inf=QVector3D(INF, INF, INF);

QMesh::QMesh(QSketch& sketch)
{
	this->planes=sketch.planes;
	QVector3D point0=inf, point1;
	for(int i=2; i<sketch.strokes.size(); i++)
	{
		if(sketch.strokes[i]==MOVE)
		{
			qreal x=sketch.strokes[++i];
			qreal y=sketch.strokes[++i];
			qreal z=sketch.strokes[++i];
			qreal w=sketch.strokes[++i];
			point1=QVector3D(x, y, z);
			if(point1!=point0)
			{
				point0=point1;
				this->strokes<<MOVE;
				this->strokes<<x<<y<<z<<w;
			}
		}
		else if(sketch.strokes[i]==LINE)
		{
			qreal x=sketch.strokes[++i];
			qreal y=sketch.strokes[++i];
			qreal z=sketch.strokes[++i];
			qreal w=sketch.strokes[++i];
			point1=QVector3D(x, y, z);
			if(point1!=point0)
			{
				point0=point1;
				this->strokes<<LINE;
				this->strokes<<x<<y<<z<<w;
			}
		}
		else if(sketch.strokes[i]==CUBIC)
		{
			this->strokes<<CUBIC;
			for(int j=0; j<12; j++)
			{
				this->strokes<<sketch.strokes[++i]; 
			}
			qreal x=sketch.strokes[i-3];
			qreal y=sketch.strokes[i-2];
			qreal z=sketch.strokes[i-1];
			point0=QVector3D(x, y, z);
		}
	}
	this->getCoordinates();
}
void QMesh::getCoordinates()
{
	for(int i=0; i<strokes.size(); i++)
	{
		if(strokes[i]==MOVE)
		{
			QVector<qreal> coordinates;
			qreal x=strokes[++i], y=strokes[++i];
			qreal z=strokes[++i], w=strokes[++i];
			QVector3D start=QVector3D(x, y, z);
			QVector3D* quad=planes.getQuad(w);
			this->addPoint(coordinates, start, quad); 
			QVector3D point0=start, point1, point2, point3;
			for(i++; i<strokes.size()&&strokes[i]!=MOVE; i++)
			{
				if(strokes[i]==LINE)
				{
					x=strokes[++i]; y=strokes[++i];
					z=strokes[++i]; w=strokes[++i];
					QVector3D point=QVector3D(x, y, z); point0=point;
					if(point!=start)this->addPoint(coordinates, point, quad); 
				}
				else if(strokes[i]==CUBIC)
				{
					x=strokes[++i]; y=strokes[++i];
					z=strokes[++i]; w=strokes[++i];
					point1=QVector3D(x, y, z); 
					x=strokes[++i]; y=strokes[++i];
					z=strokes[++i]; w=strokes[++i];
					point2=QVector3D(x, y, z); 
					x=strokes[++i]; y=strokes[++i];
					z=strokes[++i]; w=strokes[++i];
					point3=QVector3D(x, y, z);  
					this->addCurve(coordinates,
					new QVector3D[4]{point0, point1, 
					point2, point3}, quad); point0=point3;
				}
			}
			if(coordinates.size()>=6)
			{
				this->quads<<quad;
				this->coords<<coordinates;
			}
			if(i<strokes.size())i--;
		}
	}
}
void QMesh::addPoint(QVector<qreal>& coordinates, const QVector3D& point, QVector3D* quad)
{
	QVector3D p0=quad[0],  pX=quad[3], pY=quad[1];
	coordinates<<dot(point-p0, (pX-p0).normalized())/(pX-p0).length();
	coordinates<<dot(point-p0, (pY-p0).normalized())/(pY-p0).length();
}
void QMesh::addCurve(QVector<qreal>& coordinates, QVector3D* ctrlPoints,  QVector3D* quad)
{
	qreal totalLength=0.0; 
	for(int i=1; i<4; i++)
	{
		totalLength+=(ctrlPoints[i]-ctrlPoints[i-1]).length();
	}
	int size=totalLength/curveLength;
	size=size<10?10:size; qreal dt=1.0/(size-1);
	for(int i=0; i<size; i++)
	{
		QVector3D point=pointAt(ctrlPoints, i*dt);
		this->addPoint(coordinates, point, quad);
	}
}
QVector3D QMesh::pointAt(QVector3D* ctrlPoints, qreal t)
{
	const int degree=3;
	QVector3D* B=new QVector3D[degree+1];
	for(int i=0; i<=degree; B[i]=ctrlPoints[i++]);
	for(int i=1; i<=degree; i++) 
	{	
		for(int j=0; j<=degree-i; j++)
		{
	    		B[j]=(1.0-t)*B[j]+t*B[j+1];
		}
	}
	return B[0];
}
