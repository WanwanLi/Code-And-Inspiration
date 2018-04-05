#include "QStrokes.h"

qreal& QStrokes::operator[](int index)
{
	return this->path[index];
}
void QStrokes::operator=(QPoint point)
{
	QVector3D p=viewer.projectAt(point, planes[-1]);
	this->path<<MOVE<<p.x()<<p.y()<<p.z()<<planes.index;
	this->startPath(point, QVector2D(0, 0));
}
void QStrokes::operator+=(QPoint point)
{
	this->curve+=point;
	if(curve.length()<curve.minLength)this->setPath(LINE);
	else if(curve.isLinear())this->setPath(LINE);
	else if(curve.isCubic())this->setPath(CUBIC);
	else 
	{ 
		this->curve--; 
		if(curve.isLinear())this->setPath(LINE);
		else if(curve.isCubic())this->setPath(CUBIC);
		this->startPath(point, curve.direction());
	}
}
QStrokes& QStrokes::operator<<(qreal path)
{
	this->path<<path;
	return *this;
}
void QStrokes::operator--(int)
{
	this->removeLast();
	this->index=prev(size());
	this->graph--;
	this->begin=prev(begin);
	this->end=prev(end);
	while(index>2&&path[index]==MOVE)
	{
		this->removeLast();
		this->index=prev(size());
		this->begin=prev(begin);
		this->end=prev(end);
	}
}
void QStrokes::createGraph()
{
	for(int i=2, n=next(i); n<size(); i=n, n=next(i))
	{
		if(path[n]==MOVE)continue;
		this->graph+=new int[2]{i, n};
	}
	this->begin=size();
}
void QStrokes::updateGraph()
{
	this->end=index;
	this->graph+=new int[2]{begin, end};
	this->begin=end;
}
int QStrokes::prev(int index)
{
	if(index<=2)return 2;
	if(path[index-5]==MOVE)return index-5;
	if(path[index-5]==LINE)return index-5;
	if(path[index-13]==CUBIC)return index-13;
}
int QStrokes::next(int index)
{
	if(index==0)return 2;
	if(path[index]==MOVE)return index+5;
	if(path[index]==LINE)return index+5;
	if(path[index]==CUBIC)return index+13;
}
void QStrokes::clear()
{
	this->index=0;
	this->begin=0;
	this->path.clear();
	this->graph.clear();
}
void QStrokes::update()
{
	this->graph.updateEdge();
}
int QStrokes::size()
{
	return this->path.size();
}
void QStrokes::startPath(QPoint& point, QVector2D& direction)
{
	this->index=size();
	this->curve.clear();
	this->curve+=point;
	this->curve.ctrlTangent=direction;
	QVector3D p=viewer.projectAt(point, planes[-1]);
	this->path<<LINE<<p.x()<<p.y()<<p.z()<<planes.index;
	if(direction.length()==0)this->begin=next(begin);
	this->updateGraph();
}
void QStrokes::setPath(int type)
{
	this->removeLast();
	QVector2D* points=curve.ctrlPoints;
	if(type==LINE)
	{
		QVector3D p=viewer.projectAt(points[3], planes[-1]);
		this->path<<LINE<<p.x()<<p.y()<<p.z()<<planes.index;
	}
	else if(type==CUBIC)
	{
		this->path<<CUBIC;
		QVector3D p1=viewer.projectAt(points[1], planes[-1]);
		QVector3D p2=viewer.projectAt(points[2], planes[-1]);
		QVector3D p3=viewer.projectAt(points[3], planes[-1]);
		this->path<<p1.x()<<p1.y()<<p1.z()<<planes.index;
		this->path<<p2.x()<<p2.y()<<p2.z()<<planes.index;
		this->path<<p3.x()<<p3.y()<<p3.z()<<planes.index;
	}
}
void QStrokes::removeLast()
{
	if(index<=2)return;
	if(path[index]==MOVE)this->removeLast(5);
	else if(path[index]==LINE)this->removeLast(5);
	else if(path[index]==CUBIC)this->removeLast(13);
}
void QStrokes::removeLast(int length)
{
	for(int i=0; i<length; i++)this->path.removeLast();
}
