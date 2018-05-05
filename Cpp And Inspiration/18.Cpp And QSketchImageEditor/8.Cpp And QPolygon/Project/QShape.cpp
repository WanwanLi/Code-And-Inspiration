#include "QShape.h"
#include "QPolygons.h"
#include <array>
#include <QDebug>
using namespace std;
using Point=array<qreal, 2>;

void QShape::operator=(QPoint point)
{
	if(isEditable)
	{
		if(index<0)this->getIndex(point);
		else 
		{
			this->point2D[index]=getPoint(point); this->update();
			this->removeLast(); this->updateLast();
		}
	}
}
vec2 QShape::getPoint(QPoint point)
{
	for(int i=0; i<point2D.size(); i++)
	{
		if(i!=index&&equals(point, pointAt(i)))return point2D[i];
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
	else this->addCurveTo(point);
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
		if(equals(point, last()))
		this->end(point);
		else
		{
			if(equals(point, beginPoint))
			{
				point=beginPoint;
				this->end(point);
			}
			(*this)+=point;
		}
	}
	else this->start(point);
}
void QShape::addCurveTo(QPoint point)
{
	if(isStarted)
	{
		if(equals(point, last()))
		this->end(point);
		else
		{
			if(equals(point, beginPoint))
			{
				point=beginPoint;
				this->end(point);
			}
			this->curves[curveIndex]<<point;
			this->curves[curveIndex].update(path, point2D);
			this->update();
		}
	}
	else this->start(point);
}
void QShape::start(QPoint point)
{
	this->isStarted=true;
	this->endPoint=NONE;
	this->beginPoint=point;
	QSketch::operator=(point);
	if(is(CUBIC))this->startCurveAt(point);
}
void QShape::startCurveAt(QPoint point)
{
	this->curves<<QCurve(path.size(), point);
	this->curveStartPoint=point;
	this->curveIndex++;
}
void QShape::switchLineCurveMode()
{
	if(is(LINE))
	{
		this->type=CUBIC;
		if(point2D.size()>0)
		if(!isCurveStarted())
		this->startCurveAt(last());
	}
	else
	{
		this->type=LINE;
		this->curveStartPoint=NONE;
	}
}
bool QShape::isCurveStarted()
{
	if(curveIndex<0)return false;
	else return curves[curveIndex].point2D.size()==1;
}
void QShape::end(QPoint point)
{
	this->endPoint=point;
	this->isStarted=false;
	this->curveStartPoint=NONE;
}
QPoint QShape::pointAt(int index)
{
	return toQPoint(point2D[index]);
}
QPoint QShape::last()
{
	return toQPoint(point2D.last());
}
void QShape::updateLast()
{
	int last=point2D.size()-1;
	if(index>last)this->index=last;
	if(index==last&&last<point2D.size())
	this->endPoint=pointAt(last);
}
void QShape::drawLabelPoints(QPainter& painter)
{
	painter.drawText(beginPoint-QPoint(2.4*error, 0), "begin");
	painter.drawText(endPoint+QPoint(1.2*error, 0), "end");
	painter.drawText(curveStartPoint+QPoint(1.2*error, 0), "curve");
	painter.drawEllipse(beginPoint, (int)error/2, (int)error/2);
	painter.drawEllipse(endPoint, (int)error/2, (int)error/2);
	painter.drawEllipse(curveStartPoint, (int)error/2, (int)error/2);
}
void QShape::fillPolygon(QPainter& painter, QBrush brush)
{
	if(polygonCoords.size()>0)
	{
		for(int i=0; i<polygonIndices.size(); i+=3)
		{
			int index0=polygonIndices[i+0];
			int index1=polygonIndices[i+1];
			int index2=polygonIndices[i+2];
			vec2 point0=polygonCoords[index0];
			vec2 point1=polygonCoords[index1];
			vec2 point2=polygonCoords[index2];
			QPainterPath path;
			path.moveTo(point0.x(), point0.y());
			path.lineTo(point1.x(), point1.y());
			path.lineTo(point2.x(), point2.y());
			path.lineTo(point0.x(), point0.y());
			painter.fillPath(path, brush);
		}
	}
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
vec2 pointAt(vec2* ctrlPoints, qreal t)
{
	const int degree=3;
	vec2* B=new vec2[degree+1];
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
void addCubicCurve(vector<Point>& coordinates, vec2* ctrlPoints, qreal precision)
{
	qreal totalLength=0.0;
	for(int i=1; i<4; i++)
	{
		totalLength+=(ctrlPoints[i]-ctrlPoints[i-1]).length();
	}
	int size=totalLength/precision;
	size=size<10?10:size; qreal dt=1.0/size;
	for(int i=0; i<size; i++)
	{
		vec2 point=pointAt(ctrlPoints, i*dt);
		coordinates.push_back({point.x(), point.y()});
	}
}
void QShape::createPolygon()
{
	if(polygonCoords.size()>0)
	{
		this->polygonCoords.clear();
		this->polygonIndices.clear(); return;
	}
	vector<vector<Point>> polygons;
	for(int i=0, j=0; i<path.size(); i++)
	{
		if(path[i]==QSketch::MOVE)
		{
			vec coordinates;
			vector<Point> polygon;
			vec2 point1, point2, point3;
			vec2 start=point2D[j++], point0=start;
			polygon.push_back({point0.x(), point0.y()});
			for(i++; i<path.size()&&path[i]!=QSketch::MOVE; i++)
			{
				if(path[i]==QSketch::LINE)
				{
					vec2 point=point2D[j++]; point0=point;
					if(point!=start)polygon.push_back({point.x(), point.y()});
				}
				else
				{
					point1=point2D[j++]; point2=point2D[j++]; point3=point2D[j++]; i+=2;
					vec2* ctrlPoints=new vec2[4]{point0, point1, point2, point3};
					addCubicCurve(polygon, ctrlPoints, 5.0); point0=point3;
				}
			}
			if(polygon.size()>2)
			{
				polygons.push_back(polygon);
			}
			if(i<path.size())i--;
		}
	}
	this->polygonCoords.clear();
	for(vector<Point> polygon : polygons)
	{
		for(Point point : polygon)
		this->polygonCoords<<vec2(point[0], point[1]);
	}
	vector<int> indices=QPolygons::triangulate<int>(polygons);
	this->polygonIndices=veci::fromStdVector(indices);
}
void QShape::clear()
{
	QSketch::clear();
	this->curves.clear();
	this->curveIndex=-1;
	this->isStarted=false;
	this->endPoint=NONE;
	this->beginPoint=NONE;
	this->curveStartPoint=NONE;
	this->polygonCoords.clear();
	this->polygonIndices.clear();
}
