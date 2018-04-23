#include <QPainter>
#include "QCanvas.h"
#include "QOpenGLWindow.h"

QCanvas::QCanvas(QWidget* widget) : QWidget(widget)
{
	this->widget=widget;
	this->resize(widget->size());
	this->sketch.resize(size());
	this->bgColor=Qt::white;
	this->colors<<Qt::blue<<Qt::green<<Qt::red;
	this->colors<<Qt::magenta<<Qt::yellow<<Qt::cyan<<Qt::gray; 
	this->colors<<Qt::black<<Qt::darkBlue<<Qt::darkGreen<<Qt::darkRed;
	this->colors<<Qt::darkMagenta<<Qt::darkYellow<<Qt::darkCyan<<Qt::darkGray;
	this->pen=QPen(Qt::blue, 10, Qt::SolidLine, Qt::RoundCap, Qt::RoundJoin);
	this->image=QImage(size(), QImage::Format_RGB32);
	this->basicTimer.start(255, this);
	this->isMinusKeyPressed=false;
	this->isImageModified=false;
	this->isUpdated=true;
	this->clearImage();
	this->setFocus();
}
bool QCanvas::loadImage(QString fileName)
{
	if(image.load(fileName))
	{
		this->isImageModified=false;
		this->resize(image.size());
		this->resizeWindow(20);
		this->isUpdated=true;
		return true;
	}
	else if(sketch.load(fileName))
	{
		this->isImageModified=false;
		this->resize(sketch.size);
		this->image=QImage(size(), QImage::Format_RGB32);
		this->resizeWindow(20);
		this->isUpdated=true;
		this->clearImage();
		return true;
	}
	else return false;
}
bool QCanvas::saveImage(QString fileName, const char* fileFormat)
{
	if(tr(fileFormat)=="sky")return this->sketch.save(fileName);
	if(!image.save(fileName, fileFormat))return false;
	this->isImageModified=false;
        	return true;
}
void QCanvas::resizeWindow(int margin)
{
	this->widget->resize(QSize(image.width(), image.height()+margin));
}
void QCanvas::resizeImage()
{
	QImage* image=new QImage(size(), QImage::Format_RGB32);
	QPainter painter(image);
	painter.drawImage(QPoint(0, 0), this->image.scaled(size(),Qt::IgnoreAspectRatio,Qt::SmoothTransformation));
	this->isImageModified=true;
	this->sketch.resize(size());
	this->image=*image;
	this->update();
}
void QCanvas::visualize()
{
	QMesh mesh(this->sketch);
	QOpenGLWindow* window=new QOpenGLWindow(mesh.quads, mesh.coords);
	window->resize(400, 300); 
	window->show();
}
void QCanvas::clear()
{
	this->clearImage();
	this->sketch.clear();
	this->isUpdated=true;
}
void QCanvas::undo()
{
	this->sketch.strokes--;
	this->isUpdated=true;
}
void QCanvas::clearImage()
{
	this->image.fill(bgColor);
	this->update();
}
void QCanvas::paintEvent(QPaintEvent* event)
{
	QRect rect=event->rect();
	QPainter painter(this);
	painter.drawImage(rect, image, rect);
}
void QCanvas::timerEvent(QTimerEvent* event)
{
	if(!isUpdated)return;
	this->image.fill(bgColor);
	this->sketch.update();
	this->drawSketch();
	this->update();
	this->isUpdated=false;
}
void QCanvas::wheelEvent(QWheelEvent* event)
{
	this->sketch.viewer.updateViewDistance(-event->delta()/120);
	this->isUpdated=true;
}
void QCanvas::mousePressEvent(QMouseEvent* event)
{
	if(event->button()==Qt::LeftButton) 
	{
		this->point=event->pos();
		this->isLeftButtonPressed=true;
		if(sketch.planes.isOnCreating)
		this->sketch.planes=event->pos();
		else if(sketch.shapes.isOnCreating)
		this->sketch.shapes=event->pos();
		else this->sketch.strokes=event->pos();
	}
	else if(event->button()==Qt::RightButton) 
	{
		this->isRightButtonPressed=true;
		this->position=event->pos();
	}
	this->isUpdated=true;
}
void QCanvas::mouseMoveEvent(QMouseEvent* event)
{
	if(isLeftButtonPressed)
	{
		this->drawLine(event->pos());
		if(sketch.planes.isOnCreating)
		this->sketch.planes<<=event->pos();
		else if(sketch.shapes.isOnCreating)
		this->sketch.shapes+=event->pos();
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
	this->isUpdated=true;
}
void QCanvas::mouseReleaseEvent(QMouseEvent* event)
{
	if(isLeftButtonPressed)
	{
		this->drawLine(event->pos());
        		this->isLeftButtonPressed=false;
		if(sketch.planes.isOnCreating)
		this->sketch.planes.finish(sketch.viewer);
		else if(sketch.shapes.isOnCreating)
		this->sketch.shapes.finish(sketch.strokes);
		else this->sketch.strokes.update();
	}
	else if(isRightButtonPressed)
	{
        		this->isRightButtonPressed=false;
	}
	this->isUpdated=true;
}
void QCanvas::keyPressEvent(QKeyEvent* event)
{
	if(isMinusKeyPressed)minusKeyPressEvent(event);
	else if(event->key()==Qt::Key_Z)
	{
		this->sketch.viewer.right=QVector3D(1, 0, 0);
		this->sketch.viewer.up=QVector3D(0, 1, 0);
		this->sketch.viewer.forward=QVector3D(0, 0, -1);
	}
	else if(event->key()==Qt::Key_X)
	{
		if(sketch.shapes.isSymmetric)
		{
			this->sketch.shapes.addSymmetry
			(
				sketch.strokes, 
				QVector2D	
				(
					size().width()/2, 
					size().height()/2
				),
				QVector2D(1, 0)
			);
		}
		else
		{
			this->sketch.viewer.right=QVector3D(0, 0, -1);
			this->sketch.viewer.up=QVector3D(0, 1, 0);
			this->sketch.viewer.forward=QVector3D(-1, 0, 0);
		}
	}
	else if(event->key()==Qt::Key_Y)
	{
		if(sketch.shapes.isSymmetric)
		{
			this->sketch.shapes.addSymmetry
			(
				sketch.strokes, 
				QVector2D	
				(
					size().width()/2, 
					size().height()/2
				),
				QVector2D(0, 1)
			);
		}
		else
		{
			this->sketch.viewer.right=QVector3D(1, 0, 0);
			this->sketch.viewer.up=QVector3D(0, 0, -1);
			this->sketch.viewer.forward=QVector3D(0, -1, 0);
		}
	}
	else if(event->key()==Qt::Key_P)
	{
		if(sketch.shapes.isOnCreating)return;
		this->sketch.planes.create();
	}
	else if(event->key()==Qt::Key_G)
	{
		this->sketch.planes.reset();
	}
	else if(event->key()==Qt::Key_S)
	{
		if(!sketch.strokes.isSymmetric)
		{
			this->bgColor=qRgb(225, 255, 225);
			this->sketch.strokes.isSymmetric=true;
			this->sketch.shapes.symmetryIndex=sketch.strokes.size();
		}
		else 
		{
			this->bgColor=Qt::white;
			this->sketch.shapes.isSymmetric=true;
			this->sketch.strokes.isSymmetric=false;
		}
	}
	else if(event->key()==Qt::Key_D)
	{
		this->sketch.planes.removeLast();
	}
	else if(event->key()==Qt::Key_U)
	{
		this->sketch.planes.undo();
	}
	else if(event->key()==Qt::Key_R)
	{
		if(sketch.planes.isOnCreating)return;
		this->sketch.shapes.create(QShapes::RECT);
	}
	else if(event->key()==Qt::Key_E)
	{
		if(sketch.planes.isOnCreating)return;
		this->sketch.shapes.create(QShapes::ELLIPSE);
	}
	else if(event->key()==Qt::Key_O)
	{
		if(sketch.planes.isOnCreating)return;
		this->sketch.shapes.create(QShapes::POLYGON);
	}
	else if(event->key()==Qt::Key_Minus)
	{
		this->isMinusKeyPressed=true;
	}
	this->isUpdated=true;
}
void QCanvas::minusKeyPressEvent(QKeyEvent* event)
{
	if(event->key()==Qt::Key_Z)
	{
		this->sketch.viewer.right=QVector3D(-1, 0, 0);
		this->sketch.viewer.up=QVector3D(0, 1, 0);
		this->sketch.viewer.forward=QVector3D(0, 0, 1);
	}
	else if(event->key()==Qt::Key_X)
	{
		this->sketch.viewer.right=QVector3D(0, 0, 1);
		this->sketch.viewer.up=QVector3D(0, 1, 0);
		this->sketch.viewer.forward=QVector3D(1, 0, 0);
	}
	else if(event->key()==Qt::Key_Y)
	{
		this->sketch.viewer.right=QVector3D(1, 0, 0);
		this->sketch.viewer.up=QVector3D(0, 0, 1);
		this->sketch.viewer.forward=QVector3D(0, 1, 0);
	}
	else if(event->key()==Qt::Key_G)
	{
		this->sketch.strokes.extrudePlanes(-1, 0);
	}
	else if(event->key()==Qt::Key_S)
	{
		this->sketch.planes.show();
	}
	this->isMinusKeyPressed=false;
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
}
void QCanvas::depth()
{
	this->sketch.strokes.updateDepthTest();
	this->isUpdated=true;
}
void QCanvas::drawLine(QPoint point)
{
	QPainter painter(&image);
	if(sketch.planes.isOnCreating)pen.setColor(color(0));
	else if(sketch.shapes.isOnCreating)pen.setColor(color(0));
	else pen.setColor(color(sketch.planes.index));
	painter.setPen(pen);
	painter.drawLine(this->point, point);
	this->isImageModified=true;
	this->isUpdated=true;
	this->point=point;
	this->update();
}
QColor QCanvas::color(int index)
{
	return this->colors[index%(colors.size()-1)];
}
bool QCanvas::isModified()
{
	return this->isImageModified;
}
