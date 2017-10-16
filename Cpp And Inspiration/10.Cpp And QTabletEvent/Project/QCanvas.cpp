#include "QCanvas.h"

QCanvas::QCanvas(QWidget* widget) : QWidget(widget)
{
	this->widget=widget;
	this->minLineWidth=2;
	this->maxLineWidth=10;
	this->resize(widget->size());
	this->pen=QPen(Qt::blue, 5, Qt::SolidLine, Qt::RoundCap, Qt::RoundJoin);
	this->image=new QImage(size(), QImage::Format_RGB32);
	this->bgColor=qRgb(255, 255, 255);
	this->isImageModified=false;
	this->isMousePressed=false;	
	this->isTabletPressed=false;	
	this->clearImage();
}
bool QCanvas::loadImage(QString fileName)
{
	if(!image->load(fileName))return false;
	this->resize(image->size());
	this->resizeWindow(20);
	return true;
}
bool QCanvas::saveImage(QString fileName, const char* fileFormat)
{
	if(!image->save(fileName, fileFormat))return false;
	this->isImageModified=false;
        	return true;
}
void QCanvas::resizeWindow(int margin)
{
	this->widget->resize(QSize(image->width(), image->height()+margin));
}
void QCanvas::resizeImage()
{
	QImage* image=new QImage(size(), QImage::Format_RGB32);
	QPainter painter(image);
	painter.drawImage(QPoint(0, 0), this->image->scaled(size(),Qt::IgnoreAspectRatio,Qt::SmoothTransformation));
	this->isImageModified=true;
	this->image=image;
	this->update();
}
void QCanvas::clearImage()
{
	this->image->fill(bgColor);
	this->update();
}
void QCanvas::paintEvent(QPaintEvent* event)
{
	QRect rect=event->rect();
	QPainter painter(this);
	painter.drawImage(rect,* image, rect);
}
void QCanvas::mouseMoveEvent(QMouseEvent* event)
{
	if(isMousePressed)
	{
		this->drawLine(event->pos());
	}
}
void QCanvas::mousePressEvent(QMouseEvent* event)
{
	if(isTabletPressed)return;
	if(event->button()==Qt::LeftButton) 
	{
		this->pen.setWidth(5);
		this->pen.setColor(Qt::red);
		this->point=event->pos();
		this->isMousePressed=true;
	}
}
void QCanvas::mouseReleaseEvent(QMouseEvent* event)
{
	if(isMousePressed)
	{
		this->drawLine(event->pos());
        		this->isMousePressed=false;
	}
}
void QCanvas::tabletEvent(QTabletEvent* event)
{
	switch(event->type())
	{
		case QEvent::TabletMove: tabletMoveEvent(event); break;
		case QEvent::TabletPress: tabletPressEvent(event); break;
		case QEvent::TabletRelease: tabletReleaseEvent(event); break;
	}
	event->accept();
}
void QCanvas::tabletPressEvent(QTabletEvent* event)
{
	this->pen.setWidth(lineWidth(event));
	this->pen.setColor(Qt::blue);
	this->point=event->pos();
	this->isTabletPressed=true;
}
void QCanvas::tabletMoveEvent(QTabletEvent* event)
{
	if(isTabletPressed)
	{
		this->pen.setWidth(lineWidth(event));
		this->drawLine(event->pos());
	}
}
void QCanvas::tabletReleaseEvent(QTabletEvent* event)
{
	if(isTabletPressed)
	{
		this->pen.setWidth(lineWidth(event));
		this->drawLine(event->pos());
        		this->isTabletPressed=false;
	}
}
void QCanvas::drawLine(QPoint point)
{
	QPainter painter(image);
	painter.setPen(pen);
	painter.drawLine(this->point, point);
	this->isImageModified=true;
	this->point=point;
	this->update();
}
int QCanvas::lineWidth(QTabletEvent* event)
{
	return minLineWidth+(int)((maxLineWidth-minLineWidth)*event->pressure());
}
bool QCanvas::isModified()
{
	return this->isImageModified;
}
