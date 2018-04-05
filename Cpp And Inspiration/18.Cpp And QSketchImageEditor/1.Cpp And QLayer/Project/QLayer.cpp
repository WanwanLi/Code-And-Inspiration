#include "QLayer.h"

bool QLayer::load(QString fileName)
{
	if(!QImage::load(fileName))return false;
	this->ctrlPoints<<rect().center();
	this->ctrlPoints<<rect().topLeft();
	this->ctrlPoints<<rect().topRight();
	this->ctrlPoints<<rect().bottomLeft();
	this->ctrlPoints<<rect().bottomRight();
	return true;
}
QImage QLayer::image()
{
	QImage image=this->transformed(transformation);
	image.setOffset(translation);
	return image;
}
void QLayer::translate(QPainter& painter)
{
	painter.translate(translation);
}
void QLayer::reset(QPainter& painter)
{
	painter.translate(-translation);
}
bool inRange(int x, int min, int max)
{
	return min<=x&&x<=max;
}
bool inRange(QPoint point, QPoint start, QPoint end)
{
	bool xIsInRange=inRange(point.x(), start.x(), end.x());
	bool yIsInRange=inRange(point.y(), start.y(), end.y());
	return xIsInRange&&yIsInRange;
}
bool QLayer::contains(QPoint point)
{
	QPoint start=rect().topLeft()+translation;
	QPoint end=rect().bottomRight()+translation;
	return inRange(point, start, end);
}

