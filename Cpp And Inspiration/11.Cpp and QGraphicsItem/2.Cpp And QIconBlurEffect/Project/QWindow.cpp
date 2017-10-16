#include "QWindow.h"
#include <QtCore/qmath.h>
#include <QGraphicsBlurEffect>

QWindow::QWindow(QWidget* widget) : QGraphicsView(widget)
{
	this->setFocus();
	this->setFixedSize(w, h);
	this->setFrameStyle(QFrame::NoFrame);
	this->setScene(newQGraphicsScene(0, 0, w, h));
	this->setRenderHint(QPainter::Antialiasing, true);
	this->setBackgroundBrush(QPixmap("background.jpg"));
	this->propertyAnimation=new QPropertyAnimation(this, "frame");
	this->propertyAnimation->setEasingCurve(QEasingCurve::InOutSine);
	this->propertyAnimation->setDuration(400);
 	this->updateScene(0); 
}
QGraphicsScene* QWindow::newQGraphicsScene(qreal x, qreal y, qreal w, qreal h)
{
	QGraphicsScene* graphicsScene=new QGraphicsScene(x, y, w, h);
	QStringList imageList=getImageList("images");
	QGraphicsPixmapItem* pixmapItem;
	QGraphicsBlurEffect* blurEffect;
	for (int i = 0; i < imageList.count(); i++) 
	{
		QPixmap pixmap(imageList[i]);
		pixmapItem = graphicsScene->addPixmap(pixmap);
		blurEffect=new QGraphicsBlurEffect();
		blurEffect->setBlurRadius(0);
		pixmapItem->setGraphicsEffect(blurEffect);
		pixmapList<<pixmapItem;
	}
	return graphicsScene;
}
QStringList QWindow::getImageList(QString dir)
{
	QStringList imageList;
	imageList<<dir+"/accessories-calculator.png";
	imageList<<dir+"/accessories-text-editor.png";
	imageList<<dir+"/help-browser.png";
	imageList<<dir+"/internet-group-chat.png";
	imageList<<dir+"/internet-mail.png";
	imageList<<dir+"/internet-web-browser.png";
	imageList<<dir+"/office-calendar.png";
	imageList<<dir+"/system-users.png";
	return imageList;
}
void QWindow::updateScene(qreal index)
{
	this->index=index;
	for (int i = 0; i < pixmapList.count(); ++i) 
	{
		qreal a=(i-index)*(2*M_PI/pixmapList.count());
		QPointF pos(s*w/2*qCos(a), s*h/2*qSin(a)); 
		pos=QTransform().rotate(-20).map(pos);
		pos+=QPointF(w/2+dx, h/2+dy);
		pixmapList[i]->setPos(pos);
		qreal r=(0.8-pos.y()/h)*10;
		QGraphicsEffect* blurEffect;
		blurEffect=pixmapList[i]->graphicsEffect();
		((QGraphicsBlurEffect*)blurEffect)->setBlurRadius(r);
	}
	scene()->update();
}
const qreal QWindow::frame()
{
	return index;
}
void QWindow::mousePressEvent(QMouseEvent* event)
{
	if(propertyAnimation->state() == QAbstractAnimation::Stopped) 
	{
		qreal endValue=index+(event->x()>w/2?1:-1);
		this->propertyAnimation->setEndValue(endValue);
		this->propertyAnimation->start();
	}
}
void QWindow::keyPressEvent(QKeyEvent* event)
{
	if(propertyAnimation->state() == QAbstractAnimation::Stopped) 
	{
		qreal inc=event->key()==Qt::Key_Left
		?-1:(event->key()==Qt::Key_Right?1:0);
		this->propertyAnimation->setEndValue(index+inc);
		this->propertyAnimation->start();
	}
}
