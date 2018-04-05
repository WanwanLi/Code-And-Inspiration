#include <QPainter>
#include "QCanvas.h"

QCanvas::QCanvas(QWidget* widget) : QWidget(widget)
{
	this->widget=widget;
	this->resize(widget->size());
	this->sketch.resize(size());
	this->pen=QPen(Qt::blue, 10, Qt::SolidLine, Qt::RoundCap, Qt::RoundJoin);
	this->image=new QImage(size(), QImage::Format_RGB32);
	this->bgColor=qRgb(255, 255, 255);
	this->basicTimer.start(255, this);
	this->isImageModified=false;
	this->clearImage();
	this->setFocus();
}
bool QCanvas::loadImage(QString fileName)
{
	if(image->load(fileName))
	{
		this->isImageModified=false;
		this->resize(image->size());
		this->resizeWindow(20);
		return true;
	}
	else if(sketch.load(fileName))
	{
		this->isImageModified=false;
		this->resize(sketch.size);
		this->image=new QImage(size(), QImage::Format_RGB32);
		this->clearImage();
		this->resizeWindow(20);
		QPainter painter(image);
		painter.setPen(pen);
		painter.drawPath(sketch);
		this->update();
		return true;
	}
	else return false;
}
bool QCanvas::saveImage(QString fileName, const char* fileFormat)
{
	if(tr(fileFormat)=="sky")return this->sketch.save(fileName);
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
	this->sketch.resize(size());
	this->image=image;
	this->update();
}
void QCanvas::clear()
{
	this->clearImage();
	this->sketch.clear();
}
void QCanvas::undo()
{
	this->sketch.strokes--;
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
	painter.drawImage(rect,*image, rect);
}
void QCanvas::timerEvent(QTimerEvent* event)
{
	this->sketch.update();
	this->image->fill(bgColor);
	QPainter painter(image);
	painter.setPen(pen);
	painter.drawPath(sketch);
	this->update();
}
void QCanvas::wheelEvent(QWheelEvent* event)
{
	this->sketch.viewer.updateViewDistance(-event->delta()/120);
}
void QCanvas::mousePressEvent(QMouseEvent* event)
{
	if(event->button()==Qt::LeftButton) 
	{
		this->point=event->pos();
		this->isLeftButtonPressed=true;
		if(sketch.planes.isOnCreating)
		this->sketch.planes=event->pos();
		else this->sketch.strokes=event->pos();
	}
	else if(event->button()==Qt::RightButton) 
	{
		this->isRightButtonPressed=true;
		this->position=event->pos();
	}
}
void QCanvas::mouseMoveEvent(QMouseEvent* event)
{
	if(isLeftButtonPressed)
	{
		this->drawLine(event->pos());
		if(sketch.planes.isOnCreating)
		this->sketch.planes<<=event->pos();
		else this->sketch.strokes+=event->pos();
	}
	else if(isRightButtonPressed)
	{
		this->sketch.viewer.updateViewDirection
		(
			position-event->pos()
		);
		this->position=event->pos();
	}
}
void QCanvas::mouseReleaseEvent(QMouseEvent* event)
{
	if(isLeftButtonPressed)
	{
		this->drawLine(event->pos());
        		this->isLeftButtonPressed=false;
		if(sketch.planes.isOnCreating)
		this->sketch.planes.finish(sketch.viewer);
		else this->sketch.strokes.update();
	}
	else if(isRightButtonPressed)
	{
        		this->isRightButtonPressed=false;
	}
}
void QCanvas::keyPressEvent(QKeyEvent* event)
{
	if(event->key()==Qt::Key_Z)
	{
		this->sketch.viewer.right=QVector3D(1, 0, 0);
		this->sketch.viewer.up=QVector3D(0, 1, 0);
		this->sketch.viewer.forward=QVector3D(0, 0, -1);
	}
	else if(event->key()==Qt::Key_X)
	{
		this->sketch.viewer.right=QVector3D(0, 0, -1);
		this->sketch.viewer.up=QVector3D(0, 1, 0);
		this->sketch.viewer.forward=QVector3D(-1, 0, 0);
	}
	else if(event->key()==Qt::Key_Y)
	{
		this->sketch.viewer.right=QVector3D(1, 0, 0);
		this->sketch.viewer.up=QVector3D(0, 0, -1);
		this->sketch.viewer.forward=QVector3D(0, -1, 0);
	}
	else if(event->key()==Qt::Key_P)
	{
		this->sketch.planes.create();
	}
	else if(event->key()==Qt::Key_R)
	{
		this->sketch.planes.reset();
	}
	else if(event->key()==Qt::Key_S)
	{
		this->sketch.planes.show();
	}
	else if(event->key()==Qt::Key_D)
	{
		this->sketch.planes.removeLast();
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
bool QCanvas::isModified()
{
	return this->isImageModified;
}
