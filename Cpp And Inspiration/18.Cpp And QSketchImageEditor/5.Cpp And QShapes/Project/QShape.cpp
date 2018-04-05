#include "QShape.h"
#include <QDebug>

void QShape::operator=(QPoint point)
{
	if(isEditable)
	{
		if(index<0)this->getIndex(point);
		else {this->point2D[index]=getPoint(point); this->update();}
	}
}
vec2 QShape::getPoint(QPoint point)
{
	for(int i=0; i<point2D.size(); i++)
	{
		if(i!=index&&equals(point, toQPoint(point2D[i])))return point2D[i];
	}
	return vec2(point);
}
void QShape::getIndex(QPoint point)
{
	for(qreal i=0, minDist; i<point2D.size(); i++)
	{
		qreal dist=vec2(point).distanceToPoint(point2D[i]);
		if(index<0||dist<minDist){minDist=dist; this->index=i;}
	}
}
void QShape::operator<<(QPoint point)
{
	if(isEditable){(*this)=point; this->index=-1; return;}
	if(is(LINE))this->addLineTo(point);
}
bool QShape::equals(QPoint point1, QPoint point2)
{
	if(!isAutoAligned)return (point1==point2);
	return vec2(point1).distanceToPoint(vec2(point2))<error;
}
QPoint QShape::toQPoint(vec2 point)
{
	return QPoint(point.x(), point.y());
}
void QShape::addLineTo(QPoint point)
{
	if(isStarted)
	{
		if(equals(point, toQPoint(point2D.last())))
		{
			this->lastPoint=point;
			this->isStarted=false;
		}
		else
		{
			this->path<<LINE;
			if(equals(point, movePoint))
			{
				point=movePoint;
				this->lastPoint=point;
				this->isStarted=false;
			}
			this->lineTo(point.x(), point.y());
			this->point2D<<vec2(point.x(), point.y());
		}
	}
	else
	{
		this->path<<MOVE;
		this->isStarted=true;
		this->lastPoint=NONE;
		this->movePoint=point;
		this->moveTo(point.x(), point.y());
		this->point2D<<vec2(point.x(), point.y());
	}
}
void QShape::drawEndPoints(QPainter& painter)
{
	painter.drawText(movePoint-QPoint(2.4*error, 0), "begin");
	painter.drawText(lastPoint+QPoint(1.2*error, 0), "end");
	painter.drawEllipse(movePoint, (int)error/2, (int)error/2);
	painter.drawEllipse(lastPoint, (int)error/2, (int)error/2);
}
void QShape::drawPoints(QPainter& painter)
{
	if(!isEditable)return;
	for(vec2 point : point2D)
	{
			painter.drawEllipse(toQPoint(point), (int)error/3, (int)error/3);
	}
}
bool QShape::is(int type)
{
	return this->type==type;
}
void QShape::clear()
{
	QSketch::clear();
	this->isStarted=false;
	this->lastPoint=NONE;
	this->movePoint=NONE;
}
