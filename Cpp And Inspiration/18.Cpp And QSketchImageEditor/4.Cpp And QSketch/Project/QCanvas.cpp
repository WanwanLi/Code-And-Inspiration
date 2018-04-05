#include <QPainter>
#include "QCanvas.h"

QCanvas::QCanvas(QWidget* widget) : QWidget(widget)
{
	this->widget=widget;
	this->resize(widget->size());
	this->sketch.resize(this->size());
	this->bgColor=qRgb(255, 255, 255);
	this->image=QImage(size(), QImage::Format_RGB32);
	this->isImageModified=false; this->isMousePressed=false; this->clear();
	this->pen=QPen(Qt::green, 10, Qt::SolidLine, Qt::RoundCap, Qt::RoundJoin);
}
bool QCanvas::loadImage(QString fileName)
{
	if(sketch.load(fileName))
	{
		this->isImageModified=false; this->resize(sketch.size);
		this->resizeImage(); this->resizeWindow(20);
		this->update(); return true;
	}
	else if(image.load(fileName))
	{
		this->isImageModified=false;
		this->resize(image.size());
		this->sketch.resize(size());
		this->resizeWindow(20);
		this->sketch.update();
		return true;
	}
	else return false;
}
bool QCanvas::saveImage(QString fileName, const char* fileFormat)
{
	if(tr(fileFormat)=="sky")return this->sketch.save(fileName);
	else if(!image.save(fileName, fileFormat))return false;
	else this->isImageModified=false; return true;
}
void QCanvas::resizeWindow(int margin)
{
	this->widget->resize(QSize(image.width(), image.height()+margin));
}
void QCanvas::resizeCanvas()
{
	this->resizeImage(); this->sketch.resize(size()); this->sketch.update();
}
void QCanvas::resizeImage()
{
	this->image=image.scaled(size(), Qt::IgnoreAspectRatio, Qt::SmoothTransformation);
	this->isImageModified=false; this->image=image; this->update();
}
void QCanvas::clear()
{
	this->image.fill(bgColor);
	this->sketch.clear();
	this->update();
}
void QCanvas::paintEvent(QPaintEvent* event)
{
	QRect rect=event->rect();
	QPainter painter(this);
	painter.drawImage(rect, image, rect);
	painter.setPen(pen);
	painter.drawPath(sketch);
}
void QCanvas::mousePressEvent(QMouseEvent* event)
{
	if(event->button()==Qt::LeftButton) 
	{
		this->isMousePressed=true;
		this->sketch=event->pos();
		this->update();
	}
}
void QCanvas::mouseMoveEvent(QMouseEvent* event)
{
	if(isMousePressed)
	{
		this->isImageModified=true;
		this->sketch+=event->pos();
		this->update();
	}
}
void QCanvas::mouseReleaseEvent(QMouseEvent* event)
{
	if(isMousePressed)
	{
		this->isMousePressed=false;
		this->sketch+=event->pos();
		this->update();
	}
}
bool QCanvas::isModified()
{
	return this->isImageModified;
}
