#include "QWindow.h"
#include <QGraphicsTextItem>
#include <QGraphicsLineItem>
#include <QGraphicsRectItem>
#include <QGraphicsPathItem>
#include <QGraphicsEllipseItem>
#include <QGraphicsPixmapItem>
#include <QGraphicsPolygonItem>

QWindow::QWindow(QWidget* widget) : QGraphicsView(widget)
{
	this->setFixedSize(w, h);
	this->graphicsScene=new QGraphicsScene(0, 0, w, h);
	this->graphicsScene->addItem(newQGraphicsItem("Rect", QPen(Qt::black, 1), QBrush(Qt::white), 8, 10, 10, 80, 80, 50, 50));
	this->graphicsScene->addItem(newQGraphicsItem("Ellipse", QPen(Qt::blue, 2), QBrush(Qt::yellow), 5, 10, 10, 180, 80, 50, 50));
	this->graphicsScene->addItem(newQGraphicsItem("Line", QPen(Qt::white, 4), QBrush(Qt::white), 6, 20, 10, 280, 80, 50, 50));
	this->graphicsScene->addItem(newQGraphicsPolygonItem(QPen(Qt::white, 4), QBrush(Qt::black), 10, 10, 10, 380, 80));
	this->graphicsScene->addItem(newQGraphicsPathItem(QPen(Qt::cyan, 2), QBrush(Qt::green), 10, 10, 10, 480, 80));
	this->graphicsScene->addItem(newQGraphicsTextItem("Text", QPen(Qt::gray, 100), 10, 10, 10, 100, 180));
	this->graphicsScene->addItem(newQGraphicsPixmapItem("image", "gif", 15, 10, 10, 340, 200));
	this->setBackgroundBrush(*newQLinearGradient(Qt::red, Qt::black, 0, 0, w, h));
	this->setRenderHint(QPainter::Antialiasing, true);
	this->setFrameStyle(QFrame::NoFrame);
	this->setScene(graphicsScene);
}
QLinearGradient* QWindow::newQLinearGradient(QColor c1, QColor c2, qreal x1, qreal y1, qreal x2, qreal y2)
{
	QLinearGradient* linearGrad=new QLinearGradient(x1, y1, x2, y2);
	linearGrad->setColorAt(0, c1);
	linearGrad->setColorAt(1, c2);
	return linearGrad;
}
QGraphicsItem* QWindow::newQGraphicsItem(QString shape, QPen pen, QBrush brush, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y, qreal w, qreal h)
{
	QGraphicsItem* graphicsItem;
	if(shape=="Ellipse")graphicsItem=new QGraphicsEllipseItem(0, 0, w, h);
	else if(shape=="Line")graphicsItem=new QGraphicsLineItem(0, 0, w, h);
	else if(shape=="Rect")graphicsItem=new QGraphicsRectItem(0, 0, w, h);
	else if(shape=="Polygon")graphicsItem=new QGraphicsPolygonItem(0);
	else if(shape=="Pixmap")graphicsItem=new QGraphicsPixmapItem(0);
	else if(shape=="Path")graphicsItem=new QGraphicsPathItem(0);
	else if(shape=="Text")graphicsItem=new QGraphicsTextItem(0);
	else graphicsItem=new QGraphicsPolygonItem(0);
	QGraphicsDropShadowEffect* graphicsDropShadowEffect;
	graphicsDropShadowEffect = new QGraphicsDropShadowEffect();
	graphicsDropShadowEffect->setBlurRadius(blurRadius);
	graphicsDropShadowEffect->setOffset(dx, dy);
	graphicsDropShadowEffect->setColor(QColor(10, 10, 10));
	graphicsItem->setGraphicsEffect(graphicsDropShadowEffect);
	if(shape=="Line")((QGraphicsLineItem*)graphicsItem)->setPen(pen);
	else if(shape=="Rect")
	{
		((QGraphicsRectItem*)graphicsItem)->setPen(pen);
		((QGraphicsRectItem*)graphicsItem)->setBrush(brush);
	}
	else if(shape=="Path")
	{
		((QGraphicsPathItem*)graphicsItem)->setPen(pen);
		((QGraphicsPathItem*)graphicsItem)->setBrush(brush);
	}
	else if(shape=="Ellipse")
	{
		((QGraphicsEllipseItem*)graphicsItem)->setPen(pen);
		((QGraphicsEllipseItem*)graphicsItem)->setBrush(brush);
	}
	else if(shape=="Polygon")
	{
		((QGraphicsPolygonItem*)graphicsItem)->setPen(pen);
		((QGraphicsPolygonItem*)graphicsItem)->setBrush(brush);
	}
	graphicsItem->setPos(x, y);
	return graphicsItem;    
}
QGraphicsItem* QWindow::newQGraphicsPathItem(QPen pen, QBrush brush, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y)
{
	QGraphicsItem* graphicsItem=newQGraphicsItem("Path", pen, brush, blurRadius, dx, dy, x, y, 0, 0);
	QPainterPath path;
	path.addRect(10, 10, 30, 30);
	path.moveTo(0, 0);
	path.cubicTo(50, 0,  20, 20,  50, 50);
	path.cubicTo(0, 50,  20, 20,  0, 0);
	((QGraphicsPathItem*)graphicsItem)->setPath(path);
	return graphicsItem;
}
QGraphicsItem* QWindow::newQGraphicsPolygonItem(QPen pen, QBrush brush, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y)
{
	QGraphicsItem* graphicsItem=newQGraphicsItem("Polygon", pen, brush, blurRadius, dx, dy, x, y, 0, 0);
	QVector<QPointF> points;
	points<<QPointF(0, 20);
	points<<QPointF(25, 0);
	points<<QPointF(50, 20);
	points<<QPointF(50, 50);
	points<<QPointF(0, 50);
	((QGraphicsPolygonItem*)graphicsItem)->setPolygon(QPolygonF(points));
	return graphicsItem;
}
QGraphicsItem* QWindow::newQGraphicsTextItem(QString text, QPen pen, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y)
{
	QGraphicsItem* graphicsItem=newQGraphicsItem("Text", pen, QBrush(), blurRadius, dx, dy, x, y, 0, 0);
	QFont font;
	font.setBold(true);
	font.setPixelSize(pen.width());
	font.setFamily("Times New Roman");
	((QGraphicsTextItem*)graphicsItem)->setFont(font);
	((QGraphicsTextItem*)graphicsItem)->setPlainText(text);
	((QGraphicsTextItem*)graphicsItem)->setDefaultTextColor(pen.color());
	return graphicsItem;
}
QGraphicsItem* QWindow::newQGraphicsPixmapItem(QString fileName, const char* format, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y)
{
	QGraphicsItem* graphicsItem=newQGraphicsItem("Pixmap", QPen(), QBrush(), blurRadius, dx, dy, x, y, 0, 0);
	QPixmap pixmap(fileName, format, Qt::AutoColor);
	((QGraphicsPixmapItem*)graphicsItem)->setPixmap(pixmap);
	return graphicsItem;
}
