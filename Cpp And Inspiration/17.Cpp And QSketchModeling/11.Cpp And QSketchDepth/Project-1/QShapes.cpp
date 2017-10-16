#include "QShapes.h"
#include <QDebug>
#define cross QVector3D::crossProduct

int& QShapes::operator[](int index)
{
	return this->path[index];
}
void QShapes::operator=(QPoint point)
{
	this->min=point;
	this->max=point;
	this->stroke<<point;
	this->path<<MOVE<<point.x()<<point.y();
}
void QShapes::operator+=(QPoint point)
{
	this->stroke<<point; 
	this->updateMinMax(point);
	this->path<<LINE<<point.x()<<point.y();
}
int QShapes::size()
{
	return this->path.size();
}
void QShapes::create(int shape)
{
	this->isOnCreating=true;
	this->shape=shape;
}
void QShapes::updateMinMax(const QPoint& point)
{
	if(point.x()<min.x())this->min.setX(point.x());
	if(point.y()<min.y())this->min.setY(point.y());
	if(point.x()>max.x())this->max.setX(point.x());
	if(point.y()>max.y())this->max.setY(point.y());
}
void QShapes::addSymmetry(QStrokes& strokes, QVector2D& orgin, QVector2D& axis)
{
	this->isSymmetric=false;
	if(symmetryIndex<0)return; this->path.clear();
	for(int i=symmetryIndex; i<strokes.size(); i++)
	{
		if(strokes[i]==MOVE)
		{
			qreal x=strokes[++i], y=strokes[++i], z=strokes[++i], w=strokes[++i];	
			QVector2D point=reflect(strokes.viewer.lookAt(x, y, z), orgin, axis);
			this->path<<MOVE<<point.x()<<point.y();
		}
		else if(strokes[i]==LINE)
		{
			qreal x=strokes[++i], y=strokes[++i], z=strokes[++i], w=strokes[++i];
			QVector2D point=reflect(strokes.viewer.lookAt(x, y, z), orgin, axis);
			this->path<<LINE<<point.x()<< point.y();
		}
		else if(strokes[i]==CUBIC)
		{
			this->path<<CUBIC;
			qreal x=strokes[++i], y=strokes[++i], z=strokes[++i], w=strokes[++i];	
			QVector2D point=reflect(strokes.viewer.lookAt(x, y, z), orgin, axis);
			this->path<<point.x()<< point.y();
			x=strokes[++i], y=strokes[++i], z=strokes[++i], w=strokes[++i];	
			point=reflect(strokes.viewer.lookAt(x, y, z), orgin, axis);
			this->path<<point.x()<< point.y();
			x=strokes[++i], y=strokes[++i], z=strokes[++i], w=strokes[++i];	
			point=reflect(strokes.viewer.lookAt(x, y, z), orgin, axis);
			this->path<<point.x()<< point.y();
		}
		
	}
	strokes<<=path; 
	this->path.clear();
	this->symmetryIndex=-1;
}
QVector2D QShapes::reflect(QPoint& point, QVector2D& orgin, QVector2D& axis)
{
	QVector3D normal=cross(QVector3D(0, 0, 1), QVector3D(axis)).normalized();
	qreal distance=QVector2D(point).distanceToLine(orgin, axis);
	QVector2D point1=QVector2D(point)+normal.toVector2D()*2*distance;
	if(point1.distanceToLine(orgin, axis)==distance)return point1;
	return QVector2D(point)+normal.toVector2D()*-2*distance;
}
void QShapes::finish(QStrokes& strokes)
{
	this->path.clear();
	switch(shape)
	{
		case RECT: this->getRectangle(); break;
		case ELLIPSE: this->getEllipse();  break;
		case POLYGON: this->getPolygon(); break;
	}
	strokes<<=path; 
	this->path.clear();
	this->stroke.clear();
	this->isOnCreating=false;
}
void QShapes::getRectangle()
{
	this->path<<MOVE<<min.x()<<min.y();
	this->path<<LINE<<min.x()<<max.y();
	this->path<<LINE<<max.x()<<max.y();
	this->path<<LINE<<max.x()<<min.y();
	this->path<<LINE<<min.x()<<min.y();
}
void QShapes::getEllipse()
{
	QVector2D ellipse[13];
	QVector2D scale=QVector2D((max-min)/2);
	QVector2D translate=QVector2D((min+max)/2);
	for(int i=0; i<=12; i++)
	{
		ellipse[i]=this->ellipse[i]*scale+translate;
	}
	this->path<<MOVE<<ellipse[0].x()<<ellipse[0].y();
	for(int i=0; i<12; i+=3)
	{
		this->path<<CUBIC;
		this->path<<ellipse[i+1].x()<<ellipse[i+1].y();
		this->path<<ellipse[i+2].x()<<ellipse[i+2].y();
		this->path<<ellipse[i+3].x()<<ellipse[i+3].y();
	}
}
void QShapes::getPolygon()
{
	this->path<<MOVE<<stroke.last().x()<<stroke.last().y();
	this->path<<LINE<<stroke.first().x()<<stroke.first().y();
	for(int begin=0, end=begin+10; end<stroke.size(); end++)
	{
		if(distanceToLine(begin, end)>maxError)
		{
			this->path<<LINE<<stroke[end-1].x()<<stroke[end-1].y();
			begin=end-1; end=begin+10;
		}
	}
	this->path<<LINE<<stroke.last().x()<<stroke.last().y();
}
qreal QShapes::distanceToLine(int beginIndex, int endIndex)
{
	QVector2D begin=QVector2D(stroke[beginIndex]);
	QVector2D end=QVector2D(stroke[endIndex]); 
	QVector2D direction=(end-begin).normalized(); 
	qreal error=0.0, length=0.0;
	for(int i=beginIndex+1; i<endIndex; i++)
	{
		error+=QVector2D(stroke[i]).distanceToLine(begin, direction);
		length+=QVector2D(stroke[i]-stroke[i-1]).length();
	}
	return error/length;
}
