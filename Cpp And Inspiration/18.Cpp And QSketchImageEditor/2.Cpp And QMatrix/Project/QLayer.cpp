#include "QLayer.h"
#include <QVector2D>
#include <Eigen/Dense>
using namespace Eigen;

bool QLayer::load(QString fileName)
{
	if(!QImage::load(fileName))return false;
	this->controlPoints<<rect().center();
	this->controlPoints<<rect().topLeft();
	this->controlPoints<<rect().topRight();
	this->controlPoints<<rect().bottomLeft();
	this->controlPoints<<rect().bottomRight();
	this->cornerPoints=controlPoints; return true;
}
void QLayer::setTransform(QPainter& painter)
{
	painter.setTransform(transform);
}
void QLayer::reset(QPainter& painter)
{
	painter.setTransform(QTransform());
}
int triangleArea(QVector<QPoint> triangle)
{
	int x0=triangle[0].x(), y0=triangle[0].y();
	int x1=triangle[1].x(), y1=triangle[1].y();
	int x2=triangle[2].x(), y2=triangle[2].y();
	return abs(x0*(y1-y2)+x1*(y2-y0)+x2*(y0-y1));
}
bool isInsideTriangle(QPoint point, QVector<QPoint> triangle)
{
	QVector<QPoint> triangle0=triangle;
	QVector<QPoint> triangle1=triangle, triangle2=triangle; 
	triangle0[0]=point; triangle1[1]=point; triangle2[2]=point;
	int area=triangleArea(triangle), area0=triangleArea(triangle0);
	int area1=triangleArea(triangle1), area2=triangleArea(triangle2); 
	return area==(area0+area1+area2);
}
bool QLayer::contains(QPoint point)
{
	QVector<QPoint> quad=controlPoints; quad.removeAt(CENTER); 
	QVector<QPoint> triangle0=quad; triangle0.removeAt(0);
	QVector<QPoint> triangle1=quad; triangle1.removeAt(1);
	QVector<QPoint> triangle2=quad; triangle2.removeAt(2);
	QVector<QPoint> triangle3=quad; triangle3.removeAt(3);
	bool isInside=isInsideTriangle(point, triangle0)||isInsideTriangle(point, triangle1);
	return isInside||isInsideTriangle(point, triangle2)||isInsideTriangle(point, triangle3);
}
int QLayer::minDistance=5;
bool QLayer::isClose(QPoint p0, QPoint p1)
{
	return QVector2D(p0).distanceToPoint(QVector2D(p1))<minDistance;
}
bool QLayer::getControlPoint(QPoint point)
{
	this->controlIndex=-1;
	for(int i=0; i<controlPoints.size(); i++)
	{
		if(isClose(controlPoints[i], point))this->controlIndex=i;
	}
	return controlIndex>=0;
}
void QLayer::drawControlPoints(QPainter& painter, int radius)
{
	QVector<QPoint> points;
	points<<controlPoints[1];
	points<<controlPoints[2];
	points<<controlPoints[4];
	points<<controlPoints[3];
	for(int i=0; i<4; i++)
	{
		QPoint point0=points[i], point1=points[(i+1)%4];
		painter.drawEllipse(point0, radius, radius);
		painter.drawLine(point0, point1);
	}
	painter.drawEllipse(controlPoints[0], radius, radius);
}
VectorXd getTransform(VectorXd x1, VectorXd y1, VectorXd x2, VectorXd y2)
{
	MatrixXd P(x1.size()*2, 9); VectorXd b(x1.size()*2);
	for(int i=0; i<x1.size(); i++)
	{
		VectorXd X(9); X<<x1(i),y1(i),1, 0,0,0, -x2(i)*x1(i),-x2(i)*y1(i),-x2(i); P.row(i*2+0)=X;
		VectorXd Y(9); Y<<0,0,0, x1(i),y1(i),1, -y2(i)*x1(i),-y2(i)*y1(i),-y2(i); P.row(i*2+1)=Y;
	}
	return P.fullPivLu().kernel();
}
QString toString(MatrixXd mat)
{
	QString str="";
	for(int i=0; i<mat.rows(); i++)
	{
		for(int j=0; j<mat.cols(); j++)
		{
			str+=QString::number(mat(i, j))+", ";
		}
	}
	return str;
}
QTransform toTransform(VectorXd transform)
{
	#define T transform
	return QTransform
	(
		T(0), T(3), T(6),
		T(1), T(4), T(7),
		T(2), T(5), T(8)
	);
	#undef T
}
void QLayer::updateTransform()
{
	int size=cornerPoints.size()-1;
	VectorXd x1(size), y1(size), x2(size), y2(size);
	for(int i=0; i<size; i++)
	{
		x1(i)=cornerPoints[i+1].x(); y1(i)=cornerPoints[i+1].y();
		x2(i)=controlPoints[i+1].x(); y2(i)=controlPoints[i+1].y();
	}
	VectorXd x=getTransform(x1, y1, x2, y2);
	this->transform=toTransform(x);
}

bool QLayer::setControlPoint(QPoint point)
{
	if(controlIndex<0)return false;
	QPoint move=point-controlPoints[controlIndex];
	if(is(CENTER))for(QPoint& point: controlPoints)point+=move;
	else 
	{
		QVector<QPoint> otherControlPoints=controlPoints;
		otherControlPoints.removeAt(controlIndex); 
		otherControlPoints.removeAt(CENTER); 
		QPoint controlPoint=controlPoints[controlIndex]+move;
		if(isInsideTriangle(controlPoint, otherControlPoints))return false;
		else this->controlPoints[controlIndex]+=move;
	}
	this->updateTransform(); return true;
}
QPoint QLayer::controlPoint()
{
	return controlIndex>=0?controlPoints[controlIndex]:QPoint(-100, -100);
}
bool QLayer::is(int index)
{
	return index==controlIndex;
}
