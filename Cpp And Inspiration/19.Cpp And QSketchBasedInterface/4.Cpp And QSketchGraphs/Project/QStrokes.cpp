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
	for(int i=index-1; i>=2; i--)
	{
		if(path[i]==MOVE||path[i]==LINE||path[i]==CUBIC)return i;
	}
	return 2;
}
int QStrokes::next(int index)
{
	if(index==0)return 2;
	if(path[index]==MOVE)return index+3;
	if(path[index]==LINE)return index+3;
	if(path[index]==CUBIC)return index+7;
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
	this->path<<LINE<<point.x()<<point.y();
	if(direction.length()==0)this->begin=next(begin);
	this->updateGraph();
}
void QStrokes::setPath(int type)
{
	this->removeLast();
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
void QStrokes::removeLast()
{
	if(index<=2)return;
	if(path[index]==MOVE)this->removeLast(3);
	else if(path[index]==LINE)this->removeLast(3);
	else if(path[index]==CUBIC)this->removeLast(7);
}
void QStrokes::removeLast(int length)
{
	for(int i=0; i<length; i++)this->path.removeLast();
}
