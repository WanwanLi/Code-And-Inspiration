#include "QStrokes.h"

int& QStrokes::operator[](int index)
{
	return this->path[index];
}
void QStrokes::operator=(QPoint point)
{
	if(path.size()>2&&length(point)<maxLength());
	else
	{
		this->path<<MOVE<<point.x()<<point.y();
		this->begin(point);
	}
}
void QStrokes::operator+=(QPoint point)
{
	this->line<<point;
	if(length(point)<minLength())
	{
		this->end(point);
	}
	else if(distance()>maxDistance())
	{
		this->begin(point);
	}
	else this->end(point);
}
QStrokes& QStrokes::operator<<(int path)
{
	this->path<<path;
	return *this;
}
void QStrokes::undo()
{
	if(path.size()<5)return;
	this->path.removeLast();
	this->path.removeLast();
	this->path.removeLast();
	if(path.size()>=5&&path[path.size()-3]==MOVE)this->undo();
}
void QStrokes::clear()
{
	this->path.clear();
}
int QStrokes::size()
{
	return this->path.size();
}
void QStrokes::begin(QPoint point)
{
	this->line.clear();
	this->line<<point;
	this->path<<LINE<<point.x()<<point.y();
}
void QStrokes::end(QPoint point)
{
	this->path[path.size()-2]=point.x();
	this->path[path.size()-1]=point.y();
}
qreal QStrokes::length(QPoint point)
{
	if(path.size()<=2)return 0;
	qreal x=path[path.size()-2], y=path[path.size()-1];
	return QVector2D(x, y).distanceToPoint(QVector2D(point));
}
qreal QStrokes::distance()
{
	qreal totalDistance=0, length;
	for(QPoint point : line)
	{
		totalDistance+=distanceToLine(point, line, length);
	}
	return totalDistance/length;
}
qreal QStrokes::distanceToLine(QPoint& point, QVector<QPoint>& line, qreal& length)
{
	QVector2D begin=QVector2D(line[0]);
	QVector2D end=QVector2D(line[line.size()-1]);
	length=(end-begin).length(); 
	if(length==0){length=1; return 0;}
	QVector2D direction=(end-begin).normalized();
	return QVector2D(point).distanceToLine(begin, direction);
}
qreal QStrokes::minLength()
{
	return qMin(path[0], path[1])*0.0075;
}
qreal QStrokes::maxLength()
{
	return qMin(path[0], path[1])*0.05;
}
qreal QStrokes::maxDistance()
{
	return qMin(path[0], path[1])*0.0025;
}