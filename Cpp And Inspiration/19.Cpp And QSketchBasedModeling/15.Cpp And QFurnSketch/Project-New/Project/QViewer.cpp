#include "QViewer.h"
#include "QVecMath.h"

QPoint QViewer::lookAt(qreal x, qreal y, qreal z)
{
	QVector3D point(x, y, z), viewDirection=-forward;
	QVector3D eye=viewDirection*viewDistance;
	QVector3D focus=viewDirection*(viewDistance-focalLength);
	QVector4D viewPlane=QVecMath::createPlane(focus, viewDirection);
	QVector3D position=QVecMath::intersectPlane(eye, point-eye, viewPlane);
	x=screenScale*dot(position, right)*size.width()/2+size.width()/2; 
	y=screenScale*aspectRatio*dot(position, up)*size.height()/2+size.height()/2;
	return QPoint((int)x, size.height()-(int)y);
}
QVector3D QViewer::projectAt(const QVector2D& point, const QVector4D& plane)
{
	QVector3D viewDirection=-forward;
	QVector3D eye=viewDirection*viewDistance;
	QVector3D focus=viewDirection*(viewDistance-focalLength);
	qreal x=(point.x()-size.width()/2+0.0)/(size.width()/2)/screenScale;
	qreal y=(point.y()-size.height()/2+0.0)/(size.height()/2)/aspectRatio/screenScale;
	return QVecMath::intersectPlane(eye, (focus+x*right-y*up)-eye, plane);
}
QVector3D QViewer::projectAt(const QPoint& point, const QVector4D& plane)
{
	return this->projectAt(QVector2D(point), plane);
}
QVector3D QViewer::projectAt(int x, int y, const QVector4D& plane)
{
	return this->projectAt(QVector2D(x+0.0, y+0.0), plane);
}
QVector3D QViewer::projectAt(const QPoint& point)
{
	return this->projectAt(QVector2D(point), QVecMath::createPlane(QVector3D(0, 0, 0), -forward));
}
void QViewer::updateViewDistance(qreal translation)
{
	this->viewDistance+=translateSpeed*translation;
	this->viewDistance=QVecMath::clamp(viewDistance, minDistance, maxDistance);
}
void QViewer::updateViewDirection(QPoint rotation)
{
	qreal rotUp=rotateSpeed*rotation.x();
	qreal rotRight=rotateSpeed*rotation.y();
	this->forward=QVecMath::rotate(forward, up, rotUp);
	this->right=QVecMath::rotate(right, up, rotUp);
	this->forward=QVecMath::rotate(forward, right, rotRight);
	this->up=QVecMath::rotate(up, right, rotRight);
}
void QViewer::operator<<(QTextStream& textStream)
{
	textStream<<"v "<<viewDistance<<" "<<focalLength<<" "<<screenScale<<"\r\n";
	textStream<<"f "<<forward.x()<<" "<<forward.y()<<" "<<forward.z()<<"\r\n";
	textStream<<"r "<<right.x()<<" "<<right.y()<<" "<<right.z()<<"\r\n";
	textStream<<"u "<<up.x()<<" "<<up.y()<<" "<<up.z()<<"\r\n";
}
void QViewer::operator+=(QStringList& stringList)
{
	if(stringList.size()<4)return;
	if(stringList[0]=="v")
	{
		this->viewDistance=stringList[1].toDouble();
		this->focalLength=stringList[2].toDouble();
		this->screenScale=stringList[3].toDouble();
	}
	else
	{
		qreal x=stringList[1].toDouble();
		qreal y=stringList[2].toDouble();
		qreal z=stringList[3].toDouble();
		QVector3D vector=QVector3D(x, y, z);
		if(stringList[0]=="f")this->forward=vector;
		else if(stringList[0]=="u")this->up=vector;
		else if(stringList[0]=="r")this->right=vector;
	}
}
void QViewer::resize(QSize size)
{
	this->size=size;
	this->aspectRatio=(size.width()+0.0)/size.height();
}