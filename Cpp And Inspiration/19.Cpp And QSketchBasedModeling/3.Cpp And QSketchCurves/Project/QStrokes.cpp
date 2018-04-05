#include "QStrokes.h"

int& QStrokes::operator[](int index)
{
	return this->path[index];
}
void QStrokes::operator=(QPoint point)
{
	this->path<<MOVE<<point.x()<<point.y();
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
QStrokes& QStrokes::operator<<(int path)
{
	this->path<<path;
	return *this;
}
void QStrokes::undo()
{
	if(size()<5)return;
	if(path[index]==MOVE)this->removeLast(3);
	else if(path[index]==LINE)this->removeLast(3);
	else if(path[index]==CUBIC)this->removeLast(7);
	for(int i=size()-1; i>0; i--)
	{
		if(path[i]==MOVE||path[i]==LINE||path[i]==CUBIC)
		{
			this->index=i; return;
		}
	}
}
void QStrokes::clear()
{
	this->index=0;
	this->path.clear();
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
	this->path<<LINE<<point.x()<<point.y();
}
void QStrokes::setPath(int type)
{
	if(path[index]==MOVE)this->removeLast(3);
	else if(path[index]==LINE)this->removeLast(3);
	else if(path[index]==CUBIC)this->removeLast(7);
	QVector2D* points=curve.ctrlPoints;
	if(type==LINE)
	{
		this->path<<LINE<<points[3].x()<<points[3].y();
	}
	else if(type==CUBIC)
	{
		this->path<<CUBIC;
		this->path<<points[1].x()<<points[1].y();
		this->path<<points[2].x()<<points[2].y();
		this->path<<points[3].x()<<points[3].y();
	}
}
void QStrokes::removeLast(int length)
{
	for(int i=0; i<length; i++)this->path.removeLast();
}