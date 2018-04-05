#include <QPainter>
#include <QDebug>
#include "QCanvas.h"

QCanvas::QCanvas(QWidget* widget) : QWidget(widget)
{
	this->lineWidth=3;
	this->widget=widget;
	this->color=Qt::green;
	this->resize(widget->size());
	this->pen=QPen(color, lineWidth, Qt::SolidLine, Qt::RoundCap, Qt::RoundJoin);
	this->canvas=new QImage(size(), QImage::Format_RGB32);
	this->background=QImage(size(), QImage::Format_RGB32);
	this->bgColor=qRgb(255, 255, 255);
	this->isImageModified=false;
	this->selectedLayer=NULL;
	this->clearImage();
}
bool QCanvas::loadImage(QString fileName)
{
	QLayer layer;
	if(!layer.load(fileName))return false;
	this->selectedLayer=NULL;
	this->layers<<layer;
	return true;
}
bool QCanvas::saveImage(QString fileName, const char* fileFormat)
{
	 return true;
}
void QCanvas::resizeWindow(int margin)
{
	int w=background.width();
	int h=background.height()+margin;
	this->widget->resize(QSize(w, h));
}
void QCanvas::resizeImage()
{
	this->background.fill(bgColor);
	this->background.scaled(size(), Qt::IgnoreAspectRatio,Qt::SmoothTransformation);
	this->update();
}
void QCanvas::clearImage()
{
	this->background.fill(bgColor);
	this->layers.clear();
	this->update();
}
void QCanvas::drawEditingTools(QPainter& painter)
{
	if(selectedLayer)
	{
		selectedLayer->translate(painter);
		painter.drawRect(selectedLayer->rect());
		for(QPoint point : selectedLayer->ctrlPoints)
		painter.drawEllipse(point, minDistance, minDistance);
		selectedLayer->reset(painter);
	}
}
void QCanvas::paintEvent(QPaintEvent* event)
{
	QRect rect=event->rect();
	QPainter canvasPainter(canvas);
	canvasPainter.drawImage(rect, background, rect);
	for(QLayer layer: layers)
	{
		layer.translate(canvasPainter);
		canvasPainter.drawImage(rect, layer.image(), rect);
		layer.reset(canvasPainter);
	}
	QPainter painter(this);
	painter.drawImage(rect, *canvas, rect);
	painter.setPen(pen);
	this->drawEditingTools(painter);
}
void QCanvas::mouseMoveEvent(QMouseEvent* event)
{
}
void QCanvas::mousePressEvent(QMouseEvent* event)
{
	if(event->button()==Qt::LeftButton) 
	{
		this->point=event->pos();
		this->isMousePressed=true;
	}
}
bool QCanvas::isClose(QPoint p0, QPoint p1)
{
	return QVector2D(p0).distanceToPoint(QVector2D(p1))<minDistance;
}
void QCanvas::mouseReleaseEvent(QMouseEvent* event)
{
	if(isClose(event->pos(), point))
	{
		this->selectedLayer=layerOf(point);
		this->update();
	}
}
QLayer* QCanvas::layerOf(QPoint point)
{
	QLayer* topLayer=NULL;
	for(QLayer& layer : layers)
	{
		if(layer.contains(point))
		topLayer=&layer;
	}
	return topLayer;
}
void QCanvas::drawLine(QPoint point)
{
	QPainter painter(canvas);
	painter.setPen(pen);
	painter.drawLine(this->point, point);
	this->point=point;
	this->update();
}
bool QCanvas::isModified()
{
	return this->isImageModified;
}
