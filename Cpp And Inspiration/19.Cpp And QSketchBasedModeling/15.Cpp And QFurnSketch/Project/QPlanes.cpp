#include "QPlanes.h"
#include "QVecMath.h"

QPlanes::QPlanes()
{
	this->createGroundPlane(range, range/2, range);
	this->planes<<groundPlane;
}
void QPlanes::operator<<(QVector4D plane)
{
	this->planes<<plane;
	this->index=planes.size()-1;
}
void QPlanes::clear()
{
	this->index=0;
	this->line.clear();
	this->lines.clear();
	this->stack.clear();
	this->planes.clear();
	this->planes<<groundPlane;
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
	this->groundPlane=QVecMath::createPlane(QVector3D(0, -height, 0), yAxis); 
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
QVector3D* QPlanes::getQuad(int index)
{
	QVector4D plane=this->planes[index];
	QVector3D normal=plane.toVector3D();
	qreal xDotn=qAbs(dot(xAxis, normal));
	qreal yDotn=qAbs(dot(yAxis, normal));
	qreal zDotn=qAbs(dot(zAxis, normal));
	qreal max=qMax(xDotn, qMax(yDotn, zDotn));
	if(xDotn==max)return this->intersectPlane
	(
		plane, xAxis, new QVector3D[4]
		{
			p011, p001, p000, p010
		}
	);
	else if(yDotn==max)return this->intersectPlane
	(
		plane, yAxis, new QVector3D[4]
		{
			p000, p001, p101, p100
		}
	);
	else return this->intersectPlane
	(
		plane, zAxis, new QVector3D[4]
		{
			p010, p000, p100, p110
		}
	);
}
QVector3D* QPlanes::intersectPlane(const QVector4D& plane, const QVector3D& axis, QVector3D* points)
{
	QVector3D* quad=new QVector3D[4];
	for(int i=0; i<4; i++)
	{
		quad[i]=QVecMath::intersectPlane(points[i], axis, plane);
	}
	return quad;
}
void QPlanes::show()
{
	this->lines.clear(); if(index<=0)return;
	QVector3D* quad=this->getQuad(index);
	this->lines<<MOVE<<quad[0].x()<<quad[0].y()<<quad[0].z();
	for(int i=1; i<=4; i++)
	{
		this->lines<<LINE<<quad[i%4].x()<<quad[i%4].y()<<quad[i%4].z();
	}
}
void QPlanes::finish(QViewer& viewer)
{
	QVector3D begin=viewer.projectAt(this->begin);
	QVector3D end=viewer.projectAt(this->end);
	QVector3D direction=(end-begin).normalized();
	QVector3D normal=cross(direction, viewer.forward);
	QVector4D plane=QVecMath::createPlane(begin, get(normal));
	if((this->index=this->search(plane))==-1)
	{
		this->planes<<plane; 
		this->index=planes.size()-1; 
	}
	this->show(); 
	this->end=this->begin; 
	this->isOnCreating=false;
}
QVector3D QPlanes::get(QVector3D normal)
{
	QVector4D plane=QVector4D(normal);
	for(int i=0; i<planes.size(); i++)
	{
		QVector3D planeNormal=planes[i].toVector3D();
		if(QVecMath::isParallel(plane, planes[i], error))normal=planeNormal;
		if(QVecMath::isPerpendicular(plane, planes[i], error))
		{
			normal=cross(planeNormal, cross(normal, planeNormal));
		}
	}
	return normal;
}
int QPlanes::search(const QVector4D& plane)
{
	int index=-1;
	qreal distance=minDistance;
	for(int i=1; i<planes.size(); i++)
	{
		if(QVecMath::isParallel(plane, planes[i], error))
		{
			qreal d=(planes[i]-plane).length();
			if(d<distance){distance=d; index=i;}
		}
	}
	return index;
}
void QPlanes::removeFirst()
{
	if(this->planes.size()==0)return;
	this->planes.removeFirst();
	this->index=planes.size()-1;
}
void QPlanes::removeLast()
{
	if(this->planes.size()<=1)return;
	if(this->index!=planes.size()-1)
	{
		this->index=planes.size()-1;
		this->show(); 
		return;
	}
	this->stack<<planes.last();
	this->planes.removeLast(); 
	this->index=planes.size()-1;
	this->show(); 
}
void QPlanes::undo()
{
	if(stack.size()<=0)return;
	this->planes<<stack.last();
	this->stack.removeLast(); 
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
	QString endl="\r\n";
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
