#include <QPainter>
#include "QCanvas.h"
#include "QOptimizer.h"
#define cross QVector3D::crossProduct

QCanvas::QCanvas(QWidget* widget) : QWidget(widget)
{
	this->widget=widget;
	this->resize(widget->size());
	this->sketch=QSketch(this);
	this->translateSpeed=0.5;
	this->rotateSpeed=0.4;
	this->viewDistance=1;
	this->right.setX(1);
	this->up.setY(1); 
	this->forward.setZ(-1);
	this->basicTimer.start(15, this);
	this->updateViewDistance();
	this->updateViewDirection();
	this->bgColor=qRgb(255, 255, 255);
	this->colors<<Qt::blue<<Qt::green<<Qt::red;
	this->colors<<Qt::magenta<<Qt::yellow<<Qt::cyan<<Qt::gray; 
	this->colors<<Qt::black<<Qt::darkBlue<<Qt::darkGreen<<Qt::darkRed;
	this->colors<<Qt::darkMagenta<<Qt::darkYellow<<Qt::darkCyan<<Qt::darkGray;
	this->marker=QPen(Qt::red, 2, Qt::SolidLine, Qt::RoundCap, Qt::RoundJoin);
	this->pen=QPen(Qt::blue, 10, Qt::SolidLine, Qt::RoundCap, Qt::RoundJoin);
	this->image=QImage(size(), QImage::Format_RGB32);
	this->isUpdated=true;
	this->clearImage();
	this->setFocus();
}
bool QCanvas::loadImage(QString fileName)
{
	if(image.load(fileName))
	{
		this->resize(image.size());
		this->resizeWindow(20);
		this->isUpdated=true;
		return true;
	}
	else if(sketch.load(fileName))
	{
		this->resize(sketch.size());
		this->image=QImage(size(), QImage::Format_RGB32);
		this->image.fill(bgColor);
		this->resizeWindow(20);
		this->updateViewInfo();
		this->isUpdated=true;
		return true;
	}
	else return false;
}
bool QCanvas::saveImage(QString fileName, const char* fileFormat)
{
	if(tr(fileFormat)=="fsk")return this->sketch.save(fileName);
	if(!image.save(fileName, fileFormat))return false;
	return true;
}
void QCanvas::resizeWindow(int margin)
{
	this->widget->resize(QSize(image.width(), image.height()+margin));
}
void QCanvas::resizeImage()
{
	QImage image=QImage(size(), QImage::Format_RGB32);
	QPainter painter(&image);
	painter.drawImage(QPoint(0, 0), this->image.scaled(size(),Qt::IgnoreAspectRatio,Qt::SmoothTransformation));
	this->image=image;
	this->isUpdated=true;
}
void QCanvas::clear()
{
	this->clearImage();
	this->isUpdated=true;
}
void QMinimizer::run()
{
	qDebug()<<"min-start: ";
/*
	while(1)
	{
	if((++this->timer%100000000)==0)
	qDebug()<<"thread : "<<this->timer;
	}
*/
	this->sketch->minimizeEnergy();
	qDebug()<<"min-end: ";
}
/*
	this->minimizer->sketch=&this->sketch;
	this->minimizer->start();
*/
void QCanvas::minimizeEnergy()
{
	if(!sketch.isMinimizingEnergy)
	{
		this->sketch.isMinimizingEnergy=true;
		this->sketch.optimizer->iterations=0;
	}
	this->image.fill(bgColor);
	this->sketch.minimizeEnergy();
	if(isCameraUpward)this->rotateViewDirection();
	else this->updateViewDirection();
	this->isUpdated=true;
}
void QCanvas::perturbPlanes()
{
	this->image.fill(bgColor);
	this->sketch.perturbPlanes(0.01);
	this->isUpdated=true;
}
void QCanvas::createRelations()
{
	this->sketch.createRelations();
	this->isUpdated=true;
}
void QCanvas::clearImage()
{
	this->image.fill(bgColor);
	this->sketch.clear();
	this->isUpdated=true;
}
void QCanvas::paintEvent(QPaintEvent* event)
{
	QRect rect=event->rect();
	QPainter painter(this);
	painter.drawImage(rect,image, rect);
}
void QCanvas::mouseMoveEvent(QMouseEvent* event)
{
	if(isMousePressed)
	{
		this->mouseMove=event->pos()-mousePos;
		this->mousePos=event->pos();
		if(isCameraUpward)this->rotateViewDirection();
		else this->updateViewDirection();
		this->isUpdated=true;
	}
}
void QCanvas::mousePressEvent(QMouseEvent* event)
{
	if(event->button()==Qt::LeftButton) 
	{
		this->isUpdated=true;
		this->isMousePressed=true;
		this->mousePos=event->pos();

	}
}
void QCanvas::mouseReleaseEvent(QMouseEvent* event)
{
	if(isMousePressed)
	{
        		this->isMousePressed=false;
	}
}
void QCanvas::wheelEvent(QWheelEvent* event)
{
	this->mouseDelta=event->delta();
	this->updateViewDistance();
	this->mouseDelta=0;
	this->isUpdated=true;
}
int timer=0;
void QCanvas::timerEvent(QTimerEvent* event)
{


	if(sketch.isMinimizingEnergy)
{
	this->minimizeEnergy();
if(timer++%10==0)qDebug()<<timer;
}
	if(!isUpdated)return;
	this->image.fill(bgColor);
	this->sketch.update();
	this->drawSketch();
	this->update();
	this->isUpdated=false;
}
void QCanvas::keyPressEvent(QKeyEvent* event)
{
	qreal inc=event->key()==Qt::Key_Left?-1:
	(event->key()==Qt::Key_Right?1:0);
	this->matchIndex+=inc;
	this->isUpdated=true;
}
void QCanvas::drawSketch()
{
	QPainter painter(&image);
	for(int i=0; i<sketch.length(); i++)
	{
		pen.setColor(color(i));
		painter.setPen(pen);
		painter.drawPath(sketch[i]);
	}
	painter.setPen(marker);
	sketch.drawMarkers(painter);
	//sketch.drawMatchPoints(painter, matchIndex);
}
QColor QCanvas::color(int index)
{
	return this->colors[index%(colors.size()-1)];
}
bool QCanvas::isModified()
{
	return false;
}
void QCanvas::setCameraUpward()
{
	this->isCameraUpward=!isCameraUpward;
}
void QCanvas::updateViewInfo()
{
	QVector3D* viewDirection=sketch.getViewDirection();
	this->right=viewDirection[0]; 
	this->up=viewDirection[1];
	this->forward=viewDirection[2]; 
	this->viewDistance=sketch.getViewDistance();
}
void QCanvas::updateViewDirection()
{
	qreal rotUp=-rotateSpeed*mouseMove.x();
	qreal rotRight=-rotateSpeed*mouseMove.y();
	this->forward=rotate(forward, up, rotUp);
	this->right=rotate(right, up, rotUp);
	this->forward=rotate(forward, right, rotRight);
	this->up=rotate(up, right, rotRight);
	this->sketch.setViewDirection(right, up, forward);
}
void QCanvas::rotateViewDirection()
{
	qreal rotUp=-rotateSpeed*mouseMove.x();
	qreal rotRight=-rotateSpeed*mouseMove.y();
	this->up=QVector3D(0, 1, 0);
	this->forward=rotate(forward, up, rotUp);
	this->right=rotate(right, up, rotUp);
	this->forward=rotate(forward, right, rotRight);
	this->up=cross(right, forward);
	this->sketch.setViewDirection(right, up, forward);
}
void QCanvas::updateViewDistance()
{
	this->viewDistance-=translateSpeed*mouseDelta/120;
	this->viewDistance=clamp(viewDistance, 8, 18);
	this->sketch.setViewDistance(viewDistance);
}
QVector3D QCanvas::rotate(QVector3D vector, QVector3D axis, qreal angle)
{
	rotateMatrix.setToIdentity();
	rotateMatrix.rotate(angle, axis.x(), axis.y(), axis.z());
	return rotateMatrix.map(vector);
}
qreal QCanvas::clamp(qreal value, qreal min, qreal max)
{
	return value<min?min:value>max?max:value;
}
