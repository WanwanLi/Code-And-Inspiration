#include "QPlanes.h"
#include "QVecMath.h"

QPlanes::QPlanes()
{
	this->createGroundPlane(range, range/2, range);
}
void QPlanes::operator<<(QVector4D plane)
{
	this->planes<<plane;
	this->index=planes.size()-1;
}
void QPlanes::clear()
{
	this->planes.clear();
}
void QPlanes::createGroundPlane(qreal length, qreal height, qreal width)
{
	this->p000=QVector3D(-length, -height, -width);
	this->p001=QVector3D(-length, -height,  width);
	this->p011=QVector3D(-length,  height,  width);
	this->p010=QVector3D(-length,  height, -width);
	this->p100=QVector3D( length, -height, -width);
	this->p101=QVector3D( length, -height,  width);
	this->p111=QVector3D( length,  height,  width);
	this->p110=QVector3D( length,  height, -width);
	this->ground<<MOVE<<-length<<-height<<-width;
	this->ground<<  LINE<<-length<<-height<< width;
	this->ground<<  LINE<< length<<-height<< width;
	this->ground<<  LINE<< length<<-height<<-width;
	this->ground<<  LINE<<-length<<-height<<-width;
	this->ground<<MOVE<<0.000<<-height<<0.000;
	this->ground<<  LINE<<length<<-height<<0.000;
	this->ground<<MOVE<<0.000<<-height<<0.000;
	this->ground<<  LINE<<0.000<< height<<0.000;
	this->ground<<MOVE<<0.000<<-height<<0.000;
	this->ground<<  LINE<<0.000<<-height<< width;
	this->planes<<QVecMath::createPlane(QVector3D(0, -height, 0), yAxis); 
}
QVector4D& QPlanes::operator[](int index)
{
	if(index==-1)index=this->index;
	return this->planes[index];
}
void QPlanes::operator=(QPoint point)
{
	this->begin=point;
}
void QPlanes::operator<<=(QPoint point)
{
	this->end=point;
}
int QPlanes::size()
{
	return this->planes.size();
}
void QPlanes::create()
{
	this->isOnCreating=true;
	this->line.clear();
}
void QPlanes::update()
{
	if(isOnCreating)
	{
		this->line.clear();
		this->line<<MOVE<<begin.x()<<begin.y();
		this->line<<LINE<<end.x()<<end.y();
	}
}
void QPlanes::show()
{
	this->lines.clear(); if(index<=0)return;
	QVector4D plane=this->planes[index];
	QVector3D normal=plane.toVector3D();
	qreal xDotn=qAbs(dot(xAxis, normal));
	qreal yDotn=qAbs(dot(yAxis, normal));
	qreal zDotn=qAbs(dot(zAxis, normal));
	qreal max=qMax(xDotn, qMax(yDotn, zDotn));
	if(xDotn==max)this->intersectPlane
	(
		plane, xAxis, new QVector3D[4]
		{
			p000, p001, p011, p010
		}
	);
	else if(yDotn==max)this->intersectPlane
	(
		plane, yAxis, new QVector3D[4]
		{
			p000, p001, p101, p100
		}
	);
	else this->intersectPlane
	(
		plane, zAxis, new QVector3D[4]
		{
			p000, p010, p110, p100
		}
	);
}
void QPlanes::intersectPlane(const QVector4D& plane, const QVector3D& axis, QVector3D* points)
{
	QVector3D point=QVecMath::intersectPlane(points[0], axis, plane);
	this->lines<<MOVE<<point.x()<<point.y()<<point.z();
	for(int i=1; i<=4; i++)
	{
		point=QVecMath::intersectPlane(points[i%4], axis, plane);
		this->lines<<LINE<<point.x()<<point.y()<<point.z();
	}
}
void QPlanes::finish(QViewer& viewer)
{
	QVector3D begin=viewer.projectAt(this->begin);
	QVector3D end=viewer.projectAt(this->end);
	QVector3D direction=(end-begin).normalized();
	QVector3D normal=cross(direction, viewer.forward);
	QVector4D plane=QVecMath::createPlane(begin, normal);
	if((this->index=this->search(plane))==-1)
	{
		this->planes<<plane; 
		this->index=planes.size()-1; 
	}
	this->show(); 
	this->end=this->begin; 
	this->isOnCreating=false;
}
int QPlanes::search(const QVector4D& plane)
{
	int index=-1;
	qreal distance=minDistance;
	for(int i=1; i<planes.size(); i++)
	{
		qreal d=(planes[i]-plane).length();
		if(d<distance){distance=d; index=i;}
	}
	return index;
}
void QPlanes::removeLast()
{
	if(this->planes.size()<=1)return;
	this->planes.removeLast(); 
	this->index=planes.size()-1;
	this->show(); 
}
void QPlanes::reset()
{
	this->index=0;
	this->line.clear();
	this->lines.clear();
	this->isOnCreating=false;
}
void QPlanes::operator>>(QTextStream& textStream)
{
	qreal error=0.1; QString endl="\r\n";
	for(int i=1; i<planes.size(); i++)
	{
		if(QVecMath::isParallel(planes[0], planes[i], error))
		textStream<<"GPAR "<<i<<" 0 0 0"<<endl;
		else if(QVecMath::isPerpendicular(planes[0], planes[i], error))
		textStream<<"GPER "<<i<<" 0 0 0"<<endl;
		for(int j=i+1; j<planes.size(); j++)
		{
			if(QVecMath::isParallel(planes[i], planes[j], error))
			textStream<<"PAR "<<i<<" "<<j<<" 0 0"<<endl;
			else if(QVecMath::isPerpendicular(planes[i], planes[j], error))
			textStream<<"PER "<<i<<" "<<j<<" 0 0"<<endl;
		}
	}
}