#include "knob.h"

 #include <QBrush>
 #include <QDebug>
 #include <QTouchEvent>

 Knob::Knob()
     : QGraphicsEllipseItem(-50, -50, 100, 100)
 {
     setAcceptTouchEvents(true);
     setBrush(Qt::lightGray);

     QGraphicsEllipseItem *leftItem = new QGraphicsEllipseItem(0, 0, 20, 20, this);
     leftItem->setPos(-40, -10);
     leftItem->setBrush(Qt::darkGreen);

     QGraphicsEllipseItem *rightItem = new QGraphicsEllipseItem(0, 0, 20, 20, this);
     rightItem->setPos(20, -10);
     rightItem->setBrush(Qt::darkRed);
 }

 bool Knob::sceneEvent(QEvent *event)
 {
     switch (event->type()) {
     case QEvent::TouchBegin:
     case QEvent::TouchUpdate:
     case QEvent::TouchEnd:
     {
         QTouchEvent *touchEvent = static_cast<QTouchEvent *>(event);
	foreach(QTouchEvent::TouchPoint touchPoint, touchEvent->touchPoints())
	{
		qDebug()<<touchPoint.scenePos();
	}
         if (touchEvent->touchPoints().count() == 2) {
             const QTouchEvent::TouchPoint &touchPoint1 = touchEvent->touchPoints().first();
             const QTouchEvent::TouchPoint &touchPoint2 = touchEvent->touchPoints().last();

             QLineF line1(touchPoint1.lastScenePos(), touchPoint2.lastScenePos());
             QLineF line2(touchPoint1.scenePos(), touchPoint2.scenePos());

            // rotate(line2.angleTo(line1));
         }

         break;
     }

     default:
         return QGraphicsItem::sceneEvent(event);
     }

     return true;
 }
